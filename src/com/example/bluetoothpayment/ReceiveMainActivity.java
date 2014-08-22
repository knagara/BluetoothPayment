package com.example.bluetoothpayment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReceiveMainActivity extends Activity {
	
	Context mContext;
	ListView nonpairedList;
	private List<String> dataList;
	String data;
	private List<BluetoothDevice> foundDeviceList;
	private ArrayAdapter<String> nonPairedDeviceAdapter;
	private BluetoothAdapter mBtAdapter;
	//ProgressDialog progressDialog;
	RelativeLayout linear_layout;
	TextView waitText;
	ProgressBar progressBar;
	

	private final BroadcastReceiver DeviceFoundReceiver = new BroadcastReceiver(){
	    //検出されたデバイスからのブロードキャストを受ける
	    @Override
	    public void onReceive(Context context, Intent intent){
	        String action = intent.getAction();
	        String dName = null;
	        BluetoothDevice foundDevice;
	        if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
	            Log.d("BP","スキャン開始");
	        }
	        if(BluetoothDevice.ACTION_FOUND.equals(action)){
	            //デバイスが検出された
	        	linear_layout.removeView(waitText);
	        	linear_layout.removeView(progressBar);
	            foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            foundDeviceList.add(foundDevice);
	            if((dName = foundDevice.getName()) != null){

	        	    BtClientRead thread = new BtClientRead(mContext, foundDevice, mBtAdapter);
	        	    thread.start();
	        	    //ArrayList<String> dataList = null;
	        	    data = "";
	        	    try {
						data = thread.getValue();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	        	    String[] dataArray = data.split("&");
	        	    String name = dataArray[3];
	        	    nonPairedDeviceAdapter.add(""+name);
	        	    
	                //if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
	                    //接続したことのないデバイスのみアダプタに詰める
	                    //nonPairedDeviceAdapter.add(dName + "\n" + foundDevice.getAddress());
	                    //Log.d("ACTION_FOUND", dName);
	                //}
	            }
	            //nonpairedList.setAdapter(nonPairedDeviceAdapter);
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
		
		//初期化
		mContext = (Context)this;
        nonpairedList = (ListView)findViewById(R.id.nonPairedDeviceList);
		dataList = new ArrayList<String>();
		foundDeviceList = new ArrayList<BluetoothDevice>();
		//progressDialog = new ProgressDialog(this);
		//progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		linear_layout = (RelativeLayout)findViewById(R.id.linear_layout);
		waitText = (TextView)findViewById(R.id.waitText);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);

		//インテントフィルターとBroadcastReceiverの登録
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(DeviceFoundReceiver, filter);
 
        nonPairedDeviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        nonpairedList.setAdapter(nonPairedDeviceAdapter);
        
        nonpairedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	//デバイスリスト選択時の処理
        	@Override
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(mContext, InputPayAmountActivity.class);
                i.putExtra("data", data);
                mContext.startActivity(i);
        	    //BluetoothDevice device = foundDeviceList.get(position);
        	    //BluetoothClientThread BtClientThread = new BluetoothClientThread(mContext, "Receiver", device, mBtAdapter);
        	    //BtClientThread.start();
        	    //Toast.makeText(mContext, "client thread", Toast.LENGTH_SHORT).show();
        	}
        });
        
		// BluetoothAdapterのインスタンス取得
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //接続可能なデバイスを検出
        if(mBtAdapter.isDiscovering()){
            //検索中の場合は検出をキャンセルする
            mBtAdapter.cancelDiscovery();
        }
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		// 周辺デバイスの検索開始
		mBtAdapter.startDiscovery();
		//プログレスダイアログ
		//progressDialog.setMessage("デバイスを検索しています...");
		//progressDialog.setCancelable(true);
		//progressDialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 検索中止
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		unregisterReceiver(DeviceFoundReceiver);
		}
	
}
