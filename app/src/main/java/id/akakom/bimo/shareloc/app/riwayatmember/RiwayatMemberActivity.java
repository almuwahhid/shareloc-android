package id.akakom.bimo.shareloc.app.riwayatmember;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.detailriwayat.DetailRiwayatActivity;
import id.akakom.bimo.shareloc.app.riwayat.adapter.RiwayatAdapter;
import id.akakom.bimo.shareloc.app.riwayatmember.adapter.RiwayatMemberAdapter;
import id.akakom.bimo.shareloc.data.models.Shareloc;
import id.akakom.bimo.shareloc.data.models.SharelocMember;
import id.akakom.bimo.shareloc.module.Activity.ShareLocPermissionActivity;
import id.akakom.bimo.shareloc.module.Base.BaseViewModelFactory;
import lib.gmsframeworkx.utils.GmsStatic;

public class RiwayatMemberActivity extends ShareLocPermissionActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    RiwayatMemberViewModel viewModel;
    RiwayatMemberAdapter adapter;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        setSupportActionBarBack("Riwayat Mengikuti Lokasi");
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this, new BaseViewModelFactory("riwayatmember", this)).get(RiwayatMemberViewModel.class);
        observeViewModel();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                viewModel.get();
            }
        });

        adapter = new RiwayatMemberAdapter(getContext(), new RiwayatMemberAdapter.OnClick() {
            @Override
            public void onClicked(SharelocMember members) {
                startActivity(new Intent(getContext(), DetailRiwayatActivity.class).putExtra("data", gson.toJson(members)).putExtra("type", 1));
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        viewModel.get();
    }

    private void observeViewModel(){
        viewModel.isSuccess.observe(this, new Observer<ArrayList<SharelocMember>>() {
            @Override
            public void onChanged(ArrayList<SharelocMember> o) {
                if(o.size() == 0) tv_nodata.setVisibility(View.VISIBLE);
                else {
                    adapter.addList(o);
                }
            }
        });

        viewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean o) {
                if(o){
                    tv_nodata.setVisibility(View.GONE);
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