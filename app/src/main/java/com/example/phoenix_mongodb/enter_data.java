package com.example.phoenix_mongodb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.result.UpdateResult;

public class enter_data extends AppCompatActivity {
    int room_no = 0;
    EditText e1,e2,e3;
    int t,h,aq;
    Button b1;
    TextView t1,t2;
    String Appid = "phoenix_mongodb-haaqc";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        final App app = new App(new AppConfiguration.Builder(Appid).build());

        Credentials credentials = Credentials.emailPassword("kathuriabhinav@gmail.com","12345678");
        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess()){
                    Log.v("User", "Logged In Successfully");
                }else{
                    Log.v("User", "Failed to Login");
                }
            }
        });

        Intent intent = getIntent();
        room_no=intent.getIntExtra(room.MESSAGE_TEXT_INT,0);

        b1=(Button)findViewById(R.id.submit_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1 = (TextView)findViewById(R.id.textview_2);
                t1.setText("Room "+room_no);
                e1=(EditText)findViewById(R.id.temperature_edit_text);
                e2=(EditText)findViewById(R.id.humidity_edit_text);
                e3=(EditText)findViewById(R.id.air_quality_edit_text);
                t2=(TextView)findViewById(R.id.display_text_2);
                t= Integer.parseInt(e1.getText().toString());
                h= Integer.parseInt(e2.getText().toString());
                aq= Integer.parseInt(e3.getText().toString());

                User user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("phoenix_database");
                mongoCollection = mongoDatabase.getCollection("ac");

                if(aq<100)
                {
                    if(h>75)
                    {
                        if(t>=16 && t<=30)
                        {
                            if(t>24)
                            {
                                update_ac("ON",24,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","LOW","OFF");
                                update_ventilator("ON");

                            }else{
                                update_ventilator("ON");
                                update_ac("ON",24,"OFF");
                                update_fan("OFF",3);
                                update_heater("OFF","LOW","OFF");
                            }
                        }
                        else if(t>30)
                        {

                            if(t>40)
                            {
                                update_ac("ON",19,"ON");
                                update_ventilator("ON");
                                update_fan("ON",5);
                                update_heater("OFF","LOW","OFF");

                            }else{
                                update_ventilator("ON");
                                update_ac("ON",22,"ON");
                                update_fan("ON",4);
                                update_heater("OFF","LOW","OFF");
                            }

                        }
                        else if(t<16)
                        {

                            if(t<7)
                            {
                                update_ac("OFF",19,"OFF");
                                update_ventilator("ON");
                                update_fan("OFF",3);
                                update_heater("ON","HIGH","OFF");
                            }else{
                                update_ac("OFF",19,"OFF");
                                update_fan("OFF",3);
                                update_heater("ON","MEDIUM","OFF");
                                update_ventilator("ON");
                            }
                        }

                    }
                    else if(h<75)
                    {
                        if(t>=16 && t<=30)
                        {

                            if(t>24)
                            {
                                update_ac("ON",22,"OFF");
                                update_ventilator("ON");
                                update_fan("ON",3);
                                update_heater("OFF","MEDIUM","OFF");
                            }else{
                                update_ac("ON",24,"OFF");
                                update_ventilator("ON");
                                update_fan("ON",3);
                                update_heater("OFF","MEDIUM","OFF");
                            }

                        }
                        else if(t>30) {
                            if (t > 40) {
                                update_ac("ON",19,"ON");
                                update_ventilator("ON");
                                update_fan("ON",4);
                                update_heater("OFF","MEDIUM","OFF");
                            }else{
                                update_ac("ON",22,"ON");
                                update_ventilator("ON");
                                update_fan("ON",2);
                                update_heater("OFF","MEDIUM","OFF");
                            }
                        }
                        else if(t<16)
                        {
                            if(t<7)
                            {
                                update_ac("OFF",22,"OFF");
                                update_ventilator("ON");
                                update_fan("ON",1);
                                update_heater("ON","HIGH","OFF");
                            }else {
                                update_ac("OFF",22,"OFF");
                                update_ventilator("ON");
                                update_fan("ON",2);
                                update_heater("ON","MEDIUM","OFF");
                            }
                        }

                    }

                }
                else if(aq>100 && aq<=180)
                {
                    if(h>75)
                    {
                        if(t>=16 && t<=30)
                        {
                            if(t>24)
                            {
                                update_ac("ON",24,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","LOW","OFF");
                                update_ventilator("OFF");
                            }else {
                                update_ac("ON",24,"OFF");
                                update_fan("OFF",3);
                                update_heater("OFF","LOW","OFF");
                                update_ventilator("OFF");
                            }
                        }
                        else if(t>30)
                        {
                            if(t>40)
                            {
                                update_ac("ON",19,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","LOW","OFF");
                                update_ventilator("OFF");
                            }else{
                                update_ac("ON",22,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","LOW","OFF");
                                update_ventilator("OFF");
                            }
                        }
                        else if(t<16)
                        {
                            if(t<7)
                            {
                                update_ac("OFF",22,"OFF");
                                update_fan("ON",2);
                                update_heater("ON","HIGH","OFF");
                                update_ventilator("OFF");
                            }else {
                                update_ac("OFF",22,"OFF");
                                update_fan("ON",2);
                                update_heater("ON","LOW","OFF");
                                update_ventilator("OFF");
                            }
                        }

                    }
                    else if(h<75)
                    {
                        if(t>=16 && t<=30)
                        {
                            if(t>24)
                            {
                                update_ac("ON",24,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","LOW","OFF");
                                update_ventilator("OFF");
                            }else{
                                update_ac("ON",24,"OFF");
                                update_fan("OFF",2);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");
                            }
                        }
                        else if(t>30)
                        {
                            if(t>40)
                            {
                                update_ac("ON",19,"ON");
                                update_fan("OFF",2);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");

                            }else{
                                update_ac("ON",22,"OFF");
                                update_fan("ON",3);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");
                            }

                        }
                        else if(t<16)
                        {
                            if(t<7)
                            {
                                update_ac("OFF",24,"OFF");
                                update_fan("ON",2);
                                update_heater("ON","HIGH","OFF");
                                update_ventilator("OFF");
                            }else{
                                update_ac("OFF",24,"OFF");
                                update_fan("ON",2);
                                update_heater("ON","LOW","OFF");
                                update_ventilator("OFF");
                            }
                        }

                    }

                }
                else if(aq>180 && aq<400)
                {
                    if(h>75)
                    {
                        if(t>=16 && t<=30)
                        {
                            if(t>24)
                            {
                                update_ac("ON",24,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");
                            }else{
                                update_ac("ON",24,"OFF");
                                update_fan("OFF",2);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");
                            }
                        }
                        else if(t>30)
                        {
                            if(t>40)
                            {
                                update_ac("ON",19,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");

                            }else{
                                update_ac("ON",22,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");
                            }
                        }
                        else if(t<16)
                        {
                            if(t<7)
                            {
                                update_ac("OFF",22,"OFF");
                                update_fan("ON",2);
                                update_heater("ON","MEDIUM","OFF");
                                update_ventilator("OFF");
                            }else{
                                update_ac("OFF",22,"OFF");
                                update_fan("ON",2);
                                update_heater("ON","LOW","OFF");
                                update_ventilator("OFF");
                            }
                        }

                    }
                    else if(h<75)
                    {
                        if(t>=16 && t<=30)
                        {
                            if(t>24)
                            {
                                update_ac("ON",24,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");
                            }else{
                                update_ac("ON",24,"OFF");
                                update_fan("OFF",2);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");
                            }
                        }
                        else if(t>30)
                        {
                            if(t>40)
                            {
                                update_ac("ON",19,"OFF");
                                update_fan("ON",3);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");

                            }else{
                                update_ac("ON",22,"ON");
                                update_fan("ON",3);
                                update_heater("OFF","MEDIUM","OFF");
                                update_ventilator("OFF");
                            }
                        }
                        else if(t<16)
                        {
                            if(t<7)
                            {
                                update_ac("OFF",19,"OFF");
                                update_fan("OFF",2);
                                update_heater("ON","HIGH","OFF");
                                update_ventilator("OFF");
                            }else{
                                update_ac("OFF",19,"OFF");
                                update_fan("ON",2);
                                update_heater("ON","HIGH","OFF");
                                update_ventilator("OFF");
                            }
                        }

                    }

                }
                t2.setText("DEVICES UPDATED TO NEW VALUES");
            }
        });

    }

    void update_ac(String on_off, int temp, String swing) {
        String s = " ";

        Document queryFilter = (Document) new Document("room", room_no).append("device_type",1);
        Document updateDocument = new Document("on_off", on_off)
                .append("room",room_no)
                .append("device_type",1)
                .append("temperature",temp)
                .append("device_name","AC")
                .append("swing",swing);
        mongoCollection.updateMany(queryFilter, updateDocument).getAsync(new App.Callback<UpdateResult>() {
            @Override
            public void onResult(App.Result<UpdateResult> task) {
                if (task.isSuccess()) {
                    long count = task.get().getModifiedCount();
                    if (count != 0) {
                        Log.v("EXAMPLE", "successfully updated " + count + " ac documents.");
                    } else {
                        Log.v("EXAMPLE", "did not update any documents.");
                    }
                } else {
                    Log.e("EXAMPLE", "failed to update documents with: ", task.getError());
                }
            }
        });

    }

    void update_fan(String on_off, int speed) {

        Document queryFilter = new Document("room", room_no).append("device_type",2);
        Document updateDocument = new Document("on_off", on_off)
                .append("room",room_no)
                .append("device_type",2)
                .append("device_name","FAN")
                .append("speed",speed);
        mongoCollection.updateMany(queryFilter, updateDocument).getAsync(new App.Callback<UpdateResult>() {
            @Override
            public void onResult(App.Result<UpdateResult> task) {
                if (task.isSuccess()) {
                    long count = task.get().getModifiedCount();
                    if (count != 0) {
                        Log.v("EXAMPLE", "successfully updated " + count + " fan documents.");
                    } else {
                        Log.v("EXAMPLE", "did not update any documents.");
                    }
                } else {
                    Log.e("EXAMPLE", "failed to update documents with: ", task.getError());
                }
            }
        });

    }

    void update_heater(String on_off, String mode, String swing) {

        Document queryFilter = new Document("room", room_no).append("device_type",3);
        Document updateDocument = new Document("on_off", on_off)
                .append("room",room_no)
                .append("device_type",3)
                .append("mode",mode)
                .append("device_name","HEATER")
                .append("swing",swing);
        mongoCollection.updateMany(queryFilter, updateDocument).getAsync(new App.Callback<UpdateResult>() {
            @Override
            public void onResult(App.Result<UpdateResult> task) {
                if (task.isSuccess()) {
                    long count = task.get().getModifiedCount();
                    if (count != 0) {
                        Log.v("EXAMPLE", "successfully updated " + count + " heater documents.");
                    } else {
                        Log.v("EXAMPLE", "did not update any documents.");
                    }
                } else {
                    Log.e("EXAMPLE", "failed to update documents with: ", task.getError());
                }
            }
        });

    }

    void update_ventilator(String on_off) {

        Document queryFilter = new Document("room", room_no).append("device_type",4);
        Document updateDocument = new Document("on_off", on_off)
                .append("room",room_no)
                .append("device_name","VENTILATOR")
                .append("device_type",4);
        mongoCollection.updateMany(queryFilter, updateDocument).getAsync(new App.Callback<UpdateResult>() {
            @Override
            public void onResult(App.Result<UpdateResult> task) {
                if (task.isSuccess()) {
                    long count = task.get().getModifiedCount();
                    if (count != 0) {
                        Log.v("EXAMPLE", "successfully updated " + count + " vent documents.");
                    } else {
                        Log.v("EXAMPLE", "did not update any documents.");
                    }
                } else {
                    Log.e("EXAMPLE", "failed to update documents with: ", task.getError());
                }
            }
        });

    }

}
