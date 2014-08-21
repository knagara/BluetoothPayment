package com.example.bluetoothpayment;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

public class BluetoothClientThread extends Thread {
    //クライアント側の処理
    private final BluetoothSocket clientSocket;
    private final BluetoothDevice mDevice;
    private Context mContext;
    //UUIDの生成
    public static final UUID BLUETOOTH_UUID = UUID.fromString("159D63C4-E4C1-43F8-AB06-77AC5716EBF2");
    static BluetoothAdapter myClientAdapter;
    public String myNumber;
 
    //コンストラクタ定義
    public BluetoothClientThread(Context context, String myNum , BluetoothDevice device, BluetoothAdapter btAdapter){
        //各種初期化
        mContext = context;
        BluetoothSocket tmpSock = null;
        mDevice = device;
        myClientAdapter = btAdapter;
        myNumber = myNum;
 
        try{
            //自デバイスのBluetoothクライアントソケットの取得
            tmpSock = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
        }catch(IOException e){
            e.printStackTrace();
        }
        clientSocket = tmpSock;
    }
 
    public void run(){
        //接続要求を出す前に、検索処理を中断する。
        if(myClientAdapter.isDiscovering()){
            myClientAdapter.cancelDiscovery();
        }
 
        try{
            //サーバー側に接続要求
            clientSocket.connect();
        }catch(IOException e){
             try {
                 clientSocket.close();
             } catch (IOException closeException) {
                 e.printStackTrace();
             }
             return;
        }
 
        //接続完了時の処理
        ReadWriteModel rw = new ReadWriteModel(mContext, clientSocket, myNumber);
        rw.start();
    }
 
    public void cancel() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
      }
}