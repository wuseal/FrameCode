package com.dh.foundation.utils.bluetooth;

import android.graphics.Bitmap;

import java.io.UnsupportedEncodingException;

class PrintCodeAndBitmapDataMaker {
    int nStartOrgx = 5;
    int nBarcodetype = 0x49;
    int nBarcodeWidth = 4;
    int nBarcodeHeight = 4;
    int nBarcodeFontType = 0;
    int nBarcodeFontPosition = 0x02;
    String strBarcode = "123456678";
    int nOrgx = nStartOrgx * 12;// nStartOrgx 起始缩进的位置-->>以下几个参数请参考array.txt
    int nType = 65 + nBarcodetype; // nBarcodetype 条码的类型
    int nWidthX = nBarcodeWidth + 2;// nBarcodeWidth 条码宽度
    int nHeight = (nBarcodeHeight + 1) * 24; // nBarcodeHeight 条码高度
    int nHriFontType = nBarcodeFontType; // nBarcodeFontType 条码文本字体
    int nHriFontPosition = nBarcodeFontPosition; // nBarcodeFontPosition 条码文本位置

    public byte[] printBitmap(Bitmap bitmap) {
//        return POS_S_SetQRcode("helfkdsjfj", 6, 4);
        if (bitmap == null) {
            return new byte[0];
        }
        return POS_PrintPicture(bitmap, 576, 0);
//        return thresholdToBWPic(bitmap);
    }

    public byte[] printQRCode(String qrCode) {
        return POS_S_SetQRcode(qrCode, 6, 4);
    }


    // 打印条形码
    public byte[] POS_S_SetBarcode(String strCodedata, int nOrgx, int nType,
                                   int nWidthX, int nHeight, int nHriFontType, int nHriFontPosition) {
        if (nOrgx < 0 | nOrgx > 65535 | nType < 0x41 | nType > 0x49
                | nWidthX < 2 | nWidthX > 6 | nHeight < 1 | nHeight > 255)
            return new byte[0];

        byte[] bCodeData = null;
        try {
            bCodeData = strCodedata.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }

        Cmd.ESCCmd.ESC_dollors_nL_nH[2] = (byte) (nOrgx % 0x100);
        Cmd.ESCCmd.ESC_dollors_nL_nH[3] = (byte) (nOrgx / 0x100);
        Cmd.ESCCmd.GS_w_n[2] = (byte) nWidthX;
        Cmd.ESCCmd.GS_h_n[2] = (byte) nHeight;
        Cmd.ESCCmd.GS_f_n[2] = (byte) (nHriFontType & 0x01);
        Cmd.ESCCmd.GS_H_n[2] = (byte) (nHriFontPosition & 0x03);
        Cmd.ESCCmd.GS_k_m_n_[2] = (byte) nType;
        Cmd.ESCCmd.GS_k_m_n_[3] = (byte) bCodeData.length;

        byte[] data = byteArraysToBytes(new byte[][]{
                Cmd.ESCCmd.ESC_dollors_nL_nH, Cmd.ESCCmd.GS_w_n,
                Cmd.ESCCmd.GS_h_n, Cmd.ESCCmd.GS_f_n, Cmd.ESCCmd.GS_H_n,
                Cmd.ESCCmd.GS_k_m_n_, bCodeData});
        return data;
    }

    public static byte[] POS_S_SetQRcode(String strCodedata, int nWidthX,
                                         int nErrorCorrectionLevel) {

        if (nWidthX < 2 | nWidthX > 6 | nErrorCorrectionLevel < 1
                | nErrorCorrectionLevel > 4)
            return new byte[0];

        byte[] bCodeData = null;
        try {
            bCodeData = strCodedata.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            return new byte[0];
        }
        Cmd.ESCCmd.GS_w_n[2] = (byte) nWidthX;
        Cmd.ESCCmd.GS_k_m_v_r_nL_nH[3] = 0x00;//设置规格为6
        Cmd.ESCCmd.GS_k_m_v_r_nL_nH[4] = (byte) nErrorCorrectionLevel;
        Cmd.ESCCmd.GS_k_m_v_r_nL_nH[5] = (byte) (bCodeData.length & 0xff);
        Cmd.ESCCmd.GS_k_m_v_r_nL_nH[6] = (byte) ((bCodeData.length & 0xff00) >> 8);

        byte[] data = byteArraysToBytes(new byte[][]{
                Cmd.ESCCmd.GS_w_n, Cmd.ESCCmd.GS_k_m_v_r_nL_nH, bCodeData});
        return data;

    }

    public  byte[] POS_PrintPicture(Bitmap mBitmap, int nWidth, int nMode) {

        // 先转黑白，再调用函数缩放位图
        // 不转黑白
        int width = ((nWidth + 7) / 8) * 8;
        int height = mBitmap.getHeight() * width / mBitmap.getWidth();
        height = ((height + 7) / 8) * 8;
        Bitmap rszBitmap = ImageProcessing.resizeImage(mBitmap, width, height);
        Bitmap grayBitmap = ImageProcessing.toGrayscale(rszBitmap);
        byte[] dithered = bitmapToBWPix(grayBitmap);

        byte[] data = eachLinePixToCmd(dithered, width, nMode);

//        if (IO.PORT_BT == IO.GetCurPort() && pictureUseFlowControl) {
//            // 基本超时1000ms 40k的数据4000ms超时
//            POS_Write_FlowControl(data, 0, data.length, 128);
//        } else if (IO.PORT_USB == IO.GetCurPort()){
//            IO.Write(data, 0, data.length);
//        }  else {
//            // 当width = 384时，一行数据占48个字节，加上8个字节头，总共width/8+8个字节。
//            int nBytesPerLine = width / 8 + 8;
//            int nLinesPerTest = 1;
//            if (IO.PORT_BT == IO.GetCurPort())
//                nLinesPerTest = 30;
//            else if (IO.PORT_NET == IO.GetCurPort())
//                nLinesPerTest = 5;
//            else if (IO.PORT_USB == IO.GetCurPort())
//                nLinesPerTest = 60;
//
//            POS_Write_Safety(data, 0, data.length, nBytesPerLine
//                    * nLinesPerTest);
//            // IO.Write(data, 0, data.length);
//        }
        return data;
    }


    private static byte[] thresholdToBWPic(Bitmap mBitmap) {
        int[] pixels = new int[mBitmap.getWidth() * mBitmap.getHeight()];
        byte[] data = new byte[mBitmap.getWidth() * mBitmap.getHeight()];

        mBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight());

        // for the toGrayscale, we need to select a red or green or blue color
        ImageProcessing.format_K_threshold(pixels, mBitmap.getWidth(),
                mBitmap.getHeight(), data);

        return data;
    }

    /**
     * 将ARGB图转换为二值图，0代表黑，1代表白
     *
     * @param mBitmap
     * @return
     */
    private  byte[] bitmapToBWPix(Bitmap mBitmap) {

        int[] pixels = new int[mBitmap.getWidth() * mBitmap.getHeight()];
        byte[] data = new byte[mBitmap.getWidth() * mBitmap.getHeight()];

        mBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight());

        // for the toGrayscale, we need to select a red or green or blue color
        ImageProcessing.format_K_dither16x16(pixels, mBitmap.getWidth(),
                mBitmap.getHeight(), data);

        return data;
    }

    // nWidth必须为8的倍数,这个只需在上层控制即可
    // 之所以弄成一维数组，是因为一维数组速度会快一点
    private static int[] p0 = {0, 0x80};
    private static int[] p1 = {0, 0x40};
    private static int[] p2 = {0, 0x20};
    private static int[] p3 = {0, 0x10};
    private static int[] p4 = {0, 0x08};
    private static int[] p5 = {0, 0x04};
    private static int[] p6 = {0, 0x02};

    // 1行作为1个图片，这样打印不会乱
    @SuppressWarnings("unused")
    private static byte[] pixToCmd(byte[] src, int nWidth, int nMode) {
        // nWidth = 384; nHeight = 582;
        int nHeight = src.length / nWidth;
        byte[] data = new byte[8 + (src.length / 8)];
        data[0] = 0x1d;
        data[1] = 0x76;
        data[2] = 0x30;
        data[3] = (byte) (nMode & 0x01);
        data[4] = (byte) ((nWidth / 8) % 0x100);// (xl+xh*256)*8 = nWidth
        data[5] = (byte) ((nWidth / 8) / 0x100);
        data[6] = (byte) ((nHeight) % 0x100);// (yl+yh*256) = nHeight
        data[7] = (byte) ((nHeight) / 0x100);
        int k = 0;
        for (int i = 8; i < data.length; i++) {
            // 不行，没有加权
            data[i] = (byte) (p0[src[k]] + p1[src[k + 1]] + p2[src[k + 2]]
                    + p3[src[k + 3]] + p4[src[k + 4]] + p5[src[k + 5]]
                    + p6[src[k + 6]] + src[k + 7]);
            k = k + 8;
        }
        return data;

    }

    private static byte[] eachLinePixToCmd(byte[] src, int nWidth, int nMode) {
        int nHeight = src.length / nWidth;
        int nBytesPerLine = nWidth / 8;
        byte[] data = new byte[nHeight * (8 + nBytesPerLine)];
        int offset = 0;
        int k = 0;
        for (int i = 0; i < nHeight; i++) {
            offset = i * (8 + nBytesPerLine);
            data[offset + 0] = 0x1d;
            data[offset + 1] = 0x76;
            data[offset + 2] = 0x30;
            data[offset + 3] = (byte) (nMode & 0x01);
            data[offset + 4] = (byte) (nBytesPerLine % 0x100);
            data[offset + 5] = (byte) (nBytesPerLine / 0x100);
            data[offset + 6] = 0x01;
            data[offset + 7] = 0x00;
            for (int j = 0; j < nBytesPerLine; j++) {
                data[offset + 8 + j] = (byte) (p0[src[k]] + p1[src[k + 1]]
                        + p2[src[k + 2]] + p3[src[k + 3]] + p4[src[k + 4]]
                        + p5[src[k + 5]] + p6[src[k + 6]] + src[k + 7]);
                k = k + 8;
            }
        }

        return data;
    }

    public static byte[] byteArraysToBytes(byte[][] data) {

        int length = 0;
        for (int i = 0; i < data.length; i++)
            length += data[i].length;
        byte[] send = new byte[length];
        int k = 0;
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[i].length; j++)
                send[k++] = data[i][j];
        return send;
    }
}
