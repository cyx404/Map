package team.t404.gotravel.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.map.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 *  
 * 项目名称:  走·旅行
 * 包:        team.t404.gotravel.view  
 * 类名称:    TextRoundButton
 * 类描述:    变换文本的文本按钮
 * 创建人:    梁其兴
 * 创建时间:  2019/9/17 22:47  
 * 修改人:    梁其兴
 * 修改时间:  2019/9/17 22:47
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */

public class ButtonChangeTextRound extends LinearLayout {

    private Context context;
    private LinearLayout linear_1;//总布局
    private TextView button;//推荐按钮
    private int n = 0xff0A8CF5;//控制颜色渐变
    private int m = 40, ming = 40;//控制变化后的停留时间
    private int s;//按钮变化需要的时间
    private int r;//总布局绕X轴的旋转角度
    private Timer timer = new Timer(true);//计时器
    private final Handler handler;//用来展现面板出现的动画
    //按钮上面的文字
    private String originalText = "推荐";
    private String changedText = "热门";

    @SuppressLint("HandlerLeak")
    public ButtonChangeTextRound(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.button_change_text_round_view, this);
        //获取控件
        linear_1 = (LinearLayout) findViewById(R.id.linear_1);
        button = (TextView) findViewById(R.id.button_1);
        //设置开始按钮文本
        button.setText(originalText);

        //定时转换字体
        final Handler handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    shift_font_color();
                    button.setTextColor(n);//更改字体
                }
                super.handleMessage(msg);
            }
        };
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler2.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 0, 200);//延时1s，每隔200毫秒执行一次run方法

        //点击按钮变化动画
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    r -= 9;
                    if (r == 0) {
                        linear_1.setTranslationZ(10);
                        postInvalidate();
                    }
                }
                if (msg.what == 2) {
                    r += 9;
                    if (r == 720) {
                        r = 360;
                    }
                }
                linear_1.setRotationY(r);
                super.handleMessage(msg);
            }
        };

    }

    //展现TextRoundButton的动画效果
    public void openView() {
        linear_1.setVisibility(VISIBLE);
        for (s = 0; s < 40; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    this.cancel();
                }
            }, 200 + 25 * s);
        }
    }

    //隐藏TextRoundButton的动画效果
    public void closeView() {
        linear_1.setTranslationZ(0);
        for (s = 0; s < 40; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    this.cancel();
                }
            }, 25 * s);
        }
    }

    //隐藏TextRoundButton的方法
    public void invisibleView() {
        linear_1.setVisibility(INVISIBLE);
    }

    /**
     * 设置按钮上的字体
     * string1:第一个字体
     * string2:变化后的字体
     */
    public void setString(String string1, String string2) {
        this.originalText = string1;
        this.changedText = string2;
        button.setText(originalText);
    }

    /**
     * 设置按钮字体的变化时间
     * m:精确到秒
     */
    public void setTextChangeTime(int m) {
        this.m = m * 5;//用作ming的复位
        ming = m * 5;//用作减数计时
    }

    //不断切换字体的方法
    private void shift_font_color() {
        switch (n) {
            case 0xff0A8CF5:
                ming -= 1;
                if (ming == 0) {
                    n = 0xcc0A8CF5;
                    ming = m;
                }
                break;
            case 0xcc0A8CF5:
                n = 0x990A8CF5;
                break;
            case 0x990A8CF5:
                n = 0x660A8CF5;
                break;
            case 0x660A8CF5:
                n = 0x330A8CF5;
                break;
            case 0x330A8CF5:
                n = 0x000A8CF5;
                break;
            case 0xffF42121:
                ming -= 1;
                if (ming == 0) {
                    n = 0xccF42121;
                    ming = m;
                }
                break;
            case 0xccF42121:
                n = 0x99F42121;
                break;
            case 0x99F42121:
                n = 0x66F42121;
                break;
            case 0x66F42121:
                n = 0x33F42121;
                break;
            case 0x33F42121:
                n = 0x00F42121;
                break;
            case 0x000A8CF5:
                button.setText(changedText);
                n = 0x44F42121;
                break;
            case 0x00F42121:
                button.setText(originalText);
                n = 0x440A8CF5;
                break;
            case 0xbb0A8CF5:
                n = 0xff0A8CF5;
                break;
            case 0x880A8CF5:
                n = 0xbb0A8CF5;
                break;
            case 0x440A8CF5:
                n = 0x880A8CF5;
                break;
            case 0xbbF42121:
                n = 0xffF42121;
                break;
            case 0x88F42121:
                n = 0xbbF42121;
                break;
            case 0x44F42121:
                n = 0x88F42121;
                break;
        }
    }

}
