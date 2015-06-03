package info.smemo.wechatvideorecorder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import java.util.List;
import java.util.Locale;

public class PhoneUtil {


	/**
	 * 获取传感器列表 type name vendor
	 * 
	 * @param context
	 * @return
	 */
	public static String getSensorList(Context context) {
		SensorManager mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if(null == mSensorManager)
			return "";
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		StringBuilder builder = new StringBuilder();
		for (Sensor sensor : sensors)
			builder.append(sensor.getType() + "," + sensor.getName() + ","
					+ sensor.getVendor() + "|");
		return builder.toString().substring(0, builder.toString().length() - 1);
	}

	public static boolean isOpenNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		} else {
			return false;
		}
	}

	/**
	 * WIFI是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static float getDensity(Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		return displayMetrics.density;
	}

	/**
	 * 获取当前手机的IMSI号
	 */
	public static String getIMSI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if(null == telephonyManager)
			return "";
		String imsi = telephonyManager.getSubscriberId();
		if(StringUtil.isEmpty(imsi))
			return "";
		return imsi;
	}

	/**
	 * 获取当前手机的IMEI号
	 */
	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if(null == telephonyManager)
			return "";
		String id=telephonyManager.getDeviceId();
		if(StringUtil.isEmpty(id))
			return "";
		return telephonyManager.getDeviceId();
	}

	/**
	 * 获取当前手机的Mac号
	 */
	public static String getMac(Context context) {
		try {
			String macAddress = null;
			WifiManager wifiMgr = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
			if (null != info) {
				macAddress = info.getMacAddress();
				if (StringUtil.isEmpty(macAddress))
					return "";
				macAddress = macAddress.replaceAll(":", "");
				macAddress = macAddress.toLowerCase(Locale.CHINA);
				return macAddress;

			}
			return "";
		}catch (Exception e){
			return "";
		}
	}

	/**
	 * 获取当前手机当前wifi的SSID
	 */
	public static String getSSIDWithContext(Context context) {
		WifiManager mWifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo iWifi = mWifi.getConnectionInfo();
		if (null == iWifi)
			return "";
		String wifiName = iWifi.getSSID();
		if (StringUtil.isEmpty(wifiName))
			return "";
		return wifiName;
	}

	/**
	 * 获取当前连接WIFI的mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getSSIDWithMac(Context context) {
		WifiManager mWifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo iWifi = mWifi.getConnectionInfo();
		if (null == iWifi)
			return "";
		String mac = iWifi.getBSSID();
		if (StringUtil.isEmpty(mac))
			return "";
		mac = mac.replaceAll(":", "");
		mac = mac.toLowerCase(Locale.CHINA);
		return mac;
	}

	/**
	 * 获取手机的信息提供商
	 */
	public static String getPhoneType(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telManager.getSubscriberId();
		if (StringUtil.isEmpty(imsi))
			return "";
		if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
			return "中国移动";
		} else if (imsi.startsWith("46001")) {
			return "中国联通";
		} else if (imsi.startsWith("46003")) {
			return "中国电信";
		} else {
			return "";
		}
	}

	/**
	 * get resolution
	 * 
	 * @param context
	 * @return
	 */
	public static int[] getResolution(Context context) {
		int resolution[] = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		PhoneManager.getWindowManger(context).getDefaultDisplay()
				.getMetrics(dm);
		resolution[0] = dm.widthPixels;
		resolution[1] = dm.heightPixels;
		return resolution;
	}

	/**
	 * 判断是否为平板
	 * 
	 * @return
	 */
	public static boolean isPad(Context context) {
		WindowManager wm = PhoneManager.getWindowManger(context);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// 屏幕尺寸
		double screenInches = Math.sqrt(x + y);
		// 大于6尺寸则为Pad
		if (screenInches >= 6.0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取GPS经纬度坐标
	 * 
	 * @param context
	 * @return
	 */
	public static String getGPSLocation(Context context) {
		double[] long_lat = new double[2];
		LocationManager locationManager = null;
		boolean gps = false;
		try {
			// A LocationManager for controlling location (e.g., GPS) updates.
			locationManager = PhoneManager.getLocationManager(context);
			if (null != locationManager)
				gps = locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
			criteria.setAltitudeRequired(false);// 不要求海拔
			criteria.setBearingRequired(false);// 不要求方位
			criteria.setCostAllowed(true);// 允许有花费
			criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
			// 从可用的位置提供器中，匹配以上标准的最佳提供器
			String provider = locationManager.getBestProvider(criteria, true);

			// 获得最后一次变化的位置
			Location location = locationManager.getLastKnownLocation(provider);
			if (null == location) {
				return "::" + (gps ? 1 : 0);
			}
			long_lat[0] = location.getLongitude();
			long_lat[1] = location.getLatitude();

		} catch (Exception e) {
			return "error:" + e.toString();
		}
		return long_lat[0] + ":" + long_lat[1] + ":" + (gps ? 1 : 0);
	}

	public static String getGpsLongitude(Context context) {
		if (getGpsData(context) == null)
			return "";
		return String.valueOf(getGpsData(context)[0]);
	}

	public static String getGpsLatitude(Context context) {
		if (getGpsData(context) == null)
			return "";
		return String.valueOf(getGpsData(context)[1]);
	}

	public static double[] getGpsData(Context context) {
		double[] long_lat = new double[2];
		LocationManager locationManager = null;
		try {
			// A LocationManager for controlling location (e.g., GPS) updates.
			locationManager = PhoneManager.getLocationManager(context);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
			criteria.setAltitudeRequired(false);// 不要求海拔
			criteria.setBearingRequired(false);// 不要求方位
			criteria.setCostAllowed(true);// 允许有花费
			criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
			// 从可用的位置提供器中，匹配以上标准的最佳提供器
			String provider = locationManager.getBestProvider(criteria, true);

			// 获得最后一次变化的位置
			Location location = locationManager.getLastKnownLocation(provider);
			if (null == location) {
				return null;
			}
			long_lat[0] = location.getLongitude();
			long_lat[1] = location.getLatitude();

		} catch (Exception e) {
			return null;
		}

		return long_lat;
	}

	/**
	 * 获取基站坐标
	 * 
	 * @param context
	 * @return
	 */
	public static String getLBSLocation(Context context) {
		/** 网络编号46000，46002编号(134/159号段):中国移动 46001:中国联通 46003:中国电信 */
		String simOper = "";
		/**
		 * cid(GMS),lac(GMS),networkid=默认字段(GMS) cid:基站小区号(CDMA) lac:系统标识(CDMA)
		 * networkid(CDMA)
		 */
		/** 基站编码 */
		int cid = 0;
		/** 位置区域码 */
		int lac = 0;
		int networkid = 0;

		try {
			TelephonyManager tm = PhoneManager.getTelephonyManager(context);
			// SIM卡已准备好:SIM_STATE_READY=5
			if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
				// 取得SIM卡供货商代码,判断运营商是中国移动\中国联通\中国电信
				// 我国为460；中国移动为00，中国联通为01,中国电信为03
				simOper = tm.getSimOperator();
				/**
				 * 获取网络类型 NETWORK_TYPE_CDMA 网络类型为CDMA NETWORK_TYPE_EDGE
				 * 网络类型为EDGE NETWORK_TYPE_EVDO_0 网络类型为EVDO0 NETWORK_TYPE_EVDO_A
				 * 网络类型为EVDOA NETWORK_TYPE_GPRS 网络类型为GPRS NETWORK_TYPE_HSDPA
				 * 网络类型为HSDPA NETWORK_TYPE_HSPA 网络类型为HSPA NETWORK_TYPE_HSUPA
				 * 网络类型为HSUPA NETWORK_TYPE_UMTS 网络类型为UMTS
				 * 
				 * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
				 */
				int type = tm.getNetworkType();
				if (type == TelephonyManager.PHONE_TYPE_GSM // GSM网
						|| type == TelephonyManager.NETWORK_TYPE_EDGE
						|| type == TelephonyManager.NETWORK_TYPE_HSDPA) {

					GsmCellLocation gsm = ((GsmCellLocation) tm
							.getCellLocation());
					if (gsm != null) {
						// 取得SIM卡供货商代码,判断运营商是中国移动\中国联通\中国电信
						// 我国为460；中国移动为00，中国联通为01,中国电信为03
						simOper = tm.getSimOperator();
						// 基站ID
						cid = gsm.getCid();
						// 区域码
						lac = gsm.getLac();

						networkid = 0;
					} else {

					}
				} else if (type == TelephonyManager.NETWORK_TYPE_CDMA // 电信cdma网
						|| type == TelephonyManager.NETWORK_TYPE_1xRTT
						|| type == TelephonyManager.NETWORK_TYPE_EVDO_0
						|| type == TelephonyManager.NETWORK_TYPE_EVDO_A) {

					CdmaCellLocation cdma = (CdmaCellLocation) tm
							.getCellLocation();
					if (cdma != null) {
						// 运营商1
						String mcc = tm.getNetworkOperator().substring(0, 3);
						// 运营商2
						String mnc = String.valueOf(cdma.getSystemId());
						simOper = mcc + mnc;
						// 基站ID
						cid = cdma.getBaseStationId();
						// 区域码
						lac = cdma.getSystemId();

						networkid = cdma.getNetworkId();
					} else {

					}
				}
			}
		} catch (Exception e) {
			return "";
		}
		return simOper + ":" + cid + ":" + lac + ":" + networkid;
	}

}
