package com.macoscargo.mobileparcels.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.macoscargo.mobileparcels.R;
import com.macoscargo.mobileparcels.activity.MainActivity;
import com.macoscargo.mobileparcels.activity.RegisterUser;


/**
 * Created by christiannhlabano on 2019/07/16.
 */

public class Login extends AppCompatActivity implements View.OnClickListener{
    private Button login_button, forgot_password, sign_up_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        sign_up_button = findViewById(R.id.register_button);
        sign_up_button.setOnClickListener(this);
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {

            case R.id.register_button:
                i = new Intent(getBaseContext(), RegisterUser.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

            case R.id.login_button:
                i = new Intent(getBaseContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
    }
}
