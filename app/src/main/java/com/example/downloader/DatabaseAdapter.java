package com.example.downloader;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DatabaseAdapter extends RecyclerView.Adapter<DatabaseAdapter.MyViewHolder> {
    private Context context;
    private List<Database> databaseList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView song, artist, dot, time;
        public MyViewHolder(View view){
            super(view);
            song=view.findViewById(R.id.song1);
            artist=view.findViewById(R.id.artist1);
            time=view.findViewById(R.id.timestamp);
            dot=view.findViewById(R.id.dot);
        }
    }
    public DatabaseAdapter(Context context, List<Database> databaseList){
        this.context=context;
        this.databaseList=databaseList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_single_row,parent,false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Database database=databaseList.get(position);
        String art="Artist: "+database.getArtist();
        String son="Song: "+database.getSong();
        holder.dot.setText(Html.fromHtml("&#8226;"));
        holder.artist.setText(art);
        holder.song.setText(son);
        holder.time.setText(formatDate(database.getTime()));
    }
    @Override
    public int getItemCount(){
        return databaseList.size();
    }
    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
