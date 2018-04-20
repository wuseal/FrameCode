package com.dh.foundation.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.dh.foundation.utils.DLoggerUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 未捕捉异常接收器
 * Created By: Seal.Wu
 * Date: 2015/5/4
 * Time: 18:02
 */
public class UnCatchExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Context context;
    //系统默认的UncaughtException处理类
    private final Thread.UncaughtExceptionHandler mDefaultHandler;
    //用来存储设备信息和异常信息
    private final Map<String, String> infos = new HashMap<String, String>();

    public UnCatchExceptionHandler(Context context) {
        this.context = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //退出程序
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        DLoggerUtils.e(ex);
        //收集设备参数信息
        collectDeviceInfo(context);
        //保存日志文件
        saveCrashInfo2File(ex);
        return false;
    }

    /**
     * 收集设备参数信息
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            DLoggerUtils.e(e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                DLoggerUtils.e(e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E", Locale.CHINA).format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            String path;
            boolean hasWritePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && hasWritePermission) {
                path = Environment.getExternalStorageDirectory().getPath() + "/" + ApplicationUtil.getPackageName();
            } else {
                path = context.getCacheDir() + "/" + ApplicationUtil.getAppName();
            }
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    DLoggerUtils.e(new RuntimeException("创建文件夹:" + path + "失败"));
                }
            }
            File file = new File(dir, fileName);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(sb.toString().getBytes());
                    fos.close();
                } else {
                    DLoggerUtils.e(new RuntimeException("创建文件" + path + "/" + fileName + "失败"));
                }
            }

            return fileName;
        } catch (Exception e) {
            DLoggerUtils.e(e);
        }
        return null;
    }
}
