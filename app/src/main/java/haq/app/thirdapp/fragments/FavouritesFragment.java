package haq.app.thirdapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import haq.app.thirdapp.R;
import haq.app.thirdapp.activities.HomeActivity;
import haq.app.thirdapp.adapters.PhotoAdapter;
import haq.app.thirdapp.constants.Constants;
import haq.app.thirdapp.databinding.FragmentFavouritesBinding;
import haq.app.thirdapp.interfaces.FavouriteChangedInterface;
import haq.app.thirdapp.models.PhotoDTO;
import haq.app.thirdapp.roomDatabase.MyDatabase;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavouritesFragment extends Fragment implements PhotoAdapter.OnPhotoListner {

    FragmentFavouritesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getPhotos() {
        MyDatabase.getInstance()
                .wallpaperDAO()
                .getFavourites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<PhotoDTO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<PhotoDTO> list) {
                        Log.d("aaaa", "onResume: " + list.size());

                        if (!list.isEmpty()) {
                            binding.tvFavourites.setVisibility(View.INVISIBLE);
                            setPhotos(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPhotos();
    }

    private void setPhotos(List<PhotoDTO> list) {
        PhotoAdapter adapter = new PhotoAdapter(list, getActivity(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN);
        binding.rvFavourites.setLayoutManager(gridLayoutManager);
        binding.rvFavourites.setItemAnimator(new DefaultItemAnimator());
        binding.rvFavourites.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnPhotoClick(PhotoDTO photoDTO) {

        ((HomeActivity) getActivity()).gotoViewPhotoFragment(photoDTO);
    }
}
