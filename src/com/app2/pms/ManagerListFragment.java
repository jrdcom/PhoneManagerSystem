package com.app2.pms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ManagerListFragment extends BasicFragment {

    public ManagerListFragment(int layoutId) {
        super(layoutId);
        // TODO Auto-generated constructor stub
    }


    public  static ManagerListFragment newInstance() {
        return new ManagerListFragment(-1);
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, 
            Bundle savedInstanceState) {
        View view = inflateBasicView(inflater, container, R.layout.list_layout);
        return view;
    }
}
