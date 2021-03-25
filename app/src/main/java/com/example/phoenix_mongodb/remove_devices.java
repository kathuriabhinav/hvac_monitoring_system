package com.example.phoenix_mongodb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.result.DeleteResult;

public class remove_devices extends AppCompatActivity {
    TextView textview1;
    int room_no = 0;
    RadioGroup radioGroup;
    Button button;
    String s;
    RadioButton radioButton;
    String Appid = "phoenix_mongodb-haaqc";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_devices);

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
        room_no = intent.getIntExtra(add_devices.MESSAGE_TEXT_INT, 0);
        textview1 = (TextView) findViewById(R.id.room_no_info_of_remove_devices);
        textview1.setText("Room " + room_no);
        textView2=(TextView)findViewById(R.id.display_text);
        radioGroup= (RadioGroup) findViewById(R.id.radio_button_group_of_remove_devices);

        User user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("phoenix_database");
        mongoCollection = mongoDatabase.getCollection("ac");
        Document queryFilter = new Document().append("room",room_no);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
        findTask.getAsync(new App.Callback<MongoCursor<Document>>() {
            @Override
            public void onResult(App.Result<MongoCursor<Document>> task) {
                if (task.isSuccess()) {
                    MongoCursor<Document> results = task.get();
                    if (!results.hasNext()) {
                        Log.v("Result", "Couldnt Find");
                    }
                    while (results.hasNext()) {
                        Document currentDoc = results.next();
                        int x = currentDoc.getInteger("device_type");
                        String s3 = currentDoc.getString("device_name");
                        add_radio_button(s3,x);
                    }
                } else {
                    Log.v("Task Error", task.getError().toString());
                }

            }
        });

        button = (Button)findViewById(R.id.remove_device_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedID);
                System.out.println(selectedID);
                s = radioButton.getText().toString();
                Document queryFilter = new Document("device_name", s).append("room", room_no);
                mongoCollection.deleteOne(queryFilter).getAsync(new App.Callback<DeleteResult>() {
                    @Override
                    public void onResult(App.Result<DeleteResult> task) {
                        if (task.isSuccess()) {
                            long count = task.get().getDeletedCount();
                            if (count == 1) {
                                Log.v("EXAMPLE", "successfully deleted a document.");
                                textView2.setText(s+" removed");
                            } else {
                                Log.v("EXAMPLE", "did not delete a document.");
                            }
                        } else {
                            Log.e("EXAMPLE", "failed to delete document with: ", task.getError());
                        }
                    }
                });
            }
        });
    }

    void add_radio_button(String name, int device_type){
        RadioButton radioButton2 = new RadioButton(this);
        System.out.println(name+" radio button of device type "+device_type+" is created");
        radioButton2.setText(name);
        radioGroup.addView(radioButton2);
    }

}
