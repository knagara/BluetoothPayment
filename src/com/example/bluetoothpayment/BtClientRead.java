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
	// �N���C�A���g���̏���
	private final BluetoothSocket clientSocket;
	private final BluetoothDevice mDevice;
	private Context mContext;
	// UUID�̐���
	public static final UUID BLUETOOTH_UUID = UUID
			.fromString("159D63C4-E4C1-43F8-AB06-77AC5716EBF2");
	static BluetoothAdapter myClientAdapter;
	// �\�P�b�g
	public static InputStream in;
	public static OutputStream out;
	// �����p�I�u�W�F�N�g
	private Object lock = new Object();
	// ���������t���O
	private boolean flag;
	// return�p�̒l
	//private ArrayList<String> valueList;
	private String data;

	// �R���X�g���N�^��`
	public BtClientRead(Context context, BluetoothDevice device,
			BluetoothAdapter btAdapter) {
		// �e�평����
		mContext = context;
		BluetoothSocket tmpSock = null;
		mDevice = device;
		myClientAdapter = btAdapter;

		try {
			// ���f�o�C�X��Bluetooth�N���C�A���g�\�P�b�g�̎擾
			tmpSock = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
			Log.d("BP","client socket �擾");
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientSocket = tmpSock;
	}

	public void run() {
		// �ڑ��v�����o���O�ɁA���������𒆒f����B
		if (myClientAdapter.isDiscovering()) {
			myClientAdapter.cancelDiscovery();
		}

		try {
			// �T�[�o�[���ɐڑ��v��
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

		// �ڑ��������̏���
		// ReadWriteModel rw = new ReadWriteModel(mContext, clientSocket,
		// myNumber);
		// rw.start();

		// �ڑ��ς݃\�P�b�g����I/O�X�g���[�������ꂼ��擾
		try {
			in = clientSocket.getInputStream();
			out = clientSocket.getOutputStream();
			Log.d("BP","client stream �擾");
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
			// �I���t���O�𗧂Ă�
			this.flag = true;
			// �Ԃ�l
			this.data = str;
			// wait()���Ă���X���b�h���N����
			this.lock.notifyAll();
		}
	}

	/**
	 * �l�擾�p�̃��\�b�h�B Thread�̏I���܂ŏ������u���b�N����
	 * 
	 * @return Thread�ŏ������ē����l
	 * @throws InterruptedException
	 *             �u���b�N���Ɋ��荞�܂ꂽ�ꍇ
	 */
	public String getValue() throws InterruptedException {
		synchronized (this.lock) {
			// �I���O�Ȃ�wait
			while (!this.flag) {
				this.lock.wait();
			}
			// �l��Ԃ�
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