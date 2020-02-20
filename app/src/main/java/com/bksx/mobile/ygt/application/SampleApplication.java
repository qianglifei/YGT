package com.bksx.mobile.ygt.application;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class SampleApplication extends TinkerApplication {

    private static SampleApplication instance;

    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.bksx.mobile.ygt.application.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static SampleApplication getInstance(){
        return instance;
    }
}