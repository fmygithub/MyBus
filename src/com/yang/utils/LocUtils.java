package com.yang.utils;

import java.util.HashMap;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * 定位工具类
 * 
 * @author apple
 * 
 */
public class LocUtils {

	private static LocationClient locClient;
	private static boolean isFirst = true;

	/**
	 * 启动定位服务
	 * 
	 * @param context
	 * @param callback
	 * @param isNeedAddr
	 */
	public static void startLoc(final Context context, final Callback callback,
			boolean isNeedAddr) {

		if (locClient == null) { // 实例化定位客户端并注册事件及设置定位选项
			locClient = new LocationClient(context);

			LocationClientOption option = new LocationClientOption();
			option.setCoorType("bd09ll");
			option.setLocationMode(LocationMode.Hight_Accuracy);
			option.setOpenGps(true);
			option.setIsNeedAddress(isNeedAddr);
			option.setScanSpan(5000);

			locClient.setLocOption(option);

			locClient.registerLocationListener(new BDLocationListener() {

				@Override
				public void onReceiveLocation(BDLocation bdLoc) {
					// TODO 定位成功，回传定位位置信息
					if (isFirst) {
						System.out.println("经度："+bdLoc.getLongitude());
						// 通过接口回调，将数据回传给调用者
						callback.receiveLoc(bdLoc.getLongitude(),
								bdLoc.getLatitude(), bdLoc.getCity());
						isFirst = false;
					}

					// 将定位的位置保存到共享参数中
					SPUtils.saveLoc(context, bdLoc.getLongitude(),
							bdLoc.getLatitude());
				}
			});
		}

		// 启动定位服务
		locClient.start();
	}

	/**
	 * 测量给定的经纬度与定位的经纬度之间的距离
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @return 返回带有单位的距离，可能返回米、公里
	 */
	public static String distance(Context context, double x, double y) {
		// 从共享参数中获取最后一次定位的位置
		HashMap<String, Double> locInfo = SPUtils.getLoc(context);
		LatLng locLl = new LatLng(locInfo.get("y"), locInfo.get("x")); // 定位的坐标
		LatLng targetll = new LatLng(y, x); // 目标坐标

		// 获取两个坐标之间的间距，单位为米
		double dist = DistanceUtil.getDistance(locLl, targetll);

		String distTxt = "%.1f %s";
		if (dist < 1000) {
			return String.format(distTxt, dist, "米");
		} else {
			return String.format(distTxt, dist / 1000, "公里");
		}
	}

	// 停止定位服务
	public static void stopLoc() {
		if (locClient.isStarted()) {
			locClient.stop(); // 停止定位服务

			locClient = null;
		}
	}

	public interface Callback {
		// 回传纬度(x)和经度(y)
		public void receiveLoc(double x, double y, String city);
	}
}
