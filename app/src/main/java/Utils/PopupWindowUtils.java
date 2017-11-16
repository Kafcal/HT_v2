package Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * @������ ��Ǣ��
 * @����ʱ�� 2016/8/19 0019
 * @���� popupwindow����
 */
public class PopupWindowUtils {

    /**
     * �õ�����Ļ�м��popupwindow����ʾ����߶��ǰ�����ͼ��
     *
     * @param contentView popupwindowҪ��ʾ����ͼ
     * @param parentView  �ο���ͼ
     * @return
     */
    public static PopupWindow getPopupWindowInCenter(View contentView, View parentView) {
        //        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        return getPopupWindowInCenter(contentView, width, height, parentView);
    }

    /**
     * �õ�����Ļ�м��popupwindow����ʾ
     *
     * @param contentView popupwindowҪ��ʾ����ͼ
     * @param width       popupwindow�Ŀ��
     * @param height      popupwindow�ĸ߶�
     * @param parentView  �ο���ͼ
     * @return
     */
    public static PopupWindow getPopupWindowInCenter(View contentView, int width, int height, View parentView) {
        //Gravity.CENTER:����Ļ���У���ƫ��
        return getPopupWindowAtLocation(contentView, width, height, parentView, Gravity.CENTER, 0, 0);
    }

    /**
     * �õ�ָ����ĳ����ͼ��λ�õ�popupwindow����ʾ
     *
     * @param contentView popupwindowҪ��ʾ����ͼ
     * @param width       popupwindow�Ŀ��
     * @param height      popupwindow�ĸ߶�
     * @param parentView  �ο���ͼ
     * @param gravityType �ڲο���ͼ�е����λ��
     * @param xoff        x��ƫ����
     * @param yoff        y��ƫ����
     * @return
     */
    public static PopupWindow getPopupWindowAtLocation(View contentView, int width, int height, View parentView, int gravityType, int xoff, int yoff) {
        PopupWindow popupWindow = getPopupWindow(contentView, width, height);

        //��parentView��ƫ��xoff��yoff
        popupWindow.showAtLocation(parentView,
                gravityType, xoff, yoff);

        return popupWindow;
    }

    public static PopupWindow getPopupWindowAtLocation(View contentView, View parentView, int gravityType, int xoff, int yoff) {
        return getPopupWindowAtLocation(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, parentView, gravityType, xoff, yoff);
    }


    /**
     * �õ�һ���Զ�ʶ����Ŀ��ؼ��Ϸ����·���pupupwindow����ʾ
     *
     * @param contentView popupwindowҪ��ʾ����ͼ
     * @param width       popupwindow�Ŀ��
     * @param activity    �ܵõ�getWindowManager()��������
     * @return
     */
    public static PopupWindow getPopupWindowAsDropDownParentAuto(View contentView, int width, int height, View anchorView, Activity activity) {

        //        View itemView = (View) contentView.getParent();// �õ�contentView�ĸ��ؼ�
        PopupWindow popupWindow = getPopupWindow(contentView, width, height);

        // ���������õ�λ��
        if (isShowBottom(activity, anchorView)) {// ��ʾpopupwindow��itemView���·���ƫ������Ϊ0
            popupWindow.showAsDropDown(anchorView, 0, 0);
        } else {// ��ʾpopupwindow��itemView���Ϸ���ƫ����y��Ϊ-2*itemView.getHeight()
            popupWindow.showAsDropDown(anchorView, 0,
                    -2 * anchorView.getHeight());
        }

        return popupWindow;
    }


    /**
     * �õ���ָ��ĳ����ͼ���popupwindow����ʾ
     *
     * @param contentView popupwindowҪ��ʾ����ͼ
     * @param width       popupwindow�Ŀ��
     * @param height      popupwindow�ĸ߶�
     * @param anchorView  �ο���ͼ
     * @param xoff        x��ƫ����
     * @param yoff        y��ƫ����
     * @return
     */
    public static PopupWindow getPopupWindowAsDropDown(View contentView, int width, int height, View anchorView, int xoff, int yoff) {
        PopupWindow popupWindow = getPopupWindow(contentView, width, height);
        popupWindow.showAsDropDown(anchorView, xoff, yoff);
        return popupWindow;
    }

    /**
     * �õ���ָ��ĳ����ͼ���popupwindow����ʾ(�÷���ֻ֧��4.4����)
     *
     * @param contentView popupwindowҪ��ʾ����ͼ
     * @param width       popupwindow�Ŀ��
     * @param height      popupwindow�ĸ߶�
     * @param anchorView  �ο���ͼ
     * @param gravityType �ڲο���ͼ������λ��
     * @param xoff        x��ƫ����
     * @param yoff        y��ƫ����
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static PopupWindow getPopupWindowAsDropDown(View contentView, int width, int height, View anchorView, int gravityType, int xoff, int yoff) {
        PopupWindow popupWindow = getPopupWindow(contentView, width, height);
        popupWindow.showAsDropDown(anchorView, xoff, yoff, gravityType);
        return popupWindow;
    }


    /**
     * �ж�popupWindow�Ƿ���ʾ����Ŀ���·�
     *
     * @param itemView
     * @return
     */
    private static boolean isShowBottom(Activity context, View itemView) {
        // �õ���Ļ�ĸ߶�
        // int heightPixels =
        // getResources().getDisplayMetrics().heightPixels;//��ʽ1
        int screenHeight = context.getWindowManager().getDefaultDisplay().getHeight();// ��ʽ2

        int[] location = new int[2];
        // location[0]-->x
        // location[1]-->y
        itemView.getLocationInWindow(location);
        // �õ�itemView����Ļ��Y���ֵ
        int itemViewY = location[1];

        // �õ�itemView������Ļ�ײ��ľ���
        int distance = screenHeight - itemViewY - itemView.getHeight();

        if (distance < itemView.getHeight()) {// ��Ŀ�·��Ų���popupWindow
            return false;
        } else {// ��Ŀ�·��ŵ���popupWindow
            return true;
        }
    }

    /**
     * ����ĵ�pupupwindow��������
     *
     * @param contentView popupwindowҪ��ʾ����ͼ
     * @param width       popupwindow�Ŀ��
     * @param height      popupwindow�ĸ߶�
     * @return
     */
    @NonNull
    private static PopupWindow getPopupWindow(View contentView, int width, int height) {
        PopupWindow popupWindow = new PopupWindow(contentView, width, height, true);
        popupWindow.setOutsideTouchable(false);
        openOutsideTouchable(popupWindow);
        return popupWindow;
    }

    /**
     * ���popupwindow��Χ����ĵط�ʱ����
     *
     * @param popupWindow
     */
    public static void openOutsideTouchable(PopupWindow popupWindow) {
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
    }

    /**
     * ʹwindow�䰵
     */
    public static void makeWindowDark(Activity activity) {
        makeWindowDark(activity, 0.7f);
    }

    public static void makeWindowDark(Activity activity, float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * ʹwindow����
     */
    public static void makeWindowLight(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().setAttributes(lp);
    }


}
