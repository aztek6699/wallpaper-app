package haq.app.thirdapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import haq.app.thirdapp.R;
import haq.app.thirdapp.apiInterfaces.FlickrApiInterface;
import haq.app.thirdapp.constants.Constants;
import haq.app.thirdapp.databinding.ActivityHomeBinding;
import haq.app.thirdapp.fragments.AboutFragment;
import haq.app.thirdapp.fragments.FavouritesFragment;
import haq.app.thirdapp.fragments.HomeFragment;
import haq.app.thirdapp.fragments.ViewPhotoFragment;
import haq.app.thirdapp.interfaces.FavouriteChangedInterface;
import haq.app.thirdapp.models.PhotoDTO;
import haq.app.thirdapp.models.PhotosetDTO;
import haq.app.thirdapp.retrofitClient.RetrofitClient;
import haq.app.thirdapp.roomDatabase.MyDatabase;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    Menu mOptionsMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        getSupportActionBar().hide();

        MyDatabase.makeInstance(this);
        checkInternetConnection();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkDatabase(String photosetID) {

        MyDatabase.getInstance().wallpaperDAO().getPhotosetByID(photosetID)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<PhotoDTO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<PhotoDTO> photoDTOS) {
                        if (photoDTOS.isEmpty()) {
                            apiCall(photosetID);
                        } else if(photosetID.equals(Constants.STAR_WARS_PHOTOSET_ID)){
                            binding.pbLoading.setVisibility(View.GONE);
                            loadFragment(new HomeFragment());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void apiCall(String photosetID) {

        FlickrApiInterface apiService = RetrofitClient.getInstance().create(FlickrApiInterface.class);

        apiService.getPhotoset(Constants.METHOD,
                Constants.API_KEY,
                photosetID,
                Constants.USER_ID,
                Constants.EXTRAS,
                Constants.FORMAT,
                Constants.JSON_CALLBACK)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<PhotosetDTO>() {
                    @Override
                    public void onSuccess(PhotosetDTO photosetDTO) {

                        for (PhotoDTO i : photosetDTO.getPhotoListDTO().getList()) {
                            i.setPhotosetID(photosetID);
                            MyDatabase.getInstance().wallpaperDAO().insertPhotoDTO(i);
                        }

                        if(photosetID.equals(Constants.STAR_WARS_PHOTOSET_ID)) {
                            //binding.pbLoading.setVisibility(View.GONE);
                            loadFragment(new HomeFragment());
                        }

                        Log.d("room", "saved");

                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("api", "onError: " + e);
                        dispose();
                    }
                });
    }

    private void showNoInternetAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet!")
                .setMessage("Please connect to internet then press OK once done.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checkInternetConnection();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            checkDatabase(Constants.STAR_WARS_PHOTOSET_ID);
            checkDatabase(Constants.ART_PHOTOSET_ID);
            checkDatabase(Constants.ART1_PHOTOSET_ID);
            checkDatabase(Constants.ANIME_PHOTOSET_ID);
            binding.pbLoading.setVisibility(View.GONE);
        } else {
            showNoInternetAlertDialog();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                loadFragment(new HomeFragment());
                return true;
            case R.id.navigation_favourites:
                loadFragment(new FavouritesFragment());
                return true;
            case R.id.navigation_about:
                loadFragment(new AboutFragment());
                return true;
        }

        return false;
    };

    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    public void gotoViewPhotoFragment(PhotoDTO photoDTO) {

        getSupportActionBar().show();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = new Bundle();

        bundle.putParcelable(Constants.PHOTO_DTO_ID_KEY, photoDTO);

        ViewPhotoFragment fragment = new ViewPhotoFragment();

        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("")
                .commit();
    }
}
