package team.t404.gotravel.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.map.R;

public class ButtonTextRound extends LinearLayout {

    private TextView textView;
    private String buttonText;

    public ButtonTextRound(Context context,AttributeSet attrs) {
        super(context, attrs);
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.button_text_round_view, this);
        //从xml的属性中获取到字体颜色与string
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.GoTravel);
        buttonText=typedArray.getString(R.styleable.GoTravel_text);
        typedArray.recycle();
        //获取控件
        LinearLayout linearLayout= (LinearLayout) getChildAt(0);
        textView= (TextView) linearLayout.getChildAt(0);
        //将控件与设置的xml属性关联
        textView.setText(buttonText);
    }

    public void setText(CharSequence text){
        textView.setText(text);
    }
}
