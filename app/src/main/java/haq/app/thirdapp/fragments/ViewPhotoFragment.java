package haq.app.thirdapp.fragments;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import haq.app.thirdapp.R;
import haq.app.thirdapp.constants.Constants;

import haq.app.thirdapp.databinding.ViewPhotoFragmentBinding;
import haq.app.thirdapp.models.PhotoDTO;
import haq.app.thirdapp.roomDatabase.MyDatabase;
import haq.app.thirdapp.utils.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class ViewPhotoFragment extends Fragment implements View.OnClickListener {

    ViewPhotoFragmentBinding binding;
    PhotoDTO mPhotoDTO;
    Menu mOptionsMenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        binding = DataBindingUtil.inflate(inflater, R.layout.view_photo_fragment, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPhotoDTO = getArguments().getParcelable(Constants.PHOTO_DTO_ID_KEY);

        setImage();
        initListeners();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        mOptionsMenu = menu;



        super.onCreateOptionsMenu(menu, inflater);

        if (mPhotoDTO.getFavourite()) {
            menu.getItem(0).setIcon(R.drawable.ic_filled_heart);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_empty_heart);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_bar_heart) {

            updateFavourite(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initListeners() {
        binding.btnDownload.setOnClickListener(this);
        binding.btnSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnDownload:
                handleSaveToStoragePermission();
                break;

            case R.id.btnSet:
                handleSetWallpaperPermission();
                break;

            default:
                break;
        }
    }

    private void updateFavourite(MenuItem item) {
        Handler mHandler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {

                MyDatabase.getInstance().wallpaperDAO().updateFavourite(mPhotoDTO.getPhotoID(), !mPhotoDTO.getFavourite());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        mPhotoDTO.setFavourite(!mPhotoDTO.getFavourite());

                        Log.d("view", "view photo activity: " + mPhotoDTO.getFavourite());

                        if (mPhotoDTO.getFavourite()) {
                            item.setIcon(R.drawable.ic_filled_heart);
                        } else {
                            item.setIcon(R.drawable.ic_empty_heart);
                        }
                    }
                });
            }
        }).start();
    }

    // region get/set Image
    private void setImage() {
        GlideApp.with(getActivity())
                .load(mPhotoDTO.getPhotoURL())
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(binding.imageView);
    }

    private void handleSaveToStoragePermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startSave(mPhotoDTO.getPhotoURL());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void handleSetWallpaperPermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.SET_WALLPAPER)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startSet(mPhotoDTO.getPhotoURL());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void startSave(String url) {
        Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                saveImageToExternalStorage(bitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });
    }

    private void startSet(String url) {
        Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                setWallpaper(bitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });
    }

    private void saveImageToExternalStorage(Bitmap bitmap) {

        try {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_NAME);
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            String fileUri = mydir.getAbsolutePath() + File.separator + mPhotoDTO.getTitle() + "_" + mPhotoDTO.getPhotoID() + ".jpg";
            FileOutputStream outputStream = new FileOutputStream(fileUri);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(mydir); // out is your output file
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);

            Snackbar.make(getView(), "Image saved to storage", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setWallpaper(Bitmap bitmap) {
        WallpaperManager manager = WallpaperManager.getInstance(getActivity().getApplicationContext());
        try {
            manager.setBitmap(bitmap);
        } catch (IOException e) {
        }

        Snackbar.make(getView(), "Image set as wallpaper", Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .show();
    }
    // endregion
}
