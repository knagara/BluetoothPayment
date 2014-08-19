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
import android.widget.Toast;

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
				|| yourName.getText().toString().equals("")) {

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
			edit.putString("YourName", yourName.getText().toString());
			edit.putBoolean("IsCardRegistered", true);
			edit.commit();
			return true;
		}
	}

}
