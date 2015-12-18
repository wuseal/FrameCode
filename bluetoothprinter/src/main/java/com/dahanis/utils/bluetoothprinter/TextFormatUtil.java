package com.dahanis.utils.bluetoothprinter;


import com.dahanis.utils.bluetoothprinter.bluetoothbean.TextWeightBean;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 文字打印格式化工具
 * Created By: Seal.Wu
 * Date: 2015/4/30
 * Time: 17:01
 */
public class TextFormatUtil {

    /**
     * 打印纸一行最大的字节
     */
    private static final int DEFAULT_LINE_BYTE_SIZE = 48;

    private int lineByteSize = DEFAULT_LINE_BYTE_SIZE;
    /**
     * 分隔符
     */
    private static final String SEPARATOR = "$";

    private static StringBuffer sb = new StringBuffer();
    /**
     * 行元素集合
     */
    private final List<TextWeightBean> lineElements = new ArrayList<TextWeightBean>();

    /**
     * 排版居中标题
     *
     * @param title
     * @return
     */
    public String getLineTitle(String title) {
        sb.delete(0, sb.length());
        for (int i = 0; i < (lineByteSize - getBytesLength(title)) / 2; i++) {
            sb.append(" ");
        }
        sb.append(title);
        sb.append("\n");
        return sb.toString();
    }

    public void setLineByteSize(int lineByteSize) {
        this.lineByteSize = lineByteSize;
    }

    /**
     * 根据比重打印文字，全部文字居左对齐
     *
     * @return
     */
    public String getLineTextAccordingWeight(List<TextWeightBean> list) {
        sb.delete(0, sb.length());
        float totalWeight = 0;
        for (int i = 0; i < list.size(); i++) {
            totalWeight += list.get(i).getWeight();
        }
        for (int i = 0; i < list.size(); i++) {
            TextWeightBean textWeightBean = list.get(i);
            String showText = textWeightBean.getText();
            int holdSize = (int) (textWeightBean.getWeight() / totalWeight * lineByteSize);
            showText = formatText(showText, holdSize);
            sb.append(showText);
        }
        sb.append("\n");
        return sb.toString();
    }


    /**
     * 添加行元素<br>
     * 此方法以endLine方法来获取排版数据
     *
     * @param element 待添加的元素文字
     * @param weight  占一行的比重
     */
    public void addLineElement(String element, float weight) {
        lineElements.add(new TextWeightBean(element, weight));
    }

    /**
     * 结束当前编辑行此行
     *
     * @return 返回当前排版后的一行的数据
     */
    public String endLine() {
        String line = getLineTextAccordingWeight(lineElements);
        lineElements.clear();
        return line;
    }

    private String formatText(String showText, int holdSize) {
        int textSize = getBytesLength(showText);
        if (textSize > holdSize) {
            showText = subText(showText, holdSize);
        } else {
            for (int j = 0; j < holdSize - textSize; j++) {
                showText += " ";
            }
        }
        return showText;
    }

    private String subText(String showText, int holdSize) {
        int size = 0;
        int index = 0;
        int symbolLength = "..".getBytes(Charset.forName("GB2312")).length;
        for (int j = 0; j < showText.length(); j++) {
            String c = showText.substring(j, j + 1);
            size += c.getBytes(Charset.forName("GB2312")).length;
            index = j;
            if (size > holdSize - symbolLength) {
                break;
            }
        }
        showText = showText.substring(0, index) + "..";

        return formatText(showText, holdSize);
    }


    /**
     * 获取最大长度
     *
     * @param msgs
     * @return
     */
    private static int getMaxLength(Object[] msgs) {
        int max = 0;
        int tmp;
        for (Object oo : msgs) {
            tmp = getBytesLength(oo.toString());
            if (tmp > max) {
                max = tmp;
            }
        }
        return max;
    }

    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    private static int getBytesLength(String msg) {
        if (msg == null) {
            return "null".getBytes(Charset.forName("GB2312")).length;
        }
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

}
