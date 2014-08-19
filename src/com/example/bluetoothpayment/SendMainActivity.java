package com.example.bluetoothpayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class SendMainActivity extends ActionBarActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_main);

		View btnSendMainRegister = findViewById(R.id.btn_send_main_register);
		btnSendMainRegister.setOnClickListener(this);
		View btnSendMainStart = findViewById(R.id.btn_send_main_start);
		btnSendMainStart.setOnClickListener(this);
	}

	@Override
	  public void onClick(View v) {
	    switch(v.getId()){
	    case R.id.btn_send_main_register:
		      Log.d("BP", "SendMainRegister");
		      Intent intent = new Intent(SendMainActivity.this, RegisterCardActivity.class);
		      startActivity(intent);
		      break;
	    case R.id.btn_send_main_start:
		      Log.d("BP", "SendMainStart");
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
