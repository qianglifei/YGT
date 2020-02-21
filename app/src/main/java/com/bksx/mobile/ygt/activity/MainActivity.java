package com.bksx.mobile.ygt.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bksx.mobile.ygt.R;
import com.bksx.mobile.ygt.base.MyBaseActivity;
import com.bksx.mobile.ygt.fragment.EpidemicSituationFragment;
import com.bksx.mobile.ygt.fragment.FirstPageFragment;
import com.bksx.mobile.ygt.fragment.MyFragment;
import com.bksx.mobile.ygt.fragment.PersonnelManagement;
import com.bksx.mobile.ygt.utils.StaticObject;

import java.io.IOException;

import bksx.com.soymilk.bean.LoginBean;
import bksx.com.soymilk.location.LocationService;
import bksx.com.soymilk.net.HttpClient;
import bksx.com.soymilk.permission.AppPermission;
import bksx.com.soymilk.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends MyBaseActivity implements BottomNavigationBar.OnTabSelectedListener{
    @BindView(R.id.layFrame)
    FrameLayout layFrame;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBotNavBar;
    private FragmentManager fm = getSupportFragmentManager();


    private FirstPageFragment firstPageFragment;
    private PersonnelManagement personnelManagementFragment;
    private EpidemicSituationFragment epidemicSituationFragment;
    private MyFragment myFragment;
    private LoginBean lb;
    private HttpClient httpClient;
    private LocationService locationService;

    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //初始化事件
        initEvent();

    }

    private void initEvent() {

        locationService = LocationService.getInstance(getApplicationContext());

        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        AppPermission appPermission = new AppPermission();
        boolean isPermission = appPermission.lacksPermissions(this,perms);

        if(isPermission) {
            AppPermission.requestPermissions(this, perms, new AppPermission.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    //授权
                    ToastUtil.showShort(getApplicationContext(), "获取授权成功，您可以继续使用此应用");

                }

                @Override
                public void onPermissionDenied() {
                    //没有授权，或者有一个权限没有授权
                    ToastUtil.showShort(getApplicationContext(), "拒绝授权将会影响此应用部分功能，稍后为您退出此应用");

                    finish();
                }
            });
        }

        mBotNavBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBotNavBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        //配置tab与之对应的fragment
        mBotNavBar.addItem(new BottomNavigationItem(R.mipmap.icon_sy_pressed, "首页").setInactiveIconResource(R.mipmap.icon_sy))
                .addItem(new BottomNavigationItem(R.mipmap.icon_rygl_pressed, "人员管理").setInactiveIconResource(R.mipmap.icon_rygl))
                .addItem(new BottomNavigationItem(R.mipmap.icon_yqts_pressed, "疫情态势").setInactiveIconResource(R.mipmap.icon_yqts))
                .addItem(new BottomNavigationItem(R.mipmap.icon_wd_pressed, "我的").setInactiveIconResource(R.mipmap.icon_wd));

        mBotNavBar.setActiveColor("#333333");
        mBotNavBar.setInActiveColor("#999999");
        //设置Bottom
        mBotNavBar.setFirstSelectedPosition(0).initialise();
        //设置默认fragment
        onTabSelected(0);

        mBotNavBar.setTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(int position) {
        //开启一个Fragment的事物
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //先隐藏掉所有的Fragment,以防多个Fragment同时出现在界面上
        hideFragment(fragmentTransaction);
        switch (position) {
            case 0: {
                if (firstPageFragment == null) {
                    firstPageFragment = new FirstPageFragment();
                    fragmentTransaction.add(R.id.layFrame, firstPageFragment);
                } else {
                    fragmentTransaction.remove(firstPageFragment);
                    firstPageFragment = new FirstPageFragment();
                    fragmentTransaction.add(R.id.layFrame, firstPageFragment);
                }
            }
            break;
            case 1: {
                if (personnelManagementFragment == null) {
                    personnelManagementFragment = new PersonnelManagement();
                    fragmentTransaction.add(R.id.layFrame, personnelManagementFragment);
                } else {
                    fragmentTransaction.show(personnelManagementFragment);
                }

            }
            break;
            case 2: {
                if (epidemicSituationFragment == null) {
                    epidemicSituationFragment = new EpidemicSituationFragment();
                    fragmentTransaction.add(R.id.layFrame, epidemicSituationFragment);
                } else {
//                    fragmentTransaction.remove(warningInfoFragment);
//                    warningInfoFragment = new EarlyWarningInfoFragment();
                    fragmentTransaction.show(epidemicSituationFragment);
                }
            }
            break;
            case 3: {
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    fragmentTransaction.add(R.id.layFrame, myFragment);
                } else {
                    fragmentTransaction.show(myFragment);
                }
            }
            break;
            default:
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (firstPageFragment != null) {
            fragmentTransaction.hide(firstPageFragment);
        }
        if (personnelManagementFragment != null) {
            fragmentTransaction.hide(personnelManagementFragment);
        }
        if (epidemicSituationFragment != null) {
            fragmentTransaction.hide(epidemicSituationFragment);
        }
        if (myFragment != null) {
            fragmentTransaction.hide(myFragment);
        }
    }

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
           /* Toast.makeText(getApplicationContext(),
                    "再按一次退出程序", Toast.LENGTH_SHORT).show();*/
            StaticObject.showToast(getApplicationContext(), "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        }else{
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mBotNavBar.setFirstSelectedPosition(0).initialise();
        //设置默认fragment
        onTabSelected(0);

    }
}
