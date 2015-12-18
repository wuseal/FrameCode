package com.dahanis.utils.bluetoothprinter.bluetoothbean;

import java.util.List;

/**
 * 打印标签内容对象
 * Created By: Seal.Wu
 * Date: 2015/5/16
 * Time: 12:20
 */
public class PrintLabelBean {
    private String StartCity;//：开始城市
    private String EndCity;//：目的城市
    private String GoodsNo;//：货号
    private String GoodsNum;//：总件数
    private String StartDate;//：发货日期
    private String OrderCode;//：货单号
    private String ServiceType;//：服务种类
    private String Shipper;//：发货人
    private String ShipperAddress;//：发货人地址
    private String Deliver;//：收货人
    private String DeliverAddress;//：收货人地址
    private String GoodsType;//：货物种类
    private String Weight;//：体积
    private String Volume;//：重量
    private String LogisticsName;//：物流公司名称
    private String OfflineCode;//：线下清单号


    public String getStartCity() {
        return StartCity;
    }

    public void setStartCity(String startCity) {
        StartCity = startCity;
    }

    public String getEndCity() {
        return EndCity;
    }

    public void setEndCity(String endCity) {
        EndCity = endCity;
    }

    public String getGoodsNo() {
        return GoodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        GoodsNo = goodsNo;
    }

    public String getGoodsNum() {
        return GoodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        GoodsNum = goodsNum;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getShipper() {
        return Shipper;
    }

    public void setShipper(String shipper) {
        Shipper = shipper;
    }

    public String getShipperAddress() {
        return ShipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        ShipperAddress = shipperAddress;
    }

    public String getDeliver() {
        return Deliver;
    }

    public void setDeliver(String deliver) {
        Deliver = deliver;
    }

    public String getDeliverAddress() {
        return DeliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        DeliverAddress = deliverAddress;
    }

    public String getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(String goodsType) {
        GoodsType = goodsType;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getLogisticsName() {
        return LogisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        LogisticsName = logisticsName;
    }

    public String getOfflineCode() {
        return OfflineCode;
    }

    public void setOfflineCode(String offlineCode) {
        OfflineCode = offlineCode;
    }

    public static class ListBean extends BaseBean<List<PrintLabelBean>> {

    }
}
