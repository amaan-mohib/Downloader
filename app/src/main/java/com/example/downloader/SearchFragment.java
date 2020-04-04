package com.example.downloader;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {
        changeItem itemId;
        searchUrl Url;
        sendHistory history;

        public static SearchFragment newInstance() {
        return new SearchFragment();
    }
        private MaterialButton song, yt;
        private TextInputLayout song_box, artist_box;
        private TextView addto_history;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.search_fragment, container, false);
        final DatabaseHelper databaseHelper=new DatabaseHelper(getActivity().getApplicationContext());
        final InfoFragment infoFragment = new InfoFragment();
        song=inflate.findViewById(R.id.materialButton2);
        yt=inflate.findViewById(R.id.materialButton);
        song_box=inflate.findViewById(R.id.textInputLayout);
        artist_box=inflate.findViewById(R.id.textInputLayout2);
        addto_history=inflate.findViewById(R.id.textView);
        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(song_box.getEditText().getText().toString().equals("") && artist_box.getEditText().getText().toString().equals(""))
                {
                    song_box.setError("*Enter Song");
                    artist_box.setError("*Enter Artist");
                }
                else if(song_box!=null && artist_box!=null){
                    //infoFragment.addData(song_box.getEditText().getText().toString(),artist_box.getEditText().getText().toString(),getActivity());
                    //databaseHelper.insertDatabase(song_box.getEditText().getText().toString(),artist_box.getEditText().getText().toString());
                    //history.sendData(song_box.getEditText().getText().toString(),artist_box.getEditText().getText().toString());
                    String url;
                    url="https://musicpleer.cloud/#!"+song_box.getEditText().getText()+" "+artist_box.getEditText().getText();
                    itemId.sendId(url);
                }
            }
        });
        yt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(song_box.getEditText().getText().toString().equals("") && artist_box.getEditText().getText().toString().equals("")){
                    song_box.setError("*Enter Song");
                    artist_box.setError("*Enter Artist");
                }
                else if(song_box!=null && artist_box!=null){
                    //infoFragment.addData(song_box.getEditText().getText().toString(),artist_box.getEditText().getText().toString(),getActivity());
                    //databaseHelper.insertDatabase(song_box.getEditText().getText().toString(),artist_box.getEditText().getText().toString());
                    //history.sendData(song_box.getEditText().getText().toString(),artist_box.getEditText().getText().toString());
                    String url;
                    url="https://www.youtube.com/results?search_query="+song_box.getEditText().getText()+"%20"+artist_box.getEditText().getText();
                    Url.sendUrl(url);
                }
            }
        });
        addto_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(song_box.getEditText().getText().toString().equals("") && artist_box.getEditText().getText().toString().equals("")){
                    MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getView().getContext());
                    builder.setTitle("Please enter data");
                    builder.setIcon(R.drawable.ic_info_24px);
                    builder.setMessage("Do not leave the text fields empty.")
                            .setCancelable(false)
                            .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.create();
                    builder.show();
                }
                else{
                history.sendData(song_box.getEditText().getText().toString(),artist_box.getEditText().getText().toString());
                }
            }
        });
        return inflate;
    }

    public void setItem(changeItem itemId){
        this.itemId=itemId;
    }
    public void setUrl(searchUrl Url) {this.Url=Url;}
    public void setHistory(sendHistory history){this.history=history;}
    public interface changeItem {
        public void sendId(String url);
    }
    public interface searchUrl {
        public void sendUrl(String url);
    }
    public interface sendHistory{
        public void sendData(String song,String artist);
    }

}
