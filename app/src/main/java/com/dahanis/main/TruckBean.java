package com.dahanis.main;

/**
 * Created By: Seal.Wu
 * Date: 2015/9/30
 * Time: 10:14
 */
public class TruckBean {

    /**
     * Creator :
     * CreateTime : null
     * ModifyTime : null
     * Modifier :
     * Id : 1
     * LengthCode : 001
     * LengthValue : 2.5
     */
    private String Creator;
    private String CreateTime;
    private String ModifyTime;
    private String Modifier;
    private int Id;
    private String LengthCode;
    private String LengthValue;

    public void setCreator(String Creator) {
        this.Creator = Creator;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public void setModifyTime(String ModifyTime) {
        this.ModifyTime = ModifyTime;
    }

    public void setModifier(String Modifier) {
        this.Modifier = Modifier;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setLengthCode(String LengthCode) {
        this.LengthCode = LengthCode;
    }

    public void setLengthValue(String LengthValue) {
        this.LengthValue = LengthValue;
    }

    public String getCreator() {
        return Creator;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public String getModifyTime() {
        return ModifyTime;
    }

    public String getModifier() {
        return Modifier;
    }

    public int getId() {
        return Id;
    }

    public String getLengthCode() {
        return LengthCode;
    }

    public String getLengthValue() {
        return LengthValue;
    }
}