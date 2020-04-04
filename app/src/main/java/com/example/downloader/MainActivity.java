package com.example.downloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements SearchFragment.changeItem, SearchFragment.searchUrl,SearchFragment.sendHistory,YouTubeFragment.changingUrl {

    public static BottomNavigationView bottomNavigationView;
    int id;
    SearchFragment searchFrag=(SearchFragment) new SearchFragment();
    InfoFragment infoFrag=(InfoFragment) new InfoFragment();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment youtubeFragment = new YouTubeFragment();
        final Fragment searchFragment = new SearchFragment();
        final Fragment webFragment = new WebViewFragment();
        final Fragment infoFragment = new InfoFragment();

        fragmentManager.beginTransaction().add(R.id.flcontainer, searchFragment, "4").hide(searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.flcontainer, infoFragment, "3").hide(infoFragment).commit();
        fragmentManager.beginTransaction().add(R.id.flcontainer, webFragment, "2").hide(webFragment).commit();
        fragmentManager.beginTransaction().add(R.id.flcontainer, youtubeFragment, "1").commit();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment active = youtubeFragment;
                switch (item.getItemId()) {
                    case R.id.home:
                        fragmentManager.beginTransaction().hide(active).show(youtubeFragment).commit();
                        active = youtubeFragment;
                        break;
                    case R.id.search:
                        Fragment start=null;
                        if(savedInstanceState==null){
                            start=new SearchFragment();
                        }else{
                            start=fragmentManager.getFragment(savedInstanceState,"client");
                        }
                        fragmentManager.beginTransaction().hide(active).show(searchFragment).commit();
                        active = searchFragment;
                        break;
                    case R.id.music:
                        fragmentManager.beginTransaction().hide(active).show(webFragment).commit();
                        active = webFragment;

                        break;
                    case R.id.info:
                        Fragment Infostart=null;
                        if(savedInstanceState==null){
                            Infostart=new InfoFragment();
                        }else{
                            Infostart=fragmentManager.getFragment(savedInstanceState,"info");
                        }
                        fragmentManager.beginTransaction().hide(active).show(infoFragment).commit();
                        active = infoFragment;
                        break;
                    default:
                        active = youtubeFragment;
                }
                if (active != null) {
                    fragmentManager.beginTransaction().replace(R.id.flcontainer, active).addToBackStack(null).commit();
                    id=bottomNavigationView.getSelectedItemId();
                    return true;
                }

                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
        Bundle extras;
        extras = getIntent().getExtras();
        if(extras!=null){
            String gotUrl=extras.getString(Intent.EXTRA_TEXT);
            sendUrl(gotUrl);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(searchFrag.isAdded()){
        getSupportFragmentManager().putFragment(outState,"client",searchFrag);}
        if(infoFrag.isAdded()){
        getSupportFragmentManager().putFragment(outState,"info",infoFrag);}
    }
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
            bottomNavigationView.setSelectedItemId(id);
        } else {
            super.onBackPressed();
        }
    }
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof SearchFragment) {
            SearchFragment searchFragment = (SearchFragment) fragment;
            searchFragment.setItem(this);
            searchFragment.setUrl(this);
            searchFragment.setHistory(this);
        }
        if(fragment instanceof YouTubeFragment) {
            YouTubeFragment youTubeFragment=(YouTubeFragment) fragment;
            youTubeFragment.setChanged_url(this);
        }
    }

    public void sendId(String url) {
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bnv.setSelectedItemId(R.id.music);
        WebViewFragment webFrag = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webView);
        if (webFrag != null) {
            //
        } else {
            Bundle arg = new Bundle();
            arg.putString(WebViewFragment.ARG_URL, url);
            WebViewFragment newFrag = new WebViewFragment();
            newFrag.setArguments(arg);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flcontainer, newFrag,"tag1");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void sendUrl(String url) {
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bnv.setSelectedItemId(R.id.home);
        YouTubeFragment webFrag = (YouTubeFragment) getSupportFragmentManager().findFragmentById(R.id.webView2);
        if (webFrag != null) {
            //
        } else {
            Bundle arg = new Bundle();
            arg.putString(WebViewFragment.ARG_URL, url);
            YouTubeFragment newFrag = new YouTubeFragment();
            newFrag.setArguments(arg);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flcontainer, newFrag,"tag2");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public void sendChangedUrl(String url) {
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bnv.setSelectedItemId(R.id.music);
        WebViewFragment webFrag = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webView);
        if (webFrag != null) {
            //
        } else {
            Bundle arg = new Bundle();
            arg.putString(WebViewFragment.ARG_URL, url);
            WebViewFragment newFrag = new WebViewFragment();
            newFrag.setArguments(arg);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flcontainer, newFrag,"tag3");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public void sendData(String song,String artist) {
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bnv.setSelectedItemId(R.id.info);
        InfoFragment infoFragment = (InfoFragment) getSupportFragmentManager().findFragmentById(R.id.info);
        if (infoFragment != null) {
            //
        } else {
            Bundle arg = new Bundle();
            arg.putString(InfoFragment.ARG_SONG, song);
            arg.putString(InfoFragment.ARG_ARTIST,artist);
            InfoFragment newFrag = new InfoFragment();
            newFrag.setArguments(arg);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flcontainer, newFrag,"tag4");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
