package id.akakom.bimo.shareloc.module.Base;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import id.akakom.bimo.shareloc.app.detailriwayat.DetailRiwayatViewModel;
import id.akakom.bimo.shareloc.app.login.LoginViewModel;
import id.akakom.bimo.shareloc.app.main.home.HomeViewModel;
import id.akakom.bimo.shareloc.app.register.RegisterViewModel;
import id.akakom.bimo.shareloc.app.riwayat.RiwayatViewModel;
import id.akakom.bimo.shareloc.app.riwayatmember.RiwayatMemberViewModel;

public class BaseViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    private String type;


    public BaseViewModelFactory(String type, Context context) {
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        switch (type){
            case "login" :
                return (T) new LoginViewModel(context);
            case "register" :
                return (T) new RegisterViewModel(context);
            case "riwayat" :
                return (T) new RiwayatViewModel(context);
            case "riwayatmember" :
                return (T) new RiwayatMemberViewModel(context);
            case "detailriwayat" :
                return (T) new DetailRiwayatViewModel(context);
            case "home" :
                return (T) new HomeViewModel(context);
        }
        return (T) new LoginViewModel(context);
    }

    //    @Override
//    public <T extends ViewModel> T create(Class<T> modelClass) {
//        return (T) new BaseViewModelFactory(mApplication, context);
//    }
}
