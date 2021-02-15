package id.akakom.bimo.shareloc.app.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.main.home.HomeFragment;
import id.akakom.bimo.shareloc.app.main.profil.ProfilFragment;
import lib.gmsframeworkx.Activity.ActivityGeneral;

public class MainActivity extends ActivityGeneral {

    int active_fragment = 0;
    int after_active_fragment = 0;
    FragmentManager mFragmentManager = getSupportFragmentManager();

    HomeFragment home;
    ProfilFragment profil;

    @BindView(R.id.button_nav)
    BottomNavigationView button_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        home = HomeFragment.newInstance();
        profil = ProfilFragment.newInstance();

        initializeNavFragment(home);

        button_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home :
                        initializeNavFragment(home);
                        break;
                    case R.id.action_profil :
                        initializeNavFragment(profil);
                        break;
                }
                return true;
            }
        });


        if(getIntent().getData() != null){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int id_shareloc = Integer.valueOf(getIntent().getData().getLastPathSegment());
                    home.startLookingAt(id_shareloc);
                }
            }, 500);

        }
    }

    private void initializeNavFragment(Fragment curFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mFragmentManager.findFragmentByTag(curFragment.getClass().getSimpleName()) == null) {
            transaction.add(
                    R.id.page_container,
                    curFragment,
                    curFragment.getClass().getSimpleName()
            );
        }
        Log.d("hi", ""+home.getClass().getSimpleName());
        Fragment tagHome = mFragmentManager.findFragmentByTag(home.getClass().getSimpleName());
        Fragment tagProfil = mFragmentManager.findFragmentByTag(profil.getClass().getSimpleName());
        hideFragment(transaction, tagHome, tagProfil);
        showFragment(curFragment, transaction, tagHome, tagProfil);
        after_active_fragment = active_fragment;
        transaction.commitAllowingStateLoss();
    }

    private void showFragment(
            Fragment curFragment,
            FragmentTransaction transaction,
            Fragment tagHome,
            Fragment tagProfil
    ) {
        if (curFragment.getClass().getSimpleName().equals(home.getClass().getSimpleName())) {
            if (tagHome != null) {
                transaction.show(tagHome);
            }
        }
        if (curFragment.getClass().getSimpleName().equals(profil.getClass().getSimpleName())) {
            if (tagProfil != null) {
                transaction.show(tagProfil);
            }
        }
    }

    private void hideFragment(
            FragmentTransaction transaction,
            Fragment tagHome,
            Fragment tagProfil
    ) {
        if (tagHome != null) {
            transaction.hide(tagHome);
        }
        if (tagProfil != null) {
            transaction.hide(tagProfil);
        }
    }
}