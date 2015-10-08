package com.dh.foundation.utils.filemanager;

import android.os.Environment;

/**
 * Created by eve on 2015/9/22.
 */
public class FilePathUtil {
    /* 图片根路径  */
    public final static String IMAGE_DIRECTORY_PATH = "/picture";
    /*文件路径*/
    public final static String FIlE_DIRECTORY_PATH = "/foundation";
    /*assets文件绝对路径*/
    public final static String AEESTS_ABSOLUTE_PATH = "file:///android_asset/";
    /*assets文件相对路径*/
    public final static String AEESTS_RELATIVE_PATH = "/assets/";
    /*获取手机根目录*/
    public final static String ROOT_DIRECTORY_PATH = Environment.getRootDirectory().getAbsolutePath();
    /*获取SD卡根目录*/
    public final static String EXTERNAL_DIRECTORY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    /*获取 Android 数据目录*/
    public final static String DATA_DIRECTORY_PATH = Environment.getDataDirectory().getAbsolutePath();
    /*获取 Android 下载/缓存内容目录。*/
    public final static String DOWNLOAD_DIRECTORY_PATH = Environment.getDownloadCacheDirectory().getAbsolutePath();
    //    * 方法：getDataDirectory()
//    解释：返回 File ，获取 Android 数据目录。
//            * 方法：getDownloadCacheDirectory()
//    解释：返回 File ，获取 Android 下载/缓存内容目录。
//            * 方法：getExternalStorageDirectory()
//    解释：返回 File ，获取外部存储目录即 SDCard
//    * 方法：getExternalStoragePublicDirectory(String type)
//    解释：返回 File ，取一个高端的公用的外部存储器目录来摆放某些类型的文件
//    * 方法：getExternalStorageState()
//    解释：返回 File ，获取外部存储设备的当前状态
//    * 方法：getRootDirectory()
//    解释：返回 File ，获取 Android 的根目录
//    Android的Uri由以下三部分组成： "content://"、数据的路径、标示ID(可选)
//    　　举些例子，如：
//            　　　　所有联系人的Uri： content://contacts/people
//            　　　　某个联系人的Uri: content://contacts/people/5
//            　　　　所有图片Uri: content://media/external
//            　　　　某个图片的Uri：content://media/external/images/media/4
   /* 所有联系人的Uri*/
    public final static String CONTACTS_PEOPLE_URI = "content://contacts/people";
    /* 所有图片Uri*/
    public final static String MEDIA_EXTERNAL_URI = "content://media/external";
}
