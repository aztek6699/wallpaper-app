package haq.app.thirdapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import haq.app.thirdapp.R;
import haq.app.thirdapp.constants.Constants;
import haq.app.thirdapp.databinding.FragmentHomeBinding;
import haq.app.thirdapp.fragments.tabs.AnimeTab;
import haq.app.thirdapp.fragments.tabs.Art1Tab;
import haq.app.thirdapp.fragments.tabs.ArtTab;
import haq.app.thirdapp.fragments.tabs.StarWarsTab;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Star Wars"));
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Art"));
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Art1"));
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Anime"));

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.addFragment(new StarWarsTab(), "Star Wars");
        viewPagerAdapter.addFragment(new ArtTab(), "Art");
        viewPagerAdapter.addFragment(new Art1Tab(), "Art1");
        viewPagerAdapter.addFragment(new AnimeTab(), "Anime");

        binding.viewpager.setAdapter(viewPagerAdapter);
        binding.tablayout.setupWithViewPager(binding.viewpager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
