package com.dahanis.utils.bluetoothprinter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/2
 * Time: 16:20
 */
class BlueToothService implements IBlueToothService {
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final static BlueToothService blueToothService = new BlueToothService();
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BlueToothController blueToothController = BlueToothController.getInstance();
    private List<BluetoothDevice> boundBluetoothDevice = new LinkedList<BluetoothDevice>();
    private List<BluetoothDevice> unBoundBluetoothDevice = new LinkedList<BluetoothDevice>();
    private BluetoothDevice currentBlueDevice;
    private DeviceReceiver deviceReceiver;
    private Context context;
    private DataOutputStream os;
    private DataInputStream is;
    private BluetoothSocket bluetoothSocket;
    private BluetoothServerSocket bluetoothServerSocket;
    private boolean isConnected;
    private ReadThread readThread;

    public static BlueToothService getBlueToothService() {
        return blueToothService;
    }

    private BlueToothService() {
    }

    public boolean isConnected() {
        return isConnected;
    }

    private void registerBroadCastReceiver(Context context) {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void getBluetoothDevice(Context context, DeviceReceiver deviceReceiver) {
        this.context = context;
        this.deviceReceiver = deviceReceiver;
        registerBroadCastReceiver(context);
        boundBluetoothDevice.clear();
        unBoundBluetoothDevice.clear();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        boundBluetoothDevice.addAll(pairedDevices);
        deviceReceiver.boundDeviceReceive(boundBluetoothDevice);
        deviceReceiver.unBoundDeviceReceive(unBoundBluetoothDevice);
        deviceReceiver.onFinishedReceive();
    }

    public boolean checkBluetoothEnable() {
        if (bluetoothAdapter == null) {    // Device does not support Bluetooth
            Toast.makeText(context, "您的设备不支持蓝牙功能！", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
                long startTime = System.currentTimeMillis();
                while (!bluetoothAdapter.isEnabled()) {
                    if (System.currentTimeMillis() - startTime > 10 * 1000) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    @Override
    public void refreshDevices() {
        boundBluetoothDevice.clear();
        unBoundBluetoothDevice.clear();
        makeSureBluetoothEnable();
        blueToothController.searchDevices();
    }

    private void makeSureBluetoothEnable() {
        if (!blueToothController.isOpen()) {
            checkBluetoothEnable();
        }
    }

    public void closeBluetooth() {
        blueToothController.closeBluetooth();
    }

    @Override
    public void connectDevice(BluetoothDevice bluetoothDevice, ConnectListener connectListener) throws IOException {
        makeSureBluetoothEnable();
        this.currentBlueDevice = bluetoothAdapter.getRemoteDevice(bluetoothDevice.getAddress());
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        try {
            if (!bluetoothSocket.isConnected()) {
                bluetoothSocket.connect();
            }
            isConnected = true;
        } catch (IOException e) {
            BluetoothLog.e(e);
            new Thread() {
                @Override
                public void run() {
                    try {
                        bluetoothSocket.close();
                        if (bluetoothServerSocket != null) {
                            bluetoothServerSocket.close();
                        }
                        bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("printer", uuid);
                        bluetoothSocket = bluetoothServerSocket.accept();
                    } catch (IOException e1) {
                        BluetoothLog.e(e1);
                    }
                    super.run();
                }
            }.start();
            throw e;
        }

        os = new DataOutputStream(bluetoothSocket.getOutputStream());
        is = new DataInputStream(bluetoothSocket.getInputStream());
        if (readThread != null) {
            readThread.beak = true;
        }
        readThread = new ReadThread(is);
        readThread.start();
        if (connectListener != null) {
            connectListener.onConnected(bluetoothSocket);
        }
    }

    private final class ReadThread extends Thread {
        private byte[] buffer = new byte[0x1000];
        boolean beak;
        private int rec = -1;
        private DataInputStream is;

        public ReadThread(DataInputStream inputStream) {
            this.is = inputStream;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int available = is.available();
                    if (available > 0) {
                        synchronized (this) {
                            rec = is.read(buffer, 0, available > buffer.length ? buffer.length
                                    : available);
                            BluetoothLog.i("读取打印机状态完毕！");

                        }

                    }
                } catch (IOException e) {
                    BluetoothLog.e(e);
                }
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    BluetoothLog.e(e);
                }
                if (beak) {
                    break;
                }
            }
        }

        public byte[] getReadData() {
            synchronized (this) {
                return buffer;
            }
        }

        public boolean getDataSucceed() {
            return rec > 0;
        }

        public void clear() {
            synchronized (this) {
                buffer = new byte[0x1000];
            }
            rec = -1;
        }
    }

    /**
     * 断开连接并清空内部所有状态
     */
    @Override
    public void disConnectDevice() {
        if (readThread != null) {
            readThread.beak = true;
        }
        context.unregisterReceiver(broadcastReceiver);
        if (bluetoothSocket != null) {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
                bluetoothSocket.close();
            } catch (IOException e) {
                BluetoothLog.e(e);
            }
        }
        if (bluetoothServerSocket != null) {
            try {
                bluetoothServerSocket.close();
            } catch (IOException e) {
                BluetoothLog.e(e);
            }
        }
        isConnected = false;
        boundBluetoothDevice.clear();
        unBoundBluetoothDevice.clear();
    }

    public void writePic(byte[] data) throws IOException {
        if (os != null) {
            os.write(Cmd.ESCCmd.ESC_ALT);
            os.flush();
        }
        int nBytesPerLine = 384 / 8 + 8;
        int nLinesPerTest = 1;
        nLinesPerTest = 30;
        POS_Write_Pic_Data_Safety(data, 0, data.length, nLinesPerTest * nBytesPerLine);
    }

    public void writeData(byte[] data) throws IOException {
        if (os != null) {
            os.write(Cmd.ESCCmd.ESC_ALT);
            os.flush();
        }
        int nBytesPerLine = data.length;
        int nLinesPerTest = 1;
        nLinesPerTest = 30;
        POS_Write_Data_Safety(data, 0, data.length, nLinesPerTest * nBytesPerLine);
    }


    private synchronized int POS_Write_Pic_Data_Safety(byte[] buffer, int offset,
                                                       int count, int percount) throws IOException {
        int idx = 0;
        int curcount = 0;
        byte[] reset = {0x1b, 0x40};
        byte[] precbuf = new byte[1];
        int timeout = 30 * 1000;
        if (POS_QueryStatus(precbuf, timeout)) {
            while (idx < count) {
                if (count - idx > percount)
                    curcount = percount;
                else
                    curcount = count - idx;

                os.write(buffer, offset + idx, curcount);
                os.flush();
                if (!POS_QueryStatus(precbuf, timeout))
                    break;
                else {
                    os.write(reset, 0, reset.length);
                    os.flush();
                    idx += curcount;
                }
            }
        }
        return idx;
    }

    private synchronized int POS_Write_Data_Safety(byte[] buffer, int offset,
                                                   int count, int percount) throws IOException {
        int idx = 0;
        int curcount = 0;
        byte[] reset = {0x1b, 0x40};
        byte[] precbuf = new byte[1];
        int timeout = 30 * 1000;
        if (POS_QueryStatus(precbuf, timeout)) {
            while (idx < count) {
                if (count - idx > percount)
                    curcount = percount;
                else
                    curcount = count - idx;

                os.write(buffer, offset + idx, curcount);
                os.flush();
                if (!POS_QueryStatus(precbuf, timeout))
                    break;
                else {
                    os.write(reset, 0, reset.length);
                    os.flush();
                    idx += curcount;
                }
            }
        }
        return idx;
    }

    /**
     * 使用1D 72 01这个命令，获取打印机状态。
     *
     * @param precbuf 长度为1的字节数组，存储返回的状态。
     * @param timeout
     * @return
     */

    public boolean POS_QueryStatus(byte precbuf[], int timeout) throws IOException {

        int retry;
        byte pcmdbuf[] = {0x1D, 0x72, 0x01};
        byte[] buffer = new byte[0x1000];
        int rec = -1;
        //byte pcmdbuf[] = { 0x10, 0x04, 0x01 };

        retry = 3;
        while (retry > 0) {
            retry--;
            readThread.clear();
            os.write(pcmdbuf, 0, pcmdbuf.length);
            os.flush();
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < timeout) {
                if (readThread.getDataSucceed()) {
                    return true;
                }
            }
        }

        return false;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                BluetoothLog.i("Start search BlueToothDevices");
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothLog.i("Search---founded BlueToothDevice: " + bluetoothDevice.getName() + "   " + bluetoothDevice.getAddress());
                for (BluetoothDevice bluetoothDevice1 : boundBluetoothDevice) {
                    if (bluetoothDevice1.getAddress().equals(bluetoothDevice.getAddress())) {
                        return;
                    }
                }
                for (BluetoothDevice bluetoothDevice1 : unBoundBluetoothDevice) {
                    if (bluetoothDevice1.getAddress().equals(bluetoothDevice.getAddress())) {
                        return;
                    }
                }
                if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    currentBlueDevice = bluetoothDevice;
                    boundBluetoothDevice.add(bluetoothDevice);
                } else if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    boundBluetoothDevice.add(bluetoothDevice);
                } else if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    unBoundBluetoothDevice.add(bluetoothDevice);
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                deviceReceiver.boundDeviceReceive(boundBluetoothDevice);
                deviceReceiver.unBoundDeviceReceive(unBoundBluetoothDevice);
                deviceReceiver.onFinishedReceive();
                BluetoothLog.i("Finished Search BlueToothDevices");
            }
        }
    };
}
