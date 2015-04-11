package com.yang.fragment;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.yang.mybus.R;

@SuppressLint("DefaultLocale")
public class OfflineAllFragment extends Fragment implements MKOfflineMapListener{
	
	private MKOfflineMap offline = null;
	private ListView hotcity_list;
	private ListView allcity_list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_offline_all, null);
		hotcity_list = (ListView) view.findViewById(R.id.hotcitylist);
		allcity_list = (ListView) view.findViewById(R.id.allcitylist);
		
		offline = new MKOfflineMap();
		offline.init(this);
		
		ArrayList<String> hotcityList = new ArrayList<String>();
		// 获取热门城市列表
		ArrayList<MKOLSearchRecord> records1 = offline.getHotCityList();
		if (records1 != null){
			for (MKOLSearchRecord r : records1) {
				hotcityList.add(r.cityName + "   " + this.formatDataSize(r.size));
			}
		}
		ListAdapter adapter_hot = (ListAdapter) new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, hotcityList);
		hotcity_list.setAdapter(adapter_hot);
		
		ArrayList<String> allcityList = new ArrayList<String>();
		// 获取所有可下载的城市列表
		ArrayList<MKOLSearchRecord> records2 = offline.getOfflineCityList();
		if (records2 != null) {
			for (MKOLSearchRecord r : records2) {
				allcityList.add(r.cityName + "   " + this.formatDataSize(r.size));
			}
		}
		ListAdapter adapter_all = (ListAdapter) new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, allcityList);
		allcity_list.setAdapter(adapter_all);
		return view;
	}

	@Override
	public void onGetOfflineMapState(int type, int state) {

	}
	
	/**
	 * 
	 * @Description: 获取下载包的大小
	 * @return String
	 * @author: fengmengyang
	 * @date: 2015年4月10日
	 */
	@SuppressLint("DefaultLocale")
	public String formatDataSize(int size) {
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = String.format("%dK", size / 1024);
		} else {
			ret = String.format("%.1fM", size / (1024 * 1024.0));
		}
		return ret;
	}

}
