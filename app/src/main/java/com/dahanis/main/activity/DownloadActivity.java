package com.dahanis.main.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import com.dahanis.main.R;
import com.dh.foundation.utils.download.DownLoadUtil;
import com.dh.foundation.utils.download.DownloadListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件下载示例demo
 * Created By: Seal.Wu
 * Date: 2015/10/9
 * Time: 14:29
 */
public class DownloadActivity extends Activity {

    String url = "http://cdn12.down.apk.gfan.com/Pfiles/2017/09/04/1131772_44589ae3-5825-4a21-a975-fe552313f0f7.apk";


    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.startDownLoad)
    Button startDownLoad;
    @Bind(R.id.reDownload)
    Button reDownload;

    private long downloadId;//下载任务的标识唯一id

    DownLoadUtil downLoadUtil;


    @OnClick(R.id.startDownLoad)
    void startDownLoad() {

        downloadId = downLoadUtil.startADownloadTask(url, new DownloadListener() {
            @Override
            public void onLoadChange(int total, int currentSize, int state) {

                progressBar.setMax(total);

                progressBar.setProgress(currentSize);
                /**
                 * 正在下载中
                 */
                if (state == DownloadManager.STATUS_RUNNING) {


                }
                /**
                 * 下载失败
                 */
                else if (state == DownloadManager.STATUS_FAILED) {

                    startDownLoad.setText("下载失败");

                    reDownload.setText("重新下载");

                }
            }

            /**
             * 下载完成
             */
            @Override
            public void onComplete(long downloadId, String filePath) {
                startDownLoad.setText("下载完成");
                reDownload.setText("重新下载");
            }
        });
    }


    @OnClick(R.id.reDownload)
    void reDownLoad() {
        startDownLoad.setText("开始下载");
        reDownload.setText("正在下载");
        startDownLoad();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donwload);
        ButterKnife.bind(this);
        downLoadUtil = DownLoadUtil.getInstance();
    }


    @Override
    protected void onDestroy() {

        downLoadUtil.leaveActivity(downloadId);
        super.onDestroy();
    }
}
