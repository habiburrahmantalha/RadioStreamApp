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
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.interfaces.RecyclerViewClickListener;
import com.rrmsense.radiostream.models.Radio;

import java.util.ArrayList;

/**
 * Created by Talha on 2/12/2017.
 */

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder>{

    private ArrayList<Radio> radios;
    private Context mContext;
    //private String streamURL;


    private static RecyclerViewClickListener itemListener;

    public RadioAdapter(ArrayList<Radio> radios, Context mContext,RecyclerViewClickListener itemListener) {
        this.radios = radios;
        this.mContext = mContext;

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
        if(radios.get(position).getImageURL()!="")
            Glide.with(mContext).load(radios.get(position).getImageURL()).override(300,200).fitCenter().into(holder.image_radio);


        if(!radios.get(position).isButtonPlaying()){

            holder.button_play_stop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play_circle,0,0,0);
        }else{

            holder.button_play_stop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stop,0,0,0);
        }

        if(radios.get(position).isImageGif())
            holder.image_gif.setVisibility(ImageView.VISIBLE);
        else
            holder.image_gif.setVisibility(ImageView.INVISIBLE);

        Glide.with(mContext).load("http://rs177.pbsrc.com/albums/w220/Tiff_Pond/Eqalizer.gif~c200").into(holder.image_gif);

        if(radios.get(position).isImageLoading())
            holder.progressBar.setVisibility(ProgressBar.VISIBLE);
        else
            holder.progressBar.setVisibility(ProgressBar.INVISIBLE);

        if(radios.get(position).isButtonFavourite())
            holder.button_favourite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart,0,0,0);
        else
            holder.button_favourite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_outline,0,0,0);

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
        protected Button button_favourite;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            image_radio = (ImageView) view.findViewById(R.id.image_radio);
            text_title = (TextView) view.findViewById(R.id.text_title);
            button_play_stop = (Button) view.findViewById(R.id.button_play_stop);
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
            image_gif = (ImageView) view.findViewById(R.id.image_gif);
            button_favourite = (Button) view.findViewById(R.id.button_favourite);
            button_play_stop.setOnClickListener(this);
            button_favourite.setOnClickListener(this);



        }
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.button_play_stop:
                        if(!radios.get(this.getAdapterPosition()).isButtonPlaying()){
                            //Log.d("Play Button", String.valueOf(this.getAdapterPosition()));
                            Toast.makeText(mContext,"PLAY",Toast.LENGTH_SHORT).show();
                            radios.get(this.getAdapterPosition()).setButtonPlaying(true);
                            radios.get(this.getAdapterPosition()).setImageLoading(true);
                            itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
                        }else {
                            Toast.makeText(mContext,"STOP",Toast.LENGTH_SHORT).show();
                            radios.get(this.getAdapterPosition()).setButtonPlaying(false);
                            radios.get(this.getAdapterPosition()).setImageGif(false);
                            itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
                        }
                    break;
                case R.id.button_favourite:
                    Toast.makeText(mContext,"Favourite",Toast.LENGTH_SHORT).show();

                    radios.get(this.getAdapterPosition()).setButtonFavourite(!radios.get(this.getAdapterPosition()).isButtonFavourite());
                    notifyItemChanged(this.getAdapterPosition());
                    break;
            }
        }
    }
}
