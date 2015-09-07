package com.dahanis.main.activity;


import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dahanis.main.R;
import com.dh.foundation.utils.DhHttpNetUtils;
import com.dh.foundation.utils.RequestParams;
import com.dh.foundation.utils.bluetooth.bluetoothbean.BaseBean;

public class LogisticsTabActivity extends TabActivity implements OnClickListener{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistics_main);


		DhHttpNetUtils.getData(this, "", new RequestParams(), new DhHttpNetUtils.RequestListener<BaseBean<String>>() {
			@Override
			public void onSuccessfully(BaseBean<String> stringBaseBean) {

			}

		});
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
