package team.t404.gotravel.Interface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.lenovo.map.RequestServices;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import team.t404.gotravel.view.PlaceDetailsContainerView;

public class RequestServicesTool {

    //private static String url="http://172.16.154.124:8080/GoTravel/";
    private static String url="http://47.103.121.20:8080/gotravel-1.0.0/";
    //private static String url="http://192.168.43.49:8080/GoTravel/";
    //获取Retrofit对象，设置地址
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()) // 加上这句话
            .build();

    public static void findPlacesByPraise() {
        //通过Retrofit实例创建接口服务对象
        final RequestServices requestServices = retrofit.create(RequestServices.class);
        //接口服务对象调用接口中方法，获得Call对象
        Call<ResponseBody> call = requestServices.findPlacesByPraise(5, 113.324535,23.10642);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Message message = new Message();
                        message.what=1;
                        Bundle bundle=new Bundle();
                        bundle.putString("findPlacesByPraise",result);
                        message.setData(bundle);
                        PlaceDetailsContainerView.findPlacesByPraiseHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Message message = new Message();
                    Bundle data=new Bundle();
                    data.putString("错误","访问服务器失败");
                    message.setData(data);
                    message.what = 2;
                    //handler1.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("test", "访问失败！");
            }
        });

    }
}
