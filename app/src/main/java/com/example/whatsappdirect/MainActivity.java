package com.example.whatsappdirect;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editText1,editText2;
    CountryCodePicker countryCodePicker;
    Button button;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;
    private NativeBannerAd nativeBannerAd;
    private final String TAG = MainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadNativeAd();
        editText1 = (EditText) findViewById(R.id.edittext1);
        editText2 = (EditText) findViewById(R.id.edittext2);
        button = (Button) findViewById(R.id.button);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);


    }

    public void contact(View view) {


            EditText phoneNumberField = (EditText) findViewById(R.id.edittext1);
            if (phoneNumberField.getText().toString().isEmpty()) {
                Alert_Dialog_Blank_Input();
            } else {
                //  CODE FOR COUNTRY CODE SPINNER
                CountryCodePicker cpp = (CountryCodePicker) findViewById(R.id.ccp);
                cpp.registerCarrierNumberEditText(phoneNumberField);
                String phoneNumber = cpp.getFullNumber();


                boolean installed = appInstalledOrNot("com.whatsapp");
                if(installed) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+  phoneNumber ));
                    startActivity(browserIntent);



                } else {
                    Toast.makeText(this,"Whatsapp is not installed on your device",Toast.LENGTH_SHORT).show();
                }

            }


        }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void Alert_Dialog_Blank_Input() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage("Please Enter A Number");
        AlertDialog alert = builder.create();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_share) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String link = "https://play.google.com/store/apps/details?id=" + getPackageName();
                String message = getString(R.string.app_share_text) + link;
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, "Share App"));
            } catch (Exception e) {

            }
        } else if (item.getItemId() == R.id.privacy_policy) {
            startActivity(new Intent(this, Main2Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadNativeAd() {


        nativeBannerAd = new NativeBannerAd(this, "276272916588432_276273329921724");
        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Toast.makeText(MainActivity.this, "Eorror", Toast.LENGTH_SHORT).show();

                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Toast.makeText(MainActivity.this, "Sanjay", Toast.LENGTH_SHORT).show();


                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeBannerAd);
            }


            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });
        // load the ad
        nativeBannerAd.loadAd();
    }

    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = findViewById(R.id.native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.nativebannerlayout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(getApplicationContext(), nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = adView.findViewById(R.id.native_ad_icon);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

}












