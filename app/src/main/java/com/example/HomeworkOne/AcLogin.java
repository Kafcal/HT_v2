package com.example.HomeworkOne;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import MyInterface.InitView;
import Utils.JsonUserBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.*;


/**
 * Created by kafca on 17-9-28.
 */

public class AcLogin extends Activity implements InitView{
    static AcLogin instance;
    private String emailStr;
    private String passwordStr;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Bind(R.id.edit_email)
    EditText email;
    @Bind(R.id.edit_password)
    EditText password;
    @Bind(R.id.tv_no_account)
    TextView no_account;
    @Bind(R.id.btn_login)
    ButtonRectangle LoginButton;
    @Bind(R.id.ivToolbarNavigation)
    ImageView goback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.ac_login);
        initView();
        initListener();
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        goback.setVisibility(View.GONE);
        instance = this;

        //�رջ�ӭActivity
        Access.instance.finish();
    }

    @Override
    public void initListener() {

        //û���˺ţ�ת��ע��
        no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcLogin.this, AcRegister.class);
                startActivity(intent);
            }
        });

        //��¼
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //��ȡ��¼��
                try{
                    emailStr = email.getText().toString();
                    passwordStr = password.getText().toString();
                }catch (Exception e){
                    final SnackBar snackbar = new SnackBar(AcLogin.this,
                            "��������ȷ�ĵ�¼��Ϣ...", null, null);
                    snackbar.show();
                }

                //��¼����
                OkHttpClient okHttpClient = new OkHttpClient();
                JSONObject param = new JSONObject();
                try {
                    param.put("email", emailStr);
                    param.put("password", passwordStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(JSON, param.toString());
                Request request = new Request.Builder()
                        .url("http://120.78.67.135:8000/android_account/login")
                        .post(requestBody)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        AcLogin.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        AcLogin.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //��½�ɹ�
                                if(response.code() == 200){

                                    //��ȡsessionid
                                    List<String> cookies = response.headers().values("Set-Cookie");
                                    String session = cookies.get(0);
                                    String sessionid = session.substring(0, session.indexOf(";"));

                                    //ÿ�ε�¼��Ҫ����sessionid
                                    MainActivity.sessionid=sessionid;

                                    //�����û����ݲ�����
                                    Gson gson = new Gson();
                                    try {
                                        JsonUserBean jsonUserBean = gson.fromJson(response.body().string(),
                                                JsonUserBean.class);
                                        int user_id = jsonUserBean.get_id();
                                        String email = jsonUserBean.get_email();
                                        String username = jsonUserBean.get_username();
                                        String sex = jsonUserBean.get_sex();
                                        String header = jsonUserBean.get_header();
                                        sex = (sex.equals("M")) ? "��":"Ů";
                                        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
                                        SharedPreferences.Editor edit = share.edit();
                                        edit.putString("sessionid", sessionid);
                                        edit.putInt("user_id",user_id);
                                        edit.putString("email", email);
                                        edit.putString("username", username);
                                        edit.putString("sex", sex);
                                        edit.putString("header",header);
                                        edit.apply();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    //��½�ɹ�����ת��ҳ��
                                    Intent intent = new Intent(AcLogin.this, MainActivity.class);
                                    startActivity(intent);
                                }

                                //��¼������
                                else if (response.code() == 400){
                                    final SnackBar snackbar = new SnackBar(AcLogin.this,
                                            "�������!", null, null);
                                    snackbar.show();
                                }
                                else if (response.code() == 500){
                                    final SnackBar snackbar = new SnackBar(AcLogin.this,
                                            "�˺Ų�����!", null, null);
                                    snackbar.show();
                                }
                                else {
                                    final SnackBar snackbar = new SnackBar(AcLogin.this,
                                            "�������!", null, null);
                                    snackbar.show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
