package com.example.bluetoothpayment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SendMainActivity extends ActionBarActivity implements
		OnClickListener, Runnable {

	Context mContext;
	SharedPreferences sp;
	//Editor edit;
	BluetoothAdapter Bt;
    private final int REQUEST_ENABLE_BLUETOOTH = 10;
    private final int BLUETOOTH_DURATION = 300;
    TextView textViewConnecting;
    private Thread thread;
    BluetoothServerThread BtServerThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_main);
		mContext = this;
		
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        //edit = sp.edit();

		View btnSendMainRegister = findViewById(R.id.btn_send_main_register);
		btnSendMainRegister.setOnClickListener(this);
		View btnSendMainStart = findViewById(R.id.btn_send_main_start);
		btnSendMainStart.setOnClickListener(this);
		
		textViewConnecting = (TextView)findViewById(R.id.textViewConnecting);
		
		
		//Bluetooth
		//BluetoothAdapter取得
		Bt = BluetoothAdapter.getDefaultAdapter();
		//if(!Bt.equals(null)){
		if(Bt != null){
		    //Bluetooth対応端末の場合の処理
		    Log.d("BP","Bluetoothがサポートされてます。");
			boolean btEnable = Bt.isEnabled();
		    if(btEnable == true){
		        //BluetoothがONだった場合の処理
		    }else{
		        //OFFだった場合、ONにすることを促すダイアログを表示する画面に遷移
		        Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		        startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
		    }
		}else{
		    //Bluetooth非対応端末の場合の処理
		    Log.d("BP","Bluetoothがサポートされていません。");
		    Toast.makeText(this, "Bluetoothがサポートされていません", Toast.LENGTH_LONG).show();
		    finish();
		}
		
	}

	@Override
	  public void onClick(View v) {
		Intent intent;
	    switch(v.getId()){
	    case R.id.btn_send_main_register:
		      Log.d("BP", "SendMainRegister");
		      intent = new Intent(SendMainActivity.this, RegisterCardActivity.class);
		      startActivity(intent);
		      break;
	    case R.id.btn_send_main_start:
		      Log.d("BP", "SendMainStart");
		      if(sp.getBoolean("IsCardRegistered",false)==true){
		    	//自デバイスの検出を有効にする
		          Intent discoverableOn = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		          discoverableOn.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BLUETOOTH_DURATION);
		          startActivity(discoverableOn);
	              //サーバースレッド起動、クライアントのからの要求待ちを開始
		          String cardNumber = sp.getString("CardNumber", null);
		          String validMonth = sp.getString("ValidMonth", null);
		          String validYear = sp.getString("ValidYear", null);
		          String name = sp.getString("Name",null);
		          String data = cardNumber+"&"+validMonth+"&"+validYear+"&"+name;
		          //ArrayList<String> list = new ArrayList<String>();
		          //list.add(cardNumber);
		          //list.add(validMonth);
		          //list.add(validYear);
		          //list.add(name);
		          //list.add("End of data");
	              BtServerThread = new BluetoothServerThread(this, data , Bt);
	              BtServerThread.start();
	              textViewConnecting.setText(mContext.getString(R.string.connecting));
	              thread = new Thread(this);
	              thread.start();
		    	  //Toast.makeText(this, getString(R.string.start_sending), Toast.LENGTH_LONG).show();
		      }else{
		    	  Toast.makeText(this, getString(R.string.please_register), Toast.LENGTH_SHORT).show();
		      }
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
			return true;
		}
		if (id == R.id.action_receiver) {
		      Intent intent = new Intent(SendMainActivity.this, ReceiverMainActivity.class);
		      startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int ResultCode, Intent date){
	        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
	            if(ResultCode == Activity.RESULT_OK){
	                //BluetoothがONにされた場合の処理
	                Log.d("BP","BluetoothをONにしてもらえました。");
	            }else{
	                Log.d("BP","BluetoothがONにしてもらえませんでした。");
	                finish();
	            }
	        }
	}

    @Override
    public void run() {
        try {
            //ダイアログがしっかり見えるように少しだけスリープ
            //（nnn：任意のスリープ時間・ミリ秒単位）
            Thread.sleep(BLUETOOTH_DURATION * 1000);
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
     
        	textViewConnecting.setText(" ");
        	BtServerThread.cancel();
        }
    };
}
