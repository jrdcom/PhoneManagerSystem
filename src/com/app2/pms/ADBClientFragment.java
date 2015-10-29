package com.app2.pms;


import com.app2.pms.common.Configuration;
import com.app2.pms.common.LogExt;
import com.app2.pms.debug.app.DebugService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ADBClientFragment extends Fragment implements OnClickListener{
    private static final String TAG = "ADBClient";
    private DebugService mDebugService = null;
    private Button mOkButton = null;
    private Button mConnectkButton = null;
    private EditText mSetHost = null;
    private TextView mServerIp = null;
    private TextView mTextInfo = null;
    
    private Activity mActivity = null;
    private AdbClientConnect connection;
    public Handler adbMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case Configuration.MSG_UI_REMOTE_SERVER_CONNECTED :
                mTextInfo.append("\n success to connect remote adb");
                break;
            case Configuration.MSG_DISCONNECT_REMOTE_SERVER :
                mTextInfo.append("\n");
                mTextInfo.append(msg.obj.toString());
                break;
            default :
                break;
            }
        }
    };
    /*public interface Callback {
        Handler getActivityHandler();
    }*/
    public ADBClientFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view=inflater.inflate(R.layout.adbclient_layout, container, false);
        mSetHost = (EditText) view.findViewById(R.id.host);
        mOkButton = (Button) view.findViewById(R.id.bind_service);
        mConnectkButton = (Button) view.findViewById(R.id.connect_adb_service);
        mOkButton.setOnClickListener(this);
        mConnectkButton.setOnClickListener(this);
        mTextInfo = (TextView) view.findViewById(R.id.text_info);
        
        mActivity = getActivity();
        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v.getId() == R.id.bind_service) {
            String ip = mSetHost.getText().toString();
            if (TextUtils.isEmpty(ip)) {
                LogExt.d(TAG, "ip cann't be empty! ");
                return;
            } else if (!connectWifiAndServer()) {
                new AlertDialog.Builder(mActivity).setTitle("系统提示")
                .setMessage("是否进行wifi链接？")
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS"); 
                        startActivity(wifiSettingsIntent);
                        mTextInfo.append("\n 开始连接wifi！！！");
                    }
                }).setNegativeButton("返回",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        mTextInfo.append("\n 拒绝连接wifi！！！");
                    }
                }).show();
                return;
            }
            if(mDebugService != null) {
                mTextInfo.append("\n 已经绑定服务！！！");
            } else {
                startBindService(ip);
                mTextInfo.append("\n 开始绑定服务！！！");
            }
        } else {
            if(mDebugService == null) {
                mTextInfo.append("\n 请绑定服务！！！");
            }else if(mDebugService.serviceStatus){
                mTextInfo.append("\n 远程ADB服务已连接！！！");
            }else {
                mDebugService.connectAdbSerice();
            }
        }
    }
    
    private void startBindService(String ip) {
        connection = new AdbClientConnect();
        Intent intent = new Intent(new Intent(mActivity.getApplicationContext(), DebugService.class));
        intent.putExtra("ip", ip);
        intent.putExtra("port", Configuration.MANAGER_SERVER_PORT);
        boolean ret = mActivity.bindService(intent, connection, Context.BIND_AUTO_CREATE);
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
            mDebugService.setAdbHandler(adbMainHandler);
            mTextInfo.append("\n 已经绑定服务！！！");
            LogExt.d(TAG, "connected server");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (null != mDebugService) {
                mDebugService = null;
            }
            mTextInfo.append("\n 已经d断开定服务！！！");
            LogExt.d(TAG, "disconnect server");
        }
    }
    
    public boolean connectWifiAndServer() {
        WifiManager wifiManager = (WifiManager)mActivity.getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo currentWifiInfo = wifiManager.getConnectionInfo();
        if((null==currentWifiInfo.getSSID()) || (0 == currentWifiInfo.getIpAddress())) {
            return false;
        }
        return true;
    }
}
