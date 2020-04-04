package com.example.downloader;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class WebViewFragment extends Fragment {
    final static String ARG_URL="url";
    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.web_view_fragment, container, false);

        //Web view
        final WebView wv =(WebView) v.findViewById(R.id.webView);
        final TextView textView=(TextView) v.findViewById(R.id.text);
        wv.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        Bundle arg=getArguments();
        if(arg!=null){
            changeUrl(arg.getString(ARG_URL));}
    }
    public void changeUrl(String url)
    {
        final TextView textView=(TextView) getView().findViewById(R.id.text);
        final WebView wv =(WebView) getView().findViewById(R.id.webView);
        wv.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
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
