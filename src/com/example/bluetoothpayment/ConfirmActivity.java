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
		// �C���e���g���擾
		Intent intent = getIntent();
		// �C���e���g�ɕۑ����ꂽ�f�[�^���擾
		String data = intent.getStringExtra("data");
		textview.setText(data);
	}
}
