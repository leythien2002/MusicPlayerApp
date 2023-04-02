package com.example.project2_mediaplayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project2_mediaplayer.fragments.AuthorFragment;
import com.example.project2_mediaplayer.fragments.ListmusicFragment;
import com.example.project2_mediaplayer.fragments.SettingFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new ListmusicFragment();
            case 1:
                return new AuthorFragment();
            case 2:
                return new SettingFragment();
            default:
                return new ListmusicFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
