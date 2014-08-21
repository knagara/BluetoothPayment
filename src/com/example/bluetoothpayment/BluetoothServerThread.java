package com.example.bluetoothpayment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

public class BluetoothServerThread extends Thread {
    //サーバー側の処理
    //UUID：Bluetoothプロファイル毎に決められた値
    private final BluetoothServerSocket servSock;
    static BluetoothAdapter myServerAdapter;
    private Context mContext;
    //UUIDの生成
    public static final UUID BLUETOOTH_UUID = UUID.fromString("159D63C4-E4C1-43F8-AB06-77AC5716EBF2");
    //public ArrayList<String> dataList;
    public String data;
 
    //コンストラクタの定義
    //public BluetoothServerThread(Context context, ArrayList<String> arrayList, BluetoothAdapter btAdapter){
    public BluetoothServerThread(Context context, String string, BluetoothAdapter btAdapter){
          //各種初期化
        mContext = context;
        BluetoothServerSocket tmpServSock = null;
        myServerAdapter = btAdapter;
        data = string;
        try{
            //自デバイスのBluetoothサーバーソケットの取得
             tmpServSock = myServerAdapter.listenUsingRfcommWithServiceRecord("BlueToothSample03", BLUETOOTH_UUID);
        }catch(IOException e){
            e.printStackTrace();
        }
        servSock = tmpServSock;
    }
 
    public void run(){
        BluetoothSocket receivedSocket = null;
        while(true){
            try{
                //クライアント側からの接続要求待ち。ソケットが返される。
                receivedSocket = servSock.accept();
            }catch(IOException e){
                break;
            }
 
            if(receivedSocket != null){
                //ソケットを受け取れていた(接続完了時)の処理
            	
            	BtServerWrite thread = new BtServerWrite(mContext, receivedSocket, data);
            	thread.start();
            	
                //RwClassにmanageSocketを移す
                //ReadWriteModel rw = new ReadWriteModel(mContext, receivedSocket, myNumber);
                //rw.start();
 
                try {
                    //処理が完了したソケットは閉じる。
                    servSock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
 
    public void cancel() {
            try {
                servSock.close();
            } catch (IOException e) { }
        }
}