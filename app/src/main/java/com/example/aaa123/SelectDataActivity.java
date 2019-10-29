package com.example.aaa123;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class SelectDataActivity extends AppCompatActivity {
    ListView list;
    myDBHelper dbHelper;
    SQLiteDatabase db;
    String sql;
    Cursor cursor;
    final static String dbName = "productDB.db";
    final static int dbVersion = 2;
    int selectType, selectType2;
    int selectMois;
    double expectedMois, expectedOil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_data);
        list = (ListView)findViewById(R.id.list);

        dbHelper = new myDBHelper(this, dbName, null, dbVersion);
        Intent i = getIntent();
        TextView showSkintype = (TextView) findViewById(R.id.showSkintype);
        TextView typeA = (TextView)findViewById(R.id.typeA);
        selectType2= i.getExtras().getInt("selectType");//건성 중성 지성
        selectMois = i.getExtras().getInt("moisp");//수분포인트
        selectType = i.getExtras().getInt("typeA");//여드름 민감성
        expectedMois = 17.5+(2.5*(double)selectMois);
        switch(selectType2) {
            case 1:
                showSkintype.setText("건성");
                expectedOil= expectedMois*0.7;
                break;
            case 2:
                showSkintype.setText("중성");
                expectedOil= expectedMois*0.8;
                break;
            case 4:
                showSkintype.setText("복합성");
                expectedOil= expectedMois*0.9;
                break;
            default:
                showSkintype.setText("지성");
                expectedOil= expectedMois*1.1;
                break;
        }
        //여드름 5, 아토피 6, 민감성 7, 해당없음 0
        switch (selectType){
            case 5:
                typeA.setText(", 여드름성입니다.");
                expectedOil*=1.1;
                break;
            case 6:
                typeA.setText(", 아토피성입니다.");
                expectedOil*=0.9;
                break;
            case 7:
                typeA.setText(", 민감성입니다.");
                break;
            default:
                typeA.setText("입니다.");
                break;
        }

        expectedMois= Double.parseDouble(String.format("%.2f",expectedMois));
        expectedOil= Double.parseDouble(String.format("%.2f",expectedOil));
         PieChart moisChart = (PieChart) findViewById(R.id.moisGraph);
        PieChart oilChart = (PieChart) findViewById(R.id.oilGraph) ;
        moisChart.setUsePercentValues(true);
        moisChart.getDescription().setEnabled(false);
        moisChart.setExtraOffsets(5,10,5,5);
        moisChart.setDrawCenterText(true);
        moisChart.setCenterText("수분\n"+Double.toString(expectedMois)+"%");
        moisChart.setDrawHoleEnabled(true);
        moisChart.setHoleRadius(80);
        moisChart.setHoleColor(0x00ff0000);
        moisChart.setTransparentCircleRadius(61f);
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> chartColor = new ArrayList<>();
        chartColor.add(0xff3e5bff);
        chartColor.add(0x00ff0000);
        entries.add(new PieEntry((float)expectedMois,""));
        entries.add(new PieEntry((float)(100-expectedMois),""));
        PieDataSet moisData =new PieDataSet(entries,"수분");
        moisData.setColors(chartColor);
        Legend l = moisChart.getLegend();
        l.setEnabled(false);
        PieData mdata = new PieData(moisData);
        mdata.setValueTextSize(10f);
        mdata.setDrawValues(false);
        moisChart.setData(mdata);

        oilChart.setUsePercentValues(true);
        oilChart.getDescription().setEnabled(false);
        oilChart.setExtraOffsets(5,10,5,5);
        oilChart.setDrawCenterText(true);
        oilChart.setCenterText("유분\n"+Double.toString(expectedOil)+"%");
        oilChart.setDrawHoleEnabled(true);
        oilChart.setHoleRadius(80);
        oilChart.setHoleColor(0x00ff0000);
        oilChart.setTransparentCircleRadius(61f);
        List<PieEntry> oilentries = new ArrayList<>();
        List<Integer> oilchartColor = new ArrayList<>();
        oilchartColor.add(0xffff3e82);
        oilchartColor.add(0x00ff0000);
        oilentries.add(new PieEntry((float)expectedOil,""));
        oilentries.add(new PieEntry((float)(100-expectedOil),""));
        PieDataSet oilData =new PieDataSet(oilentries,"유분");
        oilData.setColors(oilchartColor);
        Legend le = oilChart.getLegend();
        le.setEnabled(false);
        PieData odata = new PieData(oilData);
        odata.setValueTextSize(10f);
        odata.setDrawValues(false);
        oilChart.setData(odata);


        selectDB();


    }

    private void selectDB(){
        db = dbHelper.getWritableDatabase();
        sql = "SELECT *, ABS( mois -"+expectedMois+") AS moisDistance FROM " +
                "(SELECT *, ABS( oil - "+expectedOil+" ) AS oilDistance FROM product where oilDistance<16 " +
                "ORDER BY oilDistance) where moisDistance<30 AND skinType="+ selectType+" AND skinType2="+selectType2+" ORDER BY moisDistance limit 5;";
        cursor = db.rawQuery(sql, null);
        if(cursor.getCount() > 0){
            startManagingCursor(cursor);
            DBAdapter dbAdapter = new DBAdapter(this, cursor);
            list.setAdapter(dbAdapter);
        }
    }



}
