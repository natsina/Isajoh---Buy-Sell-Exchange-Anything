package com.isajoh.app.profile.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.skeletonlayout.Skeleton;
import com.google.gson.JsonObject;

import com.isajoh.app.R;
import com.isajoh.app.home.HomeActivity;
import com.isajoh.app.modelsList.myAdsModel;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemMyAdsAdapter extends RecyclerView.Adapter<ItemMyAdsAdapter.MyViewHolder> {
    com.isajoh.app.utills.Network.RestService restService;
    private ArrayList<myAdsModel> list;
    private com.isajoh.app.helper.MyAdsOnclicklinstener onItemClickListener;
    private Context mContext;
    private com.isajoh.app.utills.SettingsMain settingsMain;
    private ArrayList<String> temp;

    public ItemMyAdsAdapter(Context context, ArrayList<myAdsModel> Data) {
        this.list = Data;
        this.mContext = context;
        this.settingsMain = new com.isajoh.app.utills.SettingsMain(mContext);
        restService = com.isajoh.app.utills.UrlController.createService(com.isajoh.app.utills.Network.RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemof_user_adds, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final com.isajoh.app.modelsList.myAdsModel feedItem = list.get(position);

        holder.name.setText(list.get(position).getName());

        if (!TextUtils.isEmpty(feedItem.getImage())) {
              Picasso.get().load(feedItem.getImage())
                    .resize(270, 270).centerCrop()
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.mainImage);
        }
        holder.priceTV.setText(list.get(position).getPrice());

        holder.spinner.setTag(list.get(position).getAdId());
        holder.linearLayout.setTag(list.get(position).getAdId());

        if (list.get(position).getAdType().equals("myads")) {
            holder.spinner.setVisibility(View.VISIBLE);

            holder.delAd.setText(list.get(position).getDelAd());
            holder.editAd.setText(list.get(position).getEditAd());

            holder.layoutDellAd.setTag(list.get(position).getAdId());
            holder.layoutEditAd.setTag(list.get(position).getAdId());

            temp = list.get(position).getSpinerValue();
            holder.statusTV.setText(list.get(position).getAdStatusValue());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, list.get(position).getSpinerData());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);
            holder.spinner.setSelection(list.get(position).getSpinerData().indexOf(list.get(position).getAdStatusValue()));

            if (list.get(position).getAdStatus().equals("expired")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#d9534f"));
            } else if (list.get(position).getAdStatus().equals("active")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#4caf50"));
            } else if (list.get(position).getAdStatus().equals("sold")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#3498db"));
            }

            View.OnClickListener listener2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.delViewOnClick(v, position);
                }
            };
            View.OnClickListener listener3 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.editViewOnClick(v, position);
                }
            };

            holder.removeFavBtn.setVisibility(View.GONE);
            holder.layoutEditAd.setOnClickListener(listener3);
            holder.layoutDellAd.setOnClickListener(listener2);

        }
        if (list.get(position).getAdType().equals("most_visited")) {
            holder.textViewSeen.setVisibility(View.VISIBLE);
            holder.textViewSeen.setText(list.get(position).getViews());
            holder.statusTV.setVisibility(View.GONE);
            holder.buttonLayout.setVisibility(View.GONE);
            holder.spinner.setVisibility(View.GONE);
            holder.relativeLayoutSpiner.setVisibility(View.GONE);
            holder.removeFavBtn.setVisibility(View.GONE);
        }
        if (list.get(position).getAdType().equals("draft")) {
            holder.spinner.setVisibility(View.VISIBLE);

            holder.delAd.setText(list.get(position).getDelAd());
            holder.editAd.setText(list.get(position).getEditAd());

            holder.layoutDellAd.setTag(list.get(position).getAdId());
            holder.layoutEditAd.setTag(list.get(position).getAdId());

            temp = list.get(position).getSpinerValue();
            holder.statusTV.setText(list.get(position).getAdStatusValue());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, list.get(position).getSpinerData());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);
            holder.spinner.setSelection(list.get(position).getSpinerData().indexOf(list.get(position).getAdStatusValue()));

            if (list.get(position).getAdStatus().equals("expired")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#d9534f"));
            } else if (list.get(position).getAdStatus().equals("active")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#4caf50"));
            } else if (list.get(position).getAdStatus().equals("sold")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#3498db"));
            }

            View.OnClickListener listener2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.delViewOnClick(v, position);
                }
            };
            View.OnClickListener listener3 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.editViewOnClick(v, position);
                }
            };

            holder.removeFavBtn.setVisibility(View.GONE);
            holder.layoutEditAd.setOnClickListener(listener3);
            holder.layoutDellAd.setOnClickListener(listener2);

        }
        if (list.get(position).getAdType().equals("rejected")) {
            holder.spinner.setVisibility(View.VISIBLE);
            holder.delAd.setText(list.get(position).getDelAd());
//            holder.editAd.setText(list.get(position).getEditAd());
            holder.EDitRejected.setText(list.get(position).getEditAd());
//            holder.EDitRejected.setTag(list.get(position).getEditAd());

            holder.layoutDellAd.setTag(list.get(position).getAdId());
//            holder.layoutEditAd.setTag(list.get(position).getAdId());
            holder.EDitRejected.setTag(list.get(position).getAdId());

            temp = list.get(position).getSpinerValue();
            holder.statusTV.setText(list.get(position).getAdStatusValue());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, list.get(position).getSpinerData());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);
            holder.spinner.setSelection(list.get(position).getSpinerData().indexOf(list.get(position).getAdStatusValue()));

            if (list.get(position).getAdStatus().equals("expired")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#d9534f"));
            } else if (list.get(position).getAdStatus().equals("active")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#4caf50"));
            } else if (list.get(position).getAdStatus().equals("sold")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#3498db"));
            }

            View.OnClickListener listener3 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.editViewOnClick(v, position);
                }
            };
            holder.EDitRejected.setVisibility(View.VISIBLE);
            holder.EDitRejected.setOnClickListener(listener3);
            holder.statusTV.setVisibility(View.GONE);
//            holder.layoutEditAd.setGravity(Gravity.START);
            holder.layoutEditAd.setVisibility(View.GONE);
            holder.removeFavBtn.setVisibility(View.GONE);
            holder.layoutDellAd.setVisibility(View.GONE);
            holder.EditView.setVisibility(View.GONE);
//            holder.layoutEditAd.setOnClickListener(listener3);
            holder.spinner.setVisibility(View.GONE);
        } else if (list.get(position).getAdType().equals("featured")) {

            holder.statusTV.setText(list.get(position).getAdTypeText());
            holder.statusTV.setBackgroundColor(Color.parseColor("#E52D27"));

            holder.relativeLayoutSpiner.setVisibility(View.GONE);

            holder.buttonLayout.setVisibility(View.GONE);
            holder.spinner.setVisibility(View.GONE);
            holder.removeFavBtn.setVisibility(View.GONE);

        } else if (list.get(position).getAdType().equals("favourite")) {

            holder.removeFavBtn.setTag(list.get(position).getAdId());
            holder.statusTV.setText(list.get(position).getAdStatusValue());

            holder.relativeLayoutSpiner.setVisibility(View.GONE);

            if (list.get(position).getAdStatus().equals("expired")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#d9534f"));
            } else if (list.get(position).getAdStatus().equals("active")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#4caf50"));
            } else if (list.get(position).getAdStatus().equals("sold")) {
                holder.statusTV.setBackgroundColor(Color.parseColor("#3498db"));
            }

            View.OnClickListener listener2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.delViewOnClick(v, position);
                }
            };
            holder.removeFavBtn.setOnClickListener(listener2);

            holder.removeFavBtn.setVisibility(View.VISIBLE);

            holder.buttonLayout.setVisibility(View.GONE);
            ;
            holder.spinner.setVisibility(View.GONE);

        } else if (list.get(position).getAdType().equals("inactive")) {
            holder.statusTV.setVisibility(View.GONE);
            holder.buttonLayout.setVisibility(View.GONE);
            holder.spinner.setVisibility(View.GONE);
            holder.relativeLayoutSpiner.setVisibility(View.GONE);
            holder.removeFavBtn.setVisibility(View.GONE);
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(feedItem);
            }
        };


        holder.linearLayout.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(com.isajoh.app.helper.MyAdsOnclicklinstener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    private void updateexpire(String adId, String adStatus) {

        if (com.isajoh.app.utills.SettingsMain.isConnectingToInternet(mContext)) {

            com.isajoh.app.utills.SettingsMain.showDilog(mContext);

            JsonObject params = new JsonObject();
            params.addProperty("ad_id", adId);
            params.addProperty("ad_status", adStatus);

            Log.d("info Send AdChngStatus", params.toString());
            Call<ResponseBody> myCall = restService.postUpdateAdStatus(params, com.isajoh.app.utills.UrlController.AddHeaders(mContext));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info AdStatus Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());

                            if (response.getBoolean("success")) {
                                Log.d("info AdsStatus Change", "" + response.get("message").toString());
                                Toast.makeText(mContext, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                com.isajoh.app.utills.SettingsMain.reload(mContext, "MyAdsExpire");

                            } else {
                                Toast.makeText(mContext, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            com.isajoh.app.utills.SettingsMain.hideDilog();
                        }
                    } catch (JSONException e) {
                        com.isajoh.app.utills.SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        com.isajoh.app.utills.SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                    com.isajoh.app.utills.SettingsMain.hideDilog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    com.isajoh.app.utills.SettingsMain.hideDilog();
                    Log.d("info AdStatus error", String.valueOf(t));
                    Log.d("info AdStatus error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                }
            });
        } else {
            com.isajoh.app.utills.SettingsMain.hideDilog();
            Toast.makeText(mContext, "Internet error", Toast.LENGTH_SHORT).show();
        }

    }

    private void update(String adId, String adStatus) {

        if (com.isajoh.app.utills.SettingsMain.isConnectingToInternet(mContext)) {
            HomeActivity.shimmerFrameLayout.startShimmer();
            HomeActivity.loadingLayout.setVisibility(View.VISIBLE);
            HomeActivity.shimmerFrameLayout.setVisibility(View.VISIBLE);
            JsonObject params = new JsonObject();
            params.addProperty("ad_id", adId);
            params.addProperty("ad_status", adStatus);

            Log.d("info Send AdChngStatus", params.toString());
            Call<ResponseBody> myCall = restService.postUpdateAdStatus(params, com.isajoh.app.utills.UrlController.AddHeaders(mContext));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info AdStatus Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());

                            if (response.getBoolean("success")) {
                                Log.d("info AdsStatus Change", "" + response.get("message").toString());
                                Toast.makeText(mContext, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                com.isajoh.app.utills.SettingsMain.reload(mContext, "MyAds");

                            } else {
                                Toast.makeText(mContext, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                            HomeActivity.shimmerFrameLayout.stopShimmer();
                            HomeActivity.shimmerFrameLayout.setVisibility(View.GONE);
                            HomeActivity.loadingLayout.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        HomeActivity.shimmerFrameLayout.stopShimmer();
                        HomeActivity.shimmerFrameLayout.setVisibility(View.GONE);
                        HomeActivity.loadingLayout.setVisibility(View.GONE);
                        e.printStackTrace();
                    } catch (IOException e) {
                        HomeActivity.shimmerFrameLayout.stopShimmer();
                        HomeActivity.shimmerFrameLayout.setVisibility(View.GONE);
                        HomeActivity.loadingLayout.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                    HomeActivity.shimmerFrameLayout.stopShimmer();
                    HomeActivity.shimmerFrameLayout.setVisibility(View.GONE);
                    HomeActivity.loadingLayout.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    HomeActivity.shimmerFrameLayout.stopShimmer();
                    HomeActivity.shimmerFrameLayout.setVisibility(View.GONE);
                    HomeActivity.loadingLayout.setVisibility(View.GONE);
                    Log.d("info AdStatus error", String.valueOf(t));
                    Log.d("info AdStatus error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                }
            });
        } else {
            HomeActivity.shimmerFrameLayout.stopShimmer();
            HomeActivity.shimmerFrameLayout.setVisibility(View.GONE);
            HomeActivity.loadingLayout.setVisibility(View.GONE);
            Toast.makeText(mContext, "Internet error", Toast.LENGTH_SHORT).show();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, priceTV, editAd, delAd, statusTV, removeFavBtn, EDitRejected, textViewSeen;
        ImageView mainImage;
        RelativeLayout linearLayout, relativeLayoutSpiner, buttonLayout, relativeLayoutView;
        Spinner spinner;
        LinearLayout layoutEditAd, layoutDellAd;
        View EditView;
        boolean spinnerTouched = false;

        MyViewHolder(View v) {
            super(v);

            name = v.findViewById(R.id.text_view_name);
            priceTV = v.findViewById(R.id.prices);
            priceTV.setTextColor(Color.parseColor(settingsMain.getMainColor()));

            statusTV = v.findViewById(R.id.textView4);
            delAd = v.findViewById(R.id.delAdd);
            editAd = v.findViewById(R.id.editAdd);
            spinner = v.findViewById(R.id.spinner);
            mainImage = v.findViewById(R.id.image_view);
            spinner.setVisibility(View.GONE);
            textViewSeen = v.findViewById(R.id.views);

            relativeLayoutSpiner = v.findViewById(R.id.rel1);
            linearLayout = v.findViewById(R.id.linear_layout_card_view);
            EDitRejected = v.findViewById(R.id.textViewEDitwla);
            removeFavBtn = v.findViewById(R.id.textView17);
            EditView = v.findViewById(R.id.editView);
            layoutEditAd = v.findViewById(R.id.layoutEditAd);
            layoutDellAd = v.findViewById(R.id.layoutDellAd);
            buttonLayout = v.findViewById(R.id.buttonLayout);
            relativeLayoutView = v.findViewById(R.id.edit_del_layout);

            spinner.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    System.out.println("Real touch felt.");
                    spinnerTouched = true;
                    return false;
                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                    if (spinnerTouched) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setTitle(settingsMain.getGenericAlertTitle());
                        alert.setCancelable(false);
                        alert.setMessage(settingsMain.getGenericAlertMessage());
                        alert.setPositiveButton(settingsMain.getGenericAlertOkText(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                update(spinner.getTag().toString(), temp.get(i));
//                                updateexpire(spinner.getTag().toString(), temp.get(i));
                                dialog.dismiss();
                            }
                        });
                        alert.setNegativeButton(settingsMain.getGenericAlertCancelText(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alert.show();
                    }
                    spinnerTouched = false;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

}
