package com.moringaschool.myrestaurant;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.nameEditText) EditText mUsername;
    @BindView(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @BindView(R.id.tvStringArray) TextView mStringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Resources res = getResources();
        String[] myWords = res.getStringArray(R.array.mad_libs_1);

        for (String myWord : myWords) {
            mStringArray.append("The words is: " + myWord + "\n");
        }

        mFindRestaurantsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v == mFindRestaurantsButton){
            String name = mUsername.getText().toString();

            Toast.makeText(this, "Welcome, " + name, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }
}
