package com.example.networkdemo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button URL;
    private Button OK;
    private Button Three;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL=findViewById(R.id.bt_url);
        OK=findViewById(R.id.bt_ok);
        Three=findViewById(R.id.bt_three);

        URL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,VisitActivity.class);
                startActivity(intent);
            }
        });

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,OkVisitActivity.class);
                startActivity(intent);
            }
        });
        Three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ThreeVisitActivity.class);
                startActivity(intent);
            }
        });
    }
}
