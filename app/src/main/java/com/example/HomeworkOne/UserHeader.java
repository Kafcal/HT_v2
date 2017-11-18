package com.example.HomeworkOne;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bm.library.PhotoView;
import MyInterface.InitView;
import Utils.PopupWindowUtils;
import com.google.gson.Gson;
import com.lqr.imagepicker.ui.ImageGridActivity;
import com.lqr.imagepicker.*;
import com.lqr.imagepicker.view.CropImageView;
import com.lqr.optionitemview.OptionItemView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.io.IOException;
import java.util.ArrayList;
import Utils.PicassoImageLoader;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import Utils.OssSecretBean;

import static com.example.HomeworkOne.LoginFragment.JSON;

/**
 * Created by mac on 2017/11/3.
 */

public class UserHeader extends Activity implements InitView{
    @Bind(R.id.pv)
    PhotoView header;
    @Bind(R.id.ivToolbarNavigation)
    ImageView goback;
    @Bind(R.id.ibToolbarMore)
    ImageButton more;

    public static Context context;
    private PopupWindow mPopupWindow;
    public static final int REQUEST_IMAGE_PICKER = 1000;
    private ImagePicker imagePicker;
    private View menu;
    private String signature;
    private OSS oss;
    private OptionItemView album;
    private OptionItemView camera;
    private OptionItemView cancel;
    private RequestBody requestBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_image);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        context = getApplicationContext();
        LayoutInflater factory = LayoutInflater.from(this);
        menu = factory.inflate(R.layout.popup_header, null);
        album = (OptionItemView) menu.findViewById(R.id.choose_from_album);
        camera = (OptionItemView) menu.findViewById(R.id.choose_from_camera);
        cancel = (OptionItemView) menu.findViewById(R.id.header_cancel);
        ButterKnife.bind(this);
        more.setVisibility(View.VISIBLE);
        initHeader();

        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //����ͼƬ������
        imagePicker.setShowCamera(true);  //��ʾ���հ�ť
        imagePicker.setCrop(true);        //����ü�����ѡ����Ч��
        imagePicker.setSaveRectangle(true); //�Ƿ񰴾������򱣴�
        imagePicker.setSelectLimit(1);    //ѡ����������
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //�ü������״
        imagePicker.setFocusWidth(800);   //�ü���Ŀ�ȡ���λ���أ�Բ���Զ�ȡ�����Сֵ��
        imagePicker.setFocusHeight(800);  //�ü���ĸ߶ȡ���λ���أ�Բ���Զ�ȡ�����Сֵ��
        imagePicker.setOutPutX(1000);//�����ļ��Ŀ�ȡ���λ����
        imagePicker.setOutPutY(1000);//�����ļ��ĸ߶ȡ���λ����
    }

    @Override
    public void initListener() {
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserHeader.this.finish();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initOss();
                showPopupMenu();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHeader.this, ImageGridActivity.class);
                startActivityForResult(intent,REQUEST_IMAGE_PICKER );
                mPopupWindow.dismiss();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHeader.this, ImageGridActivity.class);
                startActivityForResult(intent,REQUEST_IMAGE_PICKER );
                mPopupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }

    private void showPopupMenu() {
        mPopupWindow = PopupWindowUtils.getPopupWindowAtLocation(
               menu, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, getWindow().getDecorView().getRootView(), Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopupWindowUtils.makeWindowLight(UserHeader.this);
            }
        });
        PopupWindowUtils.makeWindowDark(UserHeader.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_PICKER:
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                    if (data != null) {
                        ArrayList<com.lqr.imagepicker.bean.ImageItem> images = (ArrayList<com.lqr.imagepicker.bean.ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        if (images != null && images.size() > 0) {
                            com.lqr.imagepicker.bean.ImageItem imageItem = images.get(0);
                            putToOss(imageItem.name,imageItem.path);
                            String url = "http://ht-data.oss-cn-shenzhen.aliyuncs.com/"
                                    +imageItem.name;
                                    //+"?x-oss-process=image/resize,m_fixed,h_50,w_50";
                            setUserHeader(url);
                        }
                    }
                }
        }
    }

    private void initOss(){
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        OSSCustomSignerCredentialProvider credentialProvider = new OSSCustomSignerCredentialProvider() {
            @Override
            public String signContent(String content) {
                // ����Ҫ����������OSS�涨��ǩ���㷨��ʵ�ּ�ǩһ���ַ����ݣ����ѵõ���ǩ����ƴ����AccessKeyId�󷵻�
                // һ��ʵ���ǣ����ַ�����post������ҵ���������Ȼ�󷵻�ǩ��
                // �����Ϊĳ��ԭ���ǩʧ�ܣ�����error��Ϣ�󣬷���nil
                // �������ñ����㷨���е���ʾ
                //return "OSS " + AccessKeyId + ":" + base64(hmac-sha1(AccessKeySecret, content));
                SharedPreferences sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
                String sessionid = sharedPreferences.getString("sessionid","null");
                String url = "http://120.78.67.135:8000/oss/android_signature/";
                OkHttpClient okHttpClient = new OkHttpClient();
                try {
                    JSONObject param = new JSONObject();
                    param.put("content", content);
                    requestBody = RequestBody.create(JSON, param.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                Request request = new Request.Builder().url(url).addHeader("cookie",sessionid)
                        .post(requestBody).build();
                Call call = okHttpClient.newCall(request);
                try{
                    Response response = call.execute();
                    Gson gson = new Gson();
                    OssSecretBean ossSecretBean = gson.fromJson(response.body().string(),
                            OssSecretBean.class);
                    signature = ossSecretBean.getAuthorization();
                    //Log.e("signature",signature);
                    return signature;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return "null";
            }
        };
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
    }

    private void putToOss(String objectKey, String path){
        PutObjectRequest put = new PutObjectRequest("ht-data", objectKey, path);
        // �ļ�Ԫ��Ϣ�������ǿ�ѡ��
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setContentType("application/octet-stream"); // ����content-type
        // metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // У��MD5
        // put.setMetadata(metadata);
        try {
            PutObjectResult putResult = oss.putObject(put);
            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // �����쳣�������쳣��
            e.printStackTrace();
        } catch (ServiceException e) {
            // �����쳣
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
    }

    private void setUserHeader(final String url){
        //�ϴ��û�ͷ��url
        final SharedPreferences sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id",0);
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject param = new JSONObject();
        try {
            param.put("header", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(JSON, param.toString());
        Request request = new Request.Builder()
                .url("http://120.78.67.135:8000/android_account/header/"+user_id+"/")
                .addHeader("cookie", MainActivity.sessionid)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UserHeader.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserHeader.this,"���������ƺ�����С��...",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                UserHeader.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("header",url);
                        editor.apply();
                        Toast.makeText(UserHeader.this,"���óɹ���",Toast.LENGTH_SHORT).show();
                        initHeader();
                    }
                });
            }
        });
    }

    private void initHeader(){
        //�û�ͷ��
        SharedPreferences sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
        String header_str = sharedPreferences.getString("header","null");
        Uri header_uri = Uri.parse(header_str);
        Picasso.with(UserHeader.this).load(header_uri).into(header);
        header.enable();
    }

}
