package team.t404.gotravel.thread;

import android.os.Bundle;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

import team.t404.gotravel.model.Place;

public class findPlacesByPraise extends Thread{

    private String result;
    public Handler mHandler;

    public findPlacesByPraise(String result) throws JSONException {
        JSONObject jsonObject=new JSONObject(result);
        JSONArray jsonArray=jsonObject.getJSONArray("places");
        JSONObject jsonObjectPlace;
        jsonObjectPlace=jsonArray.getJSONObject(0);
        jsonObjectPlace.getString("address");//地址
        jsonObjectPlace.getString("longitude_latitude");//经纬度
        jsonObjectPlace.getJSONArray("customization");//用户标签
        jsonObjectPlace.getString("introduce");//介绍
        jsonObjectPlace.getJSONArray("place_type");//类型标签
        jsonObjectPlace.getString("name");//名称
        jsonObjectPlace.getJSONArray("picture");//图片
        jsonObjectPlace.getInt("praise");//好评数
        jsonObjectPlace.getInt("place_id");//ID
        jsonObjectPlace.getJSONArray("hobby");//爱好标签
        Message msg=new Message();
        msg.what=1;
        Bundle bundle=new Bundle();

    }

    public void run(){

    }
}
