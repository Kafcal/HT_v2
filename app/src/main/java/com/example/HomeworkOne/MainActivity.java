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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity implements OnClickListener{
	//�ײ���4�������ؼ�
	private LinearLayout mTabTest;
	private LinearLayout mTabDiscover;
	private LinearLayout mTabAccount;
	//�ײ�4�������ؼ��е�ͼƬ��ť
	private ImageButton mImgTest;
	private ImageButton mImgDiscover;
	private ImageButton mImgAccount;
	//��ʼ��4��Fragment
	private Fragment tab01;
	private Fragment tab02;
	private Fragment tab03;
	public static Fragment tab_transfer;
	public static OkHttpClient okHttpClient;
	public static String sessionid;

	@Bind(R.id.ivToolbarNavigation)
	ImageView goback;
	@Bind(R.id.id_text_test)
	TextView test_tv;
	@Bind(R.id.id_text_discover)
	TextView discover_tv;
	@Bind(R.id.id_text_account)
	TextView account_tv;

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
							resetImgAndText();
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
		mTabDiscover.setOnClickListener(this);
		mTabAccount.setOnClickListener(this);
	}

	private void initView() {
		ButterKnife.bind(this);
		goback.setVisibility(View.GONE);
		mTabTest = (LinearLayout)findViewById(R.id.id_tab_test);
		mTabDiscover = (LinearLayout)findViewById(R.id.id_tab_discover);
		mTabAccount = (LinearLayout)findViewById(R.id.id_tab_account);
		mImgTest = (ImageButton)findViewById(R.id.id_tab_test_img);
		mImgDiscover = (ImageButton)findViewById(R.id.id_tab_discover_img);
		mImgAccount = (ImageButton)findViewById(R.id.id_tab_account_img);
	}

	@Override
	public void onClick(View v) {
		resetImgAndText();
		switch (v.getId()) {
		case R.id.id_tab_test://��������԰�ťʱ���л�ͼƬΪ��ɫ���л�fragmentΪ΢���������
			setSelect(0);
			break;
		case R.id.id_tab_discover:
			setSelect(1);
			break;
		case R.id.id_tab_account:
			setSelect(2);
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
			mImgTest.setImageResource(R.drawable.test_pressed);
			test_tv.setTextColor(getResources().getColor(R.color.blue));
			break;
		case 1:
			MyWebFragment.myurl="http://www.baidu.com";
			tab02 = new MyWebFragment();
			transaction.add(R.id.id_content, tab02);
			transaction.show(tab02);
			mImgDiscover.setImageResource(R.drawable.discover_pressed);
			discover_tv.setTextColor(getResources().getColor(R.color.blue));
			break;
		case 2:
			if (!sessionid.equals("null")) {
				tab03 = new AccountFragment();
			}
			else {
				tab03 = new LoginFragment();
			}
			transaction.add(R.id.id_content,tab03);
			mImgAccount.setImageResource(R.drawable.account_pressed);
			account_tv.setTextColor(getResources().getColor(R.color.blue));
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
		if (tab_transfer != null) {
			transaction.hide(tab_transfer);
		}
	}

	private void resetImgAndText() {
		mImgTest.setImageResource(R.drawable.test_normal);
		mImgDiscover.setImageResource(R.drawable.discover_normal);
		mImgAccount.setImageResource(R.drawable.account_normal);
		test_tv.setTextColor(getResources().getColor(R.color.gray0));
		discover_tv.setTextColor(getResources().getColor(R.color.gray0));
		account_tv.setTextColor(getResources().getColor(R.color.gray0));
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