package com.example.warmi.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList;
    private final List<String> fragmentTitleList;

    public ViewPagerAdapter(@NonNull Fragment fragment, List<Fragment> fragmentList, List<String> fragmentTitleList) {
        super(fragment);
        this.fragmentList = fragmentList;
        this.fragmentTitleList = fragmentTitleList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    @Nullable
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
