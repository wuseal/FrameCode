package com.dahanis.main.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dahanis.main.ReturnObj;
import com.dahanis.main.TruckBean;
import com.dh.foundation.utils.AutoPrintHttpNetUtils;
import com.dh.foundation.utils.DhHttpNetUtils;
import com.dh.foundation.utils.HttpNetUtils;
import com.dh.foundation.utils.ProgressDialogUtil;
import com.dh.foundation.utils.RequestParams;
import com.dh.foundation.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

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

        /**
         * 支持headers的添加
         */
        Map<String, String> headers = new HashMap<>();
        headers.put("user-agent", "android-volley-dahanis-foundation-app");
        params.setHeaders(headers);

        params.putParams("token", "Zrmp6OJN8JilVNd66DSRntEQAzPtNXNdQzGUK8FDhavb9Lv%2BGSEmpqVvPXLgk0S00F1isuQY5R4%3D");

        params.putParams("userId", "600000032");

        ProgressDialogUtil.showProgressDialog(this);

        AutoPrintHttpNetUtils.getData(baseAddress, params, new AutoPrintHttpNetUtils.RequestListener<ReturnObj>() {
            @Override
            public void onSuccess(ReturnObj returnObj) {

                TruckBean truckBean = returnObj.getTruckBeanList().get(0);

                ToastUtils.toastLongTime(truckBean.getId() + "\n" + truckBean.getLengthValue());
            }

            @Override
            public void onFailed(Throwable throwable) {

            }

            @Override
            public void onFinished() {
                ProgressDialogUtil.dismissProgressDialog();
            }

        }).setTag(toString());
    }


    @Override
    protected void onDestroy() {

        HttpNetUtils.cancelAll(toString());

        super.onDestroy();
    }
}
