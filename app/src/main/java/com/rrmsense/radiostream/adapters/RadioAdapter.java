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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.interfaces.RecyclerViewClickListener;
import com.rrmsense.radiostream.models.Radio;
import com.rrmsense.radiostream.models.Storage;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Talha on 2/12/2017.
 */

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder>{

    private ArrayList<String> radios;
    private Context mContext;
    //private String streamURL;


    private static RecyclerViewClickListener itemListener;

    public RadioAdapter(ArrayList<String> radios, Context mContext,RecyclerViewClickListener itemListener) {
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
        Radio radio = Storage.getRadioStation(radios.get(position),mContext);
        //Toast.makeText(mContext,radio.isButtonFavourite()+""+radio.isButtonPlaying()+""+radio.isImageLoading()+"",Toast.LENGTH_SHORT).show();
        //Log.d("Bool",radio.isButtonFavourite()+""+radio.isButtonPlaying()+""+radio.isImageLoading()+"");
        holder.text_title.setText(radio.getName());

        if(radio.getImageURL()!="")
            Glide.with(mContext).load(radio.getImageURL()).override(300,300).fitCenter().diskCacheStrategy( DiskCacheStrategy.SOURCE ).into(holder.image_radio);

        if(radio.isImageLoading()){
            holder.progressBar.setVisibility(ProgressBar.VISIBLE);
            holder.button_play.setVisibility(Button.INVISIBLE);
            holder.button_stop.setVisibility(Button.INVISIBLE);
        }
        else{
            holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
            if(radio.isButtonPlaying()){
                holder.button_play.setVisibility(Button.INVISIBLE);
                holder.button_stop.setVisibility(Button.VISIBLE);

            }else{
                holder.button_play.setVisibility(Button.VISIBLE);
                holder.button_stop.setVisibility(Button.INVISIBLE);
            }
        }

        if(radio.isImageEqualizer())
            holder.image_equalizer.setVisibility(ImageView.VISIBLE);
        else
            holder.image_equalizer.setVisibility(ImageView.INVISIBLE);

        if(radio.isButtonFavourite())
            holder.button_favourite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart,0,0,0);
        else
            holder.button_favourite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_outline,0,0,0);

    }
    @Override
    public int getItemCount() {

        if (radios!= null) {

            return radios.size();
        }
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image_radio;
        private TextView text_title;
        private Button button_play;
        private Button button_stop;
        private ProgressBar progressBar;
        private ImageView image_equalizer;
        protected Button button_favourite;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            image_radio = (ImageView) view.findViewById(R.id.image_radio);
            text_title = (TextView) view.findViewById(R.id.text_title);

            button_play = (Button) view.findViewById(R.id.button_play);
            button_play.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play_arrow_black_24dp,0,0,0);
            button_play.setOnClickListener(this);

            button_stop = (Button) view.findViewById(R.id.button_stop);
            button_stop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stop,0,0,0);
            button_stop.setOnClickListener(this);

            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

            image_equalizer = (ImageView) view.findViewById(R.id.image_equalizer);
            Glide.with(mContext).load("http://soulsearchrecords.com/media/images/levels.gif").diskCacheStrategy( DiskCacheStrategy.RESULT).into(image_equalizer);
            //Glide.with(mContext).load("http://www.beyzadogan.com/images/animated-sound-waves11.gif").diskCacheStrategy( DiskCacheStrategy.ALL ).into(image_equalizer);
            button_favourite = (Button) view.findViewById(R.id.button_favourite);
            button_favourite.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            String id = radios.get(this.getAdapterPosition());
            switch (v.getId()){
                case R.id.button_play:

                    //Log.d("Play Button", String.valueOf(this.getAdapterPosition()));
                    //Toast.makeText(mContext,"PLAY",Toast.LENGTH_SHORT).show();
                    Storage.saveStack(id,mContext);
                    Storage.setRadioSationSingleValue(id,"playing",true,mContext);
                    Storage.setRadioSationSingleValue(id,"loading",true,mContext);
                    //notifyItemChanged(this.getAdapterPosition());
                    notifyItemRangeChanged(0,getItemCount());
                    itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
                    Storage.saveRecent(id,mContext);

                    break;
                case R.id.button_stop:
                    //Toast.makeText(mContext,"STOP",Toast.LENGTH_SHORT).show();
                    Storage.setRadioSationSingleValue(id,"playing",false,mContext);
                    Storage.setRadioSationSingleValue(id,"loading",false,mContext);
                    Storage.setRadioSationSingleValue(id,"equalizer",false,mContext);
                    //notifyItemChanged(this.getAdapterPosition());
                    notifyItemRangeChanged(0,getItemCount());
                    itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
                    break;
                case R.id.button_favourite:
                    //Toast.makeText(mContext,"Favourite",Toast.LENGTH_SHORT).show();


                    boolean f = Storage.getRadioSationSingleValueBoolean(id,"favourite",mContext);
                    if(f){
                        Storage.removeFavourite(id,mContext);
                        notifyItemRemoved(this.getAdapterPosition());
                    }else{
                        Storage.saveFavourite(id,mContext);
                        notifyItemChanged(this.getAdapterPosition());
                    }
                    Storage.setRadioSationSingleValue(id,"favourite",!f,mContext);


                    break;
            }

        }
    }
}
