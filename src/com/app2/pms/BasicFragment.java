package com.app2.pms;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BasicFragment extends Fragment {
    public static final String SAVE_STATE = "BasicFragment.state";
    private int mState;
    private int mLayoutId;
    
    public BasicFragment(int layoutId) {
        super();
        this.mLayoutId = layoutId;
    }
    
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        
    }
    
    public View inflateBasicView(final LayoutInflater inflater, ViewGroup container,
            final int contentLayout) {
        System.out.println("BasicFragment :" + container.getId());
        final View basicFragment = inflater.inflate(R.layout.basic_layout, container, false);
        ViewGroup contentContainer = (ViewGroup) basicFragment.
                findViewById(R.id.basic_content);
        inflater.inflate(contentLayout, contentContainer, true);
        return basicFragment;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE, mState);
    }
}
