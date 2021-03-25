package com.example.phoenix_mongodb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE_TEXT = "com.example.phoenix.example.message";
    private RelativeLayout room1,room2,room3,room4;
    private Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);

        room1 = (RelativeLayout)findViewById(R.id.room_1);
        room2 = (RelativeLayout)findViewById(R.id.room_2);
        room3 = (RelativeLayout)findViewById(R.id.room_3);
        room4 = (RelativeLayout)findViewById(R.id.room_4);
        room1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivityroom("1");
            }
        });
        room2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivityroom("2");
            }
        });
        room3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivityroom("3");
            }
        });
        room4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivityroom("4");
            }
        });

        b1 = (Button)findViewById(R.id.test_button);
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivitytest();
            }
        });

    }
    public void openActivityroom(String x){
        Intent intent = new Intent(this, room.class);
        intent.putExtra(MESSAGE_TEXT, x);
        startActivity(intent);
    }
    public void openActivitytest(){
        Intent intent = new Intent(this, test.class);
        startActivity(intent);
    }

}
