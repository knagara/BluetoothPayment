package com.example.bluetoothpayment;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ReceiveMainActivity extends ActionBarActivity {
	
	Context mContext;
	ListView nonpairedList;
	private List<String> dataList;
	private List<BluetoothDevice> foundDeviceList;
	private ArrayAdapter<String> nonPairedDeviceAdapter;
	private BluetoothAdapter mBtAdapter;
	

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
	            foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            foundDeviceList.add(foundDevice);
	            if((dName = foundDevice.getName()) != null){
	                //if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
	                    //接続したことのないデバイスのみアダプタに詰める
	                    nonPairedDeviceAdapter.add(dName + "\n" + foundDevice.getAddress());
	                    Log.d("ACTION_FOUND", dName);
	                //}
	            }
	            //nonpairedList.setAdapter(nonPairedDeviceAdapter);
	        }
	        /*
	        if(BluetoothDevice.ACTION_NAME_CHANGED.equals(action)){
	            //名前が検出された
	            Log.d("ACTION_NAME_CHANGED", dName);
	            foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            //if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
	                //接続したことのないデバイスのみアダプタに詰める
	                nonPairedDeviceAdapter.add(dName + "\n" + foundDevice.getAddress());
	            //}
	            //nonpairedList.setAdapter(nonPairedDeviceAdapter);
	        }
	        */
	 
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
        	    BluetoothDevice device = foundDeviceList.get(position);
        	    BluetoothClientThread BtClientThread = new BluetoothClientThread(mContext, "Receiver", device, mBtAdapter);
        	    BtClientThread.start();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_sender) {
		      Intent intent = new Intent(ReceiveMainActivity.this, SendMainActivity.class);
		      startActivity(intent);
			return true;
		}
		if (id == R.id.action_receiver) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
