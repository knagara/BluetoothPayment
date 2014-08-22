package com.example.bluetoothpayment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class ReceiverMainActivity extends ActionBarActivity implements
		OnClickListener {

	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receiver_main);
		mContext = this;
		
		View btnStart = findViewById(R.id.btn_start);
		btnStart.setOnClickListener(this);
		
		
	}

	@Override
	  public void onClick(View v) {
		Intent intent;
	    switch(v.getId()){
	    case R.id.btn_start:
		      //Log.d("BP", "ReceiverProcess Start");
		      intent = new Intent(mContext, ReceiveProcessActivity.class);
		      startActivity(intent);
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
		      Intent intent = new Intent(mContext, SendMainActivity.class);
		      startActivity(intent);
			return true;
		}
		if (id == R.id.action_receiver) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
