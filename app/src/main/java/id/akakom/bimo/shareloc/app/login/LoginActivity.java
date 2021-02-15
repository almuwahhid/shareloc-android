package id.akakom.bimo.shareloc.app.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.App;
import id.akakom.bimo.shareloc.app.main.MainActivity;
import id.akakom.bimo.shareloc.app.register.RegisterActivity;
import id.akakom.bimo.shareloc.data.Preferences;
import id.akakom.bimo.shareloc.data.models.User;
import id.akakom.bimo.shareloc.module.Activity.ShareLocPermissionActivity;
import id.akakom.bimo.shareloc.module.Activity.ShareLocPermissionBackActivity;
import id.akakom.bimo.shareloc.module.Base.BaseViewModelFactory;
import lib.gmsframeworkx.Activity.ActivityGeneral;
import lib.gmsframeworkx.utils.GmsStatic;

public class LoginActivity extends ShareLocPermissionActivity {
    @BindView(R.id.edt_username)
    EditText edt_username;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.tv_daftar)
    TextView tv_daftar;

    LoginViewModel viewModel;

    ArrayList forms = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
//        setSupportActionBar("Login");

        if(App.getInstance().isLogin()){
            finish();
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        viewModel = ViewModelProviders.of(this, new BaseViewModelFactory("login", this)).get(LoginViewModel.class);
        setFormsToValidate();
        initViewModel();

        tv_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GmsStatic.isFormValid(getContext(), getWindow().getDecorView(), forms)){
                    viewModel.login(edt_username.getText().toString(), edt_password.getText().toString());
                }
            }
        });
    }

    private void setFormsToValidate() {
        forms.add(R.id.edt_username);
        forms.add(R.id.edt_password);
    }

    private void initViewModel(){
        viewModel.isUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User o) {
                App.getInstance().setUser(o);
                finish();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        viewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean o) {
                if(o){
                    GmsStatic.showLoadingDialog(getContext(), R.drawable.ic_logo);
                } else {
                    GmsStatic.hideLoadingDialog(getContext());
                }
            }
        });

        viewModel.isError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String o) {
                GmsStatic.ToastShort(getContext(), o);
            }
        });

        viewModel.isMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String o) {
                GmsStatic.ToastShort(getContext(), o);
            }
        });
    }
}