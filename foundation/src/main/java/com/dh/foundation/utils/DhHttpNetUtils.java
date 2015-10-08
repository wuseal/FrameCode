package com.dh.foundation.utils;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.dh.foundation.exception.DhBaseBeanError;
import com.dh.foundation.exception.DhRequestError;
import com.dh.foundation.manager.FoundationManager;
import com.dh.foundation.utils.bluetooth.bluetoothbean.BaseBean;

/**
 * 大含物流APP专用网络请求工具类
 * Created By: Seal.Wu
 * Date: 2015/7/16
 * Time: 15:02
 */
public class DhHttpNetUtils {


    public static abstract class RequestListener<T> extends HttpNetUtils.RequestListener<T> implements CompatListener<T> {

        @Override
        public final void onSuccess(T t) {

            if (t instanceof BaseBean) {

                if (isGetDataSuccessfully((BaseBean) t)) {

                    onSuccessfully(t);

                } else {

                    ToastUtils.toast(FoundationManager.getContext(), ((BaseBean) t).getReturnMsg());

                    onFailure(new DhRequestError(new DhBaseBeanError((BaseBean) t)));
                }
            } else {

                onSuccessfully(t);
            }
        }

        @Override
        public final void onFailed(Throwable throwable) {

            ToastUtils.toast(FoundationManager.getContext(), throwable.getMessage());

            onFailure(new DhRequestError(throwable));
        }

        @Override
        public void onFailure(DhRequestError requestError) {

        }

        @Override
        public void onFinished() {
            ProgressDialogUtil.dismissProgressDialog();
        }
    }

    /**
     * GET请求网络接口数据
     *
     * @param context         上下文
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     */
    public static synchronized <T> void getData(Context context, String baseAddress, RequestParams requestParams, RequestListener<T> requestListener) {

        if (isNetStateUnavailable(context, requestListener)) return;

        ProgressDialogUtil.showProgressDialog(context);

        AutoPrintHttpNetUtils.getData(context, baseAddress, requestParams, requestListener);
    }

    /**
     * POST请求网络接口数据
     *
     * @param context         上下文
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     */
    public static synchronized <T> void postData(Context context, String baseAddress, RequestParams requestParams, RequestListener<T> requestListener) {

        if (isNetStateUnavailable(context, requestListener)) return;

        ProgressDialogUtil.showProgressDialog(context);

        AutoPrintHttpNetUtils.postData(context, baseAddress, requestParams, requestListener);
    }

    /**
     * 网络状态是否不可用
     */
    private static <T> boolean isNetStateUnavailable(Context context, RequestListener<T> requestListener) {

        if (!NetWorkDetector.isNetConnected()) {

            ToastUtils.toast(context, "无可用网络请检查网络设置");

            if (requestListener != null) {

                requestListener.onFailure(new DhRequestError(new NetworkErrorException("无可用网络请检查网络设置")));

                requestListener.onFinished();
            }
            return true;
        }
        return false;
    }


    /**
     * 获取数据监听器
     */
    interface CompatListener<T> {

        void onSuccessfully(T t);

        void onFailure(DhRequestError requestError);
    }

    /**
     * 是否成功地获取到期望的数据
     *
     * @param baseBean 服务器返回的bean
     * @return true:代表成功
     */
    public static boolean isGetDataSuccessfully(BaseBean baseBean) {

        return StringUtils.equals("1", baseBean.getReturnCode());
    }

}
