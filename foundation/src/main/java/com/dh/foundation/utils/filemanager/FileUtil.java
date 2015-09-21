package com.dh.foundation.utils.filemanager;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by eve on 2015/9/18.目前只是整理，待测
 */
public class FileUtil {
    private static FileUtil mFileUtil;

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
        String str ="";
        try {
            //得到资源中的Raw数据流
            InputStream in = mContext.getResources().openRawResource(rawId);
            //得到数据的大小
            int length = in.available();
            byte[] buffer = new byte[length];
            //读取数据
            in.read(buffer);
            //依test.txt的编码类型选择合适的编码，如果不调整会乱码
            str  = new String(buffer, charsetName);
            //关闭
            in.close();
            return str ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //    2) 从resource的asset中读取文件数据
    public static String readFromAsset(Context mContext, String fileName, String charsetName) {
        String str  = "";
        try {
            //得到资源中的asset数据流
            InputStream in = mContext.getResources().getAssets().open(fileName);
            //得到数据的大小
            int length = in.available();
            byte[] buffer = new byte[length];
            //读取数据
            in.read(buffer);
            //依test.txt的编码类型选择合适的编码，如果不调整会乱码
            str  = new String(buffer, charsetName);
            //关闭
            in.close();
            return str ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //    二、读写/data/data/<应用程序名>目录上的文件:
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
        String str  = "";
        try {
            FileInputStream fin = mContext.openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            str  = new String(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str ;

    }

    //    三、读写SD卡中的文件。也就是/mnt/sdcard/目录下面的文件 ：
//写数据到SD中的文件
    public static void writeFileSdcardFile(String fileName, String write_str) throws IOException {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = write_str.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读SD中的文件
    public static String readFileSdcardFile(String fileName) throws IOException {
        String str  = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            str = new String(buffer, "UTF-8");

            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str ;
    }

    //    四、使用File类进行文件的读写：
//读文件
    public static String readSDFile(String fileName) throws IOException {
        String str  = "";
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte[] buffer = new byte[length];
        fis.read(buffer);
        str  = new String(buffer, "UTF-8");
        fis.close();
        return str ;
    }

    //写文件
    public static void writeSDFile(String fileName, String write_str) throws IOException {
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] bytes = write_str.getBytes();
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
  /*  09.  RandomAccessFile file = new RandomAccessFile("file", "rw");
    10.  // 以下向file文件中写数据
            11.  file.writeInt(20);// 占4个字节
    12.  file.writeDouble(8.236598);// 占8个字节
    13.  file.writeUTF("这是一个UTF字符串");// 这个长度写在当前文件指针的前两个字节处，可用readShort()读取
    14.  file.writeBoolean(true);// 占1个字节
    15.  file.writeShort(395);// 占2个字节
    16.  file.writeLong(2325451l);// 占8个字节
    17.  file.writeUTF("又是一个UTF字符串");
    18.  file.writeFloat(35.5f);// 占4个字节
    19.  file.writeChar('a');// 占2个字节
    20.
            21.  file.seek(0);// 把文件指针位置设置到文件起始处
    22.
            23.  // 以下从file文件中读数据，要注意文件指针的位置
            24.  System.out.println("——————从file文件指定位置读数据——————");
    25.  System.out.println(file.readInt());
    26.  System.out.println(file.readDouble());
    27.  System.out.println(file.readUTF());
    28.
            29.  file.skipBytes(3);// 将文件指针跳过3个字节，本例中即跳过了一个boolean值和short值。
    30.  System.out.println(file.readLong());
    31.
            32.  file.skipBytes(file.readShort()); // 跳过文件中“又是一个UTF字符串”所占字节，注意readShort()方法会移动文件指针，所以不用加2。
    33.  System.out.println(file.readFloat());
    34.
            35.  //以下演示文件复制操作
            36.  System.out.println("——————文件复制（从file到fileCopy）——————");
    37.  file.seek(0);
    38.  RandomAccessFile fileCopy=new RandomAccessFile("fileCopy","rw");
    39.  int len=(int)file.length();//取得文件长度（字节数）
    40.  byte[] b=new byte[len];
    41.  file.readFully(b);
    42.  fileCopy.write(b);
    43.  System.out.println("复制完成！");
    //        new RandomAccessFile(f,"rw");//读写方式打开
//        new RandomAccessFile(f,"r");//读方式*/
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
