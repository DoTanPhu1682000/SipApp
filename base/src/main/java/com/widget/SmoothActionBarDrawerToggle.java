package com.widget;

import android.app.Activity;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

    private Runnable runnable;
    private Activity activity;

    public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity = activity;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        activity.invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        activity.invalidateOptionsMenu();
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        super.onDrawerStateChanged(newState);
        if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
            runnable.run();
            runnable = null;
        }
    }

    public void runWhenIdle(Runnable runnable) {
        this.runnable = runnable;
    }
}

