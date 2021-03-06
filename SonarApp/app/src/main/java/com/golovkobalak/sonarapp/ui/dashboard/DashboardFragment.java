package com.golovkobalak.sonarapp.ui.dashboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.golovkobalak.sonarapp.R;
import com.golovkobalak.sonarapp.service.TrackingInterface;

public class DashboardFragment extends Fragment {
    public static final String TAG = DashboardFragment.class.getSimpleName();
    private DashboardViewModel dashboardViewModel;
    private WebSettings settings;
    private WebView webView;
    private TrackingInterface trackingService;
    private DownloadClickListener downloadClickListener;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.getActivity().setContentView(R.layout.fragment_dashboard);
//        WebView webView = (WebView)this.getActivity().findViewById(R.id.map_view);
//        if (savedInstanceState != null)
//            webView.restoreState(savedInstanceState);
//        else {
//            settings = webView.getSettings();
//            WebView.setWebContentsDebuggingEnabled(true);
//            settings.setJavaScriptEnabled(true);
//            settings.setAllowContentAccess(true);
//            settings.setAllowFileAccess(true);
//            settings.setDomStorageEnabled(true);
//            settings.setAllowUniversalAccessFromFileURLs(true);
//            settings.setGeolocationEnabled(true);
//            settings.setJavaScriptCanOpenWindowsAutomatically(true);
//            settings.setAppCacheEnabled(true);
//            settings.setDatabaseEnabled(true);
//            // adb reverse tcp:8080 tcp:8080
//            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                    ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
//            }
//            webView.setWebChromeClient(new WebChromeClient() {
//                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//                    callback.invoke(origin, true, false);
//                }
//            });
//            webView.getSettings().setGeolocationDatabasePath(this.getActivity().getFilesDir().getPath());
//            this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            webView.addJavascriptInterface(new TrackingInterface(this.getContext(), "map"), "TrackingService");
//            webView.loadUrl("file:///android_asset/AngularSonar/index.html");
//        }
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        webView = root.findViewById(R.id.map_view);
//        webView = (WebView) findViewById(R.id.webViewJS);
        settings = webView.getSettings();
        WebView.setWebContentsDebuggingEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setGeolocationEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        // adb reverse tcp:8080 tcp:8080
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webView.getSettings().setGeolocationDatabasePath(this.getActivity().getFilesDir().getPath());
        this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (trackingService == null) {
            trackingService = new TrackingInterface(this.getContext(), "map");
        }
        webView.addJavascriptInterface(trackingService, "TrackingService");
        webView.loadUrl("file:///android_asset/AngularSonar/index.html");
        final Button downloadButton = (Button) root.findViewById(R.id.download_button);
        if (downloadClickListener == null)
            downloadClickListener = new DownloadClickListener(trackingService);
        downloadButton.setOnClickListener(downloadClickListener);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
        trackingService.cancelDownloadMap();
    }
}
