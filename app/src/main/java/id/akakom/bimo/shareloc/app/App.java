package id.akakom.bimo.shareloc.app;

import com.google.gson.Gson;

import id.akakom.bimo.shareloc.data.Preferences;
import id.akakom.bimo.shareloc.data.models.Shareloc;
import id.akakom.bimo.shareloc.data.models.User;
import lib.gmsframeworkx.SuperUser.RequestHandler;
import lib.gmsframeworkx.utils.GmsStatic;

public class App extends RequestHandler {

    Gson gson;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
        instance = this;
    }

    public static App getInstance(){
        return instance;
    }

    public User getUser(){
        if(!GmsStatic.getSPString(this, Preferences.USER_ACCESS).equals("")){
            return gson.fromJson(GmsStatic.getSPString(this, Preferences.USER_ACCESS), User.class);
        }
        return null;
    }

    public void setUser(User user){
        GmsStatic.setSPString(this, Preferences.USER_ACCESS, gson.toJson(user));
    }

    public boolean isLogin(){
        return !GmsStatic.getSPString(this, Preferences.USER_ACCESS).equals("");
    }

    public void logout(){
        GmsStatic.setSPString(this, Preferences.USER_ACCESS, "");
    }

    public Shareloc getLocation(){
        if(!GmsStatic.getSPString(this, Preferences.USER_LOCATION).equals("")){
            return gson.fromJson(GmsStatic.getSPString(this, Preferences.USER_LOCATION), Shareloc.class);
        } else {
            GmsStatic.ToastShort(this, "Berbagi lokasi bermasalah, ulangi aplikasi Anda");
        }
        return null;
    }

    public void setLocation(Shareloc shareloc){
        GmsStatic.setSPString(this, Preferences.USER_LOCATION, gson.toJson(shareloc));
    }
}
