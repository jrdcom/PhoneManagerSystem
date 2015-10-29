package com.app2.pms.common;

public class Configuration {
    public static final String MANAGER_SERVER_IP = "10.128.180.43";

    public static final int MANAGER_SERVER_PORT = 6666;
    public static final int ADB_SERVER_PORT = 9999;
    
    public static final String ADBD_TCP_SYSTEM_PROPERTY = "service.adb.tcp.port";

    public static final String CMD_RETURN_TAG = "RETURN-";
    public static final String CMD_CONNECT_ADB = "cmd_connect_adb";
    public static final String CMD_LIST_DEVICES = "cmd_list_devices";
    public static final String CMD_SET_DEVICE = "cmd_set_device";

    public static final String CMD_RETRUN_LIST_DEVICES = CMD_RETURN_TAG + CMD_LIST_DEVICES;
    public static final String CMD_RETRUN_SET_DEVICE = CMD_RETURN_TAG + CMD_SET_DEVICE;
    public static final String CMD_RETURN_CONNECT_ADB = CMD_RETURN_TAG + CMD_CONNECT_ADB;

    public static final String SEG = "#@#";

    public static final String TYPE_PC_TERMINAL = "type_pc_terminal";
    public static final String TYPE_PHONE_CLIENT = "type_phone_client";

    public static final int CONFIGURATION_PER_DATA_SIZE = 1024;

    // for connect remote server begin
    public static final int MSG_CONNECT_REMOTE_SERVER = 1;
    public static final int MSG_CONNECT_LOCAL_ADBD = 2;

    public static final int MSG_UI_REMOTE_SERVER_CONNECTED = 1;
    public static final int MSG_UI_CMD_START_ADB = 2;
    // with param start ok or fail
    public static final int MSG_UI_START_ADB = 3;
    public static final int MSG_UI_END_ADB = 4;
    
    public static final int FLAG_START_ADB_SUCCESS = 1;
    public static final int FLAG_START_ADB_FAIL = 2;

    public static final int MSG_DISCONNECT_REMOTE_SERVER = 5;
    public static final int MSG_SHOW_INFO = 7;
}
