/*
package com.sm.smmap.smmap.drive_test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthNr;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sm.smmap.smmap.R;
import com.sm.smmap.smmap.Utils.GCJ02ToWGS84Util;
import com.sm.smmap.smmap.Utils.MyToast;
import com.sm.smmap.smmap.lte_nr.My4GAdapter;
import com.sm.smmap.smmap.lte_nr.My5GAdapter;
import com.sm.smmap.smmap.lte_nr.To4GBean;
import com.sm.smmap.smmap.lte_nr.To5GBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sm.smmap.smmap.Utils.DtUtils.getBand;

public class DriveTestActivity extends AppCompatActivity {
    public static DriveTestActivity mainActivity;
    private String lon;
    private String lat;
    private TextView tv_location;

    private ArrayList<To4GBean> array4GList=new ArrayList<>();
    private ArrayList<DriveTest4GBean> driveTest4GBeans=new ArrayList<>();
    private String operator;
    private TextView tv_rssl;
    private TextView tv_rsrp;
    private TextView tv_rsrq;
    private TextView tv_ssRSRP;
    private TextView tv_ssRSRQ;
    private TextView tv_ssSinr;
    private int band;
    private TelephonyManager tm;
    Handler handlerUp = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void handleMessage(Message msg) {
            if(msg.what==8){
                getData();
            }
        }
    };
    Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ArrayList<To5GBean> string = (ArrayList<To5GBean>) msg.obj;

                    if (string.size() > 1) {
                        if(operator!=null){
                            if("CUCC".equals(operator)){
                                iv_log.setImageResource(R.drawable.cucc);
                            }
                            if("CTCC".equals(operator)){
                                iv_log.setImageResource(R.drawable.ctcc);
                            }
                            if("CMCC".equals(operator)){
                                iv_log.setImageResource(R.drawable.cmcc);
                            }
                        }
                        Log.i("?????????", "handleMessage: " + string.get(0).getPCI());
                        tv_rssl.setText(string.get(1).getSINR() + "");
                        tv_rsrp.setText(string.get(1).getRSRP() + "");
                        tv_rsrq.setText(string.get(1).getRSRQ() + "");
                        tv_ssRSRP.setText(string.get(1).getRSRP() + "");
                        tv_ssRSRQ.setText(string.get(1).getRSRQ() + "");
                        tv_ssSinr.setText(string.get(1).getSINR() + "");

                        tv_mobile.setText(string.get(1).getNr());
                        tv_mobile2.setText(string.get(1).getNr());
                        tv_operator.setText(operator);
                        tv_mcc.setText(mccString);
                        tv_mnc.setText(string.get(1).getMnc());
                        tv_cell_type.setText(string.get(1).getNr());
                        tv_cell_type2.setText(string.get(1).getNrType());
                        tv_pci.setText(string.get(1).getPCI());
                        tv_ci.setText(string.get(1).getCID());
                        tv_arfcn.setText(string.get(1).getNR_ARFCN());
                        tv_bands.setText(string.get(1).getBAND()+"");

                        tv_name.setText(name);
                        if (string.get(1).getLac() > 0) {
                            tv_lacTo.setVisibility(View.VISIBLE);
                            tv_pscTo.setVisibility(View.VISIBLE);
                            tv_lac.setVisibility(View.VISIBLE);
                            tv_psc.setVisibility(View.VISIBLE);
                            tv_view.setVisibility(View.VISIBLE);
                            tv_view2.setVisibility(View.VISIBLE);
                            tv_psc.setText(string.get(1).getPsc()+"");
                            tv_lac.setText(string.get(1).getLac()+"");
                        } else {
                            tv_lacTo.setVisibility(View.GONE);
                            tv_pscTo.setVisibility(View.GONE);
                            tv_lac.setVisibility(View.GONE);
                            tv_psc.setVisibility(View.GONE);
                            tv_view2.setVisibility(View.GONE);
                            tv_view.setVisibility(View.GONE);
                        }
                    }
                    recyclerView5G.setAdapter(new My5GAdapter(list, mainActivity));
                    my5GAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private int lac;
    private int psc;
    //recycler?????????
    private ArrayList<To5GBean> list = new ArrayList<>();
    private Runnable runnable;
    @SuppressLint("MissingPermission")
    private List<CellInfo> allCellInfo;
    private My5GAdapter my5GAdapter;
    private TextView tv_psc;
    private TextView tv_lac;
    private TextView tv_mobile;
    private TextView tv_operator;
    private TextView tv_mcc;
    private TextView tv_mnc;
    private TextView tv_mobile2;
    private TextView tv_cell_type;
    private TextView tv_cell_type2;
    private TextView tv_pci;
    private TextView tv_ci;
    private TextView tv_arfcn;
    private TextView tv_bands;
    private String mccString;
    private String mncString;
    private TextView tv_pscTo;
    private TextView tv_lacTo;
    private View tv_view;
    private View tv_view2;
    private ImageView iv_log;
    private TextView tv_name;
    private String name;
    private LinearLayout linear_4G;
    private LinearLayout linear_5G;
    private int lteTac;
    private int lteCi;
    private String lteMccString;
    private String lteMncString;
    private int lteRsrp;
    private int lteRsrq;
    private int lteRssi;
    private int lteRssnr;
    private int lteEarfac;
    private int ltePci;
    private String lteBand;
    private Runnable runnable4G;
    private Handler handler4G=new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==2){
                ArrayList<To4GBean> list= (ArrayList<To4GBean>) msg.obj;
                Log.i("?????????", "handleMessage: "+list.size());
                if(list.size()>1){
                    if(list.get(0).getOperator()!=null){
                        if("CUCC".equals(list.get(1).getOperator())){
                            iv_log.setImageResource(R.drawable.cucc);
                        }
                        if("CTCC".equals(list.get(1).getOperator())){
                            iv_log.setImageResource(R.drawable.ctcc);
                        }
                        if("CMCC".equals(list.get(1).getOperator())){
                            iv_log.setImageResource(R.drawable.cmcc);
                        }
                    }
                    tv_mobile.setText(list.get(1).getLte());
                    tvLTE_lte.setText(list.get(1).getLte());
                    tvLTE_lte1.setText(list.get(1).getLte());
                    tvLTE_tac.setText(list.get(1).getLteTac());
                    tvLTE_pci.setText(list.get(1).getLtePci());
                    tvLTE_eci.setText(list.get(1).getLteCi());
                    tvLTE_earfcn.setText(list.get(1).getLteEarfac());
                    tvLTE_band.setText(list.get(1).getLteBand()+"");
                    tv_cell_type.setText("LTE");
                    tv_cell_type2.setText(list.get(1).getLteType());
                    tv_name.setText(name);
                    tv_operator.setText(list.get(1).getOperator());
                    tv_mcc.setText(list.get(1).getLteMccString());
                    tv_mnc.setText(list.get(1).getLteMncString());
                    tv_rssl.setText(list.get(1).getLteRssi());
                    tv_rsrp.setText(list.get(1).getLteRsrp());
                    tv_rsrq.setText(list.get(1).getLteRsrq());
                    tv_ssSinr.setText("---");
                    tv_ssRSRQ.setText("---");
                    tv_ssRSRP.setText("---");
                }
                recyclerView4G.setAdapter(new My4GAdapter(array4GList, mainActivity));
                my4GAdapter.notifyDataSetChanged();
            }
            super.handleMessage(msg);
        }
    };
    private TextView tvLTE_band;
    private TextView tvLTE_earfcn;
    private TextView tvLTE_lte;
    private TextView tvLTE_eci;
    private TextView tvLTE_pci;
    private TextView tvLTE_tac;
    private TextView tvLTE_lte1;
    private My4GAdapter my4GAdapter;
    private RecyclerView recyclerView4G;
    private RecyclerView recyclerView5G;
    private LinearLayout my4GRecycler;
    private LinearLayout my5GRecycler;
    private ConstraintLayout cons;
    private String formatLon;
    private String formatLat;
    private ImageView iv_black;
    private Runnable run;
    private String d;
    private TextView tv_jz;
    private MySql mySql;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);?????????????????????
        setContentView(R.layout.drive_test_activity);
        mySql = new MySql(this);
//        setStatBar();
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        mainActivity=this;

        //????????????
        findView();


        //??????????????????
        setLocation();


        //????????????5G??????4G
        run = new Runnable() {
            @Override
            public void run() {
                //???5??????????????????4 5G??????
                Message message = new Message();
                message.what = 8;
                handlerUp.sendMessage(message);
                handlerUp.postDelayed(this, 1000);
            }
        };
        handlerUp.post(run);


        //???????????????
        iv_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handlerUp!=null){
                    handlerUp.removeCallbacks(run);
                }
                getClear();
                finish();
            }
        });

    }


    @SuppressLint("NewApi")
    public void setStatBar() {//????????????????????????????????????
        View decorView = getWindow().getDecorView();
        int option =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT
        );
    }

    private void setLocation() {
        //??????????????????
        Intent intent = getIntent();
        String longitude = intent.getStringExtra("longitude");
        String latitude = intent.getStringExtra("latitude");
        if(latitude!=null&&longitude!=null){
            Map<String, Double> map = GCJ02ToWGS84Util.bd09to84(Double.parseDouble(longitude), Double.parseDouble(latitude));
            for(String s:map.keySet()){
                //???????????????????????????
//            intentMsg = new Intent("name");
//            intentMsg.putExtra("name","key : "+s+" value : "+map.get(s));
                if(s.equals("lon")){
                    lon=map.get(s)+"";
                }else{
                    lat=map.get(s)+"";
                }
            }
            System.out.println(lon+"       "+lat);
            //?????????????????????????????????
            DecimalFormat df = new DecimalFormat("#.000000");
            //fo
            formatLon = df.format(Double.parseDouble(lon));
            //fo
            formatLat = df.format(Double.parseDouble(lat));
            tv_location.setText("??????????????? "+ formatLon +"/"+ formatLat);
        }
    }


    private void findView() {
        tv_location = findViewById(R.id.tv_location);
        iv_black = findViewById(R.id.iv_black);
        tv_jz = findViewById(R.id.tv_jz);//??????????????????


         */
/*
        5G
        * *//*

        linear_5G =  findViewById(R.id.Linear_5G);
        recyclerView4G =  findViewById(R.id.recycler4G);
        recyclerView5G =  findViewById(R.id.recycler5G);
        my4GRecycler = findViewById(R.id.my4GRecycler);
        my5GRecycler = findViewById(R.id.my5GRecycler);
        tv_rssl =  findViewById(R.id.tv_rssl);
        tv_rsrp =  findViewById(R.id.tv_rsrp);
        tv_rsrq =  findViewById(R.id.tv_rsrq);
        tv_ssRSRP =  findViewById(R.id.tv_ssRSRP);
        tv_ssRSRQ =  findViewById(R.id.tv_ssRSRQ);
        tv_ssSinr =  findViewById(R.id.tv_ssSinr);


        tv_mobile =  findViewById(R.id.tv_MOBILE);
        tv_operator =  findViewById(R.id.tv_operator);
        tv_mcc =  findViewById(R.id.tv_mcc);
        tv_mnc =  findViewById(R.id.tv_mnc);
        tv_mobile2 =  findViewById(R.id.tv_MOBILE2);
        tv_cell_type =  findViewById(R.id.tv_Cell_type);
        tv_cell_type2 =  findViewById(R.id.tv_Cell_type2);
        tv_pci =  findViewById(R.id.tv_pci);
        tv_ci =  findViewById(R.id.tv_ci);
        tv_arfcn =  findViewById(R.id.tv_arfcn);
        tv_bands =  findViewById(R.id.tv_bands);




        tv_psc =  findViewById(R.id.tv_psc);
        tv_lac =  findViewById(R.id.tv_lac);
        tv_pscTo =  findViewById(R.id.tv_pscTo);
        tv_lacTo =  findViewById(R.id.tv_lacTo);
        tv_view =  findViewById(R.id.tv_view);
        tv_view2 =  findViewById(R.id.tv_view2);


        iv_log =  findViewById(R.id.iv_log);
        tv_name = findViewById(R.id.tv_Name);
        */
/*fdsf
        5G
        * *//*



        */
/*4G*//*

        linear_4G =  findViewById(R.id.Linear_4G);
        tvLTE_band =  findViewById(R.id.tvLTE_band);
        tvLTE_earfcn =  findViewById(R.id.tvLTE_EARFCN);
        tvLTE_lte =  findViewById(R.id.tvLTE_lte);
        tvLTE_eci =  findViewById(R.id.tvLTE_eci);
        tvLTE_pci =  findViewById(R.id.tvLTE_pci);
        tvLTE_tac =  findViewById(R.id.tvLTE_tac);
        tvLTE_lte1 =  findViewById(R.id.tvLTE_LTE);

        */
/*4G*//*


        //???????????????????????????????????????
        tv_jz.setOnClickListener(v -> {
            ArrayList<DriveTest4GBean> beans = mySql.selectAdd();
            for (int i = 0; i <beans.size() ; i++) {
                MyToast.showToast("eci--"+beans.get(i).getLteCi()+" "+"tac--"+beans.get(i).getLteTac()+" "+"pci--"+beans.get(i).getLtePci());
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getData() {
        TelephonyManager tm;
        tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<CellInfo> allCellInfo = tm.getAllCellInfo();
        if (allCellInfo != null && allCellInfo.size() > 0) {
            for (int i = 0; i < allCellInfo.size(); i++) {
                CellInfo info = allCellInfo.get(i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //5G
                    if (info instanceof CellInfoNr) {
                        set5GDemo();
//                        Toast.makeText(this, "NR", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(info instanceof  CellInfoLte){
                        set4GDemo();
//                        Toast.makeText(this, "Lte", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void set4GDemo() {
        getIsSmi();
        getClear();
        linear_5G.setVisibility(View.GONE);
        linear_4G.setVisibility(View.VISIBLE);
        my5GRecycler.setVisibility(View.GONE);
        my4GRecycler.setVisibility(View.VISIBLE);
        */
/*5G????????????*//*

        if (list != null) {
            list.clear();
        }
        if(handler1!=null){
            handler1.removeCallbacks(runnable);
        }
        */
/*5G????????????*//*



        //??????4G??????
        get4GDemo();
        recyclerView4G.setLayoutManager(new LinearLayoutManager(this));
        my4GAdapter = new My4GAdapter(array4GList, this);
        recyclerView4G.setAdapter(my4GAdapter);
        my4GAdapter.notifyDataSetChanged();
        runnable4G = new Runnable() {
            @Override
            public void run() {
                //??????4G
                get4GDemo();
                Message message = new Message();
                message.what = 2;
                message.obj = array4GList;
                handler4G.sendMessage(message);
                handler4G.postDelayed(this, 5000);
            }
        };
        handler4G.post(runnable4G);
//        Toast.makeText(this, "???????????????4G", Toast.LENGTH_SHORT).show();
    }

    private void getIsSmi() {
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission")
        CellLocation cel = tel.getCellLocation();
        if(tel.getPhoneType()==TelephonyManager.PHONE_TYPE_CDMA){//????????????????????????
//            Toast.makeText(mainActivity, "??????", Toast.LENGTH_SHORT).show();
        }else{
            //??????????????????????????????  ??????????????????
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cel;
            lac = gsmCellLocation.getLac();
            psc = gsmCellLocation.getPsc();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void set5GDemo() {
        getIsSmi();
        getClear();
        linear_4G.setVisibility(View.GONE);
        linear_5G.setVisibility(View.VISIBLE);
        my5GRecycler.setVisibility(View.VISIBLE);
        my4GRecycler.setVisibility(View.GONE);
                    */
/*
                    4G???????????????
                    * *//*

        if(array4GList.size()>0){
            array4GList.clear();
        }
        if(runnable4G!=null){
            handler4G.removeCallbacks(runnable4G);
        }
                    */
/*
                    4G???????????????
                    * *//*

        get5GDemo();
        recyclerView5G.setLayoutManager(new LinearLayoutManager(this));
        my5GAdapter = new My5GAdapter(list, this);
        recyclerView5G.setAdapter(my5GAdapter);
        my5GAdapter.notifyDataSetChanged();
        runnable = new Runnable() {
            @Override
            public void run() {
                //??????5G
                get5GDemo();
                Message message = new Message();
                message.what = 1;
                message.obj = list;
                handler1.sendMessage(message);
                handler1.postDelayed(this, 5000);
            }
        };
        handler1.post(runnable);
//        Toast.makeText(this, "???????????????5G", Toast.LENGTH_SHORT).show();
    }

    private void getClear() {
        if(list.size()>0){
            list.clear();
        }
        if(handler4G!=null){
            handler4G.removeCallbacks(runnable4G);
        }
        if(array4GList!=null){
            array4GList.clear();
        }
        if(handler1!=null){
            handler1.removeCallbacks(runnable);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handlerUp!=null){
            handlerUp.removeCallbacks(run);
        }
        getClear();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void get4GDemo() {
        array4GList.clear();
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission")
        List<CellInfo> cellInfoList = manager.getAllCellInfo();

        add4GMi();//????????????????????????

        for (CellInfo info : cellInfoList) {
            Log.i("?????????", "get4GDemo: "+info.toString());

            if (info.toString().contains("CellInfoLte")) {
                @SuppressLint({"NewApi", "LocalSuppress"}) CellInfoLte cellInfoLte = (CellInfoLte) info;
                @SuppressLint({"NewApi", "LocalSuppress"}) CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();

                lteTac = cellIdentityLte.getTac();
                lteCi = cellIdentityLte.getCi();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    lteMccString = cellIdentityLte.getMccString();
                    Log.i("?????????", "get4GDemo: mccString  "+ lteMccString);
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    lteMncString = cellIdentityLte.getMncString();
                    Log.i("?????????", "get4GDemo: mncString  "+ lteMncString);
                }
                Log.i("?????????", "get4GDemo: tac  "+ lteTac);
                Log.i("?????????", "get4GDemo: ci  "+ lteCi);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lteRsrp = cellInfoLte.getCellSignalStrength().getRsrp();
                    Log.i("?????????", "get4GDemo: rsrp  "+ lteRsrp);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lteRsrq = cellInfoLte.getCellSignalStrength().getRsrq();
                    Log.i("?????????", "get4GDemo: rsrq "+ lteRsrq);
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    lteRssi = cellInfoLte.getCellSignalStrength().getRssi();
                    Log.i("?????????", "get4GDemo: rssi    "+ lteRssi);
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    lteRssnr = cellInfoLte.getCellSignalStrength().getRssnr();
                    Log.i("?????????", "get4GDemo: rssnr    "+ lteRssnr);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    lteEarfac = cellIdentityLte.getEarfcn();
                    Log.i("?????????", "get4GDemo: earfcn"+ lteEarfac);
                }
                ltePci = cellIdentityLte.getPci();

                Log.i("?????????", "get4GDemo: pci"+ ltePci);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    String mobileNetworkOperator = cellIdentityLte.getMobileNetworkOperator();
                    Log.i("?????????", "get4GDemo: mobileNetworkOperator"+mobileNetworkOperator);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    lteBand = getBand(cellIdentityLte.getEarfcn());
                    Log.i("?????????", "get4GDemo: band"+ lteBand);
                }

                //?????????????????????????????????
                String operator = manager.getSimOperator();
                String ope=null;
                if(operator!=null){
                    if(operator.equals("46000") || operator.equals("46002") || operator.equals("46004") || operator.equals("46007")){
//????????????
                        Log.i("?????????", "????????????: ");
                        ope="CMCC";
                        d="CMCC";
                        name="????????????";
                    }else if(operator.equals("46001") || operator.equals("46006") || operator.equals("46009")){
//????????????
                        Log.i("?????????", "????????????: ");
                        ope="CUCC";
                        d="CUCC";
                        name="????????????";
                    }else if(operator.equals("46003") || operator.equals("46005") || operator.equals("46011")){
//????????????
                        Log.i("?????????", "????????????: ");
                        ope="CTCC";
                        d="CTCC";
                        name="????????????";
                    }
                }
                //????????????????????????????????????????????????
                array4GList.add(new To4GBean(lteMccString,lteMncString,lteTac+"",ltePci+"",
                        lteCi+"",lteEarfac+"",lteBand+"",lteRssi+"",
                        lteRsrp+"", lteRsrq+"", lteRssnr+"",ope,"LTE","???????????? LTE"));
            }

        }
        */
/*??????4g??????????????????tac??? ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        * ?????????????????????????????? ??????????????????????????????????????????
        * *//*

    }
   private void add4GMi() {
        array4GList.add(new To4GBean("lteMccString","lteMncString",lteTac+"","PCI",
                "","EARFCN","BAND","RSSI",
                "RSRP", "RSRQ", "","","LTE","???????????? LTE"));
    }

    //9264
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void get5GDemo() {
        list.clear();
        band = 0;
        tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<CellInfo> allCellInfo = tm.getAllCellInfo();
        getData5G(allCellInfo);
    }

    private void getData5G(List<CellInfo> allCellInfo) {
        addMi();//????????????????????????
        if (allCellInfo != null && allCellInfo.size() > 0) {
            for (int i = 0; i < allCellInfo.size(); i++) {
                CellInfo info = allCellInfo.get(i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //5G
                    if (info instanceof CellInfoNr) {
                        Log.i("?????????", "5G: ");
                        //??????5G????????????
                        CellIdentityNr nr = (CellIdentityNr) ((CellInfoNr) info).getCellIdentity();
                        CellSignalStrengthNr nrStrength = (CellSignalStrengthNr) ((CellInfoNr) info)
                                .getCellSignalStrength();

                        mccString = nr.getMccString();
                        String mncString = nr.getMncString();
                        int pci = nr.getPci();
                        long ci = nr.getNci();
                        String cid = String.valueOf(ci);
                        int nrarfcn = nr.getNrarfcn();
                        int ssRsrp = nrStrength.getSsRsrp();
                        int ssRsrq = nrStrength.getSsRsrq();
                        int ssSinr = nrStrength.getSsSinr();

                        //?????????????????????
                        if (rangeInDefined(nrarfcn, 151600, 160600)) {
                            band = 28;
                        }
                        if (rangeInDefined(nrarfcn, 499200, 537999)) {
                            band = 41;
                        }
                        if (rangeInDefined(nrarfcn, 620000, 653333)) {
                            band = 78;
                        }
                        if (rangeInDefined(nrarfcn, 693333, 733333)) {
                            band = 79;
                        }


                        //5G??????????????????????????????????????????
                        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String operator = tel.getSimOperator();
                        if(operator!=null){
                            if(operator.equals("46000") || operator.equals("46002") || operator.equals("46004") || operator.equals("46007")){
                                Log.i("?????????", "????????????: ");
                                this.operator="CMCC";
                                name="????????????";
                            }else if(operator.equals("46001") || operator.equals("46006") || operator.equals("46009")){
                                Log.i("?????????", "????????????: ");
                                this.operator="CUCC";
                                name="????????????";
                            }else if(operator.equals("46003") || operator.equals("46005") || operator.equals("46011")){
                                Log.i("?????????", "????????????: ");
                                this.operator="CTCC";
                                name="????????????";
                                lac=0;
                                psc=0;
                            }
                        }


                        Log.i("?????????", "mccString: "+ mccString);
                        Log.i("?????????", "mncString: "+ mncString);
                        Log.i("?????????", "NR-PCI: "+pci);
                        Log.i("?????????", "NR-CI: "+ cid);
                        Log.i("?????????", "NR-ARFCN: "+nrarfcn);
                        Log.i("?????????", "NR-BAND: "+band);

                        Log.i("?????????", "ssRsrp: "+ssRsrp);
                        Log.i("?????????", "ssRsrq: "+ssRsrq);
                        Log.i("?????????", "ssSinr: "+ssSinr);
                        Log.i("?????????", "lac: "+lac);
                        Log.i("?????????", "psc: "+psc);
                        list.add(new To5GBean(band+"", nrarfcn+"", pci+"",
                                ssRsrp+"", ssRsrq+"", ssSinr+"",cid,lac,psc,"NR","???????????? NR",mncString));
                    }
                }
            }
        }
    }
    private void addMi() {
        list.add(new To5GBean("BAND", "NR ARFCN", "PCI", "RSRP", "RSRQ", "SINR",null,0,0,"NR","???????????? NR",
                "00"));
    }


    */
/*
      ???Math????????????0~10 5??????????????????
  * *//*

    public static boolean rangeInDefined(int current, int min, int max)
    {
        return Math.max(min, current) == Math.min(current, max);
    }
}*/
