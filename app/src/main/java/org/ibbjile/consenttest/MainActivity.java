package org.ibbjile.consenttest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.sourcepoint.gdpr_cmplibrary.GDPRConsentLib;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "**MainActivity";
    private ViewGroup mainViewGroup;

    private void showView(View view) {
        if (view.getParent() == null) {
            view.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.bringToFront();
            view.requestLayout();
            mainViewGroup.addView(view);
        }
    }

    private void removeView(View view) {
        if (view.getParent() != null)
            mainViewGroup.removeView(view);
    }

    private GDPRConsentLib buildGDPRConsentLib() {
        return GDPRConsentLib.newBuilder(
                XXX,
                XXXX,
                XXX,
                "XXX",
                this)
                .setOnConsentUIReady(view -> {
                    MainActivity.this.showView(view);
                    Log.i(TAG, "onConsentUIReady");
                })
                .setOnConsentUIFinished(view -> {
                    MainActivity.this.removeView(view);
                    Log.i(TAG, "onConsentUIFinished");
                })
                .setOnConsentReady(consent -> {
                    Log.i(TAG, "onConsentReady");
                    Log.i(TAG, "consentString: " + consent.consentString);
                    Log.i(TAG, consent.TCData.toString());

                    for (String key : consent.vendorGrants.keySet()) {
                        if (consent.vendorGrants.get(key).vendorGrant == false) {
                            Log.i("disabled vendor: ", consent.vendorGrants.get(key).toString());
                        }
                    }
                })
                .setOnError(error -> {
                    Log.e(TAG, "Something went wrong: ", error);
                    Log.i(TAG, "ConsentLibErrorMessage: " + error.consentLibErrorMessage);
                })
                .build();
    }

    public void showPM(View v) {
        buildGDPRConsentLib().showPm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewGroup = findViewById(android.R.id.content);

        buildGDPRConsentLib().run();
    }
}