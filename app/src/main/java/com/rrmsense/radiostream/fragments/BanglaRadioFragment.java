package com.rrmsense.radiostream.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rrmsense.radiostream.activities.MainActivity;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.adapters.RadioAdapter;
import com.rrmsense.radiostream.interfaces.OnPreparedCallback;
import com.rrmsense.radiostream.interfaces.RecyclerViewClickListener;
import com.rrmsense.radiostream.models.Radio;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class BanglaRadioFragment extends Fragment implements RecyclerViewClickListener,OnPreparedCallback {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Parcelable recyclerViewState;



    Deque<Integer> radioStations = new ArrayDeque<>();
    ArrayList<Radio> radios = new ArrayList<>();
    public BanglaRadioFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bangla_radio, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_bangla_radio);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        //radios.add(new Radio("1","2","3"));
        //mAdapter = new RadioAdapter(radios,getActivity());
        //mRecyclerView.setAdapter(mAdapter);
        updateAdapter();
        return view;
    }
    void updateAdapter(){
        radios = ((MainActivity)getContext()).radios;
        mAdapter = new RadioAdapter(radios,getActivity(),this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void recyclerViewListClicked(View v, int position) {
        //Log.d("ITEM",mRecyclerView.getChildAt(position).toString());
        Log.d("PLAYING STATE", String.valueOf(radios.get(position).isButtonPlaying()));
        mAdapter.notifyItemChanged(position);



        if(radios.get(position).isButtonPlaying()){
            resetRadioStation();
            radioStations.push(position);
            ((MainActivity)getActivity()).playRadio(radios.get(position).getStreamURL(),position,this);
        }
        else if(!radios.get(position).isButtonPlaying()){
            resetRadioStation();
            ((MainActivity)getActivity()).stopRadio();
        }
        //Log.d("Interface",position+" "+v.getId()+" "+((TextView) child.findViewById(R.id.text_title)).getText().toString());
    }
    public void resetRadioStation(){
        while (!radioStations.isEmpty()){
            radios.get(radioStations.peek()).setButtonPlaying(false);
            radios.get(radioStations.peek()).setImageEqualizer(false);
            radios.get(radioStations.peek()).setImageLoading(false);
            mAdapter.notifyItemChanged(radioStations.peek());
            radioStations.pop();
        }
    }

    @Override
    public void OnPreparedCallback(int position) {
        radios.get(position).setImageLoading(false);
        radios.get(position).setImageEqualizer(true);
        radios.get(position).setButtonPlaying(true);
        mAdapter.notifyItemChanged(position);
    }
}
