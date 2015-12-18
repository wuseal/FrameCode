package com.dahanis.utils.bluetoothprinter.bluetoothbean;

/**
 * 文字比重内容记录对象
 * Created By: Seal.Wu
 * Date: 2015/5/16
 * Time: 15:53
 */
public class TextWeightBean {
    private String text;
    private float weight;

    public TextWeightBean(String text, float weight) {
        this.text = text;
        this.weight = weight;
    }

    public String getText() {
        return text;
    }

    public float getWeight() {
        return weight;
    }
}
