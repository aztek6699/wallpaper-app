package haq.app.thirdapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import haq.app.thirdapp.R;
import haq.app.thirdapp.databinding.ItemPhotoBinding;
import haq.app.thirdapp.models.PhotoDTO;
import haq.app.thirdapp.utils.GlideApp;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private List<PhotoDTO> mPhotoDTOList;
    private LayoutInflater mInflater;
    private Context mContext;
    private ItemPhotoBinding binding;
    private OnPhotoListner mOnPhotoListener;

    public PhotoAdapter(List<PhotoDTO> photoDTOList, Context context, OnPhotoListner onPhotoListener) {
        this.mPhotoDTOList = photoDTOList;
        this.mContext = context;
        this.mOnPhotoListener = onPhotoListener;
    }

    @Override
    public PhotoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(mInflater, R.layout.item_photo, parent, false);
        return new MyViewHolder(binding, mOnPhotoListener);
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.MyViewHolder holder, int position) {
        PhotoDTO photoDTO = mPhotoDTOList.get(position);

        GlideApp.with(mContext)
                .load(photoDTO.getPhotoURL())
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(binding.image);
    }

    public void addItems(ArrayList<PhotoDTO> wallpapersList) {
        this.mPhotoDTOList.addAll(wallpapersList);
        notifyDataSetChanged();
    }

    public void add(PhotoDTO photoDTO) {
        this.mPhotoDTOList.add(photoDTO);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPhotoDTOList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemPhotoBinding binding;
        OnPhotoListner mOnPhotoListener;

        private MyViewHolder(ItemPhotoBinding binding, OnPhotoListner onPhotoListner) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);
            this.binding = binding;
            this.mOnPhotoListener = onPhotoListner;
        }

        @Override
        public void onClick(View v) {
            Log.d("interface", "photo adapter on click");
            mOnPhotoListener.OnPhotoClick(mPhotoDTOList.get(getAdapterPosition()));
        }
    }

    public interface OnPhotoListner {
        void OnPhotoClick(PhotoDTO photoDTO);
    }
}

