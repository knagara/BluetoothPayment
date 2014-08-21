package com.example.bluetoothpayment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

public class BtServerWrite extends Thread {
    //ソケットに対するI/O処理
 
    public static InputStream in;
    public static OutputStream out;
    //private ArrayList<String> dataList;
    private String data;
    private Context mContext;
 
    //コンストラクタの定義
    //public BtServerWrite(Context context, BluetoothSocket socket, ArrayList<String> arrayList){
    public BtServerWrite(Context context, BluetoothSocket socket, String string){
        data = string;
        mContext = context;
 
        try {
            //接続済みソケットからI/Oストリームをそれぞれ取得
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
 
    public void write(byte[] buf){
        //Outputストリームへのデータ書き込み
        try {
            out.write(buf);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    public void run() {
        try {
        	//for(int i=0;i<dataList.size();i++){
        		write(data.getBytes("UTF-8"));
        		Log.d("BluetoothPayment","data write "+data);
        	//}
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
