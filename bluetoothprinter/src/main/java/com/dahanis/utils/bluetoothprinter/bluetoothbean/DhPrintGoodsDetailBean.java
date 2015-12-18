package com.dahanis.utils.bluetoothprinter.bluetoothbean;

/**
 * Created By: Seal.Wu
 * Date: 2015/5/16
 * Time: 12:14
 */
public class DhPrintGoodsDetailBean {
    private String OrderCode;//: "货单号",
    private String GoodsType;//: "品名",
    private String GoodsNum;//: "件数",
    private String AddressCity;//: "到达地",
    private String TransportFee;//: "运费",
    private String GoodsFee;//: "代收费",
    private String PayType;//: "付款方式"
    private String Support;//:保价费

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(String goodsType) {
        GoodsType = goodsType;
    }

    public String getGoodsNum() {
        return GoodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        GoodsNum = goodsNum;
    }

    public String getAddressCity() {
        return AddressCity;
    }

    public void setAddressCity(String addressCity) {
        AddressCity = addressCity;
    }

    public String getTransportFee() {
        return TransportFee;
    }

    public void setTransportFee(String transportFee) {
        TransportFee = transportFee;
    }

    public String getGoodsFee() {
        return GoodsFee;
    }

    public void setGoodsFee(String goodsFee) {
        GoodsFee = goodsFee;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String payType) {
        PayType = payType;
    }

    public String getSupport() {
        return Support;
    }

    public void setSupport(String support) {
        Support = support;
    }

    @Override
    public String toString() {
        return getOrderCode() + "  " + getGoodsType() + "  " + getGoodsNum() + "   " + getAddressCity() + "　　" + getTransportFee() + "   " + getGoodsFee() + "   " + getPayType();
    }
}
