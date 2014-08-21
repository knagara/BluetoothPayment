package com.example.bluetoothpayment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Copy_2_of_ReceiveMainActivity extends ActionBarActivity {

	private BluetoothAdapter mBtAdapter;
	private TextView mResultView;
	private String mResult = "";
	// ブロードキャストレシーバの定義
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)){
				// 見つけたデバイス情報の取得
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mResult += "Device : " + device.getName() + "/" + device.getAddress() + "\n";
				mResultView.setText(mResult);
				}
			}
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_main);

		mResultView = (TextView)findViewById(R.id.textView1);
		
		
		// インテントフィルタの作成
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// ブロードキャストレシーバの登録
		registerReceiver(mReceiver, filter);

		
		// BluetoothAdapterのインスタンス取得
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		// Bluetooth有効
		if (!mBtAdapter.isEnabled()) {
			mBtAdapter.enable();
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
		// TODO Auto-generated method stub
		super.onDestroy();
		// 検索中止
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		unregisterReceiver(mReceiver);
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
		      Intent intent = new Intent(Copy_2_of_ReceiveMainActivity.this, SendMainActivity.class);
		      startActivity(intent);
			return true;
		}
		if (id == R.id.action_receiver) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
