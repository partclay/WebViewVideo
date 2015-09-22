package com.onepixeloff.webviewvideo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private WebView webView;



    private FrameLayout webHolder;
    private FrameLayout videoHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);

        webHolder = (FrameLayout) findViewById(R.id.webHolder);
        videoHolder = (FrameLayout) findViewById(R.id.fullScreen);

        class CustomChromeClient extends WebChromeClient {
            private CustomViewCallback mCustomViewCallback;
            private View mTargetVideoView;

            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (view instanceof FrameLayout) {

                    FrameLayout frameLayout = (FrameLayout) view;

                    mCustomViewCallback = callback;
                    mTargetVideoView = view;


                    webHolder.setVisibility(View.GONE);


                    videoHolder.addView(mTargetVideoView, new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));


                    videoHolder.setVisibility(View.VISIBLE);
                    mTargetVideoView.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint({"NewApi"})
            public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
                onShowCustomView(view,callback);
            }


            public void onHideCustomView() {

                if (mTargetVideoView == null)
                    return;

                if (mTargetVideoView instanceof VideoView)
                    ((VideoView)mTargetVideoView).stopPlayback();

                webHolder.setVisibility(View.VISIBLE);

                videoHolder.setVisibility(View.GONE);
                videoHolder.removeView(mTargetVideoView);
                mTargetVideoView = null;

                mCustomViewCallback.onCustomViewHidden();
            }
        }

        webView.setWebChromeClient(new CustomChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");
    }
}
