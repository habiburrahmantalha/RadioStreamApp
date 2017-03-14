package com.rrmsense.radiostream.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrmsense.radiostream.R;
import com.rrmsense.radiostream.activities.MainActivity;
import com.rrmsense.radiostream.adapters.RadioAdapter;
import com.rrmsense.radiostream.interfaces.NotifyItem;
import com.rrmsense.radiostream.models.SelectFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioFragment extends Fragment implements NotifyItem {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static int fragmentID;

    ArrayList<String> radios = new ArrayList<>();

    public RadioFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        fragmentID = bundle.getInt("ID", SelectFragment.FRAGMENT_BANGLA_RADIO);
        ((MainActivity)getActivity()).notifyItemChanged(this);
        Log.d("ID", String.valueOf(fragmentID));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_bangla_radio);
        updateAdapter();
        return view;
    }
    void updateAdapter(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        switch (fragmentID){
            case SelectFragment.FRAGMENT_BANGLA_RADIO:
                radios = ((MainActivity)getContext()).banglaRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),SelectFragment.FRAGMENT_BANGLA_RADIO);
                //Log.d("ID", String.valueOf(fragmentID));
                break;
            case SelectFragment.FRAGMENT_INTERNATIONAL_RADIO:
                radios = ((MainActivity)getContext()).internationalRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),SelectFragment.FRAGMENT_INTERNATIONAL_RADIO);
                //Log.d("ID", String.valueOf(fragmentID));
                break;
            case SelectFragment.FRAGMENT_MUSIC_RADIO:
                radios = ((MainActivity)getContext()).musicRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),SelectFragment.FRAGMENT_MUSIC_RADIO);
                //Log.d("ID", String.valueOf(fragmentID));
                break;
            case SelectFragment.FRAGMENT_NEWS_RADIO:
                radios = ((MainActivity)getContext()).newsRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),SelectFragment.FRAGMENT_NEWS_RADIO);
                //Log.d("ID", String.valueOf(fragmentID));
                break;
            case SelectFragment.FRAGMENT_FAVOURITE:
                radios = ((MainActivity)getContext()).favouriteRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),SelectFragment.FRAGMENT_FAVOURITE);
                break;
            case SelectFragment.FRAGMENT_RECENT:
                radios = ((MainActivity)getContext()).recentRadios;
                mAdapter = new RadioAdapter(radios,getActivity(),SelectFragment.FRAGMENT_RECENT);
                break;
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void OnItemChanged(String id) {
        int position = -1;
        position = radios.indexOf(id);
        if(position>=0)
        mAdapter.notifyItemChanged(position);
    }
}
