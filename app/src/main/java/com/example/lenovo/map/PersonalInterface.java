package com.example.lenovo.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PersonalInterface extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_interface);
    }

    //导航栏全部单击事件
    public void clickHome(View view) {//首页
        Intent it = new Intent(PersonalInterface.this, MainActivity.class);
        startActivity(it);
        finish();
    }

    public void clickForum(View view) {//论坛

    }

    public void clickPersonal(View view) {//个人
//        Intent it = new Intent(MainActivity.this, PersonalInterface.class);
//        startActivity(it);
//        finish();
    }
}
