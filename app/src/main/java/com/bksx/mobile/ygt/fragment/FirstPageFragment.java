package com.bksx.mobile.ygt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bksx.mobile.ygt.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstPageFragment extends Fragment {
    @BindView(R.id.textView_communityName)
    TextView textViewCommunityName;
    @BindView(R.id.textView_personName)
    TextView textViewPersonName;
    @BindView(R.id.textView_OnlineTime)
    TextView textViewOnlineTime;
    @BindView(R.id.linearLayout_infoManagement)
    LinearLayout linearLayoutInfoManagement;
    @BindView(R.id.button_personGoInto)
    RadioButton buttonPersonGoInto;
    @BindView(R.id.button_personGoOut)
    RadioButton buttonPersonGoOut;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_page, container, false);
        ButterKnife.bind(this, view);
        //初始化事件
        initEvent();
        return view;
    }

    private void initEvent() {

        buttonPersonGoInto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonPersonGoOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
