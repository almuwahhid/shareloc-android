package id.akakom.bimo.shareloc.app.login;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.akakom.bimo.shareloc.data.EndPoints;
import id.akakom.bimo.shareloc.data.models.Shareloc;
import id.akakom.bimo.shareloc.data.models.User;
import id.akakom.bimo.shareloc.module.Base.BaseViewModel;
import id.akakom.bimo.shareloc.module.Base.RequestHelper;
import lib.gmsframeworkx.utils.GmsRequest;
import lib.gmsframeworkx.utils.GmsStatic;

public class LoginViewModel extends BaseViewModel {

    public LoginViewModel(@NotNull Context context) {
        super(context);
    }

    MutableLiveData isUser = new MutableLiveData<User>();

    void login(String username, String password){
        GmsRequest.POST(EndPoints.stringLogin(), getContext(), new GmsRequest.OnPostRequest() {
            @Override
            public void onPreExecuted() {
                isLoading.postValue(true);
            }

            @Override
            public void onSuccess(JSONObject response) {
                isLoading.postValue(false);
                try {
                    if (response.getInt("status") == 200) {
                        User c = getGson().fromJson(response.getString("data"), User.class);
                        isMessage.postValue(response.getString("message"));
                        isUser.postValue(c);
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
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
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
