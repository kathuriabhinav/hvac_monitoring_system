package com.example.phoenix_mongodb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.result.InsertOneResult;

public class add_devices extends AppCompatActivity {
    public static final String MESSAGE_TEXT_INT = "com.example.phoenix.example.message_num";
    private Button button;
    private TextView textView, textView2, textView3;
    private EditText editText;
    public RadioGroup radioGroup;
    public RadioButton radioButton;
    public String device_name, s;
    int room_no = 0;
    String Appid = "phoenix_mongodb-haaqc";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_devices);

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
        room_no = intent.getIntExtra(room.MESSAGE_TEXT_INT, 0);
        textView = (TextView) findViewById(R.id.textview_1);
        textView.setText("Room " + room_no);
        textView2 = (TextView) findViewById(R.id.remove_device_textview);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_remove_a_device_activity(room_no);
            }
        });
        textView3 = (TextView) findViewById(R.id.test);

        Button button = (Button) findViewById(R.id.add_device_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText = (EditText) findViewById(R.id.device_name_edit_text);
                device_name = editText.getText().toString();
                radioGroup = (RadioGroup) findViewById(R.id.radio_button_group);
                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedID);
                System.out.println(selectedID);
                s = radioButton.getText().toString();
                System.out.println("string is :'"+s+"'");
                User user = app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("phoenix_database");
                System.out.println("Document to be inserted");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("ac");
                Document document = new Document();
                document.append("device_name", device_name);
                document.append("room", room_no);
                document.append("on_off", "OFF");
                if(s.equals("AC")){
                    document.append("device_type", 1);
                    document.append("temperature", 27);
                    document.append("swing", "OFF");
                }else if(s.equals("FAN")){
                    document.append("device_type", 2);
                    document.append("speed", 0);
                }else if(s.equals("HEATER")){
                    document.append("device_type", 3);
                    document.append("mode", "MEDIUM");
                    document.append("swing", "OFF");
                }else if(s.equals("VENTILATOR")){
                    document.append("device_type", 4);
                }
                mongoCollection.insertOne(document).getAsync(new App.Callback<InsertOneResult>() {
                    @Override
                    public void onResult(App.Result<InsertOneResult> result) {
                        if (result.isSuccess()) {
                            Log.v("Data", "Data Inserted Successfully");
                        } else {
                            Log.v("Data", "Error:" + result.getError().toString());
                        }
                    }
                });
                textView3.setText(device_name+" ADDED");
            }
        });

    }
    void open_remove_a_device_activity(int x) {
        Intent intent = new Intent(this, remove_devices.class);
        intent.putExtra(MESSAGE_TEXT_INT, x);
        startActivity(intent);
    }
}
