package team.t404.gotravel.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.map.R;

import java.util.Timer;
import java.util.TimerTask;

public class ButtonImageRound extends LinearLayout {

    private LinearLayout imageButton;//总布局
    private TextView button2;//按钮图标
    private int image;
    private float alpha;//总面板透明度
    private Timer timer = new Timer(true);//计时器
    private final Handler handler;//用来展现面板出现的动画

    @SuppressLint("HandlerLeak")
    public ButtonImageRound(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.button_image_round_view, this);
        //从xml的属性中获取到字体颜色与string
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.GoTravel);
        image=typedArray.getResourceId(R.styleable.GoTravel_image, R.color.white);
        typedArray.recycle();
        //获取控件
        imageButton = (LinearLayout) findViewById(R.id.imageButton);
        button2 = (TextView) findViewById(R.id.button_2);
        //将控件与设置的xml属性关联
        button2.setBackgroundResource(image);

        //点击后窗口展示动画
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    alpha += 0.1;
                }
                if (msg.what == 2) {
                    alpha -= 0.1;
                }
                //设置组件的透明度
                imageButton.setAlpha(alpha);
                super.handleMessage(msg);
            }
        };
    }

    public void setButtonImage(int drawable) {
        button2.setBackgroundResource(drawable);
    }

    //打开ImageRoundButton的动画效果
    public void openView() {
        alpha = 0;
        for (int s = 0; s < 10; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    this.cancel();
                }
            }, 50 * s);
        }
    }

    //关闭ImageRoundButton的动画效果
    public void closeView() {
        alpha = 1;
        for (int s = 0; s < 10; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    this.cancel();
                }
            }, 50 * s);
        }
    }
}
