package com.dh.foundation.utils.filemanager;

import android.content.Context;
import android.os.Environment;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Created by eve on 2015/9/18.目前只是整理，待测
 */
public class FileUtil {
    private static FileUtil mFileUtil;
    private RandomAccessFile RandomAccess = null;

    private FileUtil() {
    }

    public static FileUtil getInstance() {
        if (mFileUtil == null)
            mFileUtil = new FileUtil();
        return mFileUtil;
    }

    //    一、资源文件的读取：
//            1) 从resource的raw中读取文件数据：
    public static String readFromRaw(Context mContext, int rawId, String charsetName) {
        String str = "";
        try {
            //得到资源中的Raw数据流
            InputStream in = mContext.getResources().openRawResource(rawId);
            //得到数据的大小
            int length = in.available();
            byte[] buffer = new byte[length];
            //读取数据
            in.read(buffer);
            //依test.txt的编码类型选择合适的编码，如果不调整会乱码
            str = new String(buffer, charsetName);
            //关闭
            in.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //    public static void readInRaw(Context mContext,String packName,String  fileName){
//        Uri video = Uri.parse("android.resource://"+packName+"/raw/"+fileName);
//      File  file= new File(video.toString());
//        try {
//            FileInputStream      inputStream = new FileInputStream(file);
//            FileOutputStream     fos =mContext.openFileOutput("output"+fileName, Context.MODE_WORLD_READABLE);
//            byte buffer[]=new byte[1024];
//            int len=0;
//            while((len=inputStream.read(buffer))>0)
//                fos.write(buffer,0,len);fos.close();
//        }catch(Exception e){
//
//        }
//
//    };
    //    2) 从resource的asset中读取文件数据
    public static String readFromAsset(Context mContext, String fileName, String charsetName) {
        String str = "";
        try {
            //得到资源中的asset数据流
            InputStream in = mContext.getResources().getAssets().open(fileName);

            //得到数据的大小
            int length = in.available();
            byte[] buffer = new byte[length];
            //读取数据
            in.read(buffer);
            //依test.txt的编码类型选择合适的编码，如果不调整会乱码
            str = new String(buffer, charsetName);
            //关闭
            in.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //    二、读写/data/data/<应用程序名>目录上的文件:/data/data/包.名/files
//写数据
    public static void writeFile(Context mContext, String fileName, String writestr) throws IOException {
        try {
            FileOutputStream fout = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] bytes = writestr.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读数据
    public static String readFile(Context mContext, String fileName) throws IOException {
        String str = "";
        try {
            FileInputStream fin = mContext.openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            str = new String(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    //    三、读写SD卡中的文件。也就是/mnt/sdcard/目录下面的文件 ：
//写数据到SD中的文件FilePath.FIlE_DIRECTORY_PATH
//    public static void writeSdcardFile(String filePathType,String fileName,byte[] bytes) throws IOException {
//        String DirPath=FilePathUtil.EXTERNAL_DIRECTORY_PATH+filePathType+"/";
//        try {
//            FileOutputStream fout = new FileOutputStream(DirPath+fileName);
//            fout.write(bytes);
//            fout.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //读SD中的文件
//    public static String readSdcardFile(String filePathType,String fileName) throws IOException {
//        String DirPath=FilePathUtil.EXTERNAL_DIRECTORY_PATH+filePathType+"/";
//        String str  = "";
//        try {
//            FileInputStream fin = new FileInputStream(DirPath+fileName);
//            int length = fin.available();
//            byte[] buffer = new byte[length];
//            fin.read(buffer);
//            str = new String(buffer, "UTF-8");
//
//            fin.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return str ;
//    }

    //    四、使用File类进行文件的读写：
//读文件
    public static String readSDFile(String filePathType, String fileName) throws IOException {
        String dir = "";
        if (isExistSdCard())
            dir = FilePathUtil.EXTERNAL_DIRECTORY_PATH;
        else {
            dir = FilePathUtil.ROOT_DIRECTORY_PATH;
        }
        String str = "";
        File file = new File(dir + filePathType, fileName);
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte[] buffer = new byte[length];
        fis.read(buffer);
        str = new String(buffer, "UTF-8");
        fis.close();
        return str;
    }

    //写文件
    public static void writeSDFile(String filePathType, String fileName, byte[] bytes) throws IOException {
        String dir = "";
        if (isExistSdCard())
            dir = FilePathUtil.EXTERNAL_DIRECTORY_PATH;
        else {
            dir = FilePathUtil.ROOT_DIRECTORY_PATH;
        }
        File file = new File(dir + filePathType, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
    }

    //删除文件
    public void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            System.out.println("文件不存在！");
        }
    }

    //删除文件
    public void deleteCaCheFile() {
        if (isExistSdCard()) {
            File file = new File(FilePathUtil.EXTERNAL_DIRECTORY_PATH + FilePathUtil.FIlE_DIRECTORY_PATH);
            if (file.exists())
                deleteFile(file);
            File photoFile = new File(FilePathUtil.EXTERNAL_DIRECTORY_PATH + FilePathUtil.IMAGE_DIRECTORY_PATH);
            if (photoFile.exists())
                deleteFile(photoFile);
        } else {
            File file = new File(FilePathUtil.ROOT_DIRECTORY_PATH + FilePathUtil.FIlE_DIRECTORY_PATH);
            if (file.exists())
                deleteFile(file);
            File photoFile = new File(FilePathUtil.ROOT_DIRECTORY_PATH + FilePathUtil.IMAGE_DIRECTORY_PATH);
            if (photoFile.exists())
                deleteFile(photoFile);
        }
    }

    //    APK资源文件的大小不能超过1M，如果超过了怎么办？我们可以将这个数据再复制到data目录下，然后再使用。复制数据的代码如下：
    public static boolean assetsCopyData(Context mContext, String strAssetsFilePath, String strDesFilePath) {
        boolean bIsSuc = true;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = new File(strDesFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Runtime.getRuntime().exec("chmod 766 " + file);
            } catch (IOException e) {
                bIsSuc = false;
            }
        } else {//存在
            return true;
        }

        try {
            inputStream = mContext.getAssets().open(strAssetsFilePath);
            outputStream = new FileOutputStream(file);
            int nLen = 0;
            byte[] buff = new byte[1024 * 1];
            while ((nLen = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, nLen);
            }

            //完成
        } catch (IOException e) {
            bIsSuc = false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                bIsSuc = false;
            }

        }

        return bIsSuc;
    }

    public static boolean isExistSdCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public RandomAccessFile getRandomAccessFile() {
        if (RandomAccess == null)
            try {
                RandomAccess = new RandomAccessFile("foundation", "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        return RandomAccess;
    }
    private static String  getString(InputStream inputStream, String charset) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[]  buffer=new byte[512];
        int  len=0;
        while((len=inputStream.read(buffer))!=-1){
            baos.write(buffer,0,len);
        }
        byte[]  data=baos.toByteArray();
        inputStream.close();
        baos.close();
        return new String(data,charset);
    }
    public class FilePathType {
        public final static String PHOTO = FilePathUtil.IMAGE_DIRECTORY_PATH;
        public final static String FILE = FilePathUtil.FIlE_DIRECTORY_PATH;
    }
//    <!-- SDCard中创建与删除文件权限 -->
//            02.  <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
//            03. <!-- 向SDCard写入数据权限 -->
//            04. <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    //            与data相关权限
//   在应用程序AndroidManifest.xml中的manifest节点中加入
//    android:sharedUerId="android.uid.system"这个属性。
//    放在源码环境中编译，并通过adb install 的方式进行安装
//    mk文件中的属性改为LOCAL_CERTIFICATE := platform

}
