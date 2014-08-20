package com.example.bluetoothpayment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ReceiveMainActivity extends Activity {

	ArrayAdapter nonPairedDeviceAdapter;
	private BluetoothAdapter mBtAdapter;
	
	private final BroadcastReceiver DeviceFoundReceiver = new BroadcastReceiver(){
	    //検出されたデバイスからのブロードキャストを受ける
	    @Override
	    public void onReceive(Context context, Intent intent){
	        String action = intent.getAction();
	        String dName = null;
	        BluetoothDevice foundDevice;
	        ListView nonpairedList = (ListView)findViewById(R.id.nonPairedDeviceList);
	        if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
	            Log.d("BP","スキャン開始");
	        }
	        if(BluetoothDevice.ACTION_FOUND.equals(action)){
	            //デバイスが検出された
	            foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            if((dName = foundDevice.getName()) != null){
	                if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
	                    //接続したことのないデバイスのみアダプタに詰める
	                    nonPairedDeviceAdapter.add(dName + "\n" + foundDevice.getAddress());
	                    Log.d("ACTION_FOUND", dName);
	                }
	            }
	            nonpairedList.setAdapter(nonPairedDeviceAdapter);
	        }
	        if(BluetoothDevice.ACTION_NAME_CHANGED.equals(action)){
	            //名前が検出された
	            Log.d("ACTION_NAME_CHANGED", dName);
	            foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
	                //接続したことのないデバイスのみアダプタに詰める
	                nonPairedDeviceAdapter.add(dName + "\n" + foundDevice.getAddress());
	            }
	            nonpairedList.setAdapter(nonPairedDeviceAdapter);
	        }
	 
	        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
	            Log.d("BP","スキャン終了");
	        }
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_main);

	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		//インテントフィルターとBroadcastReceiverの登録
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(DeviceFoundReceiver, filter);
 
        nonPairedDeviceAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //接続可能なデバイスを検出
        if(mBtAdapter.isDiscovering()){
            //検索中の場合は検出をキャンセルする
            mBtAdapter.cancelDiscovery();
        }
        //デバイスを検索する
        //一定時間の間検出を行う
        mBtAdapter.startDiscovery();
	}
	
}
