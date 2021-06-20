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

import com.faltenreich.skeletonlayout.Skeleton;
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
import com.isajoh.app.utills.Network.RestService;
import com.isajoh.app.utills.SettingsMain;
import com.isajoh.app.utills.UrlController;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecievedOffers extends Fragment {
    RecyclerView recyclerView;
    ArrayList<messageSentRecivModel> listitems = new ArrayList<>();
    SettingsMain settingsMain;
    int currentPage = 1, nextPage = 1, totalPage = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loading = true, hasNextPage = false;
    ItemSendRecMesageAdapter itemSendRecMesageAdapter;
    ProgressBar progressBar;
    RestService restService;
    Skeleton skeleton;

    public RecievedOffers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recieved_offers, container, false);
        settingsMain = new SettingsMain(getActivity());
        skeleton = view.findViewById(R.id.skeletonLayout);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.cardView);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

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

    private void adforest_getAllData() {

        if (SettingsMain.isConnectingToInternet(getActivity())) {

            //SettingsMain.showDilog(getActivity(), "Please Wait...");

            Call<ResponseBody> myCall = restService.getRecievedOffers(UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info Recived Offers ", "Responce" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info Recived Offers", "Responce" + response.getJSONObject("data"));
                                adforest_initializeList(response.getJSONObject("data"));

                                itemSendRecMesageAdapter = new ItemSendRecMesageAdapter(getActivity(), listitems);
                                if (listitems.size() > 0 & recyclerView != null) {
                                    recyclerView.setAdapter(itemSendRecMesageAdapter);

                                    itemSendRecMesageAdapter.setOnItemClickListener(new SendReciveONClickListner() {
                                        @Override
                                        public void onItemClick(messageSentRecivModel item) {
                                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                                            intent.putExtra("adId", item.getId());
                                            intent.putExtra("senderId", "");
                                            startActivity(intent);
                                            getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        skeleton.showOriginal();
//                        SettingsMain.hideDilog();

                    } catch (JSONException e) {
                        skeleton.showOriginal();

//                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        skeleton.showOriginal();

//                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    SettingsMain.hideDilog();
                    skeleton.showOriginal();

                    Log.d("info Recieved offers ", "error" + String.valueOf(t));
                    Log.d("info Recieved offers ", "error" + String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                }
            });
        } else {
            skeleton.showOriginal();

//            SettingsMain.hideDilog();
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    private void adforest_loadMore(int nextPag) {

        if (SettingsMain.isConnectingToInternet(getActivity())) {

            JsonObject params = new JsonObject();
            params.addProperty("page_number", nextPag);

            Log.d("info SendLoad OffersLst", params.toString());
            Call<ResponseBody> myCall = restService.postLoadMoreRecievedOffer(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info LoadOffers List ", "Responce" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info LoadOffer List obj", "" + response.getJSONObject("data"));

                                JSONObject jsonObjectPagination = response.getJSONObject("data").getJSONObject("pagination");

                                nextPage = jsonObjectPagination.getInt("next_page");
                                currentPage = jsonObjectPagination.getInt("current_page");
                                totalPage = jsonObjectPagination.getInt("max_num_pages");
                                hasNextPage = jsonObjectPagination.getBoolean("has_next_page");

                                JSONArray jsonArrayMessage = response.getJSONObject("data").getJSONObject("received_offers").getJSONArray("items");
                                for (int i = 0; i < jsonArrayMessage.length(); i++) {

                                    messageSentRecivModel item = new messageSentRecivModel();

                                    item.setId(jsonArrayMessage.getJSONObject(i).getString("ad_id"));
                                    item.setName(jsonArrayMessage.getJSONObject(i).getString("message_ad_title"));
                                    item.setIsBlock(jsonArrayMessage.getJSONObject(i).getString("is_block"));
                                    item.setMessageRead(jsonArrayMessage.getJSONObject(i).getBoolean("message_read_status"));
                                    //item.setTopic(jsonArrayMessage.getJSONObject(i).getString("message_author_name"));
                                    item.setTumbnail(jsonArrayMessage.getJSONObject(i).getJSONArray("message_ad_img")
                                            .getJSONObject(0).getString("thumb"));

                                    listitems.add(item);
                                }
                                loading = true;
                                //recyclerView.setAdapter(itemSendRecMesageAdapter);
                                itemSendRecMesageAdapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
//                        SettingsMain.hideDilog();
                        skeleton.showOriginal();

                    } catch (JSONException e) {
//                        SettingsMain.hideDilog();
                        skeleton.showOriginal();

                        e.printStackTrace();
                    } catch (IOException e) {
//                        SettingsMain.hideDilog();
                        skeleton.showOriginal();

                        e.printStackTrace();
                    }
//                    SettingsMain.hideDilog();
                    skeleton.showOriginal();
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                        SettingsMain.hideDilog();
                        skeleton.showOriginal();

                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                        SettingsMain.hideDilog();
                        skeleton.showOriginal();

                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info LoadOffers Excptn ", "NullPointert Exception" + t.getLocalizedMessage());
//                        SettingsMain.hideDilog();
                        skeleton.showOriginal();

                    } else {
//                        SettingsMain.hideDilog();
                        skeleton.showOriginal();

                        Log.d("info LoadOffers error", String.valueOf(t));
                        Log.d("info LoadOffers error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
//            SettingsMain.hideDilog();
            skeleton.showOriginal();

            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }


    public void adforest_initializeList(JSONObject jsonObjectData) {

        try {

            listitems.clear();


            JSONObject jsonObjectPagination = jsonObjectData.getJSONObject("pagination");

            nextPage = jsonObjectPagination.getInt("next_page");
            currentPage = jsonObjectPagination.getInt("current_page");
            totalPage = jsonObjectPagination.getInt("max_num_pages");
            hasNextPage = jsonObjectPagination.getBoolean("has_next_page");

            JSONArray jsonArrayMessage = jsonObjectData.getJSONObject("received_offers").getJSONArray("items");
            for (int i = 0; i < jsonArrayMessage.length(); i++) {

                messageSentRecivModel item = new messageSentRecivModel();

                item.setId(jsonArrayMessage.getJSONObject(i).getString("ad_id"));
                item.setName(jsonArrayMessage.getJSONObject(i).getString("message_ad_title"));
                item.setMessageRead(jsonArrayMessage.getJSONObject(i).getBoolean("message_read_status"));
                item.setIsBlock(jsonArrayMessage.getJSONObject(i).getString("is_block"));
                //item.setTopic(jsonArrayMessage.getJSONObject(i).getString("message_author_name"));
                item.setTumbnail(jsonArrayMessage.getJSONObject(i).getJSONArray("message_ad_img")
                        .getJSONObject(0).getString("thumb"));

                listitems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}