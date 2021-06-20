package com.isajoh.app.messages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.isajoh.app.R;
import com.isajoh.app.home.HomeActivity;
import com.isajoh.app.utills.AnalyticsTrackers;
import com.isajoh.app.utills.SettingsMain;

public class ChatActivity extends AppCompatActivity {

    SettingsMain settingsMain;
    Intent intent;
    ImageView HomeButton;
    boolean calledFromSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        settingsMain = new SettingsMain(this);
        HomeButton = findViewById(R.id.home);
        if (settingsMain.getShowHome()) {
            HomeButton.setVisibility(View.VISIBLE);
            HomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            HomeButton.setVisibility(View.GONE);
        }

        toolbar.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(settingsMain.getMainColor()));
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();

        if (!intent.getStringExtra("senderId").equals("")) {

            ChatFragment chatFragment = new ChatFragment();
            Bundle bundle = new Bundle();
            bundle.putString("adId", intent.getStringExtra("adId"));
            bundle.putString("senderId", intent.getStringExtra("senderId"));
            bundle.putString("recieverId", intent.getStringExtra("recieverId"));
            bundle.putString("type", intent.getStringExtra("type"));
            bundle.putString("is_block", intent.getStringExtra("is_block"));
            bundle.putBoolean("calledFromSplash", intent.getBooleanExtra("calledFromSplash", false));
            bundle.putBoolean("calledFromChat", intent.getBooleanExtra("calledFromChat", false));
            chatFragment.setArguments(bundle);
            startFragment(chatFragment);
        } else {
            RecievedOffersList recievedOffersList = new RecievedOffersList();
            Bundle bundle = new Bundle();
            bundle.putString("adId", intent.getStringExtra("adId"));
            recievedOffersList.setArguments(bundle);
            startFragment(recievedOffersList);
        }
        if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals("")) {
            AnalyticsTrackers.initialize(this);
            Log.d("analyticsID", settingsMain.getAnalyticsId());
            AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, settingsMain.getAnalyticsId());
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.message, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            ChatActivity.this.recreate();
            return true;
        }
        if (id == android.R.id.home) {
            if (getIntent().getBooleanExtra("calledFromSplash", false)) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        }
//        if (id == R.id.block_Button) {
//                ChatFragment chatFragment = new ChatFragment();
//                    chatFragment.getContext();
//                    chatFragment.adforest_BlockChat();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void startFragment(Fragment someFragment) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frameContainer);

        if (fragment == null) {
            fragment = someFragment;
            fm.beginTransaction()
                    .add(R.id.frameContainer, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getBooleanExtra("calledFromSplash", false)) {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            finish();
        }
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        try {
            if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals(""))
                AnalyticsTrackers.getInstance().trackScreenView("Chat Box");
            super.onResume();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
