package id.akakom.bimo.shareloc.app.main.home;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.App;
import id.akakom.bimo.shareloc.data.EndPoints;
import id.akakom.bimo.shareloc.data.models.FcmData;
import id.akakom.bimo.shareloc.data.models.Shareloc;
import id.akakom.bimo.shareloc.data.models.SharelocData;
import id.akakom.bimo.shareloc.data.models.SharelocMember;
import id.akakom.bimo.shareloc.data.models.SharelocMemberRequest;
import id.akakom.bimo.shareloc.data.models.User;
import id.akakom.bimo.shareloc.module.Base.BaseViewModel;
import id.akakom.bimo.shareloc.module.Base.RequestHelper;
import lib.gmsframeworkx.utils.GmsRequest;

public class HomeViewModel extends BaseViewModel {

    MutableLiveData isShareStarted = new MutableLiveData<Boolean>();
    MutableLiveData isShareLoc = new MutableLiveData<Shareloc>();
    MutableLiveData isLookingAt = new MutableLiveData<Integer>();

    public HomeViewModel(@NotNull Context context) {
        super(context);
    }

    public void broadcastShareloc(int id_shareloc, double lat, double lng){
        SharelocData sharelocData = new SharelocData();
        sharelocData.setId_shareloc(id_shareloc);
        sharelocData.setLat(""+lat);
        sharelocData.setLng(""+lng);

        GmsRequest.POST(EndPoints.stringAddLocation(), getContext(), new GmsRequest.OnPostRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                isLoading.postValue(false);
                new RequestHelper().stringResponse(response, new RequestHelper.OnStringResponse() {
                    @Override
                    public void onSuccess(@NotNull String message) {

                    }

                    @Override
                    public void onFail(@NotNull String message) {

                    }
                });
            }

            @Override
            public void onFailure(String error) {

            }

            @Override
            public Map<String, String> requestParam() {
                HashMap param = new HashMap<String, String>();
                param.put("data", getGson().toJson(sharelocData));
                return param;
            }

            @Override
            public Map<String, String> requestHeaders() {
                HashMap param = new HashMap<String, String>();
                return param;
            }
        });

        GmsRequest.POSTRaw("https://fcm.googleapis.com/fcm/send", getContext(), new GmsRequest.OnPostRawRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String error) {

            }

            @Override
            public String requestParam() {
                FcmData fcmData = new FcmData();
                fcmData.setTo("/topics/"+id_shareloc);
                fcmData.setData(new FcmData.Data(sharelocData, "running"));
                fcmData.setTitle("Shareloc");
                fcmData.setDescription("Shareloc");
                Log.d("data", getGson().toJson(fcmData));
                return getGson().toJson(fcmData);
            }

            @Override
            public Map<String, String> requestHeaders() {
                HashMap params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "key="+getContext().getString(R.string.firebase_key));
                return params;
            }
        });
    }

    public void startShared(){
        Shareloc shareloc = new Shareloc();
        shareloc.setId_user(App.getInstance().getUser().getId_user());
        GmsRequest.POST(EndPoints.stringShare(), getContext(), new GmsRequest.OnPostRequest() {
            @Override
            public void onPreExecuted() {
                isLoading.postValue(true);
            }

            @Override
            public void onSuccess(JSONObject response) {
                isLoading.postValue(false);
                try {
                    if (response.getInt("status") == 200) {
                        Shareloc c = getGson().fromJson(response.getString("data"), Shareloc.class);
                        isShareLoc.postValue(c);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isShareStarted.postValue(true);
                            }
                        }, 500);
                    } else {
                        isMessage.postValue(response.getString("message"));
                    }
                } catch (JSONException e) {
                    isMessage.postValue(e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                isMessage.postValue(error);
            }

            @Override
            public Map<String, String> requestParam() {
                HashMap param = new HashMap<String, String>();
                param.put("data", getGson().toJson(shareloc));
                return param;
            }

            @Override
            public Map<String, String> requestHeaders() {
                HashMap param = new HashMap<String, String>();
                return param;
            }
        });
    }

    public void joinShareLocation(int id_shareloc){
        SharelocMemberRequest sharelocMember = new SharelocMemberRequest();
        sharelocMember.setId_member(App.getInstance().getUser().getId_user());
        sharelocMember.setId_shareloc(id_shareloc);
        GmsRequest.POST(EndPoints.stringAddPartisipan(), getContext(), new GmsRequest.OnPostRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getInt("status") == 200) {
                        Shareloc c = getGson().fromJson(response.getString("data"), Shareloc.class);
                        isShareLoc.postValue(c);
                    } else {
                        isMessage.postValue(response.getString("message"));
                    }
                } catch (JSONException e) {
                    isMessage.postValue(e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(String error) {

                isMessage.postValue(error);
            }

            @Override
            public Map<String, String> requestParam() {
                HashMap param = new HashMap<String, String>();
                param.put("data", getGson().toJson(sharelocMember));
                return param;
            }

            @Override
            public Map<String, String> requestHeaders() {
                HashMap param = new HashMap<String, String>();
                return param;
            }
        });
    }

    public void stopShared(int id_shareloc){
        SharelocMember sharelocMember = new SharelocMember();
        sharelocMember.setId_member(App.getInstance().getUser().getId_user());
        sharelocMember.setId_shareloc(id_shareloc);
        GmsRequest.POSTRaw("https://fcm.googleapis.com/fcm/send", getContext(), new GmsRequest.OnPostRawRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onFailure(String error) {

            }

            @Override
            public String requestParam() {
                FcmData fcmData = new FcmData();
                fcmData.setTo("/topics/"+id_shareloc);
                fcmData.setData(new FcmData.Data(new SharelocData(), "stop"));
                fcmData.setTitle("Shareloc");
                fcmData.setDescription("Shareloc");
                Log.d("data", getGson().toJson(fcmData));
                return getGson().toJson(fcmData);
            }

            @Override
            public Map<String, String> requestHeaders() {
                HashMap params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "key="+getContext().getString(R.string.firebase_key));
                return params;
            }
        });
        GmsRequest.POST(EndPoints.stringStopShare(), getContext(), new GmsRequest.OnPostRequest() {
            @Override
            public void onPreExecuted() {
                isLoading.postValue(true);
            }

            @Override
            public void onSuccess(JSONObject response) {
                isLoading.postValue(false);
                new RequestHelper().stringResponse(response, new RequestHelper.OnStringResponse() {
                    @Override
                    public void onSuccess(@NotNull String message) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isShareStarted.postValue(false);
                            }
                        }, 500);
                        isMessage.postValue(message);
                    }

                    @Override
                    public void onFail(@NotNull String message) {
                        isMessage.postValue(message);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                isMessage.postValue(error);
            }

            @Override
            public Map<String, String> requestParam() {
                HashMap param = new HashMap<String, String>();
                param.put("id_shareloc", ""+id_shareloc);
                return param;
            }

            @Override
            public Map<String, String> requestHeaders() {
                HashMap param = new HashMap<String, String>();
                return param;
            }
        });
    }

    public void getLookedLocation(int id){
        GmsRequest.POST(EndPoints.stringDetailShareloc(), getContext(), new GmsRequest.OnPostRequest() {
            @Override
            public void onPreExecuted() {
                isLoading.postValue(true);
            }

            @Override
            public void onSuccess(JSONObject response) {
                isLoading.postValue(false);
                try {
                    if (response.getInt("status") == 200) {
                        Shareloc shareloc = getGson().fromJson(response.getString("data"), Shareloc.class);
                        if(shareloc.getStatus() == 0){
                            isLookingAt.postValue(0);
                            isMessage.postValue("Link berbagi lokasi telah usang");
                        } else {
                            isLookingAt.postValue(id);
//                            isShareLoc.postValue(shareloc);
                            isMessage.postValue("Mulai memantau lokasi");
                        }
                    } else {
                        isMessage.postValue(response.getString("message"));
                    }
                } catch (JSONException e) {
                    isMessage.postValue(e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                isLoading.postValue(false);
                isMessage.postValue(error);
            }

            @Override
            public Map<String, String> requestParam() {
                HashMap param = new HashMap<String, String>();
                param.put("id_shareloc", ""+id);
                return param;
            }

            @Override
            public Map<String, String> requestHeaders() {
                HashMap param = new HashMap<String, String>();
                return param;
            }
        });
    }


}
