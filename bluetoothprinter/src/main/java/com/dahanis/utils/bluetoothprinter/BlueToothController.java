package com.dahanis.utils.bluetoothprinter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/3
 * Time: 11:31
 */
class BlueToothController {
    private BluetoothAdapter bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
    private static final BlueToothController instance = new BlueToothController();

    public static final BlueToothController getInstance() {
        return instance;
    }
    /**
     * 打开蓝牙
     */
    public void openBluetooth(Activity activity) {
        if (!isOpen()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
        }
    }

    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        if (isOpen()) {
            this.bluetoothAdapter.disable();
        }
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return boolean
     */
    public boolean isOpen() {
        return this.bluetoothAdapter.isEnabled();

    }

    /**
     * 搜索蓝牙设备
     */
    public boolean searchDevices() {

        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
       return this.bluetoothAdapter.startDiscovery();
    }
}
