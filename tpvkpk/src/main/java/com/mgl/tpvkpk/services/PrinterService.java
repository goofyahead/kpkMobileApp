package com.mgl.tpvkpk.services;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mgl.tpvkpk.R;
import com.mgl.tpvkpk.base.TpvKpkApplication;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.util.Set;

import javax.inject.Inject;

import kpklib.api.KaprikaApiInterface;
import kpklib.models.CartItem;
import kpklib.models.PrintableOrder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by goofyahead on 10/2/15.
 */
public class PrinterService extends Service {
    private static final String TAG = PrinterService.class.getName();
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private boolean canPrint;
    @Inject
    KaprikaApiInterface api;

    private Runnable requestTickets = new Runnable() {
        @Override
        public void run() {
            if (canPrint) {
                api.getOrdersToPrint(new Callback<PrintableOrder>() {
                    @Override
                    public void success(PrintableOrder printableOrder, Response response) {
                        if (printableOrder != null) {

                            Bitmap patito = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon_delivering);
                            Bitmap resized = Bitmap.createScaledBitmap(patito, 80, 80, false);

                            PrintPic pg = new PrintPic();
                            pg.initCanvas(384);
                            pg.initPaint();
                            pg.canvas.drawBitmap(resized, 0, 0, null);

                            Paint textPaint = new Paint();
                            textPaint.setAntiAlias(true);
                            textPaint.setTextAlign(Paint.Align.LEFT);
                            textPaint.setColor(Color.BLACK);
                            textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                            textPaint.setTextSize(40);
                            pg.canvas.drawText("hola   ....", 0, 120, textPaint);

                            pg.length = 160;

                            mService.write(pg.printDraw());

//                            pg.canvas.drawBitmap(icon, null, dest, null);

//                            byte [] data = pg.printDraw();

//                            Log.d(TAG, data.toString());

                            // draw the canvas itself


                            Log.d(TAG, "last order is" + printableOrder.getAmount());
                            if (canPrint) {
                                Log.d(TAG, "printing:");


                                for (CartItem item : printableOrder.getItemList().values()) {
                                    mService.sendMessage(item.getItem().getName() + item.getItem().getPrice() + " x " + item.getQuantity(), "GBK");
                                    for (String option : item.getOptions().keySet()){
                                        mService.sendMessage(option + " : " + item.getOptions().get(option), "GBK");
                                    }
                                }

                                mService.sendMessage("--------------------------------", "GBK");
                                mService.sendMessage("Total: " + printableOrder.getAmount(), "GBK");


                            } else {
                                Log.d(TAG, "printer not ready");
                            }
                        } else {
                            Log.d(TAG, "No new orders to print");
                        }

                        mServiceHandler.postDelayed(requestTickets, 5 * 1000);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "error: " + error.getMessage());
                        mServiceHandler.postDelayed(requestTickets, 5 * 1000);
                    }
                });

            } else {
                Log.d(TAG, "Printer not ready yet!");
            }
        }
    };


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        ((TpvKpkApplication)getApplicationContext()).inject(this);

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                HandlerThread.NORM_PRIORITY);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        mService = new BluetoothService(this, mHandler);

        if( !mService.isAvailable() ) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        if( !mService.isBTopen() ) {
            Toast.makeText(this, "Bluetooth is OFF", Toast.LENGTH_LONG).show();
        }

        Set<BluetoothDevice> pairedDevices = mService.getPairedDev();

        for(BluetoothDevice device : pairedDevices){
            Log.d(TAG, "already paired to: " + device.getName() + " : " + device.getAddress() + " Bonded: " + device.getBondState());
            mService.connect(device);

            Log.d(TAG, "connecting to it");
//            mService.sendMessage("Hello world!", "GBK");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service printing starting", Toast.LENGTH_SHORT).show();

        mServiceHandler.postDelayed(requestTickets, 5 * 1000);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        if (mService != null)
            mService.stop();
        mService = null;
    }


    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //������
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            canPrint = true;
//                            btnClose.setEnabled(true);
//                            btnSend.setEnabled(true);
//                            btnSendDraw.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:  //��������
                            Log.d(TAG, "Connecting.....");
                            break;
                        case BluetoothService.STATE_LISTEN:     //�������ӵĵ���
                        case BluetoothService.STATE_NONE:
                            Log.d(TAG, "NONE.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //�����ѶϿ�����
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
//                    btnClose.setEnabled(false);
//                    btnSend.setEnabled(false);
//                    btnSendDraw.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //�޷������豸
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };
}
