package com.yang.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

	private static String SH_NAME = "appsets";

	/**
	 * 保存定位到的位置数据
	 * 
	 * @param context
	 * @param x
	 * @param y
	 */
	public static void saveLoc(Context context, double x, double y) {
		SharedPreferences sp = context.getSharedPreferences(SH_NAME,
				Context.MODE_PRIVATE);

		// 获取共享参数的编辑对象
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("loc_x", String.valueOf(x));
		editor.putString("loc_y", String.valueOf(y));

		editor.commit();// 提供本次编辑的数据
	}

	/**
	 * 获取最后一次保存定位的位置
	 * 
	 * @param context
	 * @return
	 */
	public static HashMap<String, Double> getLoc(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SH_NAME,
				Context.MODE_PRIVATE);
		// (116.377041,40.043142)天丰利商城
		HashMap<String, Double> locInfo = new HashMap<String, Double>();
		locInfo.put("x",
				Double.parseDouble(sp.getString("loc_x", "116.377041")));
		locInfo.put("y", Double.parseDouble(sp.getString("loc_y", "40.043142")));

		return locInfo;
	}

}
