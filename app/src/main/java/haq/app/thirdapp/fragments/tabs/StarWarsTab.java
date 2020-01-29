package haq.app.thirdapp.fragments.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import haq.app.thirdapp.R;
import haq.app.thirdapp.activities.HomeActivity;
import haq.app.thirdapp.adapters.PhotoAdapter;
import haq.app.thirdapp.constants.Constants;
import haq.app.thirdapp.databinding.FragmentStarWarsTabBinding;
import haq.app.thirdapp.fragments.ViewPhotoFragment;
import haq.app.thirdapp.models.PhotoDTO;
import haq.app.thirdapp.roomDatabase.MyDatabase;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StarWarsTab extends Fragment implements PhotoAdapter.OnPhotoListner {

    FragmentStarWarsTabBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_star_wars_tab, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPhotos();
    }

    private void getPhotos() {
        MyDatabase.getInstance()
                .wallpaperDAO()
                .getPhotosetByID(Constants.STAR_WARS_PHOTOSET_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<PhotoDTO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onSuccess(List<PhotoDTO> list) {
                        setPhotos(list);
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void setPhotos(List<PhotoDTO> list) {
        PhotoAdapter adapter = new PhotoAdapter(list, getActivity(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN);
        binding.rvStarWars.setLayoutManager(gridLayoutManager);
        binding.rvStarWars.setItemAnimator(new DefaultItemAnimator());
        binding.rvStarWars.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnPhotoClick(PhotoDTO photoDTO) {

        ((HomeActivity) getActivity()).gotoViewPhotoFragment(photoDTO);
    }
}
