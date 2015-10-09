package com.dahanis.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.dahanis.main.R;
import com.dh.foundation.adapter.DhBaseAdapter;
import com.dh.foundation.utils.ImageNetLoader;
import com.dh.foundation.widget.afkimageview.AfkImageView;
import com.dh.foundation.widget.afkimageview.NetAfkImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created By: Seal.Wu
 * Date: 2015/7/20
 * Time: 16:17
 */
public class LoadNetAfkImageViewActivity extends Activity {

    String[] list  = {"http://www.guolv.com/file/upload/201402/11/12-32-09-39-592.jpg",
            "http://pic1a.nipic.com/2008-09-05/200895134351570_2.jpg",
            "http://pic.nipic.com/2007-12-22/20071222144424844_2.jpg",
            "http://pic12.nipic.com/20101228/4151459_000644738360_2.jpg",
            "http://pic31.nipic.com/20130630/5892523_003233711151_2.jpg",
            "http://pic24.nipic.com/20121010/4388163_025151167143_2.jpg",
            "http://pic.nipic.com/2007-12-22/20071222144424844_2.jpg",
            "http://pic12.nipic.com/20101228/4151459_000644738360_2.jpg",
            "http://pica.nipic.com/2008-01-16/2008116155040332_2.jpg",
            "http://pic12.nipic.com/20101228/4151459_000644738360_2.jpg",
            "http://pic31.nipic.com/20130630/5892523_003233711151_2.jpg",
            "http://img4.duitang.com/uploads/item/201401/22/20140122010535_vSdrr.thumb.600_0.jpeg",
            "http://pic21.nipic.com/20120517.jpg",
            "http://pic3.nipic.com/20090615/1242397_114546011_2.jpg",
            "http://pic12.nipic.com/20101227/4750538_110523035117_2.jpg",
            "http://img2.duitang.com/uploads/item/201207/09/20120709191344_iKkfX.thumb.600_0.jpeg",
            "http://picm.photophoto.cn/087/006/180/0061800057.jpg",
            "http://pic1a.nipic.com/2008-10-20/2008102010494177_2.jpg",
            "http://pic22.nipic.com/20120711/8092962_215635683351_2.jpg",
            "http://img1.lvyou114.com/TukuMax/66/201252193134.jpg",
            "http://pic21.nipic.com/20120517/9806651_204206499115_2.jpg",
            "http://pic3.nipic.com/20090615/1242397_114546011_2.jpg",
            "http://pic12.nipic.com/20101227/4750538_110523035117_2.jpg",
            "http://img2.duitang.com/uploads/item/201207/09/20120709191344_iKkfX.thumb.600_0.jpeg",
            "http://picm.photophoto.cn/087/006/180/0061800057.jpg",

            "http://pic31.nipic.com/20130630/5892523_003233711151_2.jpg",
            "http://pic24.nipic.com/20121010/4388163_025151167143_2.jpg",
            "http://pic.nipic.com/2007-12-22/20071222144424844_2.jpg",
            "http://pic12.nipic.com/20101228/4151459_000644738360_2.jpg"
    };

    String url = "http://222.73.3.43/softfile.3g.qq.com/myapp/rcps/d/85439/com.tencent.android.qqdownloader_85439_150824110528a.apk?mkey=55deec9c807bc481&f=ae10&p=.apk";

    @Bind(R.id.list_View)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_image_activity);
        ButterKnife.bind(this);
        List<String> strings = new ArrayList<String>();
        strings.addAll(Arrays.asList(list));
        MAdapter mAdapter = new MAdapter();
        mAdapter.setList(strings);
        listView.setAdapter(mAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class MAdapter extends DhBaseAdapter<String> {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NetAfkImageView imageView;
            if (convertView == null) {
                imageView = new NetAfkImageView(parent.getContext());
                imageView.setLayoutParams(new AbsListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 600));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (NetAfkImageView) convertView;
            }
           imageView.setImageUrl(getItem(position), R.drawable.mm, R.drawable.ic_launcher);
            return imageView;
        }
    }
}
