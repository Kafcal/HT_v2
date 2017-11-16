package com.example.HomeworkOne;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;


public class MainActivity extends FragmentActivity implements OnClickListener{
	//�ײ���4�������ؼ�
	private LinearLayout mTabTest;
	private LinearLayout mTabRecord;
	private LinearLayout mTabHelp;
	private LinearLayout mTabAccount;
	//�ײ�4�������ؼ��е�ͼƬ��ť
	private ImageButton mImgTest;
	private ImageButton mImgRecord;
	private ImageButton mImgHelp;
	private ImageButton mImgAccount;
	//��ʼ��4��Fragment
	private Fragment tab01;
	private Fragment tab02;
	private Fragment tab03;
	private Fragment tab04;
	public static Fragment tab_transfer;
	public static OkHttpClient okHttpClient;
	public static String sessionid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();//��ʼ�����е�view
		initEvents();
		setSelect(0);//Ĭ����ʾ���Խ���
		createHttpClient();
		SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
		sessionid = share.getString("sessionid","null");
		if(sessionid.equals("null")){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true);
			builder.setTitle("��¼").setMessage("��½��������ƶ˱������Ĳ��Լ�¼Ŷ��������¼��")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface Arg, int arg) {
							// TODO Auto-generated method stub
							resetImg();
							setSelect(3);
						}
					}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				}
			});
			builder.create().show();
		}
	}

	private void initEvents() {
		mTabTest.setOnClickListener(this);
		mTabRecord.setOnClickListener(this);
		mTabHelp.setOnClickListener(this);
		mTabAccount.setOnClickListener(this);
	}

	private void initView() {
		mTabTest = (LinearLayout)findViewById(R.id.id_tab_test);
		mTabRecord = (LinearLayout)findViewById(R.id.id_tab_record);
		mTabHelp = (LinearLayout)findViewById(R.id.id_tab_help);
		mTabAccount = (LinearLayout)findViewById(R.id.id_tab_account);
		mImgTest = (ImageButton)findViewById(R.id.id_tab_test_img);
		mImgRecord = (ImageButton)findViewById(R.id.id_tab_record_img);
		mImgHelp = (ImageButton)findViewById(R.id.id_tab_help_img);
		mImgAccount = (ImageButton)findViewById(R.id.id_tab_account_img);
		
	}



	@Override
	public void onClick(View v) {
		resetImg();
		switch (v.getId()) {
		case R.id.id_tab_test://��������԰�ťʱ���л�ͼƬΪ��ɫ���л�fragmentΪ΢���������
			setSelect(0);
			break;
		case R.id.id_tab_record:
			setSelect(1);
			break;
		case R.id.id_tab_help:
			setSelect(2);
			break;
		case R.id.id_tab_account:
			setSelect(3);
			break;

		default:
			break;
		}
		
	}

	/*
	 * ��ͼƬ����Ϊ��ɫ�ģ��л���ʾ���ݵ�fragment
	 * */
	private void setSelect(int i) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();//����һ������
		hideFragment(transaction);//�����Ȱ����е�Fragment�����ˣ�Ȼ�������ٿ�ʼ�������Ҫ��ʾ��Fragment
		switch (i) {
		case 0:
			if (tab01 == null) {
				tab01 = new TestFragment();
				transaction.add(R.id.id_content, tab01);
			}else {
				transaction.show(tab01);
			}
			mImgTest.setImageResource(R.drawable.tab_weixin_pressed);
			break;
		case 1:
			tab02 = new RecordFragment();
			transaction.add(R.id.id_content, tab02);
			transaction.show(tab02);
			mImgRecord.setImageResource(R.drawable.tab_find_frd_pressed);
			break;
		case 2:
			if (tab03 == null) {
				MyWebFragment.myurl="http://kafca.legendh5.com/h5/hthelp.html";
				tab03 = new MyWebFragment();
				transaction.add(R.id.id_content, tab03);
			}else {
				transaction.show(tab03);
			}
			mImgHelp.setImageResource(R.drawable.tab_address_pressed);
			break;
		case 3:
			if (!sessionid.equals("null")) {
				tab04 = new AccountFragment();
			}
			else {
				tab04 = new LoginFragment();
			}
			transaction.add(R.id.id_content,tab04);
			mImgAccount.setImageResource(R.drawable.tab_settings_pressed);
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/*
	 * �������е�Fragment
	 * */
	private void hideFragment(FragmentTransaction transaction) {
		if (tab01 != null) {
			transaction.hide(tab01);
		}
		if (tab02 != null) {
			transaction.hide(tab02);
		}
		if (tab03 != null) {
			transaction.hide(tab03);
		}
		if (tab04 != null) {
			transaction.hide(tab04);
		}
		if (tab_transfer != null) {
			transaction.hide(tab_transfer);
		}
	}

	private void resetImg() {
		mImgTest.setImageResource(R.drawable.tab_weixin_normal);
		mImgRecord.setImageResource(R.drawable.tab_find_frd_normal);
		mImgHelp.setImageResource(R.drawable.tab_address_normal);
		mImgAccount.setImageResource(R.drawable.tab_settings_normal);
	}



		 void  createHttpClient() {
			okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
				private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();
				@Override
				public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
					cookieStore.put(url.host(), cookies);
				}

				@Override
				public List<Cookie> loadForRequest(HttpUrl url) {
					List<Cookie> cookies = cookieStore.get(url.host());
					return cookies != null ? cookies : new ArrayList<Cookie>();
				}
			}).connectTimeout(5, TimeUnit.SECONDS)
					.readTimeout(10, TimeUnit.SECONDS)
					.build();
		}
}


