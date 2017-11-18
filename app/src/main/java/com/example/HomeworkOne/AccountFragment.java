package com.example.HomeworkOne;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import MyInterface.InitView;
import butterknife.Bind;
import butterknife.ButterKnife;
import lrq.com.addpopmenu.PopMenu;
import lrq.com.addpopmenu.PopMenuItem;
import lrq.com.addpopmenu.PopMenuItemListener;
import com.lqr.optionitemview.OptionItemView;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.platformtools.Util;
import com.zhy.autolayout.AutoLinearLayout;

public class AccountFragment extends Fragment implements InitView{
	private IWXAPI api;
	private static final String APP_ID="wxac23c0af2a986db5";
	@Bind(R.id.account_contact)
	OptionItemView contact;
	@Bind(R.id.account_record)
	OptionItemView record;
	@Bind(R.id.account_help)
	OptionItemView help;
	@Bind(R.id.llMyInfo)
	AutoLinearLayout my_info;
	@Bind(R.id.myWeb)
	OptionItemView myWeb;
	@Bind(R.id.shareApp)
	OptionItemView shareApp;
	@Bind(R.id.tvName)
	TextView name;
	@Bind(R.id.tvEmail)
	TextView email;
	@Bind(R.id.ivHeader)
	ImageView header;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.account_main, container, false);
		reToWx();
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		ButterKnife.bind(this,view);
		initView();
		initListener();
	}

	private void switchFragment(android.support.v4.app.Fragment targetFragment) {
		MainActivity.tab_transfer=targetFragment;
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		if (!targetFragment.isAdded()) {
			transaction
					.hide(AccountFragment.this)
					.add(R.id.id_content, MainActivity.tab_transfer)
					.commit();
		} else {
			transaction
					.hide(AccountFragment.this)
					.show( MainActivity.tab_transfer)
					.commit();
		}

	}

	private void reToWx(){
		api= WXAPIFactory.createWXAPI(getActivity().getApplication(),APP_ID,true);
		api.registerApp(APP_ID);
	}

	private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	@Override
	public void initView() {
		SharedPreferences sharedPreferences = getActivity().
				getSharedPreferences("Session", Context.MODE_PRIVATE);
		String name_str = sharedPreferences.getString("username","null");
		String email_str = sharedPreferences.getString("email","null");
		String header_str = sharedPreferences.getString("header","null");
		Uri header_uri = Uri.parse(header_str);
		Picasso.with(getActivity()).load(header_uri).into(header);
		name.setText(name_str);
		email.setText(email_str);
	}

	@Override
	public void initListener() {
		contact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SharedPreferences share = getActivity().getSharedPreferences("Session",
						getActivity().MODE_PRIVATE);
				int user_id = share.getInt("user_id",0);
				String sessionid = share.getString("sessionid","null");
				Bundle bundle = new Bundle();
				bundle.putInt("user_id",user_id);
				bundle.putString("sessionid",sessionid);
				Intent intent = new Intent(getActivity(),ContactDeveloper.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		my_info.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(),UserInfo.class);
				startActivity(intent);
			}
		});
		record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(),RecordActivity.class);
				startActivity(intent);
			}
		});
		help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyWebFragment.myurl="http://kafca.legendh5.com/h5/hthelp.html";
				switchFragment(new MyWebFragment());
			}
		});
		myWeb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyWebFragment.myurl="http://120.78.67.135:8000/home/index";
				switchFragment(new MyWebFragment());
			}
		});
		shareApp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PopMenu mPopMenu = new PopMenu.Builder().attachToActivity(getActivity())
						.addMenuItem(new PopMenuItem("微信好友",
								getResources().getDrawable(R.drawable.icon64_appwx_logo)))
						.addMenuItem(new PopMenuItem("微信朋友圈",
								getResources().getDrawable(R.drawable.icon_res_download_moments)))
						.setOnItemClickListener(new PopMenuItemListener() {
							@Override
							public void onItemClick(PopMenu popMenu, int position) {
								switch (position){
									case 0:
										WXWebpageObject webpageObject=new WXWebpageObject();
										webpageObject.webpageUrl="http://kafca.baiduux.com/h5/00995a8c-3555-895e-e141-c20faeb69724.html";

										WXMediaMessage msg=new WXMediaMessage(webpageObject);
										msg.title="体型测试";
										msg.description="用户输入自己的身高和体重，应用会根据输入值计算用户的BMI指数（身体质量指数），衡量人体的肥胖程度。";

										Bitmap thump=android.graphics.BitmapFactory.decodeResource(getResources(),
												R.drawable.logo);
										msg.thumbData= Util.bmpToByteArray(thump,true);

										SendMessageToWX.Req req=new SendMessageToWX.Req();
										req.transaction =buildTransaction("webpage");
										req.message=msg;
										req.scene = SendMessageToWX.Req.WXSceneSession;
										api.sendReq(req);
										break;
									case 1:
										WXWebpageObject webpageObject1=new WXWebpageObject();
										webpageObject1.webpageUrl="http://kafca.baiduux.com/h5/00995a8c-3555-895e-e141-c20faeb69724.html";

										WXMediaMessage msg1=new WXMediaMessage(webpageObject1);
										msg1.title="体型测试";
										msg1.description="用户输入自己的身高和体重，应用会根据输入值计算用户的BMI指数（身体质量指数），衡量人体的肥胖程度。";

										Bitmap thump1=android.graphics.BitmapFactory.decodeResource(getResources(),
												R.drawable.logo);
										msg1.thumbData=Util.bmpToByteArray(thump1,true);

										SendMessageToWX.Req req1=new SendMessageToWX.Req();
										req1.transaction =buildTransaction("webpage");
										req1.message=msg1;
										req1.scene = SendMessageToWX.Req.WXSceneTimeline;
										api.sendReq(req1);
										break;
									default:
										break;
								}
							}
						})
						.columnCount(2)
						.build();
				mPopMenu.setmMarginTopRemainSpace(1.3f);
				mPopMenu.show();
			}
		});
	}
}
