package com.example.downloader;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;


public class InfoFragment extends Fragment {
    final static String ARG_SONG="song";
    final static String ARG_ARTIST="artist";

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }
    private DatabaseAdapter databaseAdapter;
    private List<Database> databaseList=new ArrayList<>();
    private TextView noHistory;
    private TextView clearHistory;
    private Toolbar toolbar;
    private DatabaseHelper db;
    private MainActivity mainActivity;
    int i=1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v=inflater.inflate(R.layout.info_fragment, container, false);
        final RecyclerView recyclerView=(RecyclerView) v.findViewById(R.id.recycler_view);
        noHistory=(TextView) v.findViewById(R.id.empty);
        clearHistory=(TextView) v.findViewById(R.id.textView2);
        toolbar=(Toolbar)v.findViewById(R.id.toolbar);
        db =new DatabaseHelper(getActivity());
        databaseList.clear();
        databaseList.addAll(db.getAllDatabase());
        databaseAdapter=new DatabaseAdapter(getActivity(),databaseList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDec(getActivity(),LinearLayoutManager.VERTICAL,16));
        recyclerView.setAdapter(databaseAdapter);
        toggleEmpty();
        recyclerView.addOnItemTouchListener(new RecyclerViewListener(getActivity().getApplicationContext(), recyclerView, new RecyclerViewListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getView().getContext());
                builder.setTitle("Are you sure?");
                builder.setIcon(R.drawable.ic_info_24px);
                builder.setMessage("Do you want to clear the history?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAll();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.about:
                        Intent intent=new Intent(getActivity(),About.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        i--;
        Bundle arg=getArguments();
        if(arg!=null && i==0){
            createDatabase(arg.getString(ARG_SONG),arg.getString(ARG_ARTIST));
        }
    }

    private void createDatabase(String song, String artist){
        long id=db.insertDatabase(song, artist);
        Database n=db.getDatabase(id);
        if(n!=null){
            // adding new note to array list at 0 position
            databaseList.add(0,n);
            //Refreshing list
            databaseAdapter.notifyDataSetChanged();
            toggleEmpty();
        }

    }
    public void updateDatabase(String song, String artist, int pos){
        Database n=databaseList.get(pos);
        //updating
        n.setSong(song);
        n.setArtist(artist);
        //refreshing list
        databaseList.set(pos,n);
        databaseAdapter.notifyItemChanged(pos);
        toggleEmpty();
    }
    private void deleteDatabase(int position){
        db.deleteContent(databaseList.get(position));
        //removing
        databaseList.remove(position);
        databaseAdapter.notifyItemRemoved(position);
        toggleEmpty();
    }
    private void deleteAll(){
        Database database=new Database();
        RecyclerView recyclerView=(RecyclerView) getActivity().findViewById(R.id.recycler_view);
        db.deleteAll(database);
        databaseAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.GONE);
        toggleEmpty();
    }
    private void showActionsDialog(final int position){
        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Do you want delete?");
        builder.setIcon(R.drawable.ic_info_24px);
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDatabase(position);
            }
        });
        builder.create();
        builder.show();
    }
    private void toggleEmpty(){
        if(db.getDatabaseCount()>0){
            noHistory.setVisibility(View.GONE);
            clearHistory.setVisibility(View.VISIBLE);
        }
        else {
            noHistory.setVisibility(View.VISIBLE);
            clearHistory.setVisibility(View.GONE);
        }
    }


}
