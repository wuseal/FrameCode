package com.dahanis.utils.bluetoothprinter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙打印工具类
 * Created By: Seal.Wu
 * Date: 2015/5/14
 * Time: 18:32
 */
public class BluetoothPrintUtil implements IBluetoothPrintUtil {

    private Context context;
    private OnPrintListener onPrintListener;
    /**
     * 是否已经初始化蓝牙连接状态
     */
    private boolean started;
    private boolean starting;//正在开启初始化中
    private BlueToothService blueToothService;
    private ArrayList<DhPrintDataBuilder> printDataBuilders = new ArrayList<DhPrintDataBuilder>();
    private List<BluetoothDevice> boundBluetoothDevices = new ArrayList<BluetoothDevice>();
    private List<BluetoothDevice> unBoundBluetoothDevices = new ArrayList<BluetoothDevice>();
    private List<BluetoothDevice> allAvailableDevices = new ArrayList<BluetoothDevice>();
    private IBlueToothService.DeviceReceiver deviceReceiver = new IBlueToothService.DeviceReceiver() {
        @Override
        public void boundDeviceReceive(List<BluetoothDevice> bluetoothDevices) {
            boundBluetoothDevices.clear();
            boundBluetoothDevices.addAll(bluetoothDevices);
        }

        @Override
        public void unBoundDeviceReceive(List<BluetoothDevice> bluetoothDevices) {
            unBoundBluetoothDevices.clear();
            unBoundBluetoothDevices.addAll(bluetoothDevices);
        }

        @Override
        public void onFinishedReceive() {
            started = true;
            starting = false;
            allAvailableDevices.clear();
            allAvailableDevices.addAll(boundBluetoothDevices);
            allAvailableDevices.addAll(unBoundBluetoothDevices);
            if (onPrintListener != null) {
                onPrintListener.onReceiveDevices(unBoundBluetoothDevices.size() == 0, allAvailableDevices);
            } else {
                throw new RuntimeException("has not set OnPrintListener at bluetoothPrintUtil.class");
            }
        }
    };
    private final static IBluetoothPrintUtil instance = new BluetoothPrintUtil();

    private BluetoothPrintUtil() {
    }

    public static IBluetoothPrintUtil getBluetoothUtil() {
        return instance;
    }


    @Override
    public void setOnPrintListener(OnPrintListener onPrintListener) {
        this.onPrintListener = onPrintListener;
    }

    @Override
    public boolean isConnected() {
        return blueToothService.isConnected();
    }

    /**
     * 执行连接设备
     *
     * @param bluetoothDevice
     */
    @Override
    public void connectDevice(BluetoothDevice bluetoothDevice) {
        new ConnectTask().execute(bluetoothDevice);
    }

    /**
     * 执行打印数据
     */
    @Override
    public void printData() {
        if (started && blueToothService != null && blueToothService.isConnected()) {
            new PrintTask().execute();
        } else {
            throw new RuntimeException("BluetoothPrintUtil has not been init,make sure that you have called method print(Context context) or showDevice(Context context) before call this method!");
        }
    }

    /**
     * @param context activity上下文
     */
    @Override
    public void print(Context context) {
        this.context = context;
        if (onPrintListener == null) {
            setOnPrintListener(new DefaultOnPrintListener(context, this));
        }
        if (started && blueToothService != null && blueToothService.isConnected() && this.context != null) {
            printData();
        } else {
            showDevices(context);
        }
    }

    /**
     * @param context activity上下文
     * @deprecated
     */
    @Override
    public void showDevices(Context context) {
        if (context == null) {
            return;
        }
        this.context = context;
        if (onPrintListener == null) {
            setOnPrintListener(new DefaultOnPrintListener(context, this));
        }
        if (!started) {
            if (!starting) {
                starting = true;
                start();
            }
        } else {
            if (onPrintListener != null) {
                onPrintListener.onReceiveDevices(unBoundBluetoothDevices.size() == 0, allAvailableDevices);
            } else {
                throw new RuntimeException("has not set OnPrintListener at bluetoothPrintUtil.class");
            }
        }
    }

    /**
     * 开启蓝牙准备状态
     */
    private void start() {
        onPrintListener.onInitBluetoothFunction();
        blueToothService = BlueToothService.getBlueToothService();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return new Boolean(blueToothService.checkBluetoothEnable());
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    blueToothService.getBluetoothDevice(context.getApplicationContext(), deviceReceiver);
                } else {
                    onPrintListener.onInitBluetoothFunctionError();
                }
            }
        }.execute();
    }

    @Override
    public void refreshDevice() {
        if (blueToothService != null) {
            blueToothService.refreshDevices();
        }
    }

    @Override
    public void closeBluetooth() {
        if (blueToothService != null) {
            blueToothService.closeBluetooth();
        }
    }

    @Override
    public void leaveCurrentActivity() {
        this.context = null;
        setOnPrintListener(null);
    }

    /**
     * 断开蓝牙连接，并复位，不清空内部数据
     */
    @Override
    public void finishBluetoothConnection() {

        started = false;//恢复成未初始化状态

        if (blueToothService != null) {
            blueToothService.disConnectDevice();
            blueToothService = null;
        }
    }

    @Override
    public void finishBluetoothFunction() {
        finishBluetoothConnection();
        context = null;
        cleanPrintDataBuilders();
        setOnPrintListener(null);
    }

    private class ConnectTask extends AsyncTask<BluetoothDevice, Integer, Void> {
        private Throwable throwable;
        private final static int CONNECTED_OK = 1;
        private final static int CONNECTED_ERROR = 2;

        @Override
        protected void onPreExecute() {
            onPrintListener.onStartConnectDevice();
        }

        @Override
        protected Void doInBackground(final BluetoothDevice... params) {
            try {
                blueToothService.connectDevice(params[0], new IBlueToothService.ConnectListener() {
                    @Override
                    public void onConnected(BluetoothSocket socket) {
                        publishProgress(CONNECTED_OK);
                    }
                });
            } catch (IOException e) {
                BluetoothLog.e(e);
                throwable = e;
                publishProgress(CONNECTED_ERROR);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0] == CONNECTED_OK) {
                onPrintListener.onConnectedDevice();
            } else if (values[0] == CONNECTED_ERROR) {
                onPrintListener.onConnectedError(throwable);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

    protected void executePrintFunction() throws IOException {
        for (int i = 0; i < printDataBuilders.size(); i++) {
            DhPrintDataBuilder dhPrintDataBuilder = printDataBuilders.get(i);
            if (DhPrintDataBuilder.TYPE_PICTURE.equals(dhPrintDataBuilder.getPrintDataType())) {
                for (byte[] data : dhPrintDataBuilder.getPrintOutData()) {
                    blueToothService.writePic(data);
                }
            } else {
                for (byte[] data : dhPrintDataBuilder.getPrintOutData()) {
                    blueToothService.writeData(data);
                }
            }
        }
    }

    @Override
    public void addPrintDataBuilders(DhPrintDataBuilder printDataBuilder) {
        printDataBuilders.add(printDataBuilder);
    }

    @Override
    public void cleanPrintDataBuilders() {
        printDataBuilders.clear();
    }

    private class PrintTask extends AsyncTask<Void, Void, Boolean> {
        private Throwable throwable;

        @Override
        protected void onPreExecute() {
            onPrintListener.onStartPrint();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                executePrintFunction();
            } catch (IOException e) {
                BluetoothLog.e(e);
                throwable = e;
                return false;
            } catch (Exception e) {
                BluetoothLog.e(e);
                throwable = e;
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if (!b) {
                onPrintListener.onPrintError(throwable);
            } else {
                onPrintListener.onSuccessPrint();
            }
        }
    }
}
