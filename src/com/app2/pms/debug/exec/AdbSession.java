package com.app2.pms.debug.exec;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.app2.pms.common.LogExt;
import com.app2.pms.debug.app.DebugService.AdbLocaltListener;
import com.app2.pms.debug.net.Data;
import com.esotericsoftware.kryonet.Client;

public class AdbSession {

    private static final String TAG = "app2-AdbSession";

   // private Client mRemoteClient = null;
    private WriteThread2 mRemoteWriteThread = null;

    private BlockingQueue<Data> mRemoteClientBufferExs = new LinkedBlockingQueue<Data>();

    // for connect local adb client begin
    private Socket mAdbClientSocket = null;

    private BufferedOutputStream mAdbClientOutputStream = null;
    private BufferedInputStream mAdbClientInputStream = null;

    private ReadThread mAdbClientReadThread = null;
    private WriteThread mAdbClientWriteThread = null;

    private BlockingQueue<Data> mLocalClientBufferExs = new LinkedBlockingQueue<Data>();
    //add by xuxr@tcl.com begin -->
	private AdbLocaltListener mAdbLocaltListener;
    //add by xuxr@tcl.com end <--

    public AdbSession(AdbLocaltListener l, Socket socket) {
        //mRemoteClient = remoteClient;
        mAdbLocaltListener = l;
        mAdbClientSocket = socket;
    }

    public void onRemoteClientNewData(Data data) {
        try {
            mRemoteClientBufferExs.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startSession() throws IOException {
        mAdbClientOutputStream = new BufferedOutputStream(mAdbClientSocket.getOutputStream());
        mAdbClientInputStream = new BufferedInputStream(mAdbClientSocket.getInputStream());

        mAdbClientReadThread = new ReadThread(mAdbClientInputStream, mLocalClientBufferExs, "client read thread");
        mAdbClientReadThread.start();
        mAdbClientWriteThread = new WriteThread(mAdbClientOutputStream, mRemoteClientBufferExs, "client write thread");
        mAdbClientWriteThread.start();

        mRemoteWriteThread = new WriteThread2(mAdbLocaltListener, mLocalClientBufferExs, "send to remote server thread");
        mRemoteWriteThread.start();
    }

    public void exitSession() {
        localClientClose();
    }

    private void localClientClose() {
        if (null != mAdbClientReadThread) {
            mAdbClientReadThread.setStop();
            mAdbClientReadThread = null;
        }
        if (null != mAdbClientWriteThread) {
            mAdbClientWriteThread.setStop();
            mAdbClientWriteThread = null;
        }
        if (null != mAdbClientSocket) {
            try {
                mAdbClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mAdbClientSocket = null;
        }
        if (null != mAdbClientOutputStream) {
            try {
                mAdbClientOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mAdbClientOutputStream = null;
        }

        if (null != mAdbClientInputStream) {
            try {
                mAdbClientInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mAdbClientInputStream = null;
        }

    }

    class ReadThread extends Thread {

        BufferedInputStream inputStream = null;
        BlockingQueue<Data> bufferExs = null;
        boolean stop = false;

        public void setStop() {
            stop = true;
        }

        public ReadThread(BufferedInputStream i, BlockingQueue<Data> bExs, String name) {
            super(name);
            inputStream = i;
            bufferExs = bExs;
        }

        @Override
        public void run() {
            int size = 0;
            byte[] buffer = new byte[1024];
            Data temp = null;
            try {
                while (!stop && ((size = inputStream.read(buffer)) > 0)) {
                    temp = new Data(buffer, 0, size);
                    bufferExs.put(temp);
                    LogExt.d(TAG, "received: " + temp.getString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    class WriteThread extends Thread {

        BufferedOutputStream outputStream = null;
        BlockingQueue<Data> bufferExs = null;
        boolean stop = false;

        public void setStop() {
            stop = true;
        }

        public WriteThread(BufferedOutputStream o, BlockingQueue<Data> bExs, String name) {
            super(name);
            outputStream = o;
            bufferExs = bExs;
        }

        @Override
        public void run() {
            Data buffer = null;
            try {
                while (!stop && (null != (buffer = bufferExs.take()))) {
                    LogExt.d(TAG, "write: " + buffer.getString());
                    outputStream.write(buffer.getBytes());
                    outputStream.flush();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class WriteThread2 extends Thread {

        BlockingQueue<Data> bufferExs = null;
        //Client client = null;
        AdbLocaltListener adbLocaltListener = null;
        boolean stop = false;

        public void setStop() {
            stop = true;
        }

        public WriteThread2(AdbLocaltListener listener, BlockingQueue<Data> bExs, String name) {
            super(name);
            bufferExs = bExs;
            //this.client = client;
            adbLocaltListener = listener;
        }

        @Override
        public void run() {
            Data buffer = null;
            try {
                while (!stop && (null != (buffer = bufferExs.take()))) {
                    LogExt.d(TAG, "write: " + buffer.getString());
                  //  client.sendTCP(buffer);
                    adbLocaltListener.onSendTcp(buffer);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
	
	 public interface LocalListener {
        public void onLocalListener(Data data);
    }
}
