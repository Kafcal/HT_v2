package com.example.HomeworkOne;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageViewUtil {
    public static void matchAll(Context context, ImageView imageView) {
        int width, height;//ImageView������Ŀ��
        //��ȡ��Ļ���
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int sWidth = metrics.widthPixels;
        int sHeight = metrics.heightPixels;
        //��ȡͼƬ���
        Drawable drawable = imageView.getDrawable();
        int dWidth = drawable.getIntrinsicWidth();
        int dHeight = drawable.getIntrinsicHeight();

        //��Ļ��߱�,һ��Ҫ�Ȱ�����һ��תΪfloat
        float sScale = (float) sWidth / sHeight;
        //ͼƬ��߱�
        float dScale = (float) dWidth / dHeight;
        /*
        ���ű�
        ���sScale>dScale����ʾ�ڸ���ȵ�����£�����Ļ�ȽϿ���ʱ��Ҫ��Ӧ�߶ȣ����űȾ�������ĸ�֮�ȣ�ͼƬ��������űȼ���
        ���sScale<dScale����ʾ�ڸ���ȵ�����£�ͼƬ�ȽϿ���ʱ��Ҫ��Ӧ��ȣ����űȾ�������Ŀ�֮�ȣ�ͼƬ�߶������űȼ���
         */
        float scale = 1.0f;
        if (sScale > dScale) {
            scale = (float) dHeight / sHeight;
            height = sHeight;//ͼƬ�߶Ⱦ�����Ļ�߶�
            width = (int) (dWidth * scale);//�������ű����ͼƬ���ź�Ŀ��
        } else if (sScale < dScale) {
            scale = (float) dWidth / sWidth;
            width = sWidth;
            height = (int) (dHeight / scale);//�����ó�
        } else {
            //������߸պñ�����ͬ����ʵ���Բ���д���պó���
            width = sWidth;
            height = sHeight;
        }
        //����ImageView���
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        imageView.setLayoutParams(params);
        //�����ͻ����һ������Ӧ��Ļ����Ӧ�ڲ�ͼƬ��ImageView�������پ���ø�ImageView�趨ʲô�ߴ������
    }
}
