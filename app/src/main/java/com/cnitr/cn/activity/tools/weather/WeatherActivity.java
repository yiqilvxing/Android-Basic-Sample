package com.cnitr.cn.activity.tools.weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cnitr.cn.R;
import com.cnitr.cn.activity.BaseActivity;
import com.cnitr.cn.activity.tools.city.CitySearchActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 天气
 * Created by YangChen on 2018/11/1.
 */
public class WeatherActivity extends BaseActivity implements AMapLocationListener, EasyPermissions.PermissionCallbacks {

    @Bind(R.id.degreeText)
    TextView degreeText;

    @Bind(R.id.degreeText0)
    TextView degreeText0;

    @Bind(R.id.weatherInfoTmp)
    TextView weatherInfoTmp;

    @Bind(R.id.weatherInfoText)
    TextView weatherInfoText;

    @Bind(R.id.weatherInfoImage)
    ImageView weatherInfoImage;

    @Bind(R.id.tViewLocation)
    TextView tViewLocation;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;

    @Bind(R.id.weather_hourly)
    RecyclerView weather_hourly;

    @Bind(R.id.forecast_layout)
    LinearLayout forecast_layout;

    @Bind(R.id.weather_suggestion)
    View weather_suggestion;

    @Bind(R.id.comfort_text)
    TextView comfort_text;

    @Bind(R.id.car_wash_text)
    TextView car_wash_text;

    @Bind(R.id.sport_text)
    TextView sport_text;

    @Bind(R.id.cold_text)
    TextView cold_text;

    @Bind(R.id.clothes_text)
    TextView clothes_text;

    @Bind(R.id.uv_text)
    TextView uv_text;

    private String currentLocation = "深圳";
    private boolean getLocation = false;

    // 定位
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption;
    private double latitude;
    private double longitude;

    private static final int CODE_CITY = 1001;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_weather;
    }

    /**
     * 初始化
     */
    @Override
    protected void init() {
        swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "BEBAS.ttf");
        degreeText.setTypeface(typeFace);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //  请求天气预报
                requestWeather();
            }
        });

        tViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, CitySearchActivity.class);
                intent.putExtra("title", "切换城市");
                startActivityForResult(intent, CODE_CITY);
            }
        });

        swipe_refresh.setRefreshing(true);

        // 运行时权限检测
        checkPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(60 * 1000);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String city = aMapLocation.getCity();
                if (city != null && !TextUtils.isEmpty(city) && !getLocation) {
                    currentLocation = city;
                    getLocation = true;
                    requestWeather();
                }
                latitude = aMapLocation.getLatitude();//获取纬度
                longitude = aMapLocation.getLongitude();//获取经度

                String location = "{\"city\": \"" + city + "\",\"latitude\": \"" + latitude + "\",\"longitude\": \"" + longitude + "\"}";
                Log.i("location", "高德定位信息：" + location);
            } else {
                requestWeather();
            }
        }
    }

    /**
     * 请求天气预报
     */
    private void requestWeather() {

        if (currentLocation != null && !TextUtils.isEmpty(currentLocation)) {

            // 查询当前天气
            requestWeatherNow(currentLocation);

            // 查询逐小时天气
            requestWeatherHourly(currentLocation);

            // 查询3-10天预报
            requestWeatherForecast(currentLocation);

            // 查询生活指数
            requestWeatherLifeStyle(currentLocation);
        }
    }

    /**
     * 查询当前天气
     */
    private void requestWeatherNow(String city) {
        HeWeather.getWeatherNow(this, city, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                if (swipe_refresh != null) {
                    swipe_refresh.setRefreshing(false);
                    Log.e("", "", throwable);
                }
            }

            @Override
            public void onSuccess(List<Now> list) {
                if (swipe_refresh != null) {
                    swipe_refresh.setRefreshing(false);
                    if (list != null) {
                        Now now = list.get(0);
                        if (now != null) {
                            refreshWeather(now);
                        }
                    }
                }
            }
        });
    }

    /**
     * 刷新当前天气
     */
    private void refreshWeather(Now now) {
        if (tViewLocation == null)
            return;
        Basic basic = now.getBasic();
        if (basic != null) {
            String location = basic.getLocation();
            tViewLocation.setText(location == null ? "" : location);
        }
        NowBase nowbase = now.getNow();
        if (nowbase == null)
            return;
        tViewLocation.setVisibility(View.VISIBLE);
        String tmp = nowbase.getTmp();
        String CondTxt = nowbase.getCond_txt();
        degreeText.setText(tmp == null ? "" : tmp);
        degreeText0.setVisibility(tmp == null ? View.GONE : View.VISIBLE);
        weatherInfoText.setText(CondTxt == null ? "" : CondTxt);

        // 设置当前天气状态图标
        setWeatherImage(weatherInfoImage, nowbase.getCond_code());
    }

    /**
     * 查询逐小时天气
     */
    private void requestWeatherHourly(String city) {
        HeWeather.getWeatherHourly(this, city, new HeWeather.OnResultWeatherHourlyBeanListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(List<Hourly> list) {
                refreshWeatherHourly(list);
            }
        });

    }

    /**
     * 刷新逐小时天气
     */
    private void refreshWeatherHourly(List<Hourly> list) {
        if (list != null && !list.isEmpty()) {
            List<HourlyBase> hourlyBeanList = list.get(0).getHourly();
            HourAdapter adapter = new HourAdapter(hourlyBeanList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            weather_hourly.setLayoutManager(linearLayoutManager);
            weather_hourly.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 查询3-10天预报
     */
    private void requestWeatherForecast(String city) {
        HeWeather.getWeatherForecast(this, city, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(List<Forecast> list) {
                refreshWeatherForecast(list);
            }
        });

    }

    /**
     * 刷新3-10天预报
     */
    private void refreshWeatherForecast(List<Forecast> list) {
        if (forecast_layout == null)
            return;
        forecast_layout.removeAllViews();
        if (list != null && !list.isEmpty()) {
            Forecast forecast = list.get(0);
            List<ForecastBase> forecastList = forecast.getDaily_forecast();
            for (int i = 0; i < forecastList.size(); i++) {
                ForecastBase forecastBase = forecastList.get(i);
                if (forecastBase != null) {

                    // 将未来几天的天气添加到视图中
                    View view = LayoutInflater.from(this).inflate(R.layout.weather_forecast_item, forecast_layout, false);
                    TextView dateText = (TextView) view.findViewById(R.id.data_text);
                    TextView infoText = (TextView) view.findViewById(R.id.info_text);
                    TextView maxMinText = (TextView) view.findViewById(R.id.max_min_text);
                    ImageView weatherPic = (ImageView) view.findViewById(R.id.weather_pic);

                    // 设置天气状态图标
                    setWeatherImage(weatherPic, forecastBase.getCond_code_d());

                    dateText.setText(Time.parseTime(forecastBase.getDate()));
                    infoText.setText(forecastBase.getCond_txt_d());
                    maxMinText.setText(forecastBase.getTmp_min() + " ～ " + forecastBase.getTmp_max() + "°C");
                    forecast_layout.addView(view);

                    if (i == 0) {
                        weatherInfoTmp.setText(forecastBase.getTmp_min() + "°" + "/" + forecastBase.getTmp_max() + "°");
                    }
                }
            }

        }
    }

    /**
     * 设置天气状态图标
     */
    private void setWeatherImage(ImageView weatherPic, String code) {
        String weatherCode = "weather_" + code;
        int resId = getResources().getIdentifier(weatherCode, "drawable", this.getPackageName());
        if (resId != 0) {
            weatherPic.setImageResource(resId);
        } else {
            if (weatherCode.startsWith("weather_1")) {
                weatherPic.setImageResource(R.drawable.weather_100);
            } else if (weatherCode.startsWith("weather_2")) {
                weatherPic.setImageResource(R.drawable.weather_200);
            } else if (weatherCode.startsWith("weather_3")) {
                weatherPic.setImageResource(R.drawable.weather_300);
            } else if (weatherCode.startsWith("weather_4")) {
                weatherPic.setImageResource(R.drawable.weather_400);
            } else if (weatherCode.startsWith("weather_5")) {
                weatherPic.setImageResource(R.drawable.weather_500);
            } else {
                weatherPic.setImageResource(R.drawable.weather_100);
            }
        }
    }

    /**
     * 查询生活指数
     */
    private void requestWeatherLifeStyle(String city) {
        HeWeather.getWeatherLifeStyle(this, city, new HeWeather.OnResultWeatherLifeStyleBeanListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(List<Lifestyle> list) {
                refreshWeatherLifeStyle(list);
            }
        });

    }

    /**
     * 刷新生活指数
     */
    private void refreshWeatherLifeStyle(List<Lifestyle> list) {
        if (comfort_text == null)
            return;
        if (list != null && !list.isEmpty()) {
            Lifestyle forecast = list.get(0);
            List<LifestyleBase> forecastList = forecast.getLifestyle();
            if (forecastList != null && !forecastList.isEmpty()) {
                weather_suggestion.setVisibility(View.VISIBLE);
                for (LifestyleBase lifestyleBase : forecastList) {
                    String result = lifestyleBase.getBrf() == null ? "" : lifestyleBase.getBrf();
                    switch (lifestyleBase.getType()) {
                        case "comf":
                            comfort_text.setText(result);
                            break;
                        case "cw":
                            car_wash_text.setText(result);
                            break;
                        case "sport":
                            sport_text.setText(result);
                            break;
                        case "flu":
                            cold_text.setText(result);
                            break;
                        case "drsg":
                            clothes_text.setText(result);
                            break;
                        case "uv":
                            uv_text.setText(result);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE_CITY) {
            if (data != null) {
                String city = data.getStringExtra("city");
                if (!TextUtils.isEmpty(city)) {
                    currentLocation = city;
                    requestWeather();
                }
            }
        }
    }

    // 运行时权限申请
    private static final String[] ACCESS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int RC_ACCESS_LOCATION = 102;

    @AfterPermissionGranted(RC_ACCESS_LOCATION)
    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(this, ACCESS_LOCATION)) {
            initLocation();
        } else {
            EasyPermissions.requestPermissions(this, "需要GPS定位权限", RC_ACCESS_LOCATION, ACCESS_LOCATION);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.requestPermissions(this, "需要GPS定位权限", RC_ACCESS_LOCATION, ACCESS_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RC_ACCESS_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

}
