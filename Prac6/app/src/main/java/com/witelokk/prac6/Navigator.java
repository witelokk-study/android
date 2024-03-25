package com.witelokk.prac6;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class Navigator {
    AppCompatActivity activity;

    Navigator(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void switchFragment(int menuOptionId) {
        if (menuOptionId == R.id.menu_open_bottom_navigation_activity) {
            Intent intent = new Intent(activity, BottomNavigationActivity.class);
            activity.startActivity(intent);
            return;
        }

        Fragment fragment = null;
        String fragmentName;
        if (menuOptionId == R.id.menu_fragment_1) {
            fragment = new FirstFragment();
            fragmentName = "Fragment 1";
        }
        else if (menuOptionId == R.id.menu_fragment_2) {
            fragment = new SecondFragment();
            fragmentName = "Fragment 2";
        }
        else if (menuOptionId == R.id.menu_fragment_3) {
            fragment = new ThirdFragment();
            fragmentName = "Fragment 3";
        } else {
            return;
        }

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(fragmentName);
        }

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }
}
