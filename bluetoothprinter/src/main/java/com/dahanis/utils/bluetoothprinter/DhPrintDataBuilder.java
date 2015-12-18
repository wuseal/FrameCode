package com.dahanis.utils.bluetoothprinter;

import android.graphics.Bitmap;

import com.dahanis.utils.bluetoothprinter.bluetoothbean.DhNameValuePair;
import com.dahanis.utils.bluetoothprinter.bluetoothbean.DhPrintGoodsDetailBean;
import com.dahanis.utils.bluetoothprinter.bluetoothbean.PrintLabelBean;
import com.dahanis.utils.bluetoothprinter.bluetoothbean.TakeOverTemplateBean;
import com.dahanis.utils.bluetoothprinter.bluetoothbean.TextWeightBean;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 待打印数据构建器
 * Created By: Seal.Wu
 * Date: 2015/5/15
 * Time: 13:56
 */
public class DhPrintDataBuilder {
    /**
     * 货主交接清单
     */
    private static final String TYPE_TAKE_OVER_OF_SHIPPER = "001";
    /**
     * 马夹交接清单
     */
    private static final String TYPE_TAKE_OVER_OF_VEST = "002";
    /**
     * 交接清单数据类型
     */
    public static final String TYPE_TAKE_OVER = "0";
    /**
     * 打印标签数据类型
     */
    public static final String TYPE_LABEL = "1";
    /**
     * 打印图片
     */
    public static final String TYPE_PICTURE = "2";
    /**
     * 纯String数据
     */
    public final static String TYPE_STRING = "3";
    /**
     * 纯byte[]
     */
    public final static String TYPE_BYTES = "4";

    private String printDataType;

    private String printData;

    private Bitmap bitmap;

    private byte[] bytes;

    private final List<byte[]> printOutDataList = new ArrayList<byte[]>();

    public DhPrintDataBuilder(String printDataType, String printData) {
        this.printDataType = printDataType;
        this.printData = printData;
    }

    public DhPrintDataBuilder(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.printDataType = TYPE_PICTURE;

    }

    public DhPrintDataBuilder(byte[] data) {
        this.bytes = data;
        this.printDataType = TYPE_BYTES;
    }


    public String getPrintData() {
        return printData;
    }

    public void setPrintData(String printData) {
        this.printData = printData;
    }

    public String getPrintDataType() {
        return printDataType;
    }

    public void setPrintDataType(String printDataType) {
        this.printDataType = printDataType;
    }

    public List<byte[]> getPrintOutData() {
        printOutDataList.clear();
        if (StringUtils.equals(printDataType, TYPE_TAKE_OVER)) {
            addTakeOverData();
        } else if (StringUtils.equals(printDataType, TYPE_LABEL)) {
            addLabels();
        } else if (StringUtils.equals(printDataType, TYPE_PICTURE)) {
            printOutDataList.add(new PrintCodeAndBitmapDataMaker().printBitmap(bitmap));
        } else if (StringUtils.equals(printDataType, TYPE_STRING)) {
            printOutDataList.add(printData.getBytes(Charset.forName("gbk")));
        } else if (StringUtils.equals(printDataType, TYPE_BYTES)) {
            printOutDataList.add(bytes == null ? new byte[0] : bytes);
        }
        return printOutDataList;
    }

    /**
     * 添加打印标签数据
     */
    private void addLabels() {
        PrintLabelBean.ListBean listBean = new Gson().fromJson(printData, PrintLabelBean.ListBean.class);
        for (PrintLabelBean printLabelBean : listBean.getReturnData()) {
            addQRCode(printLabelBean);
            addSuperBigTitlePrintData(printLabelBean.getStartCity() + " - " + printLabelBean.getEndCity());
            addSuperBigTitlePrintData(printLabelBean.getGoodsNo() + "/" + printLabelBean.getGoodsNum() + "件" + "    " + printLabelBean.getStartDate() + "日");
            addSuperBigTitlePrintData(printLabelBean.getOrderCode());
            if (StringUtils.isNotEmpty(printLabelBean.getServiceType())) {
                addNormalTitlePrintData(printLabelBean.getServiceType());
            }
            StringBuffer buffer = new StringBuffer();
            List<Byte> data = new ArrayList<Byte>();
            com.dahanis.utils.bluetoothprinter.TextFormatUtil textFormatUtil = new com.dahanis.utils.bluetoothprinter.TextFormatUtil();
            textFormatUtil.setLineByteSize(48);
            addBigTitlePrintData(printLabelBean.getLogisticsName());

            textFormatUtil.addLineElement("发货:" + printLabelBean.getShipper(), 1f);
            buffer.append(textFormatUtil.endLine());
            textFormatUtil.addLineElement(printLabelBean.getShipperAddress(), 1f);
            buffer.append(textFormatUtil.endLine());
            textFormatUtil.addLineElement("收货:" + printLabelBean.getDeliver(), 1f);
            buffer.append(textFormatUtil.endLine());
            textFormatUtil.addLineElement(printLabelBean.getDeliverAddress(), 1f);
            buffer.append(textFormatUtil.endLine());
            textFormatUtil.addLineElement(printLabelBean.getGoodsType(), 1f);
            textFormatUtil.addLineElement(printLabelBean.getVolume() + "m3", 1f);
            textFormatUtil.addLineElement(printLabelBean.getWeight() + "kg", 1f);
            buffer.append(textFormatUtil.endLine());
            textFormatUtil.addLineElement(printLabelBean.getOfflineCode(), 1);
            buffer.append(textFormatUtil.endLine());
            buffer.append("\n\n\n");
            addToPrintOutDataListAndClear(buffer, data);
        }
    }

    private void addToPrintOutDataListAndClear(StringBuffer buffer, List<Byte> data) {
        addStringToByes(data, buffer);
        byte[] outByteData = makeBytes(data);
        printOutDataList.add(outByteData);
        buffer.delete(0, buffer.length());
        data.clear();
    }

    /**
     * 添加二维码
     */
    private void addQRCode(PrintLabelBean printLabelBean) {
        byte[] qrCode = new PrintCodeAndBitmapDataMaker().printQRCode(printLabelBean.getOrderCode() + "|" + printLabelBean.getGoodsNo().split("-")[1]);
        printOutDataList.add(qrCode);
    }

    /**
     * 添加交接清单排版数据
     */
    private void addTakeOverData() {

        TakeOverTemplateBean.Bean bean = new Gson().fromJson(printData, TakeOverTemplateBean.Bean.class);
        TakeOverTemplateBean takeOverTemplateBean = bean.getReturnData();

        if (StringUtils.equals(takeOverTemplateBean.getTemplate().getValue(), TYPE_TAKE_OVER_OF_VEST)) {
            addVestTakeOver(takeOverTemplateBean);
        } else {
            addShipperTakeOver(takeOverTemplateBean);
        }

    }

    /**
     * 添加马夹交接清单
     */
    private void addVestTakeOver(TakeOverTemplateBean takeOverTemplateBean) {
        addNormalTitlePrintData(takeOverTemplateBean.getTitle().getValue());
        com.dahanis.utils.bluetoothprinter.TextFormatUtil textFormatUtil = new com.dahanis.utils.bluetoothprinter.TextFormatUtil();
        textFormatUtil.setLineByteSize(72);
        List<Byte> data = new ArrayList<Byte>();
        StringBuffer buffer = new StringBuffer();
        changeToSmallTextSize(data);
        for (DhNameValuePair nameValuePair : takeOverTemplateBean.getSummary()) {
            textFormatUtil.addLineElement(nameValuePair.getName(), 1f);
            textFormatUtil.addLineElement(nameValuePair.getValue(), 1f);
            buffer.append(textFormatUtil.endLine());
        }
        for (DhPrintGoodsDetailBean printGoodsDetailBean : takeOverTemplateBean.getList()) {
            textFormatUtil.addLineElement(printGoodsDetailBean.getOrderCode(), 2f);
            textFormatUtil.addLineElement(printGoodsDetailBean.getGoodsType(), 1f);
            textFormatUtil.addLineElement(printGoodsDetailBean.getGoodsNum(), 1f);
            textFormatUtil.addLineElement(printGoodsDetailBean.getAddressCity(), 1f);
            buffer.append(textFormatUtil.endLine());
        }
        List<DhNameValuePair> listSummary = takeOverTemplateBean.getListSummary();
        textFormatUtil.addLineElement(listSummary.get(0).getValue(), 2f);
        textFormatUtil.addLineElement("", 1f);
        textFormatUtil.addLineElement(listSummary.get(1).getValue(), 1f);
        textFormatUtil.addLineElement("", 1f);
        buffer.append(textFormatUtil.endLine());

        buffer.append("\n");
        textFormatUtil.addLineElement("", 1f);
        textFormatUtil.addLineElement("收货签字:", 1f);
        buffer.append(textFormatUtil.endLine());
        buffer.append("\n\n\n");
        addToPrintOutDataListAndClear(buffer, data);

        addSuperBigTitlePrintData("");
        addSuperBigTitlePrintData("");
        addSuperBigTitlePrintData("");
        addSuperBigTitlePrintData("");
        addSuperBigTitlePrintData("");
    }

    /**
     * 添加货主交接清单
     */
    private void addShipperTakeOver(TakeOverTemplateBean takeOverTemplateBean) {
        List<Byte> data = new ArrayList<Byte>();
        com.dahanis.utils.bluetoothprinter.TextFormatUtil textFormatUtil = new com.dahanis.utils.bluetoothprinter.TextFormatUtil();
        textFormatUtil.setLineByteSize(72);
        StringBuffer buffer = new StringBuffer();
        addNormalTitlePrintData(takeOverTemplateBean.getTitle().getValue());
        changeToSmallTextSize(data);
        for (DhNameValuePair nameValuePair : takeOverTemplateBean.getSummary()) {
            List<TextWeightBean> list = new ArrayList<TextWeightBean>();
            list.add(new TextWeightBean(nameValuePair.getName(), 1f));
            list.add(new TextWeightBean(nameValuePair.getValue(), 1f));
            buffer.append(textFormatUtil.getLineTextAccordingWeight(list));
        }
        for (DhPrintGoodsDetailBean printGoodsDetailBean : takeOverTemplateBean.getList()) {
            List<TextWeightBean> list = new ArrayList<TextWeightBean>();
            list.add(new TextWeightBean(printGoodsDetailBean.getOrderCode(), 2f));
            list.add(new TextWeightBean(printGoodsDetailBean.getGoodsType(), 1f));
            list.add(new TextWeightBean(printGoodsDetailBean.getGoodsNum(), 1f));
            list.add(new TextWeightBean(printGoodsDetailBean.getAddressCity(), 1f));
//            list.add(new TextWeightBean(printGoodsDetailBean.getTransportFee(), 1f));
            list.add(new TextWeightBean(printGoodsDetailBean.getGoodsFee(), 1f));
            list.add(new TextWeightBean(printGoodsDetailBean.getSupport(), 1f));
            list.add(new TextWeightBean(printGoodsDetailBean.getPayType(), 1f));
            buffer.append(textFormatUtil.getLineTextAccordingWeight(list));
        }

        List<TextWeightBean> list = new ArrayList<TextWeightBean>();

        List<DhNameValuePair> listSummary = takeOverTemplateBean.getListSummary();
        list.add(new TextWeightBean(listSummary.get(0).getValue(), 3f));
        list.add(new TextWeightBean(listSummary.get(1).getValue(), 2f));
        list.add(new TextWeightBean(listSummary.get(2).getValue(), 1f));
        list.add(new TextWeightBean(listSummary.get(3).getValue(), 2f));
        buffer.append(textFormatUtil.getLineTextAccordingWeight(list));
        list.clear();

//        list.add(new TextWeightBean("其它费用", 1));
//        buffer.append(formatUtil.getLineTextAccordingWeight(list));
//        list.clear();
//
//        int number = 0;
//        for (DhNameValuePair nameValuePair : takeOverTemplateBean.getOtherFee()) {
//            list.add(new TextWeightBean(nameValuePair.getName(), 1f));
//            list.add(new TextWeightBean(nameValuePair.getValue(), 1f));
//            number++;
//            if (number == 4) {
//                buffer.append(formatUtil.getLineTextAccordingWeight(list));
//                number = 0;
//                list.clear();
//            }
//        }
//        for (int i = 0; i < 8 - list.size(); i++) {
//            list.add(new TextWeightBean("  ", 1f));
//            list.add(new TextWeightBean("  ", 1f));
//        }
//        buffer.append(formatUtil.getLineTextAccordingWeight(list));
//        list.clear();

        buffer.append("\n");

        list.add(new TextWeightBean("", 1f));
        list.add(new TextWeightBean("收货签字:", 1f));
        buffer.append(textFormatUtil.getLineTextAccordingWeight(list));
        list.clear();

        buffer.append("\n\n\n");
        addToPrintOutDataListAndClear(buffer, data);
        addSuperBigTitlePrintData("");
        addSuperBigTitlePrintData("");
        addSuperBigTitlePrintData("");
        addSuperBigTitlePrintData("");
        addSuperBigTitlePrintData("");
    }

    private void addStringToByes(List<Byte> outData, StringBuffer buffer) {
        byte[] data = new byte[0];
        try {
            data = buffer.toString().getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            BluetoothLog.e(e);
        }
        for (int i = 0; i < data.length; i++) {
            outData.add(data[i]);
        }
    }

    /**
     * 转换成byte[]
     */
    private byte[] makeBytes(List<Byte> outData) {
        byte[] outByteData = new byte[outData.size()];
        for (int i = 0; i < outData.size(); i++) {
            outByteData[i] = outData.get(i);
        }
        return outByteData;
    }

    private void addNormalTitlePrintData(String title) {
        StringBuffer buffer = new StringBuffer();
        List<Byte> outData = new ArrayList<Byte>();
        com.dahanis.utils.bluetoothprinter.TextFormatUtil textFormatUtil = new com.dahanis.utils.bluetoothprinter.TextFormatUtil();
        textFormatUtil.setLineByteSize(48);
        buffer.append(textFormatUtil.getLineTitle(title));
        printOutDataList.add(buffer.toString().getBytes(Charset.forName("GBK")));
        buffer.delete(0, buffer.length());
        outData.clear();
    }

    private void addBigTitlePrintData(String title) {
        StringBuffer buffer = new StringBuffer();
        List<Byte> outData = new ArrayList<Byte>();
        com.dahanis.utils.bluetoothprinter.TextFormatUtil textFormatUtil = new com.dahanis.utils.bluetoothprinter.TextFormatUtil();
        textFormatUtil.setLineByteSize(24);
        changeToBigTextSize(outData);
        buffer.append(textFormatUtil.getLineTitle(title));
        addStringToByes(outData, buffer);
        printOutDataList.add(makeBytes(outData));
        buffer.delete(0, buffer.length());
        outData.clear();
    }

    private void addSuperBigTitlePrintData(String title) {
        StringBuffer buffer = new StringBuffer();
        List<Byte> outData = new ArrayList<Byte>();
        com.dahanis.utils.bluetoothprinter.TextFormatUtil textFormatUtil = new com.dahanis.utils.bluetoothprinter.TextFormatUtil();
        textFormatUtil.setLineByteSize(24);
        changeToSuperBigTextSize(outData);
        buffer.append(textFormatUtil.getLineTitle(title));
        addStringToByes(outData, buffer);
        printOutDataList.add(makeBytes(outData));
        buffer.delete(0, buffer.length());
        outData.clear();
    }

    /**
     * 初始化打印机
     */
    private void init(List<Byte> outData) {
        outData.add((byte) 0x1b);
        outData.add((byte) 0x40);
    }

    /**
     * 改变到打印小字体状态
     */
    private void changeToSmallTextSize(List<Byte> outData) {
        outData.add((byte) 0x1b);
        outData.add((byte) 0x21);
        outData.add((byte) 0x01);
    }

    /**
     * 改变到打印正常体状态
     */
    private void changeToNormalTextSize(List<Byte> outData) {
        outData.add((byte) 0x1b);
        outData.add((byte) 0x21);
        outData.add((byte) 0x00);
    }

    /**
     * 改变到打印大型字体体状态
     */
    private void changeToBigTextSize(List<Byte> outData) {
        outData.add((byte) 0x1d);
        outData.add((byte) 0x21);
        outData.add((byte) 0x10);
    }

    /**
     * 改变到打印超大型字体体状态
     */
    private void changeToSuperBigTextSize(List<Byte> outData) {
        outData.add((byte) 0x1d);
        outData.add((byte) 0x21);
        outData.add((byte) 0x11);
    }


    private void addCmd(List<Byte> outData, byte[] cmd) {
        for (int i = 0; i < cmd.length; i++) {
            outData.add(cmd[i]);
        }
    }
}
