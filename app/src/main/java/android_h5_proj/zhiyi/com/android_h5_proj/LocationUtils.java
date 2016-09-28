package android_h5_proj.zhiyi.com.android_h5_proj;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by waynezu on 16/3/2.
 */
public class LocationUtils {
    public AMapLocationClient mLocationClient = null;
    public AMapLocationListener mLocationListener;
    public AMapLocationClientOption mLocationOption = null;

    public void initLoacation(Context context) {
        mLocationClient = new AMapLocationClient(context);

        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public interface LocatedCallback {
        void locatedCallback(String locatedInfo);
    }

    private LocatedCallback mCallback;

    public void setLocatedCallback(LocatedCallback callback) {
        this.mCallback = callback;
    }

    public void getProvinceAndCity() {
        final TreeMap<String, String> map = new TreeMap<>();
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        double latitude = aMapLocation.getLatitude();//获取纬度
                        double longitude = aMapLocation.getLongitude();//获取经度
                        aMapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);//定位时间
                        String address = aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        String country = aMapLocation.getCountry();//国家信息
                        String province = aMapLocation.getProvince();//省信息
                        String city = aMapLocation.getCity();//城市信息
                        String district = aMapLocation.getDistrict();//城区信息
                        String street = aMapLocation.getStreet();//街道信息
                        aMapLocation.getStreetNum();//街道门牌号信息
                        aMapLocation.getCityCode();//城市编码
                        aMapLocation.getAdCode();//地区编码
                        Log.e("定位信息", address + "," + country + "," + province + "," + city + "," + district + "," + street + "," + longitude + "," + latitude);
                        if (province.equals(city)) {
                            map.put("province", city);
                            map.put("city", district);
                        } else {
                            map.put("province", province);
                            map.put("city", city);
                        }
                        Gson gson = new Gson();
                        String locatedInfo = gson.toJson(map);
                        mCallback.locatedCallback(locatedInfo);
                        stopLoacation();
                    } else {
                        mCallback.locatedCallback("定位服务启动失败");
                        stopLoacation();
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);
    }

    public void stopLoacation() {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
    }
}
