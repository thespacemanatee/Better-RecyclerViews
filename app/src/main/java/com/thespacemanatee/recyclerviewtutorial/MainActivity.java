package com.thespacemanatee.recyclerviewtutorial;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.thespacemanatee.recyclerviewtutorial.databinding.ActivityMainBinding;
import com.thespacemanatee.recyclerviewtutorial.screens.BadStatelessExampleFragment;
import com.thespacemanatee.recyclerviewtutorial.screens.BadStatefulExampleFragment;
import com.thespacemanatee.recyclerviewtutorial.screens.EasyModeStatefulExampleFragment;
import com.thespacemanatee.recyclerviewtutorial.screens.GoodStatefulExampleFragment;
import com.thespacemanatee.recyclerviewtutorial.screens.MultipleViewTypesStatefulExampleFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        binding.pager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Bad Stateless");
                    } else if (position == 1) {
                        tab.setText("Bad Stateful");
                    } else if (position == 2) {
                        tab.setText("Good Stateful");
                    } else if (position == 3) {
                        tab.setText("LifeHax");
                    } else if (position == 4) {
                        tab.setText("Multiple ViewTypes");
                    } else {
                        throw new IllegalArgumentException("Fragment does not exist!");
                    }
                }
        ).attach();
    }

    @Override
    public void onBackPressed() {
        if (binding.pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            binding.pager.setCurrentItem(binding.pager.getCurrentItem() - 1);
        }
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private static final int NUM_PAGES = 5;

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new BadStatelessExampleFragment();
            }
            if (position == 1) {
                return new BadStatefulExampleFragment();
            }
            if (position == 2) {
                return new GoodStatefulExampleFragment();
            }
            if (position == 3) {
                return new EasyModeStatefulExampleFragment();
            }
            if (position == 4) {
                return new MultipleViewTypesStatefulExampleFragment();
            }
            throw new IllegalArgumentException("Fragment does not exist!");
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}
