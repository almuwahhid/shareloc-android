package id.akakom.bimo.shareloc.app.main.profil;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.App;
import id.akakom.bimo.shareloc.app.login.LoginActivity;
import id.akakom.bimo.shareloc.app.riwayat.RiwayatActivity;
import id.akakom.bimo.shareloc.app.riwayatmember.RiwayatMemberActivity;
import id.akakom.bimo.shareloc.data.StaticData;
import id.akakom.bimo.shareloc.data.models.User;
import id.akakom.bimo.shareloc.utils.avatarview.AvatarPlaceholder;
import id.akakom.bimo.shareloc.utils.avatarview.loader.PicassoLoader;
import id.akakom.bimo.shareloc.utils.avatarview.views.AvatarView;

public class ProfilFragment extends Fragment {

    @BindView(R.id.btn_logout)
    LinearLayout btn_logout;
    @BindView(R.id.btn_riwayat_berbagi)
    LinearLayout btn_riwayat_berbagi;
    @BindView(R.id.btn_riwayat_mengikuti)
    LinearLayout btn_riwayat_mengikuti;

    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.avatar)
    AvatarView avatar;

    public ProfilFragment() {

    }

    public static ProfilFragment newInstance() {
        ProfilFragment fragment = new ProfilFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = App.getInstance().getUser();

        PicassoLoader imageLoader = new PicassoLoader();
        AvatarPlaceholder refreshableAvatarPlaceholder = new AvatarPlaceholder(user.getName());
        imageLoader.loadImage(avatar, refreshableAvatarPlaceholder, user.getName());
        tv_name.setText(user.getName());
        tv_phone.setText(user.getPhone_number());

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getInstance().logout();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        btn_riwayat_berbagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RiwayatActivity.class).putExtra("type", StaticData.TYPE_BERBAGI));
            }
        });
        btn_riwayat_mengikuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RiwayatMemberActivity.class).putExtra("type", StaticData.TYPE_MENGIKUTI));
            }
        });
    }
}