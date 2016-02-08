package com.mgl.tpvkpk.services;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
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
    BluetoothService cashierPrinterService = null;
    BluetoothService kitchenPrinterService = null;
    BluetoothDevice con_dev = null;
    private String kitchenMac = "00:19:5D:25:1A:33";
    private String cashierMac = "00:19:5D:25:19:CB";

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

                            printOrder(printableOrder);

                            // to home for printing
                            Log.d(TAG, "sending broadcast to home to print from activity");
                            sendMessage(printableOrder);

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


    private void printOrder(PrintableOrder printableOrder) {
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
        pg.canvas.drawText("KAPRIKA GO", 0, 120, textPaint);
        pg.length = 130;

        cashierPrinterService.write(pg.printDraw());
        cashierPrinterService.sendMessage("Plaza Atenas 2, 28224", "GBK");
        cashierPrinterService.sendMessage("Tel: 91 351 22 22", "GBK");
        cashierPrinterService.sendMessage("CIF B85456937", "GBK");

        kitchenPrinterService.sendMessage("------------------------------", "GBK");
        kitchenPrinterService.sendMessage("Order: " + printableOrder.getNonce().substring(printableOrder.getNonce().length() - 4), "GBK");
        kitchenPrinterService.sendMessage("------------------------------", "GBK");

        cashierPrinterService.sendMessage("------------------------------", "GBK");
        cashierPrinterService.sendMessage("Order: " + printableOrder.getNonce().substring(printableOrder.getNonce().length() - 4, printableOrder.getNonce().length()), "GBK");
        cashierPrinterService.sendMessage("------------------------------", "GBK");

        Log.d(TAG, "last order is" + printableOrder.getAmount());
        if (canPrint) {
            Log.d(TAG, "printing:");
            String itemName;
            for (CartItem item : printableOrder.getItemList()) {
                itemName = item.getItem().getName().length() > 20 ? item.getItem().getName().substring(0, 20) : item.getItem().getName();
                cashierPrinterService.sendMessage(item.getQuantity() + "x " + String.format("%-21s", itemName) + String.format("%7s", String.format("%.2f", item.getItem().getPrice()) + "e"), "GBK");

                if (item.getItem().isKitchen()) {
                    kitchenPrinterService.sendMessage("\n", "GBK");
                    kitchenPrinterService.sendMessage(item.getQuantity() + "x " +  item.getItem().getName(), "GBK");
                }
                for (String option : item.getOptions().keySet()) {
                    cashierPrinterService.sendMessage(option + " : " + item.getOptions().get(option), "GBK");
                    if (item.getItem().isKitchen())
                    kitchenPrinterService.sendMessage(option + " : " + item.getOptions().get(option), "GBK");
                }
            }

            cashierPrinterService.sendMessage("--------------------------------", "GBK");
            cashierPrinterService.sendMessage("Total: " + printableOrder.getAmount() + " e", "GBK");
            cashierPrinterService.sendMessage("--------------------------------", "GBK");
            cashierPrinterService.sendMessage("\n", "GBK");

            if (printableOrder.getDeliveryOption().equalsIgnoreCase("PICK_UP")) {
                cashierPrinterService.sendMessage("A RECOGER: ", "GBK");
                cashierPrinterService.sendMessage(printableOrder.getAddress().getName(), "GBK");
            } else {
                cashierPrinterService.sendMessage("A DOMICILIO: ", "GBK");
                cashierPrinterService.sendMessage(printableOrder.getAddress().getName(), "GBK");
                cashierPrinterService.sendMessage(printableOrder.getAddress().getStreet(), "GBK");
                cashierPrinterService.sendMessage(printableOrder.getAddress().getFloor(), "GBK");
                cashierPrinterService.sendMessage(printableOrder.getAddress().getExtraDeliver(), "GBK");
                cashierPrinterService.sendMessage(printableOrder.getAddress().getPhone(), "GBK");
            }
            cashierPrinterService.sendMessage("\n", "GBK");
            kitchenPrinterService.sendMessage("\n", "GBK");
            kitchenPrinterService.sendMessage("------------------------------", "GBK");
            kitchenPrinterService.sendMessage("\n", "GBK");

        } else {
            Log.d(TAG, "printer not ready");
        }
    }

    // Send an Intent with an action named "my-event".
    private void sendMessage(PrintableOrder order) {
        Intent intent = new Intent("my-event");
        // add data
        intent.putExtra("message", order);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

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

        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("re-print"));
        HandlerThread thread = new HandlerThread("ServiceStartArguments", HandlerThread.NORM_PRIORITY);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        cashierPrinterService = new BluetoothService(this, mHandler);
        kitchenPrinterService = new BluetoothService(this, kitchenHandler);

        if( !cashierPrinterService.isAvailable() ) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        if( !cashierPrinterService.isBTopen() ) {
            Toast.makeText(this, "Bluetooth is OFF", Toast.LENGTH_LONG).show();
        }


        Set<BluetoothDevice> pairedDevices2 = kitchenPrinterService.getPairedDev();
//        Log.d(TAG, "kitchen printer device " + kitchenPrinterService.getDevByMac(kitchenMac).getName());
//
//        cashierPrinterService.connect(cashierPrinterService.getDevByMac(cashierMac));
//        kitchenPrinterService.connect(kitchenPrinterService.getDevByMac(kitchenMac));

        for(BluetoothDevice device : pairedDevices2){
            Log.d(TAG, "already paired to: " + device.getName() + " : " + device.getAddress() + " Bonded: " + device.getBondState());
//            if (device.getAddress().equalsIgnoreCase(cashierMac)) cashierPrinterService.connect(device);
            if (device.getAddress().equalsIgnoreCase(kitchenMac)) kitchenPrinterService.connect(device);

            Log.d(TAG, "connecting to it");
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        if (cashierPrinterService != null)
            cashierPrinterService.stop();
        cashierPrinterService = null;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            PrintableOrder message = (PrintableOrder) intent.getSerializableExtra("message");
            printOrder(message);
        }
    };

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //������
                            Log.d(TAG, "Connected to cashier sucessfully");
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            canPrint = true;
//                            btnClose.setEnabled(true);
//                            btnSend.setEnabled(true);
//                            btnSendDraw.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:  //��������
                            Log.d(TAG, "Connecting to cashier.....");
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
                    Log.d(TAG, "Unable to connect to cashier device");
                    Toast.makeText(getApplicationContext(), "Unable to connect to cashier device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    private final Handler kitchenHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //������
                            Set<BluetoothDevice> pairedDevices = cashierPrinterService.getPairedDev();
                            for(BluetoothDevice device : pairedDevices){
                                Log.d(TAG, "already paired to: " + device.getName() + " : " + device.getAddress() + " Bonded: " + device.getBondState());
                                if (device.getAddress().equalsIgnoreCase(cashierMac)) cashierPrinterService.connect(device);
//            if (device.getAddress().equalsIgnoreCase(kitchenMac)) kitchenPrinterService.connect(device);

                                Log.d(TAG, "connecting to it");
                            }
                            Log.d(TAG, "Connect successful to kitchen");
                            Toast.makeText(PrinterService.this, "Connect successful to kitchen",
                                    Toast.LENGTH_SHORT).show();
                            canPrint = true;
//                            btnClose.setEnabled(true);
//                            btnSend.setEnabled(true);
//                            btnSendDraw.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:  //��������
                            Log.d(TAG, "Connecting to kitchen.....");
                            break;
                        case BluetoothService.STATE_LISTEN:     //�������ӵĵ���
                        case BluetoothService.STATE_NONE:
                            Log.d(TAG, "NONE.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //�����ѶϿ�����
                    Toast.makeText(getApplicationContext(), "Device kitchen connection was lost",
                            Toast.LENGTH_SHORT).show();
//                    btnClose.setEnabled(false);
//                    btnSend.setEnabled(false);
//                    btnSendDraw.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //�޷������豸
                    Log.d(TAG, "Unable to connect kitchen device");
                    Toast.makeText(getApplicationContext(), "Unable to connect kitchen device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };
}
