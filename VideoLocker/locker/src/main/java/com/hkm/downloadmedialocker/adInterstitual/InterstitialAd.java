package com.hkm.downloadmedialocker.adInterstitual;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.hkm.downloadmedialocker.BuildConfig;
import com.hkm.downloadmedialocker.R;

/**
 * Created by hesk on 27/5/15.
 */
public class InterstitialAd extends AppCompatActivity {

    private PublisherInterstitialAd mInterstitialAd;
    private CountDownTimer mCountDownTimer;
    private Button mRetryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullad);

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new PublisherInterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(BuildConfig.DFP_INTER_ID);

        // Create an ad request.
        PublisherAdRequest.Builder adRequestBuilder = new PublisherAdRequest.Builder();

        // Optionally populate the ad request builder.
        adRequestBuilder.addTestDevice(PublisherAdRequest.DEVICE_ID_EMULATOR);

        // Set an AdListener.
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(InterstitialAd.this, "The interstitial is loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                //goToNextLevel();
            }
        });
        mInterstitialAd.loadAd(adRequestBuilder.build());

        // Create the "retry" button, which tries to show an interstitial between game plays.
        mRetryButton = ((Button) findViewById(R.id.retry_button));
        mRetryButton.setVisibility(View.INVISIBLE);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitial();
            }
        });

        // Create the game timer, which counts down to the end of the level
        // and shows the "retry" button.
        final TextView textView = ((TextView) findViewById(R.id.timer));
        mCountDownTimer = new CountDownTimer(3000, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                textView.setText("seconds remaining: " + ((millisUnitFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                textView.setText("done!");
                mRetryButton.setVisibility(View.VISIBLE);
            }
        };
    }


    @Override
    public void onResume() {
        // Initialize the timer if it hasn't been initialized yet.
        // Start the game.
        super.onResume();
        startGame();
    }

    @Override
    public void onPause() {
        // Cancel the timer if the game is paused.
        mCountDownTimer.cancel();
        super.onPause();
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            startGame();
        }
    }

    private void startGame() {
        // Hide the retry button, load the ad, and start the timer.
        mRetryButton.setVisibility(View.INVISIBLE);
        PublisherAdRequest publisherAdRequest = new PublisherAdRequest.Builder().build();

        mInterstitialAd.loadAd(publisherAdRequest);
        String url = publisherAdRequest.getContentUrl();
        //  publisherAdRequest.zzac()   ;
        Toast.makeText(getBaseContext(), url, Toast.LENGTH_LONG).show();
        mCountDownTimer.start();
    }


    /**
     * Gets a string error reason from an error code.
     */
    private String getErrorReason(int errorCode) {
        String errorReason = "";
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                errorReason = "Internal error";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                errorReason = "Invalid request";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                errorReason = "Network Error";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                errorReason = "No fill";
                break;
        }
        return errorReason;
    }


}
