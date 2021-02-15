package id.akakom.bimo.shareloc.app.register;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.akakom.bimo.shareloc.data.EndPoints;
import id.akakom.bimo.shareloc.data.models.User;
import id.akakom.bimo.shareloc.module.Base.BaseViewModel;
import id.akakom.bimo.shareloc.module.Base.RequestHelper;
import lib.gmsframeworkx.utils.GmsRequest;

public class RegisterViewModel extends BaseViewModel {

    MutableLiveData isSuccess = new MutableLiveData<Boolean>();

    public RegisterViewModel(@NotNull Context context) {
        super(context);
    }

    void register(User user){
        GmsRequest.POST(EndPoints.stringRegister(), getContext(), new GmsRequest.OnPostRequest() {
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
                        isSuccess.postValue(true);
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
                param.put("data", getGson().toJson(user));
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
