package com.isajoh.app.ad_detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.isajoh.app.R;
import com.isajoh.app.Search.FragmentCatSubNSearch;
import com.isajoh.app.helper.LocaleHelper;
import com.isajoh.app.home.AddNewAdPost;
import com.isajoh.app.home.HomeActivity;
import com.isajoh.app.public_profile.FragmentPublic_Profile;
import com.isajoh.app.utills.Admob;
import com.isajoh.app.utills.AnalyticsTrackers;
import com.isajoh.app.utills.RuntimePermissionHelper;
import com.isajoh.app.utills.SettingsMain;

public class Ad_detail_activity extends AppCompatActivity implements RuntimePermissionHelper.permissionInterface {

    SettingsMain settingsMain;
    Intent intent;
    RuntimePermissionHelper runtimePermissionHelper;
    ImageView HomeButton;
    public static ImageView favBtn, shareBtn, reportBtn;
    UpdateFragment updatfrag;
    int windowwidth,windowheight;
//    private android.widget.LinearLayout.LayoutParams layoutParams;

    public void updateApooi(UpdateFragment listener) {
        updatfrag = listener;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_detail_activity);

        settingsMain = new SettingsMain(this);

        intent = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(settingsMain.getMainColor()));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        runtimePermissionHelper = new RuntimePermissionHelper(this, this);
        HomeButton = findViewById(R.id.home);
        favBtn = findViewById(R.id.favourite);
        shareBtn = findViewById(R.id.share);
        reportBtn = findViewById(R.id.report);
        if (settingsMain.getAdDetailScreenStyle().equals("style1")) {
            favBtn.setVisibility(View.GONE);
            shareBtn.setVisibility(View.GONE);
            reportBtn.setVisibility(View.GONE);
            if (settingsMain.getShowHome()) {
                HomeButton.setVisibility(View.VISIBLE);
                HomeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Ad_detail_activity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                HomeButton.setVisibility(View.GONE);
            }
        } else {
            HomeButton.setVisibility(View.GONE);
            favBtn.setVisibility(View.VISIBLE);
            shareBtn.setVisibility(View.VISIBLE);
            reportBtn.setVisibility(View.VISIBLE);

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(settingsMain.getMainColor())));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runtimePermissionHelper.requestLocationPermission(1);
            }
        });
//        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
//        windowheight = getWindowManager().getDefaultDisplay().getHeight();
//        ImageView whatsapp = (ImageView) findViewById(R.id.img_whats_app);
//        whatsapp.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) whatsapp.getLayoutParams();
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        int x_cord = (int) event.getRawX();
//                        int y_cord = (int) event.getRawY();
//
//                        if (x_cord > windowwidth) {
//                            x_cord = windowwidth;
//                        }
//                        if (y_cord > windowheight) {
//                            y_cord = windowheight;
//                        }
//
//                        layoutParams.leftMargin = x_cord - 25;
//                        layoutParams.topMargin = y_cord - 75;
//
//                        whatsapp.setLayoutParams(layoutParams);
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });

        toolbar.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        if (settingsMain.getAppOpen()) {
            fab.setVisibility(View.GONE);
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (settingsMain.getBannerShow()) {
            if (settingsMain.getAdsShow() && !settingsMain.getBannerAdsId().equals("")) {
                if (settingsMain.getAdsPostion().equals("top")) {
                    LinearLayout frameLayout = (LinearLayout) findViewById(R.id.AdDetailsAdmob);
                    Admob.adforest_Displaybanners(Ad_detail_activity.this, frameLayout);
                } else {
                    LinearLayout frameLayout = (LinearLayout) findViewById(R.id.AdDetailsAdmobBottom);
                    RelativeLayout maimFrame = (RelativeLayout) findViewById(R.id.adDetailsLayout);
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);

                    int width = size.x;
                    int height = size.y;

                    Admob.adforest_DisplaybannersForAdDetail(this, frameLayout, maimFrame, fab);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.bottomMargin = height / 10;
                    maimFrame.setLayoutParams(layoutParams);
                    CoordinatorLayout.LayoutParams layoutParams2 = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams2.bottomMargin = height / 5;
                    layoutParams2.setMarginStart(16);
                    layoutParams2.setMarginEnd(16);
                    layoutParams2.leftMargin = 16;
                    layoutParams2.rightMargin = 16;
                    layoutParams2.gravity = Gravity.BOTTOM | Gravity.END;
                    fab.setLayoutParams(layoutParams2);
                }
            }
        }
        if (settingsMain.getAdDetailScreenStyle().equals("style1")) {
            FragmentAdDetail fragmentAdDetail = new FragmentAdDetail();
            Bundle bundle = new Bundle();
            bundle.putString("id", intent.getStringExtra("adId"));
            bundle.putString("is_rejected", intent.getStringExtra("is_rejected"));
            fragmentAdDetail.setArguments(bundle);
            startFragment(fragmentAdDetail, "fragmentAdDetail");
            updateViews(settingsMain.getAlertDialogMessage("gmap_lang"));
        } else {
            MarvelAdDetailFragment marvelAdDetailFragment = new MarvelAdDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", intent.getStringExtra("adId"));
            bundle.putString("is_rejected", intent.getStringExtra("is_rejected"));
            marvelAdDetailFragment.setArguments(bundle);
            startFragment(marvelAdDetailFragment, "MarvelAdDetailFragment");
            updateViews(settingsMain.getAlertDialogMessage("gmap_lang"));
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.above, menu);
//        MenuItem searchViewItem = menu.findItem(R.id.action_search);
//        if (!settingsMain.getShowAdvancedSearch()) {
//            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
//            searchView.setQueryHint(settingsMain.getAlertDialogMessage("search_text"));
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    searchView.clearFocus();
//
//                    if (!query.equals("")) {
//
//                        FragmentManager fm = getSupportFragmentManager();
//                        Fragment fragment = fm.findFragmentByTag("FragmentCatSubNSearch");
//                        Fragment fragment2 = fm.findFragmentById(R.id.frameContainer);
//
//                        FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("id", "");
//                        bundle.putString("title", query);
//                        bundle.putString("RequestFrom","");
//                        fragment_search.setArguments(bundle);
//
//                        if (fragment != fragment2) {
//                            replaceFragment(fragment_search, "FragmentCatSubNSearch");
//                            return true;
//                        } else {
//                            updatfrag.update(query);
//                            return true;
//                        }
//                    }
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });
//
//            return super.onCreateOptionsMenu(menu);
//        }
//        return true;
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void updateViews(String languageCode) {
        LocaleHelper.setLocale(this, languageCode);
    }

    @Override
    public void onBackPressed() {
        if (FragmentAdDetail.onLoad || FragmentPublic_Profile.onLoading || MarvelAdDetailFragment.onLoad) {


        } else {

            super.onBackPressed();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        }
    }

    public void startFragment(Fragment someFragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = someFragment;
            fm.beginTransaction()
                    .add(R.id.frameContainer, fragment, tag).commit();
        }
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
                AnalyticsTrackers.getInstance().trackScreenView("Ad Details");
            super.onResume();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(Fragment someFragment, String tag) {

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        Fragment fragment2 = fm.findFragmentById(R.id.frameContainer);

        if (fragment != fragment2) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);
            transaction.replace(R.id.frameContainer, someFragment, tag);
            transaction.addToBackStack(tag);
            transaction.commit();
        }
    }

    @Override
    public void onSuccessPermission(int code) {
        Intent intent = new Intent(Ad_detail_activity.this, AddNewAdPost.class);
        startActivity(intent);
    }

    public interface UpdateFragment {
        void update(String s);
    }

}
