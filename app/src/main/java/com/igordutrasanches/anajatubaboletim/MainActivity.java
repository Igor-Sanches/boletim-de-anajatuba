package com.igordutrasanches.anajatubaboletim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.igordutrasanches.anajatubaboletim.activity.BaseViewActivity;
import com.igordutrasanches.anajatubaboletim.activity.ContaActivity;
import com.igordutrasanches.anajatubaboletim.activity.DonateActivity;
import com.igordutrasanches.anajatubaboletim.activity.ProfileInfoActivity;
import com.igordutrasanches.anajatubaboletim.activity.SintomasActivity;
import com.igordutrasanches.anajatubaboletim.activity.SobreActivity;
import com.igordutrasanches.anajatubaboletim.activity.TimeLineActivity;
import com.igordutrasanches.anajatubaboletim.adapter.AdapterFragment;
import com.igordutrasanches.anajatubaboletim.conta.Conta;
import com.igordutrasanches.anajatubaboletim.conta.User;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.fragment.RegioesFragment;
import com.igordutrasanches.anajatubaboletim.fragment.VisaoGeralFragment;
import com.igordutrasanches.anajatubaboletim.manager.ImageUtils;
import com.igordutrasanches.anajatubaboletim.tools.MyToast;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseViewActivity {

    private final String TAB_1 = "Visão Geral dos Casos";
    private final String TAB_2 = "Localidades";
    private ViewPager viewPager;
    boolean isAdViewHide = false;
    private TabLayout tabLayout;
    private AdapterFragment adapter;
    private long countClick = 0;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    public Runnable runnable = null;
    public Handler handler = new Handler();
    private MenuItem sorting;

    private void checkBtn(){
        try{
            if(sorting!= null){
                sorting.setVisible(viewPager.getCurrentItem() == 1);
         }
        }catch (Exception c){
            Toast.makeText(this, c.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        sorting = menu.findItem(R.id.sort);
        checkBtn();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.sort){
            RegioesFragment.getInstance().showSorting();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exit();
        }
    }

    private void exit(){

        if(System.currentTimeMillis() - countClick > 2000){
            countClick = System.currentTimeMillis();
            toast("Clique novamente para sair!", 0);
            return;
        }
        finishAffinity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       try{
           setContentView(R.layout.activity_main);
           Toolbar toolbar = findViewById(R.id.toolbar);
           setSupportActionBar(toolbar);
           DrawerLayout drawer = findViewById(R.id.drawer_layout);
           NavigationView navigationView = findViewById(R.id.nav_view);
           ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                   this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
           drawer.addDrawerListener(toggle);
           toggle.syncState();
           viewPager = (ViewPager) findViewById(R.id.viewpager);
           menuHamburger();
           initTab();
           mAdView = findViewById(R.id.adView);
           if(!Data.isDonate(this)){
               if(!Data.isShowDialogDonate(this)){
                   showDialogDonate();
               }
               mAdView.setVisibility(View.VISIBLE);
               iniciarBanner();
           }
           ((NestedScrollView) findViewById(R.id.nested_scroll_view)).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
               public void onScrollChange(NestedScrollView nestedScrollView, int i, int i2, int i3, int i4) {

                   if (i2 < i4) {
                       animateAdView(false);
                   }
                   if (i2 > i4) {
                       animateAdView(true);
                   }
               }
           });
       }catch (Exception x){
           Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    private void showDialogDonate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção!");
        builder.setMessage("Como este aplicativo é gratis e você paga nada para usar, acabamos sem der como manter o app ativo.\nCom isso colocamos anúncio e propagandas para gerar lucros, mais caso a Prefeitura entre em acordo iremos remover tudo.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Data.setShowDialogDonate(true, MainActivity.this);
            }
        });
        builder.show();

    }

    private void animateAdView(boolean z) {
        try{
            if ((!this.isAdViewHide || !z) && (this.isAdViewHide || z)) {
                this.isAdViewHide = z;
                this.mAdView.animate().translationY((float) (z ? this.mAdView.getHeight() * 2 : 0)).setStartDelay(100).setDuration(300).start();
            }
        }catch (Exception x){
            Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void iniciarBanner(){
       try{
           MobileAds.initialize(this, "ca-app-pub-6700943406551338~9291412022");
           AdRequest adRequest = new AdRequest.Builder().build();
           mAdView.loadAd(adRequest);
           mInterstitialAd = new InterstitialAd(this);
           mInterstitialAd.setAdUnitId("ca-app-pub-6700943406551338/8587337467");
           mInterstitialAd.loadAd(new AdRequest.Builder().build());
           mInterstitialAd.setAdListener(new AdListener() {
               @Override
               public void onAdClosed() {
                   // Load the next interstitial.
                   mInterstitialAd.loadAd(new AdRequest.Builder().build());
               }

           });
           initInterstitial();
       }catch (Exception x){
           Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    public void onDestroy() {
       try{
           Runnable runnable2 = this.runnable;
           if (runnable2 != null) {
               this.handler.removeCallbacks(runnable2);
           }
       }catch (Exception x){
           Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
       }
        super.onDestroy();
    }

    private void initInterstitial(){
       try{
           this.runnable = new Runnable() {
               public void run() {
                   if(mInterstitialAd.isLoaded()){
                       mInterstitialAd.show();
                   }
                   MainActivity.this.handler.postDelayed(MainActivity.this.runnable, 25000);
               }
           };
           this.handler.postDelayed(this.runnable, 10000);
       }catch (Exception x){
           Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    protected void onResume() {
        layoutBarMenu();
        super.onResume();
    }

    private void layoutBarMenu(){
        try{
            User user = Conta.getUserProfile(this);
            CircularImageView photoUser =  findViewById(R.id.photoUser);
            TextView nameUser =  findViewById(R.id.nameUser);
            TextView emailUser =  findViewById(R.id.emailUser);
            if(user != null){
                nameUser.setText(user.getName());
                emailUser.setText(user.getEmail());
                ImageUtils.displayImageFromUrl(this, user.getAvatar(), photoUser, getResources().getDrawable(R.drawable.app_icone));
            }

        }catch (Exception c){
            toast(c.getMessage(), 2);
        }
    }

    private void menuHamburger(){


        try{
            layoutBarMenu();

            ((CardView)findViewById(R.id.cardUser))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavigationItemSelected(10);
                        }
                    });
            ((LinearLayout)findViewById(R.id.btnSintomas))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavigationItemSelected(0);
                        }
                    });
            ((LinearLayout)findViewById(R.id.btnDonate))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavigationItemSelected(1);
                        }
                    });
            ((LinearLayout)findViewById(R.id.btn_Msg))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavigationItemSelected(2);
                        }
                    });
            ((LinearLayout)findViewById(R.id.btnProfile))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavigationItemSelected(3);
                        }
                    });
            ((LinearLayout)findViewById(R.id.btnFeedback))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavigationItemSelected(4);
                        }
                    });
            ((LinearLayout)findViewById(R.id.btnRate))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavigationItemSelected(5);
                        }
                    });
            ((LinearLayout)findViewById(R.id.btnSobre))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavigationItemSelected(6);
                        }
                    });
            ((LinearLayout)findViewById(R.id.btnShared))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNavigationItemSelected(7);
                        }
                    });
        }catch (Exception c){
            toast(c.getMessage(), 2);
        }
    }

    public boolean onNavigationItemSelected(int itemTag) {
        switch (itemTag){
            case 10:
                startActivity(new Intent(this, ContaActivity.class));
                break;
            case 0:
                startActivity(new Intent(this, SintomasActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, DonateActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, TimeLineActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, ProfileInfoActivity.class));
                break;
            case 4:
                try{

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    String email = Data.getEmailSupport(this);
                    intent.setData(Uri.parse("mailto:"+email));
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    if(intent.resolveActivity(getPackageManager()) != null){
                        startActivity(intent);
                    }
                }catch (Exception x){
                    Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.igordutrasanches.anajatubaboletim")));
                break;
            case 6:
                startActivity(new Intent(this, SobreActivity.class));
                break;
            case 7:
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "");
                intent.putExtra("android.intent.extra.TEXT", getPlainText());
                startActivity(Intent.createChooser(intent, "Boletim de Anajatuba"));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getPlainText() {
        String text = "Olá, que tal ficar informado sobre os casos da pandemia em Anajatuba?\nBaixe este app para acompanhar a situção dos casos.\n";
        return text + "https://play.google.com/store/apps/details?id=com.igordutrasanches.anajatubaboletim";
    }

    private void initTab() {
        try{
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(this.viewPager);
        }catch (Exception c){
            toast(c.getMessage(), 2);
        }
    }

    private void setupViewPager(ViewPager viewPager2) {
       try{
           adapter = new AdapterFragment(getSupportFragmentManager());
           adapter.adicionar(new VisaoGeralFragment(), TAB_1);
           adapter.adicionar(new RegioesFragment(), TAB_2);
           viewPager2.setAdapter(this.adapter);
           viewPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
               @Override
               public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                   checkBtn();
               }

               @Override
               public void onPageSelected(int position) {

               }

               @Override
               public void onPageScrollStateChanged(int state) {

               }
           });

       }catch (Exception c){
           toast(c.getMessage(), 2);
       }
    }

    private void toast(String msg, int x){
        int index = 0;
        switch (x){
            case 0: index = MyToast.NOME; break;
            case 1: index = MyToast.SUCCESS; break;
            case 2: index = MyToast.ERROR; break;
        }
        MyToast.makeText(this, msg, index).show();
    }

}