package team.t404.gotravel.listeren;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

/**   
 * 项目名称:  走·旅行
 * 包:        team.t404.gotravel.listeren  
 * 类名称:    GTMapLocationListener
 * 类描述:    用于监听用户定位的改变，并在地图上把定位点描绘出来
 * 创建人:    梁其兴
 * 创建时间:  2019/9/12 17:24  
 * 修改人:    梁其兴
 * 修改时间:  2019/9/12 17:24
 * 修改备注:  [说明本次修改内容]
 * 版本:      [v1.0]
 */

public class GTMapLocationListener implements AMapLocationListener {

    private AMap aMap;
    private AMapLocationClient mLocationClient;//定位客户端
    private Marker marker;//定位标记
    private String address;//定位地址描述
    private String Latitude;//定位纬度
    private String Longitude;//定位经度
    private CameraUpdate cu;//定位经纬度
    private boolean location_tracking = false;//判断是否跟踪定位

    public AMapLocationClient getMLocationClient() {
        return mLocationClient;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLocation_tracking(boolean location_tracking) {
        this.location_tracking = location_tracking;
    }

    /** 
     * 方法名称:  走·旅行地图定位监听器
     * 方法描述:  对定位改变监听器作初始化设置
     * 参数说明： [说明参数的作用]
     * applicationContext： 应用程序的环境
     * activityContext：    MainActivity的环境
     * aMap：               高德地图的AMap对象
     * 返回描述： null
     * 创建人:    梁其兴
     * 创建时间:  2019/9/12 17:43  
     * 修改人:    梁其兴
     * 修改时间:  2019/9/12 17:43
     * 修改备注:  null
     * 建立版本:  [v1.0]
     */
    public GTMapLocationListener(Context applicationContext, Context activityContext, AMap aMap) {
        //获取AMap对象
        this.aMap = aMap;
        /*
        加载应用最后退出时的定位
        SharedPreferences存储用户关闭应用时的定位地点
        */
        SharedPreferences preferences = activityContext.getSharedPreferences("lal", Context.MODE_PRIVATE);
        updatePosition(preferences.getString("Latitude", "23.13"), preferences.getString("Longitude", "113.27"));
        aMap.moveCamera(cu);
        //初始化定位
        mLocationClient = new AMapLocationClient(applicationContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();

        /*
        获取最近3秒内精度最高的一次定位结果：
        设置setOnceLocationLatest(boolean b)接口为true，
        启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，
        反之不会，默认为false。
        */
        mLocationOption.setOnceLocation(false);//获取一次定位结果，该方法默认为false。
        mLocationOption.setOnceLocationLatest(false);//返回最近3s内精度最高的一次定位结果，默认为false

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /** 
     * 方法名称:  地图定位改变监听器
     * 方法描述:  每隔一小段时间就会返回一次用户当前位置的地理信息
     * 参数说明： [说明参数的作用]
     * aMapLocation： 当前定位的信息对象，可以获取地理信息
     * 返回描述： null
     * 创建人:    梁其兴
     * 创建时间:  2019/9/12 17:59  
     * 修改人:    梁其兴
     * 修改时间:  2019/9/12 17:59
     * 修改备注:  [说明本次修改内容]
     * 建立版本:  [v1.0]
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                updatePosition(aMapLocation.getLatitude() + "", aMapLocation.getLongitude() + "");
                address = aMapLocation.getAddress();//获取定位地点地址描述
                Latitude = aMapLocation.getLatitude() + "";//获取定位地点纬度值
                Longitude = aMapLocation.getLongitude() + "";//获取定位地点经度值
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }

    //地图视角移动到定位点
    public void positioningCenter() {
        aMap.moveCamera(cu);
    }

    //定位图标对象
    private void updatePosition(String a, String b) {
        double ad = Double.parseDouble(a);//定位纬度
        double bd = Double.parseDouble(b);//定位经度
        LatLng pos = new LatLng(ad, bd);//定位经纬度
        //创建一个设置经纬度的CameraUpdate
        cu = CameraUpdateFactory.changeLatLng(pos);
        //更新地图的显示区域，通过location_tracking变量控制是否进行定位视图跟踪
        if (location_tracking) {
            aMap.moveCamera(cu);
        }
        //清除定位Marker覆盖物
        if (marker != null) {
            marker.remove();
        }
        //创建一个MarkerOptions对象
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        //添加MarkerOptions(实际上是添加Marker)
        marker = aMap.addMarker(markerOptions);
    }

}
