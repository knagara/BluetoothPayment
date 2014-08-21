package com.example.bluetoothpayment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class RegisterCardActivity extends Activity implements OnClickListener {

	EditText cardNumber, validMonth, validYear, name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_card);

		cardNumber = (EditText) findViewById(R.id.card_number);
		validMonth = (EditText) findViewById(R.id.valid_month);
		validYear = (EditText) findViewById(R.id.valid_year);
		name = (EditText) findViewById(R.id.name);

		View btnRegister = findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(this);
	}
	
	@Override
	protected void onResume(){
		super.onResume();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String cardNumberDefault = sp.getString("CardNumber", "");
        String validMonthDefault = sp.getString("ValidMonth", "");
        String validYearDefault = sp.getString("ValidYear", "");
        String nameDefault = sp.getString("Name", "");

        cardNumber.setText(cardNumberDefault,BufferType.NORMAL);
        validMonth.setText(validMonthDefault,BufferType.NORMAL);
        validYear.setText(validYearDefault,BufferType.NORMAL);
        name.setText(nameDefault,BufferType.NORMAL);
        
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			Log.d("BP", "CardRegister");
			boolean isOK = saveButtonClick();
			if (isOK) {
				Toast.makeText(this, getString(R.string.register_success),
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(RegisterCardActivity.this,
						SendMainActivity.class);
				startActivity(intent);
			}
			break;
		}
	}

	private boolean saveButtonClick() {
		// データ入力確認
		if (cardNumber.getText().toString().equals("")
				|| validMonth.getText().toString().equals("")
				|| validYear.getText().toString().equals("")
				|| name.getText().toString().equals("")) {

			Toast.makeText(this, getString(R.string.please_fill),
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			// 保存
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(this);
			Editor edit = sp.edit();
			edit.putString("CardNumber", cardNumber.getText().toString());
			edit.putString("ValidMonth", validMonth.getText().toString());
			edit.putString("ValidYear", validYear.getText().toString());
			edit.putString("Name", name.getText().toString());
			edit.putBoolean("IsCardRegistered", true);
			edit.commit();
			return true;
		}
	}

}
