package haq.app.thirdapp.fragments.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
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
import haq.app.thirdapp.databinding.FragmentAnimeTabBinding;
import haq.app.thirdapp.interfaces.FavouriteChangedInterface;
import haq.app.thirdapp.models.PhotoDTO;
import haq.app.thirdapp.roomDatabase.MyDatabase;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnimeTab extends Fragment implements PhotoAdapter.OnPhotoListner {

    FragmentAnimeTabBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_anime_tab, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
                .getPhotosetByID(Constants.ANIME_PHOTOSET_ID)
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
        binding.rvAnime.setLayoutManager(gridLayoutManager);
        binding.rvAnime.setItemAnimator(new DefaultItemAnimator());
        binding.rvAnime.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnPhotoClick(PhotoDTO photoDTO) {


        ((HomeActivity) getActivity()).gotoViewPhotoFragment(photoDTO);
    }
}
