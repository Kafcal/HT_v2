package com.example.HomeworkOne;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.HomeworkOne.globalConfig.MyApplication;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;
import MyInterface.InitView;
import Utils.JsonRecordBean;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.thefinestartist.utils.content.ContextUtil.getSharedPreferences;

public class RecordActivity extends Activity implements InitView {
	@Bind(R.id.record_list)
	ListView listView;
	@Bind(R.id.ivToolbarNavigation)
	ImageView goback;
	@Bind(R.id.avi)
	AVLoadingIndicatorView avLoadingIndicatorView;

	private SimpleAdapter simpleAdapter;
	private List<Map<String, Object>> dataList;
	private int count_record;
	private int result_label;
	private String host;
	private ArrayList<JsonRecordBean.RecordBean> records;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.record_list);
		initView();
		initListener();
	}

	private void getData() {
		SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
		String session_id = share.getString("sessionid", "null");
		int user_id = share.getInt("user_id", 0);
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url(host+"/android_health_test/record/user/"
				+ user_id + "/")
				.addHeader("cookie", session_id)
				.build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				RecordActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(RecordActivity.this,
								"���������ƺ�����С��...", Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onResponse(Call call, final Response response) throws IOException {
				Gson gson = new Gson();
				try {
					JsonRecordBean jsonRecordBean = gson.fromJson(response.body().string(),
							JsonRecordBean.class);
					count_record = jsonRecordBean.get_count_record();
					records = jsonRecordBean.get_records();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(response.code() != 200){
					RecordActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toasty.warning(RecordActivity.this,"��¼��Ϣ���ڣ������µ�¼!",
									Toast.LENGTH_SHORT).show();
							SharedPreferences sharedPreferences =
									getSharedPreferences("Session",MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPreferences.edit();
							editor.clear().apply();
							Intent intent = new Intent(RecordActivity.this, AcLogin.class);
							startActivity(intent);
						}
					});
				}
				else if (count_record == 0) {
					RecordActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toasty.warning(RecordActivity.this, "��û���κμ�¼Ŷ~",
									Toast.LENGTH_SHORT, true).show();
							avLoadingIndicatorView.setVisibility(View.GONE);
						}
					});
				} else {
					for (int i = count_record - 1; i >= 0; i--) {
						String result = "";
						double height = records.get(i).get_height() / 100;
						double weight = records.get(i).get_weight();
						double BMI = weight / (height * height);
						if (BMI < 18.5) {
							result = "ƫ��";
							result_label = R.mipmap.thin_label;
						} else if (BMI <= 23.9) {
							result = "����";
							result_label = R.mipmap.fitness;
						} else if (BMI <= 27) {
							result = "΢��";
							result_label = R.mipmap.fat_label;
						} else if (BMI <= 32) {
							result = "ƫ��";
							result_label = R.mipmap.fat_label;
						} else {
							result = "����";
							result_label = R.mipmap.fat_label;
						}
						Map<String, Object> map = new HashMap<>();
						map.put("record_id", records.get(i).get_record_id());
						map.put("height", height * 100);
						map.put("weight", weight);
						map.put("BMI", BMI);
						map.put("date", records.get(i).get_record_time().substring(0,10)+ ' '+
								records.get(i).get_record_time().substring(11,19));
						map.put("result", result);
						map.put("result_label",result_label);
						dataList.add(map);
					}
					RecordActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							simpleAdapter = new SimpleAdapter(RecordActivity.this, dataList, R.layout.title,
									new String[]{"result", "result_label", "date"}, new int[]{R.id.show_result_title,
									R.id.result_label,
									R.id.show_date_title});
							listView.setAdapter(simpleAdapter);
							avLoadingIndicatorView.setVisibility(View.GONE);
						}
					});
				}
			}
		});
	}

	@Override
	public void initView() {
		ButterKnife.bind(this);
		avLoadingIndicatorView.setVisibility(View.VISIBLE);
		dataList = new ArrayList<>();
		MyApplication myApplication = (MyApplication) getApplication();
		host = myApplication.getHost();
		getData();
	}

	@Override
	public void initListener() {
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
										   final int arg2, long arg3) {
				ListView listView = (ListView) arg0;
				final HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(arg2);
				AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
				builder.setCancelable(false);
				builder.setTitle("ɾ����¼").setMessage("ȷ��Ҫɾ��������¼��")
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface Arg, int arg) {
								// TODO Auto-generated method stub
								final int record_id = (Integer) map.get("record_id");
								OkHttpClient okHttpClient = new OkHttpClient();
								Request request = new Request.Builder().url(host +
										"/android_health_test/record/"
										+ record_id + "/")
										.delete().build();
								Call call = okHttpClient.newCall(request);
								call.enqueue(new Callback() {
									@Override
									public void onFailure(Call call, IOException e) {
										RecordActivity.this.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(RecordActivity.this,
														"���������ƺ�����С��...", Toast.LENGTH_SHORT).show();
											}
										});
									}

									@Override
									public void onResponse(Call call, Response response) throws IOException {
										RecordActivity.this.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(RecordActivity.this,
														"ɾ���ɹ���", Toast.LENGTH_SHORT).show();
												dataList.remove(map);
												simpleAdapter.notifyDataSetChanged();
											}
										});
									}
								});
							}
						})
						.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});
				builder.setCancelable(true);
				AlertDialog dialog=builder.create();
				dialog.show();
				return true;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				ListView listView = (ListView) arg0;
				HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(arg2);
				Intent intent = new Intent(RecordActivity.this,RecordDetail.class);
				intent.putExtra("height", (Double) map.get("height"));
				intent.putExtra("weight", (Double) map.get("weight"));
				intent.putExtra("bmi", map.get("BMI").toString().substring(0,4));
				intent.putExtra("date", (String) map.get("date"));
				intent.putExtra("result", (String) map.get("result"));
				startActivity(intent);
			}
		});
		goback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
}
