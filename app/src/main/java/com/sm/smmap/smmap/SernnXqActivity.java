package com.sm.smmap.smmap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.sm.smmap.smmap.Adapter.SerrnTaAdapter;
import com.sm.smmap.smmap.OrmSqlLite.Bean.GuijiViewBeanjizhan;
import com.sm.smmap.smmap.OrmSqlLite.DBManagerJZ;
import com.sm.smmap.smmap.Utils.It.IT.CallBack;
import com.sm.smmap.smmap.Utils.MyUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.baidu.mapapi.utils.CoordinateConverter.CoordType.BD09LL;
import static com.sm.smmap.smmap.MainActivity.mylag;

public class SernnXqActivity extends FragmentActivity implements View.OnClickListener {
    SerrnTaAdapter serrnAdapter;
    String lon = "";
    String lat = "";
    String mnc = "";
    String address = "";
    String tac = "";
    String resources = "";
    String eci = "";
    TextView tv_title, tv_fugai, tv_mnc, tv_lac, tv_cid, tv_address, tv_lat_lon, tv_resources;
    ImageButton bt_openMap;
    Button bt_quanjing;
    Button bt_m_dele;
    ImageView finsh;
    private List<Double> list = new ArrayList<>();
    CallBack callBack = new CallBack() {
        @Override
        public void call(String data, int i) {

        }
    };
    DBManagerJZ dbManagerJZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sernn_xq);
        setStatBar();
        findViewById(R.id.finsh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        lon = intent.getStringExtra("lon");
        lat = intent.getStringExtra("lat");
        mnc = intent.getStringExtra("mnc");
        address = intent.getStringExtra("address");
        tac = intent.getStringExtra("tac");
        eci = intent.getStringExtra("eci");
        resources = intent.getStringExtra("resources");
        tv_title = findViewById(R.id.tv_title);
        tv_fugai = findViewById(R.id.tv_fugai);
        tv_mnc = findViewById(R.id.tv_mnc);
        tv_lac = findViewById(R.id.tv_lac);
        tv_cid = findViewById(R.id.tv_cid);
        tv_address = findViewById(R.id.tv_address);
        tv_lat_lon = findViewById(R.id.tv_lat_lon);
        tv_resources=findViewById(R.id.tv_resources);
        bt_openMap = findViewById(R.id.bt_openMap);
        bt_openMap.setOnClickListener(this);
        bt_quanjing = findViewById(R.id.bt_quanjing);
        bt_quanjing.setOnClickListener(this);
        bt_m_dele = findViewById(R.id.bt_m_dele);
        bt_m_dele.setOnClickListener(this);
        finsh = findViewById(R.id.finsh);
        String str = "";
        if (mnc.equals("0")) {
            str = "??????";
        }
        if (mnc.equals("1")) {
            str = "??????";
        }
        if (mnc.equals("11")) {
            str = "??????";
        }
        tv_title.setText(str);
        tv_fugai.setText("");
        tv_mnc.setText(mnc);
        tv_lac.setText(tac);
        tv_cid.setText(eci);
        tv_address.setText(address);
        tv_lat_lon.setText("??????:" + lat + "," + "??????:" + lon);
        tv_resources.setText(resources);

        try {
            dbManagerJZ = new DBManagerJZ(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_openMap:
                showdiaolog();
                break;

            case R.id.bt_quanjing:
                final Intent intent = new Intent(SernnXqActivity.this, PanoramaDemoActivityMain.class);
                Bundle bundle = new Bundle();
                bundle.putString("lat", lat);
                bundle.putString("lon", lon);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.bt_m_dele:
                final Dialog dialog2 = new Dialog(SernnXqActivity.this, R.style.ActionSheetDialogStyle);
                //????????????????????????
                View inflate = LayoutInflater.from(SernnXqActivity.this).inflate(R.layout.item_dibushow2, null);
                //???????????????
                final EditText editText = inflate.findViewById(R.id.et_ta);
                TextView tv_titls = inflate.findViewById(R.id.tv_titls);
                tv_titls.setText("??????TA???");
                ImageView iv_finish = inflate.findViewById(R.id.iv_finish);
                iv_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });
                final RecyclerView recyclerView = inflate.findViewById(R.id.recylerview);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                serrnAdapter = new SerrnTaAdapter(list, callBack);
                recyclerView.setAdapter(serrnAdapter);
                Button btadd = inflate.findViewById(R.id.btadd);
                btadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            Toast.makeText(SernnXqActivity.this, "??????????????????", Toast.LENGTH_LONG).show();
                            return;
                        }
                        list.add(Double.parseDouble(editText.getText().toString()));
                        editText.setText("");
                        serrnAdapter.notifyDataSetChanged();

//                aaa
                    }
                });

                Button bt_adddilao = inflate.findViewById(R.id.bt_adddilao);
                bt_adddilao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (list.size() > 0) {
                            String s = MyUtils.listToString(list);
                            GuijiViewBeanjizhan d = new GuijiViewBeanjizhan();
                            d.setId(1);
                            d.setAddress(address);
                            d.setCi("");
                            d.setLac(tac);
                            d.setMcc("");
                            d.setMnc(mnc);
                            d.setRadius("0");
                            d.setTa(s);
                            d.setType(0);
                            d.setLat(lat);
                            d.setLon(lon);
                            d.setCi(eci);
                            d.setResources(resources);
                            try {
                                int i = dbManagerJZ.insertStudent(d);
                                Log.d(TAG, "insertonClick: " + i);
                                if (i == 1) {
                                    dialog2.dismiss();
                                    Toast.makeText(AppLication.getInstance().getApplicationContext(), "????????????", Toast.LENGTH_LONG).show();
//                                    Intent intent1 = new Intent(SernnXqActivity.this);
//                                    intent1.putExtra("id", lat);
//                                    intent1.putExtra("lon", lon);
//                                    startActivityForResult(intent1, 14);
                                    finish();
                                }

                            } catch (SQLException e) {
                                e.printStackTrace();
                                Log.d(TAG, "insertonClickerro: " + e.getMessage());
                            }
                        } else {
                            Toast.makeText(AppLication.getInstance().getApplicationContext(), "??????????????????TA???", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //??????????????????Dialog
                dialog2.setContentView(inflate);
                //????????????Activity???????????????
                Window dialogWindow = dialog2.getWindow();
                //??????Dialog?????????????????????
                dialogWindow.setGravity(Gravity.CENTER);
                //?????????????????????
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.y = 20;//??????Dialog?????????????????????
//       ????????????????????????
                dialogWindow.setAttributes(lp);
                dialog2.show();//???????????????
                break;
            case R.id.finsh:
                finish();
                break;
        }

    }


    private void showdiaolog() {
        //???????????????

        Dialog dialog2 = new Dialog(SernnXqActivity.this, R.style.ActionSheetDialogStyle);
        //????????????????????????
        View inflate = LayoutInflater.from(SernnXqActivity.this).inflate(R.layout.item_dibushowdaohao, null);
        //???????????????
        TextView baidu = (TextView) inflate.findViewById(R.id.baidu);
//        choosePhoto.setVisibility(View.GONE);
        TextView gaode = (TextView) inflate.findViewById(R.id.gaode);
//                            baidu.setOnClickListener(new MyonclickXian(mylag));
        baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LatLng startLatLng = new LatLng(39.940387, 116.29446);
                    LatLng endLatLng = new LatLng(39.87397, 116.529025);
                    String uri = String.format("baidumap://map/direction?origin=%s,%s&destination=" +
                                    "%s,%s&mode=driving&src=com.34xian.demo", mylag.latitude, mylag.longitude,
                            lat, lon);
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
//                    ToastUtil.showShort(this, "?????????????????????");
                    Toast.makeText(SernnXqActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    double gdLatitude = 39.92848272
//                    double gdLongitude = 116.39560823
                    //?????????????????????????????????????????????????????????????????????
                    //?????????????????????????????????????????????????????????????????????
                    LatLng latLngs = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                    CoordinateConverter converter = new CoordinateConverter()
                            .from(BD09LL)
                            .coord(latLngs);

//????????????
                    LatLng desLatLng = converter.convert();

                    String uri = String.format("amapuri://route/plan/?dlat=%s&dlon=%s&dname=??????&dev=0&t=0",
                            desLatLng.latitude, desLatLng.longitude);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(uri));
                    intent.setPackage("com.autonavi.minimap");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SernnXqActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();

                }
            }
        });
        //??????????????????Dialog
        dialog2.setContentView(inflate);
        //????????????Activity???????????????
        Window dialogWindow = dialog2.getWindow();
        //??????Dialog?????????????????????
        dialogWindow.setGravity(Gravity.BOTTOM);
        //?????????????????????
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//??????Dialog?????????????????????
//       ????????????????????????
        dialogWindow.setAttributes(lp);
        dialog2.show();//???????????????
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
}
