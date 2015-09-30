package com.dahanis.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dahanis.main.R;
import com.dahanis.main.ReturnObj;
import com.dahanis.main.TruckBean;
import com.dh.foundation.adapter.NetListViewBaseAdapter;
import com.dh.foundation.utils.RequestParams;
import com.dh.foundation.widget.netlistview.NetListViewCompat;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 网络加载ListView demo
 * Created By: Seal.Wu
 * Date: 2015/9/30
 * Time: 10:05
 */
public class NetListViewActivity extends Activity {


    @Bind(R.id.list_view)
    NetListViewCompat listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_list_view_activity);
        ButterKnife.bind(this);


        /**
         * 还是以以下的地址来进行说明，因些地址没有做分页功能，所以我们要专门的定制化下，若是有分页功能，则正常定制就可以了
         * http://m.dahanis.com:24080/BasicService.asmx/GetVehicleLengthList?
         * token=Zrmp6OJN8JilVNd66DSRntEQAzPtNXNdQzGUK8FDhavb9Lv%2BGSEmpqVvPXLgk0S00F1isuQY5R4%3D&userId=600000032&";
         */

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

        /**
         * 接着为ListView设置加载更多功能配置监听器
         */

        listView.setLoadMoreAbleListener(new NetListViewCompat.LoadMoreAbleListener<ReturnObj>() {
            @Override
            public boolean isLoadMoreAble(ReturnObj returnObj, List allListData) {
                /**
                 * 此处处理的是每次请求加载完数据后来判断是否可以再进行加载更多功能，常规做法是判断returnObj里返回的数据列表条数与期望的是否一致
                 * 比如如果我们有做分页功能，且每页请求数据是期望１０条，那么我们可以这么判断
                 *  if (returnObj.getTruckBeanList().size() == 10) {
                 *      return true;
                 *  }
                 *  当获得的数据条目数为１０的时候方可继续加载
                 *
                 *  然而我们并没有做分页，所有为了有继续加载的效果　我们就直接返回true，代表永远可以加载更多
                 */


                return true;
            }

            @Override
            public List<?> getLoadedData(ReturnObj returnObj) {
                /**
                 * 此处返回获取的对象中的列表
                 */
                return returnObj.getTruckBeanList();
            }
        });

        /**
         * 因为没有做分页，所以pageName分页名称我们就不传了
         */
        listView.initNetListView(baseAddress, params, new MBaseAdapter(), null);

        /**
         * OK,全部工作已经做完了，自动加载更多并自动获取网络数据的listView配制完成
         * 注:
         * adapter必须是继承自NetListViewBaseAdapter,且尖括号必须写上类型，第一个为返回对象类型，第二个为列表数据item类型
         * 像下面的MBaseAdapter一样
         *
         */


    }

    class MBaseAdapter extends NetListViewBaseAdapter<ReturnObj, TruckBean> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, listView, false);
            textView.setText(getItem(position).getId() + "  " + getItem(position).getLengthValue());
            return textView;
        }
    }
}
