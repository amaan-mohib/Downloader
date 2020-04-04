package com.example.downloader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class YouTubeFragment extends Fragment {
    final static String ARG_URL="url";
    changingUrl changed_url;

    public static YouTubeFragment newinstance(){return new YouTubeFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.youtube_fragment, container, false);

        //Web view
        final WebView wv =(WebView) v.findViewById(R.id.webView2);
        String yturl="https://www.youtube.com";
        wv.loadUrl(yturl);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        wv.canGoBack();
        wv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()== MotionEvent.ACTION_UP && wv.canGoBack()){
                    wv.goBack();
                    return true;
                }
                return false;
            }
        });
        // Get Connectivity Manager class object from Systems Service
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get Network Info from connectivity Manager
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo == null)
        {
            MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(v.getContext());
            builder.setTitle("You are not connected to the internet");
            builder.setIcon(R.drawable.ic_info_24px);
            builder.setMessage("Check your WiFi or Mobile Data connection.")
                    .setCancelable(false)
                    .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            wv.reload();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.create();
            builder.show();
        }
        FloatingActionButton fab=(FloatingActionButton)v.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wv.getUrl().contains("watch")) {
                    String chars[] = {"!", "@", "#", "%", "^", "&", "*", "(", ")", ",", ";", "-", ":", "[", "]", "{", "}", "Music", "Lyric", "Video", "Official", "|", "Audio", "YouTube"};
                    String url = wv.getTitle();
                    for (int i = 0; i < chars.length; i++) {
                        url = url.replace(chars[i], "");
                    }
                    url = url.replace(" ", "+");
                    String newurl = "https://www.musicpleer.cloud/#!" + url;
                    changed_url.sendChangedUrl(newurl);
                }
                else{
                    MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getView().getContext());
                    builder.setTitle("No song selected");
                    builder.setIcon(R.drawable.ic_info_24px);
                    builder.setMessage("Play any song to proceed.")
                            .setCancelable(false)
                            .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.create();
                    builder.show();
                }
            }
        });
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        Bundle arg=getArguments();
        if(arg!=null){
            changeUrl(arg.getString(ARG_URL));}
    }
    public void setChanged_url(changingUrl changed_url){this.changed_url=changed_url;}
    public interface changingUrl{
        public void sendChangedUrl(String url);
    }
    public void changeUrl(String url){
        final WebView wv =(WebView) getView().findViewById(R.id.webView2);
        if(url!=null)
        {
            wv.loadUrl(url);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);

                    return true;
                }
            });
            wv.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
            wv.canGoBack();
            wv.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()== MotionEvent.ACTION_UP && wv.canGoBack()){
                        wv.goBack();
                        return true;
                    }
                    return false;
                }
            });
            // Get Connectivity Manager class object from Systems Service
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            // Get Network Info from connectivity Manager
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            // if no network is available networkInfo will be null
            // otherwise check if we are connected
            if (networkInfo == null) {
                MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getView().getContext());
                builder.setTitle("You are not connected to the internet");
                builder.setIcon(R.drawable.ic_info_24px);
                builder.setMessage("Check your WiFi or Mobile Data connection.")
                        .setCancelable(false)
                        .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                wv.reload();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create();
                builder.show();
            }
        }
    }
}
