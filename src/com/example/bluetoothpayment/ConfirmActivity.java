package com.example.bluetoothpayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class ConfirmActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm);
		
		TextView textview = (TextView)findViewById(R.id.textView1);
		// インテントを取得
		Intent intent = getIntent();
		// インテントに保存されたデータを取得
		String data = intent.getStringExtra("data");
		textview.setText(data);
	}
}
