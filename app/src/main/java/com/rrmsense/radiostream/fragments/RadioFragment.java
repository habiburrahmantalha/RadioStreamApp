package com.rrmsense.radiostream.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrmsense.radiostream.activities.MainActivity;
import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.adapters.RadioAdapter;
import com.rrmsense.radiostream.interfaces.OnPreparedCallback;
import com.rrmsense.radiostream.interfaces.RecyclerViewClickListener;
import com.rrmsense.radiostream.models.SelectFragment;
import com.rrmsense.radiostream.models.Storage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioFragment extends Fragment implements RecyclerViewClickListener,OnPreparedCallback {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static int fragmentID;
    private boolean viewCreated = false;
    ArrayList<String> radios = new ArrayList<>();
    Deque<Integer> history = new ArrayDeque<>();
    public RadioFragment() {

    }
    public static RadioFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("ID", id);
        RadioFragment fragment = new RadioFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        fragmentID = bundle.getInt("ID", SelectFragment.FRAGMENT_BANGLA_RADIO);
        Log.d("ID", String.valueOf(fragmentID));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_bangla_radio);

        updateAdapter();
        viewCreated = true;
        return view;
    }
    void updateAdapter(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        switch (fragmentID){
            case SelectFragment.FRAGMENT_BANGLA_RADIO:
                radios = ((MainActivity)getContext()).banglaRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),this,SelectFragment.FRAGMENT_BANGLA_RADIO);
                //Log.d("ID", String.valueOf(fragmentID));
                break;
            case SelectFragment.FRAGMENT_INTERNATIONAL_RADIO:
                radios = ((MainActivity)getContext()).internationalRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),this,SelectFragment.FRAGMENT_INTERNATIONAL_RADIO);
                //Log.d("ID", String.valueOf(fragmentID));
                break;
            case SelectFragment.FRAGMENT_MUSIC_RADIO:
                radios = ((MainActivity)getContext()).musicRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),this,SelectFragment.FRAGMENT_MUSIC_RADIO);
                //Log.d("ID", String.valueOf(fragmentID));
                break;
            case SelectFragment.FRAGMENT_NEWS_RADIO:
                radios = ((MainActivity)getContext()).newsRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),this,SelectFragment.FRAGMENT_NEWS_RADIO);
                //Log.d("ID", String.valueOf(fragmentID));
                break;
            case SelectFragment.FRAGMENT_FAVOURITE:
                radios = Storage.getFavourite(getActivity());
                mAdapter = new RadioAdapter(radios,getActivity(),this,SelectFragment.FRAGMENT_FAVOURITE);
                break;
            case SelectFragment.FRAGMENT_RECENT:
                radios = Storage.getRecent(getActivity());
                mAdapter = new RadioAdapter(radios,getActivity(),this,SelectFragment.FRAGMENT_RECENT);
                break;
        }
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void recyclerViewListClicked(View v, int position) {
        resetRadio();
        if(position<mAdapter.getItemCount())
        if(Storage.getRadioStationSingleValueBoolean(radios.get(position),"playing",getActivity())){
            history.push(position);
            //((MainActivity)getActivity()).playRadio(radios.get(position),Storage.getRadioStationSingleValueString(radios.get(position),"stream",getActivity()),position,this);
        }
        else{
            resetRadio();
            //((MainActivity)getActivity()).stopRadio();
        }
    }
    private void resetRadio() {
        while (!history.isEmpty()){
            Storage.setRadioStationSingleValueBoolean(radios.get(history.peek()),"playing",false,getActivity());
            Storage.setRadioStationSingleValueBoolean(radios.get(history.peek()),"loading",false,getActivity());
            Storage.setRadioStationSingleValueBoolean(radios.get(history.peek()),"equalizer",false,getActivity());
            mAdapter.notifyItemChanged(history.peek());
            history.pop();
        }
    }
    @Override
    public void OnPreparedCallback(int position) {
        Storage.setRadioStationSingleValueBoolean(radios.get(position),"playing",true,getActivity());
        Storage.setRadioStationSingleValueBoolean(radios.get(position),"loading",false,getActivity());
        Storage.setRadioStationSingleValueBoolean(radios.get(position),"equalizer",true,getActivity());
        mAdapter.notifyItemChanged(position);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!viewCreated)
            return;
        if (isVisibleToUser) {
            fragmentID = ((MainActivity)getContext()).FRAGMENT;
            updateAdapter();
        } else {

        }
    }
}
