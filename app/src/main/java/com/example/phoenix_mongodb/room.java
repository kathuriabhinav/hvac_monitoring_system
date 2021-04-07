package com.example.phoenix_mongodb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class room extends AppCompatActivity {
    int room_no = 0;
    public static final String MESSAGE_TEXT_INT = "com.example.phoenix.example.message_num";
    private Button b1, b2;
    TextView textview1;
    String Appid = "phoenix_mongodb-haaqc";
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

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
        String text = intent.getStringExtra(MainActivity.MESSAGE_TEXT);
        room_no = Integer.parseInt(text);
        text = "Room " + text;
        textview1 = (TextView) findViewById(R.id.room_no_info);
        textview1.setText(text);


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
                        if (x == 1) {
                            add_ac(currentDoc.getString("device_name")
                                    , currentDoc.getInteger("temperature")
                                    , currentDoc.getString("on_off")
                                    , currentDoc.getString("swing"));
                        } else if (x == 2) {
                            add_fan(currentDoc.getString("device_name")
                                    ,currentDoc.getString("on_off")
                                    ,currentDoc.getInteger("speed") );
                        } else if (x == 3) {
                            add_heater(currentDoc.getString("device_name")
                                    ,currentDoc.getString("mode")
                                    ,currentDoc.getString("on_off")
                                    ,currentDoc.getString("swing"));
                        } else if (x == 4) {
                            add_ventilator(currentDoc.getString("device_name")
                                    ,currentDoc.getString("on_off"));
                        }
                    }
                } else {
                    Log.v("Task Error", task.getError().toString());
                }

            }
        });

        b1 = (Button) findViewById(R.id.edit_devices_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_edit_devices_activity(room_no);
            }
        });
        b2 = (Button) findViewById(R.id.enter_data_button);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_enter_data_activity(room_no);
            }
        });

    }

    public void add_ac(String name, int temperature, String on_off, String swing) {
        LinearLayout parent = (LinearLayout) findViewById(R.id.linear_layout_of_room);
        parent.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 10;
        float density = getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);

        LinearLayout ac_layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams wrapcontentparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ac_layout.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        ac_layout.setLayoutParams(params);
        ac_layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.ac_icon);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(12 * paddingPixel, 12 * paddingPixel);
        image.setLayoutParams(layoutParams);
        image.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        ac_layout.addView(image);

        LinearLayout texts = new LinearLayout(this);
        texts.setPadding(3 * paddingPixel, 0, 0, 0);
        texts.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 12 * paddingPixel));
        texts.setOrientation(LinearLayout.VERTICAL);

        TextView ac_name = new TextView(this);
        ac_name.setLayoutParams(wrapcontentparams);
        ac_name.setText(name);
        ac_name.setTextSize(25);
        ac_name.setTextColor(getResources().getColor(android.R.color.black));
        texts.addView(ac_name);

        TextView ac_on_off = new TextView(this);
        ac_on_off.setText(on_off);
        ac_on_off.setLayoutParams(wrapcontentparams);
        ac_on_off.setTextColor(getResources().getColor(android.R.color.black));
        ac_on_off.setTextSize(15);
        texts.addView(ac_on_off);

        TextView ac_temperature = new TextView(this);
        ac_temperature.setText("Temperature : " + temperature);
        ac_temperature.setLayoutParams(wrapcontentparams);
        ac_temperature.setTextSize(15);
        ac_temperature.setTextColor(getResources().getColor(android.R.color.black));
        texts.addView(ac_temperature);

        TextView ac_swing = new TextView(this);
        ac_swing.setLayoutParams(wrapcontentparams);
        ac_swing.setText("Swing : " + swing);
        ac_swing.setTextColor(getResources().getColor(android.R.color.black));
        ac_swing.setTextSize(15);
        texts.addView(ac_swing);

        ac_layout.addView(texts);

        parent.addView(ac_layout);
    }

    public void add_heater(String name, String mode, String on_off, String swing) {
        LinearLayout parent = (LinearLayout) findViewById(R.id.linear_layout_of_room);
        parent.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 10;
        float density = getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);

        LinearLayout heater_layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams wrapcontentparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        heater_layout.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        heater_layout.setLayoutParams(params);
        heater_layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.heater_icon);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(12 * paddingPixel, 12 * paddingPixel);
        image.setLayoutParams(layoutParams);
        image.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        heater_layout.addView(image);

        LinearLayout texts = new LinearLayout(this);
        texts.setPadding(3 * paddingPixel, 0, 0, 0);
        texts.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 12 * paddingPixel));
        texts.setOrientation(LinearLayout.VERTICAL);

        TextView heater_name = new TextView(this);
        heater_name.setLayoutParams(wrapcontentparams);
        heater_name.setText(name);
        heater_name.setTextSize(25);
        heater_name.setTextColor(getResources().getColor(android.R.color.black));
        texts.addView(heater_name);

        TextView heater_on_off = new TextView(this);
        heater_on_off.setText(on_off);
        heater_on_off.setLayoutParams(wrapcontentparams);
        heater_on_off.setTextColor(getResources().getColor(android.R.color.black));
        heater_on_off.setTextSize(15);
        texts.addView(heater_on_off);

        TextView heater_mode = new TextView(this);
        heater_mode.setText("Mode : " + mode);
        heater_mode.setLayoutParams(wrapcontentparams);
        heater_mode.setTextSize(15);
        heater_mode.setTextColor(getResources().getColor(android.R.color.black));
        texts.addView(heater_mode);

        TextView heater_swing = new TextView(this);
        heater_swing.setLayoutParams(wrapcontentparams);
        heater_swing.setText("Swing : " + swing);
        heater_swing.setTextColor(getResources().getColor(android.R.color.black));
        heater_swing.setTextSize(15);
        texts.addView(heater_swing);

        heater_layout.addView(texts);

        parent.addView(heater_layout);
    }

    public void add_ventilator(String name, String on_off) {
        LinearLayout parent = (LinearLayout) findViewById(R.id.linear_layout_of_room);
        parent.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 10;
        float density = getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);

        LinearLayout ventilator_layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams wrapcontentparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ventilator_layout.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        ventilator_layout.setLayoutParams(params);
        ventilator_layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.ventilator_icon);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(12 * paddingPixel, 12 * paddingPixel);
        image.setLayoutParams(layoutParams);
        image.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        ventilator_layout.addView(image);

        LinearLayout texts = new LinearLayout(this);
        texts.setPadding(3 * paddingPixel, 0, 0, 0);
        texts.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 12 * paddingPixel));
        texts.setOrientation(LinearLayout.VERTICAL);

        TextView ventilator_name = new TextView(this);
        ventilator_name.setLayoutParams(wrapcontentparams);
        ventilator_name.setText(name);
        ventilator_name.setTextSize(25);
        ventilator_name.setTextColor(getResources().getColor(android.R.color.black));
        texts.addView(ventilator_name);

        TextView ventilator_on_off = new TextView(this);
        ventilator_on_off.setText(on_off);
        ventilator_on_off.setLayoutParams(wrapcontentparams);
        ventilator_on_off.setTextColor(getResources().getColor(android.R.color.black));
        ventilator_on_off.setTextSize(15);
        texts.addView(ventilator_on_off);

        ventilator_layout.addView(texts);

        parent.addView(ventilator_layout);
    }

    public void add_fan(String name, String on_off, int speed) {
        LinearLayout parent = (LinearLayout) findViewById(R.id.linear_layout_of_room);
        parent.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 10;
        float density = getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);

        LinearLayout fan_layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams wrapcontentparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fan_layout.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        fan_layout.setLayoutParams(params);
        fan_layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.fan_icon);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(12 * paddingPixel, 12 * paddingPixel);
        image.setLayoutParams(layoutParams);
        image.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        fan_layout.addView(image);

        LinearLayout texts = new LinearLayout(this);
        texts.setPadding(3 * paddingPixel, 0, 0, 0);
        texts.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 12 * paddingPixel));
        texts.setOrientation(LinearLayout.VERTICAL);

        TextView fan_name = new TextView(this);
        fan_name.setLayoutParams(wrapcontentparams);
        fan_name.setText(name);
        fan_name.setTextSize(25);
        fan_name.setTextColor(getResources().getColor(android.R.color.black));
        texts.addView(fan_name);

        TextView fan_on_off = new TextView(this);
        fan_on_off.setText(on_off);
        fan_on_off.setLayoutParams(wrapcontentparams);
        fan_on_off.setTextColor(getResources().getColor(android.R.color.black));
        fan_on_off.setTextSize(15);
        texts.addView(fan_on_off);

        TextView fan_speed = new TextView(this);
        fan_speed.setText("speed : " + speed);
        fan_speed.setLayoutParams(wrapcontentparams);
        fan_speed.setTextColor(getResources().getColor(android.R.color.black));
        fan_speed.setTextSize(15);
        texts.addView(fan_speed);

        fan_layout.addView(texts);

        parent.addView(fan_layout);
    }

    public void open_edit_devices_activity(int x) {
        Intent intent = new Intent(this, add_devices.class);
        intent.putExtra(MESSAGE_TEXT_INT, x);
        startActivity(intent);
    }

    public void open_enter_data_activity(int x) {
        Intent intent = new Intent(this, enter_data.class);
        intent.putExtra(MESSAGE_TEXT_INT, x);
        startActivity(intent);
    }
}
