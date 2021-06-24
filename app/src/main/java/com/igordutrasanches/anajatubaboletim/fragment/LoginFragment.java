package com.igordutrasanches.anajatubaboletim.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.activity.LoginEmailActivity;
import com.igordutrasanches.anajatubaboletim.activity.WelcomeActivity;
import com.igordutrasanches.anajatubaboletim.conta.Conta;
import com.igordutrasanches.anajatubaboletim.services.Conexao;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;


public class LoginFragment extends WelcomeFragment implements WelcomeActivity.WelcomeContent {

    private LinearLayout btns;
    private ViewPagerAdapter adapter;
    private ViewPager pager;
    private ProgressDialog waitingDialog;
    private int colorPointSelected = R.color.colorAccent;
    private int colorPointNotSelected = R.color.white;
    private CardView btnGo, btnEmail;
    private int position=0;
    private View layout;
    private int[] res = new int[] {
            R.drawable.covid_2,
            R.drawable.covid_3,
            R.drawable.anajatuba_icone
    };

    private int[] content = new int[] {
            R.string.covid_2,
            R.string.covid_3,
            R.string.anajatuba_icone
    };

    private void getBtns(boolean isTrue){
        btns.setVisibility(isTrue? View.VISIBLE : View.GONE);
        btnGo.setVisibility(isTrue? View.GONE : View.VISIBLE);
    }

    private CheckBox termosCheck;

    //Carregar Firebase
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mAuth = Conexao.getFirebaseAuth();
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_login, container, false);
        try {
            iniciar();
        } catch (Exception x) {
            Toast.makeText(getActivity(), x.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return layout;
    }

    private void iniciar() {
        try{
            waitingDialog = new ProgressDialog(getContext());
            waitingDialog.setMessage(getString(R.string.login_label));
            waitingDialog.setCancelable(false);
            ((TextView)layout.findViewById(R.id.policy))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1YNVVPP2Ek-kUntTXI6IQZBgQwtmZsBCoq8mkJX8BgSE/edit?usp=drivesdk"));
                  startActivity(intent);
                        }
                    });
            termosCheck=layout.findViewById(R.id.termosCheck);
            btnEmail=layout.findViewById(R.id.btnEmail);
            pager = layout.findViewById(R.id.pager);
            btns = layout.findViewById(R.id.btns);
            btnGo = layout.findViewById(R.id.btn_enter);
            adapter = new ViewPagerAdapter();
            pager.setAdapter(adapter);
            pager.setOnPageChangeListener(pagerListener);
            bottomProgressDots(position);
            btnEmail.setEnabled(false);
            termosCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    btnEmail.setEnabled(isChecked);
                }
            });
            btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                       if(termosCheck.isChecked()){
                           startActivity(new Intent(getActivity(), LoginEmailActivity.class));
                       }else{
                           Toast.makeText(getActivity(), "Aceite os Termos e Politica de Privacidade para continuar", Toast.LENGTH_SHORT).show();
                       }
                    }catch(Exception x){
                        Toast.makeText(getActivity(), x.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
            btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }
            });
        }catch (Exception x){
            Toast.makeText(getActivity(), x.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ViewPager.OnPageChangeListener pagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            LoginFragment.this.position = position;
            bottomProgressDots(position);

            getBtns(position == 2);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private void bottomProgressDots(int position) {
        LinearLayout linearLayout = layout.findViewById(R.id.layout_dots);
        ImageView[] imagens = new ImageView[3];
        linearLayout.removeAllViews();
        for(int i = 0; i < imagens.length; i++){
            imagens[i] = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(20, 20));
            params.setMargins(10,10,10,10);
            imagens[i].setLayoutParams(params);
            imagens[i].setImageResource(R.drawable.shape_cicle);
            imagens[i].setColorFilter(getResources().getColor(colorPointNotSelected));
            linearLayout.addView(imagens[i]);
        }
        if(imagens.length > 0){
            imagens[position].setImageResource(R.drawable.shape_cicle);
            imagens[position].setColorFilter(getResources().getColor(colorPointSelected));
        }
        linearLayout.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.layout_view_pager, container, false);
            ((ImageView)view.findViewById(R.id.card_image_pager)).setImageResource(res[position]);
            ((TextView)view.findViewById(R.id.card_text_pager)).setText(getString(content[position]));
            container.addView(view);
            return view;

        }

        @Override
        public int getCount() {
            return content.length;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    public void startWaitingDialog(boolean b) {
        if(b){
            waitingDialog.show();
        }else{
            waitingDialog.hide();
        }
    }

    public boolean shouldDisplay(Context context) {
        if(mAuth == null){
            mAuth = Conexao.getFirebaseAuth();
        }
        return mAuth.getCurrentUser() == null;
    }


}