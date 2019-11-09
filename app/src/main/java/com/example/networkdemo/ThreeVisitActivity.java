package com.example.networkdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ThreeVisitActivity extends AppCompatActivity {
    private TextView Three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_visit);

        Three=findViewById(R.id.tv_three);
    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_oil:
                break;
            case R.id.bt_new:
                break;
            case R.id.bt_weather:
                break;
        }
    }
}
