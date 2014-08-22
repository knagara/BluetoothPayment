package com.example.bluetoothpayment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

public class BtClientRead extends Thread {
	// クライアント側の処理
	private final BluetoothSocket clientSocket;
	private final BluetoothDevice mDevice;
	private Context mContext;
	// UUIDの生成
	public static final UUID BLUETOOTH_UUID = UUID
			.fromString("159D63C4-E4C1-43F8-AB06-77AC5716EBF2");
	static BluetoothAdapter myClientAdapter;
	// ソケット
	public static InputStream in;
	public static OutputStream out;
	// 同期用オブジェクト
	private Object lock = new Object();
	// 処理完了フラグ
	private boolean flag;
	// return用の値
	//private ArrayList<String> valueList;
	private String data;

	// コンストラクタ定義
	public BtClientRead(Context context, BluetoothDevice device,
			BluetoothAdapter btAdapter) {
		// 各種初期化
		mContext = context;
		BluetoothSocket tmpSock = null;
		mDevice = device;
		myClientAdapter = btAdapter;

		try {
			// 自デバイスのBluetoothクライアントソケットの取得
			tmpSock = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
			Log.d("BP","client socket 取得");
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientSocket = tmpSock;
	}

	public void run() {
		// 接続要求を出す前に、検索処理を中断する。
		if (myClientAdapter.isDiscovering()) {
			myClientAdapter.cancelDiscovery();
		}

		try {
			// サーバー側に接続要求
			clientSocket.connect();
			Log.d("BP","client socket connect");
		} catch (IOException e) {
			try {
				clientSocket.close();
			} catch (IOException closeException) {
				e.printStackTrace();
			}
			return;
		}

		// 接続完了時の処理
		// ReadWriteModel rw = new ReadWriteModel(mContext, clientSocket,
		// myNumber);
		// rw.start();

		// 接続済みソケットからI/Oストリームをそれぞれ取得
		try {
			in = clientSocket.getInputStream();
			out = clientSocket.getOutputStream();
			Log.d("BP","client stream 取得");
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = new byte[1024];
		String str = null;
		int tmpBuf = 0;

		//while (true) {
			try {
				tmpBuf = in.read(buf);
				Log.d("BP","client in.read(buf)");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (tmpBuf != 0) {
				try {
					str = new String(buf, "UTF-8");
	        		Log.d("BP","client data read ");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		//}
		synchronized (this.lock) {
			// 終了フラグを立てる
			this.flag = true;
			// 返り値
			this.data = str;
			// wait()しているスレッドを起こす
			this.lock.notifyAll();
		}
	}

	/**
	 * 値取得用のメソッド。 Threadの終了まで処理をブロックする
	 * 
	 * @return Threadで処理して得た値
	 * @throws InterruptedException
	 *             ブロック中に割り込まれた場合
	 */
	public String getValue() throws InterruptedException {
		synchronized (this.lock) {
			// 終了前ならwait
			while (!this.flag) {
				this.lock.wait();
			}
			// 値を返す
			return this.data;
		}
	}

	public void cancel() {
		try {
			clientSocket.close();
			Log.d("BP","client close");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}