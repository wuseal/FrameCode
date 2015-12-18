package com.dahanis.utils.bluetoothprinter.bluetoothbean;

/**
 * 键值对象
 * Created By: Seal.Wu
 * Date: 2015/5/16
 * Time: 12:11
 */
public class DhNameValuePair {
    private String Name;///
    private String Value;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }


    @Override
    public String toString() {
        return String.format("%-20s:%-30s", getName(), getValue());
    }
}
