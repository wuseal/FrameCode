package com.dahanis.main.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dahanis.main.ReturnObj;
import com.dahanis.main.TruckBean;
import com.dh.foundation.utils.AutoPrintHttpNetUtils;
import com.dh.foundation.utils.DhHttpNetUtils;
import com.dh.foundation.utils.RequestParams;
import com.dh.foundation.utils.ToastUtils;

/**
 * 网络加载json数据直接到对象使用示例
 * Created By: Seal.Wu
 * Date: 2015/10/9
 * Time: 10:56
 */
public class HttpNetUtilActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /**
         * 先定义基础地址
         */
        String baseAddress = "http://m.dahanis.com:24080/BasicService.asmx/GetVehicleLengthList";
        /**
         * 然后再实例一个相应的参数对象并设值
         */
        RequestParams params = new RequestParams();

        params.putParams("token", "Zrmp6OJN8JilVNd66DSRntEQAzPtNXNdQzGUK8FDhavb9Lv%2BGSEmpqVvPXLgk0S00F1isuQY5R4%3D");

        params.putParams("userId", "600000032");


        AutoPrintHttpNetUtils.getData(baseAddress, params, new AutoPrintHttpNetUtils.RequestListener<ReturnObj>() {
            @Override
            public void onSuccess(ReturnObj returnObj) {

                TruckBean truckBean = returnObj.getTruckBeanList().get(0);

                ToastUtils.toastLongTime(getBaseContext(), truckBean.getId() + "\n" + truckBean.getLengthValue());
            }

            @Override
            public void onFailed(Throwable throwable) {

            }

            @Override
            public void onFinished() {

            }

        });
    }
}
