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
	    //���o���ꂽ�f�o�C�X����̃u���[�h�L���X�g���󂯂�
	    @Override
	    public void onReceive(Context context, Intent intent){
	        String action = intent.getAction();
	        String dName = null;
	        BluetoothDevice foundDevice;
	        if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
	            Log.d("BP","�X�L�����J�n");
	        }
	        if(BluetoothDevice.ACTION_FOUND.equals(action)){
	            //�f�o�C�X�����o���ꂽ
	            foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            foundDeviceList.add(foundDevice);
	            if((dName = foundDevice.getName()) != null){
	                //if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
	                    //�ڑ��������Ƃ̂Ȃ��f�o�C�X�̂݃A�_�v�^�ɋl�߂�
	                    nonPairedDeviceAdapter.add(dName + "\n" + foundDevice.getAddress());
	                    Log.d("ACTION_FOUND", dName);
	                //}
	            }
	            //nonpairedList.setAdapter(nonPairedDeviceAdapter);
	        }
	        /*
	        if(BluetoothDevice.ACTION_NAME_CHANGED.equals(action)){
	            //���O�����o���ꂽ
	            Log.d("ACTION_NAME_CHANGED", dName);
	            foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            //if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
	                //�ڑ��������Ƃ̂Ȃ��f�o�C�X�̂݃A�_�v�^�ɋl�߂�
	                nonPairedDeviceAdapter.add(dName + "\n" + foundDevice.getAddress());
	            //}
	            //nonpairedList.setAdapter(nonPairedDeviceAdapter);
	        }
	        */
	 
	        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
	            Log.d("BP","�X�L�����I��");
	        }
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive_main);
		
		//������
		mContext = (Context)this;
        nonpairedList = (ListView)findViewById(R.id.nonPairedDeviceList);
		dataList = new ArrayList<String>();
		foundDeviceList = new ArrayList<BluetoothDevice>();

		//�C���e���g�t�B���^�[��BroadcastReceiver�̓o�^
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(DeviceFoundReceiver, filter);
 
        nonPairedDeviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        nonpairedList.setAdapter(nonPairedDeviceAdapter);
        
        nonpairedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	//�f�o�C�X���X�g�I�����̏���
        	@Override
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        	    BluetoothDevice device = foundDeviceList.get(position);
        	    BluetoothClientThread BtClientThread = new BluetoothClientThread(mContext, "Receiver", device, mBtAdapter);
        	    BtClientThread.start();
        	    //Toast.makeText(mContext, "client thread", Toast.LENGTH_SHORT).show();
        	}
        });
        
		// BluetoothAdapter�̃C���X�^���X�擾
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //�ڑ��\�ȃf�o�C�X�����o
        if(mBtAdapter.isDiscovering()){
            //�������̏ꍇ�͌��o���L�����Z������
            mBtAdapter.cancelDiscovery();
        }
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		// ���Ӄf�o�C�X�̌����J�n
		mBtAdapter.startDiscovery();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �������~
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
