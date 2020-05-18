package com.moringaschool.myrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Button mFindRestaurantsButton;
    private EditText mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsername = (EditText) findViewById(R.id.nameEditText);

        mFindRestaurantsButton = (Button) findViewById(R.id.findRestaurantsButton);
            mFindRestaurantsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = mUsername.getText().toString();
                    Log.d(TAG, name);

                    Toast.makeText(MainActivity.this, "Welcome, " + name, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);

                }
            });
    }
}
