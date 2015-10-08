package com.dh.foundation.utils.filemanager;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eve on 2015/9/22.
 */
public class FilePathUtil {
    /* 图片根路径  */
    public final static String getImageDirectoryPath(Context mContext) {
//
        return  "/" + mContext.getApplicationInfo().packageName +"/picture";
    }

    /*文件路径*/
    public final static String getFileDirectoryPath(Context mContext) {
//
        return  "/" + mContext.getApplicationInfo().packageName +"/files";
    }

    /*assets文件绝对路径*/
    public final static String AEESTS_ABSOLUTE_PATH = "file:///android_asset/";
    /*assets文件相对路径*/
    public final static String AEESTS_RELATIVE_PATH = "/assets/";

    /*     * 方法：getDataDirectory()
     解释：返回 File ，获取 Android 数据目录。
             * 方法：getDownloadCacheDirectory()
     解释：返回 File ，获取 Android 下载/缓存内容目录。
             * 方法：getExternalStorageDirectory()
     解释：返回 File ，获取外部存储目录即 SDCard
     * 方法：getExternalStoragePublicDirectory(String type)
     解释：返回 File ，取一个高端的公用的外部存储器目录来摆放某些类型的文件
     * 方法：getExternalStorageState()
     解释：返回 File ，获取外部存储设备的当前状态
     * 方法：getRootDirectory()
     解释：返回 File ，获取 Android 的根目录
     Android的Uri由以下三部分组成： "content://"、数据的路径、标示ID(可选)
     　　举些例子，如：
             　　　　所有联系人的Uri： content://contacts/people
             　　　　某个联系人的Uri: content://contacts/people/5
             　　　　所有图片Uri: content://media/external
             　　　　某个图片的Uri：content://media/external/images/media/4*/
   /* 所有联系人的Uri*/
    public final static String CONTACTS_PEOPLE_URI = "content://contacts/people";
    /* 所有图片Uri*/
    public final static String MEDIA_EXTERNAL_URI = "content://media/external";


    // 测试外置sd卡是否卸载，不能直接判断外置sd卡是否为null，因为当外置sd卡拔出时，仍然能得到外置sd卡路径。我这种方法是按照android谷歌测试DICM的方法，
    // 创建一个文件，然后立即删除，看是否卸载外置sd卡
    // 注意这里有一个小bug，即使外置sd卡没有卸载，但是存储空间不够大，或者文件数已至最大数，此时，也不能创建新文件。此时，统一提示用户清理sd卡吧
    private static boolean checkFsWritable(String dir) {

        if (dir == null)
            return false;

        File directory = new File(dir);

        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }

        File f = new File(directory, ".keysharetestgzc");
        try {
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;

        } catch (Exception e) {
        }
        return false;

    }

    // 返回值不带File seperater "/",如果没有外置第二个sd卡,返回null
    public static String getFirstExterPath() {
//        Log.i("TAGG","f=="+Environment.getExternalStorageDirectory().getPath());
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static String getSecondExterPath() {
        List<String> paths = getAllExterSdcardPath();

        if (paths.size() == 2) {

            for (String path : paths) {
                if (path != null && !path.equals(getFirstExterPath())) {
                    Log.i("TAGG","s=="+path);
                    return path;
                }
            }

            return null;

        } else {
            return null;
        }
    }

    /*获取系统目录*/
    public final static String getSystemPath(Context mContext) {
        return "/data/data/" + mContext.getPackageName() + "/files";
    }

    public static boolean isFirstSdcardMounted() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static boolean isSecondSDcardMounted() {
//        if(Environment.getStorageState(Environment.STORAGE_PATH_SD2).equals(Environment.MEDIA_MOUNTED))
//        {
////为true的话，外置sd卡存在
//        }
        String sd2 = getSecondExterPath();
        if (sd2 == null) {
            return false;
        }
        Log.i("TAG","isSec"+checkFsWritable(sd2 + File.separator));
        return checkFsWritable(sd2 + File.separator);

    }

    /* sdcard 路径*/
    public static String getExtPath() {
        if (isSecondSDcardMounted())
            return getSecondExterPath();
        if (isFirstSdcardMounted())
            return getFirstExterPath();
      /*  "/data/data/" + mContext.getPackageName() + "/files"*/
        return null;
    }

    public static boolean isExistExtPath() {
        if (getExtPath() == null)
            return false;
        return true;
    }

    public static List<String> getAllExterSdcardPath() {
        List<String> SdList = new ArrayList<String>();

        String firstPath = getFirstExterPath();

        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                // 将常见的linux分区过滤掉
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("media"))
                    continue;
                if (line.contains("system") || line.contains("cache")
                        || line.contains("sys") || line.contains("data")
                        || line.contains("tmpfs") || line.contains("shell")
                        || line.contains("root") || line.contains("acct")
                        || line.contains("proc") || line.contains("misc")
                        || line.contains("obb")) {
                    continue;
                }

                if (line.contains("fat") || line.contains("fuse") || (line
                        .contains("ntfs"))) {

                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        String path = columns[1];
                        if (path != null && !SdList.contains(path) && path.contains("sd"))
                            SdList.add(columns[1]);
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!SdList.contains(firstPath)) {
            SdList.add(firstPath);
        }

        return SdList;
    }
}
