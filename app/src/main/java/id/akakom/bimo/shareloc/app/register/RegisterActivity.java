package id.akakom.bimo.shareloc.app.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.App;
import id.akakom.bimo.shareloc.app.login.LoginViewModel;
import id.akakom.bimo.shareloc.data.models.User;
import id.akakom.bimo.shareloc.dialogs.JenisKelaminDialog;
import id.akakom.bimo.shareloc.module.Activity.ShareLocPermissionBackActivity;
import id.akakom.bimo.shareloc.module.Base.BaseViewModelFactory;
import lib.gmsframeworkx.Activity.ActivityGeneral;
import lib.gmsframeworkx.utils.GmsStatic;

public class RegisterActivity extends ShareLocPermissionBackActivity {
    @BindView(R.id.edt_username)
    EditText edt_username;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_alamat)
    EditText edt_alamat;
    @BindView(R.id.edt_telp)
    EditText edt_telp;
    @BindView(R.id.edt_jk)
    EditText edt_jk;
    @BindView(R.id.btn_register)
    Button btn_register;

    RegisterViewModel viewModel;

    ArrayList forms = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSupportActionBarBack("Register");

        viewModel = ViewModelProviders.of(this, new BaseViewModelFactory("register", this)).get(RegisterViewModel.class);

        edt_jk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JenisKelaminDialog(getContext(), new JenisKelaminDialog.OnJenisKelamin() {
                    @Override
                    public void onPilihanJk(@NotNull String jk) {
                        edt_jk.setText(jk);
                    }
                });
            }
        });
        setFormsToValidate();
        initViewModel();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GmsStatic.isFormValid(getContext(), getWindow().getDecorView(), forms)){
                    User user = new User(edt_username.getText().toString(),
                                         edt_password.getText().toString(),
                                         edt_name.getText().toString(),
                                         edt_telp.getText().toString(),
                                         edt_jk.getText().toString(),
                                         edt_alamat.getText().toString(), 0);
                    viewModel.register(user);
                }
            }
        });
    }

    private void initViewModel(){
        viewModel.isSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean o) {
                if(o) finish();
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

    private void setFormsToValidate() {
        forms.add(R.id.edt_username);
        forms.add(R.id.edt_password);
        forms.add(R.id.edt_name);
        forms.add(R.id.edt_telp);
        forms.add(R.id.edt_jk);
        forms.add(R.id.edt_alamat);
    }
}