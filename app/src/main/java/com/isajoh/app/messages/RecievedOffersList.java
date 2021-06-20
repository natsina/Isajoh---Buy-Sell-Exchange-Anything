package com.isajoh.app.messages;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.isajoh.app.R;
import com.isajoh.app.helper.SendReciveONClickListner;
import com.isajoh.app.messages.adapter.ItemSendRecMesageAdapter;
import com.isajoh.app.modelsList.messageSentRecivModel;
import com.isajoh.app.utills.AnalyticsTrackers;
import com.isajoh.app.utills.Network.RestService;
import com.isajoh.app.utills.SettingsMain;
import com.isajoh.app.utills.UrlController;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecievedOffersList extends Fragment {
    RecyclerView recyclerView;
    SettingsMain settingsMain;

    ArrayList<messageSentRecivModel> listitems = new ArrayList<>();

    int currentPage = 1, nextPage = 1, totalPage = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loading = true, hasNextPage = false;
    ItemSendRecMesageAdapter itemSendRecMesageAdapter;
    ProgressBar progressBar;
    RestService restService;

    String adID;

    public RecievedOffersList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_offers, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            adID = bundle.getString("adId", "0");
        }

        settingsMain = new SettingsMain(getActivity());

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.cardView);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(MyLayoutManager);
        restService = UrlController.createService(RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), getActivity());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = MyLayoutManager.getChildCount();
                    totalItemCount = MyLayoutManager.getItemCount();
                    pastVisiblesItems = MyLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (hasNextPage) {
                                progressBar.setVisibility(View.VISIBLE);
                                adforest_loadMore(nextPage);
                            }
                        }
                    }
                }
            }
        });


        adforest_getAllData();
        return view;
    }

    private void adforest_loadMore(int nextPag) {

        if (SettingsMain.isConnectingToInternet(getActivity())) {

            JsonObject params = new JsonObject();

            params.addProperty("page_number", nextPag);
            params.addProperty("ad_id", adID);

            Log.d("info Send OffersList", params.toString());
            Call<ResponseBody> myCall = restService.postGetRecievedOffersList(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info MoreOffer List", "Responce" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info MoreOffer object", "" + response.getJSONObject("data"));

                                JSONObject jsonObjectPagination = response.getJSONObject("data").getJSONObject("pagination");

                                nextPage = jsonObjectPagination.getInt("next_page");
                                currentPage = jsonObjectPagination.getInt("current_page");
                                totalPage = jsonObjectPagination.getInt("max_num_pages");
                                hasNextPage = jsonObjectPagination.getBoolean("has_next_page");

                                JSONArray jsonArrayMessage = response.getJSONObject("data").getJSONObject("received_offers").getJSONArray("items");
                                for (int i = 0; i < jsonArrayMessage.length(); i++) {

                                    messageSentRecivModel item = new messageSentRecivModel();


                                    item.setId(jsonArrayMessage.getJSONObject(i).getString("ad_id"));
                                    item.setName(jsonArrayMessage.getJSONObject(i).getString("message_author_name"));
                                    item.setTopic(jsonArrayMessage.getJSONObject(i).getString("message_ad_title"));
                                    item.setType("receive");
                                    item.setMessageRead(jsonArrayMessage.getJSONObject(i).getBoolean("message_read_status"));
                                    item.setSender_id(jsonArrayMessage.getJSONObject(i).getString("message_sender_id"));
                                    item.setReceiver_id(jsonArrayMessage.getJSONObject(i).getString("message_receiver_id"));
                                    item.setTumbnail(jsonArrayMessage.getJSONObject(i).getString("message_ad_img"));

                                    listitems.add(item);
                                }
                                loading = true;
                                //recyclerView.setAdapter(itemSendRecMesageAdapter);
                                itemSendRecMesageAdapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();

                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                    SettingsMain.hideDilog();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        SettingsMain.hideDilog();
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        SettingsMain.hideDilog();
                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info MoreOffer List", "NullPointert Exception" + t.getLocalizedMessage());
                        SettingsMain.hideDilog();
                    } else {
                        SettingsMain.hideDilog();
                        Log.d("info MoreOffer List err", String.valueOf(t));
                        Log.d("info MoreOffer List err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            SettingsMain.hideDilog();
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }


    private void adforest_getAllData() {

        if (SettingsMain.isConnectingToInternet(getActivity())) {

            SettingsMain.showDilog(getActivity());

            JsonObject params = new JsonObject();
            params.addProperty("ad_id", adID);

            Log.d("info Send RecievedOffer", params.toString());
            Call<ResponseBody> myCall = restService.postGetRecievedOffersList(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info RecievedOffer List", "Responce" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            getActivity().setTitle(response.getJSONObject("extra").getString("page_title"));
                            if (response.getBoolean("success")) {
                                Log.d("info RecievedList obj", "" + response.getJSONObject("data"));
                                adforest_initializeList(response.getJSONObject("data"));

                                ItemSendRecMesageAdapter itemSendRecMesageAdapter = new ItemSendRecMesageAdapter(getActivity(), listitems);
                                if (listitems.size() > 0 & recyclerView != null) {
                                    recyclerView.setAdapter(itemSendRecMesageAdapter);

                                    itemSendRecMesageAdapter.setOnItemClickListener(new SendReciveONClickListner() {
                                        @Override
                                        public void onItemClick(messageSentRecivModel item) {
                                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                                            intent.putExtra("adId", item.getId());
                                            intent.putExtra("senderId", item.getSender_id());
                                            intent.putExtra("recieverId", item.getReceiver_id());
                                            intent.putExtra("type", item.getType());
                                            startActivity(intent);
                                            getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();

                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                    SettingsMain.hideDilog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        SettingsMain.hideDilog();
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        SettingsMain.hideDilog();
                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info RecievedLis", "NullPointert Exception" + t.getLocalizedMessage());
                        SettingsMain.hideDilog();
                    } else {
                        SettingsMain.hideDilog();
                        Log.d("info RecievedList error", String.valueOf(t));
                        Log.d("info RecievedList error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            SettingsMain.hideDilog();
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }


    public void adforest_initializeList(JSONObject jsonObjectData) {

        try {
            listitems.clear();

            JSONArray jsonArrayMessage = jsonObjectData.getJSONObject("received_offers").getJSONArray("items");
            for (int i = 0; i < jsonArrayMessage.length(); i++) {

                messageSentRecivModel item = new messageSentRecivModel();

                item.setId(jsonArrayMessage.getJSONObject(i).getString("ad_id"));
                item.setName(jsonArrayMessage.getJSONObject(i).getString("message_author_name"));
                item.setTopic(jsonArrayMessage.getJSONObject(i).getString("message_ad_title"));

                item.setType("receive");
                item.setMessageRead(jsonArrayMessage.getJSONObject(i).getBoolean("message_read_status"));

                item.setSender_id(jsonArrayMessage.getJSONObject(i).getString("message_sender_id"));
                item.setReceiver_id(jsonArrayMessage.getJSONObject(i).getString("message_receiver_id"));
                item.setTumbnail(jsonArrayMessage.getJSONObject(i).getString("message_ad_img"));

                listitems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        try {
            if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals(""))
                AnalyticsTrackers.getInstance().trackScreenView("Recieved Offers List");
            super.onResume();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
}
