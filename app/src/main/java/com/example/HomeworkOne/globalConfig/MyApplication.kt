package com.example.HomeworkOne.globalConfig

import Utils.OssSecretBean
import Utils.PicassoImageLoader
import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.google.gson.Gson
import com.lqr.imagepicker.ImagePicker
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.SPCookieStore
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.model.HttpHeaders
import okhttp3.Response
import org.json.JSONObject


/**
 * Author: kafca
 * Date: 2017/12/6
 * Description: ȫ������
 */

class MyApplication : Application() {
    lateinit var host: String
    lateinit var share: SharedPreferences
    private lateinit var oss: OSS
    override fun onCreate() {
        host = "https://api.mochuxian.top"
        share = getSharedPreferences("Session", MODE_PRIVATE)
        initOkGo()
        intiOss()
        initImagePicker()
        super.onCreate()
    }

    private fun initOkGo() {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor("OkGo")
        //log��ӡ���𣬾�����log��ʾ����ϸ�̶�
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
        //log��ɫ���𣬾�����log�ڿ���̨��ʾ����ɫ
        loggingInterceptor.setColorLevel(Level.INFO)
        //ȫ�ֵĶ�ȡ��ʱʱ��
        builder.readTimeout(10000, TimeUnit.MILLISECONDS)
        //ȫ�ֵ�д�볬ʱʱ��
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS)
        //ȫ�ֵ����ӳ�ʱʱ��
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS)
        builder.addInterceptor(loggingInterceptor)
        builder.cookieJar(CookieJarImpl(SPCookieStore(this)))
        val sslParams1 = HttpsUtils.getSslSocketFactory()
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager)
        OkGo.getInstance().init(this)                       //������ó�ʼ��
                .setOkHttpClient(builder.build())               //��������OkHttpClient�������ý�ʹ��Ĭ�ϵ�
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE).retryCount = 3
    }

    fun header(): HttpHeaders {
        val token = share.getString("token", "null")
        val headers = HttpHeaders()
        headers.put("Authorization", "Token $token")
        return headers
    }

    private fun intiOss() {
        val endpoint = "http://oss-cn-shenzhen.aliyuncs.com"
        val credentialProvider = object : OSSCustomSignerCredentialProvider() {
            override fun signContent(content: String): String {
                // ����Ҫ����������OSS�涨��ǩ���㷨��ʵ�ּ�ǩһ���ַ����ݣ����ѵõ���ǩ����ƴ����AccessKeyId�󷵻�
                // һ��ʵ���ǣ����ַ�����post������ҵ���������Ȼ�󷵻�ǩ��
                // �����Ϊĳ��ԭ���ǩʧ�ܣ�����error��Ϣ�󣬷���nil
                // �������ñ����㷨���е���ʾ
                //return "OSS " + AccessKeyId + ":" + base64(hmac-sha1(AccessKeySecret, content));
                val url = "$host/oss/signature/"
                val param = JSONObject()
                param.put("content", content)
                var signature = ""
                val response: Response  = OkGo.post<String>(url).headers(header()).upJson(param).execute()
                if (response.code() == 200) {
                    val gson = Gson()
                    val ossSecretBean = gson.fromJson(response.body()!!.string()!!,
                            OssSecretBean::class.java)
                    signature = ossSecretBean.authorization
                }
                return signature
            }
        }
        oss = OSSClient(applicationContext, endpoint, credentialProvider)
    }

    fun putToOss(objectKey: String, path: String) {
        val put = PutObjectRequest("ht-data", objectKey, path)
        // �ļ�Ԫ��Ϣ�������ǿ�ѡ��
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setContentType("application/octet-stream"); // ����content-type
        // metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // У��MD5
        // put.setMetadata(metadata);
        try {
            val putResult = oss.putObject(put)
            Log.d("PutObject", "UploadSuccess")
            Log.d("ETag", putResult.eTag)
            Log.d("RequestId", putResult.requestId)
        } catch (e: ClientException) {
            // �����쳣�������쳣��
            e.printStackTrace()
        } catch (e: ServiceException) {
            // �����쳣
            Log.e("RequestId", e.requestId)
            Log.e("ErrorCode", e.errorCode)
            Log.e("HostId", e.hostId)
            Log.e("RawMessage", e.rawMessage)
        }
    }

    private fun initImagePicker() {
        val imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = PicassoImageLoader()   //����ͼƬ������
        imagePicker.isMultiMode = true
        imagePicker.isShowCamera = true  //��ʾ���հ�ť
        imagePicker.selectLimit = 1    //ѡ����������
        imagePicker.isCrop = false        //����ü�����ѡ����Ч��
    }

}