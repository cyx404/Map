package team.t404.gotravel.Interface;

import android.content.Context;
import android.util.DisplayMetrics;

public class publicTool {

    //根据手机的分辨率从dp的单位转成为px(像素)
    public static int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
}
