package com.app2.pms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShowFragment extends BasicFragment {

    public ShowFragment(int layoutId) {
        super(layoutId);
        // TODO Auto-generated constructor stub
    }


    public  static ShowFragment newInstance() {
        return new ShowFragment(-1);
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, 
            Bundle savedInstanceState) {
        View view = inflateBasicView(inflater, container, R.layout.show_layout);
        return view;
    }
}
