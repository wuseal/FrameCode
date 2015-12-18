package com.dahanis.utils.bluetoothprinter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.util.List;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/2
 * Time: 15:35
 */
interface IBlueToothService {

    public void getBluetoothDevice(Context context, DeviceReceiver deviceReceiver);

    public void refreshDevices();

    public void connectDevice(BluetoothDevice bluetoothDevice, ConnectListener connectListener) throws IOException;

    public void disConnectDevice();

    public void writeData(byte[] data) throws IOException;

    public interface ConnectListener {
        public void onConnected(BluetoothSocket socket);
    }

    public interface DeviceReceiver {
        public void boundDeviceReceive(List<BluetoothDevice> bluetoothDevices);

        public void unBoundDeviceReceive(List<BluetoothDevice> bluetoothDevices);

        public void onFinishedReceive();

    }
}
