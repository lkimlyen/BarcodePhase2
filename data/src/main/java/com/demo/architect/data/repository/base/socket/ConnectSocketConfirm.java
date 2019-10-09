package com.demo.architect.data.repository.base.socket;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.SocketRespone;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ConnectSocketConfirm extends AsyncTask<Void, Void, SocketRespone> {
    private final String TAG = ConnectSocketConfirm.class.getName();
    String ipAddress;
    int port;
    private Socket mSocket;
    private long id;
    private onPostExecuteResult listener;

    public ConnectSocketConfirm(String addr, int port, long id, onPostExecuteResult listener) {
        ipAddress = addr;
        this.port = port;
        this.id = id;
        this.listener = listener;
    }

    @Override
    protected SocketRespone doInBackground(Void... voids) {
        SocketRespone result = null;
        try {
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress(ipAddress, port), 3000);
            byte[] buffer = new byte[2048];

            int red = -1;
            byte[] redData;
            StringBuilder clientData = new StringBuilder();
            String redDataText;
            red = mSocket.getInputStream().read(buffer);
            for (int i = 1; i < red; i++) {
                redData = new byte[red];
                System.arraycopy(buffer, 0, redData, 0, red);
                redDataText = new String(redData, "US-ASCII"); // assumption that client sends data UTF-8 encoded
                clientData.append(redDataText);
            }
            Log.e(TAG, "handShake " + clientData.toString());

            if (!clientData.toString().isEmpty()) {
                if(id > 0){
                    sendEvent();
                }
                Log.e(TAG, "Send Success " + id);
                result = new SocketRespone(Constants.SUCCESS, Constants.CONNECTED);
            } else {
                result = new SocketRespone(Constants.FAIL, Constants.NOCONNECT);
            }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = new SocketRespone(Constants.FAIL, Constants.NOCONNECT);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = new SocketRespone(Constants.FAIL, Constants.NOCONNECT);

        }
        return result;
    }

    private void sendEvent() {
        try {
            if (mSocket != null) {
                byte[] tmp = toByteArray(true, 2, id);
                mSocket.getOutputStream().write(tmp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(SocketRespone socketRespone) {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            socketRespone = new SocketRespone(Constants.FAIL, Constants.DISCONNECT);
            Log.e("Client:" + " Sent Print: ", " Fail");
        }
        listener.onPostExecute(socketRespone);
        super.onPostExecute(socketRespone);
    }

    public interface onPostExecuteResult {
        void onPostExecute(SocketRespone respone);
    }

    public byte[] toByteArray(boolean isEvent, int player, long key) throws UnsupportedEncodingException {
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.clear();
        String tmp = String.valueOf(isEvent) + "|" + player + "|" + key;
        buffer.put(tmp.getBytes("US-ASCII"));
        return buffer.array();
    }
}
