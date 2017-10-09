package com.example.HomeworkOne;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;


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
	public static Fragment tab04;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();//��ʼ�����е�view
		initEvents();
		setSelect(0);//Ĭ����ʾ���Խ���
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
				/*
				 * ��Fragment��ӵ���У�public abstract FragmentTransaction add (int containerViewId, Fragment fragment)
				*containerViewId��ΪOptional identifier of the container this fragment is to be placed in. If 0, it will not be placed in a container.
				 * */
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
			if (tab04 == null) {
				Runnable networkTask = new Runnable() {
					@Override
					public void run() {
						try {
							OkHttpClient okHttpClient = new OkHttpClient();
							RequestBody requestBody = RequestBody.create();
							Request request = new Request.Builder()
									.url("http://120.78.67.135:8000/androidaccount/register")
									.post(requestBody)
									.build();
							Response response=okHttpClient.newCall(request).execute();
							if(response.isSuccessful()){
								//��ӡ����˷��ؽ��
								register_flag=true;
							}
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				};
				tab04 = new LoginFragment();
				transaction.add(R.id.id_content, tab04);
			}else {
				transaction.show(tab04);
			}
			mImgAccount.setImageResource(R.drawable.tab_settings_pressed);
			break;

		default:
			break;
		}
		transaction.commit();//�ύ����
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
		
	}

	private void resetImg() {
		mImgTest.setImageResource(R.drawable.tab_weixin_normal);
		mImgRecord.setImageResource(R.drawable.tab_find_frd_normal);
		mImgHelp.setImageResource(R.drawable.tab_address_normal);
		mImgAccount.setImageResource(R.drawable.tab_settings_normal);
	}
}
