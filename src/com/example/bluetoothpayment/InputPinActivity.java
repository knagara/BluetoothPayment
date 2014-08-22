package com.example.bluetoothpayment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class InputPinActivity extends Activity implements OnClickListener, Runnable{

	Context mContext;
	String amount, data;
	//TextView amountText;
    private static ProgressDialog waitDialog;
    private Thread thread;
	
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.input_pin);
        mContext = this;
        
        amount = "";
        
        // インテントを取得
     	Intent intent = getIntent();
     	// インテントに保存されたデータを取得
     	data = intent.getStringExtra("data");
     	amount = intent.getStringExtra("amount");
        
        //amountText = (TextView)findViewById(R.id.amountText);
        //amountText.setText(amount);

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

		View btnConfirm = findViewById(R.id.buttonConfirm);
		btnConfirm.setOnClickListener(this);
		
    }

    @Override
    public void onClick( View v )
    {

	    switch(v.getId()){
	    case R.id.button1:
	    	break;
	    case R.id.button2:
	    	break;
	    case R.id.button3:
	    	break;
	    case R.id.button4:
	    	break;
	    case R.id.button5:
	    	break;
	    case R.id.button6:
	    	break;
	    case R.id.button7:
	    	break;
	    case R.id.button8:
	    	break;
	    case R.id.button9:
	    	break;
	    case R.id.button0:
	    	break;
	    case R.id.buttonConfirm:
	    	setWait();
            break;
	    }
    }
    private void setWait(){
        // プログレスダイアログの設定
        waitDialog = new ProgressDialog(this);
        // プログレスダイアログのメッセージを設定します
        waitDialog.setMessage("認証中...");
        // 円スタイル（くるくる回るタイプ）に設定します
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // プログレスダイアログを表示
        waitDialog.show();
     
        thread = new Thread(this);
        /* show()メソッドでプログレスダイアログを表示しつつ、
         * 別スレッドを使い、裏で重い処理を行う。
         */
        thread.start();
    }
    @Override
    public void run() {
        try {
            //ダイアログがしっかり見えるように少しだけスリープ
            //（nnn：任意のスリープ時間・ミリ秒単位）
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //スレッドの割り込み処理を行った場合に発生、catchの実装は割愛
        }
        //run内でUIの操作をしてしまうと、例外が発生する為、
        //Handlerにバトンタッチ
        handler.sendEmptyMessage(0);
    }
     
    private Handler handler = new Handler() {
        public void handleMessage(Message msg){
            // HandlerクラスではActivityを継承してないため
            // 別の親クラスのメソッドにて処理を行うようにした。
            //YYY();
     
            // プログレスダイアログ終了
            waitDialog.dismiss();
            waitDialog = null;
            
            nextIntent();
        }
    };
    
    private void nextIntent(){
        Intent i = new Intent(mContext, ConfirmActivity.class);
        i.putExtra("data", data);
        i.putExtra("amount", amount);
        mContext.startActivity(i);
    }
}
