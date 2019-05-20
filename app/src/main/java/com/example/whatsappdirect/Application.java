package com.example.whatsappdirect;

import com.facebook.ads.AudienceNetworkAds;
import com.onesignal.OneSignal;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);


        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
