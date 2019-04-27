package me.yokeyword.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import me.yokeyword.sample.city.PickCityActivity;
import me.yokeyword.sample.contact.PickContactActivity;


/**
 * Created by YoKey on 16/10/7.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_pick_city).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PickCityActivity.class)));
        findViewById(R.id.btn_pick_contact).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PickContactActivity.class)));
    }
}
