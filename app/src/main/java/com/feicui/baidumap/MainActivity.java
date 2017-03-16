package com.feicui.baidumap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_sate)
    Button btnSate;
    @BindView(R.id.btn_location)
    Button btnLocation;
    private MapView mapView;
    private BaiduMap map;
    private LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /**
         * 1.找到MapView
         * 2.获取操作地图的控制器
         * 3.通过切换实现地图变化
         */
        mapView = (MapView) findViewById(R.id.mv_view);//快捷键Ctrl+alt+f局部变量  v成员变量也称全局变量
        map = mapView.getMap();
        //地图状态
        MapStatus mapStatus = new MapStatus.Builder().overlook(0).zoom(14).build();
        BaiduMapOptions options = new BaiduMapOptions().zoomGesturesEnabled(true).mapStatus(mapStatus);
        //MapView mapView=new MapView(this,options);

        map.setOnMapStatusChangeListener(mapStatusListener);
    }

    //地图状态的监听
    private BaiduMap.OnMapStatusChangeListener mapStatusListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            Toast.makeText(MainActivity.this, "移动的维度:" + mapStatus.target.latitude + " 经度:" + mapStatus.target.longitude, Toast.LENGTH_SHORT).show();
        }
    };
    @OnClick({R.id.btn_sate, R.id.btn_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sate:
        //切换视图
        if (map.getMapType() == BaiduMap.MAP_TYPE_SATELLITE) {
            map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            btnSate.setText("普通视图");
            return;
        } else {
            map.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            btnSate.setText("卫星视图");
        }
                break;
            case R.id.btn_location:{
                //定位 1.开启定位图层

                //打开定位
                map.setMyLocationEnabled(true);
                //初始化定位
                locationClient = new LocationClient(getApplicationContext());
                //配置
                LocationClientOption option=new LocationClientOption();
                option.setOpenGps(true);
                option.setCoorType("bd09ll");
                option.setIsNeedAddress(true);
                option.setScanSpan(5000);

                //设置监听
                locationClient.registerLocationListener(locationListener);
                //开启定位
                locationClient.start();
                locationClient.requestLocation();
            }
                break;
        }
    }
    private BDLocationListener locationListener=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation==null){
                locationClient.requestLocation();
                return;
            }
            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();
            Toast.makeText(MainActivity.this, "纬度:"+latitude+" 经度;"+longitude, Toast.LENGTH_SHORT).show();
        }
    };
}
