package com.sm.smmap.smmap.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.AppUtils;
import com.sm.smmap.smmap.Bean.NumberBean;
import com.sm.smmap.smmap.MainActivity;
import com.sm.smmap.smmap.R;
import com.sm.smmap.smmap.Retrofit.DataBean;
import com.sm.smmap.smmap.Retrofit.MyURL;
import com.sm.smmap.smmap.Retrofit.RetrofitFactory;
import com.sm.smmap.smmap.Utils.Bean.DownBean;
import com.sm.smmap.smmap.Utils.http.OkGoUpdateHttpUtil;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.listener.ExceptionHandler;
import com.vector.update_app.listener.IUpdateDialogFragmentListener;
import com.vector.update_app.utils.ColorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MyUtils {
    private static Context context;
    private static boolean typeAppup = false;//??????????????????

    public static void getPermissions(MainActivity mainActivity) {
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(mainActivity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//?????????????????????????????????????????????
//            Toast.makeText(LoginActivity.this,"????????????",Toast.LENGTH_LONG).show();
        } else {//??????????????????
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//???List????????????
            ActivityCompat.requestPermissions(mainActivity, permissions, MY_PERMISSIONS_REQUEST_CALL_CAMERA);
        }
    }

    public MyUtils(Context context) {
        this.context = context;
    }

    //??????
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;
    // ??????????????????????????????????????????????????????????????????????????????
    public static List<String> mPermissionList = new ArrayList<>();
    public static String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,

            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.GET_TASKS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
    };//???????????????

    /**
     * ????????????
     */
    public static void getPermissions() {//??????????????????
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//?????????????????????????????????????????????
//            Toast.makeText(LoginActivity.this,"????????????",Toast.LENGTH_LONG).show();
        } else {//??????????????????
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//???List????????????
            ActivityCompat.requestPermissions((Activity) context, permissions, MY_PERMISSIONS_REQUEST_CALL_CAMERA);
        }
    }

    @SuppressLint("NewApi")
    public static void setStatBar(MainActivity mainActivity) {//????????????????????????????????????
        View decorView = mainActivity.getWindow().getDecorView();
        int option =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        mainActivity.getWindow().setStatusBarColor(Color.TRANSPARENT
        );
    }

    public static void Viewjizhan(Marker markerMy, BaiduMap mBaiduMap, DataBean dataBean) {
        if (markerMy != null) {
            LatLng positionjingbaojizhan = markerMy.getPosition();//???????????????
            Log.d(TAG, "onReceiveLocation: markerMy" + "?????????");
            //?????????
            int TAS = 78;
            String ta = ACacheUtil.getTa();
            int i = Integer.parseInt(ta);
            int Myradius = i * TAS;
            OverlayOptions ooCircle = new CircleOptions()
//                            .fillColor(0x000000FF)
                    .fillColor(Color.argb(40, 176,
                            224,
                            230))
                    .center(positionjingbaojizhan)
                    .stroke(new Stroke(2, Color.rgb(135,
                            206,
                            235)))
                    .radius(Myradius);
            LatLng center = ((CircleOptions) ooCircle).getCenter();
            Log.d("nzq", "onCreate: " + center);
            mBaiduMap.addOverlay(ooCircle);
            //??????
//                    LatLng llDot = new LatLng(39.90923, 116.447428);
            OverlayOptions ooDot = new DotOptions().center(positionjingbaojizhan).radius(6).color(0xFF0000FF);
            mBaiduMap.addOverlay(ooDot);
            //??????Marker??????
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.jizhan1);
            //??????MarkerOption???????????????????????????Marker
            Bundle bundles = new Bundle();
            bundles.putString("mcc", dataBean.getResult().getMcc());
            bundles.putString("mnc", dataBean.getResult().getMnc());
            bundles.putString("lac", dataBean.getResult().getLac());
            bundles.putString("ci", dataBean.getResult().getCi());
            bundles.putString("lat", String.valueOf(positionjingbaojizhan.latitude));
            bundles.putString("lon", String.valueOf(positionjingbaojizhan.longitude));
            bundles.putString("radius", dataBean.getResult().getRadius());
            bundles.putString("address", dataBean.getResult().getAddress());
            OverlayOptions optiona = new MarkerOptions()
                    .anchor(10, 30)
                    .extraInfo(bundles)
                    .position(positionjingbaojizhan) //????????????
                    .perspective(true)
                    .icon(bitmap) //????????????
                    .draggable(true)
                    .draggable(true)
                    //?????????????????????????????????????????????????????????
                    .flat(true)
                    .alpha(0.5f);
            //??????????????????Marker????????????
            markerMy = (Marker) mBaiduMap.addOverlay(optiona);//??????????????????????????????  Marker marker
            //??????Marker??????
//            Log.d(TAG, "pointsonMapClick: " + points.size());
        }
    }
    /**
     * ??????2???????????????
     * yyyy-MM-dd HH:mm ??????????????????????????????????????????????????????
     * @param startTime
     * @param endTime
     * @return
     */
    public static String DATE_TO_STRING_DETAIAL_PATTERN = "yyyy-MM-dd";
    public static boolean getTimeCompareSize(String startTime, String endTime){

        boolean str = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TO_STRING_DETAIAL_PATTERN);//???-???-??? ???-???
        try {
            Date date1 = dateFormat.parse(startTime);//????????????
            Date date2 = dateFormat.parse(endTime);//????????????
            // 1 ?????????????????????????????? 2 ????????????????????????????????? 3 ??????????????????????????????
            if (date1.getTime()<date2.getTime()){
                //??????????????????????????????.
                str=true;
            }else{
                str=false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str ;
    }
    //????????????
    public static void upApp(final Context context, String appId) {
        RetrofitFactory.getInstence().API().download("1").enqueue(new Callback<DownBean>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<DownBean> call, final Response<DownBean> response) {
                Log.d(TAG, "upApponResponse: " + response.body());
                if (response.body().getData() != null) {
                    String versionCode = response.body().getData().getVersionCode();
                    Log.i("TAGUp", "onResponse: "+versionCode+"--"+AppUtils.getAppVersionCode());
                    int DoubuleversionCode = Integer.parseInt(versionCode);
                    int appVersionCode = AppUtils.getAppVersionCode();

                    //??????????????????
                    if (response.body().getData().getAppType() == 1) {
                        typeAppup = true;
                    } else {
                        typeAppup = false;
                    }
//
                    if (DoubuleversionCode > appVersionCode) {// ??????????????? ????????????APP ??????
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

                        Map<String, String> params = new HashMap<String, String>();

                        params.put("appId", "1");
//        params.put("appVersion", AppUpdateUtils.getVersionName((Activity) context));

                        new UpdateAppManager
                                .Builder()
                                //?????????????????????Activity
                                .setActivity((Activity) context)
                                //?????????????????????httpManager???????????????
                                .setHttpManager(new OkGoUpdateHttpUtil())
                                //???????????????????????????
//                .setUpdateUrl(MyURL.BASE_URL + "UpdateInfo.aspx")
                                .setUpdateUrl(MyURL.DOWNLOAD)
                                .handleException(new ExceptionHandler() {
                                    @Override
                                    public void onException(Exception e) {
                                        e.printStackTrace();
                                    }
                                })
                                //???????????????????????????
                                //???????????????????????????get
                                .setPost(false)
                                //??????????????????????????????version=1.0.0???app???versionName??????apkKey=??????????????????AndroidManifest.xml?????????
                                .setParams(params)

                                //?????????????????????????????????????????????????????????????????????????????????????????????
                                .hideDialogOnDownloading()
                                //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                                .setTopPic(R.mipmap.top_3)
                                //????????????????????????????????????????????????????????????????????????
                                .setThemeColor(ColorUtil.getRandomColor())
                                .setThemeColor(Color.rgb(255, 0, 0))
                                //??????apk????????????????????????????????????sd??????/Download/1.0.0/test.apk
                                .setTargetPath(path)
                                //??????appKey????????????AndroidManifest.xml?????????????????????????????????????????????????????????
                                //.setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")
                                //???????????????????????????
//                                .dismissNotificationProgress()
                                //??????????????????
                                //.showIgnoreVersion()
                                .setIgnoreDefParams(true)
                                .setUpdateDialogFragmentListener(new IUpdateDialogFragmentListener() {
                                    @Override
                                    public void onUpdateNotifyDialogCancel(UpdateAppBean updateApp) {
                                        String apkFileUrl = updateApp.getApkFileUrl();
                                        Log.d(TAG, "onUpdateNotifyDialogCancel: " + apkFileUrl);
                                    }
                                })
                                .build()
                                //????????????????????????
                                .checkNewApp(new UpdateCallback() {
                                    /**
                                     * ??????json,???????????????
                                     *
                                     * @param json ??????????????????json
                                     * @return UpdateAppBean
                                     */
                                    @Override
                                    protected UpdateAppBean parseJson(String json) {
                                        Log.d(TAG, "parseJson: " + json);
                                        UpdateAppBean updateAppBean = new UpdateAppBean();
                                        try {
                                            JSONObject jsonObject = new JSONObject(json);
                                            updateAppBean
                                                    //????????????????????????Yes,No
                                                    .setUpdate("Yes")
//                                    .setUpdate("e")
                                                    //???????????????????????????
                                                    .setNewVersion(jsonObject.optString("versionName"))
//
                                                    //????????????????????????
//                                                    .setApkFileUrl(response.body().getData().getAppPath() + "")
                                                    .setApkFileUrl("http://114.116.149.148:81/uploads/com.sm.map.apk")
                                                    .setUpdateDefDialogTitle("????????????????????????" + response.body().getData().getVersionName() + "")
                                                    //????????????????????????
                                                    .setUpdateLog(response.body().getData().getNewFunction())
                                                    //???????????????????????????????????????????????????
//                                    .setTargetSize(jsonObject.optString("newFunction"))
                                                    //????????????????????????????????????

                                                    .setConstraint(typeAppup);
                                            //??????md5??????????????????
//                                    .setNewMd5(jsonObject.optString("new_md51"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        return updateAppBean;
                                    }

                                    /**
                                     * ??????????????????
                                     */
                                    @Override
                                    public void onBefore() {
                                        CProgressDialogUtils.showProgressDialog((Activity) context);
                                    }

                                    /**
                                     * ??????????????????
                                     */
                                    @Override
                                    public void onAfter() {
                                        CProgressDialogUtils.cancelProgressDialog((Activity) context);
                                    }

                                });


                    }

                }
            }

            @Override
            public void onFailure(Call<DownBean> call, Throwable t) {
                Log.d(TAG, "upApponFailure: " + t.getMessage());
            }
        });

    }

    //????????????
    public static void getNumber(String appId) {
        RetrofitFactory.getInstence().API().NUMBER(appId).enqueue(new Callback<NumberBean>() {
            @Override
            public void onResponse(Call<NumberBean> call, Response<NumberBean> response) {
                if (response.body().getData() != null) {
                    ACacheUtil.putNumberMax(response.body().getData().getTotal() + "");//??????????????????
                    ACacheUtil.putNumberremainder(response.body().getData().getRemainder() + "");
                    Log.d(TAG, "getNumberonResponse: " + "??????" + ACacheUtil.getNumberMax() + "??????:" + ACacheUtil.getNumberremainder());
                }
            }

            @Override
            public void onFailure(Call<NumberBean> call, Throwable t) {

            }
        });
    }

    //?????????String
    public static String listToString(List<Double> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (Double string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    public static List<Double> StringTolist(String str) {

        List list = Arrays.asList(str.split(","));
        List<Double> integerList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
//            Integer.parseInt((String) list.get(i));
//            integerList.add(Integer.parseInt((String) list.get(i)));
            integerList.add(Double.parseDouble((String) list.get(i)));
        }
        return integerList;
    }
    public static List<BillObject> removeDuplicate(List<BillObject> list) {

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getTac().equals(list.get(i).getTac()) && list.get(j).getCi().equals(list.get(i).getCi())) {
                    list.remove(j);
                }
            }
        }

        return list;
    }
}
