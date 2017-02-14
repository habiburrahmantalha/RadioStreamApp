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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rrmsense.radiostream.activities.MainActivity;
import com.rrmsense.radiostream.interfaces.AdapterCallback;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.adapters.RadioAdapter;
import com.rrmsense.radiostream.interfaces.RecyclerViewClickListener;
import com.rrmsense.radiostream.models.Radio;
import com.rrmsense.radiostream.models.RadioPlayerSetting;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class BanglaRadioFragment extends Fragment implements AdapterCallback,RecyclerViewClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Parcelable recyclerViewState;


    ArrayList<Radio> radios = new ArrayList<>();
    Deque<Integer> radioStations = new ArrayDeque<>();

    public BanglaRadioFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bangla_radio, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_bangla_radio);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://www.rrmelectronics.com/appserver/RadioStreamLink.php", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Log.d("JSON",response.toString());
                for(int i=0;i < response.length();i++){
                    //Log.d("JSON",response.get(i).toString());
                    Gson gson = new Gson();
                    Radio r = null;
                    try {
                        r = gson.fromJson(response.get(i).toString(), Radio.class);
                        radios.add(r);
                        Log.d("JSON",r.getName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                updateAdapter();
            }
        });
        recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        //radios.add(new Radio("1","2","3"));
        //mAdapter = new RadioAdapter(radios,getActivity());
        //mRecyclerView.setAdapter(mAdapter);

        return view;
    }
    void updateAdapter(){
        mAdapter = new RadioAdapter(radios,getActivity(),this,this);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onMethodCallback() {
        View child;
        for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
            child = mRecyclerView.getChildAt(i);
            ((TextView) child.findViewById(R.id.text_title)).setText("t");
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        //Log.d("ITEM",mRecyclerView.getChildAt(position).toString());
        resetRadioStation();
        radioStations.push(position);

        ((MainActivity)getActivity()).playRadio(radios.get(position).getStreamURL());

        //Log.d("Interface",position+" "+v.getId()+" "+((TextView) child.findViewById(R.id.text_title)).getText().toString());

    }
    public void resetRadioStation(){
        while (!radioStations.isEmpty()){
            /*
            View child = radioStations.peek();
            ((ProgressBar) child.findViewById(R.id.progressBar)).setVisibility(ProgressBar.INVISIBLE);
            ((ImageView) child.findViewById(R.id.image_gif)).setVisibility(ProgressBar.INVISIBLE);
            ((Button) child.findViewById(R.id.button_play_stop)).setText("PLAY");
            */
            //mAdapter.notifyItemChanged(radioStations.peek());
            radioStations.pop();
        }
    }
}
