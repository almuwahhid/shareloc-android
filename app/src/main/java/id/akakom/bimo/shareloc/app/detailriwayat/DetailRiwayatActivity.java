package id.akakom.bimo.shareloc.app.detailriwayat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.App;
import id.akakom.bimo.shareloc.app.detailriwayat.adapter.DetailRiwayatAdapter;
import id.akakom.bimo.shareloc.app.main.home.HomeFragment;
import id.akakom.bimo.shareloc.app.riwayatmember.RiwayatMemberViewModel;
import id.akakom.bimo.shareloc.app.riwayatmember.adapter.RiwayatMemberAdapter;
import id.akakom.bimo.shareloc.data.models.Shareloc;
import id.akakom.bimo.shareloc.data.models.SharelocData;
import id.akakom.bimo.shareloc.data.models.SharelocMember;
import id.akakom.bimo.shareloc.module.Activity.ShareLocPermissionActivity;
import id.akakom.bimo.shareloc.module.Base.BaseViewModelFactory;
import lib.gmsframeworkx.Activity.Interfaces.PermissionResultInterface;
import lib.gmsframeworkx.utils.GmsStatic;

import static id.akakom.bimo.shareloc.data.Preferences.SHARED_STATUS;
import static id.akakom.bimo.shareloc.data.StaticData.SHARED_RUNNING;
import static id.akakom.bimo.shareloc.data.StaticData.SHARED_STOPPED;
import static id.akakom.bimo.shareloc.utils.SharelocUtils.bitmapDescriptorFromVector;

public class DetailRiwayatActivity extends ShareLocPermissionActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap googleMap;

    SupportMapFragment map;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;

    private static final long UPDATE_INTERVAL = 2000, FASTEST_INTERVAL = 2000; // = 5 seconds

    LatLng myltlng;

    Gson gson = new Gson();

    DetailRiwayatViewModel viewModel;
    DetailRiwayatAdapter adapter;

    String id_shareloc = "";

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat);
        setSupportActionBarBack("Detail");
        ButterKnife.bind(this);

        if(getIntent().hasExtra("data")){
            if(getIntent().getIntExtra("type", 0) == 0){
                Shareloc shareloc = gson.fromJson(getIntent().getStringExtra("data"), Shareloc.class);
                id_shareloc = ""+shareloc.getId_shareloc();
            } else {
                SharelocMember member = gson.fromJson(getIntent().getStringExtra("data"), SharelocMember.class);
                id_shareloc = ""+member.getId_shareloc();
            }
        } else {
            finish();
        }

            askCompactPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionResultInterface() {
                @Override
                public void permissionGranted() {
                    map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    map.getMapAsync(DetailRiwayatActivity.this);
                }

                @Override
                public void permissionDenied() {
                    GmsStatic.ToastShort(getContext(), "Anda harus memberikan akses lokasi terlebih dahulu");
                    finish();
                }
            });

            adapter = new DetailRiwayatAdapter(getContext(), new DetailRiwayatAdapter.OnClick() {
                @Override
                public void onClicked(SharelocMember members) {

                }
            });
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(adapter);
            viewModel = ViewModelProviders.of(this, new BaseViewModelFactory("detailriwayat", this)).get(DetailRiwayatViewModel.class);
            observeViewModel();

            viewModel.getMember(id_shareloc);
            viewModel.getRoute(id_shareloc);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            GmsStatic.ToastShort(getContext(), "Anda perlu memberi izin fitur GPS terlebih dahulu");
            return;
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myltlng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        askCompactPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionResultInterface() {
            @Override
            public void permissionGranted() {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                startGps();
            }

            @Override
            public void permissionDenied() {
                GmsStatic.ToastShort(getContext(), "Anda harus memberikan akses lokasi terlebih dahulu");
                finish();
            }
        });
    }

    private void startGps(){
        googleApiClient = new GoogleApiClient.Builder(getContext()).addApi(LocationServices.API).
                addConnectionCallbacks(DetailRiwayatActivity.this).
                addOnConnectionFailedListener(DetailRiwayatActivity.this).build();
        googleApiClient.connect();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void onFocusMap(double lat, double lng, int x){
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        if(x != 0){
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(x));
        }
    }

    private void observeViewModel(){
        viewModel.isMember.observe(this, new Observer<ArrayList<SharelocMember>>() {
            @Override
            public void onChanged(ArrayList<SharelocMember> o) {
                if(o.size() == 0) tv_nodata.setVisibility(View.VISIBLE);
                else {
                    adapter.addList(o);
                }
            }
        });

        viewModel.isLocations.observe(this, new Observer<ArrayList<SharelocData>>() {
            @Override
            public void onChanged(ArrayList<SharelocData> o) {
                if(o.size() > 0) {
                    onFocusMap(Double.valueOf(o.get(0).getLat()), Double.valueOf(o.get(0).getLng()), 12);
                    drawPath(o);
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

    public void drawPath(ArrayList<SharelocData> o) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(o.get(0).getLat()), Double.valueOf(o.get(0).getLng()))).icon(
                bitmapDescriptorFromVector(getContext(), R.drawable.marker_user_start)));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(o.get(o.size()-1).getLat()), Double.valueOf(o.get(o.size()-1).getLng()))).icon(
                bitmapDescriptorFromVector(getContext(), R.drawable.marker_user_end)));

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int z = 0; z < o.size(); z++) {
            LatLng point = new LatLng(Double.valueOf(o.get(z).getLat()), Double.valueOf(o.get(z).getLng()));
            options.add(point);
        }
        googleMap.addPolyline(options);

    }
}