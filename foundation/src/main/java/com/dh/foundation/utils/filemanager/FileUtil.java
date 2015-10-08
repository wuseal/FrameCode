package com.dh.foundation.utils.filemanager;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

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
    private static Context mContext;

    private FileUtil() {
    }

    public static FileUtil getInstance(Context context) {
        mContext = context;
        initRootDirectory(mContext);
        if (mFileUtil == null)
            mFileUtil = new FileUtil();
        return mFileUtil;
    }

    private static void initRootDirectory(Context context) {
        String dir = FilePathUtil.getExtPath();
        if (FilePathUtil.isExistExtPath()) {
            String dirFile = dir + FilePathUtil.getFileDirectoryPath(context);
            if (!new File(dirFile).exists())
                new File(dirFile).mkdirs();
            String dirPhoto = dir + FilePathUtil.getImageDirectoryPath(context);
            if (!new File(dirPhoto).exists())
                new File(dirPhoto).mkdirs();
        }
    }

    //    一、资源文件的读取：
//            1) 从resource的raw中读取文件数据：
    public byte[] readFromRaw(int rawId) {
        byte[] buffer = null;
        try {
            //得到资源中的Raw数据流
            InputStream in = mContext.getResources().openRawResource(rawId);
            //得到数据的大小
            int length = in.available();
            buffer = new byte[length];
            //读取数据
            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public byte[] readFromAsset(String fileName) {
        byte[] buffer = null;
        try {
            //得到资源中的asset数据流
            InputStream in = mContext.getResources().getAssets().open(fileName);

            //得到数据的大小
            int length = in.available();
            buffer = new byte[length];
            //读取数据
            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    二、读写/data/data/<应用程序名>目录上的文件:/data/data/包.名/files
//写数据
    public void writeFileToSys(String fileName, byte[] bytes) throws IOException {
        if(getAvailableInternalMemorySize()<bytes.length)
        {
            Log.e("TAG","系统内部可用空间不足");
            return;
        }
        try {
            FileOutputStream fout = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读数据
    public byte[] readFileFromSys(String fileName) throws IOException {
        byte[] buffer = null;
        try {
            FileInputStream fin = mContext.openFileInput(fileName);
            int length = fin.available();
            buffer = new byte[length];
            fin.read(buffer);
            fin.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /*根据名称获取文件*/
    private File getFile(int filePathType, String fileName) {
        String dir = "";
        if (FilePathType.PHOTO == filePathType)
            dir = FilePathUtil.getImageDirectoryPath(mContext);
        else {
            dir = FilePathUtil.getFileDirectoryPath(mContext);
        }
        if (FilePathUtil.isExistExtPath()) {
            if (fileName != null)
                return new File(FilePathUtil.getExtPath() + dir, fileName);
            else {
                return new File(FilePathUtil.getExtPath() + dir);
            }
        } else {
            if (fileName != null)
                return new File(FilePathUtil.getSystemPath(mContext), fileName);
            else {
                return new File(FilePathUtil.getSystemPath(mContext));
            }
        }
    }

    //    四、使用File类进行文件的读写：
//读文件
    public byte[] readSDFile(int filePathType, String fileName) throws IOException {

        File file = getFile(filePathType, fileName);
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte[] buffer = new byte[length];
        fis.read(buffer);
        fis.close();
        return buffer;
    }

    //写文件
    public void writeSDFile(int filePathType, String fileName, byte[] bytes) throws IOException {
        if(getAvailableExternalMemorySize()<bytes.length)
        {
            Log.e("TAG","sdcard可用空间不足");
            return;
        }
        File file = getFile(filePathType, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
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
//            file.delete();
        } else {
            System.out.println("文件不存在！");
        }
    }

    //删除文件
    public void deleteCaCheFile() {

        File file = getFile(FilePathType.FILE, null);
        if (file.exists())
            deleteFile(file);
        File photoFile = getFile(FilePathType.PHOTO, null);
        if (photoFile.exists())
            deleteFile(photoFile);
        File rootFile = new File(FilePathUtil.getSystemPath(mContext));
        if (rootFile.exists())
            deleteFile(rootFile);
    }

    /*获取根目录下所有文件路径*/
    public String[] getCaCheFilePaths(int filePathType) {
        File[] files = null;
        File file = getFile(filePathType, null);
        if (file.exists()) {
            files = file.listFiles();
            if (files.length > 0) {
                String[] str = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    str[i] = files[i].getAbsolutePath();
//                    Log.i("TAGG","CaCheF=="+ str[i]);
                }
                return str;
            }
        }
        return null;
    }

    /*获取根目录下某文件路径*/
    public String getFilePath(int filePathType, String fileName) {

        File file = getFile(filePathType, fileName);
        if (file.exists())
            return file.getAbsolutePath();
        return "";
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
//        Environment.MEDIA_MOUNTED.equals(Environment
//                .getExternalStorageState())
//        Environment.getExternalStorageState()
//                .equals(android.os.Environment.MEDIA_MOUNTED);
//        Log.i("TAGG",android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)+"");
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部剩余存储空间
     * @return
     */
    public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     * @return
     */
    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取SDCARD剩余存储空间
     * @return
     */
    public long getAvailableExternalMemorySize() {
        if (isExistSdCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;//这里返回错误信息
        }
    }

    /**
     * 获取SDCARD总的存储空间
     * @return
     */
    public long getTotalExternalMemorySize() {
        if (isExistSdCard()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;//这里返回错误信息
        }
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

    private static String getString(InputStream inputStream, String charset) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] data = baos.toByteArray();
        inputStream.close();
        baos.close();
        return new String(data, charset);
    }

    public class FilePathType {
        public final static int PHOTO = 1000;
        public final static int FILE = 1001;
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
//WRITE_MEDIA_STORAGE 权限仅提供给系统应用，不再授予第三方App。所以暂时无法对外置sdcard读写
}
