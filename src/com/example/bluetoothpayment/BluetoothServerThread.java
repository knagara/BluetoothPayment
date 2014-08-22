package com.example.bluetoothpayment;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

public class BluetoothServerThread extends Thread {
    //�T�[�o�[���̏���
    //UUID�FBluetooth�v���t�@�C�����Ɍ��߂�ꂽ�l
    private final BluetoothServerSocket servSock;
    static BluetoothAdapter myServerAdapter;
    private Context mContext;
    //UUID�̐���
    public static final UUID BLUETOOTH_UUID = UUID.fromString("159D63C4-E4C1-43F8-AB06-77AC5716EBF2");
    //public ArrayList<String> dataList;
    public String data;
 
    //�R���X�g���N�^�̒�`
    //public BluetoothServerThread(Context context, ArrayList<String> arrayList, BluetoothAdapter btAdapter){
    public BluetoothServerThread(Context context, String string, BluetoothAdapter btAdapter){
          //�e�평����
        mContext = context;
        BluetoothServerSocket tmpServSock = null;
        myServerAdapter = btAdapter;
        data = string;
        try{
            //���f�o�C�X��Bluetooth�T�[�o�[�\�P�b�g�̎擾
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
                //�N���C�A���g������̐ڑ��v���҂��B�\�P�b�g���Ԃ����B
            	Log.d("BP","server accept �҂�");
                receivedSocket = servSock.accept();
            }catch(IOException e){
                break;
            }
 
            if(receivedSocket != null){
                //�\�P�b�g���󂯎��Ă���(�ڑ�������)�̏���
            	Log.d("BP","server �\�P�b�g�󂯎��");
            	
            	BtServerWrite thread = new BtServerWrite(mContext, receivedSocket, data);
            	thread.start();
            	Log.d("BP","server BtServerWrite�X�^�[�g");
            	
                //RwClass��manageSocket���ڂ�
                //ReadWriteModel rw = new ReadWriteModel(mContext, receivedSocket, myNumber);
                //rw.start();
 
                try {
                    //���������������\�P�b�g�͕���B
                    servSock.close();
                	Log.d("BP","server close");
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
            	Log.d("BP","server close");
            } catch (IOException e) { }
        }
}