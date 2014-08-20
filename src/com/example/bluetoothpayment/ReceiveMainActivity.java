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
	    //���o���ꂽ�f�o�C�X����̃u���[�h�L���X�g���󂯂�
	    @Override
	    public void onReceive(Context context, Intent intent){
	        String action = intent.getAction();
	        String dName = null;
	        BluetoothDevice foundDevice;
	        ListView nonpairedList = (ListView)findViewById(R.id.nonPairedDeviceList);
	        if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
	            Log.d("BP","�X�L�����J�n");
	        }
	        if(BluetoothDevice.ACTION_FOUND.equals(action)){
	            //�f�o�C�X�����o���ꂽ
	            foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            if((dName = foundDevice.getName()) != null){
	                if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
	                    //�ڑ��������Ƃ̂Ȃ��f�o�C�X�̂݃A�_�v�^�ɋl�߂�
	                    nonPairedDeviceAdapter.add(dName + "\n" + foundDevice.getAddress());
	                    Log.d("ACTION_FOUND", dName);
	                }
	            }
	            nonpairedList.setAdapter(nonPairedDeviceAdapter);
	        }
	        if(BluetoothDevice.ACTION_NAME_CHANGED.equals(action)){
	            //���O�����o���ꂽ
	            Log.d("ACTION_NAME_CHANGED", dName);
	            foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            if(foundDevice.getBondState() != BluetoothDevice.BOND_BONDED){
	                //�ڑ��������Ƃ̂Ȃ��f�o�C�X�̂݃A�_�v�^�ɋl�߂�
	                nonPairedDeviceAdapter.add(dName + "\n" + foundDevice.getAddress());
	            }
	            nonpairedList.setAdapter(nonPairedDeviceAdapter);
	        }
	 
	        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
	            Log.d("BP","�X�L�����I��");
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
		
		//�C���e���g�t�B���^�[��BroadcastReceiver�̓o�^
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(DeviceFoundReceiver, filter);
 
        nonPairedDeviceAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //�ڑ��\�ȃf�o�C�X�����o
        if(mBtAdapter.isDiscovering()){
            //�������̏ꍇ�͌��o���L�����Z������
            mBtAdapter.cancelDiscovery();
        }
        //�f�o�C�X����������
        //��莞�Ԃ̊Ԍ��o���s��
        mBtAdapter.startDiscovery();
	}
	
}
