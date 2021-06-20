package com.isajoh.app.home;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.isajoh.app.Blog.BlogDetailFragment;
import com.isajoh.app.Blog.BlogFragment;
import com.isajoh.app.Notification.Config;
import com.isajoh.app.R;
import com.isajoh.app.Search.FragmentCatSubNSearch;
import com.isajoh.app.Search.SearchActivity;
import com.isajoh.app.Shop.shopActivity;
import com.isajoh.app.home.adapter.TooltipWindow;
import com.isajoh.app.ad_detail.Ad_detail_activity;
import com.isajoh.app.adapters.ItemSearchFeatureAdsAdapter;
import com.isajoh.app.adapters.MarvelItemMainHomeRelatedAdapter;
import com.isajoh.app.adapters.MarvelItemSearchFeatureAdsAdapter;
import com.isajoh.app.helper.BlogItemOnclicklinstener;
import com.isajoh.app.helper.CatSubCatOnclicklinstener;
import com.isajoh.app.helper.GridSpacingItemDecoration;
import com.isajoh.app.helper.MyAdsOnclicklinstener;
import com.isajoh.app.helper.OnItemClickListener;
import com.isajoh.app.helper.OnItemClickListener2;
import com.isajoh.app.home.adapter.ItemBlogHomeAdapter;
import com.isajoh.app.home.adapter.ItemMainAllLocationAds;
import com.isajoh.app.home.adapter.MarvelItemMainAllCatAdapter;
import com.isajoh.app.home.adapter.MarvelItemMainCAT_Related_All;
import com.isajoh.app.home.adapter.MultiItemMainAllLocationAds;
import com.isajoh.app.modelsList.blogModel;
import com.isajoh.app.modelsList.catSubCatlistModel;
import com.isajoh.app.modelsList.homeCatListModel;
import com.isajoh.app.modelsList.homeCatRelatedList;
import com.isajoh.app.modelsList.myAdsModel;
import com.isajoh.app.packages.Authorize2.AuthorizeNetModel;
import com.isajoh.app.packages.BrainTreeModel;
import com.isajoh.app.packages.PayHereModel;
import com.isajoh.app.packages.WorldPay.WorldPayModel;
import com.isajoh.app.utills.Admob;
import com.isajoh.app.utills.AnalyticsTrackers;
import com.isajoh.app.utills.CustomBorderDrawable;
import com.isajoh.app.utills.Network.RestService;
import com.isajoh.app.utills.SettingsMain;
import com.isajoh.app.utills.UrlController;

public class FragmentHomeMarvel extends Fragment {

    static int adsCounter = 0;
    public JSONObject jsonObjectSubMenu, responseData;
    static Boolean Ad_post = false;
    String regId, locationIDd, locationIdHomePOpup, locationIdHomePOpupName, imageViewLocation;
    static String Verfiedmessage;
    ArrayList<homeCatListModel> listitems = new ArrayList<>();
    ArrayList<homeCatListModel> locationAdscat = new ArrayList<>();
    ArrayList<homeCatRelatedList> listitemsRelated = new ArrayList<>();
    ArrayList<catSubCatlistModel> featureAdsList = new ArrayList<>();
    ArrayList<blogModel> blogsArrayList = new ArrayList<>();
    ItemSearchFeatureAdsAdapter itemFeatureAdsAdapter;
    int[] iconsId = {R.drawable.ic_pages, R.drawable.ic_help_outline_black_24dp, R.drawable.ic_about_black_24dp, R.drawable.ic_file};
    LinearLayout featureAboveLayoyut, featurebelowLayoyut, featuredMidLayout;
    Menu menu;
    View viw;
    RestService restService;
    TextView textViewTitleFeature, textViewTitleFeatureBelow, textViewTitleFeatureMid;
    CardView catCardView;
    LinearLayout HomeCustomLayout, staticSlider;
    TextView tv_search_title, tv_search_subTitle, tv_locationName, tv_location_title;
    EditText et_search, searchViewLcoation;
    TextView et_location;
    Boolean dikhao = false;
    ImageButton img_location;
    ImageButton img_btn_search, img_btn_location, img_btn_filter;
    RelativeLayout searchLayout, mainActivity, searchBarRelative;
    ImageView backgroundImage;
    Button buttonAllCat;
    static boolean title_Nav;
    private SettingsMain settingsMain;
    private ArrayList<catSubCatlistModel> latesetAdsList = new ArrayList<>();
    private ArrayList<catSubCatlistModel> nearByAdsList = new ArrayList<>();
    private RecyclerView mRecyclerView, mRecyclerView2, featuredRecylerViewAbove, featuredRecylerViewBelow,
            featuredRecylerViewMid;
    private Context context;
    public static View locationFragmentView;
    public static AlertDialog locationDialog;
    //    FirebaseDatabase database;
//    DatabaseReference myRef;
    private String btnViewAllText;
    boolean verticalNew = false;
    boolean HorizontalvertNew = false;
    String HorizontalHome;
    String latestAdsLayout;
    String FeaturedAdsLayout;
    String nearByAdsLayout;
    String SliderAdsLayout;

    DialogFragment dialogFragment;

    //    ToolTip.Builder toolTipViewBuilder;
    TooltipWindow tipWindow;
    JSONArray dialogLocations;
    private InfiniteScrollAdapter<?> infiniteAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout loadingLayout;

    public FragmentHomeMarvel() {
        // Required empty public constructor
    }

    public void adforest_recylerview_autoScroll(final int duration, final int pixelsToMove, final int delayMillis,
                                                final RecyclerView recyclerView, final GridLayoutManager gridLayoutManager
            , final MarvelItemSearchFeatureAdsAdapter adapter) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;

            @Override
            public void run() {
                try {
                    if (count < adapter.getItemCount()) {
                        if (count == adapter.getItemCount() - 1) {
                            flag = false;
                        } else if (count == 0) {
                            flag = true;
                        }
                        if (flag) count++;
                        else count--;

                        recyclerView.smoothScrollToPosition(count);
                        handler.postDelayed(this, duration);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }
        };

        handler.postDelayed(runnable, delayMillis);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_home_marvel, container, false);
        context = getActivity();
        settingsMain = new SettingsMain(getActivity());

        NavigationView navigationView = this.getActivity().findViewById(R.id.nav_view);

        // get menu from navigationView
        menu = navigationView.getMenu();
        mRecyclerView = view.findViewById(R.id.recycler_view);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        loadingLayout = view.findViewById(R.id.shimmerMain);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
// Either use an existing Skeletonlayout

        // or create a new SkeletonLayout from a given View
//        skeleton = SkeletonLayoutUtils.createSkeleton(view);
//        if (!HomeActivity.checkLoading) {
//            skeleton.showSkeleton();
//        }

//        toolTipViewBuilder = new ToolTip.Builder();
        tipWindow = new TooltipWindow(getActivity());

        featureAboveLayoyut = view.findViewById(R.id.featureAboveLayoyut);
        featurebelowLayoyut = view.findViewById(R.id.featureAboveLayoutBelow);
        featuredMidLayout = view.findViewById(R.id.featureLayoutMid);
        textViewTitleFeature = view.findViewById(R.id.textView6);
        textViewTitleFeatureBelow = view.findViewById(R.id.textView7);
        textViewTitleFeatureMid = view.findViewById(R.id.textView8);
        featuredRecylerViewAbove = view.findViewById(R.id.recycler_view3);
        featuredRecylerViewBelow = view.findViewById(R.id.featuredRecylerViewBelow);
        featuredRecylerViewMid = view.findViewById(R.id.featuredRecylerViewMid);
        catCardView = view.findViewById(R.id.card_view);
        buttonAllCat = view.findViewById(R.id.buttonAllCat);
        buttonAllCat.setBackground(CustomBorderDrawable.customButton(6, 6, 6, 6, SettingsMain.getMainColor(), SettingsMain.getMainColor(), SettingsMain.getMainColor(), 3));
        mainActivity = view.findViewById(R.id.activity_main);
        mainActivity.setBackgroundColor(Color.WHITE);
        //(Color.parseColor("#F2F2F2"));
        HomeCustomLayout = view.findViewById(R.id.HomeCustomLayout);
//        HomeCustomLayout.setBackgroundColor(Color.parseColor("#DCDCDC"));
        searchBarRelative = view.findViewById(R.id.searchBarRelative);
        staticSlider = view.findViewById(R.id.linear1);
        mRecyclerView2 = view.findViewById(R.id.recycler_view2);
        viw = view.findViewById(R.id.viw);
        et_search = view.findViewById(R.id.et_search);
        img_btn_search = view.findViewById(R.id.img_btn_search);
        img_btn_location = view.findViewById(R.id.img_btn_location);
        img_btn_filter = view.findViewById(R.id.img_btn_filter);
        searchLayout = view.findViewById(R.id.searchLayout);
        tv_location_title = view.findViewById(R.id.tv_location_title);
        tv_locationName = view.findViewById(R.id.tv_locationName);
        backgroundImage = view.findViewById(R.id.imgBg_main);
        backgroundImage.setBackgroundColor(Color.WHITE);
        //(Color.parseColor("#F2F2F2"));
//        backgroundImage.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        viw = view.findViewById(R.id.viw);
        tv_search_title = view.findViewById(R.id.tv_search_title);
        tv_search_subTitle = view.findViewById(R.id.tv_search_subTitle);
        if (getArguments() != null) {
            locationIdHomePOpup = getArguments().getString("location_id");
            locationIdHomePOpupName = getArguments().getString("location_name");

        }
        Drawable drawable = getResources().getDrawable(R.drawable.bg_marvel_top_field).mutate();
        drawable.setColorFilter(Color.parseColor(SettingsMain.getMainColor()), PorterDuff.Mode.SRC_ATOP);
        img_btn_search.setBackground(drawable);

        Drawable drawable1 = getResources().getDrawable(R.drawable.bg_search_items).mutate();
        searchBarRelative.setBackground(drawable1);

        Drawable drawableFilter = getResources().getDrawable(R.drawable.bg_search_items).mutate();
        img_btn_filter.setBackground(drawableFilter);

        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("catId", "");
                intent.putExtra("ad_title", et_search.getText().toString());
                intent.putExtra("requestFrom", "Home");

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
            return false;
        });

        img_btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TooltipWindow tipWindow = new TooltipWindow(getActivity());
                if (!tipWindow.isTooltipShown()) {
                    if (locationIdHomePOpupName != null) {
                        tipWindow.toolTiptext.setText(locationIdHomePOpupName);
                    } else
                        tipWindow.toolTiptext.setText("Select Location");
                    tipWindow.toolTiptext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogFragment = FragmentAllLocationsPopUpHome.newInstance();
                            dialogFragment.show(getFragmentManager(), "locationDialog");
                            return;
                        }
                    });
                    tipWindow.showToolTip(v);
                }


            }
        });
        img_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                Log.d("info Search filter Home", intent.toString());
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);

            }
        });


        img_btn_search.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.putExtra("catId", "");
            intent.putExtra("ad_title", et_search.getText().toString());
            intent.putExtra("requestFrom", "Home");
            intent.putExtra("ad_country", locationIdHomePOpup);
            startActivity(intent);
            Log.d("info Search intent Home", intent.toString());
            getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
        });


        if (settingsMain.getAppOpen()) {
            restService = UrlController.createService(RestService.class);
        } else
            restService = UrlController.createService(RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), getActivity());

        ((HomeActivity) getActivity()).changeImage();
        if (!settingsMain.getUserLogin().equals("0")) {
            Log.d("info Firebase topic", "subscribe");

            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        }
        Log.d("FireBaseId", settingsMain.getFireBaseId());
        SharedPreferences pref = getActivity().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("Firebase Regid", null);
        if (settingsMain.getFireBaseId().equals("") && !settingsMain.getUserLogin().equals("0")) {
            adforest_AddFirebaseid(regId);

        }
        //Testing response time
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        Log.d("currentTimeBeforeFUnc", currentDateTimeString);
        adforest_getAllData();

        SwipeRefreshLayout swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(true);

        buttonAllCat.setOnClickListener(v -> {
            FragmentAllCategories fragmentAllCategories = new FragmentAllCategories();
            replaceFragment(fragmentAllCategories, "FragmentAllCategories");
        });

//        if (!settingsMain.getUserLogin().equals("0")) {
//
//            ChatUserModel userModel = new ChatUserModel(true, settingsMain.getUserLogin());
//            myRef.child(settingsMain.getUserLogin()).setValue(userModel);
//        }

        return view;

    }


    private void adforest_getAllData() {

        if (SettingsMain.isConnectingToInternet(getActivity())) {

            if (!HomeActivity.checkLoading)
                loadingLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            //Testing response time
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
            Log.d("currentTimeBefCall", currentDateTimeString);
            Call<ResponseBody> myCall = restService.getHomeDetails(UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        //Testing response time
                        String currentDateTimeStringss = java.text.DateFormat.getDateTimeInstance().format(new Date());
                        Log.d("currentTimeBefRes", currentDateTimeStringss);
                        if (responseObj.isSuccessful()) {
                            Log.d("info HomeGet Responce", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                loadingLayout.setVisibility(View.GONE);
                                //Testing response time
                                String currentDateTimeStringzz = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                Log.d("currentTimeAfterRes", currentDateTimeStringzz);
                                responseData = response.getJSONObject("data");
                                HomeActivity.checkLoading = false;

                                Log.d("info Home Responce", "" + response.toString());

                                getActivity().setTitle(response.getJSONObject("data").getString("page_title"));


                                btnViewAllText = response.getJSONObject("data").getString("view_all");
                                JSONObject sharedSettings = response.getJSONObject("settings");


                                jsonObjectSubMenu = response.getJSONObject("data").getJSONObject("menu").getJSONObject("submenu");

                                if (jsonObjectSubMenu.getBoolean("has_page")) {

                                    menu.findItem(R.id.custom).setVisible(true);

                                    final JSONArray jsonArray = jsonObjectSubMenu.getJSONArray("pages");
                                    menu.findItem(R.id.custom).setTitle(jsonObjectSubMenu.getString("title"));
                                    menu.findItem(R.id.custom).getSubMenu().clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        menu.findItem(R.id.custom).getSubMenu().add(0, jsonArray.getJSONObject(i).getInt("page_id"), Menu.NONE,
                                                jsonArray.getJSONObject(i).getString("page_title"));

                                        final int finalI = i;
                                        menu.findItem(R.id.custom).getSubMenu().getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem menuItem) {
                                                Bundle bundle = new Bundle();
                                                try {
                                                    if (jsonArray.getJSONObject(finalI).getString("type").equals("webview")) {
                                                        bundle.putString("page_url", jsonArray.getJSONObject(finalI).getString("page_url"));
                                                        bundle.putString("pageTitle", jsonArray.getJSONObject(finalI).getString("page_title"));
                                                        Log.d("fraghome", jsonArray.getJSONObject(finalI).getString("page_title"));
                                                        Log.d("pagetitle", jsonArray.toString());
                                                    } else {
                                                        bundle.putString("page_url", "");
                                                        bundle.putString("pageTitle", "");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                FragmentCustomPages fragment_search = new FragmentCustomPages();

                                                bundle.putString("id", "" + menuItem.getItemId());

                                                fragment_search.setArguments(bundle);
                                                replaceFragment(fragment_search, "FragmentCustomPages");

                                                return false;
                                            }
                                        });
                                    }
                                    menu.findItem(R.id.custom).getSubMenu().setGroupCheckable(0, true, true);
                                } else {
                                    menu.findItem(R.id.custom).setVisible(false);
                                }
                                if (jsonObjectSubMenu.getBoolean("has_page")) {
                                    FragmentHomeMarvel.AsyncImageTask asyncImageTask = new FragmentHomeMarvel.AsyncImageTask();
                                    asyncImageTask.execute();
                                }
                                menu.findItem(R.id.home).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("home"));
                                menu.findItem(R.id.search).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("search"));
                                menu.findItem(R.id.packages).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("packages"));

                                if (settingsMain.getAppOpen()) {
                                    menu.findItem(R.id.message).setVisible(false);
                                    menu.findItem(R.id.profile).setVisible(false);
                                    menu.findItem(R.id.myAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("login"));
                                    menu.findItem(R.id.inActiveAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("register"));
                                    menu.findItem(R.id.myAds).setIcon(R.drawable.ic_login_icon);
                                    menu.findItem(R.id.inActiveAds).setIcon(R.drawable.ic_register_user);
                                    menu.findItem(R.id.featureAds).setVisible(false);
                                    menu.findItem(R.id.favAds).setVisible(false);
                                    menu.findItem(R.id.rejectedAds).setVisible(false);
                                    menu.findItem(R.id.nav_shop).setVisible(false);
                                    menu.findItem(R.id.nav_location).setVisible(false);
                                    menu.findItem(R.id.nav_language).setVisible(false);
                                    menu.findItem(R.id.expire_sold_Ads).setVisible(false);
                                    menu.findItem(R.id.most_viewed_Ads).setVisible(false);
                                    menu.findItem(R.id.nav_sellers).setVisible(false);
                                    menu.findItem(R.id.nav_log_out).setVisible(false);


                                } else {
                                    menu.findItem(R.id.message).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("messages"));
                                    menu.findItem(R.id.profile).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("profile"));
                                    menu.findItem(R.id.myAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("my_ads"));
                                    menu.findItem(R.id.inActiveAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("inactive_ads"));
                                    menu.findItem(R.id.featureAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("featured_ads"));
                                    menu.findItem(R.id.favAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("fav_ads"));
                                    menu.findItem(R.id.rejectedAds).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("rejected_ads"));
                                    menu.findItem(R.id.expire_sold_Ads).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("expire_sold_ads"));
                                    menu.findItem(R.id.most_viewed_Ads).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("most_visited_ads"));
                                }
                                menu.findItem(R.id.other).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("others"));
                                menu.findItem(R.id.nav_blog).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("blog"));
                                menu.findItem(R.id.nav_log_out).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("logout"));
                                menu.findItem(R.id.nav_shop).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("shop"));
                                menu.findItem(R.id.nav_settings).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("app_settings"));
                                menu.findItem(R.id.nav_sellers).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("sellers"));

                                shopActivity.title = response.getJSONObject("data").getJSONObject("menu").getString("shop");
                                JSONObject jsonObjectMenu = response.getJSONObject("data").getJSONObject("menu").getJSONObject("is_show_menu");
                                if (jsonObjectMenu.getBoolean("is_wpml_active"))
                                    menu.findItem(R.id.nav_language).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("wpml_menu_text"));
                                if (jsonObjectMenu.getBoolean("is_top_location"))
                                    menu.findItem(R.id.nav_location).setTitle(response.getJSONObject("data").getJSONObject("menu").getString("top_location_text"));

                                if (!jsonObjectMenu.getBoolean("blog")) {
                                    menu.findItem(R.id.nav_blog).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("message")) {
                                    menu.findItem(R.id.message).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("package") ||
                                        !response.getJSONObject("data").getJSONObject("menu").getBoolean("menu_is_show_packages")) {
                                    menu.findItem(R.id.packages).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("shop")) {

                                    menu.findItem(R.id.nav_shop).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("is_wpml_active")) {
                                    menu.findItem(R.id.nav_language).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("is_top_location")) {
                                    menu.findItem(R.id.nav_location).setVisible(false);
                                }

                                if (!jsonObjectMenu.getBoolean("settings")) {
                                    menu.findItem(R.id.nav_settings).setVisible(false);
                                }
                                if (!jsonObjectMenu.getBoolean("sellers")) {
                                    menu.findItem(R.id.nav_sellers).setVisible(false);
                                }


                                if (responseData.getBoolean("ads_position_sorter")) {
                                    HomeCustomLayout.setVisibility(View.VISIBLE);
                                    adforest_showDynamicViews(responseData.getJSONArray("ads_position"));
                                } else {
                                    if (responseData.getJSONObject("cat_icons_column_btn").getBoolean("is_show")) {
                                        buttonAllCat.setVisibility(View.VISIBLE);
                                        buttonAllCat.setText(responseData.getJSONObject("cat_icons_column_btn").getString("text"));
                                    }
                                    if (response.getJSONObject("data").getJSONArray("cat_icons").length() == 0) {
                                        catCardView.setVisibility(View.GONE);
                                    } else {
//                                        response.getJSONObject("data").getInt("cat_icons_column"),
                                        adforest_setAllCatgories(response.getJSONObject("data").getJSONArray("cat_icons"),
                                                mRecyclerView);
                                        catCardView.setVisibility(View.VISIBLE);
                                        Log.e("infoi", "asdasdasdsa");
                                    }
                                    if (response.getJSONObject("data").getJSONArray("sliders").length() > 0) {
                                        staticSlider.setVisibility(View.VISIBLE);
                                        adforest_setAllRelated(response.getJSONObject("data").getJSONArray("sliders"), mRecyclerView2);
                                    }

                                    if (response.getJSONObject("data").getBoolean("is_show_featured")) {
                                        JSONObject featuredObject = response.getJSONObject("data").getJSONObject("featured_ads");
                                        String featuredPosition = response.getJSONObject("data").getString("featured_position");

                                        switch (featuredPosition) {
                                            case "1":
                                                Log.e("infoi", "asdasdasdsa" + featuredPosition);
                                                featureAboveLayoyut.setVisibility(View.VISIBLE);
                                                adforest_setAllFeaturedAds(featuredObject, featuredRecylerViewAbove, textViewTitleFeature);
                                                break;
                                            case "2":
                                                Log.e("infoi", "asdasdasdsa" + featuredPosition);

                                                featuredMidLayout.setVisibility(View.VISIBLE);
                                                adforest_setAllFeaturedAds(featuredObject, featuredRecylerViewMid, textViewTitleFeatureMid);
                                                break;
                                            case "3":
                                                Log.e("infoi", "asdasdasdsa" + featuredPosition);
                                                featurebelowLayoyut.setVisibility(View.VISIBLE);
                                                adforest_setAllFeaturedAds(featuredObject, featuredRecylerViewBelow, textViewTitleFeatureBelow);
                                                break;
                                        }
                                    }
                                }
                                settingsMain.setKey("stripeKey", sharedSettings.getJSONObject("appKey").getString("stripe"));
                                WorldPayModel worldPay = new WorldPayModel();
                                worldPay.clientKey = sharedSettings.getJSONObject("appKey").getJSONObject("worldpay").getString("client_key");
                                worldPay.serviceKey = sharedSettings.getJSONObject("appKey").getJSONObject("worldpay").getString("service_key");
                                worldPay.mode = sharedSettings.getJSONObject("appKey").getJSONObject("worldpay").getString("mode");
                                worldPay.currencySign = sharedSettings.getJSONObject("appKey").getJSONObject("worldpay").getString("currency_sign");
                                settingsMain.setWorldPayCreds(worldPay);


//PayHere Model
                                PayHereModel payHereModel = new PayHereModel();
                                payHereModel.setMode(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("mode"));
                                payHereModel.setMerchant_id(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("merchant_id"));
                                payHereModel.setMerchant_secret_id(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("merchant_secret_id"));
                                payHereModel.setFirst_name(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("first_name"));
                                payHereModel.setLast_name(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("last_name"));
                                payHereModel.setEmail(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("email"));
                                payHereModel.setPhone(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("phone"));
                                payHereModel.setAddress(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("address"));
                                payHereModel.setCity(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("city"));
                                payHereModel.setCountry(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("country"));
                                payHereModel.setCurrency(sharedSettings.getJSONObject("appKey").getJSONObject("payhere").getString("currency_sign"));
                                settingsMain.setPayHereModel(payHereModel);

//BrainTree Model
                                BrainTreeModel brainTreeModel = new BrainTreeModel();
                                brainTreeModel.merchant_id = sharedSettings.getJSONObject("appKey").getJSONObject("braintree").getString("merchant_id");
                                brainTreeModel.mode = sharedSettings.getJSONObject("appKey").getJSONObject("braintree").getString("mode");
                                brainTreeModel.publicKey = sharedSettings.getJSONObject("appKey").getJSONObject("braintree").getString("public_key");
                                brainTreeModel.privateKey = sharedSettings.getJSONObject("appKey").getJSONObject("braintree").getString("private_key");
                                brainTreeModel.token_key = sharedSettings.getJSONObject("appKey").getJSONObject("braintree").getString("token_key");
                                settingsMain.setBrainTreeModel(brainTreeModel);


//AuthorizeNet Model
                                AuthorizeNetModel authorizeNetModel = new AuthorizeNetModel();
                                authorizeNetModel.mode = sharedSettings.getJSONObject("appKey").getJSONObject("authorizenet").getString("mode");
                                authorizeNetModel.name = sharedSettings.getJSONObject("appKey").getJSONObject("authorizenet").getString("name");
                                authorizeNetModel.transactionKey = sharedSettings.getJSONObject("appKey").getJSONObject("authorizenet").getString("transaction_key");
                                authorizeNetModel.referenceNum = sharedSettings.getJSONObject("appKey").getJSONObject("authorizenet").getString("reference_num");
                                settingsMain.setAuthorizeNetModel(authorizeNetModel);


                                settingsMain.setAdsShow(sharedSettings.getJSONObject("ads").getBoolean("show"));
                                if (settingsMain.getAdsShow()) {
//                                    settingsMain.setAdsType(sharedSettings.getJSONObject("ads").getString("type"));
                                    JSONObject jsonObjectAds = sharedSettings.getJSONObject("ads");
                                    if (jsonObjectAds.getBoolean("is_show_banner")) {
                                        Log.d("info banner", jsonObjectAds.toString());

                                        settingsMain.setBannerShow(jsonObjectAds.getBoolean("is_show_banner"));
                                        settingsMain.setAdsPosition(jsonObjectAds.getString("position"));
                                        settingsMain.setBannerAdsId(jsonObjectAds.getString("banner_id"));

                                    } else {
                                        settingsMain.setBannerShow(false);
                                        settingsMain.setAdsPosition("");
                                        settingsMain.setBannerAdsId("");
                                    }
                                    if (jsonObjectAds.getBoolean("is_show_initial")) {
                                        Log.d("info initial", jsonObjectAds.toString());
                                        settingsMain.setInterstitalShow(jsonObjectAds.getBoolean("is_show_initial"));
                                        settingsMain.setInterstitialAdsId(jsonObjectAds.getString("interstital_id"));
                                    } else {
                                        settingsMain.setInterstitalShow(false);
                                        settingsMain.setInterstitialAdsId("");
                                    }
                                } else {
                                    settingsMain.setBannerShow(false);
                                    settingsMain.setAdsPosition("");
                                    settingsMain.setBannerAdsId("");
                                    settingsMain.setInterstitalShow(false);
                                    settingsMain.setInterstitialAdsId("");
                                }
                                settingsMain.setAnalyticsShow(sharedSettings.getJSONObject("analytics").getBoolean("show"));
                                if (sharedSettings.getJSONObject("analytics").getBoolean("show")) {
                                    settingsMain.setAnalyticsId(sharedSettings.getJSONObject("analytics").getString("id"));
                                    Log.d("analytica======>", sharedSettings.getJSONObject("analytics").getString("id"));
                                }
                                settingsMain.setYoutubeApi(sharedSettings.getJSONObject("appKey").getString("youtube"));
                                Ad_post = responseData.getJSONObject("ad_post").getBoolean("can_post");
                                Log.d(" info_can_post", Ad_post.toString());
                                Verfiedmessage = responseData.getJSONObject("ad_post").getString("verified_msg");
                                Log.d("info_verified_msg", Verfiedmessage.toString());
//                                if (locationIdHomePOpupName != null) {
//                                    showToolTipView(img_btn_location, Gravity.BOTTOM, locationIdHomePOpupName,
//                                            ContextCompat.getColor(getContext(), R.color.green));
//                                }
                                JSONArray site_locations = responseData.getJSONArray("app_top_location_list");
//                                if (site_locations.length()==0) {
//                                 ChooseLocationFragment.emptyView = new TextView(getActivity());
//                                    ChooseLocationFragment.emptyView.setVisibility(View.VISIBLE);
//                                    ChooseLocationFragment.emptyView.setText("NO Data");
//                                }
                                ChooseLocationFragment.setData(response.getJSONObject("data").getJSONObject("menu").getString("top_location_text"), site_locations);
                                Log.d("info_top_location_list", site_locations.toString());
                                googleAnalytics();
                                AdsNDAnalytics();
                                adforest_searchLayout(response.getJSONObject("data"));

                                //Testing response time
                                String currentDateTimeStringee = java.text.DateFormat.getDateTimeInstance().format(new Date());
                                Log.d("currentTimeFuncENd", currentDateTimeStringee);
                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
//                        SettingsMain.hideDilog();

                    } catch (JSONException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        e.printStackTrace();
                    } catch (IOException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                    Log.d("info HomeGet error", String.valueOf(t));
                    Log.d("info HomeGet error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                }
            });

        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }


    private void adforest_searchLayout(JSONObject jsonObject) {
        try {
            JSONObject search_section = jsonObject.getJSONObject("search_section");
            if (search_section.getBoolean("is_show")) {
                Log.d("is_showSS", search_section.toString());

                searchLayout.setVisibility(View.VISIBLE);

                tv_search_title.setText(search_section.getString("main_title"));
                tv_search_subTitle.setText(search_section.getString("sub_title"));
                viw.setBackgroundColor(Color.parseColor(SettingsMain.getMainColor()));
                et_search.setHint(search_section.getString("placeholder"));
                if (search_section.getBoolean("is_show_location")) {
                    Log.d("is_show_location", search_section.toString());
//                    tv_locationName.setVisibility(View.VISIBLE);
//                    tv_locationName.setHint(search_section.getString("location_placeholder"));
                }
//                if (!TextUtils.isEmpty(search_section.getString("image"))) {
//                    Picasso.with(getContext()).load(search_section.getString("image"))
//                            .error(R.drawable.placeholder)
//                            .placeholder(R.drawable.placeholder)
//                            .into(backgroundImage);
//                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                Uri s = data.getData();
                tv_locationName.setText(s.toString());
            }
        }
    }


    public void adforest_showDynamicViews(JSONArray jsonArray) {
        Log.d("info ads_position", jsonArray.toString());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {

                if (jsonArray.get(i).equals("cat_icons") && responseData.getJSONArray("cat_icons").length() > 0) {
//                    CardView cardView = new CardView(getActivity());
//                    cardView.setCardElevation(3);
//                    cardView.setUseCompatPadding(true);
//                    cardView.setRadius(0);
//                    cardView.setContentPadding(5, 5, 5, 5);
                    LinearLayout linearLayout = new LinearLayout(getActivity());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);


                    LinearLayout firstLayout = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    firstLayout.setOrientation(LinearLayout.HORIZONTAL);
                    params2.setMargins(0, 10, 0, 10);
                    firstLayout.setLayoutParams(params2);


                    TextView title = new TextView(getActivity());
                    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params3.weight = 1;
                    title.setPaddingRelative(3, 3, 3, 3);
                    title.setTextColor(Color.BLACK);
                    title.setLayoutParams(params3);
                    title.setTextSize(18);
                    title.setText(settingsMain.getCatBtnTitle());
                    TextView buttonAll = new TextView(getActivity());
                    LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    buttonAll.setLayoutParams(params4);
                    buttonAll.setVisibility(View.GONE);
                    buttonAll.setPaddingRelative(23, 10, 23, 10);

                    firstLayout.addView(title);
                    firstLayout.addView(buttonAll);
//                    HomeCustomLayout.addView(firstLayout);
                    if (responseData.getJSONObject("cat_icons_column_btn").getBoolean("is_show")) {
                        buttonAll.setTextColor(Color.parseColor(settingsMain.getMainColor()));
//                        buttonAll.setBackground(CustomBorderDrawable.customButton(0, 0, 0, 0, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));
                        buttonAll.setVisibility(View.VISIBLE);
                        buttonAll.setText(responseData.getJSONObject("cat_icons_column_btn").getString("text"));


                        buttonAll.setOnClickListener(v -> {
                            FragmentAllCategories fragmentAllCategories = new FragmentAllCategories();
                            replaceFragment(fragmentAllCategories, "FragmentAllCategories");
                        });
                    }
                    linearLayout.addView(firstLayout);
                    RecyclerView recyclerView = new RecyclerView(getActivity());
                    recyclerView.setScrollContainer(false);
                    recyclerView.setPadding(8, 8, 8, 8);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setNestedScrollingEnabled(false);
                    ViewCompat.setNestedScrollingEnabled(recyclerView, false);
                    linearLayout.addView(recyclerView);
//                    try {
//                        if (responseData.getJSONObject("cat_icons_column_btn").getBoolean("is_show")) {
//                            Button button = new Button(getActivity());
//                            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                            buttonParams.setMargins(0, 10, 0, 20);
//                            buttonParams.setMarginStart(20);
//                            buttonParams.setMarginEnd(20);
//                            button.setText(responseData.getJSONObject("cat_icons_column_btn").getString("text"));
//
//                            button.setTextColor(Color.WHITE);
//                            button.setTextSize(14);
//                            button.setLayoutParams(buttonParams);
//                            button.setTransformationMethod(null);
//                            button.setBackground(CustomBorderDrawable.customButton(6, 6, 6, 6, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));
//                            linearLayout.addView(button);
//                            button.setOnClickListener(v -> {
//                                FragmentAllCategories fragmentAllCategories = new FragmentAllCategories();
//                                replaceFragment(fragmentAllCategories, "FragmentAllCategories");
//                            });
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    cardView.addView(linearLayout);

                    HomeCustomLayout.addView(linearLayout);
//                    responseData.getInt("cat_icons_column")
                    adforest_setAllCatgories(responseData.getJSONArray("cat_icons"),
                            recyclerView);
                }
                if (jsonArray.get(i).equals("sliders")) {

                    RecyclerView recyclerView = new RecyclerView(getActivity());
                    HomeCustomLayout.addView(recyclerView);
                    adforest_setAllRelated(responseData.getJSONArray("sliders"), recyclerView);
                }
                if (jsonArray.get(i).equals("featured_ads")) {

                    if (responseData.getBoolean("is_show_featured")) {

                        RecyclerView recyclerView = new RecyclerView(getActivity());
                        recyclerView.setPadding(4, 4, 4, 4);

                        TextView textView = new TextView(getActivity());
                        textView.setPadding(5, 5, 5, 5);
                        textView.setTextColor(Color.BLACK);
                        textView.setTextSize(18);

                        HomeCustomLayout.addView(textView);
                        HomeCustomLayout.addView(recyclerView);
                        adforest_setAllFeaturedAds(responseData.getJSONObject("featured_ads"), recyclerView, textView);
                    }
                }
                if (jsonArray.get(i).equals("latest_ads")) {
                    if (responseData.getBoolean("is_show_latest")) {
                        adforest_latesetAdsAndNearBy(responseData.getJSONObject("latest_ads"), latesetAdsList, "latest");
                    }
                }
                if (jsonArray.get(i).equals("cat_locations")) {
                    if (responseData.getJSONArray("cat_locations").length() > 0) {
                        if (settingsMain.getLocationStyle().equals("style1")) {
                            adforest_simpleLocationAds();
                        } else {
                            adforest_locationAds();
                        }
                    }


                }
                if (jsonArray.get(i).equals("nearby")) {
                    if (responseData.getBoolean("is_show_nearby")) {
                        adforest_latesetAdsAndNearBy(responseData.getJSONObject("nearby_ads"), nearByAdsList, "nearby");
                    }
                }
                if (jsonArray.get(i).equals("blogNews")) {
                    adforest_setBlogs();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void adforest_setBlogs() {
        try {
            LinearLayout firstLayout = new LinearLayout(getActivity());
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            firstLayout.setOrientation(LinearLayout.HORIZONTAL);
            firstLayout.setPadding(2, 2, 2, 2);
            firstLayout.setLayoutParams(params2);


            TextView title = new TextView(getActivity());
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params3.weight = 1;
            title.setPadding(3, 3, 3, 3);
            title.setTextColor(Color.BLACK);
            title.setLayoutParams(params3);
            title.setTextSize(18);

            TextView buttonAll = new TextView(getActivity());
            LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            buttonAll.setLayoutParams(params4);

            buttonAll.setPaddingRelative(23, 10, 23, 10);
            buttonAll.setTextColor(Color.WHITE);
            buttonAll.setBackground(CustomBorderDrawable.customButton(0, 0, 0, 0, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));

            firstLayout.addView(title);
            firstLayout.addView(buttonAll);

            RecyclerView recyclerView = new RecyclerView(getActivity());
            recyclerView.setPadding(4, 4, 4, 4);

            HomeCustomLayout.addView(firstLayout);
            HomeCustomLayout.addView(recyclerView);

            if (responseData.getBoolean("is_show_blog")) {
                JSONObject blogsobject = responseData.getJSONObject("latest_blog");
                JSONArray blogsArray = blogsobject.getJSONArray("blogs");
                if (blogsArray.length() > 0) {

                    blogsArrayList.clear();
                    buttonAll.setText(responseData.getString("view_all"));
                    title.setText(blogsobject.getString("text"));

                    GridLayoutManager MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
                    MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setLayoutManager(MyLayoutManager2);
                    ViewCompat.setNestedScrollingEnabled(recyclerView, false);

                    for (int i = 0; i < blogsArray.length(); i++) {
                        blogModel item = new blogModel();
                        JSONObject jsonObject = blogsArray.getJSONObject(i);
                        item.setPostId(jsonObject.getString("post_id"));
                        item.setName(jsonObject.getString("title"));
                        item.setCategory(jsonObject.getJSONArray("cats").getJSONObject(0).getString("name"));
                        item.setDate(jsonObject.getString("date"));
                        item.setImage(jsonObject.getString("image"));
                        item.setHasImage(jsonObject.getBoolean("has_image"));
                        blogsArrayList.add(item);
                    }
                    ItemBlogHomeAdapter itemBlogHomeAdapter = new ItemBlogHomeAdapter(getContext(), blogsArrayList);
                    recyclerView.setAdapter(itemBlogHomeAdapter);
                    itemBlogHomeAdapter.setOnItemClickListener(new BlogItemOnclicklinstener() {
                        @Override
                        public void onItemClick(blogModel item) {
                            BlogDetailFragment fragment = new BlogDetailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("id", item.getPostId());
                            fragment.setArguments(bundle);

                            replaceFragment(fragment, "BlogDetailFragment");
                        }
                    });
                    buttonAll.setOnClickListener(v -> replaceFragment(new BlogFragment(), "BlogFragment"));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void adforest_setAllFeaturedAds(JSONObject featureObject, RecyclerView featuredRecylerView,
                                            TextView textViewTitleFeature) {
        featureAdsList.clear();

        GridLayoutManager MyLayoutManager2 = null;
        FeaturedAdsLayout = settingsMain.getfeaturedAdsLayout();
        Log.d("featured", FeaturedAdsLayout);
        if (FeaturedAdsLayout.equals("vertical")) {
            MyLayoutManager2 = new GridLayoutManager(getActivity(), 2);
            MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        } else if (FeaturedAdsLayout.equals("default")) {
            MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
            MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        if (FeaturedAdsLayout.equals("horizental")) {
            MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
            MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        }
        featuredRecylerView.setHasFixedSize(true);
        featuredRecylerView.setNestedScrollingEnabled(false);
        featuredRecylerView.setLayoutManager(MyLayoutManager2);

        ViewCompat.setNestedScrollingEnabled(featuredRecylerView, false);

        try {

            textViewTitleFeature.setText(featureObject.getString("text"));
            if (featureObject.getJSONArray("ads").length() > 0)
                for (int i = 0; i < featureObject.getJSONArray("ads").length(); i++) {

                    catSubCatlistModel item = new catSubCatlistModel();
                    JSONObject object = featureObject.getJSONArray("ads").getJSONObject(i);

                    item.setAddTypeFeature(object.getJSONObject("ad_status").getString("featured_type_text"));
                    item.setId(object.getString("ad_id"));
                    item.setCardName(object.getString("ad_title"));
                    item.setDate(object.getString("ad_date"));
                    item.setAdViews(object.getString("ad_views"));
                    item.setPath(object.getString("ad_cats_name"));
                    item.setPrice(object.getJSONObject("ad_price").getString("price"));
                    item.setPriceType(object.getJSONObject("ad_price").getString("price_type"));
                    item.setCatName(object.getString("ad_cats_name"));
                    item.setImageResourceId((object.getJSONArray("ad_images").getJSONObject(0).getString("thumb")));
                    item.setLocation(object.getJSONObject("ad_location").getString("address"));
                    item.setIsfav(object.getJSONObject("ad_saved").getInt("is_saved"));
                    item.setFavBtnText(object.getJSONObject("ad_saved").getString("text"));
                    item.setIs_show_countDown(object.getJSONObject("ad_timer").getBoolean("is_show"));
                    if (object.getJSONObject("ad_timer").getBoolean("is_show"))
                        item.setTimer_array(object.getJSONObject("ad_timer").getJSONArray("timer"));
                    item.setIsturned(1);
                    featureAdsList.add(item);
                }
            MarvelItemSearchFeatureAdsAdapter itemFeatureAdsAdapter = new MarvelItemSearchFeatureAdsAdapter(getActivity(), featureAdsList);
            itemFeatureAdsAdapter.setHorizontelAd(FeaturedAdsLayout);
//            itemFeatureAdsAdapter.setMultiLine(false);
            featuredRecylerView.setAdapter(itemFeatureAdsAdapter);


            itemFeatureAdsAdapter.setOnItemClickListener(new CatSubCatOnclicklinstener() {
                @Override
                public void onItemClick(catSubCatlistModel item) {
                    Intent intent = new Intent(getActivity(), Ad_detail_activity.class);
                    intent.putExtra("adId", item.getId());
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }

                @Override
                public void onItemTouch(catSubCatlistModel item) {
                }

                @Override
                public void addToFavClick(View v, String position) {
                }
            });
            if (settingsMain.isFeaturedScrollEnable()) {
                adforest_recylerview_autoScroll(settingsMain.getFeaturedScroolDuration(),
                        40, settingsMain.getFeaturedScroolLoop(),
                        featuredRecylerView, MyLayoutManager2, itemFeatureAdsAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void adforest_setAllCatgories(JSONArray jsonArray, RecyclerView recyclerView) {
        listitems = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                homeCatListModel item = new homeCatListModel();
                item.setTitle(jsonArray.optJSONObject(i).optString("name"));
                item.setThumbnail(jsonArray.optJSONObject(i).optString("img"));
                item.setId(jsonArray.optJSONObject(i).optString("cat_id"));
                item.setHas_children(jsonArray.optJSONObject(i).optBoolean("has_sub"));

//                item.setColor(String.valueOf(Color.parseColor("#a4c639")));

                listitems.add(item);
            }
        } catch (NumberFormatException num) {
            num.printStackTrace();
        }


        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);


        recyclerView.setLayoutManager(MyLayoutManager);
        int spacing = 0; // 50px


//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, spacing, false));
        MarvelItemMainAllCatAdapter adapter = new MarvelItemMainAllCatAdapter(context, listitems, 4);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(item -> {
            if (item.isHas_children()) {
                FragmentAllCategories fragmentAllCategories = new FragmentAllCategories();
                Bundle bundle = new Bundle();
                bundle.putString("term_id", item.getId());
                bundle.putString("term_name", item.getTitle());
                fragmentAllCategories.fromMulti = true;
                fragmentAllCategories.setArguments(bundle);
                replaceFragment(fragmentAllCategories, "FragmentAllCategories");

            } else {
                FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getId());
                bundle.putString("title", "");
                fragment_search.setArguments(bundle);
                replaceFragment(fragment_search, "FragmentCatSubNSearch");
            }

        });


    }

    private void adforest_setAllRelated(JSONArray jsonArray, RecyclerView recyclerView) {

        listitemsRelated.clear();

        GridLayoutManager MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
        MyLayoutManager2.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(MyLayoutManager2);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        Log.d("data array", "" + jsonArray.length());
        for (int each = 0; each < jsonArray.length(); each++) {
            homeCatRelatedList relateItem = new homeCatRelatedList();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(each);

                relateItem.setTitle(jsonObject.getString("name"));
                relateItem.setViewAllBtnText(btnViewAllText);
                relateItem.setCatId(jsonObject.getString("cat_id"));
                JSONArray innerList = jsonObject.getJSONArray("data");

                ArrayList<catSubCatlistModel> list = new ArrayList<>();

                for (int i = 0; i < innerList.length(); i++) {
                    catSubCatlistModel item = new catSubCatlistModel();

                    item.setId(innerList.getJSONObject(i).getString("ad_id"));
                    item.setCardName(innerList.getJSONObject(i).getString("ad_title"));
                    item.setDate(innerList.getJSONObject(i).getString("ad_date"));
                    item.setPrice(innerList.getJSONObject(i).getJSONObject("ad_price").getString("price"));
                    item.setPriceType(innerList.getJSONObject(i).getJSONObject("ad_price").getString("price_type"));
                    item.setLocation(innerList.getJSONObject(i).getJSONObject("ad_location").getString("address"));
                    item.setImageResourceId(innerList.getJSONObject(i).getJSONArray("ad_images").getJSONObject(0).getString("thumb"));
                    item.setCatName(innerList.getJSONObject(i).getString("ad_cats_name"));
                    item.setIs_show_countDown(innerList.getJSONObject(i).getJSONObject("ad_timer").getBoolean("is_show"));
                    if (innerList.getJSONObject(i).getJSONObject("ad_timer").getBoolean("is_show"))
                        item.setTimer_array(innerList.getJSONObject(i).getJSONObject("ad_timer").getJSONArray("timer"));
                    list.add(item);
                }

                relateItem.setArrayList(list);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listitemsRelated.add(relateItem);
        }


        MarvelItemMainCAT_Related_All itemMainCAT_related_all = new MarvelItemMainCAT_Related_All(context, listitemsRelated);
        recyclerView.setAdapter(itemMainCAT_related_all);

        itemMainCAT_related_all.setOnItemClickListener(new MyAdsOnclicklinstener() {
            @Override
            public void onItemClick(myAdsModel item) {
            }

            @Override
            public void delViewOnClick(View v, int position) {
                FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                Bundle bundle = new Bundle();
                bundle.putString("id", v.getTag().toString());
                bundle.putString("title", "");

                fragment_search.setArguments(bundle);
                replaceFragment(fragment_search, "FragmentCatSubNSearch");
            }

            @Override
            public void editViewOnClick(View v, int position) {

            }
        });

    }

    private void adforest_latesetAdsAndNearBy(JSONObject jsonObject, ArrayList arrayList, String checkAdsType) {

        LinearLayout firstLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        firstLayout.setOrientation(LinearLayout.HORIZONTAL);
        params2.setMargins(0, 10, 0, 10);
        firstLayout.setLayoutParams(params2);


        TextView title = new TextView(getActivity());
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3.weight = 1;
        title.setPaddingRelative(3, 3, 3, 3);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(params3);
        title.setTextSize(18);

        TextView buttonAll = new TextView(getActivity());
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonAll.setLayoutParams(params4);

        buttonAll.setPaddingRelative(23, 10, 23, 10);
        buttonAll.setTextColor(Color.WHITE);
        buttonAll.setBackground(CustomBorderDrawable.customButton(0, 0, 0, 0, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));

        firstLayout.addView(title);
        firstLayout.addView(buttonAll);

        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setPaddingRelative(4, 4, 4, 4);


        HomeCustomLayout.addView(firstLayout);
        HomeCustomLayout.addView(recyclerView);
        adforest_setAllLatesetAds(title, buttonAll, recyclerView, jsonObject, arrayList, checkAdsType);
    }

    private void adforest_setAllLatesetAds(TextView title, final TextView viewAll, RecyclerView recyclerView,
                                           JSONObject jsonObject, ArrayList arrayList, final String checkAdsType) {

        arrayList.clear();
        GridLayoutManager MyLayoutManager2 = null;
        latestAdsLayout = settingsMain.getlatestAdsLayout();
        nearByAdsLayout = settingsMain.getnearbyAdsLayout();


        if (checkAdsType.equals("nearby")) {
            if (nearByAdsLayout.equals("vertical")) {
                MyLayoutManager2 = new GridLayoutManager(getActivity(), 2);
                MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
            } else if (nearByAdsLayout.equals("default")) {
                MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
                MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            }
            if (nearByAdsLayout.equals("horizental")) {
                MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
                MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

            }
        } else {
            if (latestAdsLayout.equals("vertical")) {
                MyLayoutManager2 = new GridLayoutManager(getActivity(), 2);
                MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
            } else if (latestAdsLayout.equals("default")) {
                MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
                MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            }
            if (latestAdsLayout.equals("horizental")) {
                MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
                MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

            }
        }
        recyclerView.setHasFixedSize(true);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(MyLayoutManager2);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);


        try {
            JSONObject object = jsonObject;

            JSONArray data = object.getJSONArray("ads");

            title.setText(object.getString("text"));
            viewAll.setText(responseData.getString("view_all"));


            for (int i = 0; i < data.length(); i++) {
                catSubCatlistModel item = new catSubCatlistModel();

                item.setId(data.getJSONObject(i).getString("ad_id"));
                item.setCardName(data.getJSONObject(i).getString("ad_title"));
                item.setDate(data.getJSONObject(i).getString("ad_date"));
                item.setPrice(data.getJSONObject(i).getJSONObject("ad_price").getString("price"));
                item.setLocation(data.getJSONObject(i).getJSONObject("ad_location").getString("address"));
                item.setImageResourceId(data.getJSONObject(i).getJSONArray("ad_images").getJSONObject(0).getString("thumb"));
                item.setPriceType(data.getJSONObject(i).getJSONObject("ad_price").getString("price_type"));
                item.setCatName(data.getJSONObject(i).getString("ad_cats_name"));
                item.setIs_show_countDown(data.getJSONObject(i).getJSONObject("ad_timer").getBoolean("is_show"));
                if (data.getJSONObject(i).getJSONObject("ad_timer").getBoolean("is_show"))
                    item.setTimer_array(data.getJSONObject(i).getJSONObject("ad_timer").getJSONArray("timer"));
                arrayList.add(item);
            }
            if (checkAdsType.equals("nearby")) {
                MarvelItemMainHomeRelatedAdapter adapter = new MarvelItemMainHomeRelatedAdapter(getActivity(), arrayList);
                adapter.fromNearBy = true;
                adapter.setHorizontelAd(nearByAdsLayout);
                adapter.checkAdsType = "nearby";
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new OnItemClickListener2() {
                    @Override
                    public void onItemClick(catSubCatlistModel item) {
                        Log.d("item_id", item.getId());
                        Intent intent = new Intent(getActivity(), Ad_detail_activity.class);
                        intent.putExtra("adId", item.getId());
                        startActivity(intent);
                    }
                });
            } else {
                MarvelItemMainHomeRelatedAdapter adapter = new MarvelItemMainHomeRelatedAdapter(getActivity(), arrayList);
                adapter.fromSlider = false;
                adapter.checkAdsType = "latest";
                adapter.setHorizontelAd(latestAdsLayout);

                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new OnItemClickListener2() {
                    @Override
                    public void onItemClick(catSubCatlistModel item) {
                        Log.d("item_id", item.getId());
                        Intent intent = new Intent(getActivity(), Ad_detail_activity.class);
                        intent.putExtra("adId", item.getId());
                        startActivity(intent);
                    }
                });
            }
            viewAll.setOnClickListener(v -> {
                if (checkAdsType.equals("nearby")) {
                    FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                    Bundle bundle = new Bundle();
                    bundle.putString("nearby_latitude", settingsMain.getLatitude());
                    bundle.putString("nearby_longitude", settingsMain.getLongitude());
                    bundle.putString("nearby_distance", settingsMain.getDistance());

                    fragment_search.setArguments(bundle);
                    replaceFragment(fragment_search, "FragmentCatSubNSearch");
                }
                if (checkAdsType.equals("latest")) {
                    FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", "");
                    bundle.putString("title", "");

                    fragment_search.setArguments(bundle);
                    replaceFragment(fragment_search, "FragmentCatSubNSearch");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void adforest_simpleLocationAds() {
        TextView title = new TextView(getActivity());
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        title.setPadding(3, 3, 3, 3);
        title.setPaddingRelative(3, 3, 3, 3);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(params3);
        title.setTextSize(17);


        CardView cardView = new CardView(getActivity());
        cardView.setCardElevation(3);
        cardView.setUseCompatPadding(true);
        cardView.setRadius(0);
//        cardView.setContentPadding(5, 5, 5, 5);
        cardView.setPaddingRelative(5, 5, 5, 5);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setScrollContainer(false);
//        recyclerView.setPadding(5, 5, 5, 5);
        recyclerView.setPaddingRelative(5, 5, 5, 5);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        linearLayout.addView(recyclerView);

        try {
            if (responseData.getJSONObject("cat_locations_btn").getBoolean("is_show")) {
                Button button = new Button(getActivity());
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonParams.setMargins(0, 10, 0, 20);
                buttonParams.setMarginStart(20);
                buttonParams.setMarginEnd(20);
                button.setText(responseData.getJSONObject("cat_locations_btn").getString("text"));

                button.setTextColor(Color.WHITE);
                button.setTextSize(14);
                button.setLayoutParams(buttonParams);
                button.setTransformationMethod(null);
                button.setBackground(CustomBorderDrawable.customButton(6, 6, 6, 6, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));
                linearLayout.addView(button);
                button.setOnClickListener(v -> {
                    FragmentAllLocations fragmentAllLocations = new FragmentAllLocations();
                    replaceFragment(fragmentAllLocations, "FragmentAllLocations");
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cardView.addView(linearLayout);

        HomeCustomLayout.addView(title);
        HomeCustomLayout.addView(cardView);
        try {
            title.setText(responseData.getString("cat_locations_title"));
            adforest_simple_SetAllLocationAds(responseData.getJSONArray("cat_locations"), recyclerView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void adforest_locationAds() {
        TextView title = new TextView(getActivity());
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        title.setPadding(3, 3, 3, 3);
        title.setPaddingRelative(3, 3, 3, 3);
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(params3);
        title.setTextSize(17);


//        CardView cardView = new CardView(getActivity());
//        cardView.setCardElevation(3);
//        cardView.setUseCompatPadding(true);
//        cardView.setRadius(0);
////        cardView.setContentPadding(5, 5, 5, 5);
//        cardView.setPaddingRelative(5, 5, 5, 5);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

//        RecyclerView recyclerView = new RecyclerView(getActivity());
        DiscreteScrollView recyclerView = new DiscreteScrollView(getActivity());

        recyclerView.setScrollContainer(false);
//        recyclerView.setPadding(5, 5, 5, 5);
        recyclerView.setPaddingRelative(2, 5, 2, 5);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        linearLayout.addView(recyclerView);

        try {
            if (responseData.getJSONObject("cat_locations_btn").getBoolean("is_show")) {
                Button button = new Button(getActivity());
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonParams.setMargins(0, 10, 0, 20);
                buttonParams.setMarginStart(20);
                buttonParams.setMarginEnd(20);
                button.setText(responseData.getJSONObject("cat_locations_btn").getString("text"));

                button.setTextColor(Color.WHITE);
                button.setTextSize(14);
                button.setLayoutParams(buttonParams);
                button.setTransformationMethod(null);
                button.setBackground(CustomBorderDrawable.customButton(6, 6, 6, 6, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));
                linearLayout.addView(button);
                button.setOnClickListener(v -> {
                    FragmentAllLocations fragmentAllLocations = new FragmentAllLocations();
                    replaceFragment(fragmentAllLocations, "FragmentAllLocations");
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        cardView.addView(linearLayout);

        HomeCustomLayout.addView(title);
        HomeCustomLayout.addView(linearLayout);
        try {
            title.setText(responseData.getString("cat_locations_title"));

            adforest_setAllLocationAds(responseData.getJSONArray("cat_locations"), recyclerView);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void adforest_simple_SetAllLocationAds(JSONArray jsonArray, RecyclerView recyclerView) {
        locationAdscat.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            homeCatListModel item = new homeCatListModel();
            try {
                Log.d("info location icons", jsonArray.getJSONObject(i).getString("count"));
                item.setTitle(jsonArray.optJSONObject(i).getString("name"));
                item.setThumbnail(jsonArray.optJSONObject(i).getString("img"));
                item.setId(jsonArray.optJSONObject(i).getString("cat_id"));
                item.setAdsCount(jsonArray.getJSONObject(i).getString("count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            locationAdscat.add(item);
        }

        GridLayoutManager MyLayoutManager = new GridLayoutManager(getActivity(), 2);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(MyLayoutManager);
        int spacing = 15; // 50px

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, false));

        ItemMainAllLocationAds adapter = new ItemMainAllLocationAds(context, locationAdscat, 2);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(homeCatListModel item) {
                FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
                Bundle bundle = new Bundle();
                bundle.putString("ad_country", item.getId());

                fragment_search.setArguments(bundle);
                replaceFragment(fragment_search, "FragmentCatSubNSearch");

            }
        });

    }

    private void adforest_setAllLocationAds(JSONArray jsonArray, DiscreteScrollView recyclerView) {
        locationAdscat.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            homeCatListModel item = new homeCatListModel();
            try {
                Log.d("info location icons", jsonArray.getJSONObject(i).getString("count"));
                item.setTitle(jsonArray.optJSONObject(i).getString("name"));
                item.setThumbnail(jsonArray.optJSONObject(i).getString("img"));
                item.setId(jsonArray.optJSONObject(i).getString("cat_id"));
                item.setAdsCount(jsonArray.getJSONObject(i).getString("count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            locationAdscat.add(item);
        }
        recyclerView.setOrientation(DSVOrientation.HORIZONTAL);

        recyclerView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        recyclerView.setSlideOnFlingThreshold(500);

        infiniteAdapter = InfiniteScrollAdapter.wrap(new MultiItemMainAllLocationAds(context, locationAdscat, 2));
        recyclerView.setAdapter(infiniteAdapter);


    }

//    private void adforest_locationAds() {
//        TextView title = new TextView(getActivity());
//        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        title.setPadding(3, 3, 3, 3);
//        title.setPaddingRelative(3, 3, 3, 3);
//        title.setTextColor(Color.BLACK);
//        title.setLayoutParams(params3);
//        title.setTextSize(17);
//
//
//        CardView cardView = new CardView(getActivity());
//        cardView.setCardElevation(3);
//        cardView.setUseCompatPadding(true);
//        cardView.setRadius(0);
////        cardView.setContentPadding(5, 5, 5, 5);
//        cardView.setPaddingRelative(5, 5, 5, 5);
//
//        LinearLayout linearLayout = new LinearLayout(getActivity());
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//
//        RecyclerView recyclerView = new RecyclerView(getActivity());
//        recyclerView.setScrollContainer(false);
////        recyclerView.setPadding(5, 5, 5, 5);
//        recyclerView.setPaddingRelative(5, 5, 5, 5);
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
//        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
//        linearLayout.addView(recyclerView);
//
//        try {
//            if (responseData.getJSONObject("cat_locations_btn").getBoolean("is_show")) {
//                Button button = new Button(getActivity());
//                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                buttonParams.setMargins(0, 10, 0, 20);
//                buttonParams.setMarginStart(20);
//                buttonParams.setMarginEnd(20);
//                button.setText(responseData.getJSONObject("cat_locations_btn").getString("text"));
//
//                button.setTextColor(Color.WHITE);
//                button.setTextSize(14);
//                button.setLayoutParams(buttonParams);
//                button.setTransformationMethod(null);
//                button.setBackground(CustomBorderDrawable.customButton(6, 6, 6, 6, settingsMain.getMainColor(), settingsMain.getMainColor(), settingsMain.getMainColor(), 3));
//                linearLayout.addView(button);
//                button.setOnClickListener(v -> {
//                    FragmentAllLocations fragmentAllLocations = new FragmentAllLocations();
//                    replaceFragment(fragmentAllLocations, "FragmentAllLocations");
//                });
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        cardView.addView(linearLayout);
//
//        HomeCustomLayout.addView(title);
//        HomeCustomLayout.addView(cardView);
//        try {
//            title.setText(responseData.getString("cat_locations_title"));
//            adforest_setAllLocationAds(responseData.getJSONArray("cat_locations"), recyclerView);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void adforest_setAllLocationAds(JSONArray jsonArray, RecyclerView recyclerView) {
//        locationAdscat.clear();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            homeCatListModel item = new homeCatListModel();
//            try {
//                Log.d("info location icons", jsonArray.getJSONObject(i).getString("count"));
//                item.setTitle(jsonArray.optJSONObject(i).getString("name"));
//                item.setThumbnail(jsonArray.optJSONObject(i).getString("img"));
//                item.setId(jsonArray.optJSONObject(i).getString("cat_id"));
//                item.setAdsCount(jsonArray.getJSONObject(i).getString("count"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            locationAdscat.add(item);
//        }
//
//        GridLayoutManager MyLayoutManager = new GridLayoutManager(getActivity(), 2);
//        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
//
//        recyclerView.setLayoutManager(MyLayoutManager);
//        int spacing = 15; // 50px
//
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, false));
//
//        ItemMainAllLocationAds adapter = new ItemMainAllLocationAds(context, locationAdscat, 2);
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(homeCatListModel item) {
//                FragmentCatSubNSearch fragment_search = new FragmentCatSubNSearch();
//                Bundle bundle = new Bundle();
//                bundle.putString("ad_country", item.getId());
//
//                fragment_search.setArguments(bundle);
//                replaceFragment(fragment_search, "FragmentCatSubNSearch");
//
//            }
//        });
//
//    }

    void replaceFragment(Fragment someFragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);
        transaction.replace(R.id.frameContainer, someFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    private void googleAnalytics() {
        if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals("")) {
            AnalyticsTrackers.initialize(getActivity());
            Log.d("analyticsID", settingsMain.getAnalyticsId());
            AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, settingsMain.getAnalyticsId());
        }

        if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals("") && AnalyticsTrackers.getInstance() != null)
            AnalyticsTrackers.getInstance().trackScreenView("Home");
        super.onResume();
    }

    private void adforest_AddFirebaseid(String regId) {
        if (SettingsMain.isConnectingToInternet(getActivity())) {


            JsonObject params = new JsonObject();


            params.addProperty("firebase_id", regId);
            Log.e("info send FireBase ", "Firebase reg id: " + regId);

            Call<ResponseBody> myCall = restService.postFirebaseId(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info FireBase Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info Data FireBase", response.getJSONObject("data").toString());
                                settingsMain.setFireBaseId(response.getJSONObject("data").getString("firebase_reg_id"));
                                Log.d("info FireBase ID", response.getJSONObject("data").getString("firebase_reg_id"));
                                Log.d("info FireBase", "Firebase id is set with server.!");
                            }
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        e.printStackTrace();
                    } catch (IOException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info FireBase ", "NullPointert Exception" + t.getLocalizedMessage());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        Log.d("info FireBase err", String.valueOf(t));
                        Log.d("info FireBase err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    public void AdsNDAnalytics() {
        new Handler().postDelayed(() -> {
//                Log.d("AdsType", settingsMain.getAdsType());
            if (settingsMain.getInterstitalShow()) {
                adforest_InterstitalAds();
            }
            if (settingsMain.getBannerShow() && settingsMain.isAdShowOrNot()) {
                adforest_bannersAds();
            }

        }, 1500);
    }

    //&& settingsMain.getAdsDisplayTime() != 0 && settingsMain.getAdsInitialTime() != 0
    public void adforest_InterstitalAds() {
        if (settingsMain.getAdsShow() && !settingsMain.getInterstitialAdsId().equals("")) {
            Admob.loadInterstitial(getActivity());

        }
//        else{
//            Admob.adforest_cancelInterstitial();
//            Toast.makeText(context, "caneled", Toast.LENGTH_LONG).show();
//        }
    }

    public void adforest_bannersAds() {
        adsCounter = 1;
        if (settingsMain.getAdsShow() && !settingsMain.getBannerAdsId().equals("")) {
            if (settingsMain.getAdsPostion().equals("top")) {
                LinearLayout frameLayout = getActivity().findViewById(R.id.adView);
                Admob.adforest_Displaybanners(getActivity(), frameLayout);
            } else {
                LinearLayout frameLayout = getActivity().findViewById(R.id.adViewBelow);
                FrameLayout maimFrame = getActivity().findViewById(R.id.frameContainer);
//                maimFrame.setPaddingRelative(0,0,0,100);
                Admob.adforest_Displaybanners(getActivity(), frameLayout);
            }
        }
    }

    private void adforest_setCustomPagesIcon(ArrayList<Drawable> drawables) throws JSONException {
        final JSONArray jsonArray = jsonObjectSubMenu.getJSONArray("pages");
        for (int i = 0; i < jsonArray.length(); i++) {
            menu.findItem(R.id.custom).getSubMenu().getItem(i).setIcon(drawables.get(i));

        }
    }


//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        if (i > 0) {
//            locationIDd = spinnerID.get(i - 1);
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncImageTask extends AsyncTask<Void, Void, ArrayList<Drawable>> {
        @Override
        protected void onPostExecute(ArrayList<Drawable> drawables) {
            super.onPostExecute(drawables);
            try {
                adforest_setCustomPagesIcon(drawables);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<Drawable> doInBackground(Void... strings) {
            Bitmap bitmap = null;
            ArrayList<Drawable> drawables = new ArrayList<>();
            try {
                final JSONArray jsonArray = jsonObjectSubMenu.getJSONArray("pages");
                for (int i = 0; i < jsonArray.length(); i++) {
                    HttpURLConnection connection = (HttpURLConnection) new URL(jsonArray.getJSONObject(i).getString("url")).openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                    drawables.add(new BitmapDrawable(bitmap));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NetworkOnMainThreadException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return drawables;
        }

        @Override
        protected void onPreExecute() {

        }

    }
}




