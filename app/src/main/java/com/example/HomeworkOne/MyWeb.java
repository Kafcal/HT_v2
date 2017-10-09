package com.example.HomeworkOne;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWeb extends Activity{
	
	private WebView webview;    
    @SuppressLint("SetJavaScriptEnabled")  
    @Override  
    protected void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        webview = (WebView) findViewById(R.id.webview);  
        WebSettings webSettings = webview.getSettings();  
        //����WebView���ԣ��ܹ�ִ��Javascript�ű�    
        webSettings.setJavaScriptEnabled(true);    
        //���ÿ��Է����ļ�  
        webSettings.setAllowFileAccess(true);  
         //����֧������  
        webSettings.setBuiltInZoomControls(true);  
        //������Ҫ��ʾ����ҳ    
        webview.loadUrl("http://www.baidu.com");    
        //����Web��ͼ    
        webview.setWebViewClient(new webViewClient ());    
          
    }  
       
  
    @Override  
    public boolean onCreateOptionsMenu(Menu menu)  
    {  
        // Inflate the menu; this adds items to the action bar if it is present.  
        getMenuInflater().inflate(R.menu.main, menu);  
        return true;  
    }  
      
    @Override   
    //���û���    
    //����Activity���onKeyDown(int keyCoder,KeyEvent event)����    
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {    
            webview.goBack(); //goBack()��ʾ����WebView����һҳ��    
            return true;    
        }    
        finish();//�����˳�����  
        return false;    
    }    
        
    //Web��ͼ    
    public class webViewClient extends WebViewClient {    
        public boolean shouldOverrideUrlLoading(WebView view, String url) {    
            view.loadUrl(url);    
            return true;    
        }    
    }    
  
}  


