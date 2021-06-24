package com.igordutrasanches.anajatubaboletim.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.fragment.IntroFragment;
import com.igordutrasanches.anajatubaboletim.fragment.LoginFragment;
import com.igordutrasanches.anajatubaboletim.fragment.WelcomeFragment;
import com.igordutrasanches.anajatubaboletim.tools.MyToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    public WelcomeContent contentFragment;
    private View view;

    private long countClick = 0;

    private void toast(String msg, int x){
        int index = 0;
        switch (x){
            case 0: index = MyToast.NOME; break;
            case 1: index = MyToast.SUCCESS; break;
            case 2: index = MyToast.ERROR; break;
        }
        MyToast.makeText(this, msg, index).show();
    }

    @Override
    public void onBackPressed() {
       exit();
    }

    private void exit(){
        if(System.currentTimeMillis() - countClick > 2000){
            countClick = System.currentTimeMillis();
            toast("Clique novamente para sair!", 0);
            return;
        }
        finishAffinity();
    }

    public interface WelcomeContent {
        boolean shouldDisplay(Context context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_welcome);
            this.contentFragment = getCurrentFragment(this);
            if (this.contentFragment == null) {
                finish();
            }
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.add(R.id.welcome_content, (Fragment) contentFragment);
            beginTransaction.commit();


        }catch(Exception x){
         }
    }

    private static WelcomeContent getCurrentFragment(Context context) {
        for (WelcomeContent welcomeContent : getWelcomeContent()) {
            if (welcomeContent.shouldDisplay(context)) {
                return welcomeContent;
            }
        }
        return null;
    }

    private static List<WelcomeContent> getWelcomeContent() {
        return new ArrayList(Arrays.asList(new WelcomeFragment[]{new LoginFragment(), new IntroFragment()}));
    }

    public static boolean shouldDisplay(Context context) {
        return getCurrentFragment(context) != null;
    }
}

