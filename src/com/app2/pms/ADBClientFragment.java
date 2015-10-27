package com.app2.pms;


import com.app2.pms.common.Configuration;
import com.app2.pms.common.IPv4v6Utils;
import com.app2.pms.common.LogExt;
import com.app2.pms.debug.app.DebugService;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class ADBClientFragment extends Fragment implements OnClickListener{
    private static final String TAG = "ADBClient";
    private DebugService mDebugService = null;
    private Button mOkButton = null;
    private EditText mSetHost = null;
    private TextView mServerIp = null;
    private TextView mOutTextView = null;
    
    private Activity mActivity = null;
    private AdbClientConnect connection;
    public interface Callback {
        void onImageButton(View v);
    }
    public ADBClientFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view=inflater.inflate(R.layout.adbclient_layout, container, false);
        mSetHost = (EditText) view.findViewById(R.id.host);
        mOkButton = (Button) view.findViewById(R.id.ok);
        mOkButton.setOnClickListener(this);
        mServerIp = (TextView) view.findViewById(R.id.serverIp);
        mServerIp.setText(IPv4v6Utils.getLocalIPAddress());
        mOutTextView = (TextView) view.findViewById(R.id.outPut);
        
        mActivity = getActivity();
        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String ip = mSetHost.getText().toString();
        if (TextUtils.isEmpty(ip)) {
            LogExt.d(TAG, "ip cann't be empty! ");
            return;
        } /*else if (null == mDebugService) {
            LogExt.d(TAG, "server is not exist or bound");
            return;
        }*/
        startAndBindService(ip);
    }
    
    private void startAndBindService(String ip) {
       // mActivity.startService(new Intent(mActivity, DebugService.class));
        connection = new AdbClientConnect();
        Intent intent = new Intent(new Intent(mActivity.getApplicationContext(), DebugService.class));
        Bundle bundle = new Bundle();
        bundle.putString("ip", ip);
        bundle.putInt("port", Configuration.MANAGER_SERVER_PORT);
        intent.putExtra("ip-port", bundle);
        boolean ret = mActivity.bindService(intent, connection, mActivity.BIND_AUTO_CREATE);
        LogExt.d(TAG, "bindService ret = " + ret);
    }
    
    @Override
    public void onDestroy() {
        mActivity.unbindService(connection);
        mDebugService = null;
        super.onDestroy();
    }


    
    public class AdbClientConnect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDebugService = ((DebugService.ExchangeBinder) service).getService();
            LogExt.d(TAG, "connected server");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (null != mDebugService) {
                mDebugService = null;
            }
        }
    }
}
