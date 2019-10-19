package team.t404.gotravel.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.map.MainActivity;
import com.example.lenovo.map.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import team.t404.gotravel.Interface.publicTool;

public class PlaceDetailsView extends RelativeLayout {

    private Context context;
    private int num;//控件编号
    private int placeId;//景点编号
    private RelativeLayout placeDetailsView;
    private TextView page, totalPage;//页码记录
    private TextView placeName;//景点名称
    private TextView placeDistance;//景点距离
    private TextView placeAddress;//景点地址
    private TextView placePraise;//景点好评数
    private TextView placeIntroduce;//景点介绍
    private HorizontalScrollView photo1;
    private LinearLayout photo1_1;
    private LabelListView labelList1;
    private LabelListView labelList3;
    private LabelListView labelList4;
    private LabelListView labelList5;
    private Button details;//详情按钮
    private Button addPlan;//添加计划按钮
    private boolean isDetails;//判断此时控件按钮的事件
    private ArrayList<View> decorativeViewList = new ArrayList<View>();//装饰性的View列表
    private int labelWidth = 0;//获取第一行标签的宽度，用作换行判断
    private int labelWidth2 = 0;//获取第二行标签的宽度，用作换行判断
    private int labelNum = 0;//统计第一行标签的数量，用作换行判断
    private int labelNum2 = 0;//统计第二行标签的数量，用作删除多余标签判断
    private int h;//面板高度
    private Timer timer = new Timer(true);//计时器
    private final Handler handler;//用来展现面板出现的动画

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @SuppressLint({"HandlerLeak", "ClickableViewAccessibility"})
    public PlaceDetailsView(Context activityContext, AttributeSet attrs) {
        super(activityContext, attrs);
        context = activityContext;
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.place_details_view, this);
        //获取控件
        placeDetailsView = (RelativeLayout) findViewById(R.id.placeDetailsView);
        page = (TextView) findViewById(R.id.page);
        totalPage = (TextView) findViewById(R.id.totalPage);
        placeName = (TextView) findViewById(R.id.placeName);
        placeDistance = (TextView) findViewById(R.id.placeDistance);
        placePraise = (TextView) findViewById(R.id.placePraise);
        placeAddress = (TextView) findViewById(R.id.placeAddress);
        placeIntroduce = (TextView) findViewById(R.id.placeIntroduce);
        photo1 = (HorizontalScrollView) findViewById(R.id.photo1);
        photo1_1 = (LinearLayout) findViewById(R.id.photo1_1);
        labelList1 = (LabelListView) findViewById(R.id.labelList1);
        labelList3 = (LabelListView) findViewById(R.id.labelList3);
        labelList4 = (LabelListView) findViewById(R.id.labelList4);
        labelList5 = (LabelListView) findViewById(R.id.labelList5);
        details = (Button) findViewById(R.id.details);
        addPlan = (Button) findViewById(R.id.addPlan);
        decorativeViewList.add((TextView) findViewById(R.id.line1));
        decorativeViewList.add((TextView) findViewById(R.id.line2));
        decorativeViewList.add((TextView) findViewById(R.id.line3));
        decorativeViewList.add((TextView) findViewById(R.id.line4));
        decorativeViewList.add((TextView) findViewById(R.id.line5));
        decorativeViewList.add((TextView) findViewById(R.id.line6));
        decorativeViewList.add((TextView) findViewById(R.id.text1));
        decorativeViewList.add((TextView) findViewById(R.id.text2));
        decorativeViewList.add((TextView) findViewById(R.id.text3));
        decorativeViewList.add((TextView) findViewById(R.id.text4));
        decorativeViewList.add((TextView) findViewById(R.id.text5));

        //获取控件大小
        placeDetailsView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                h = (short) placeDetailsView.getHeight();//高度
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        photo1.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroup = (ViewGroup) v.getParent();
                viewGroup.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //详情按钮/返回按钮
        details.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDetails) {
                    details.setText("详情");
                    closeView();
                } else {
                    details.setText("返回");
                    openView();
                }
                details.setClickable(false);
                isDetails = !isDetails;
            }
        });
        //手势监听器
        final GestureDetector detector2 = new GestureDetector(details.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override//单击屏幕，双击不会回调
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }
        });
        details.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector2.onTouchEvent(event);
            }
        });

        //添加计划按钮
        addPlan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 10;
                Bundle bundle = new Bundle();
                bundle.putInt("placeId", placeId);
                message.setData(bundle);
                PlaceDetailsContainerView.handler.sendMessage(message);//更改其他介绍面板的高度
            }
        });

        //点击后窗口展示动画
        handler = new Handler() {
            short a = 0, b = 0;//计数变量

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    a++;
                    if (a < 15) {
                        h += 16;
                    } else {
                        h += 16;
                        a = 0;
                        Message message = new Message();
                        message.what = 5;
                        Bundle bundle = new Bundle();
                        bundle.putInt("height", h);
                        bundle.putInt("verdict", 1);
                        message.setData(bundle);
                        PlaceDetailsContainerView.handler.sendMessage(message);//更改其他介绍面板的高度
                    }
                }
                if (msg.what == 2) {
                    b++;
                    if (b < 15) {
                        h -= 16;
                    } else {
                        h -= 16;
                        b = 0;
                        Message message = new Message();
                        message.what = 5;
                        Bundle bundle = new Bundle();
                        bundle.putInt("height", h);
                        bundle.putInt("verdict", 2);
                        message.setData(bundle);
                        PlaceDetailsContainerView.handler.sendMessage(message);//更改其他介绍面板的高度
                    }
                }
                //设置组件的高度
                placeDetailsView.getLayoutParams().height = publicTool.dipTopx(context, h);
                placeDetailsView.setLayoutParams(placeDetailsView.getLayoutParams());
                super.handleMessage(msg);
            }
        };
    }

    //设置页码总数
    public void setTotalPage(int total) {
        totalPage.setText(String.valueOf(total));
    }

    //设置景点介绍面板内容
    public void addText(String pageNum, int id, String name, String distance, String praise, String address, String introduce) {
        page.setText(pageNum);
        placeId = id;
        placeName.setText(name);
        placeDistance.setText(distance.concat(" 公里"));
        placePraise.setText("好评数:".concat(praise));
        placeAddress.setText(address);
        placeIntroduce.setText(introduce);
    }

    //设置图片
    public void addImage(JSONArray imageArray) {
        int imageNum = imageArray.length();//获取图片的数量
        //设置控件的外边距
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(6, 0, 6, 0);
        for (int i = 0; i < imageNum; i++) {
            ImageView imageView = new ImageView(context);
            photo1_1.addView(imageView);
            imageView.setLayoutParams(layoutParams);
            Glide.with(context).load(imageArray.optString(i)).override(publicTool.dipTopx(context, 330), publicTool.dipTopx(context, 110)).into(imageView);
        }
    }

    /**
     * 添加景点标签内容
     * customization:景点标签数组
     * labelDrawable：标签样式
     */
    public void addLabel(JSONArray customization,int labelDrawable) {
        if (customization != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 3, 6, 0);//设置控件的外边距
            for (int i = 0; i < customization.length(); i++) {
                final TextView textView = new TextView(getContext());
                textView.setBackground(getResources().getDrawable(labelDrawable, null));
                textView.setLayoutParams(layoutParams);//设置控件的外边距
                textView.setPadding(6, 0, 6, 0);//设置控件内边距
                textView.setSingleLine(true);//设置控件不能换行
                textView.setText(customization.optString(i));
                textView.setTextColor(0xff505050);
                labelList1.addView(textView);
            }
        }
    }

    public void addLabel(JSONArray customization,int labelDrawable,int labelType) {
        if (customization != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 3, 6, 0);//设置控件的外边距
            for (int i = labelNum; i < customization.length(); i++) {
                final TextView textView = new TextView(getContext());
                textView.setBackground(getResources().getDrawable(labelDrawable, null));
                textView.setLayoutParams(layoutParams);//设置控件的外边距
                textView.setPadding(6, 0, 6, 0);//设置控件内边距
                textView.setSingleLine(true);//设置控件不能换行
                textView.setText(customization.optString(i));
                textView.setTextSize(16);
                textView.setTextColor(0xff505050);
                labelList3.setOnItemClickCallback(new LabelListView.Callback() {
                    @Override
                    public void onItemClick(int position) {
                        Log.i("test","123");
                    }
                });
                if(labelType==1) {
                    labelList3.addView(textView);
                }else if(labelType==2){
                    labelList4.addView(textView);
                }else if(labelType==3){
                    labelList5.addView(textView);
                }
            }
        }
    }

    //清空整个对象的数据的方法
    public void removeAllContentView() {
        labelWidth = 0;
        labelWidth2 = 0;
        labelNum = 0;
        labelNum2 = 0;
        labelList1.removeAllViews();
        photo1_1.removeAllViews();
        labelList3.removeAllViews();
        labelList4.removeAllViews();
        labelList5.removeAllViews();
    }

    //设置控件大小
    public void setHeight(int h) {
        this.h = h;
        placeDetailsView.getLayoutParams().height = publicTool.dipTopx(context, h);
        placeDetailsView.setLayoutParams(placeDetailsView.getLayoutParams());
    }

    //更改按钮的点击事件
    public void changeOnClickListener(boolean b) {
        int num = decorativeViewList.size();
        if (b) {
            isDetails = true;
            details.setClickable(true);
            photo1.setVisibility(VISIBLE);
            labelList1.setVisibility(GONE);
            labelList3.setVisibility(VISIBLE);
            labelList4.setVisibility(VISIBLE);
            labelList5.setVisibility(VISIBLE);
            placeIntroduce.setVisibility(VISIBLE);
            details.setText("返回");
            for (int i = 0; i < num; i++) {
                decorativeViewList.get(i).setVisibility(VISIBLE);
            }
        } else {
            isDetails = false;
            details.setClickable(true);
            photo1.setVisibility(GONE);
            labelList1.setVisibility(VISIBLE);
            labelList3.setVisibility(GONE);
            labelList4.setVisibility(GONE);
            labelList5.setVisibility(GONE);
            placeIntroduce.setVisibility(GONE);
            details.setText("详情");
            for (int i = 0; i < num; i++) {
                decorativeViewList.get(i).setVisibility(GONE);
            }
        }
    }

    //打开PlaceDetailsView详情的动画效果
    public void openView() {
        for (int s = 0; s < 15; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    Message message1 = new Message();
                    message.what = 1;
                    message1.what = 3;
                    PlaceDetailsContainerView.handler.sendMessage(message1);
                    handler.sendMessage(message);
                    this.cancel();
                }
            }, 20 * s);
        }
        Message message = new Message();
        Bundle bundle = new Bundle();
        message.what = 3;
        bundle.putInt("num", num);
        message.setData(bundle);
        MainActivity.handler.sendMessage(message);//更改返回按钮事件
    }

    //关闭PlaceDetailsView详情的动画效果
    public void closeView() {
        for (int s = 0; s < 15; s++) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    Message message1 = new Message();
                    message.what = 2;
                    message1.what = 4;
                    PlaceDetailsContainerView.handler.sendMessage(message1);
                    handler.sendMessage(message);
                    this.cancel();
                }
            }, 20 * s);
        }
        Message message = new Message();
        message.what = 4;
        MainActivity.handler.sendMessage(message);//更改返回按钮事件
    }
}
