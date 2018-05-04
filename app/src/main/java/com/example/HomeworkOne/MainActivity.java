package com.example.HomeworkOne;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

import com.tapadoo.alerter.Alerter;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity implements OnClickListener{
	//�ײ���4�������ؼ�
	private LinearLayout mTabTest;
	@Bind(R.id.id_tab_data)
	LinearLayout mTabData;
	private LinearLayout mTabDiscover;
	private LinearLayout mTabAccount;
	//�ײ�4�������ؼ��е�ͼƬ��ť
	private ImageButton mImgTest;
	@Bind(R.id.id_tab_data_img)
	ImageButton mImgData;
	private ImageButton mImgDiscover;
	private ImageButton mImgAccount;
	//��ʼ��4��Fragment
	private Fragment tab01;
	private Fragment tab02;
	private Fragment tab03;
	private Fragment tabData;
	public static Fragment tab_transfer;

	@Bind(R.id.ivToolbarNavigation)
	ImageView goback;
	@Bind(R.id.id_text_test)
	TextView test_tv;
	@Bind(R.id.id_text_data)
	TextView data_tv;
	@Bind(R.id.id_text_discover)
	TextView discover_tv;
	@Bind(R.id.id_text_account)
	TextView account_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initView();//��ʼ�����е�view
		initEvents();
		setSelect(0);//Ĭ����ʾ���Խ���
	}

	private void initEvents() {
		mTabTest.setOnClickListener(this);
		mTabData.setOnClickListener(this);
		mTabDiscover.setOnClickListener(this);
		mTabAccount.setOnClickListener(this);
	}

	private void initView() {
		whereToGo();
		ButterKnife.bind(this);
		goback.setVisibility(View.GONE);
		mTabTest = findViewById(R.id.id_tab_test);
		mTabDiscover = findViewById(R.id.id_tab_discover);
		mTabAccount = findViewById(R.id.id_tab_account);
		mImgTest = findViewById(R.id.id_tab_test_img);
		mImgDiscover = findViewById(R.id.id_tab_discover_img);
		mImgAccount = findViewById(R.id.id_tab_account_img);

		if (isLate()){
			Alerter.create(this)
					.setTitle("ע����Ϣ")
					.setText("ҹ�����ע����Ϣ...")
					.setBackgroundColorRes(R.color.Color_DarkBlue)
					.setIcon(R.mipmap.sleep)
					.show();
		}
	}

	@Override
	public void onClick(View v) {
		resetImgAndText();
		switch (v.getId()) {
			case R.id.id_tab_test://��������԰�ťʱ���л�ͼƬΪ��ɫ���л�fragmentΪ΢���������
				setSelect(0);
				break;
			case R.id.id_tab_data:
				setSelect(3);
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
					tab01 = new FmTest();
					transaction.add(R.id.id_content, tab01);
				}else {
					transaction.show(tab01);
				}
				mImgTest.setImageResource(R.drawable.test_pressed);
				test_tv.setTextColor(getResources().getColor(R.color.blue));
				break;
			case 1:
				if(tab02 == null){
					tab02 = new FmDiscoverNav();
					transaction.add(R.id.id_content,tab02);
				}
				else {
					transaction.show(tab02);
				}
				mImgDiscover.setImageResource(R.drawable.discover_pressed);
				discover_tv.setTextColor(getResources().getColor(R.color.blue));
				break;
			case 2:
				if (tab03 == null){
					tab03 = new AccountFragment();
					transaction.add(R.id.id_content,tab03);
				}
				else {
					transaction.show(tab03);
				}
				mImgAccount.setImageResource(R.drawable.account_pressed);
				account_tv.setTextColor(getResources().getColor(R.color.blue));
				break;
			case 3:
				if (tabData == null){
					tabData = new FmData();
					transaction.add(R.id.id_content,tabData);
				}
				else {
					transaction.show(tabData);
				}
				mImgData.setImageResource(R.mipmap.data_pressed);
				data_tv.setTextColor(getResources().getColor(R.color.blue));
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
		if (tabData != null) {
			transaction.hide(tabData);
		}
	}

	private void resetImgAndText() {
		mImgTest.setImageResource(R.drawable.test_normal);
		mImgData.setImageResource(R.mipmap.data_normal);
		mImgDiscover.setImageResource(R.drawable.discover_normal);
		mImgAccount.setImageResource(R.drawable.account_normal);
		test_tv.setTextColor(getResources().getColor(R.color.gray0));
		data_tv.setTextColor(getResources().getColor(R.color.gray0));
		discover_tv.setTextColor(getResources().getColor(R.color.gray0));
		account_tv.setTextColor(getResources().getColor(R.color.gray0));
	}

	boolean isLate(){
		long time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour >= 0 && hour <= 5 || hour >= 23;
	}

	/**
	 * �ж��Ƿ��Ѿ���¼
	 */
	private void whereToGo(){
		//���Ի�ȡtoken
		SharedPreferences sharedPreferences = getSharedPreferences("Session",MODE_PRIVATE);
		String token = sharedPreferences.getString("token","null");
		//���û�û�е�¼�������AcLogin
		if(token.equals("null")){
			Intent intent = new Intent(MainActivity.this, AcLogin.class);
			startActivity(intent);
		}
	}
}