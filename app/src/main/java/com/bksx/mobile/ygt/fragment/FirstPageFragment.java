package com.bksx.mobile.ygt.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bksx.mobile.ygt.R;
import com.bksx.mobile.ygt.utils.DensityUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;


import java.io.IOException;

import bksx.com.soymilk.bean.LoginBean;
import bksx.com.soymilk.net.HttpClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import okhttp3.Response;

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

    @BindView(R.id.linearLayout_drive)
    LinearLayout linearLayoutDrive;
    private PopupWindow mPopWindow;
    private Context mContext;
    private LoginBean lb;
    private HttpClient httpClient;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
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


    @OnClick({R.id.linearLayout_infoManagement, R.id.linearLayout_temperature})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_infoManagement:
                  showPopupWindow();
//                lb = new LoginBean();
//                lb.setYhzh("17600121006");
//                lb.setUnittype("iPhone10,2");
//                lb.setYhmm("a00000");
//                lb.setBb("GyzrcGr_iOS_1.0.4.190827_R");
//                lb.setUdid("4A088875-BED9-4DBE-A862-088B37DEF077");
//                lb.setAppbb("GyzrcGr_iOS_1.0.4.190827_R");
//                httpClient = HttpClient.getInstance(mContext);
//
//
//
//                httpClient.postJsonSecurity(mContext, "http://app.gyrc.cn/grapp/dlzc/dlzc/grdlCx",lb, new HttpClient.ResultCallback() {
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(Response response) throws IOException {
//                        System.out.println(response.body().string());
//                    }
//                });
                break;
            case R.id.linearLayout_temperature:
                break;
        }
    }
    private void showPopupWindow() {
        DensityUtils du = new DensityUtils();
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop, null);
        final TextView textViewAll = contentView.findViewById(R.id.textView_temporaryStorage);
        final TextView textViewEvent = contentView.findViewById(R.id.textView_DetailCollection);
        final TextView textViewHouse = contentView.findViewById(R.id.textView_check);
        final TextView textViewPeople = contentView.findViewById(R.id.textView_population);

        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(du.dip2px(mContext,150f));
        mPopWindow.setHeight(du.dip2px(mContext, 200f));
        mPopWindow.setFocusable(true);

        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置整个窗口阴影消失
        mPopWindow.setOnDismissListener(new PopOnDismissListener());
        //设置PopWindow周边背景
        backgroundAlpha(0.5f);
        //设置各控件点击响应
        mPopWindow.showAsDropDown(linearLayoutInfoManagement,0,du.dip2px(mContext, 25.0f));

        //设置textView 点击事件
        textViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转界面
                mPopWindow.dismiss();
            }
        });

        //文字事件
        textViewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });

        //房屋信息
        textViewHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });

        //人员信息
        textViewPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
    }

    class PopOnDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            //点击空白区域的时候，关闭PopupWindow，并且把真个窗口的阴影全部关闭掉你
            backgroundAlpha(1f);
           // mCheckBoxPop.setChecked(false);
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }
}
