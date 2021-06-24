package com.igordutrasanches.anajatubaboletim.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.igordutrasanches.anajatubaboletim.MainActivity;

public abstract class WelcomeFragment extends Fragment {
    protected Activity mActivity;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    /* access modifiers changed from: 0000 */
    public void doNext() {
        startActivity(new Intent(this.mActivity, MainActivity.class));
        this.mActivity.finish();
    }

    /* access modifiers changed from: 0000 */
    public void doFinish() {
        this.mActivity.finish();
    }

}
