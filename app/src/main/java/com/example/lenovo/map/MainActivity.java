package com.example.lenovo.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import team.t404.gotravel.Interface.RequestServicesTool;
import team.t404.gotravel.listeren.GTMapLocationListener;
import team.t404.gotravel.view.ButtonImageRound;
import team.t404.gotravel.view.PlaceDetailsContainerView;
import team.t404.gotravel.view.PlaceDetailsView;
import team.t404.gotravel.view.SearchBarView;
import team.t404.gotravel.view.ButtonChangeTextRound;
import team.t404.gotravel.view.TitleView;

public class MainActivity extends Activity implements GeocodeSearch.OnGeocodeSearchListener {
    private MapView mapView;
    private AMap aMap;
    private ArrayList<PlaceDetailsView> placeDetailsView=new ArrayList<PlaceDetailsView>();
    private LocationManager locationManager;
    private Context context;
    private TextView recommend_content;
    private Button return_superior;
    private ButtonImageRound location_my, returnSuperior;
    private ButtonChangeTextRound recommend;
    private RelativeLayout component, recommend_hot_window_1;
    private PlaceDetailsContainerView recommend_hot_window;
    private SearchBarView component_1;
    private TitleView titleView;
    private LinearLayout component_2, component_3, component_3_2, component_4, navigation_bar;
    private AutoCompleteTextView addrTv;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private CameraUpdate cu;//定位经纬度
    private String address;//定位地址描述
    private Marker marker;//定位标记
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private GestureDetector detector1, detector2;//手势检测器
    private boolean location_tracking = false;//是否跟随定位移动判断
    private long firstClick = 0;//第一次点击时间
    private long secondClick = 0;//第二次点击时间
    private int screenWidth, screenHeight, component_height, n, m, s = 0, h = 0, x = 0, p = 0;//用于计时的参考数
    private Timer timer;
    public static Handler handler;
    GTMapLocationListener GTMapLL;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //状态栏半透明化
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);

        //获取手机屏幕高度宽度
        DisplayMetrics dm = new DisplayMetrics();//定义DisplayMetrics对象
        getWindowManager().getDefaultDisplay().getMetrics(dm);//取得窗口属性
        screenWidth = dm.widthPixels;//手机屏幕宽度
        screenHeight = dm.heightPixels;//手机屏幕高度

        context = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //component = (RelativeLayout) findViewById(R.id.component);
        mapView = (MapView) findViewById(R.id.map);
        //必须回调MapView的onCreate()方法
        mapView.onCreate(savedInstanceState);
        init();
        GTMapLL = new GTMapLocationListener(getApplicationContext(), this, aMap);

        //监听组件高度和宽度
//        ViewTreeObserver vto = component.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                component.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                component_height = component.getHeight();
//                component.getWidth();
//            }
//        });

        //创建手势检测器
        detector1 = new GestureDetector(mapView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override//手势滑动
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                GTMapLL.setLocation_tracking(false);//取消地图跟随定位点移动
                location_my.setButtonImage(R.drawable.location_1);
                return true;
            }

            @Override//单击屏幕，双击不会回调
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }

        });
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {//当地图发生触碰事件时回调
            @Override
            public void onTouch(MotionEvent motionEvent) {
                detector1.onTouchEvent(motionEvent);
            }
        });

        //地图交互对象
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);//地图显示大小级按钮

        //锁定自身定位
        location_my = (ButtonImageRound) findViewById(R.id.location_my);
        location_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GTMapLL.positioningCenter();//地图视角移动到定位点
                GTMapLL.setLocation_tracking(true);//地图跟随定位点移动
                location_my.setButtonImage(R.drawable.location_2);
                Toast.makeText(MainActivity.this, address, Toast.LENGTH_LONG).show();
            }
        });

        //推荐/热门按钮
        recommend = (ButtonChangeTextRound) findViewById(R.id.recommend);
        returnSuperior = (ButtonImageRound) findViewById(R.id.returnSuperior);
        return_superior = (Button) findViewById(R.id.return_superior);
        recommend_hot_window = (PlaceDetailsContainerView) findViewById(R.id.recommend_hot_window);
        component_1 = (SearchBarView) findViewById(R.id.component_1);
        titleView = (TitleView) findViewById(R.id.titleView);
        navigation_bar = (LinearLayout) findViewById(R.id.navigation_bar);
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView1));
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView2));
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView3));
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView4));
        placeDetailsView.add((PlaceDetailsView) findViewById(R.id.placeDetailsView5));

        handler = new Handler() {//接收findPlacesByPraise方法的服务器反馈
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    timer.cancel();
                    recommend_hot_window.openView();
                    component_1.closeView();
                    returnSuperior.setVisibility(View.VISIBLE);
                    returnSuperior.openView();
                    recommend.invisibleView();
                    //关闭窗口事件
                    returnSuperior.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recommend.setClickable(true);
                            recommend.openView();
                            recommend_hot_window.closeView();
                            component_1.openView();
                            returnSuperior.setVisibility(View.GONE);
                        }
                    });
                } else if (msg.what == 2) {
                    Toast.makeText(MainActivity.this, msg.getData().getString("错误"), Toast.LENGTH_LONG).show();
                } else if (msg.what == 3) {//更改返回按钮的事件内容
                    location_my.setVisibility(View.GONE);//隐藏定位按钮
                    final int num = msg.getData().getInt("num");
                    returnSuperior.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            placeDetailsView.get(num).closeView();
                        }
                    });
                } else if (msg.what == 4) {
                    location_my.setVisibility(View.VISIBLE);
                    returnSuperior.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recommend.setClickable(true);
                            recommend.openView();
                            recommend_hot_window.closeView();
                            component_1.openView();
                            returnSuperior.setVisibility(View.GONE);
                        }
                    });
                }
                super.handleMessage(msg);
            }
        };

        //点击打开窗口事件
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommend.setClickable(false);
                RequestServicesTool.findPlacesByPraise();
                timer = new Timer(true);
                for (s = 0; s < 20; s++) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            recommend.closeView();
                            this.cancel();
                        }
                    }, 1000 * s);
                }
            }
        });

//        //创建手势检测器
//        detector2 = new GestureDetector(recommend_hot_window.getContext(), new GestureDetector.SimpleOnGestureListener() {
//            @Override//手势滑动
//            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                int h1 = dipTopx(context, h + e1.getRawY() - e2.getRawY());
//                if (e1.getRawY() - e2.getRawY() > 5) {
//                    title.setVisibility(View.VISIBLE);
//                    Toast.makeText(MainActivity.this, h1 + "", Toast.LENGTH_LONG).show();
//                    recommend_hot_window_1.setVisibility(View.VISIBLE);
//                    recommend_hot_window_1.getLayoutParams().height = h1;
//                    recommend_hot_window.getLayoutParams().height = h1;
//                    recommend_hot_window_1.setLayoutParams(recommend_hot_window_1.getLayoutParams());
//                    recommend_hot_window.setLayoutParams(recommend_hot_window.getLayoutParams());
//                    return true;
//                } else if (e2.getRawY() - e1.getRawY() > 5) {
//                    recommend_hot_window_1.setVisibility(View.VISIBLE);
//                    recommend_hot_window_1.getLayoutParams().height = h1;
//                    recommend_hot_window.getLayoutParams().height = h1;
//                    recommend_hot_window_1.setLayoutParams(recommend_hot_window_1.getLayoutParams());
//                    recommend_hot_window.setLayoutParams(recommend_hot_window.getLayoutParams());
//                    return true;
//                }
//                return false;
//            }
//
//            @Override//单击屏幕，双击不会回调
//            public boolean onSingleTapConfirmed(MotionEvent e) {
//                return true;
//            }
//        });

//        recommend_hot_window.setLongClickable(true);
//        recommend_hot_window.setOnTouchListener(new View.OnTouchListener() {//设置景点介绍窗口的手势监听器
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return detector2.onTouchEvent(event);
//            }
//        });

        //ImageView bn1 = (ImageView) findViewById(R.id.loc2);
        //addrTv = (AutoCompleteTextView) findViewById(R.id.search);

//        bn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String addr = addrTv.getText().toString().trim();
//                PoiSearch.Query query = new PoiSearch.Query(addr, "", "");
//                query.setPageSize(10);// 设置每页最多返回多少条poiitem
//                query.setPageNum(1);//设置查询页码
//                PoiSearch poiSearch = new PoiSearch(context, query);
//                poiSearch.searchPOIAsyn();
//                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
//                    @Override
//                    public void onPoiSearched(PoiResult poiResult, int i) {
//                        ArrayList<PoiItem> pois = poiResult.getPois();
//                        LatLonPoint pos = pois.get(0).getLatLonPoint();
//                        LatLng targetPos = new LatLng(pos.getLatitude(), pos.getLongitude());
//                        //创建一个设置经纬度的CameraUpdate
//                        CameraUpdate cu = CameraUpdateFactory.changeLatLng(targetPos);
//                        //创建一个设置放大级别的CameraUpdate
//                        CameraUpdate cu1 = CameraUpdateFactory.zoomTo(16);
//                        //清除原本绘图痕迹
//                        aMap.clear();
//                        //更新地图的显示区域
//                        aMap.moveCamera(cu1);
//                        aMap.moveCamera(cu);
//                        //创建一个CircleOptions(用于向地图上添加圆形)
//                        CircleOptions cOptions = new CircleOptions().center(targetPos)          //设置圆心
//                                .fillColor(0x80ffff00)      //设置圆形的填充颜色
//                                .radius(80)                 //设置圆形的半径
//                                .strokeWidth(1)             //设置圆形的线条宽度
//                                .strokeColor(0xff000000);   //设置圆形的线条颜色
//                        aMap.addCircle(cOptions);
//                        Toast.makeText(MainActivity.this, pois.get(0).getSnippet(), Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//                    }
//                });
//            }
//        });
        //当输入框文字改变后，触发搜索提示
//        addrTv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String addr = addrTv.getText().toString().trim();
//                //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
//                InputtipsQuery inputquery = new InputtipsQuery(addr, "");
//                inputquery.setCityLimit(true);//限制在当前城市
//                Inputtips inputTips = new Inputtips(context, inputquery);
//                inputTips.requestInputtipsAsyn();
//                inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
//                    @Override
//                    public void onGetInputtips(List<Tip> list, int i) {
//                        int num = list.toArray().length;
//                        //Toast.makeText(MainActivity.this, num + ":" + i, Toast.LENGTH_LONG).show();
//                        String[] listName = new String[num];
//                        for (i = 0; i < num; i++) {
//                            listName[i] = list.get(i).getName();
//                        }
//                        ArrayAdapter<String> aa = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, listName);
//                        addrTv.setAdapter(aa);
//                        addrTv.showDropDown();
//                    }
//                });
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        //焦点重新返回输入框时，触发搜索提示
//        addrTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addrTv.showDropDown();
//            }
//        });
    }

    //初始化AMap对象
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            //创建一个设置放大级别的CameraUpdate
            CameraUpdate cu = CameraUpdateFactory.zoomTo(16);
            //设置地图的默认放大级别
            aMap.moveCamera(cu);
            //创建一个更改地图倾斜度的CameraUpdate
            CameraUpdate tiltUpdate = CameraUpdateFactory.changeTilt(30);
            //改变地图倾斜度
            aMap.moveCamera(tiltUpdate);
        }
    }

    //导航栏全部单击事件
    public void clickHome(View view) {//首页
//        Intent it=new Intent(MainActivity.this,PersonalInterface.class);
//        startActivity(it);
//        finish();
    }

    public void clickForum(View view) {//论坛

    }

    public void clickPersonal(View view) {//个人
        Intent it = new Intent(MainActivity.this, PersonalInterface.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //必须回调MapView的onResume()方法
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //必须回调MapView的onPause()方法
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //必须回调MapView的onSaveInstanceState()方法
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferences = getSharedPreferences("lal", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("Latitude", GTMapLL.getLatitude());
        editor.putString("Longitude", GTMapLL.getLongitude());
        editor.apply();
        GTMapLL.getMLocationClient().onDestroy();
        //必须回调MapView的onDestroy()方法
        mapView.onDestroy();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (i == 1000) {
            //获取解析得到的第一个地址
            GeocodeAddress geo = geocodeResult.getGeocodeAddressList().get(0);
            GeocodeQuery geocodeQuery = geocodeResult.getGeocodeQuery();
            //获取解析得到的经纬度
            String string = geo.getBuilding();
            String string1 = geocodeQuery.getLocationName();
            Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
            LatLonPoint pos = geo.getLatLonPoint();
            LatLng targetPos = new LatLng(pos.getLatitude(), pos.getLongitude());
            aMap.clear();
            //创建一个设置经纬度的CameraUpdate
            CameraUpdate cu = CameraUpdateFactory.changeLatLng(targetPos);
            //更新地图的显示区域
            aMap.moveCamera(cu);
            //创建一个CircleOptions(用于向地图上添加圆形)
            CircleOptions cOptions = new CircleOptions().center(targetPos)          //设置圆心
                    .fillColor(0x80ffff00)      //设置圆形的填充颜色
                    .radius(80)                 //设置圆形的半径
                    .strokeWidth(1)             //设置圆形的线条宽度
                    .strokeColor(0xff000000);   //设置圆形的线条颜色
            aMap.addCircle(cOptions);
        } else {
            Toast.makeText(MainActivity.this, "无法查询到结果", Toast.LENGTH_LONG).show();
        }
    }

    private void GroundOverlayOptions() {

    }

    //根据手机的分辨率从dp的单位转成为px(像素)
    public int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}