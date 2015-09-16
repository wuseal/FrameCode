package com.dh.foundation.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * 默认对话框生产者
 * Created By: Seal.Wu
 * Date: 2015/8/17
 * Time: 11:32
 */
class DefaultProgressDialogMaker extends ProgressDialogMaker {

    /**
     * 对话框内容
     */
    private String content;


    /**
     * 对话框样式
     */
    private int style;

    /**
     * 自定义对话框视图
     */
    private int customViewLayout;


    @Override
    public Dialog makeDialog(Context context) {

        Dialog dialog;

        if (customViewLayout != 0) {

            dialog = new Dialog(context, style);

            if (StringUtils.isNotEmpty(content)) {

                dialog.setTitle(content);
            }
            dialog.setContentView(customViewLayout);

        } else {

            ProgressDialog progressDialog;

            if (style == 0) {

                progressDialog = new ProgressDialog(context);

            } else {

                progressDialog = new ProgressDialog(context, style);
            }

            progressDialog.setIndeterminate(false);

            progressDialog.setTitle(null);

            if (StringUtils.isNotEmpty(content)) {

                progressDialog.setMessage(content);
            }
            dialog = progressDialog;


        }
        dialog.setCanceledOnTouchOutside(false);

        dialog.setCancelable(true);


        return dialog;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public void setCustomViewLayout(int customViewLayout) {

        this.customViewLayout = customViewLayout;

    }
}
