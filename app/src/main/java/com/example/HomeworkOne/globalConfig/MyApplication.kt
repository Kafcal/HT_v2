package com.example.HomeworkOne.globalConfig

import android.app.Application
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheEntity
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.SPCookieStore
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import com.lzy.okgo.https.HttpsUtils



/**
 * Author: kafca
 * Date: 2017/12/6
 * Description: ȫ������
 */

class MyApplication : Application() {
    lateinit var host: String
    override fun onCreate() {
        host = "http://120.78.199.2:8000"
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor("OkGo")
        //log��ӡ���𣬾�����log��ʾ����ϸ�̶�
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
        //log��ɫ���𣬾�����log�ڿ���̨��ʾ����ɫ
        loggingInterceptor.setColorLevel(Level.INFO)
        //ȫ�ֵĶ�ȡ��ʱʱ��
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);
        //ȫ�ֵ�д�볬ʱʱ��
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);
        //ȫ�ֵ����ӳ�ʱʱ��
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        builder.addInterceptor(loggingInterceptor)
        builder.cookieJar(CookieJarImpl(SPCookieStore(this)));
        val sslParams1 = HttpsUtils.getSslSocketFactory()
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        OkGo.getInstance().init(this)                       //������ó�ʼ��
                .setOkHttpClient(builder.build())               //��������OkHttpClient�������ý�ʹ��Ĭ�ϵ�
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //ȫ��ͳһ����ʱ�䣬Ĭ���������ڣ����Բ���
                .setRetryCount(3)                               //ȫ��ͳһ��ʱ����������Ĭ��Ϊ���Σ���ô�������������4��(һ��ԭʼ����������������)������Ҫ��������Ϊ0
        super.onCreate()
    }
}