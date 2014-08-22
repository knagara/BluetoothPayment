package com.example.bluetoothpayment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class InputPayAmountActivity extends Activity implements OnClickListener {

	Context mContext;
	String amount, data;
	TextView amountText;
	
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.input_pay_amount);
        mContext = this;
        
        amount = "円";
        
        // インテントを取得
     	Intent intent = getIntent();
     	// インテントに保存されたデータを取得
     	data = intent.getStringExtra("data");
        
        amountText = (TextView)findViewById(R.id.amountText);

		//View btn = findViewById(R.id.button);
		//btn.setOnClickListener(this);
		View btn1 = findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		View btn2 = findViewById(R.id.button2);
		btn2.setOnClickListener(this);
		View btn3 = findViewById(R.id.button3);
		btn3.setOnClickListener(this);
		View btn4 = findViewById(R.id.button4);
		btn4.setOnClickListener(this);
		View btn5 = findViewById(R.id.button5);
		btn5.setOnClickListener(this);
		View btn6 = findViewById(R.id.button6);
		btn6.setOnClickListener(this);
		View btn7 = findViewById(R.id.button7);
		btn7.setOnClickListener(this);
		View btn8 = findViewById(R.id.button8);
		btn8.setOnClickListener(this);
		View btn9 = findViewById(R.id.button9);
		btn9.setOnClickListener(this);
		View btn0 = findViewById(R.id.button0);
		btn0.setOnClickListener(this);
		View btn00 = findViewById(R.id.button00);
		btn00.setOnClickListener(this);

		View btnConfirm = findViewById(R.id.buttonConfirm);
		btnConfirm.setOnClickListener(this);
		
    }

    @Override
    public void onClick( View v )
    {

	    switch(v.getId()){
	    case R.id.button1:
	    	setAmount("1");
	    	break;
	    case R.id.button2:
	    	setAmount("2");
	    	break;
	    case R.id.button3:
	    	setAmount("3");
	    	break;
	    case R.id.button4:
	    	setAmount("4");
	    	break;
	    case R.id.button5:
	    	setAmount("5");
	    	break;
	    case R.id.button6:
	    	setAmount("6");
	    	break;
	    case R.id.button7:
	    	setAmount("7");
	    	break;
	    case R.id.button8:
	    	setAmount("8");
	    	break;
	    case R.id.button9:
	    	setAmount("9");
	    	break;
	    case R.id.button0:
	    	setAmount("0");
	    	break;
	    case R.id.button00:
	    	setAmount("00");
	    	break;
	    case R.id.buttonConfirm:
            Intent i = new Intent(mContext, InputPinActivity.class);
            i.putExtra("data", data);
            i.putExtra("amount", amount);
            mContext.startActivity(i);
            break;
	    }
    }
    
    private void setAmount(String num){
    	amount = amount.substring(0,amount.length()-1);
    	amount = amount + num;
    	amount = amount + "円";
    	amountText.setText(amount);
    }
}
