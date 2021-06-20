package com.isajoh.app.home;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.faltenreich.skeletonlayout.Skeleton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.JsonObject;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.opensooq.supernova.gligar.GligarPicker;
import com.wonshinhyo.dragrecyclerview.DragRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import jp.wasabeef.richeditor.RichEditor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.isajoh.app.home.ProgressRequestBody;
import com.isajoh.app.R;
import com.isajoh.app.SplashScreen;
import com.isajoh.app.ad_detail.Ad_detail_activity;
import com.isajoh.app.adapters.SpinnerAndListAdapter;
import com.isajoh.app.helper.LocaleHelper;
import com.isajoh.app.helper.MyAdsOnclicklinstener;
import com.isajoh.app.helper.WorkaroundMapFragment;
import com.isajoh.app.home.adapter.ItemEditImageAdapter;
import com.isajoh.app.home.helper.AdPostImageModel;
import com.isajoh.app.home.helper.CalanderTextModel;
import com.isajoh.app.home.helper.ProgressModel;
import com.isajoh.app.modelsList.myAdsModel;
import com.isajoh.app.modelsList.subcatDiloglist;
import com.isajoh.app.utills.Admob;
import com.isajoh.app.utills.AnalyticsTrackers;
import com.isajoh.app.utills.CircularProgressBar;
import com.isajoh.app.utills.GPSTracker;
import com.isajoh.app.utills.Network.RestService;
import com.isajoh.app.utills.RuntimePermissionHelper;
import com.isajoh.app.utills.SettingsMain;
import com.isajoh.app.utills.UrlController;


public class EditAdPost extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener
        , RuntimePermissionHelper.permissionInterface, AdapterView.OnItemClickListener, PermissionsListener, LocationEngineCallback<LocationEngineResult>, com.mapbox.mapboxsdk.maps.OnMapReadyCallback, OnLocationClickListener, OnCameraTrackingChangedListener, ProgressRequestBody.UploadCallbacks {

    private static final String TAG = "Sample";
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    protected GoogleMap mMap;
    SwitchDateTimeDialogFragment dateTimeFragment;
    List<View> page1RequiredFields = new ArrayList<>();
    List<View> page2RequiredFields = new ArrayList<>();
    AutoCompleteTextView mAutocompleteTextView;
    CardView cardViewPriceInput;
    boolean edittextShowHide = true, checkSpineerCliked = true;
    EditText editTextPrice, editTextTitle, editTexttBidingTimer;
    List<View> allViewInstanceforCustom = new ArrayList<>();
    List<View> requiredFields = new ArrayList<>();
    List<View> requiredFieldsForCustom = new ArrayList<>();
    JSONObject jsonObject, jsonObjectforCustom;
    TextView textViewUserName, textViewUserPhone, textViewLat, textViewLong, textViewLocation, textViewTitle,
            textViewadCountry, btnPostAd, bumpAdTV, featureAdTV, terms_conditionsTitleTV, featureAdByPackages, Gallary, tv_done;
    TextView btnImageChooser, btnSelectPix, tv_uploading;
    EditText editTextUserName, editTextUserPhone,
            editTextUserLat, editTextuserLong;
    Activity context;
    FrameLayout frameLayout, loadingLayout;
    SettingsMain settingsMain;
    LinearLayout linearLayoutCustom;
    String catID, stringImageLimitText = "", require_message = "";
    boolean ison = false;
    List<View> allViewInstance = new ArrayList<>();
    LinearLayout page1Layout, page2Layout, linearLayoutMapView;
    CircularProgressBar c3;
    int imageLimit, per_limit;
    LinearLayout page1, page2, page3, showHideLocation;
    ImageView imageViewNext1, imageViewNext2, imageViewBack1, imageViewBack2;
    List<File> allFile = new ArrayList<>();
    int addId;
    String updateId;
    HorizontalScrollView horizontalScrollView;
    ItemEditImageAdapter itemEditImageAdapter;
    ArrayList<myAdsModel> myImages;
    DragRecyclerView recyclerView;
    TextView textViewInfo;
    Spinner spinnerLocation;
    RestService restService;
    RelativeLayout bumAdLayout, featureAdLayout, terms_ConditionLayout;
    CheckBox featureAdChkBox, chkBxBumpAd, terms_conditionChkBox;
    RuntimePermissionHelper runtimePermissionHelper;
    ProgressBar progress_bar;
    ArrayList<String> places = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    private NestedScrollView mScrollView;
    private Boolean spinnerTouched = false;
    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<String> imagePaths;
    private TextView textViewInfoforDrag;
    private CardView cardViewPriceType, cardViewtBidingTimer, meraCardView, catCard;
    private Calendar myCalendar = Calendar.getInstance();
    private int currentFileNumber = 1;
    private int totalFiles = 1, currentSize, totalUploadedImages;
    private AdPostImageModel adPostImageModel;
    private ProgressModel progressModel;
    private PlacesClient placesClient;
    private static Animation shakeAnimation;
    EditText placesContainer;
    MapView mapView;
    Marker marker;
    double lat_by_mapbox, lon_by_mapbox;
    String address_by_mapbox;
    LinearLayout latlongLayout;
    ImageButton locationButton;
    LatLng point;
    MapboxMap mapBoxMap;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    LocationEngineRequest request;
    LocationComponent locationComponent;
    private boolean isInTrackingMode;
    int successfullyUploadedImagesCount;
    AsyncImageTask asyncImageTask;
    Boolean isRequired = false;
    public boolean bid_check = false;
    int imageRequestCount = 1;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout loadingLinearLayout;

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_edit_ad_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        updateId = intent.getStringExtra("id");
        runtimePermissionHelper = new RuntimePermissionHelper(this, this);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.editorToolbar);

        settingsMain = new SettingsMain(this);
        context = EditAdPost.this;
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(settingsMain.getMainColor()));
        }

        toolbar.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        mScrollView = (NestedScrollView) findViewById(R.id.scrollView);

        WorkaroundMapFragment mapFragment = ((WorkaroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.setListener(() -> mScrollView.requestDisallowInterceptTouchEvent(true));

        mapFragment.getMapAsync(this);
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmerFrameLayout);
        loadingLinearLayout = (LinearLayout) findViewById(R.id.shimmerMain);
        editTextUserName = (EditText) findViewById(R.id.yourNameET);
        editTextUserPhone = (EditText) findViewById(R.id.phoneNumberET);
        frameLayout = (FrameLayout) findViewById(R.id.frame);

        textViewInfo = (TextView) findViewById(R.id.textView21);

        btnPostAd = (TextView) findViewById(R.id.postAd);
        btnPostAd.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        btnImageChooser = (TextView) findViewById(R.id.Gallary);
        btnSelectPix = (TextView) findViewById(R.id.selectPix);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        tv_uploading = (TextView) findViewById(R.id.tv_uploading);
        loadingLayout = (FrameLayout) findViewById(R.id.loadingLayout);
        Gallary = (TextView) findViewById(R.id.Gallary);
        tv_done = (TextView) findViewById(R.id.tv_done);
        progressModel = SettingsMain.getProgressSettings(context);
        adPostImageModel = settingsMain.getAdPostImageModel(context);

        page1Layout = (LinearLayout) findViewById(R.id.customLayout1);
        page2Layout = (LinearLayout) findViewById(R.id.customLayout2);
        linearLayoutCustom = (LinearLayout) findViewById(R.id.customFieldLayout);
        linearLayoutMapView = (LinearLayout) findViewById(R.id.mapViewONOFF);
        latlongLayout = findViewById(R.id.latlongLayout);
        placesContainer = findViewById(R.id.placeContainer);
        placesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(getString(R.string.access_token))
                        .placeOptions(PlaceOptions.builder().backgroundColor(Color.parseColor("#EEEEEE")).limit(10).build(PlaceOptions.MODE_CARDS))
                        .build(EditAdPost.this);
                startActivityForResult(intent, 35);
            }
        });
        editTextUserLat = (EditText) findViewById(R.id.latET);
        editTextuserLong = (EditText) findViewById(R.id.longET);

        page1 = (LinearLayout) findViewById(R.id.line1);
        page2 = (LinearLayout) findViewById(R.id.line2);
        page3 = (LinearLayout) findViewById(R.id.line3);
        showHideLocation = (LinearLayout) findViewById(R.id.line4);

        imageViewBack1 = (ImageView) findViewById(R.id.back1);
        imageViewBack2 = (ImageView) findViewById(R.id.back2);
        imageViewNext1 = (ImageView) findViewById(R.id.next1);
        imageViewNext2 = (ImageView) findViewById(R.id.next2);

        page1.setVisibility(View.VISIBLE);
        page2.setVisibility(View.GONE);
        page3.setVisibility(View.GONE);

        textViewInfoforDrag = (TextView) findViewById(R.id.textView27);

        textViewInfoforDrag.setVisibility(View.GONE);
        recyclerView = (DragRecyclerView) findViewById(R.id.cardView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        GridLayoutManager MyLayoutManager = new GridLayoutManager(context, 3);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(MyLayoutManager);
        restService = UrlController.createService(RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), this);
        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shake);
        btnPostAd.setOnClickListener(view -> {


            boolean b = false;
            if (editTextUserName.getText().toString().isEmpty()) {
                b = true;

            }
            if (mAutocompleteTextView.getVisibility() == View.VISIBLE) {
                if (mAutocompleteTextView.getText().toString().isEmpty()) {
                    b = true;
                }
            }

            if (edittextShowHide) {
                if (editTextPrice != null) {
                    if (editTextPrice.getText().toString().equals("")) {
                        editTextPrice.setError("!");
                        b = true;
                    }
                }
            }
            if (terms_ConditionLayout.getVisibility() == View.VISIBLE && !terms_conditionChkBox.isChecked()) {
                Log.d("oeee", String.valueOf(!terms_conditionChkBox.isChecked()));
                terms_ConditionLayout.startAnimation(shakeAnimation);
                Toast.makeText(context, require_message, Toast.LENGTH_SHORT).show();
                b = true;
            }
            if (b) {
                if (editTextUserName.getText().toString().isEmpty()) {
                    editTextUserName.requestFocus();
                    editTextUserName.setError("!");
                    Toast.makeText(context, require_message, Toast.LENGTH_SHORT).show();
                }
                if (mAutocompleteTextView.getVisibility() == View.VISIBLE) {
                    if (mAutocompleteTextView.getText().toString().isEmpty()) {
                        mAutocompleteTextView.requestFocus();
                        mAutocompleteTextView.setError("!");
                        Toast.makeText(context, require_message, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {

                adforest_submitQuery(adforest_getDataFromDynamicViews());
            }
        });

        btnSelectPix.setOnClickListener((View view) -> {
            runtimePermissionHelper.requestStorageCameraPermission(1);
        });
        imageViewNext1.setOnClickListener(view -> {
            if (adforest_page1Validation()) {
                page1.setVisibility(View.GONE);
                page3.setVisibility(View.GONE);
                page2.setVisibility(View.VISIBLE);
                frameLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_out));
            } else {
                Toast.makeText(context, require_message, Toast.LENGTH_SHORT).show();

            }
//                else {
//                    editTextTitle.setError("");
//                    mScrollView.scrollTo(0, 0);
//                }
        });
        imageViewNext2.setOnClickListener(view -> {
            if (adforest_page2Validation()) {
                page1.setVisibility(View.GONE);
                page2.setVisibility(View.GONE);
                page3.setVisibility(View.VISIBLE);
                frameLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.right_enter));
            } else {
                Toast.makeText(context, require_message, Toast.LENGTH_SHORT).show();

            }
        });
        imageViewBack1.setOnClickListener(view -> {
            page3.setVisibility(View.GONE);
            page2.setVisibility(View.GONE);
            page1.setVisibility(View.VISIBLE);
            frameLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_enter));
        });
        imageViewBack2.setOnClickListener(view -> {
            page1.setVisibility(View.GONE);
            page3.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);
            frameLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_enter));
        });

        textViewTitle = (TextView) findViewById(R.id.textUserProfileTV);
        textViewUserName = (TextView) findViewById(R.id.yourNameTV);
        textViewUserPhone = (TextView) findViewById(R.id.phoneNumberTV);
        textViewLat = (TextView) findViewById(R.id.latTV);
        textViewLong = (TextView) findViewById(R.id.longTV);
        textViewLocation = (TextView) findViewById(R.id.locationTV);
        textViewadCountry = (TextView) findViewById(R.id.adCountryTV);
        spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);
        bumAdLayout = (RelativeLayout) findViewById(R.id.bumAdLayout);
        featureAdLayout = (RelativeLayout) findViewById(R.id.featureAdLayout);
        bumpAdTV = (TextView) findViewById(R.id.bumpAdTV);
        terms_ConditionLayout = (RelativeLayout) findViewById(R.id.terms_conditionlayout);
        terms_conditionChkBox = (CheckBox) findViewById(R.id.terms_conditionChkBox);
        terms_conditionsTitleTV = (TextView) findViewById(R.id.terms_conditionsTitle);
        chkBxBumpAd = (CheckBox) findViewById(R.id.chkBxBumpAd);
        featureAdTV = (TextView) findViewById(R.id.featureAdTV);
        featureAdChkBox = (CheckBox) findViewById(R.id.featureAdChkBox);
        featureAdByPackages = (TextView) findViewById(R.id.featureAdByPackages);
        bumAdLayout.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        featureAdLayout.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        terms_ConditionLayout.setBackgroundColor(Color.parseColor(SettingsMain.getMainColor()));
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);

        spinnerLocation.setFocusable(true);
        editTextUserName.setFocusable(true);
        mAutocompleteTextView.setFocusable(true);

        btnImageChooser.setVisibility(View.VISIBLE);
        adforest_initiImagesAdapter();
        // get view from server
        adforest_getViews();
        if (settingsMain.getBannerShow()) {
            adforest_bannersAds();
        }
        updateViews(settingsMain.getAlertDialogMessage("gmap_lang"));
        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        mAutocompleteTextView.setOnItemClickListener(this);

        mAutocompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                manageAutoComplete(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        locationButton = findViewById(R.id.imageButton2);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapBoxMap != null) {
                    enableLocationComponent(mapBoxMap.getStyle());
                }
            }
        });
    }

    private void manageAutoComplete(String query) {
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest.Builder request = FindAutocompletePredictionsRequest.builder();

        if (SplashScreen.gmap_has_countries) {
            request.setCountry(SplashScreen.gmap_countries);
        }
        if (settingsMain.getAlertDialogMessage("location_type").equals("regions")) {
            request.setTypeFilter(TypeFilter.ADDRESS);
        } else {
            request
                    .setTypeFilter(TypeFilter.REGIONS);
        }
        request.setSessionToken(token)
                .setQuery(query);


        placesClient.findAutocompletePredictions(request.build()).addOnSuccessListener((response) -> {

            ids.clear();
            places.clear();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                places.add(prediction.getFullText(null).toString());
                ids.add(prediction.getPlaceId());
                Log.i("Places", prediction.getPlaceId());
                Log.i("Places", prediction.getFullText(null).toString());
            }
            String[] data = places.toArray(new String[places.size()]); // terms is a List<String>

            ArrayAdapter<?> adapter = new ArrayAdapter<Object>(EditAdPost.this, android.R.layout.simple_dropdown_item_1line, data);
            mAutocompleteTextView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("Places", "Place not found: " + apiException.getStatusCode());
            }
        });


    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        String placeId = ids.get(position);
        List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);

// Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                .build();
// Add a listener to handle the response.
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            com.google.android.libraries.places.api.model.Place place = response.getPlace();
            Log.i("Places", "Place found: " + place.getLatLng().latitude + " " + place.getLatLng().longitude);
            if (mMap != null) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                editTextuserLong.setText(String.format("%s", place.getLatLng().longitude));
                editTextUserLat.setText(String.format("%s", place.getLatLng().latitude));
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
// Handle error with given status code.
                Log.e("Places", "Place not found: " + exception.getMessage());
            }
        });


    }

    public void adforest_initiImagesAdapter() {
        myImages = new ArrayList<>();
        itemEditImageAdapter = new ItemEditImageAdapter(context, myImages);
        recyclerView.setAdapter(itemEditImageAdapter);
        itemEditImageAdapter.setHandleDragEnabled(true);
        itemEditImageAdapter.setLongPressDragEnabled(true);
        itemEditImageAdapter.setSwipeEnabled(true);

        itemEditImageAdapter.setOnItemClickListener(new MyAdsOnclicklinstener() {
            @Override
            public void onItemClick(myAdsModel item) {

            }

            @Override
            public void delViewOnClick(View v, int position) {
                delImage(v.getTag().toString());
            }

            @Override
            public void editViewOnClick(View v, int position) {

            }
        });

    }

    public void adforest_bannersAds() {
        if (settingsMain.getAdsShow() && !settingsMain.getBannerAdsId().equals("")) {
            if (settingsMain.getAdsPostion().equals("top")) {
                LinearLayout frameLayout = (LinearLayout) findViewById(R.id.adVi);
                Admob.adforest_Displaybanners(EditAdPost.this, frameLayout);
            } else {
                LinearLayout frameLayout = (LinearLayout) findViewById(R.id.adViBottom);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.bottomMargin = 120;
                mScrollView.setLayoutParams(layoutParams);
                Admob.adforest_Displaybanners(this, frameLayout);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void updateViews(String languageCode) {
        LocaleHelper.setLocale(this, languageCode);
    }

    public boolean adforest_page1Validation() {

        boolean b = true;

        for (int i = 0; i < page1RequiredFields.size(); i++) {

            if (page1RequiredFields.get(i) instanceof EditText) {
                EditText editText = (EditText) page1RequiredFields.get(i);
                if (editText.getText().toString().equals("")) {
                    if (edittextShowHide) {
                        if (editTextPrice != null) {
                            if (editTextPrice.getText().toString().trim().equals("")) {
                                editTextPrice.setError("!");
                                editText.requestFocus();
                                b = false;
                                break;
                            }
                        }
                    }
                    if (editTextPrice != null && !edittextShowHide) {
                        b = true;
                    } else {
                        editText.setError("!");
                        editText.requestFocus();
                        b = false;
                        break;
                    }
                }
            }

        }
        return b;
    }

    public boolean adforest_page2Validation() {
        boolean b = true;

        for (int i = 0; i < requiredFieldsForCustom.size(); i++) {
            if (requiredFieldsForCustom.get(i) instanceof EditText) {
                EditText editText = (EditText) requiredFieldsForCustom.get(i);
                if (editText.getText().toString().trim().equals("")) {
                    editText.setError("!");
                    editText.requestFocus();
                    b = false;
                    break;
                }
                if (editText.getTag() != null) {
                    if (editText.getTag().equals("textfield_url")) {
                        if (!URLUtil.isValidUrl(editText.getText().toString())) {
                            editText.setError("!");
                            editText.requestFocus();
                            b = false;
                            break;
                        }

                    }
                }
            }

            if (requiredFieldsForCustom.get(i) instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) requiredFieldsForCustom.get(i);
                boolean b1 = false;
                Log.d("info LinearLayout", linearLayout + "");

                if (linearLayout instanceof RadioGroup) {
                    Log.d("info RadioGroup", linearLayout + "");

                    boolean check = false;
                    RadioGroup radioGroup = (RadioGroup) requiredFieldsForCustom.get(i);
                    for (int q = 0; q < radioGroup.getChildCount(); q++) {
                        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(q);
                        if (radioButton.isChecked()) {
                            check = true;
                            b = true;
                            break;
                        }
                    }
                    if (!check) {
                        RadioButton radiobutton = (RadioButton) radioGroup.getChildAt(0);
                        radiobutton.setError("!");
                        radiobutton.requestFocus();
                        b = false;

                    } else {
                        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(0);
                        radioButton.setError(null);
                        b = true;

                    }
                } else {
                    Log.d("info CheckBox", linearLayout + "");

                    if (requiredFieldsForCustom.get(i) instanceof LinearLayout) {
                        LinearLayout layout = (LinearLayout) requiredFieldsForCustom.get(i);
                        for (int j = 0; j < layout.getChildCount(); j++) {
                            CheckBox checkBox = (CheckBox) layout.getChildAt(j);
                            if (checkBox.isChecked()) {
                                b1 = true;
                                break;
                            }
                        }
                        if (!b1) {
                            CheckBox checkBox = (CheckBox) layout.getChildAt(0);
                            checkBox.setError("!");
                            checkBox.requestFocus();
                            b = false;
                        } else {
                            CheckBox checkBox = (CheckBox) layout.getChildAt(0);
                            checkBox.setError(null);
                            b = true;
                        }
                    }

                }
            }
        }
        if (edittextShowHide) {
            if (editTextPrice != null) {
                if (editTextPrice.getText().toString().equals("")) {
                    editTextPrice.setError("!");
                    editTextPrice.requestFocus();
                    b = false;
                }
            }
        }

        for (int i = 0; i < page2RequiredFields.size(); i++) {
            if (page2RequiredFields.get(i) instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) page2RequiredFields.get(i);
                TextView textView = (TextView) linearLayout.getChildAt(0);
                RichEditor editText = (RichEditor) linearLayout.getChildAt(1);

                if (editText.getHtml() == null || editText.getHtml().equals("")) {
                    textView.setError("!");
                    textView.requestFocus();
                    editText.requestFocus();
                    b = false;
                } else {
                    textView.setError(null);
                }
            }
            if (page2RequiredFields.get(i) instanceof EditText) {
                EditText editText = (EditText) page2RequiredFields.get(i);
                if (editText.getText().toString().equals("")) {
                    if (edittextShowHide) {
                        if (editTextPrice != null) {
                            if (editTextPrice.getText().toString().equals("")) {
                                editTextPrice.setError("!");
                                editText.requestFocus();
                                b = false;
                                break;
                            }
                        }
                    }
                    if (editTextPrice != null && !edittextShowHide) {
                        b = true;
                    } else {
                        editText.setError("!");
                        editText.requestFocus();
                        b = false;
                        break;
                    }
                }
            }

        }
        return b;
    }

    //APi for submiting post to server
    private void adforest_submitQuery(JsonObject params) {
        if (jsonObjectforCustom != null && adforest_getDataFromDynamicViewsForCustom() != null) {
            params.add("custom_fields", adforest_getDataFromDynamicViewsForCustom());
        }

        JSONObject jsonObj;
        try {


            if (itemEditImageAdapter != null) {
                if (itemEditImageAdapter.getItemCount() > 0)
                    params.addProperty("images_arr", itemEditImageAdapter.getAllTags());
            } else
                params.addProperty("images_arr", "");

            params.addProperty("ad_id", addId);
            params.addProperty("is_update", addId);

            jsonObj = jsonObject.getJSONObject("data").getJSONObject("profile");

            params.addProperty(jsonObj.getJSONObject("name").getString("field_type_name"), editTextUserName.getText().toString());
            try {
                String phoneNumber = editTextUserPhone.getText().toString();
                if (jsonObj.getBoolean("is_phone_verification_on")) {
                    if (phoneNumber.contains("+"))
                        params.addProperty(jsonObj.getJSONObject("phone").getString("field_type_name"), phoneNumber);
                    else
                        params.addProperty(jsonObj.getJSONObject("phone").getString("field_type_name"), "+" + phoneNumber);
                } else
                    params.addProperty(jsonObj.getJSONObject("phone").getString("field_type_name"), phoneNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mAutocompleteTextView.getVisibility() == View.VISIBLE)
                params.addProperty(jsonObj.getJSONObject("location").getString("field_type_name"), mAutocompleteTextView.getText().toString());
            else
                params.addProperty(jsonObj.getJSONObject("location").getString("field_type_name"), placesContainer.getText().toString());
            params.addProperty(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("field_type_name"), editTextUserLat.getText().toString());
            params.addProperty(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("field_type_name"), editTextuserLong.getText().toString());

            if (jsonObj.getBoolean("ad_country_show")) {
                subcatDiloglist subDiloglist = (subcatDiloglist) spinnerLocation.getSelectedView().getTag();
                params.addProperty("ad_country", subDiloglist.getId());
            }
            if (jsonObj.getBoolean("bump_ad_is_show")) {
                if (chkBxBumpAd.isChecked()) {
                    params.addProperty(jsonObj.getJSONObject("bump_ad").getString("field_type_name"), "true");
                } else
                    params.addProperty(jsonObj.getJSONObject("bump_ad").getString("field_type_name"), "false");
            }
            if (jsonObj.getBoolean("featured_ad_is_show")) {
                if (featureAdChkBox.isChecked()) {
                    params.addProperty(jsonObj.getJSONObject("featured_ad").getString("field_type_name"), "true");
                } else
                    params.addProperty(jsonObj.getJSONObject("featured_ad").getString("field_type_name"), "false");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (SettingsMain.isConnectingToInternet(context)) {
            loadingLinearLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            frameLayout.setVisibility(View.GONE);

            Log.d("info SendEditPost Data", "" + params.toString());
            Call<ResponseBody> myCall = restService.postAdNewPost(params, UrlController.AddHeaders(this));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info EditPost Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info EditPost object", response.toString());

                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(EditAdPost.this, Ad_detail_activity.class);
                                intent.putExtra("adId", response.getJSONObject("data").getString("ad_id"));
                                startActivity(intent);

                                EditAdPost.this.finish();
                            } else {
                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        e.printStackTrace();
                    } catch (IOException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        e.printStackTrace();
                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    loadingLinearLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info EditPost", "NullPointert Exception" + t.getLocalizedMessage());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        Log.d("info EditPost error", String.valueOf(t));
                        Log.d("info EditPost error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            loadingLinearLayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    // getting images selected from gallery for post and sending them to server
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
// super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    imagePaths = new ArrayList<>();
                    imagePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    if (SettingsMain.isConnectingToInternet(context)) {
                        if (imagePaths.size() > 0) {
                            btnSelectPix.setEnabled(false);
                            asyncImageTask = new AsyncImageTask();
                            asyncImageTask.execute(imagePaths);
                        }
                    }
                } else {
                    btnSelectPix.setEnabled(true);
                    Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (requestCode == 35) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                CarmenFeature feature = PlaceAutocomplete.getPlace(data);
                Point point = feature.center();
                lat_by_mapbox = point.latitude();
                lon_by_mapbox = point.longitude();
                address_by_mapbox = feature.placeName();
                placesContainer.setText(address_by_mapbox);
                if (placesContainer != null) {
                    editTextUserLat.setText(String.valueOf(lat_by_mapbox));
                    editTextuserLong.setText(String.valueOf(lon_by_mapbox));
                }
                if (mapBoxMap != null) {
                    if (marker != null) {
                        mapBoxMap.removeMarker(marker);
                    }
                    marker = mapBoxMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                            .position(new com.mapbox.mapboxsdk.geometry.LatLng(lat_by_mapbox, lon_by_mapbox)));

                    com.mapbox.mapboxsdk.camera.CameraPosition position = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder()
                            .target(new com.mapbox.mapboxsdk.geometry.LatLng(marker.getPosition().getLatitude(), marker.getPosition().getLongitude())) // Sets the new camera position
                            .zoom(15) // Sets the zoom
//                        .bearing(180) // Rotate the camera
                            // Set the camera tilt
                            .build();
                    mapBoxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory
                            .newCameraPosition(position), 1000);

                }

            } else {
                btnSelectPix.setEnabled(true);
                Toast.makeText(context, settingsMain.getAlertDialogMessage("Failed to get location data"), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 1234) {
            if (resultCode == Activity.RESULT_OK && requestCode == requestCode && data != null) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                File filesDir = getApplicationContext().getFilesDir();
                File imageFile = new File(filesDir, "files" + ".jpg");
                OutputStream os;
                try {
                    os = new FileOutputStream(imageFile);
                    yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }
                String pathsList[] = {
                        String.valueOf(yourSelectedImage)
//                        imageFile.getAbsolutePath()
                }; // return list of selected images paths.
                List<MultipartBody.Part> parts = null;

                imagePaths = new ArrayList<>();
                imagePaths.clear();
                for (int i = 0; i < pathsList.length; i++) {
                    imagePaths.add(pathsList[i]);
                }
                imageRequestCount = 1;
                if (SettingsMain.isConnectingToInternet(context)) {
                    if (imagePaths.size() > 0) {
                        parts = new ArrayList<>();
                        btnSelectPix.setEnabled(false);
                        RequestBody requestFile =
                                RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
// MultipartBody.Part is used to send also the actual file name
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);
                        parts.add(body);
                        adforest_uploadImages(parts);


                    }
                }
            } else {
                btnSelectPix.setEnabled(true);
                Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 69) {
            if (resultCode == Activity.RESULT_OK && requestCode == requestCode && data != null) {
                String pathsList[] = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT); // return list of selected images paths.
                imagePaths = new ArrayList<>();
                for (int i = 0; i < pathsList.length; i++) {
                    imagePaths.add(pathsList[i]);
                }
                imageRequestCount = 1;
                if (SettingsMain.isConnectingToInternet(context)) {
                    if (imagePaths.size() > 0) {
                        btnSelectPix.setEnabled(false);
                        asyncImageTask = new AsyncImageTask();
                        asyncImageTask.execute(imagePaths);

                    }
                }
            }
        } else {
            btnSelectPix.setEnabled(true);
            Toast.makeText(context, settingsMain.getAlertDialogMessage("Failed to get location data"), Toast.LENGTH_SHORT).show();
        }


    }

    private void adforest_uploadImages(List<MultipartBody.Part> parts) {
        Log.d("info image parts", parts.toString());
        String ad_id = Integer.toString(addId);
        RequestBody adID =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, ad_id);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(isRequired));

        Log.d("info SendImage", addId + "");

        final Call<ResponseBody> req = restService.postUploadImage(adID, requestBody, parts, UrlController.UploadImageAddHeaders(context));

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    Log.v("info Upload", response.toString());
                    JSONObject responseobj = null;
                    try {
                        responseobj = new JSONObject(response.body().string());
                        Log.d("info UploadImage object", "" + responseobj.getJSONObject("data").toString());
                        if (responseobj.getBoolean("success")) {

                            adforest_updateImages(responseobj.getJSONObject("data"));

                            int selectedImageSize = imagePaths.size();
                            int totalSize = currentSize + selectedImageSize;
                            Log.d("info image2", "muImage" + totalSize + "imagePaths" + totalUploadedImages);
                            if (totalSize == totalUploadedImages) {
                                adforest_UploadSuccessImage();
                                imagePaths.clear();
                                paths.clear();
                                if (allFile.size() > 0) {
                                    for (File file : allFile) {
                                        if (file.exists()) {
                                            if (file.delete()) {
                                                Log.d("file Deleted :", file.getPath());
                                            } else {
                                                Log.d("file not Deleted :", file.getPath());
                                            }
                                        }
                                    }
                                }
                            }

                        } else {
                            adforest_UploadFailedImage();
                            Toast.makeText(context, responseobj.get("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        adforest_UploadFailedImage();
                        e.printStackTrace();
                    } catch (IOException e) {
                        adforest_UploadFailedImage();
                        e.printStackTrace();
                    }

                    btnSelectPix.setEnabled(true);

                } else {
                    adforest_UploadFailedImage();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("info Upload Image Err:", t.toString());

                if (t instanceof TimeoutException) {
                    adforest_UploadFailedImage();

//                    adforest_requestForImages();
                }
                if (t instanceof SocketTimeoutException) {
                    adforest_UploadFailedImage();
//                    adforest_requestForImages();
//
                } else {
                    adforest_UploadFailedImage();
//                    adforest_requestForImages();
                }
            }
        });

    }

    private void adforest_UploadFailedImage() {
        progress_bar.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        Gallary.setVisibility(View.VISIBLE);
        Gallary.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
        Gallary.setText("" + 0);
        Gallary.setTextColor(Color.parseColor("#a0a0a0"));
        tv_done.setVisibility(View.VISIBLE);
        tv_done.setTextColor(Color.parseColor("#ff0000"));
        tv_done.setText(progressModel.getFailMessage());
        btnSelectPix.setEnabled(true);
        Toast.makeText(context, progressModel.getFailMessage(), Toast.LENGTH_SHORT).show();

    }

    private void adforest_UploadSuccessImage() {
        progress_bar.setVisibility(View.GONE);
        Gallary.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        Gallary.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_green_24dp, 0, 0, 0);
        Gallary.setText(totalFiles + "");
        tv_done.setText(progressModel.getSuccessMessage());
        Toast.makeText(context, progressModel.getSuccessMessage(), Toast.LENGTH_SHORT).show();
        btnSelectPix.setEnabled(true);
        tv_done.setTextColor(Color.parseColor("#20a406"));
    }

    private MultipartBody.Part adforestst_prepareFilePart(String fileName, Uri fileUri) {

        File finalFile = new File(fileUri.toString());
        allFile.add(finalFile);
        // create RequestBody instance from file

        ProgressRequestBody requestFile = new ProgressRequestBody(finalFile, this);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(fileName, finalFile.getName(), requestFile);
    }

    private File adforest_rotateImage(String path) {
        File file = null;
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        file = new File(getRealPathFromURI(getImageUri(rotatedBitmap)));
        allFile.add(file);
        return file;
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                path = saveBitmap(inImage, Bitmap.CompressFormat.JPEG, "image/jpeg", "Title");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        }
//        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String saveBitmap(@NonNull final Bitmap bitmap,
                              @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                              @NonNull final String displayName) throws IOException {
        final String relativeLocation = Environment.DIRECTORY_PICTURES;

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);

        final ContentResolver resolver = context.getContentResolver();

        OutputStream stream = null;
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            uri = resolver.insert(contentUri, contentValues);


            if (uri == null) {
                throw new IOException("Failed to create new MediaStore record.");
            }

            stream = resolver.openOutputStream(uri);


            resolver.update(uri, contentValues, null, null);
            if (stream == null) {
                throw new IOException("Failed to get output stream.");
            }

            if (bitmap.compress(format, 95, stream) == false) {
                throw new IOException("Failed to save bitmap.");
            }
        } catch (IOException e) {
            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }

            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return uri.toString();
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    //setting spiner error for validation
    private void setSpinnerError(Spinner spinner) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("!");
            selectedTextView.setTextColor(Color.RED);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        request = new LocationEngineRequest.Builder(5000L)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(10000).build();


        locationEngine.requestLocationUpdates(request, this, getMainLooper());
        locationEngine.getLastLocation(this);
    }

    @Override
    public void onSuccess(LocationEngineResult result) {
        Location location = result.getLastLocation();

        if (location == null) {
            return;
        }
        List<Address> addresses = null;
        if (location != null) {
            Geocoder geocoder = new Geocoder(EditAdPost.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (addresses != null) {
            StringBuilder stringBuilder = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                int maxIndex = address.getMaxAddressLineIndex();
                for (int x = 0; x <= maxIndex; x++) {
                    stringBuilder.append(address.getAddressLine(x));
                    //result.append(",");
                }
            }
            placesContainer.setText(stringBuilder.toString());
        } else {
            Toast.makeText(EditAdPost.this, "Failed To Get Address. Please Try Again", Toast.LENGTH_SHORT).show();
        }


        // Create a Toast which displays the new location's coordinates
//        Toast.makeText(this, String.format(getString(R.string.new_location)+
//                        String.valueOf(result.getLastLocation().getLatitude())+
//                        String.valueOf(result.getLastLocation().getLongitude())),
//                Toast.LENGTH_SHORT).show();

        com.mapbox.mapboxsdk.camera.CameraPosition position = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder()
                .target(new com.mapbox.mapboxsdk.geometry.LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude())) // Sets the new camera position
                .zoom(17) // Sets the zoom
//                        .bearing(180) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build();

        mapBoxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory
                .newCameraPosition(position), 4000);


        // Pass the new location to the Maps SDK's LocationComponent
        if (mapBoxMap != null && result.getLastLocation() != null) {
            mapBoxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null)
            locationEngine.removeLocationUpdates(this);

    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        Log.d("LocationChangeActivity", exception.getLocalizedMessage());

        Toast.makeText(this, exception.getLocalizedMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Explanation Needed",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapBoxMap.getStyle() != null) {
                enableLocationComponent(mapBoxMap.getStyle());
            }
        } else {
            Toast.makeText(this, "Location Permission Not Granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onCameraTrackingDismissed() {
        isInTrackingMode = false;
    }

    @Override
    public void onCameraTrackingChanged(int currentMode) {

    }

    @Override
    public void onLocationComponentClick() {
        if (locationComponent.getLastKnownLocation() != null) {
            Toast.makeText(this, String.format("sdfsdfj",
                    locationComponent.getLastKnownLocation().getLatitude(),
                    locationComponent.getLastKnownLocation().getLongitude()), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Create and customize the LocationComponent's options
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .build();

            // Get an instance of the component
            locationComponent = mapBoxMap.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            // Add the location icon click listener
            locationComponent.addOnLocationClickListener(this);

            // Add the camera tracking listener. Fires if the map camera is manually moved.
            locationComponent.addOnCameraTrackingChangedListener(this);

            initLocationEngine();
            findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ex) {
                    }
                    if (gps_enabled) {
                        if (!isInTrackingMode) {
                            isInTrackingMode = true;
                            locationComponent.setCameraMode(CameraMode.TRACKING);
                            locationComponent.zoomWhileTracking(16f);
                            Location location = locationComponent.getLastKnownLocation();
                            List<Address> addresses = null;
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(EditAdPost.this, Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                            if (addresses != null) {
                                StringBuilder result = new StringBuilder();
                                if (addresses.size() > 0) {
                                    Address address = addresses.get(0);
                                    int maxIndex = address.getMaxAddressLineIndex();
                                    for (int x = 0; x <= maxIndex; x++) {
                                        result.append(address.getAddressLine(x));
                                        //result.append(",");
                                    }
                                }
                                placesContainer.setText(result.toString());
                            } else {
                                Toast.makeText(EditAdPost.this, "Failed To Get Address. Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        displayLocationSettingsRequest(EditAdPost.this);
                    }
                }
            });

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(EditAdPost.this, 990);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    private static class LatLngEvaluator implements TypeEvaluator<com.mapbox.mapboxsdk.geometry.LatLng> {
        // Method is used to interpolate the marker animation.

        private com.mapbox.mapboxsdk.geometry.LatLng latLng = new com.mapbox.mapboxsdk.geometry.LatLng();

        @Override
        public com.mapbox.mapboxsdk.geometry.LatLng evaluate(float fraction, com.mapbox.mapboxsdk.geometry.LatLng startValue, com.mapbox.mapboxsdk.geometry.LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapBoxMap = mapboxMap;
        marker = mapboxMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                .position(new com.mapbox.mapboxsdk.geometry.LatLng(point.latitude, point.longitude)));
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                com.mapbox.mapboxsdk.camera.CameraPosition position = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder()
                        .target(new com.mapbox.mapboxsdk.geometry.LatLng(marker.getPosition().getLatitude(), marker.getPosition().getLongitude())) // Sets the new camera position
                        .zoom(15) // Sets the zoom
//                        .bearing(180) // Rotate the camera
                        // Set the camera tilt
                        .build();
                mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory
                        .newCameraPosition(position), 1000);
                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull com.mapbox.mapboxsdk.geometry.LatLng point) {

                        ValueAnimator markerAnimator = ObjectAnimator.ofObject(marker, "position",
                                new LatLngEvaluator(), marker.getPosition(), point);
                        Geocoder geocoder = new Geocoder(EditAdPost.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addresses != null) {
                            StringBuilder result = new StringBuilder();
                            if (addresses.size() > 0) {
                                Address address = addresses.get(0);
                                int maxIndex = address.getMaxAddressLineIndex();
                                for (int x = 0; x <= maxIndex; x++) {
                                    result.append(address.getAddressLine(x));
                                    //result.append(",");
                                }
                            }
                            placesContainer.setText(result.toString());
                        } else {
                            Toast.makeText(EditAdPost.this, "Failed To Get Address", Toast.LENGTH_SHORT).show();
                        }
                        markerAnimator.setDuration(100);
                        markerAnimator.start();
                        return false;
                    }
                });
                if (point != null) {
                    Geocoder geocoder = new Geocoder(EditAdPost.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses != null) {
                        StringBuilder result = new StringBuilder();
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            int maxIndex = address.getMaxAddressLineIndex();
                            for (int x = 0; x <= maxIndex; x++) {
                                result.append(address.getAddressLine(x));
                                //result.append(",");
                            }
                        }
                        placesContainer.setText(result.toString());
                    }
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap Map) {
        mMap = Map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        LatLng sydney = new LatLng(40.7127837, -74.0059413);
        // create marker
        MarkerOptions marker = new MarkerOptions()
                .position(sydney)
                .title("New York, NY, United States")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(40.7127837, -74.0059413)).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            editTextuserLong.setText(String.format("%s", latLng.longitude));
            editTextUserLat.setText(String.format("%s", latLng.latitude));
        });
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        GPSTracker gpsTracker = new GPSTracker(EditAdPost.this);
        if (!gpsTracker.canGetLocation())
            gpsTracker.showSettingsAlert();
        else {
            Geocoder geocoder;
            List<Address> addresses1 = null;
            try {
                addresses1 = new Geocoder(this, Locale.getDefault()).getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder result = new StringBuilder();
            if (addresses1.size() > 0) {
                Address address = addresses1.get(0);
                int maxIndex = address.getMaxAddressLineIndex();
                for (int x = 0; x <= maxIndex; x++) {
                    result.append(address.getAddressLine(x));
                    //result.append(",");
                }
            }
            try {
                mAutocompleteTextView.setText(result.toString());
                editTextuserLong.setText(gpsTracker.getLongitude() + "");
                editTextUserLat.setText(gpsTracker.getLatitude() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //calling APi for geting views from server
    private void adforest_getViews() {

        if (SettingsMain.isConnectingToInternet(context)) {
            loadingLinearLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            JsonObject params = new JsonObject();
            params.addProperty("is_update", updateId);

            Log.d("info sendEdit Ad", "" + params.toString());
            Call<ResponseBody> myCall = restService.postGetAdNewPostViews(params, UrlController.AddHeaders(this));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info EditAd Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());

                            if (response.getBoolean("success")) {
                                adforest_setViews(response);
                                Log.d("info EditAd Data", response.getJSONObject("data").toString());
                                require_message = response.getJSONObject("extra").getString("require_message");
                                JSONArray customOptnList = jsonObject.getJSONObject("data").getJSONArray("fields");

                            } else {
                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    } catch (IOException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    loadingLinearLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info EditAd ", "NullPointert Exception" + t.getLocalizedMessage());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        Log.d("info EditAd err", String.valueOf(t));
                        Log.d("info EditAd err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            loadingLinearLayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public class ViewDialog {

        public void showDialog(Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialogforcat);
            ImageView imageView = dialog.findViewById(R.id.a);
            Glide.with(EditAdPost.this).load(R.drawable.angry).into(imageView);
            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    //generating fields of layout
    @SuppressLint("ResourceType")
    void adforest_setViews(JSONObject jsonObjec) {

        try {
            jsonObject = jsonObjec;
            Log.d("info data ===== ", jsonObject.toString());
            JSONArray customOptnList = jsonObject.getJSONObject("data").getJSONArray("fields");
            textViewInfoforDrag.setText(jsonObject.getJSONObject("extra").getString("sort_image_msg"));
            addId = jsonObject.getJSONObject("data").getInt("ad_id");
            setTitle(jsonObjec.getJSONObject("data").getString("title"));

            btnSelectPix.setText(jsonObject.getJSONObject("extra").getString("image_text"));
            textViewTitle.setText(jsonObject.getJSONObject("extra").getString("user_info"));

            textViewInfo.setText(jsonObjec.getJSONObject("data").getString("update_notice"));

            adforest_updateImages(jsonObject.getJSONObject("data"));

            requiredFields.clear();

            Log.d("info Fields data ===== ", "" + customOptnList.length());

            for (int noOfCustomOpt = 0; noOfCustomOpt < customOptnList.length(); noOfCustomOpt++) {

                CardView cardView = new CardView(EditAdPost.this);

                cardView.setCardElevation(2);
                cardView.setUseCompatPadding(true);
                cardView.setRadius(0);
                cardView.setContentPadding(10, 10, 10, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 10;
                params.bottomMargin = 10;
                cardView.setLayoutParams(params);

                final JSONObject eachData = customOptnList.getJSONObject(noOfCustomOpt);
                TextView customOptionsName = new TextView(context);
                customOptionsName.setTextSize(12);
                customOptionsName.setAllCaps(true);
                customOptionsName.setTextColor(Color.BLACK);
                customOptionsName.setPadding(10, 15, 10, 15);

                customOptionsName.setText(eachData.getString("title"));

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setPadding(5, 5, 5, 5);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(customOptionsName);

                if (eachData.getString("field_type").equals("select")) {
                    meraCardView = cardView;
                    cardViewtBidingTimer = cardView;
                    if (eachData.getString("field_type_name").equals("ad_cats1")) {
                        catCard = cardView;
                    }
                    final JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                    final ArrayList<subcatDiloglist> SpinnerOptions;
                    SpinnerOptions = new ArrayList<>();
                    for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                        subcatDiloglist subDiloglist = new subcatDiloglist();
                        subDiloglist.setId(dropDownJSONOpt.getJSONObject(j).getString("id"));
                        subDiloglist.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                        subDiloglist.setHasSub(dropDownJSONOpt.getJSONObject(j).getBoolean("has_sub"));
                        subDiloglist.setHasCustom(dropDownJSONOpt.getJSONObject(j).getBoolean("has_template"));
                        try {

                            if (eachData.getString("field_type_name").equals("ad_cats1")) {
                                if (subDiloglist.getId().length() != 0) {
                                    subDiloglist.setBidding(dropDownJSONOpt.getJSONObject(j).getBoolean("is_bidding"));
                                    subDiloglist.setPaid(dropDownJSONOpt.getJSONObject(j).getBoolean("can_post"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //String optionString = dropDownJSONOpt.getJSONObject(j).getString("name");
                        if (eachData.getString("field_type_name").equals(
                                jsonObject.getJSONObject("data").getJSONArray("hide_price").get(1))) {
                            subDiloglist.setShow(dropDownJSONOpt.getJSONObject(j).getBoolean("is_show"));
                        }
                        SpinnerOptions.add(subDiloglist);
                    }

                    final SpinnerAndListAdapter spinnerAndListAdapter;
                    spinnerAndListAdapter = new SpinnerAndListAdapter(context, SpinnerOptions, true);
                    final Spinner spinner = new Spinner(context);
                    if (eachData.getString("field_type_name").equals("ad_cats1")) {
                        spinner.setId(2222);
                    }
                    if (eachData.getString("field_type_name").equals(
                            jsonObject.getJSONObject("data").getJSONArray("hide_currency").get(1))) {
                        cardViewPriceType = cardView;
                    }
                    if (eachData.getBoolean("is_required")) {
                        requiredFields.add(spinner);
                    }

                    allViewInstance.add(spinner);
                    spinner.setAdapter(spinnerAndListAdapter);
                    spinner.setSelection(0, false);

                    spinner.setOnTouchListener((v, event) -> {
                        System.out.println("Real touch felt.");
                        spinnerTouched = true;
                        return false;
                    });
                    try {
                        if (eachData.getBoolean("has_cat_template") && checkSpineerCliked) {
                            if (!jsonObject.getJSONObject("data").getString("ad_cat_id").equals("")) {
                                linearLayoutCustom.removeAllViews();
                                allViewInstanceforCustom.clear();
//                                catID = jsonObject.getJSONObject("data").getString("ad_cat_id")
                                catID = jsonObject.getJSONObject("data").getJSONArray("fields").getJSONObject(1)
                                        .getJSONArray("values").getJSONObject(0).getString("id");
                                adforest_showCustom(eachData);
                                ison = true;
                                Log.d("true===== ", "add All=======" + catID);
                            }
                            if (ison) {
                                linearLayoutCustom.removeAllViews();
                                allViewInstanceforCustom.clear();
                                ison = false;
                                Log.d("true====", "remove All");

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) selectedItemView.getTag();

                            Log.d("info position", "" + position);
                            if (spinnerTouched) {
                                checkSpineerCliked = false;
                                //for checking if category has bidding or not in package
//                                if (position != 0) {
                                if (parentView.getId() == 2222)
                                    if (subcatDiloglistitem.isBidding() && !String.valueOf(subcatDiloglistitem.isBidding()).equals("")) {
                                        meraCardView.setVisibility(View.VISIBLE);

                                    } else {
                                        if (!String.valueOf(subcatDiloglistitem.isBidding()).equals("")) {
                                            meraCardView.setVisibility(View.GONE);
                                            catCard.setVisibility(View.VISIBLE);
                                            cardViewtBidingTimer.setVisibility(View.GONE);

                                        }
                                    }
                                //for checking if category  is paid or not in package
                                if (parentView.getId() == 2222)
                                    if (!subcatDiloglistitem.isPaid()) {
                                        catCard.setVisibility(View.VISIBLE);
                                        spinner.setSelection(0);
                                        AlertDialog.Builder alert1 = new AlertDialog.Builder(EditAdPost.this);
                                        alert1.setTitle(settingsMain.getAlertDialogTitle("info"));
                                        alert1.setCancelable(false);
                                        alert1.setMessage("Please Buy the Package which has the category present in it");
                                        alert1.setPositiveButton(settingsMain.getAlertOkText(), (dialog, which) -> {

                                            dialog.dismiss();
                                        });
                                        alert1.show();
                                        return;
                                    }
                                if (subcatDiloglistitem.isHasSub()) {
                                    if (SettingsMain.isConnectingToInternet(context)) {

                                        loadingLinearLayout.setVisibility(View.VISIBLE);
                                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                                        shimmerFrameLayout.startShimmer();
                                        frameLayout.setVisibility(View.GONE);
                                        //for serlecting the Categoreis if Categoreis have SubCategoreis

                                        JsonObject params = new JsonObject();
                                        params.addProperty("subcat", subcatDiloglistitem.getId());
                                        Log.d("info SendSubCat", params.toString());

                                        Call<ResponseBody> myCall = restService.postGetSubCategories(params, UrlController.AddHeaders(context));
                                        myCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                                try {
                                                    if (responseObj.isSuccessful()) {
                                                        Log.d("info SubCat Resp", "" + responseObj.toString());

                                                        JSONObject response = new JSONObject(responseObj.body().string());
                                                        if (response.getBoolean("success")) {
                                                            Log.d("info SubCat object", "" + response.getJSONObject("data"));
                                                            spinnerTouched = false;

                                                            adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, SpinnerOptions
                                                                    , spinnerAndListAdapter, spinner, eachData.getString("field_type_name"));

                                                        } else {
                                                            Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);
                                                } catch (JSONException e) {
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);
                                                    e.printStackTrace();
                                                }
                                                shimmerFrameLayout.stopShimmer();
                                                shimmerFrameLayout.setVisibility(View.GONE);
                                                loadingLinearLayout.setVisibility(View.GONE);
                                                frameLayout.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                if (t instanceof TimeoutException) {
                                                    Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);

                                                }
                                                if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                                    Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);

                                                }
                                                if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                                    Log.d("info SubCat Exception", "NullPointert Exception" + t.getLocalizedMessage());
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);

                                                } else {
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);
                                                    Log.d("info SubCat error", String.valueOf(t));
                                                    Log.d("info SubCat error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                                }
                                            }
                                        });

                                    } else {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);

                                        Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
                                    }
                                    spinnerTouched = false;
                                }

                                try {
                                    if (eachData.getString("field_type_name").equals(
                                            jsonObject.getJSONObject("data").getJSONArray("hide_price").get(1))) {

                                        if (subcatDiloglistitem.isShow()) {
                                            Log.d("showwwwww===== ", "showwwwwww All  " + cardViewPriceInput.getChildCount());
                                            cardViewPriceInput.setVisibility(View.VISIBLE);
                                            if (cardViewPriceType != null)
                                                cardViewPriceType.setVisibility(View.VISIBLE);

                                            edittextShowHide = true;

                                        } else {
                                            cardViewPriceInput.setVisibility(View.GONE);
                                            if (cardViewPriceType != null)
                                                cardViewPriceType.setVisibility(View.GONE);

                                            edittextShowHide = false;
                                        }
                                    }
                                    if (eachData.getString("field_type_name").equals("ad_bidding")) {
                                        if (cardViewtBidingTimer != null) {
                                            if (subcatDiloglistitem.getId().equals("1")) {
                                                cardViewtBidingTimer.setVisibility(View.VISIBLE);
                                            } else {

                                                cardViewtBidingTimer.setVisibility(View.GONE);

                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                spinnerTouched = false;

//                                }
                            }

//                            if (position != 0) {
                            try {
                                if (eachData.getBoolean("has_cat_template"))
                                    if (subcatDiloglistitem.isHasCustom()) {
                                        linearLayoutCustom.removeAllViews();
                                        allViewInstanceforCustom.clear();
                                        catID = subcatDiloglistitem.getId();
                                        requiredFieldsForCustom.clear();
                                        adforest_showCustom(eachData);
                                        ison = true;
                                        Log.d("true===== ", "add All=======" + catID);

                                    } else {
                                        if (ison) {
                                            linearLayoutCustom.removeAllViews();
                                            allViewInstanceforCustom.clear();
                                            ison = false;
                                            Log.d("true====", "remove All");

                                        }
                                    }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            }
                            spinnerTouched = false;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }

                    });


                    linearLayout.addView(spinner, 1);
                    cardView.addView(linearLayout);
                    if (eachData.getString("has_page_number").

                            equals("1")) {
                        page1Layout.addView(cardView);
                        if (eachData.getBoolean("is_required")) {
                            page1RequiredFields.add(spinner);
                        }
                    }
                    if (eachData.getString("has_page_number").

                            equals("2")) {
                        page2Layout.addView(cardView);
                        if (eachData.getBoolean("is_required")) {
                            page2RequiredFields.add(spinner);
                        }
                    }
                }

                if (eachData.getString("field_type").equals("textfield")) {
                    TextInputLayout til = new TextInputLayout(context);
                    til.setHint(eachData.getString("title"));
                    EditText et = new EditText(context);

                    if (eachData.getString("field_type_name").equals("ad_price")) {
                        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }

                    et.setTextSize(14);
                    til.addView(et);
                    til.setLayoutParams(params);
                    allViewInstance.add(et);
                    if (jsonObject.getJSONObject("data").getString("title_field_name")
                            .equals(eachData.getString("field_type_name")))
                        editTextTitle = et;
                    if (eachData.getString("field_type_name").equals(
                            jsonObject.getJSONObject("data").getJSONArray("hide_price").get(0))) {
                        cardViewPriceInput = cardView;
                        editTextPrice = et;

                    } else {
                        if (eachData.getBoolean("is_required")) {
                            requiredFields.add(et);
                        }
                    }
                    et.setText(eachData.getString("field_val"));

                    cardView.addView(til);

                    cardView.setContentPadding(10, 20, 10, 20);

                    if (eachData.getString("field_type_name").equals("ad_bidding_time")) {
                        cardViewtBidingTimer = cardView;
                        if (!jsonObject.getJSONObject("extra").getBoolean("is_show_bidtime")) {
                            cardViewtBidingTimer.setVisibility(View.GONE);
                        }
                        et.setClickable(false);
                        et.setFocusable(false);
                        editTexttBidingTimer = et;
                        final EditText editText = et;
                        editTexttBidingTimer.setOnTouchListener((v, event) -> {
                            if (event.getAction() == MotionEvent.ACTION_DOWN)
                                adforest_showDateTime(editText);
                            return false;
                        });

                    }
                    if (eachData.getString("has_page_number").equals("1")) {
                        if (eachData.getBoolean("is_required")) {
                            page1RequiredFields.add(et);
                        }
                        page1Layout.addView(cardView);
                    }
                    if (eachData.getString("has_page_number").equals("2")) {
                        if (eachData.getBoolean("is_required")) {
                            page2RequiredFields.add(et);
                        }
                        page2Layout.addView(cardView);
                    }
                }

                if (eachData.getString("field_type").equals("textarea")) {
                    final RichEditor mEditor = new RichEditor(context);

                    mEditor.setEditorHeight(200);
                    mEditor.setPadding(10, 10, 10, 10);
                    mEditor.setPlaceholder(eachData.getString("title"));

                    mEditor.setHtml(eachData.getString("field_val"));

                    mEditor.setOnFocusChangeListener((view, b) -> {
                        if (b) {
                            horizontalScrollView.setVisibility(View.VISIBLE);
                        } else {
                            horizontalScrollView.setVisibility(View.GONE);
                        }
                    });

                    findViewById(R.id.action_undo).

                            setOnClickListener(v -> mEditor.undo());

                    findViewById(R.id.action_redo).

                            setOnClickListener(v -> mEditor.redo());

                    findViewById(R.id.action_bold).

                            setOnClickListener(v -> mEditor.setBold());

                    findViewById(R.id.action_italic).

                            setOnClickListener(v -> mEditor.setItalic());

                    findViewById(R.id.action_underline).

                            setOnClickListener(v -> mEditor.setUnderline());


                    findViewById(R.id.action_insert_bullets).

                            setOnClickListener(v -> mEditor.setBullets());

                    findViewById(R.id.action_insert_numbers).

                            setOnClickListener(v -> mEditor.setNumbers());
                    linearLayout.addView(mEditor, 1);

                    allViewInstance.add(mEditor);
                    if (eachData.getBoolean("is_required")) {
                        Log.d("field_types", eachData.toString());
                        requiredFields.add(linearLayout);
                    }

                    cardView.addView(linearLayout);

                    cardView.setContentPadding(10, 20, 10, 20);

                    if (eachData.getString("has_page_number").equals("1")) {
                        if (eachData.getBoolean("is_required")) {
                            page1RequiredFields.add(linearLayout);
                        }
                        page1Layout.addView(cardView);
                    }
                    if (eachData.getString("has_page_number").equals("2")) {
                        if (eachData.getBoolean("is_required")) {
                            page2RequiredFields.add(linearLayout);
                        }
                        page2Layout.addView(cardView);
                    }
                }

            }

            final JSONObject jsonObj = jsonObject.getJSONObject("data").getJSONObject("profile");
            textViewUserName.setText(jsonObj.getJSONObject("name").getString("title"));
            editTextUserName.setText(jsonObj.getJSONObject("name").getString("values"));
            textViewUserPhone.setText(jsonObj.getJSONObject("phone").getString("title"));
            editTextUserPhone.setText(jsonObj.getJSONObject("phone").getString("values"));
            if (!jsonObj.getBoolean("phone_editable")) {
                editTextUserPhone.setEnabled(false);
            }

            if (jsonObj.getBoolean("ad_country_show")) {
                showHideLocation.setVisibility(View.VISIBLE);
                textViewadCountry.setText(jsonObj.getJSONObject("ad_country").getString("title"));
                final JSONArray dropDownJSONOpt = jsonObj.getJSONObject("ad_country").getJSONArray("values");
                final ArrayList<subcatDiloglist> SpinnerOptions;
                SpinnerOptions = new ArrayList<>();
                for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                    subcatDiloglist subDiloglist = new subcatDiloglist();
                    subDiloglist.setId(dropDownJSONOpt.getJSONObject(j).getString("id"));
                    subDiloglist.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                    subDiloglist.setHasSub(dropDownJSONOpt.getJSONObject(j).getBoolean("has_sub"));
                    subDiloglist.setHasCustom(dropDownJSONOpt.getJSONObject(j).getBoolean("has_template"));

                    SpinnerOptions.add(subDiloglist);
                }
                final SpinnerAndListAdapter spinnerAndListAdapter;
                spinnerAndListAdapter = new SpinnerAndListAdapter(context, SpinnerOptions, true);
                allViewInstance.add(spinnerLocation);
                spinnerLocation.setAdapter(spinnerAndListAdapter);
                spinnerLocation.setSelection(0, false);

                spinnerLocation.setOnTouchListener((v, event) -> {
                    System.out.println("Real touch felt.");
                    spinnerTouched = true;
                    return false;
                });
                //on location clickListener
                spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (spinnerTouched) {
                            //String variant_name = dropDownJSONOpt.getJSONObject(position).getString("name");
                            final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) selectedItemView.getTag();

                            if (position != 0) {
                                if (subcatDiloglistitem.isHasSub()) {

                                    if (SettingsMain.isConnectingToInternet(context)) {

                                        loadingLinearLayout.setVisibility(View.VISIBLE);
                                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                                        shimmerFrameLayout.startShimmer();
                                        frameLayout.setVisibility(View.GONE);

                                        //for serlecting the location if location have sabLocations

                                        JsonObject params1 = new JsonObject();
                                        params1.addProperty("ad_country", subcatDiloglistitem.getId());

                                        Log.d("info SendLocations", params1.toString());
                                        Call<ResponseBody> myCall = restService.postGetSubLocations(params1, UrlController.AddHeaders(context));
                                        myCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                                try {
                                                    if (responseObj.isSuccessful()) {
                                                        Log.d("info SubLocation Resp", "" + responseObj.toString());

                                                        JSONObject response = new JSONObject(responseObj.body().string());
                                                        if (response.getBoolean("success")) {
                                                            Log.d("info SubLocation object", "" + response.getJSONObject("data"));
                                                            spinnerTouched = false;

                                                            adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, SpinnerOptions
                                                                    , spinnerAndListAdapter, spinnerLocation, "ad_country");

                                                        } else {
                                                            Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);
                                                } catch (JSONException e) {
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);

                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);

                                                    e.printStackTrace();
                                                }
                                                shimmerFrameLayout.stopShimmer();
                                                shimmerFrameLayout.setVisibility(View.GONE);
                                                loadingLinearLayout.setVisibility(View.GONE);
                                                frameLayout.setVisibility(View.VISIBLE);

                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                if (t instanceof TimeoutException) {
                                                    Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);
                                                }
                                                if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                                    Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);

                                                }
                                                if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                                    Log.d("info SubLocation==", "==Exception" + t.getLocalizedMessage());
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);

                                                } else {
                                                    shimmerFrameLayout.stopShimmer();
                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                    loadingLinearLayout.setVisibility(View.GONE);
                                                    frameLayout.setVisibility(View.VISIBLE);
                                                    Log.d("info SubLocation error", String.valueOf(t));
                                                    Log.d("info SubLocation error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                                }
                                            }
                                        });
                                    } else {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);
                                        Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
                                    }
                                    spinnerTouched = false;
                                }


                            }
                            spinnerTouched = false;
                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            //Location Country Vise


            textViewLocation.setText(jsonObj.getJSONObject("location").getString("title"));
            mAutocompleteTextView.setText(jsonObj.getJSONObject("location").getString("values"));

            textViewLat.setText(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("title"));
            editTextUserLat.setText(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("values"));
            try {
                point = new LatLng(Double.parseDouble(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("values")), Double.parseDouble(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("values")));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textViewLong.setText(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("title"));
            editTextuserLong.setText(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("values"));
            if (jsonObj.getJSONObject("map").getBoolean("on_off")) {
                linearLayoutMapView.setVisibility(View.VISIBLE);

                try {
                    if (!jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("values").equals("")) {
                        point = new LatLng(Double.parseDouble(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("values")), Double.parseDouble(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("values")));
                        final String markerTitle = jsonObj.getJSONObject("location").getString("values");
                        LinearLayout googleMapLayout = findViewById(R.id.googleMapLayout);
                        LinearLayout mapBoxLayout = findViewById(R.id.mapBoxLayout);

                        if (!jsonObj.getJSONObject("map").getString("map_style").equals("map_box")) {
                            placesContainer.setVisibility(View.GONE);
                            mAutocompleteTextView.setVisibility(View.VISIBLE);
                            mapBoxLayout.setVisibility(View.GONE);
                            googleMapLayout.setVisibility(View.VISIBLE);
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                            mapFragment.getMapAsync(googleMap -> {
                                googleMap.clear();
                                mMap.clear();
                                if (ActivityCompat.checkSelfPermission(EditAdPost.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EditAdPost.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                mMap.setMyLocationEnabled(true);
                                mMap.setOnMyLocationButtonClickListener(EditAdPost.this);
                                mMap.setOnMyLocationClickListener(EditAdPost.this);
                                googleMap.addMarker(new MarkerOptions().title(markerTitle).position(point));

                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                            });
                        } else {
                            mAutocompleteTextView.setVisibility(View.GONE);
                            placesContainer.setVisibility(View.VISIBLE);
                            googleMapLayout.setVisibility(View.GONE);
                            mapBoxLayout.setVisibility(View.VISIBLE);
                            mapView = findViewById(R.id.mapView);
                            mapView.getMapAsync(this);
                        }

                    }
                } catch (NumberFormatException n) {
                    n.printStackTrace();
                }
            } else {

                placesContainer.setVisibility(View.GONE);
                mAutocompleteTextView.setVisibility(View.VISIBLE);
                linearLayoutMapView.setVisibility(View.GONE);
            }
//            if (jsonObj.getJSONObject("map").getBoolean("on_off")) {
//                linearLayoutMapView.setVisibility(View.VISIBLE);
//            } else {
//                linearLayoutMapView.setVisibility(View.GONE);
//            }
//            try {
//                if (!jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("values").equals("")) {
//                    final LatLng point = new LatLng(Double.parseDouble(jsonObj.getJSONObject("map").getJSONObject("location_lat").getString("values")), Double.parseDouble(jsonObj.getJSONObject("map").getJSONObject("location_long").getString("values")));
//                    final String markerTitle = jsonObj.getJSONObject("location").getString("values");
//                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//                    mapFragment.getMapAsync(new OnMapReadyCallback() {
//                        @Override
//                        public void onMapReady(GoogleMap googleMap) {
//                            googleMap.clear();
//                            mMap.clear();
//                            if (ActivityCompat.checkSelfPermission(EditAdPost.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EditAdPost.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                // TODO: Consider calling
//                                //    ActivityCompat#requestPermissions
//                                // here to request the missing permissions, and then overriding
//                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                //                                          int[] grantResults)
//                                // to handle the case where the user grants the permission. See the documentation
//                                // for ActivityCompat#requestPermissions for more details.
//                                return;
//                            }
//                            mMap.setMyLocationEnabled(true);
//                            mMap.setOnMyLocationButtonClickListener(EditAdPost.this);
//                            mMap.setOnMyLocationClickListener(EditAdPost.this);
//                            googleMap.addMarker(new MarkerOptions().title(markerTitle).position(point));
//
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
//                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//
//                        }
//                    });
//                }
//            } catch (NumberFormatException n) {
//                n.printStackTrace();
//            }

            //Bum Ad Feature
            if (jsonObj.getBoolean("bump_ad_is_show")) {
                JSONObject bumpAd = jsonObj.getJSONObject("bump_ad");
                JSONObject alertObject = jsonObj.getJSONObject("bump_ad_text");
                Log.d("info AdPost Bump ad", bumpAd.toString());

                adforest_bumpNDFeatureAds(bumAdLayout, bumpAdTV, chkBxBumpAd, bumpAd, alertObject);
            }
            //Feature Ads
            if (jsonObj.getBoolean("featured_ad_is_show")) {
                JSONObject bumpAd = jsonObj.getJSONObject("featured_ad");
                JSONObject alertObject = jsonObj.getJSONObject("featured_ad_text");
                Log.d("info AdPost feature ad", bumpAd.toString());
                featureAdChkBox.setVisibility(View.VISIBLE);

                adforest_bumpNDFeatureAds(featureAdLayout, featureAdTV, featureAdChkBox, bumpAd, alertObject);
            }
            if (jsonObj.getBoolean("featured_ad_buy")) {
                featureAdLayout.setVisibility(View.VISIBLE);
                featureAdByPackages.setVisibility(View.VISIBLE);
                final JSONObject finalAlertText = jsonObj.getJSONObject("featured_ad_notify");
                featureAdTV.setText(finalAlertText.getString("text"));
                featureAdByPackages.setText(finalAlertText.getString("btn"));
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                featureAdByPackages.setOnClickListener(v -> {
                    try {
                        Toast.makeText(EditAdPost.this, finalAlertText.getString("text"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });

            }
            final JSONArray dropDownJSONOpt = jsonObject.getJSONObject("extra").getJSONArray("price_type_data");
            if (!dropDownJSONOpt.getJSONObject(0).getBoolean("is_show")) {
                if (cardViewPriceInput != null)
                    cardViewPriceInput.setVisibility(View.GONE);
                if (cardViewPriceType != null)
                    cardViewPriceType.setVisibility(View.GONE);
                edittextShowHide = false;
            }
            if (jsonObject.getJSONObject("extra").getBoolean("adpost_terms_switch")) {
                terms_conditionsTitleTV.setText(jsonObject.getJSONObject("extra").getString("adpost_terms_title"));
//            terms_conditionsTitleTV.setHtml("<b>" + jsonObject.getJSONObject("extra").getString("adpost_terms_title") + "</b> : " +
//                    "<a href=" + jsonObject.getJSONObject("extra").getString("adpost_terms_title") + ">"+ "</a>", new HtmlResImageGetter(terms_conditionsTitleTV));
                terms_conditionChkBox.setVisibility(View.VISIBLE);

//            if(!terms_conditionChkBox.isChecked()) {
//
//
//            }else

                terms_conditionsTitleTV.setMovementMethod(LinkMovementMethod.getInstance());
                terms_conditionsTitleTV.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Uri uri = null; // missing 'http://' will cause crashed
                        try {
                            uri = Uri.parse(jsonObject.getJSONObject("extra").getString("adpost_terms_url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                            browserIntent.setData(Uri.parse("https://www.google.com"));
//                        startActivity(browserIntent);

                    }
                });
            } else
                terms_ConditionLayout.setVisibility(View.GONE);

            btnPostAd.setText(jsonObject.getJSONObject("data").getString("btn_submit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //show bump and feature ads
    private void adforest_bumpNDFeatureAds(RelativeLayout relativeLayout, TextView textView,
                                           final CheckBox checkBox
            , JSONObject adObject, final JSONObject alertObject) {
        String alertTile = null, alertText = null, alertYesBtn = null, alertNoBtn = null;
        relativeLayout.setVisibility(View.VISIBLE);
        try {
            textView.setText(adObject.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("info AdPost CheckBox", adObject.toString());
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        try {
            alertTile = alertObject.getString("title");
            alertText = alertObject.getString("text");
            alertYesBtn = alertObject.getString("btn_ok");
            alertNoBtn = alertObject.getString("btn_no");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalAlertTile = alertTile;
        final String finalAlertText = alertText;
        final String finalAlertYesBtn = alertYesBtn;
        final String finalAlertNoBtn = alertNoBtn;
        checkBox.setOnClickListener(v -> {
            alert.setTitle(finalAlertTile);
            alert.setCancelable(false);
            alert.setMessage(finalAlertText);
            alert.setPositiveButton(finalAlertYesBtn, (dialog, which) -> {
                checkBox.setChecked(true);
                dialog.dismiss();
            });
            alert.setNegativeButton(finalAlertNoBtn, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                checkBox.setChecked(false);
            });
            alert.show();
        });
    }

    //showing dilog for catgory selection
    private void adforest_ShowDialog(JSONObject data, final subcatDiloglist main,
                                     final ArrayList<subcatDiloglist> spinnerOptionsout,
                                     final SpinnerAndListAdapter spinnerAndListAdapterout,
                                     final Spinner spinner1, final String field_type_name) {

        Log.d("info Show Dialog ===== ", "adforest_ShowDialog");
        try {
            Log.d("info Show Dialog ===== ", data.getJSONArray("values").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Dialog dialog = new Dialog(context, R.style.PauseDialog);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_sub_cat);

        dialog.setTitle(main.getName());
        ListView listView = dialog.findViewById(R.id.listView);

        final ArrayList<subcatDiloglist> listitems = new ArrayList<>();
        final JSONArray listArray;
        try {
            listArray = data.getJSONArray("values");
            for (int j = 0; j < listArray.length(); j++) {
                subcatDiloglist subDiloglist = new subcatDiloglist();
                subDiloglist.setId(listArray.getJSONObject(j).getString("id"));
                subDiloglist.setName(listArray.getJSONObject(j).getString("name"));
                subDiloglist.setHasSub(listArray.getJSONObject(j).getBoolean("has_sub"));
                subDiloglist.setHasCustom(listArray.getJSONObject(j).getBoolean("has_template"));
                listitems.add(subDiloglist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SpinnerAndListAdapter spinnerAndListAdapter1 = new SpinnerAndListAdapter(context, listitems);
        listView.setAdapter(spinnerAndListAdapter1);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) view.getTag();

            //Log.d("helllo" , spinnerOptionsout.adforest_get(1).getId() + " === " + spinnerOptionsout.adforest_get(1).getName());

            if (!spinnerOptionsout.get(0).getId().equals(subcatDiloglistitem.getId())) {

                if (subcatDiloglistitem.isHasSub()) {


                    if (SettingsMain.isConnectingToInternet(context)) {
                        loadingLinearLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.startShimmer();
                        frameLayout.setVisibility(View.GONE);

                        //if user select subcategoreis and again select subCategoreis
                        if (field_type_name.equals("ad_cats1")) {
                            JsonObject params = new JsonObject();
                            params.addProperty("subcat", subcatDiloglistitem.getId());

                            Log.d("info SendSubCatAg", params.toString());
                            Call<ResponseBody> myCall = restService.postGetSubCategories(params, UrlController.AddHeaders(context));
                            myCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                    try {
                                        if (responseObj.isSuccessful()) {
                                            Log.d("info SendSubCatAg Resp", "" + responseObj.toString());

                                            JSONObject response = new JSONObject(responseObj.body().string());
                                            if (response.getBoolean("success")) {
                                                Log.d("info SubCatAg object", "" + response.getJSONObject("data"));

                                                adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, spinnerOptionsout
                                                        , spinnerAndListAdapterout, spinner1, field_type_name);

                                            } else {
                                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);
                                    } catch (JSONException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);
                                        e.printStackTrace();
                                    }
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLinearLayout.setVisibility(View.GONE);
                                    frameLayout.setVisibility(View.VISIBLE);

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    if (t instanceof TimeoutException) {
                                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);

                                    }
                                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);

                                    }
                                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                        Log.d("info SubCatAg Exception", "NullPointert Exception" + t.getLocalizedMessage());
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);

                                    } else {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);
                                        Log.d("info SubCatAg error", String.valueOf(t));
                                        Log.d("info SubCatAg error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                    }
                                }
                            });
                        }

                        //if user select subLocation and again select subLocation
                        if (field_type_name.equals("ad_country")) {

                            JsonObject params1 = new JsonObject();
                            params1.addProperty("ad_country", subcatDiloglistitem.getId());

                            Log.d("info SendLocationsAg", params1.toString());
                            Call<ResponseBody> myCall = restService.postGetSubLocations(params1, UrlController.AddHeaders(context));
                            myCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                    try {
                                        if (responseObj.isSuccessful()) {
                                            Log.d("info SubLocationAg Resp", "" + responseObj.toString());

                                            JSONObject response = new JSONObject(responseObj.body().string());
                                            if (response.getBoolean("success")) {
                                                Log.d("info SubLocationAg obj", "" + response.getJSONObject("data"));

                                                adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, spinnerOptionsout
                                                        , spinnerAndListAdapterout, spinner1, field_type_name);

                                            } else {
                                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);
                                    } catch (JSONException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);

                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);

                                        e.printStackTrace();
                                    }
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLinearLayout.setVisibility(View.GONE);
                                    frameLayout.setVisibility(View.VISIBLE);

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    if (t instanceof TimeoutException) {
                                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);

                                    }
                                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);

                                    }
                                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                        Log.d("info SubLocationAg", "Exception" + t.getLocalizedMessage());
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);

                                    } else {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        frameLayout.setVisibility(View.VISIBLE);
                                        Log.d("info SubLocationAg err", String.valueOf(t));
                                        Log.d("info SubLocationAg err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                    }
                                }
                            });

                        }
                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    for (int ii = 0; ii < spinnerOptionsout.size(); ii++) {
                        if (spinnerOptionsout.get(ii).getId().equals(subcatDiloglistitem.getId())) {
                            spinnerOptionsout.remove(ii);
                            Log.d("info ===== ", "else of list inner is 1st button into for loop");
                            break;
                        }
                    }
                    Log.d("info ===== ", "else of list inner is 1st button out of for loop");

                    spinnerOptionsout.add(1, subcatDiloglistitem);
                    spinner1.setSelection(1, false);
                    spinnerAndListAdapterout.notifyDataSetChanged();

                }

                if (subcatDiloglistitem.isHasCustom() && field_type_name.equals("ad_cats1")) {
                    linearLayoutCustom.removeAllViews();
                    allViewInstanceforCustom.clear();
                    catID = subcatDiloglistitem.getId();
                    adforest_showCustom(data);
                    ison = true;
                    Log.d("true===== ", "inter add All");

                } else {
                    if (ison && field_type_name.equals("ad_cats1")) {
                        linearLayoutCustom.removeAllViews();
                        allViewInstanceforCustom.clear();
                        ison = false;
                        Log.d("true===== ", "inter remove All");
                    }
                }
            } else {
                spinner1.setSelection(1, false);
                Log.d("info ===== ", "else of chk is 1st button out");

            }
            dialog.dismiss();
        });

        Button Send = dialog.findViewById(R.id.send_button);
        Button Cancel = dialog.findViewById(R.id.cancel_button);

        try {
            Send.setText(jsonObject.getJSONObject("extra").getString("dialog_send"));
            Cancel.setText(jsonObject.getJSONObject("extra").getString("dialg_cancel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Send.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        Cancel.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        Send.setOnClickListener(v -> {

            for (int i = 0; i < spinnerOptionsout.size(); i++) {
                if (spinnerOptionsout.get(i).getId().equals(main.getId())) {
                    spinnerOptionsout.remove(i);
                    Log.d("info ===== ", "send button in");
                    break;
                }
            }

            spinnerOptionsout.add(1, main);
            spinnerAndListAdapterout.notifyDataSetChanged();
            spinner1.setSelection(1, false);
            Log.d("info ===== ", "send button out");

            dialog.dismiss();
        });

        Cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    //api calling for getting catgory type custom fields...
    private void adforest_showCustom(JSONObject eachData) {

        if (linearLayoutCustom != null) {

            if (SettingsMain.isConnectingToInternet(context)) {

                JsonObject params = new JsonObject();
                params.addProperty("cat_id", catID);
                params.addProperty("ad_id", addId);
                Log.d("info Send DynamicFields", params.toString());

                Call<ResponseBody> myCall = restService.postGetDynamicFields(params, UrlController.AddHeaders(this));
                myCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                        try {
                            if (responseObj.isSuccessful()) {
                                Log.d("info DynamicFields Resp", "" + responseObj.toString());

                                JSONObject response = new JSONObject(responseObj.body().string());

                                if (response.getBoolean("success")) {
                                    bid_check = response.getBoolean("bid_check");

                                    Log.d("info bidCheck", String.valueOf(bid_check));
                                    if (bid_check) {
                                        if (eachData.getString("field_type_name").equals("ad_bidding") || eachData.getString("field_type_name").equals("ad_bidding_time")) {
                                            meraCardView.setVisibility(View.VISIBLE);
                                            cardViewtBidingTimer.setVisibility(View.VISIBLE);

                                        }
                                    } else {
                                        if (!eachData.getString("field_type_name").equals("ad_bidding") || !eachData.getString("field_type_name").equals("ad_bidding_time")) {
                                            meraCardView.setVisibility(View.GONE);
                                            catCard.setVisibility(View.VISIBLE);

                                        }

                                    }

                                    adforest_setViewsForCustom(response);
                                    Log.d("info data DynamicFields", "" + response.getJSONArray("data"));
                                } else {
                                    Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLinearLayout.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLinearLayout.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        } catch (IOException e) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLinearLayout.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (t instanceof TimeoutException) {
                            Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLinearLayout.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);

                        }
                        if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                            Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLinearLayout.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);
                        }
                        if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                            Log.d("info DynamicFields", "NullPointert Exception" + t.getLocalizedMessage());
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLinearLayout.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);

                        } else {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLinearLayout.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);

                            Log.d("info DynamicFields err", String.valueOf(t));
                            Log.d("info DynamicFields err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                        }
                    }
                });
            } else {
                Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    // generating custom fields of catagory related
    void adforest_setViewsForCustom(JSONObject jsonObjec) {
        linearLayoutCustom.removeAllViews();

        try {
            edittextShowHide = true;
            jsonObjectforCustom = jsonObjec;
            Log.d("info custom data ", jsonObjectforCustom.toString());
            JSONArray customOptnList = jsonObjectforCustom.getJSONArray("data");

            requiredFieldsForCustom.clear();

            for (int noOfCustomOpt = 0; noOfCustomOpt < customOptnList.length(); noOfCustomOpt++) {

                CardView cardView = new CardView(context);
                cardView.setCardElevation(2);
                cardView.setUseCompatPadding(true);
                cardView.setRadius(0);
                cardView.setContentPadding(10, 10, 10, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 10;
                params.bottomMargin = 10;
                cardView.setLayoutParams(params);

                final JSONObject eachData = customOptnList.getJSONObject(noOfCustomOpt);
                TextView customOptionsName = new TextView(context);
                customOptionsName.setTextSize(12);
                customOptionsName.setAllCaps(true);
                customOptionsName.setTextColor(Color.BLACK);
                customOptionsName.setPadding(10, 15, 10, 15);
                customOptionsName.setText(eachData.getString("title"));
                customOptionsName.setFocusable(true);

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.addView(customOptionsName);

                if (eachData.getString("field_type").equals("select")) {

                    final JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                    final ArrayList<subcatDiloglist> SpinnerOptions;
                    SpinnerOptions = new ArrayList<>();
                    for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                        subcatDiloglist subDiloglist = new subcatDiloglist();
                        subDiloglist.setId(dropDownJSONOpt.getJSONObject(j).getString("id"));
                        subDiloglist.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                        subDiloglist.setHasSub(dropDownJSONOpt.getJSONObject(j).getBoolean("has_sub"));

                        //String optionString = dropDownJSONOpt.getJSONObject(j).getString("name");
                        if (eachData.getString("field_type_name").equals(
                                jsonObject.getJSONObject("data").getJSONArray("hide_price").get(1))) {
                            subDiloglist.setShow(dropDownJSONOpt.getJSONObject(j).getBoolean("is_show"));
                        }
                        SpinnerOptions.add(subDiloglist);
                    }
                    final SpinnerAndListAdapter spinnerAndListAdapter;

                    spinnerAndListAdapter = new SpinnerAndListAdapter(context, SpinnerOptions, true);

                    final Spinner spinner = new Spinner(context);

                    allViewInstanceforCustom.add(spinner);
                    spinner.setAdapter(spinnerAndListAdapter);
                    spinner.setSelection(0, false);
                    spinner.setFocusable(true);
                    spinner.setOnTouchListener((v, event) -> {
                        System.out.println("Real touch felt.");
                        spinnerTouched = true;
                        return false;
                    });

                    if (eachData.getString("field_type_name").equals(
                            jsonObject.getJSONObject("data").getJSONArray("hide_currency").get(1))) {
                        cardViewPriceType = cardView;
                    }
                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(spinner);
                    }

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (spinnerTouched) {
                                //String variant_name = dropDownJSONOpt.getJSONObject(position).getString("name");
                                final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) selectedItemView.getTag();


                                try {
                                    if (eachData.getString("field_type_name").equals(
                                            jsonObject.getJSONObject("data").getJSONArray("hide_price").get(1))) {
                                        if (subcatDiloglistitem.isShow()) {
                                            Log.d("showwwwww===== ", "showwwwwww All  " + cardViewPriceInput.getChildCount());
                                            cardViewPriceInput.setVisibility(View.VISIBLE);
                                            edittextShowHide = true;
                                            if (cardViewPriceType != null)
                                                cardViewPriceType.setVisibility(View.VISIBLE);

                                        } else {
                                            cardViewPriceInput.setVisibility(View.GONE);
                                            if (cardViewPriceType != null)
                                                cardViewPriceType.setVisibility(View.GONE);

                                            edittextShowHide = false;
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            spinnerTouched = false;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }

                    });

                    linearLayout.addView(spinner, 1);
                    cardView.addView(linearLayout);
                    linearLayoutCustom.addView(cardView);
                }

                if (eachData.getString("field_type").equals("textfield")) {
                    TextInputLayout til = new TextInputLayout(context);
                    til.setHint(eachData.getString("title"));
                    EditText et = new EditText(context);
                    if (eachData.getString("field_type_name").equals("ad_price")) {
                        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    }
                    et.setTextSize(14);
                    et.setText(eachData.getString("field_val"));
                    et.setFocusable(true);
                    til.addView(et);
                    allViewInstanceforCustom.add(et);
                    cardView.addView(til);
                    if (jsonObject.getJSONObject("data").getString("title_field_name")
                            .equals(eachData.getString("field_type_name")))
                        editTextTitle = et;
                    if (eachData.getString("field_type_name").equals(
                            jsonObject.getJSONObject("data").getJSONArray("hide_price").get(0))) {
                        cardViewPriceInput = cardView;
                        editTextPrice = et;

                    } else if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(et);
                    }
                    linearLayoutCustom.addView(cardView);
                }

                if (eachData.getString("field_type").equals("radio")) {
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.topMargin = 3;
                    params2.bottomMargin = 3;

                    final JSONArray radioButtonJSONOpt = eachData.getJSONArray("values");
                    RadioGroup rg = new RadioGroup(context); //create the RadioGroup
                    allViewInstanceforCustom.add(rg);
                    for (int j = 0; j < radioButtonJSONOpt.length(); j++) {

                        RadioButton rb = new RadioButton(context);
                        rg.addView(rb, params2);
                        if (j == 0)

                            rb.setLayoutParams(params2);
                        rb.setFocusable(true);
                        rb.setTag(radioButtonJSONOpt.getJSONObject(j).getString("id"));
                        rb.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        String optionString = radioButtonJSONOpt.getJSONObject(j).getString("name");
                        if (radioButtonJSONOpt.getJSONObject(j).getBoolean("is_checked")) {
                            rb.setChecked(true);
                        }
                        rb.setText(optionString);
                        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                View radioButton = group.findViewById(checkedId);
                                String variant_name = radioButton.getTag().toString();
                            }
                        });

                    }
                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(rg);
                    }
                    linearLayout.addView(rg, params2);
                    cardView.addView(linearLayout);
                    linearLayoutCustom.addView(cardView);
                }
                if (eachData.getString("field_type").equals("checkbox")) {
                    Log.d("info add", noOfCustomOpt + "");
                    LinearLayout linearLayout1 = new LinearLayout(context);
                    linearLayout1.setOrientation(LinearLayout.VERTICAL);

                    JSONArray checkBoxJSONOpt = eachData.getJSONArray("values");

                    for (int j = 0; j < checkBoxJSONOpt.length(); j++) {

                        CheckBox checkBox = new CheckBox(context);
                        checkBox.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        checkBox.setTag(checkBoxJSONOpt.getJSONObject(j).getString("id"));
                        Log.d("idcheckbox", checkBoxJSONOpt.getJSONObject(j).getString("id"));
                        if (checkBoxJSONOpt.getJSONObject(j).getBoolean("is_checked"))
                            checkBox.setChecked(true);
                        checkBox.setFocusable(true);
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params2.topMargin = 3;
                        params2.bottomMargin = 3;
                        String optionString = checkBoxJSONOpt.getJSONObject(j).getString("name");
                        checkBox.setText(optionString);
                        linearLayout1.addView(checkBox, params2);
                    }
                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(linearLayout1);
                    }

                    allViewInstanceforCustom.add(linearLayout1);
                    linearLayout.addView(linearLayout1);
                    cardView.addView(linearLayout);
                    linearLayoutCustom.addView(cardView);

                }
                if (eachData.getString("field_type").equals("textfield_date")) {
                    TextInputLayout til = new TextInputLayout(context);
                    til.setHint(eachData.getString("title"));
                    EditText et = new EditText(context);
                    et.setText(eachData.getString("field_val"));
                    et.setTextSize(14);
                    et.setFocusable(true);
                    Drawable img = getResources().getDrawable(R.drawable.ic_calendar);
                    et.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, img, null);

                    til.addView(et);
                    til.setLayoutParams(params);

                    cardView.addView(til);

                    cardView.setContentPadding(10, 20, 10, 20);
                    et.setClickable(false);
                    et.setFocusable(false);
                    final EditText editText = et;
                    editText.setOnClickListener(v -> adforest_showDate(editText));
//                    editText.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            if (event.getAction() == MotionEvent.ACTION_DOWN)
//                                adforest_showDateTime(editText);
//                            return false;
//                        }
//                    });

                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(et);
                    }
                    allViewInstanceforCustom.add(et);
                    linearLayoutCustom.addView(cardView);
                }
                if (eachData.getString("field_type").equals("textfield_url")) {
                    TextInputLayout til = new TextInputLayout(context);
                    til.setHint(eachData.getString("title"));
                    EditText et = new EditText(context);

                    et.setTextSize(14);
                    et.setFocusable(true);
                    et.setTag("textfield_url");
                    et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);

                    til.addView(et);
                    til.setLayoutParams(params);


                    cardView.addView(til);

                    cardView.setContentPadding(10, 20, 10, 20);

                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(et);
                    }
                    allViewInstanceforCustom.add(et);
                    linearLayoutCustom.addView(cardView);
                }
                if (eachData.getString("field_type").equals("textfield_number")) {
                    TextInputLayout til = new TextInputLayout(context);
                    til.setHint(eachData.getString("title"));
                    final EditText et = new EditText(context);
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    til.addView(et);
                    et.setText(eachData.getString("field_val"));
                    cardView.addView(til);
                    cardView.setContentPadding(10, 20, 10, 20);

                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(et);
                    }
                    allViewInstanceforCustom.add(et);
                    linearLayoutCustom.addView(cardView);
                }
                if (eachData.getString("field_type").equals("radio_color")) {
                    HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.topMargin = 3;
                    params2.bottomMargin = 3;
                    horizontalScrollView.setLayoutParams(params2);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.topMargin = 3;
                    layoutParams.bottomMargin = 3;
                    layoutParams.setMarginEnd(5);

                    final JSONArray radioButtonJSONOpt = eachData.getJSONArray("values");
                    RadioGroup rg = new RadioGroup(context); //create the RadioGroup
                    rg.setOrientation(LinearLayout.HORIZONTAL);
                    for (int j = 0; j < radioButtonJSONOpt.length(); j++) {

                        RadioButton rb = new RadioButton(context);
                        rg.addView(rb, layoutParams);
                        rb.setLayoutParams(layoutParams);
                        rb.setTag(radioButtonJSONOpt.getJSONObject(j).getString("id"));
                        if (radioButtonJSONOpt.getJSONObject(j).getBoolean("is_checked")) {
                            rb.setChecked(true);
                        }

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{
                                        new int[]{-android.R.attr.state_enabled}, //disabled
                                        new int[]{android.R.attr.state_enabled} //enabled
                                },
                                new int[]{
                                        Color.BLACK //disabled
                                        , Color.parseColor(radioButtonJSONOpt.getJSONObject(j).getString("id")) //enabled
                                }
                        );


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            rb.setButtonTintList(colorStateList);//set the color tint list
                        }

                        rg.setOnCheckedChangeListener((group, checkedId) -> {
                            View radioButton = group.findViewById(checkedId);
//                                String variant_name = radioButton.getTag().toString();
//                                Toast.makeText(getActivity(), variant_name + "", Toast.LENGTH_LONG).show();
                        });

                    }
                    if (eachData.getBoolean("is_required")) {
                        requiredFieldsForCustom.add(rg);
                    }
                    allViewInstanceforCustom.add(rg);
                    horizontalScrollView.addView(rg);
                    linearLayout.addView(horizontalScrollView, params2);
                    linearLayoutCustom.addView(linearLayout);
                }


            }
//                        Log.d("dataaaaaa",eachData.getJSONArray("values").getString(""));
            final JSONArray dropDownJSONOpt = jsonObject.getJSONObject("extra").getJSONArray("price_type_data");
            if (!dropDownJSONOpt.getJSONObject(0).getBoolean("is_show")) {
                cardViewPriceInput.setVisibility(View.GONE);
                if (cardViewPriceType != null)
                    cardViewPriceType.setVisibility(View.GONE);
                edittextShowHide = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //getting data from created fields
    public JsonObject adforest_getDataFromDynamicViews() {
        JsonObject optionsObj = null;

        try {
            JSONArray customOptnList = jsonObject.getJSONObject("data").getJSONArray("fields");
            optionsObj = new JsonObject();
            for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {
                JSONObject eachData = customOptnList.getJSONObject(noOfViews);

                if (eachData.getString("field_type").equals("select")) {
                    Spinner spinner = (Spinner) allViewInstance.get(noOfViews);

                    subcatDiloglist subcatDiloglist1 = (subcatDiloglist) spinner.getSelectedView().getTag();
                    JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                    String variant_name = dropDownJSONOpt.getJSONObject(spinner.getSelectedItemPosition()).getString("id");
                    Log.d("value id", variant_name + "");


                    optionsObj.addProperty(eachData.getString("field_type_name"),
                            "" + subcatDiloglist1.getId());
                }
                if (eachData.getString("field_type").equals("textfield")) {
                    TextView textView = (TextView) allViewInstance.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    else
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }

                if (eachData.getString("field_type").equals("textarea")) {
                    RichEditor textView = (RichEditor) allViewInstance.get(noOfViews);
                    optionsObj.addProperty(eachData.getString("field_type_name"), textView.getHtml());
                    Log.d("variant_name", textView.getHtml() + "");
                }

                if (eachData.getString("field_type").equals("glocation_textfield")) {
                    TextView textView = (TextView) allViewInstance.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    else
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }
                if (eachData.getString("field_type").equals("range_textfield")) {
                    LinearLayout linearLayout = (LinearLayout) allViewInstance.get(noOfViews);

                    TextInputLayout textView = (TextInputLayout) linearLayout.getChildAt(0);
                    TextInputLayout textView2 = (TextInputLayout) linearLayout.getChildAt(1);

                    if (textView.getEditText() != null && textView2.getEditText() != null)
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getEditText().getText().toString() + "-" +
                                textView2.getEditText().getText().toString());
                }
            }

            hideSoftKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("array us", (optionsObj != null ? optionsObj.toString() : null) + " ==== size====  " + allViewInstance.size());

        return optionsObj;
    }

    //getting data from custom created fields generated by catgory selection...
    public JsonObject adforest_getDataFromDynamicViewsForCustom() {
        JsonObject optionsObj = null;

        try {
            JSONArray customOptnList = jsonObjectforCustom.getJSONArray("data");
            optionsObj = new JsonObject();
            for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {
                JSONObject eachData = customOptnList.getJSONObject(noOfViews);

                if (eachData.getString("field_type").equals("select")) {
                    Spinner spinner = (Spinner) allViewInstanceforCustom.get(noOfViews);

                    subcatDiloglist subcatDiloglist1 = (subcatDiloglist) spinner.getSelectedView().getTag();
                    JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                    String variant_name = dropDownJSONOpt.getJSONObject(spinner.getSelectedItemPosition()).getString("id");
                    Log.d("value id", variant_name + "");

                    optionsObj.addProperty(eachData.getString("field_type_name"),
                            "" + subcatDiloglist1.getId());
                }
                if (eachData.getString("field_type").equals("textfield")) {
                    TextView textView = (TextView) allViewInstanceforCustom.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    else
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }

                if (eachData.getString("field_type").equals("radio")) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstanceforCustom.get(noOfViews);
                    RadioButton selectedRadioBtn = context.findViewById(radioGroup.getCheckedRadioButtonId());
                    if (selectedRadioBtn != null) {
                        Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                        optionsObj.addProperty(eachData.getString("field_type_name"),
                                "" + selectedRadioBtn.getTag().toString());
                    }
                }

                if (eachData.getString("field_type").equals("checkbox")) {
                    LinearLayout linearLayout = (LinearLayout) allViewInstanceforCustom.get(noOfViews);
                    JSONArray checkBoxJSONOpt = eachData.getJSONArray("values");
                    String values = "";
                    for (int j = 0; j < checkBoxJSONOpt.length(); j++) {
                        CheckBox chk = (CheckBox) linearLayout.getChildAt(j);
                        if (chk.isChecked()) {
                            values = values.concat("," + chk.getTag());
                        }
                    }
                    optionsObj.addProperty(eachData.getString("field_type_name"), values);
                }
                if (eachData.getString("field_type").equals("textfield_date")) {
                    TextView textView = (TextView) allViewInstanceforCustom.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    else
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }

                if (eachData.getString("field_type").equals("textfield_url")) {
                    TextView textView = (TextView) allViewInstanceforCustom.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    else
                        optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }
                if (eachData.getString("field_type").equals("textfield_number")) {

                    EditText editText = (EditText) allViewInstanceforCustom.get(noOfViews);

                    if (!editText.getText().toString().equalsIgnoreCase(""))
                        optionsObj.addProperty(eachData.getString("field_type_name"), editText.getText().toString());
                    else
                        optionsObj.addProperty(eachData.getString("field_type_name"), editText.getText().toString());
                    Log.d("variant_name", editText.getText().toString() + "");


                }
                if (eachData.getString("field_type").equals("radio_color")) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstanceforCustom.get(noOfViews);
                    RadioButton selectedRadioBtn = context.findViewById(radioGroup.getCheckedRadioButtonId());
                    if (selectedRadioBtn != null) {
                        Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                        optionsObj.addProperty(eachData.getString("field_type_name"),
                                "" + selectedRadioBtn.getTag().toString());
                    }
                }
            }

            hideSoftKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("array us custom", (optionsObj != null ? optionsObj.toString() : null) + " ==== size====  " + allViewInstanceforCustom.size());

        return optionsObj;
    }

    public void hideSoftKeyboard() {
        if (context.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (page2.getVisibility() == View.VISIBLE) {
            imageViewBack1.performClick();
        } else if (page3.getVisibility() == View.VISIBLE) {
            imageViewBack2.performClick();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
        }
        return true;
    }

    private void delImage(String tag) {

        if (SettingsMain.isConnectingToInternet(context)) {

            loadingLinearLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            frameLayout.setVisibility(View.GONE);
            JsonObject params = new JsonObject();
            params.addProperty("img_id", tag);
            params.addProperty("ad_id", addId);
            params.addProperty("is_required", isRequired);

            Log.d("info send DeleteImage", params.toString());
            Call<ResponseBody> myCall = restService.postDeleteImages(params, UrlController.AddHeaders(this));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info GetAdnewPost Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());

                            if (response.getBoolean("success")) {
                                Log.d("info DeleteImage object", "" + response.toString());
                                adforest_updateImages(response.getJSONObject("data"));
                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        e.printStackTrace();
                    } catch (IOException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        e.printStackTrace();
                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    loadingLinearLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(context, settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info DeleteImage", "NullPointert Exception" + t.getLocalizedMessage());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        Log.d("info DeleteImage err", String.valueOf(t));
                        Log.d("info DeleteImage err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            loadingLinearLayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
        }
    }


    void adforest_initiImageList(JSONObject jsonObject) {
        myImages.clear();
        try {
            imageLimit = jsonObject.getJSONObject("images").getInt("numbers");
            per_limit = jsonObject.getJSONObject("images").getInt("per_limit");
            stringImageLimitText = jsonObject.getJSONObject("images").getString("message");
            isRequired = jsonObject.getJSONObject("images").getBoolean("is_required");

            JSONArray jsonArrayImages = jsonObject.getJSONArray("ad_images");

            totalUploadedImages = jsonArrayImages.length();
            Log.d("info Images Data", "" + jsonArrayImages.toString());

            if (jsonArrayImages.length() > 0) {
                recyclerView.setVisibility(View.VISIBLE);

                for (int i = 0; i < jsonArrayImages.length(); i++) {
                    myAdsModel item = new myAdsModel();
                    JSONObject object = new JSONObject();
                    object = jsonArrayImages.getJSONObject(i);

                    item.setAdId(object.getString("img_id"));
                    item.setImage((object.getString("thumb")));


                    myImages.add(item);
                }
                textViewInfoforDrag.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.GONE);
//                recyclerView.setAdapter(null);
                textViewInfoforDrag.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void adforest_updateImages(JSONObject jsonObject) {
        adforest_initiImageList(jsonObject);
        itemEditImageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        try {

            if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals(""))
                AnalyticsTrackers.getInstance().trackScreenView("Edit Add");

            super.onResume();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void adforest_showDate(final EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditAdPost.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, java.util.Locale.getDefault());
                if (editText != null)
                    editText.setText(sdf.format(myCalendar.getTime()));
            }
        }, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void adforest_showDateTime(final EditText editText) {

        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        CalanderTextModel calanderTextModel = settingsMain.getCalanderTextModel(context);

        if (dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    calanderTextModel.getTitle(),
                    calanderTextModel.getBtn_ok(),
                    calanderTextModel.getBtn_cancel());
        }
//        // Init format
//        Date date = new Date();
//        String strDateFormat = "M";
//        final SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Log.d("info calender", year + "" + month + "" + day);
//        2018-04-11 11:09:00
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        // Assign unmodifiable values
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(year, month, day).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());

        // Define new day and month format
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set listener for date
        // Or use dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                editText.setText(myDateFormat.format(date));
//                Toast.makeText(context, myDateFormat.format(date) + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Do nothing
            }

            @Override
            public void onNeutralButtonClick(Date date) {
                // Optional if neutral button does'nt exists
//                myDateFormat.format(date)            }
            }
        });
        dateTimeFragment.startAtCalendarView();

        dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);

    }

    @Override
    public void onSuccessPermission(int code) {
        if (imageLimit < per_limit) {
            per_limit = imageLimit;
        }
        if (imageLimit > 0) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {

                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                startActivityForResult(i, 1234);
            } else {
                new GligarPicker().requestCode(69).withActivity(this)
                        .limit(per_limit).show();

            }

//            FilePickerBuilder.getInstance().setMaxCount(per_limit)
//                    .setSelectedFiles(paths)
//                    .setActivityTheme(R.style.Theme_AppCompat_DayNight_DarkActionBar)
//                    .pickPhoto(EditAdPost.this);
            Toast.makeText(context, stringImageLimitText, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, stringImageLimitText, Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyncImageTask extends AsyncTask<ArrayList<String>, Void, List<MultipartBody.Part>> {
        ArrayList<String> imaagesLis = null;
        boolean checkDimensions = true, checkImageSize;


        @SafeVarargs
        @Override
        protected final List<MultipartBody.Part> doInBackground(ArrayList<String>... params) {
            List<MultipartBody.Part> parts = null;
            imaagesLis = params[0];
            successfullyUploadedImagesCount = 0;
            totalFiles = imaagesLis.size();
            for (int i = 0; i < imaagesLis.size(); i++) {
                parts = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date());
                Log.d("info image", currentDateandTime);
                checkDimensions = true;
                checkImageSize = true;
                if (adPostImageModel.getDim_is_show()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imaagesLis.get(i), options);
                    int imageWidth = options.outWidth;
                    int imageHeight = options.outHeight;
                    if (imageHeight > Integer.parseInt(adPostImageModel.getDim_height()) &&
                            imageWidth > Integer.parseInt(adPostImageModel.getDim_width())) {
                        checkDimensions = true;
                        Log.d("treuwala", String.valueOf(checkDimensions));

                    } else {
                        checkDimensions = false;
                        runOnUiThread(() -> {
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(EditAdPost.this);
                            alert1.setTitle(settingsMain.getAlertDialogTitle("info"));
                            alert1.setCancelable(false);
                            alert1.setMessage(adPostImageModel.getDim_height_message());
                            alert1.setPositiveButton(settingsMain.getAlertOkText(), (dialog, which) -> {

                                dialog.dismiss();
                            });
                            alert1.show();
                        });


                        Log.d("falsewala", String.valueOf(checkDimensions));

                    }
                }

                File file = new File(imaagesLis.get(i));
                long fileSizeInBytes = file.length();
                Integer fileSizeBytes = Math.round(fileSizeInBytes);
                if (fileSizeBytes > Integer.parseInt(adPostImageModel.getImg_size())) {
                    checkImageSize = false;
                    Log.d("falsewalasize", String.valueOf(checkImageSize));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, adPostImageModel.getImg_message(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    checkImageSize = true;
                    Log.d("truewalasize", String.valueOf(checkImageSize));
                }
                if (checkImageSize && checkDimensions) {
                    File finalFile1 = null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        finalFile1 = new File(imaagesLis.get(i));
                    } else {
                        finalFile1 = adforest_rotateImage(imaagesLis.get(i));
                    }

                    Uri tempUri = SettingsMain.decodeFile(context, finalFile1);

                    parts.add(adforestst_prepareFilePart("file" + i, tempUri));
                    adforest_uploadImages(parts);

                }
                if (imaagesLis.size() - 1 == i) {
                    asyncImageTask.cancel(true);
                }
            }
            return parts;
        }

        @Override
        protected void onPostExecute(List<MultipartBody.Part> result) {
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (successfullyUploadedImagesCount == 0) {
                progress_bar.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
                Gallary.setVisibility(View.VISIBLE);
                Gallary.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
                Gallary.setText("" + 0);
                Gallary.setTextColor(Color.parseColor("#a0a0a0"));
                tv_done.setVisibility(View.VISIBLE);
                tv_done.setTextColor(Color.parseColor("#ff0000"));
                tv_done.setText("");
                btnSelectPix.setEnabled(true);
            } else {
                progress_bar.setVisibility(View.GONE);
                Gallary.setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.GONE);
                Gallary.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_green_24dp, 0, 0, 0);
                Gallary.setText(successfullyUploadedImagesCount + "");
                tv_done.setText(progressModel.getSuccessMessage());
                Toast.makeText(context, progressModel.getSuccessMessage(), Toast.LENGTH_SHORT).show();
                btnSelectPix.setEnabled(true);
                tv_done.setTextColor(Color.parseColor("#20a406"));
            }


        }

        @Override
        protected void onPreExecute() {
            currentSize = myImages.size();
            progress_bar.setVisibility(View.VISIBLE);
            tv_done.setVisibility(View.VISIBLE);
            tv_done.setTextColor(Color.parseColor("#242424"));
            tv_done.setText(progressModel.getTitle());
            Gallary.setVisibility(View.GONE);

            Drawable drawable = getResources().getDrawable(R.drawable.bg_uploading).mutate();
            drawable.setColorFilter(Color.parseColor(SettingsMain.getMainColor()), PorterDuff.Mode.SRC_ATOP);
            loadingLayout.setBackground(drawable);

            tv_uploading.setText(progressModel.getTitle());
            loadingLayout.setVisibility(View.VISIBLE);


        }

    }
}
