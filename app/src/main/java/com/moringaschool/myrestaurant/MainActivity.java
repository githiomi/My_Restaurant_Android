package com.moringaschool.myrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mFindRestaurantsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFindRestaurantsButton = (Button) findViewById(R.id.findRestaurantsButton);
            mFindRestaurantsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Hello world", Toast.LENGTH_LONG).show();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
                    startActivity(intent);

                }
            });
    }
}
