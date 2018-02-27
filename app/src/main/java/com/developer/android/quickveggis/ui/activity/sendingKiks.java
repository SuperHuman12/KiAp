package com.developer.android.quickveggis.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.android.quickveggis.R;

public class sendingKiks extends AppCompatActivity implements View.OnClickListener {


    Button btnsend, pastebtn;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    EditText toet, amountET;
    ImageView removetextIV;
    TextView balanceTV,insufficientTV,to_tv;
    LinearLayout varient_height_lay;
    LinearLayout amount_lay,paste_sub_lay;
    ScrollView scrollview;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_kiks);


        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Send KIKs");


        to_tv = (TextView) findViewById(R.id.to_tv);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        amount_lay = (LinearLayout) findViewById(R.id.amount_lay);
        paste_sub_lay = (LinearLayout) findViewById(R.id.paste_sub_lay);
        varient_height_lay = (LinearLayout) findViewById(R.id.varient_height_lay);
        insufficientTV = (TextView) findViewById(R.id.insufficientTV);
        balanceTV = (TextView) findViewById(R.id.balanceTV);
        amountET = (EditText) findViewById(R.id.amountET);
        removetextIV = (ImageView) findViewById(R.id.removetextIV);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        pastebtn = (Button) findViewById(R.id.pastebtn);
        btnsend = (Button) findViewById(R.id.btnsend);
        toet = (EditText) findViewById(R.id.toet);
        btnsend.setOnClickListener(this);
        pastebtn.setOnClickListener(this);
        removetextIV.setOnClickListener(this);
        to_tv.setOnClickListener(this);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.mainGreenDark));



        amountET.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s){

                if(amount_lay.getVisibility() == View.GONE){
                    amount_lay.setVisibility(View.VISIBLE);
                }if(s.length() == 0 ){
                    amount_lay.setVisibility(View.GONE);
                 }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count){
                if(s.length() != 0){
                     if(amount_lay.getVisibility() == View.GONE){
                         amount_lay.setVisibility(View.VISIBLE);
                         scrollview.scrollTo(0, scrollview.getBottom());

                          //scrollview.scrollTo(0, 1000);
                    }
                 }if(s.length() == 0){
                    amount_lay.setVisibility(View.GONE);
                }
            }
        });



        pastebtn.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0dee4eb, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();

                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.info).setVisible(true);
        menu.findItem(R.id.action_map).setVisible(false);
        menu.findItem(R.id.action_notifications).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_cart).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_history).setVisible(false);

        this.invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnsend.getId()){
            Intent i = new Intent(this, kiksSuccess.class);
            startActivity(i);
        }else if(view.getId() == pastebtn.getId()){

            paste_sub_lay.setVisibility(View.VISIBLE);
            try{
                ClipData abc = myClipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                String text = item.getText().toString();
                toet.setText(text);
                toet.setSelection(toet.getText().length());
            }catch (Exception e){

            }

        }else if(view.getId() == removetextIV.getId()){
            paste_sub_lay.setVisibility(View.GONE);
            toet.setText("");
        }else if(view.getId() == to_tv.getId()){
            paste_sub_lay.setVisibility(View.VISIBLE);
        }
    }
}
