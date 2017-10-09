package com.example.HomeworkOne;

import java.text.DecimalFormat;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class TestDetail extends Fragment {
	private Button button1;
	private EditText editText1;
	private EditText editText2;
	private EditText input_name;
	private String content="���";
	private boolean sex=true;
	private View v;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//�������ǵĲ���
		v=inflater.inflate(R.layout.sactivity, container, false);
		button1=(Button) v.findViewById(R.id.button1);
		editText1=(EditText) v.findViewById(R.id.editText1);
		editText2=(EditText) v.findViewById(R.id.editText2);
		input_name=(EditText) v.findViewById(R.id.input_name);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				try{
				String user_name=input_name.getText().toString();
				double height=Double.parseDouble(editText1.getText().toString());
				double Mheight=height/100;
				double weight=Double.parseDouble(editText2.getText().toString());
				double BMI=weight/(Mheight*Mheight);
				RadioButton male=(RadioButton) v.findViewById(R.id.btnMan);
				if(male.isChecked()){
				     sex=false;
				}
				int sex_flag=(sex)?1:0;
			    int photo=0;                           // '1-5' for male , '6-10' for female
			    if(sex==false){
				    if(BMI<18.5){
						content="̫���ˣ��Ͻ�������!";
						photo=1;
					}
					else if(BMI<=23.9){
						content="������̫���ˣ���������֣�";
						photo=2;
					}
					else if(BMI<=27){
						content="�е����ˣ�����˶��ɣ�";
						photo=3;
					}
					else if(BMI<=32){
						content="���ӣ��Ͻ����ʰɣ�";
						photo=4;
					}
					else{
						content="�ռ����ӣ��ܲ����ٳԵ㣿";
						photo=5;
					}
			    }
			    else {
			    	if(BMI<18.5){
						content="̫���ˣ��Ͻ�������!";
						photo=6;
					}
					else if(BMI<=23.9){
						content="������̫���ˣ���������֣�";
						photo=7;
					}
					else if(BMI<=27){
						content="�е����ˣ�����˶��ɣ�";
						photo=8;
					}
					else if(BMI<=32){
						content="��椣��Ͻ����ʰɣ�";
						photo=9;
					}
					else{
						content="�ռ���椣��ܲ����ٳԵ㣿";
						photo=10;
					}
				}
				
				
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);  
			    int month= c.get(Calendar.MONTH)+1;  
			    int day = c.get(Calendar.DAY_OF_MONTH);  
				
				SQLiteDatabase db = getActivity().openOrCreateDatabase("user.db", getActivity().MODE_PRIVATE, null);
				db.execSQL("create table if not exists records(_id integer primary key autoincrement,name text not null,Height double not null,"
						+ "Weight double not null,BMI double not null,currDate date not null,sex integer)");
				ContentValues values = new ContentValues();
				values.put("name", user_name);
				values.put("Height", height);
				values.put("Weight", weight);
				DecimalFormat df = new DecimalFormat("#.0");
				values.put("BMI", df.format(BMI));
				values.put("currDate", year+"-"+month+"-"+day);
				values.put("sex", sex_flag);
				db.insert("records", null, values);
				values.clear();
				
				Bundle bundle=new Bundle();
                bundle.putInt("photo", photo);
                
                FragmentManager fManager=getFragmentManager();
				FragmentTransaction ftFragmentTransaction= fManager.beginTransaction();
				TestFragment testFragment=new TestFragment();
				testFragment.setArguments(bundle);
				ftFragmentTransaction.replace(R.id.id_activity_main,testFragment);
				ftFragmentTransaction.addToBackStack(null);
				ftFragmentTransaction.commit();
				
				
				}catch(NumberFormatException e){
					Toast.makeText(getActivity(), "��������ȷ�����ֵ������ֵ��",
						     Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		
	
		return v;
	}

}
