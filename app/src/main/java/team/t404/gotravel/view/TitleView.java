package team.t404.gotravel.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.lenovo.map.R;

import java.util.Timer;
import java.util.TimerTask;

import team.t404.gotravel.Interface.publicTool;

public class TitleView extends RelativeLayout {

    private Context context;
    private RelativeLayout title;//总布局
    private int h;//总面板高度
    private Timer timer= new Timer(true);//计时器
    private final Handler handler;//用来展现面板出现的动画

    @SuppressLint("HandlerLeak")
    public TitleView(Context activityContext, AttributeSet attrs) {
        super(activityContext, attrs);
        context = activityContext;
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.title_view, this);
        //获取控件
        title = (RelativeLayout) findViewById(R.id.title);

        //点击后窗口展示动画
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    h += 6;
                }
                if (msg.what == 2) {
                    h -= 6;
                    if (h == 0) {
                        title.setVisibility(INVISIBLE);
                    }
                }
                //设置组件的高度
                title.getLayoutParams().height = publicTool.dipTopx(context, h);
                title.setLayoutParams(title.getLayoutParams());
                super.handleMessage(msg);
            }
        };
    }

    //打开TitleView的动画效果
    public void openView() {
        title.setVisibility(VISIBLE);
        for (int s = 0; s < 11; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    this.cancel();
                }
            },  20 * s);
        }
    }

    //关闭TitleView的动画效果
    public void closeView() {
        for (int s = 0; s < 11; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    this.cancel();
                }
            },  20 * s);
        }
    }

}
