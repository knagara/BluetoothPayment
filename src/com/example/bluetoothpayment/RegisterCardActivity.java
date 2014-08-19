package com.example.bluetoothpayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class RegisterCardActivity extends Activity implements OnClickListener {

	EditText cardNumber, validMonth, validYear, yourName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_card);

		cardNumber = (EditText) findViewById(R.id.card_number);
		validMonth = (EditText) findViewById(R.id.valid_month);
		validYear = (EditText) findViewById(R.id.valid_year);
		yourName = (EditText) findViewById(R.id.your_name);
		
	    View btnRegister = findViewById(R.id.btn_register);
	    btnRegister.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
	    switch(v.getId()){
	    case R.id.btn_register:
		      Log.d("BP", "CardRegister");
		      Intent intent = new Intent(RegisterCardActivity.this, SendMainActivity.class);
		      startActivity(intent);
		      break;
	    }  
	}

}
