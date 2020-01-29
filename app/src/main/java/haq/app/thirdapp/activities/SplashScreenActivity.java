package haq.app.thirdapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import haq.app.thirdapp.R;
import haq.app.thirdapp.apiInterfaces.FlickrApiInterface;
import haq.app.thirdapp.constants.Constants;
import haq.app.thirdapp.models.PhotoDTO;
import haq.app.thirdapp.models.PhotosetDTO;
import haq.app.thirdapp.retrofitClient.RetrofitClient;
import haq.app.thirdapp.roomDatabase.MyDatabase;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        MyDatabase.makeInstance(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }


}
