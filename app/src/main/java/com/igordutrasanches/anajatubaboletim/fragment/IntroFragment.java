package com.igordutrasanches.anajatubaboletim.fragment;

import android.content.Context;

import com.igordutrasanches.anajatubaboletim.activity.WelcomeActivity;

public class IntroFragment extends WelcomeFragment implements WelcomeActivity.WelcomeContent {
    public boolean shouldDisplay(Context context) {
        return false;
    }


}
