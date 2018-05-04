package com.example.HomeworkOne;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.String;

import MyInterface.InitView;
import es.dmoral.toasty.Toasty;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.HomeworkOne.globalConfig.MyApplication;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.CheckBox;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;


/**
 * Created by kafca on 17-9-28.
 */

public class AcRegister extends Activity implements InitView{
    @Bind(R.id.edit_email)
    EditText email;
    @Bind(R.id.edit_phone)
    EditText phone;
    @Bind(R.id.edit_username)
    EditText username;
    @Bind(R.id.edit_password)
    EditText password;
    @Bind(R.id.btn_register)
    ButtonRectangle registerButton;
    @Bind(R.id.cbx_male)
    CheckBox male;
    @Bind(R.id.cbx_female)
    CheckBox female;
    @Bind(R.id.tv_has_account)
    TextView has_account;
    @Bind(R.id.ivToolbarNavigation)
    ImageView goback;
    private String emailStr;
    private String phoneStr;
    private String usernameStr;
    private String passwordStr;
    private String sex="F";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.ac_register);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        Toasty.info(this, "�ֻ��ź���������Ҫ��дһ��Ŷ",
                Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void initListener() {

        //���ص�¼ҳ��
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //ѡ�в�ѡŮ��ѡŮ��ѡ��
        male.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(CheckBox checkBox, boolean b) {
                if(b){
                    female.setChecked(false);
                }
            }
        });
        female.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(CheckBox checkBox, boolean b) {
                if(b){
                    male.setChecked(false);
                }
            }
        });

        //ע��
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int email_length = email.getText().length();
                int phone_length = phone.getText().length();
                int username_length = username.getText().length();
                int password_length = password.getText().length();
                if (email_length == 0 && phone_length == 0){
                    Toasty.warning(AcRegister.this, "�ֻ��ź���������Ҫ��дһ��Ŷ",
                            Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (username_length * password_length == 0){
                    Toasty.warning(AcRegister.this, "��������ȷ��ע����Ϣ!",
                            Toast.LENGTH_SHORT, true).show();
                    return;
                }
                //��ȡע����Ϣ
                if (email_length > 0){
                    emailStr = email.getText().toString();
                }
                if (phone_length > 0){
                    phoneStr = phone.getText().toString();
                }
                usernameStr = username.getText().toString();
                passwordStr = password.getText().toString();
                if(male.isCheck()){
                    sex = "M";
                }
                JSONObject param = new JSONObject();
                try {
                    if (email_length > 0){
                        param.put("email", emailStr);
                    }
                    if (phone_length > 0){
                        param.put("phone", phoneStr);
                    }
                    param.put("username", usernameStr);
                    param.put("sex", sex);
                    param.put("password", passwordStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyApplication myApplication = (MyApplication) getApplication();
                OkGo.<String>post(myApplication.getHost() + "/android_account/create/")
                        .upJson(param)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                                if(response.code() == 201){
                                    Toasty.success(AcRegister.this,
                                            "ע��ɹ�!", Toast.LENGTH_SHORT, true).show();

                                    //ת����½
                                    Intent intent = new Intent(AcRegister.this, AcLogin.class);
                                    intent.putExtra("email", emailStr);
                                    startActivity(intent);
                                }
                                else {
                                    Toasty.warning(AcRegister.this, "�˺��Ѵ���",
                                            Toast.LENGTH_SHORT, true).show();
                                }
                            }

                            @Override
                            public void onError(com.lzy.okgo.model.Response<String> response) {
                                Toasty.error(AcRegister.this, "�������").show();
                            }
                        });
            }
        });

        //�����˺�,ת����½
        has_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcRegister.this, AcLogin.class);
                startActivity(intent);
            }
        });
    }
}
