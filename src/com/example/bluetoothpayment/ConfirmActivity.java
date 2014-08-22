package com.example.bluetoothpayment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ConfirmActivity extends ActionBarActivity implements OnClickListener {
	
	String data, amount;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm);
		mContext = this;

		TextView textName = (TextView)findViewById(R.id.textName);
		TextView textAmount = (TextView)findViewById(R.id.textAmount);

        // インテントを取得
     	Intent intent = getIntent();
     	// インテントに保存されたデータを取得
     	data = intent.getStringExtra("data");
     	amount = intent.getStringExtra("amount");

     	if(data != null){
		    String[] dataArray = data.split("&");
		    String name = dataArray[3];
			textName.setText(name);
     	}
		textAmount.setText(amount);

		View btnBack = findViewById(R.id.buttonBack);
		btnBack.setOnClickListener(this);
	}

    @Override
    public void onClick( View v )
    {

	    switch(v.getId()){
	    case R.id.buttonBack:
            Intent i = new Intent(mContext, ReceiveMainActivity.class);
            mContext.startActivity(i);
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
		      Intent intent = new Intent(this, SendMainActivity.class);
		      startActivity(intent);
			return true;
		}
		if (id == R.id.action_receiver) {
		      Intent intent = new Intent(this, ReceiveMainActivity.class);
		      startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
