package id.akakom.bimo.shareloc.app.main.home;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.akakom.bimo.shareloc.R;
import id.akakom.bimo.shareloc.app.App;
import id.akakom.bimo.shareloc.data.models.Shareloc;
import id.akakom.bimo.shareloc.data.models.SharelocData;
import id.akakom.bimo.shareloc.module.Base.BaseViewModelFactory;
import lib.gmsframeworkx.Activity.FragmentPermission;
import lib.gmsframeworkx.Activity.Interfaces.PermissionResultInterface;
import lib.gmsframeworkx.utils.GmsStatic;

import static id.akakom.bimo.shareloc.data.Preferences.SHARED_STATUS;
import static id.akakom.bimo.shareloc.data.StaticData.SHARED_LOOKINGAT;
import static id.akakom.bimo.shareloc.data.StaticData.SHARED_RUNNING;
import static id.akakom.bimo.shareloc.data.StaticData.SHARED_STOPPED;
import static id.akakom.bimo.shareloc.utils.SharelocUtils.bitmapDescriptorFromVector;

public class HomeFragment extends FragmentPermission implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap googleMap;

    SupportMapFragment map;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.btn_stop)
    CardView btn_stop;
    @BindView(R.id.tv_stop)
    TextView tv_stop;
    @BindView(R.id.btn_share)
    Button btn_share;
    @BindView(R.id.btn_myloc)
    FloatingActionButton btn_myloc;

    HomeViewModel viewModel;

    private static final long UPDATE_INTERVAL = 2000, FASTEST_INTERVAL = 2000; // = 5 seconds

    LatLng position, myltlng;

    Gson gson = new Gson();

    Marker mCurrLocationMarker;

    IntentFilter filter = new IntentFilter();
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("running")){
                SharelocData sharelocData = gson.fromJson(intent.getStringExtra("data"), SharelocData.class);
                lookingAt(Double.valueOf(sharelocData.getLat()), Double.valueOf(sharelocData.getLng()));
            } else if(intent.getAction().equals("stop")){
                if(App.getInstance().getLocation() != null){
                    FirebaseApp.initializeApp(getContext());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(""+App.getInstance().getLocation().getId_shareloc());
                    GmsStatic.setSPString(getContext(), SHARED_STATUS, SHARED_STOPPED);
                    checkStatus();
                    if(mCurrLocationMarker != null){
                        mCurrLocationMarker.remove();
                    }
                }
            }

        }
    };

    public HomeFragment() {

    }

    void setintentfilter(){
        filter.addAction("running");
        filter.addAction("stop");
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel  = ViewModelProviders.of(this, new BaseViewModelFactory("home", getContext())).get(HomeViewModel.class);
        setintentfilter();

        askCompactPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionResultInterface() {
            @Override
            public void permissionGranted() {
                map = (SupportMapFragment) (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                map.getMapAsync(HomeFragment.this);
            }

            @Override
            public void permissionDenied() {
                GmsStatic.ToastShort(getContext(), "Anda harus memberikan akses lokasi terlebih dahulu");
                getActivity().finish();
            }
        });

        btn_myloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFocusMap(myltlng.latitude, myltlng.longitude, 12);
            }
        });

        observeViewModel();
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
        try {
            if(GmsStatic.getSPString(getContext(), SHARED_STATUS).equals(SHARED_RUNNING)){
                if(mCurrLocationMarker != null){
                    mCurrLocationMarker.remove();
                }
                if(App.getInstance().getLocation() != null){
                    onFocusMap(myltlng.latitude, myltlng.longitude, 12);
                    mCurrLocationMarker = googleMap.addMarker(new MarkerOptions().title("Lokasi Anda").position(myltlng).icon(bitmapDescriptorFromVector(getContext(), R.drawable.marker_user)));
                    viewModel.broadcastShareloc(App.getInstance().getLocation().getId_shareloc(), location.getLatitude(), location.getLongitude());
                }
            } else if(GmsStatic.getSPString(getContext(), SHARED_STATUS).equals(SHARED_STOPPED)){
                if(mCurrLocationMarker != null){
                    mCurrLocationMarker.remove();
                }
            }
        } catch (Exception e){

        }
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
                getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(receiver, filter);
        checkStatus();
    }

    private void checkStatus(){
        if(GmsStatic.getSPString(getContext(), SHARED_STATUS).equals(SHARED_RUNNING)){
            btn_share.setBackground(getResources().getDrawable(R.drawable.button_disable));
            btn_share.setText("Bagikan link sharelokasi");
            btn_share.setOnClickListener(null);
            btn_stop.setVisibility(View.VISIBLE);
            btn_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(App.getInstance().getLocation() != null){
                        viewModel.stopShared(App.getInstance().getLocation().getId_shareloc());
                    }
                    GmsStatic.setSPString(getContext(), SHARED_STATUS, SHARED_STOPPED);
                    checkStatus();
                }
            });
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(App.getInstance().getLocation() != null){
                        String shareBody = "shareloc.id/token/"+App.getInstance().getLocation().getId_shareloc();
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Berbagi Lokasi");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(intent, "Pilih aplikasi untuk berbagi"));
                    }
                }
            });
            tv_stop.setText("Hentikan membagikan lokasi");
        } else if(GmsStatic.getSPString(getContext(), SHARED_STATUS).equals(SHARED_LOOKINGAT)){
            btn_share.setBackground(getResources().getDrawable(R.drawable.button_lookingat));
            btn_share.setText("Menuju Lokasi");
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position != null){
                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", position.latitude, position.longitude);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    } else {
                        GmsStatic.ToastShort(getContext(), "Lokasi belum terdeteksi");
                    }

                }
            });
            btn_stop.setVisibility(View.VISIBLE);
            btn_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(App.getInstance().getLocation() != null){
                        FirebaseApp.initializeApp(getContext());
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(""+App.getInstance().getLocation().getId_shareloc());
                        GmsStatic.setSPString(getContext(), SHARED_STATUS, SHARED_STOPPED);
                        checkStatus();
                        if(mCurrLocationMarker != null){
                            mCurrLocationMarker.remove();
                        }
                    }

                }
            });
            tv_stop.setText("Hentikan memantau lokasi");
        } else {
            btn_share.setBackground(getResources().getDrawable(R.drawable.button_active));
            btn_share.setText("Bagikan Lokasi");
            btn_stop.setVisibility(View.GONE);
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GmsStatic.setSPString(getContext(), SHARED_STATUS, SHARED_RUNNING);
                    viewModel.startShared();
                }
            });
        }
    }

    private void lookingAt(double lat, double lng){
        position  = new LatLng(lat, lng);
        if(mCurrLocationMarker != null){
            mCurrLocationMarker.remove();
        }
        onFocusMap(lat, lng, 12);
        mCurrLocationMarker = googleMap.addMarker(new MarkerOptions().title("Lokasi Anda").position(new LatLng(lat, lng)).icon(bitmapDescriptorFromVector(getContext(), R.drawable.marker_user)));
    }

    public void startLookingAt(int id){
        GmsStatic.setSPString(getContext(), SHARED_STATUS, SHARED_LOOKINGAT);
        viewModel.getLookedLocation(id);
        viewModel.joinShareLocation(id);
    }

    private void startGps(){
        googleApiClient = new GoogleApiClient.Builder(getContext()).addApi(LocationServices.API).
                addConnectionCallbacks(HomeFragment.this).
                addOnConnectionFailedListener(HomeFragment.this).build();
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
        viewModel.isShareLoc.observe(this, new Observer<Shareloc>() {
            @Override
            public void onChanged(Shareloc o) {
                App.getInstance().setLocation(o);
                if(GmsStatic.getSPString(getContext(), SHARED_STATUS).equals(SHARED_LOOKINGAT)){
                    if(App.getInstance().getLocation() != null){
                        FirebaseApp.initializeApp(getContext());
                        FirebaseMessaging.getInstance().subscribeToTopic(""+App.getInstance().getLocation().getId_shareloc()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                GmsStatic.ToastShort(getContext(), "Listening...");
                            }
                        });
                    }
                } else if(GmsStatic.getSPString(getContext(), SHARED_STATUS).equals(SHARED_RUNNING)){
                    GmsStatic.ToastShort(getContext(), "Sharing...");
                }
                checkStatus();
            }
        });

        viewModel.isShareStarted.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean o) {
                if(o){
                    startLocationUpdates();
                }
                checkStatus();
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

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        if(App.getInstance().getLocation() != null){
            if(GmsStatic.getSPString(getContext(), SHARED_STATUS).equals(SHARED_LOOKINGAT)){
                if(App.getInstance().getLocation() != null) {
                    FirebaseApp.initializeApp(getContext());
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("" + App.getInstance().getLocation().getId_shareloc());
                }
            } else if(GmsStatic.getSPString(getContext(), SHARED_STATUS).equals(SHARED_RUNNING)){
                // TODO : Request stop
            }
        }
        GmsStatic.setSPString(getContext(), SHARED_STATUS, SHARED_STOPPED);
        super.onDestroy();
    }
}