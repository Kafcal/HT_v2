package com.example.HomeworkOne;

import android.content.Intent;
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

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.HomeworkOne.BaseActivity.BaseActivity;
import com.example.HomeworkOne.globalConfig.MyApplication;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.CheckBox;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import static Utils.Md5.md5;


/**
 * Created by kafca on 17-9-28.
 */

public class AcRegister extends BaseActivity{
    @Bind(R.id.edit_phone)
    EditText phone;
    @Bind(R.id.edit_username)
    EditText username;
    @Bind(R.id.edit_code)
    EditText code;
    @Bind(R.id.edit_password)
    EditText password;
    @Bind(R.id.verify_code)
    ButtonRectangle verifyBtn;
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
    private String codeStr;
    private String phoneStr;
    private String usernameStr;
    private String passwordStr;
    private String sex="F";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_register);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
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


        // verify-code
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int phone_length = phone.getText().length();
                if (phone_length == 11){
                    phoneStr = phone.getText().toString();
                    sendCode("86", phoneStr);
                }
                else
                    showWarningToast("��������ȷ���ֻ�����");
            }
        });

        //ע��
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int phone_length = phone.getText().length();
                int username_length = username.getText().length();
                int password_length = password.getText().length();
                int code_length = code.getText().length();
                if (username_length * password_length * code_length== 0 || phone_length != 11){
                    Toasty.warning(AcRegister.this, "��������ȷ��ע����Ϣ!",
                            Toast.LENGTH_SHORT, true).show();
                    return;
                }
                //��ȡע����Ϣ
                phoneStr = phone.getText().toString();
                codeStr = code.getText().toString();
                usernameStr = username.getText().toString();
                passwordStr = md5(password.getText().toString());
                if(male.isCheck()){
                    sex = "M";
                }
                JSONObject param = new JSONObject();
                try {
                    param.put("phone", phoneStr);
                    param.put("username", usernameStr);
                    param.put("sex", sex);
                    param.put("password", passwordStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                submitCode("86", phoneStr, codeStr, param);
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

    public void sendCode(String country, String phone) {
        // ע��һ���¼��ص������ڴ�������֤������Ľ��
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showSuccessToast("��֤�뷢�ͳɹ�");
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWarningToast("��֤�뷢��ʧ�ܣ����Ժ�����");
                        }
                    });
                }
                SMSSDK.unregisterAllEventHandler();
            }
        });
        // ��������
        SMSSDK.getVerificationCode(country, phone);
    }

    // �ύ��֤�룬���е�code��ʾ��֤�룬�硰1357��
    public void submitCode(String country, String phone, String code, final JSONObject param) {
        // ע��һ���¼��ص������ڴ����ύ��֤������Ľ��
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
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
                } else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showErrorToast("��֤�����");
                        }
                    });
                }
                SMSSDK.unregisterAllEventHandler();
            }
        });
        // ��������
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //����ص�Ҫע������������ܻ�����ڴ�й¶
        SMSSDK.unregisterAllEventHandler();
    }
}
