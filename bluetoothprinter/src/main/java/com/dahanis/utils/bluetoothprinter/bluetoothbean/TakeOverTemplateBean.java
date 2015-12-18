package com.dahanis.utils.bluetoothprinter.bluetoothbean;


/**
 * 打印交接清单
 * Created By: Seal.Wu
 * Date: 2015/5/16
 * Time: 12:16
 */
public class TakeOverTemplateBean {
    private DhNameValuePair Template;//：KeyValue 001表示有运费，002表示没有
    private DhNameValuePair Title;//：KeyValue
    private java.util.List<DhNameValuePair> Summary;// ：KeyValue的集合
    private java.util.List<DhPrintGoodsDetailBean> List;//：交接明细，如左侧
    private java.util.List<DhNameValuePair> ListSummary;// ：KeyValue的集合
    private java.util.List<DhNameValuePair> OtherFee;// ：KeyValue的集合

    public DhNameValuePair getTemplate() {
        return Template;
    }

    public void setTemplate(DhNameValuePair template) {
        Template = template;
    }

    public DhNameValuePair getTitle() {
        return Title;
    }

    public void setTitle(DhNameValuePair title) {
        Title = title;
    }

    public java.util.List<DhNameValuePair> getSummary() {
        return Summary;
    }

    public void setSummary(java.util.List<DhNameValuePair> summary) {
        Summary = summary;
    }

    public java.util.List<DhPrintGoodsDetailBean> getList() {
        return List;
    }

    public void setList(java.util.List<DhPrintGoodsDetailBean> list) {
        List = list;
    }

    public java.util.List<DhNameValuePair> getListSummary() {
        return ListSummary;
    }

    public void setListSummary(java.util.List<DhNameValuePair> listSummary) {
        ListSummary = listSummary;
    }

    public java.util.List<DhNameValuePair> getOtherFee() {
        return OtherFee;
    }

    public void setOtherFee(java.util.List<DhNameValuePair> otherFee) {
        OtherFee = otherFee;
    }


    public static class Bean extends BaseBean<TakeOverTemplateBean> {

    }
}
