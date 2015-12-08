package com.mgl.tpvkpk.printer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mgl.tpvkpk.R;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;


public class PrintDemo extends Activity {
	private static final String TAG = PrintDemo.class.getName();
	Button btnSearch;
	Button btnSendDraw;
	Button btnSend;
	Button btnClose;
	EditText edtContext;
	EditText edtPrint;
	private static final int REQUEST_ENABLE_BT = 2;
	BluetoothService mService = null;
	BluetoothDevice con_dev = null;
	private static final int REQUEST_CONNECT_DEVICE = 1;  //��ȡ�豸��Ϣ
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mService = new BluetoothService(this, mHandler);
		//�����������˳�����
		if( mService.isAvailable() == false ){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
		}		
	}

    @Override
    public void onStart() {
    	super.onStart();
    	//����δ�򿪣�������
		if( mService.isBTopen() == false)
		{
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		try {
			btnSendDraw = (Button) this.findViewById(R.id.btn_test);
			btnSendDraw.setOnClickListener(new ClickEvent());
			btnSearch = (Button) this.findViewById(R.id.btnSearch);
			btnSearch.setOnClickListener(new ClickEvent());
			btnSend = (Button) this.findViewById(R.id.btnSend);
			btnSend.setOnClickListener(new ClickEvent());
			btnClose = (Button) this.findViewById(R.id.btnClose);
			btnClose.setOnClickListener(new ClickEvent());
			edtContext = (EditText) findViewById(R.id.txt_content);
			btnClose.setEnabled(false);
			btnSend.setEnabled(false);
			btnSendDraw.setEnabled(false);
		} catch (Exception ex) {
            Log.e("������Ϣ", ex.getMessage());
		}
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mService != null) 
			mService.stop();
		mService = null; 
	}
	
	class ClickEvent implements View.OnClickListener {
		public void onClick(View v) {
			if (v == btnSearch) {			
				Intent serverIntent = new Intent(PrintDemo.this,DeviceListActivity.class);      //��������һ����Ļ
				startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
			} else if (v == btnSend) {
                String msg = edtContext.getText().toString();
                if( msg.length() > 0 ){
                    mService.sendMessage(msg+"\n", "GBK");
                }
			} else if (v == btnClose) {
				mService.stop();
			} else if (v == btnSendDraw) {
                String msg = "";
                String lang = getString(R.string.strLang);
				//printImage();
				
            	byte[] cmd = new byte[3];
        	    cmd[0] = 0x1b;
        	    cmd[1] = 0x21;
            	if((lang.compareTo("en")) == 0){	
            		cmd[2] |= 0x10;
            		mService.write(cmd);           //���?����ģʽ
            		mService.sendMessage("Congratulations!\n", "GBK"); 
            		cmd[2] &= 0xEF;
            		mService.write(cmd);           //ȡ��ߡ�����ģʽ
            		msg = "  You have sucessfully created communications between your device and our bluetooth printer.\n\n"
                          +"  the company is a high-tech enterprise which specializes" +
                          " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";
                         

            		mService.sendMessage(msg,"GBK");
            	}else if((lang.compareTo("ch")) == 0){
            		cmd[2] |= 0x10;
            		mService.write(cmd);           //���?����ģʽ
        		    mService.sendMessage("��ϲ��\n", "GBK"); 
            		cmd[2] &= 0xEF;
            		mService.write(cmd);           //ȡ��ߡ�����ģʽ
            		msg = "  ���Ѿ��ɹ��������������ǵ�������ӡ��\n\n"
            		+ "  ����˾��һ��רҵ�����з��������������Ʊ�ݴ�ӡ�������ɨ���豸��һ��ĸ߿Ƽ���ҵ.\n\n";
            	    
            		mService.sendMessage(msg,"GBK");	
            	}
			}
		}
	}
	
    /**
     * ����һ��Handlerʵ�����ڽ���BluetoothService�෵�ػ�������Ϣ
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BluetoothService.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:   //������
                	Toast.makeText(getApplicationContext(), "Connect successful",
							Toast.LENGTH_SHORT).show();
        			btnClose.setEnabled(true);
        			btnSend.setEnabled(true);
        			btnSendDraw.setEnabled(true);
                    break;
                case BluetoothService.STATE_CONNECTING:  //��������
                	Log.d("��������", "��������.....");
                    break;
                case BluetoothService.STATE_LISTEN:     //�������ӵĵ���
                case BluetoothService.STATE_NONE:
                	Log.d("��������", "�ȴ�����.....");
                    break;
                }
                break;
            case BluetoothService.MESSAGE_CONNECTION_LOST:    //�����ѶϿ�����
                Toast.makeText(getApplicationContext(), "Device connection was lost",
						Toast.LENGTH_SHORT).show();
    			btnClose.setEnabled(false);
    			btnSend.setEnabled(false);
    			btnSendDraw.setEnabled(false);
                break;
            case BluetoothService.MESSAGE_UNABLE_CONNECT:     //�޷������豸
            	Toast.makeText(getApplicationContext(), "Unable to connect device",
						Toast.LENGTH_SHORT).show();
            	break;
            }
        }
        
    };
        
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_ENABLE_BT:      //���������
            if (resultCode == Activity.RESULT_OK) {   //�����Ѿ���
            	Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
            } else {                 //�û������������
            	finish();
            }
            break;
        case  REQUEST_CONNECT_DEVICE:     //��������ĳһ�����豸
        	if (resultCode == Activity.RESULT_OK) {   //�ѵ�������б��е�ĳ���豸��
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);  //��ȡ�б������豸��mac��ַ
                con_dev = mService.getDevByMac(address);   
                
                mService.connect(con_dev);
            }
            break;
        }
    } 
    
    //��ӡͼ��
    @SuppressLint("SdCardPath")
	private void printImage() {
    	byte[] sendData = null;
    	PrintPic pg = new PrintPic();
    	pg.initCanvas(384);     
    	pg.initPaint();
//		pg.canvas.drawTe
//		pg.drawImage(0,0, getDrawable(R.drawable.patito).get);
//		pg.canvas.drawBitmap(BitmapDrawable.createFromResourceStream(R.drawable.patito));
		pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
    	sendData = pg.printDraw();
    	mService.write(sendData);   //��ӡbyte�����

		Bitmap patito = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon_delivering);
		Bitmap resized = Bitmap.createScaledBitmap(patito, 32, 32, false);

		byte [] result = new byte[resized.getHeight() + 8];
		int [] resultInt = new int [resized.getWidth() * resized.getHeight()];
//        byte [] byteCode = new byte[resized.getWidth() * resized.getHeight()];
		result[0] = 29;
		result[1] = 118;
		result[2] = 48;
		result[3] = 0;
		result[4] = (byte)(resized.getWidth());
		result[5] = 0;
		result[6] = (byte)(resized.getHeight() % 256);
		result[7] = (byte)(resized.getHeight() / 256);

		resized.getPixels(resultInt, 0, resized.getWidth(), 0, 0, resized.getHeight() , resized.getWidth());
		int acumlatedInteger = 0;
		String acumulated = "";
		int counterArray = 8;
		byte [] strinByte = new byte[resized.getWidth()];
		Log.d(TAG, "array is: " + resultInt.length);

		for (int r = 0; r < resultInt.length; r ++){
//            Log.d(TAG, "module is " + (r % 40));
			if ( r % resized.getWidth() == 0 && acumulated.length() > 1) {

				int row = Integer.parseInt(acumulated, 2);
				result[counterArray] = (byte) row;
				counterArray++;
//                String.format("%16s", Integer.toBinaryString(1)).replace(" ", "0")
//                                    Log.d(TAG, "row:" + String.format("%32s", Integer.toBinaryString(row)).replace(" ", "0"));
				acumulated = "";
				acumlatedInteger = 0;
			} else {
//                Log.d(TAG, "" + r);
				if (resultInt[r] == -1){
					strinByte[r%resized.getWidth()] = 1;
					acumlatedInteger = acumlatedInteger + (2 ^ r);
					acumulated = acumulated + "1";
				} else {
					strinByte[r%resized.getWidth()] = 0;
					acumulated = acumulated + "0";
				}
			}
		}
    }
}
