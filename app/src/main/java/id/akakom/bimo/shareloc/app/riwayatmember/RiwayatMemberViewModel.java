package id.akakom.bimo.shareloc.app.riwayatmember;

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
import id.akakom.bimo.shareloc.data.models.Shareloc;
import id.akakom.bimo.shareloc.data.models.SharelocMember;
import id.akakom.bimo.shareloc.module.Base.BaseViewModel;
import id.akakom.bimo.shareloc.module.Base.RequestHelper;
import lib.gmsframeworkx.utils.GmsRequest;

public class RiwayatMemberViewModel extends BaseViewModel {

    MutableLiveData isSuccess = new MutableLiveData<ArrayList<SharelocMember>>();

    public RiwayatMemberViewModel(@NotNull Context context) {
        super(context);
    }

    void get(){
        GmsRequest.POST(EndPoints.stringAllRiwayatMember(), getContext(), new GmsRequest.OnPostRequest() {
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
                        isSuccess.postValue(sharelocs);
                    } else {
                        isSuccess.postValue(new ArrayList<SharelocMember>());
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
                param.put("id_user", ""+App.getInstance().getUser().getId_user());
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
