package com.developer.android.quickveggis.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.developer.android.quickveggis.R;
import com.developer.android.quickveggis.api.ServiceAPI;
import com.developer.android.quickveggis.api.model.SocialLoginData;
import com.developer.android.quickveggis.api.response.ResponseCallback;
import com.developer.android.quickveggis.config.Config;
import com.developer.android.quickveggis.controller.CustomerController;
import com.developer.android.quickveggis.controller.SessionController;
import com.developer.android.quickveggis.model.Session;
import com.developer.android.quickveggis.ui.fragments.SplashFragment;
import com.developer.android.quickveggis.ui.utils.FragmentUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;

import static com.developer.android.quickveggis.App.bool;
import static com.developer.android.quickveggis.App.launched;
import static com.developer.android.quickveggis.ui.fragments.TouchIDFragment.FINGERPRINT_ALLOW_STATE;
import static com.developer.android.quickveggis.ui.fragments.TouchIDFragment.FINGERPRINT_CHECK_STATE;
import static com.developer.android.quickveggis.ui.fragments.TouchIDFragment.FINGERPRINT_INIT_STATE;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmen_splash);

        SharedPreferences preferences = getSharedPreferences("com.login.user.social", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FINGERPRINT_INIT_STATE,false);
        editor.putBoolean(FINGERPRINT_CHECK_STATE,false);
        editor.commit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1010);
        } else {
            launched = false;
           // FragmentUtils.changeFragment(this, R.id.content, SplashFragment.newInstance(), false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityOnCondition();
                }
            }, Config.SPLASH_DELAY_TIME);
        }
    }
    // Requestion of External storage read/write permission using Code  (Can set it using manifest permission)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1010) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launched = false;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivityOnCondition();
                    }
                }, Config.SPLASH_DELAY_TIME);
            } else {
                Toast.makeText(this,"Please allow all premissions",Toast.LENGTH_SHORT).show();
                finish();
            }

        } else if(requestCode == 334) {
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences=getSharedPreferences("com.login.user.social", Context.MODE_PRIVATE);
        boolean finger_check_result=preferences.getBoolean(FINGERPRINT_CHECK_STATE,false);
        boolean finger_allow_state=preferences.getBoolean(FINGERPRINT_ALLOW_STATE,false);
        boolean finger_init_state=preferences.getBoolean(FINGERPRINT_INIT_STATE,false);

        if (finger_allow_state){
            if (finger_init_state && finger_check_result) start();
            if (finger_init_state && !finger_check_result){
                finish();
                System.exit(0);
            }
        }
    }

    private void startActivityOnCondition(){
        SharedPreferences preferences = getSharedPreferences("com.login.user.social", Context.MODE_PRIVATE);
        if (preferences.getBoolean(FINGERPRINT_ALLOW_STATE,false)){
            startActivity(new Intent(SplashActivity.this,FingerprintActivity.class));
//            finish();
        }
        else
            start();
    }

    void start(){
        if (SessionController.getInstance().isLoggedInSession()) {
            checkTutorial();
            Log.d("session", SessionController.getInstance().getLoggedInSession());
        } else {
            ServiceAPI.newInstance().getSession(new ResponseCallback<Session>() {
                @Override
                public void onSuccess(Session data) {
                    SessionController.getInstance().saveLoginSession(data.getSession());
                    checkTutorial();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                    restartThisActivity();
                }
            });
        }
    }
    private void checkTutorial(){
        if (SessionController.getInstance().isExistShownFirstTutorial())
        {
            if (CustomerController.getInstance().isCustomerLoggedIn()) {
                startMainActivity();
            }
            else {
                startLoginActivity();
            }
        }
        else {
            startWalk1Activity();
            SessionController.getInstance().setFirstTutorial(true);
        }
    }

    private void startLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void startWalk1Activity(){
        startActivity(new Intent(this, Walk1Activity.class));
        finish();
    }

    void restartThisActivity(){
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    void startMainActivity(){
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }
}
