package com.example.HomeworkOne;
import android.os.Bundle;

import com.thefinestartist.finestwebview.FinestWebView;
import com.wang.avi.AVLoadingIndicatorView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebView extends android.support.v4.app.Fragment{
	WebView webview;
	public static String myurl;
    @SuppressLint("SetJavaScriptEnabled")  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	View v=inflater.inflate(R.layout.webview, container, false);
//        webview = (WebView) v.findViewById(R.id.webview);
//        WebSettings webSettings = webview.getSettings();
//        //����WebView���ԣ��ܹ�ִ��Javascript�ű�
//        webSettings.setJavaScriptEnabled(true);
//        //���ÿ��Է����ļ�
//        webSettings.setAllowFileAccess(true);
//         //����֧������
//        webSettings.setBuiltInZoomControls(true);
//        //������Ҫ��ʾ����ҳ
//        webview.loadUrl(myurl);
//        //����Web��ͼ
//        webview.setWebViewClient(new WebViewClient(){
//            @Override
//         public boolean shouldOverrideUrlLoading(WebView view, String url) {
//             // TODO Auto-generated method stub
//                //����ֵ��true��ʱ�����ȥWebView�򿪣�Ϊfalse����ϵͳ�����������������
//              view.loadUrl(url);
//             return true;
//         }
//        });
		new FinestWebView.Builder(getActivity()).show(myurl);
    	return v;
    }
}