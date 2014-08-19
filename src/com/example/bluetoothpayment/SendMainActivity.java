package com.example.bluetoothpayment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SendMainActivity extends ActionBarActivity implements
		OnClickListener {

	SharedPreferences sp;
	//Editor edit;
    private final int REQUEST_ENABLE_BLUETOOTH = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_main);
		
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        //edit = sp.edit();

		View btnSendMainRegister = findViewById(R.id.btn_send_main_register);
		btnSendMainRegister.setOnClickListener(this);
		View btnSendMainStart = findViewById(R.id.btn_send_main_start);
		btnSendMainStart.setOnClickListener(this);
		
		//Bluetooth
		//BluetoothAdapter�擾
		BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
		if(!Bt.equals(null)){
		    //Bluetooth�Ή��[���̏ꍇ�̏���
		    Log.d("BP","Bluetooth���T�|�[�g����Ă܂��B");
		}else{
		    //Bluetooth��Ή��[���̏ꍇ�̏���
		    Log.d("BP","Bluetooth���T�|�[�g��Ă��܂���B");
		    Toast.makeText(this, "Bluetooth���T�|�[�g��Ă��܂���", Toast.LENGTH_LONG).show();
		    finish();
		}
		boolean btEnable = Bt.isEnabled();
	    if(btEnable == true){
	        //Bluetooth��ON�������ꍇ�̏���
	    }else{
	        //OFF�������ꍇ�AON�ɂ��邱�Ƃ𑣂��_�C�A���O��\�������ʂɑJ��
	        Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	        startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
	    }
	}

	@Override
	  public void onClick(View v) {
		Intent intent;
	    switch(v.getId()){
	    case R.id.btn_send_main_register:
		      Log.d("BP", "SendMainRegister");
		      intent = new Intent(SendMainActivity.this, RegisterCardActivity.class);
		      startActivity(intent);
		      break;
	    case R.id.btn_send_main_start:
		      Log.d("BP", "SendMainStart");
		      if(sp.getBoolean("IsCardRegistered",false)==true){
		    	  // TODO
		    	  //intent = new Intent(SendMainActivity.this, RegisterCardActivity.class);
			      //startActivity(intent);
		      }else{
		    	  Toast.makeText(this, getString(R.string.please_register), Toast.LENGTH_SHORT).show();
		      }
		      break;
	    }  
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
			return true;
		}
		if (id == R.id.action_receiver) {
			// TODO
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int ResultCode, Intent date){
	        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
	            if(ResultCode == Activity.RESULT_OK){
	                //Bluetooth��ON�ɂ��ꂽ�ꍇ�̏���
	                Log.d("BP","Bluetooth��ON�ɂ��Ă��炦�܂����B");
	            }else{
	                Log.d("BP","Bluetooth��ON�ɂ��Ă��炦�܂���ł����B");
	                finish();
	            }
	        }
	}
}
