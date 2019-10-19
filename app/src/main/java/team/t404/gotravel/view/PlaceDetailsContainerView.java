package team.t404.gotravel.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.lenovo.map.MainActivity;
import com.example.lenovo.map.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import team.t404.gotravel.Interface.publicTool;

public class PlaceDetailsContainerView extends HorizontalScrollView {

    private Context context;
    private ArrayList<PlaceDetailsView> placeDetailsView = new ArrayList<PlaceDetailsView>();
    private int windowWidth;//手机屏幕宽度
    private short h;//控件高度
    private short w;//控件宽度
    private short partitionH;//控件高度分割后一份的长度
    private short partitionW;//控件宽度分割后一份的长度
    private short partitionHRemainder;//控件高度分割后剩余的部分
    private short partitionWRemainder;//控件宽度分割后剩余的部分
    private short ms;//总面板出现需要的时间
    private Timer timer = new Timer(true), timer1 = new Timer(true);//计时器
    private boolean perform = true;//判断介绍面板是否可以滑动
    private boolean isDetails;//判断面板打开状态（详情/概况）
    private int x;//实时记录滚动条的滚动距离
    private int xOld, xNew;//记录滚动条的滚动距离,用作计算
    private short remainderX;//记录当前滚动位置到介绍面板居中的距离
    private short remainderXRemainder;//记录当前滚动位置到介绍面板居中的距离的余数
    private short oldNum = 0;//记录滑动前是第几个介绍面板
    private short num;//记录当前滚动到第几个介绍面板，第一个面板记录为0
    public static Handler handler;//用来展现面板出现的动画
    private final Handler handler1;//用来展现面板出现的动画
    public static Handler findPlacesByPraiseHandler;//用来展现面板出现的动画
    private JSONObject jsonObject;//从服务器获取到的数据包
    private JSONArray jsonArray;//数据包的景点信息列表

    @SuppressLint({"HandlerLeak", "ClickableViewAccessibility"})
    public PlaceDetailsContainerView(Context activityContext, AttributeSet attrs) {
        super(activityContext, attrs);
        context = activityContext;
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.place_details_container_view, this);
        //获取控件
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView1));
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView2));
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView3));
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView4));
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView5));
        //设置编号
        for (int i = 0; i < 5; i++) {
            placeDetailsView.get(i).setNum(i);
        }

        //获取屏幕宽度
        windowWidth = getResources().getDisplayMetrics().widthPixels;

        //获取控件大小
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                h = (short) getHeight();//高度
                w = (short) getWidth();//宽度
                partitionHRemainder = (short) (h % 15);
                partitionH = (short) ((h - partitionHRemainder) / 15);
                partitionWRemainder = (short) (w % 15);
                partitionW = (short) ((w - partitionWRemainder) / 15);
                setVisibility(INVISIBLE);
                getLayoutParams().height = 0;
                setLayoutParams(getLayoutParams());
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //初始化
        findPlacesByPraiseHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("findPlacesByPraise");
                try {
                    jsonObject = new JSONObject(result);
                    jsonArray = jsonObject.getJSONArray("places");//获取服务器发过来的景点数据
                    num = 0;
                    for (int i = 0; i < 5; i++) {
                        placeDetailsView.get(i).setTotalPage(jsonArray.length());//设置总标签
                    }
                    createPlaceDetailsView(num);//创建添加景点介绍面板(PlaceDetailsView)
                    Message message = new Message();
                    message.what = 1;
                    MainActivity.handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        //点击后窗口展示动画
        handler = new Handler() {
            short a = 0, b = 0, h1 = 0;//计数变量

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    a++;
                    if (a == 15) {
                        h1 = (short) (h1 + partitionH + partitionHRemainder);
                        a = 0;
                    } else {
                        h1 += partitionH;
                    }
                }
                if (msg.what == 2) {
                    b++;
                    if (b == 15) {
                        h1 = (short) (h1 - partitionH - partitionHRemainder);
                        setVisibility(INVISIBLE);
                        getChildAt(0).setVisibility(GONE);
                        b = 0;
                    } else {
                        h1 -= partitionH;
                    }
                }
                if (msg.what == 3) {
                    h1 += 32;
                }
                if (msg.what == 4) {
                    h1 -= 32;
                }
                if (msg.what == 5) {
                    int height = msg.getData().getInt("height");
                    for (int i = 0; i < 5; i++) {
                        placeDetailsView.get(i).setHeight(height);//设置其他介绍面板的高度
                    }
                    //更改按钮的点击事件
                    isDetails = msg.getData().getInt("verdict") == 1;
                    for (int i = 0; i < 5; i++) {
                        placeDetailsView.get(i).changeOnClickListener(isDetails);
                    }
                }
                if (msg.what < 5) {
                    //设置组件的高度
                    getLayoutParams().height = h1;
                    setLayoutParams(getLayoutParams());
                }
                super.handleMessage(msg);
            }
        };

        //居中展现每个景点介绍面板动画
        handler1 = new Handler() {
            int a = 0, b = 0;//计数变量

            @Override
            public void handleMessage(Message msg) {
                //判断滚动条向前还是向后移动
                if (msg.what == 1) {
                    a++;
                    if (a < 15) {
                        xNew += remainderX;
                    } else if (a == 15) {
                        xNew = xNew + remainderX + remainderXRemainder;
                    } else {
                        a = 0;
                    }
                }
                if (msg.what == 2) {
                    b++;
                    if (b < 15) {
                        xNew -= remainderX;
                    } else if (b == 15) {
                        xNew = xNew - remainderX - remainderXRemainder;
                    } else {
                        b = 0;
                    }
                }
                //设置滚动位置
                scrollTo(xNew, 0);
                if (a == 0 && b == 0) {//判断动画是否展示完毕
                    try {//判断滚动到哪一个景点介绍表,动态更改景点介绍表
                        num += (xNew - xOld) / windowWidth;
                        xOld = xNew;
                        if (num - oldNum == 2 || oldNum - num == 2) {
                            createPlaceDetailsView(num);//更改景点介绍面板(PlaceDetailsView)内容
                        }
                        perform = true;//解除对滑动的锁定
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);
            }
        };

        //监听介绍总面板滚动事件
        setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                x = scrollX;//获取滚动面板滑动距离
            }
        });
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!perform) {//松手后介绍面板没居中前,无法滑动
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {//监听手指离开手机屏幕事件
                    perform = false;
                    xNew = x;//通过滚动事件记录的x传值给xNew进行计算
                    remainderX = (short) (xNew % windowWidth);
                    if (remainderX < windowWidth - 50 && xOld > xNew) {
                        remainderXRemainder = (short) (remainderX % 15);
                        remainderX = (short) (remainderX - remainderXRemainder);
                        remainderX = (short) (remainderX / 15);
                        slidingBackward();
                    } else if (remainderX >= windowWidth - 50 && xOld > xNew) {
                        remainderX = (short) (windowWidth - remainderX);
                        remainderXRemainder = (short) (remainderX % 15);
                        remainderX = (short) (remainderX - remainderXRemainder);
                        remainderX = (short) (remainderX / 15);
                        slidingBefore();
                    } else if (remainderX < 50) {
                        remainderXRemainder = (short) (remainderX % 15);
                        remainderX = (short) (remainderX - remainderXRemainder);
                        remainderX = (short) (remainderX / 15);
                        slidingBackward();
                    } else {
                        remainderX = (short) (windowWidth - remainderX);
                        remainderXRemainder = (short) (remainderX % 15);
                        remainderX = (short) (remainderX - remainderXRemainder);
                        remainderX = (short) (remainderX / 15);
                        slidingBefore();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 创建景点介绍面板的方法
     * placeNum：表示服务器传过来的数组里的第几个景点信息
     */
    public void createPlaceDetailsView(short placeNum) {
        int first = 2;//placeNum在队列的位置
        if (num == 0) {
            first = 0;
        } else if (num == 1) {
            first = 1;
        } else if (num + 2 == jsonArray.length()) {
            first = 3;
        } else if (num + 1 == jsonArray.length()) {
            first = 4;
        }
        int num = first;//用于计算需要添加第几个标签
        for (int i = 0; i < 5; i++) {
            createPlaceDetailsView_1(placeDetailsView.get(i), placeNum - num);
            num--;
        }
        oldNum = placeNum;//记录左右滚动面板中点的景点介绍面板序号
        //num和对应内容对齐操作
        xOld = windowWidth * first;
        scrollTo(xOld, 0);
    }

    //被createPlaceDetailsView(int placeNum)循环调用的方法
    public void createPlaceDetailsView_1(PlaceDetailsView placeDetailsView, int placeNum) {
        JSONObject jsonObjectPlace = jsonArray.optJSONObject(placeNum);//景点信息列表的一个景点信息
        JSONObject jsonObjectPlaceInformation = jsonObjectPlace.optJSONObject("place");//获取景点基本信息
        JSONArray userCustomization = jsonObjectPlaceInformation.optJSONArray("customization");//获取用户定制标签
        JSONArray placeType = jsonObjectPlaceInformation.optJSONArray("place_type");//获取景点类型标签
        JSONArray userHobby = jsonObjectPlaceInformation.optJSONArray("hobby");//获取用户爱好标签
        JSONArray imageArray = jsonObjectPlaceInformation.optJSONArray("picture");//获取用户爱好标签
        //清除对象原来的数据
        placeDetailsView.removeAllContentView();
        //向控件添加景点文字内容
        placeDetailsView.addText(String.valueOf(placeNum+1),//列表序号
                jsonObjectPlaceInformation.optInt("place_id"),//景点ID
                jsonObjectPlaceInformation.optString("name"),//景点名称
                jsonObjectPlace.optString("distance"),//获取景点到定位点的直线距离
                jsonObjectPlaceInformation.optString("praise"),//景点好评数
                jsonObjectPlaceInformation.optString("address"),//景点地址
                jsonObjectPlaceInformation.optString("introduce"));//景点地址
        //向控件添加标签内容(大概的)
        placeDetailsView.addLabel(userCustomization, R.drawable.button_border_blue);
        placeDetailsView.addLabel(placeType, R.drawable.button_border_green);
        placeDetailsView.addLabel(userHobby, R.drawable.button_border_red);
        //向控件添加图片内容
        placeDetailsView.addImage(imageArray);
        //向控件添加标签内容(详细的)
        placeDetailsView.addLabel(userCustomization, R.drawable.button_border_blue, 1);
        placeDetailsView.addLabel(placeType, R.drawable.button_border_green, 2);
        placeDetailsView.addLabel(userHobby, R.drawable.button_border_red, 3);
    }

    //打开PlaceDetailsView的动画效果
    public void openView() {
        setVisibility(VISIBLE);
        getChildAt(0).setVisibility(VISIBLE);
        for (int s = 0; s < 15; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    this.cancel();
                }
            }, 20 * s);
        }
    }

    //关闭PlaceDetailsView的动画效果
    public void closeView() {
        for (int s = 0; s < 15; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    this.cancel();
                }
            }, 20 * s);
        }
    }

    //介绍面板向前移动动画效果
    public void slidingBefore() {
        for (int s = 0; s < 16; s++) {
            timer1.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler1.sendMessage(message);
                    this.cancel();
                }
            }, 20 * s);
        }
    }

    //介绍面板向后移动动画效果
    public void slidingBackward() {
        for (int s = 0; s < 16; s++) {
            timer1.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 2;
                    handler1.sendMessage(message);
                    this.cancel();
                }
            }, 20 * s);
        }
    }
}
