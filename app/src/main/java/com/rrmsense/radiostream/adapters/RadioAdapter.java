package com.rrmsense.radiostream.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rrmsense.radiostream.interfaces.AdapterCallback;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.activities.MainActivity;
import com.rrmsense.radiostream.interfaces.RecyclerViewClickListener;
import com.rrmsense.radiostream.models.Radio;
import com.rrmsense.radiostream.models.RadioPlayerSetting;

import java.util.ArrayList;

/**
 * Created by Talha on 2/12/2017.
 */

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder>{

    private ArrayList<Radio> radios;
    private Context mContext;
    //private String streamURL;

    private AdapterCallback mAdapterCallback;
    private static RecyclerViewClickListener itemListener;

    public RadioAdapter(ArrayList<Radio> radios, Context mContext,AdapterCallback callback,RecyclerViewClickListener itemListener) {
        this.radios = radios;
        this.mContext = mContext;
        this.mAdapterCallback = callback;
        this.itemListener = itemListener;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_radio, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text_title.setText(radios.get(position).getName());
        holder.button_play_stop.setTag(radios.get(position).getStreamURL());
        Glide.with(mContext).load("http://rs177.pbsrc.com/albums/w220/Tiff_Pond/Eqalizer.gif~c200").into(holder.image_gif);
        //holder.button_play_stop.setText("PLAY");
        holder.image_gif.setVisibility(ImageView.INVISIBLE);
        holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
    }
    @Override
    public int getItemCount() {
        return radios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image_radio;
        private TextView text_title;
        private Button button_play_stop;
        private ProgressBar progressBar;
        private ImageView image_gif;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            image_radio = (ImageView) view.findViewById(R.id.image_radio);
            text_title = (TextView) view.findViewById(R.id.text_title);
            button_play_stop = (Button) view.findViewById(R.id.button_play_stop);
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
            image_gif = (ImageView) view.findViewById(R.id.image_gif);

            button_play_stop.setOnClickListener(this);



        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_play_stop:
                        if(button_play_stop.getText()=="PLAY"){
                            //Uri uri = Uri.parse(streamURL);
                            Log.d("Play Button", String.valueOf(this.getAdapterPosition()));
                            Toast.makeText(mContext,"PLAY",Toast.LENGTH_SHORT).show();
                            //((MainActivity)mContext).playRadio(new RadioPlayerSetting(progressBar,image_gif,button_play_stop));
                            button_play_stop.setText("STOP");
                            itemListener.recyclerViewListClicked(v, this.getAdapterPosition());

                        }else{
                            Toast.makeText(mContext,"STOP",Toast.LENGTH_SHORT).show();
                            ((MainActivity)mContext).stopRadio();
                            button_play_stop.setText("PLAY");

                        }
                    break;
            }
        }
    }
}
