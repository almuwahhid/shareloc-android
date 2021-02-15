package id.akakom.bimo.shareloc.app.detailriwayat;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.akakom.bimo.shareloc.app.App;
import id.akakom.bimo.shareloc.data.EndPoints;
import id.akakom.bimo.shareloc.data.models.SharelocData;
import id.akakom.bimo.shareloc.data.models.SharelocMember;
import id.akakom.bimo.shareloc.module.Base.BaseViewModel;
import lib.gmsframeworkx.utils.GmsRequest;

public class DetailRiwayatViewModel extends BaseViewModel {

    MutableLiveData isMember = new MutableLiveData<ArrayList<SharelocMember>>();
    MutableLiveData isLocations = new MutableLiveData<ArrayList<SharelocData>>();

    public DetailRiwayatViewModel(@NotNull Context context) {
        super(context);
    }

    void getMember(String id_shareloc){
        GmsRequest.POST(EndPoints.stringMemberShareloc(), getContext(), new GmsRequest.OnPostRequest() {
            @Override
            public void onPreExecuted() {
                isLoading.postValue(true);
            }

            @Override
            public void onSuccess(JSONObject response) {
                isLoading.postValue(false);
                try {
                    isMessage.postValue(response.getString("message"));
                    if (response.getInt("status") == 200) {
                        Type listType = new TypeToken<ArrayList<SharelocMember>>() {}.getType();
                        ArrayList<SharelocMember> sharelocs = new Gson().fromJson(response.getString("data"), listType);
                        isMember.postValue(sharelocs);
                    } else {
                        isMember.postValue(new ArrayList<SharelocMember>());
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
                param.put("id_shareloc", ""+ id_shareloc);
                return param;
            }

            @Override
            public Map<String, String> requestHeaders() {
                HashMap param = new HashMap<String, String>();
                return param;
            }
        });
    }

    void getRoute(String id_shareloc){
        GmsRequest.POST(EndPoints.stringLocationsShareloc(), getContext(), new GmsRequest.OnPostRequest() {
            @Override
            public void onPreExecuted() {

            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getInt("status") == 200) {
                        Type listType = new TypeToken<ArrayList<SharelocData>>() {}.getType();
                        ArrayList<SharelocData> sharelocs = new Gson().fromJson(response.getString("data"), listType);
                        isLocations.postValue(sharelocs);
                    } else {
                        isLocations.postValue(new ArrayList<SharelocMember>());
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
                param.put("id_shareloc", ""+ id_shareloc);
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
