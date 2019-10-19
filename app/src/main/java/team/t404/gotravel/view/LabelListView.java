package team.t404.gotravel.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LabelListView extends RelativeLayout {

    List<Integer> eachLineHeight = new ArrayList<>();
    List<List<View>> allViews = new ArrayList<>();


    public LabelListView(Context context) {
        super(context);
    }

    public LabelListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        1.measure children and  measure  self
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);//获取控件高度

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);//获取控件宽度

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;


        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams childLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            int childMeasuredHeight = child.getMeasuredHeight() + childLayoutParams.bottomMargin + childLayoutParams.topMargin;
            int childMeasuredWidth = child.getMeasuredWidth() + childLayoutParams.leftMargin + childLayoutParams.rightMargin;

            if (lineWidth + childMeasuredWidth > widthSpecSize) {//The line is full
                width = Math.max(lineWidth, childMeasuredWidth);
                height = lineHeight + height;
                lineWidth = childMeasuredWidth;
                lineHeight = childMeasuredHeight;
            } else {
                //record line info
                lineWidth = lineWidth + childMeasuredWidth;
                lineHeight = Math.max(lineHeight, childMeasuredHeight);
            }

            if (i == childCount - 1) { //The last one
                width = Math.max(width, lineWidth);
                height = lineHeight + height;
            }

        }
        //2.set params
        setMeasuredDimension(widthSpecMode == MeasureSpec.EXACTLY ? widthSpecSize : width, heightSpecMode == MeasureSpec.EXACTLY ? heightSpecSize : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        1.record how many views each row and   get max height of the row
        eachLineHeight.clear();
        allViews.clear();

        int childCount = getChildCount();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int childMeasuredHeight = child.getMeasuredHeight() + layoutParams.bottomMargin + layoutParams.topMargin;
            int childMeasuredWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;

            //The line is full
            if (lineWidth + childMeasuredWidth > getWidth()) {
                lineWidth = 0;
                eachLineHeight.add(lineHeight);
                allViews.add(lineViews);
                lineViews = new ArrayList<>();
            }

            //The line is not full
            lineHeight = Math.max(lineHeight, childMeasuredHeight);
            lineWidth = lineWidth + childMeasuredWidth;
            lineViews.add(child);

            if (i == childCount - 1) {//The last one
                eachLineHeight.add(lineHeight);
                allViews.add(lineViews);
            }

            final int finalI = i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {// set callback
                        mCallback.onItemClick(finalI);
                    }
                }
            });
        }

        int left = 0;
        int top = 0;

        for (int i = 0; i < allViews.size(); i++) {

            List<View> views = allViews.get(i);
            lineHeight = eachLineHeight.get(i);

            //2.layout row
            for (int j = 0; j < views.size(); j++) {
                View child = views.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                //calc childView's left,top,right,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
                Point point = new Point();
                point.x = lc;
                point.y = tc;
                child.setTag(point);
            }
            top = top + lineHeight;
            left = 0;
        }
    }

    //*********************about callback
    Callback mCallback;

    public void setOnItemClickCallback(Callback callback) {
        mCallback = callback;
    }

    public void setItemContent(int position, String content) {
        if (getChildAt(position) instanceof TextView) {
            TextView tv = (TextView) getChildAt(position);
            tv.setText(content);
            tv.requestLayout();
        }
    }

    public String getItemContent(int position) {
        String content = "";
        if (getChildAt(position) instanceof TextView) {
            TextView tv = (TextView) getChildAt(position);
            content = tv.getText().toString();
        }
        return content;
    }

    public interface Callback {
        void onItemClick(int position);
    }
}
