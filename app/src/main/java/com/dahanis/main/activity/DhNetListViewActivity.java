package com.dahanis.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dahanis.main.R;
import com.dh.foundation.adapter.DhNetListViewBaseAdapter;
import com.dh.foundation.manager.FoundationManager;
import com.dh.foundation.utils.bluetooth.bluetoothbean.BaseBean;
import com.dh.foundation.widget.netlistview.DhDefaultLoadMoreAbleListener;
import com.dh.foundation.widget.netlistview.DhNetListView;
import com.dh.foundation.widget.netlistview.NetListView;
import com.google.gson.JsonObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By: Seal.Wu
 * Date: 2015/8/14
 * Time: 9:41
 */
public class DhNetListViewActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.listview)
    DhNetListView listview;
    @Bind(R.id.swipe)
    SwipeRefreshLayout swipe;


    @OnClick(R.id.load_image)
    public void to() {
        startActivity(new Intent(DhNetListViewActivity.this, LoadImageActivity.class));
    }


    private String url = "http://m.dahanis.com:24080/BasicService.asmx/GetVehicleLengthList?token=Zrmp6OJN8JilVNd66DSRntEQAzPtNXNdQzGUK8FDhavb9Lv%2BGSEmpqVvPXLgk0S00F1isuQY5R4%3D&userId=600000032&";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        FoundationManager.init(this);
        listview.initNetListView(url, null, new MBaseAdapter(), null);
        listview.setLoadMoreAbleListener(new DhDefaultLoadMoreAbleListener(){
            @Override
            public boolean isLoadMoreAble(BaseBean<List<JsonObject>> listBaseBean, List<?> allListData) {
                return true;
            }
        });
        listview.setOnLoadFinishListener(new NetListView.OnLoadFinishListener() {
            @Override
            public void onLoadFinished() {
                swipe.setRefreshing(false);
            }
        });
        swipe.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        listview.refreshData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class MBaseAdapter extends DhNetListViewBaseAdapter<BaseBean<List<Truck>>, Truck> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, listview, false);
            textView.setText(getItem(position).getId() + "  " + getItem(position).getLengthValue());
            return textView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    class Truck {

        /**
         * Creator :
         * CreateTime : null
         * ModifyTime : null
         * Modifier :
         * Id : 1
         * LengthCode : 001
         * LengthValue : 2.5
         */
        private String Creator;
        private String CreateTime;
        private String ModifyTime;
        private String Modifier;
        private int Id;
        private String LengthCode;
        private String LengthValue;

        public void setCreator(String Creator) {
            this.Creator = Creator;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public void setModifyTime(String ModifyTime) {
            this.ModifyTime = ModifyTime;
        }

        public void setModifier(String Modifier) {
            this.Modifier = Modifier;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public void setLengthCode(String LengthCode) {
            this.LengthCode = LengthCode;
        }

        public void setLengthValue(String LengthValue) {
            this.LengthValue = LengthValue;
        }

        public String getCreator() {
            return Creator;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public String getModifier() {
            return Modifier;
        }

        public int getId() {
            return Id;
        }

        public String getLengthCode() {
            return LengthCode;
        }

        public String getLengthValue() {
            return LengthValue;
        }
    }
}
