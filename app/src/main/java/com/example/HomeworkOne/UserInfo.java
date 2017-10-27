package com.example.HomeworkOne;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import Utils.HttpRequest;
import Utils.JsonUserBean;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kafca on 17-9-30.
 */

public class UserInfo extends Fragment{
    private EditText email;
    private EditText username;
    private EditText sex;
    private Button logoutButton;

    private String email_str;
    private String username_str;
    private String sex_str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.userinfo, container, false);
        email= (EditText) view.findViewById(R.id.info_email);
        username= (EditText) view.findViewById(R.id.info_username);
        sex= (EditText) view.findViewById(R.id.info_sex);
        logoutButton=(Button) view.findViewById(R.id.LogoutButton);

        SharedPreferences share = getActivity().getSharedPreferences("Session", getActivity().MODE_PRIVATE);
        email_str = share.getString("email","null");
        username_str=share.getString("username","null");
        sex_str=share.getString("sex","null");

        email.setText(email_str);
        username.setText(username_str);
        if(sex_str.equals("M")){
            sex.setText("   ��");
        }
        else
            sex.setText("   Ů");

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Bundle data = msg.getData();
                        int status = data.getInt("status");
                        // TODO
                        // UI����ĸ��µ���ز���
                        if(status==200) {
                            SharedPreferences share = getActivity().getSharedPreferences("Session", MODE_PRIVATE);
                            SharedPreferences.Editor edit = share.edit();
                            edit.clear().commit();
                            MainActivity.sessionid="null"; //ÿ���˳���¼��sessionid��null
                            //SQLiteDatabase db = getActivity().openOrCreateDatabase("user.db",
                                    //getActivity().MODE_PRIVATE, null);
                            //db.execSQL("delete from records");
                            switchFragment(new LoginFragment());
                        }
                        else{
                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                            builder.setMessage("����ʱ!");
                            builder.setCancelable(true);
                            AlertDialog dialog=builder.create();
                            dialog.show();
                        }
                    }
                };


                Runnable networkTask = new Runnable() {
                    @Override
                    public void run() {
                        // TODO
                        // ��������� http request.����������ز���
                        try {
                            HttpRequest.get("http://120.78.67.135:8000/android_account/logout");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putInt("status", HttpRequest.response_code);
                        msg.setData(data);
                        handler.sendMessage(msg);
                    }
                };
                new Thread(networkTask).start();
            }
        });

        return view;
    }
    private void switchFragment(android.support.v4.app.Fragment targetFragment) {
        MainActivity.tab_transfer=targetFragment;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction
                    .hide(UserInfo.this)
                    .add(R.id.id_content, MainActivity.tab_transfer)
                    .commit();
        } else {
            transaction
                    .hide(UserInfo.this)
                    .show( MainActivity.tab_transfer)
                    .commit();
        }

    }
}
