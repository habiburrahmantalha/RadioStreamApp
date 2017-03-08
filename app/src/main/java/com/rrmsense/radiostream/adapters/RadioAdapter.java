package com.rrmsense.radiostream.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.interfaces.RecyclerViewClickListener;
import com.rrmsense.radiostream.models.Radio;
import com.rrmsense.radiostream.models.SelectFragment;
import com.rrmsense.radiostream.models.Storage;

import java.util.ArrayList;

/**
 * Created by Talha on 2/12/2017.
 */

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder>{

    private ArrayList<String> radios;
    private Context mContext;
    private int CURRENT_FRAGMENT;

    private static RecyclerViewClickListener itemListener;

    public RadioAdapter(ArrayList<String> radios, Context mContext,RecyclerViewClickListener itemListener,int CURRENT_FRAGMENT) {
        this.radios = radios;
        this.mContext = mContext;
        this.itemListener = itemListener;
        this.CURRENT_FRAGMENT = CURRENT_FRAGMENT;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_radio, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Radio radio = Storage.getRadioStation(radios.get(position),mContext);

        holder.item.setText(position+"");

        if(radio.getImageURL()!="")
            Glide.with(mContext).load(radio.getImageURL()).override(200,200).fitCenter().diskCacheStrategy( DiskCacheStrategy.SOURCE ).into(holder.image_radio);

        if(radio.isLoading()){
            holder.progressBar.setVisibility(ProgressBar.VISIBLE);
        }
        else{
            holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
        }

        if(radio.isEqualizer())
            holder.equalizer.setVisibility(ImageView.VISIBLE);
        else
            holder.equalizer.setVisibility(ImageView.INVISIBLE);

        if(radio.isFavourite())
            holder.favourite.setImageResource(R.drawable.heart_yellow);
        else
            holder.favourite.setImageResource(R.drawable.heart_outline);

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
        private ProgressBar progressBar;
        private ImageView equalizer;
        private ImageButton favourite;
        private CardView cardView;
        private TextView item;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            image_radio = (ImageView) view.findViewById(R.id.image_radio);
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
            equalizer = (ImageView) view.findViewById(R.id.equalizer);
            //Glide.with(mContext).load("http://soulsearchrecords.com/media/images/levels.gif").diskCacheStrategy( DiskCacheStrategy.RESULT).into(equalizer);

            favourite = (ImageButton) view.findViewById(R.id.favourite);
            favourite.setOnClickListener(this);
            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
            item = (TextView) view.findViewById(R.id.item);

        }
        @Override
        public void onClick(View v) {
            String id = radios.get(this.getAdapterPosition());
            switch (v.getId()){
                case R.id.button_play:

                    //Log.d("Play Button", String.valueOf(this.getAdapterPosition()));
                    //Toast.makeText(mContext,"PLAY",Toast.LENGTH_SHORT).show();
                    Storage.saveStack(id,mContext);
                    Storage.setRadioStationSingleValueString("playing","id",id,mContext);
                    Storage.setRadioStationSingleValueBoolean(id,"playing",true,mContext);
                    Storage.setRadioStationSingleValueBoolean(id,"loading",true,mContext);
                    //notifyItemChanged(this.getAdapterPosition());
                    notifyItemRangeChanged(0,getItemCount());
                    itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
                    Storage.saveRecent(id,mContext);

                    break;
                case R.id.button_stop:
                    //Toast.makeText(mContext,"STOP",Toast.LENGTH_SHORT).show();
                    Storage.setRadioStationSingleValueBoolean(id,"playing",false,mContext);
                    Storage.setRadioStationSingleValueBoolean(id,"loading",false,mContext);
                    Storage.setRadioStationSingleValueBoolean(id,"equalizer",false,mContext);
                    //notifyItemChanged(this.getAdapterPosition());
                    notifyItemRangeChanged(0,getItemCount());
                    itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
                    break;
                case R.id.favourite:
                    //Toast.makeText(mContext,"Favourite",Toast.LENGTH_SHORT).show();
                    boolean f = Storage.getRadioStationSingleValueBoolean(id,"favourite",mContext);
                    if(f){
                        Storage.removeFavourite(id,mContext);
                        if(CURRENT_FRAGMENT== SelectFragment.FRAGMENT_FAVOURITE)
                            notifyItemRemoved(this.getAdapterPosition());
                        else
                            notifyItemChanged(this.getAdapterPosition());
                    }else{
                        Storage.saveFavourite(id,mContext);
                        notifyItemChanged(this.getAdapterPosition());
                    }
                    Storage.setRadioStationSingleValueBoolean(id,"favourite",!f,mContext);


                    break;
                case R.id.cardView:
                    Storage.setRadioStationSingleValueString("playing","id",id,mContext);

                    break;
            }

        }
    }
}
