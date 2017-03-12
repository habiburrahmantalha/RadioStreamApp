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
import com.rrmsense.radiostream.activities.MainActivity;
import com.rrmsense.radiostream.interfaces.RecyclerViewClickListener;
import com.rrmsense.radiostream.models.Radio;
import com.rrmsense.radiostream.models.SelectFragment;
import com.rrmsense.radiostream.models.Storage;

import java.util.ArrayList;

/**
 * Created by Talha on 2/12/2017.
 */

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {

    private static RecyclerViewClickListener itemListener;
    private ArrayList<String> radios;
    private Context mContext;
    private int CURRENT_FRAGMENT;

    public RadioAdapter(ArrayList<String> radios, Context mContext, RecyclerViewClickListener itemListener, int CURRENT_FRAGMENT) {
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
        Radio radio = Storage.getRadioStation(radios.get(position), mContext);

        holder.item.setText((position+1) + "");

        if (radio.getImageURL() != "")
            Glide.with(mContext).load(radio.getImageURL()).override(300, 200).fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.image_radio);

        switch (radio.getState()) {
            case Radio.LOADING:
                //holder.progressBar.setVisibility(ProgressBar.VISIBLE);
                holder.equalizer.setVisibility(ImageView.INVISIBLE);
                break;

            case Radio.PLAYING:
                holder.equalizer.setVisibility(ImageView.VISIBLE);
                //holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
                break;

            case Radio.STOPPED:
                holder.equalizer.setVisibility(ImageView.INVISIBLE);
                //holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
                break;
        }
        if (radio.isFavourite())
            holder.favourite.setImageResource(R.drawable.heart_outline);
        else
            holder.favourite.setImageResource(R.drawable.heart_yellow);

    }

    @Override
    public int getItemCount() {

        if (radios != null) {

            return radios.size();
        } else return 0;
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
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            equalizer = (ImageView) view.findViewById(R.id.equalizer);
            favourite = (ImageButton) view.findViewById(R.id.favourite);
            favourite.setOnClickListener(this);
            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
            item = (TextView) view.findViewById(R.id.item);
        }

        @Override
        public void onClick(View v) {
            String id = radios.get(this.getAdapterPosition());
            switch (v.getId()) {
                case R.id.favourite:
                    boolean f = Storage.getRadioStationSingleValueBoolean(id, "favourite", mContext);
                    if (f) {
                        Storage.removeFavourite(id, mContext);
                        if (CURRENT_FRAGMENT == SelectFragment.FRAGMENT_FAVOURITE)
                            notifyItemRemoved(this.getAdapterPosition());
                        else
                            notifyItemChanged(this.getAdapterPosition());
                    } else {
                        Storage.saveFavourite(id, mContext);
                        notifyItemChanged(this.getAdapterPosition());
                    }
                    Storage.setRadioStationSingleValueBoolean(id, "favourite", !f, mContext);
                    break;
                case R.id.cardView:
                    if(Storage.getValue("playing_id",mContext).equals(id)){
                        ((MainActivity)mContext).resumePlay();
                    }else{
                        Storage.saveState(id, Radio.LOADING, mContext);
                        Storage.setRadioStationSingleValueString("playing", "id", id, mContext);
                        Storage.saveRecent(id, mContext);

                    }
                    notifyItemRangeChanged(0, getItemCount());
                    itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
                    break;
            }
        }
    }
}
