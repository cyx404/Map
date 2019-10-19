package team.t404.gotravel.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lenovo.map.R;

import java.util.Timer;
import java.util.TimerTask;

import team.t404.gotravel.Interface.publicTool;


/**
 *    
 * 项目名称:  走·旅行
 * 包:        team.t404.gotravel.view  
 * 类名称:    SearchBarView
 * 类描述:    搜索框的view 
 * 创建人:    梁其兴
 * 创建时间:  2019/9/17 12:34  
 * 修改人:    梁其兴
 * 修改时间:  2019/9/20 22:00
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */
public class SearchBarView extends LinearLayout {

    private Context context;
    private LinearLayout search_bar;//总布局
    private EditText search;//输入框
    private ImageView icon;//搜索图标
    private int h = 42;//总面板高度
    private int s;//总面板出现需要的时间
    private Timer timer=new Timer(true);//计时器
    private final Handler handler;//用来展现面板出现的动画

    //构造方法
    @SuppressLint("HandlerLeak")
    public SearchBarView(Context activityContext, AttributeSet attrs) {
        super(activityContext, attrs);
        context = activityContext;
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.search_bar, this);
        //获取控件
        search_bar = (LinearLayout) findViewById(R.id.search_bar);

        //点击后窗口展示动画
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    h += 3;
                }
                if (msg.what == 2) {
                    h -= 3;
                    if (h == 0) {
                        search_bar.setVisibility(INVISIBLE);
                    }
                }
                //设置组件的高度
                search_bar.getLayoutParams().height = publicTool.dipTopx(context, h);
                search_bar.setLayoutParams(search_bar.getLayoutParams());
                super.handleMessage(msg);
            }
        };
    }

    //打开SearchBarView的动画效果
    public void openView() {
        search_bar.setVisibility(VISIBLE);
        for (int s = 0; s < 14; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    this.cancel();
                }
            },  15 * s);
        }
    }

    //关闭SearchBarView的动画效果
    public void closeView() {
        for (int s = 0; s < 14; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    this.cancel();
                }
            },  15 * s);
        }
    }

}
