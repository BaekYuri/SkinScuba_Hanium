package com.example.aaa123;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DBAdapter extends CursorAdapter {


    public DBAdapter(Context context, Cursor c) {
        super(context, c);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView rankCount = (TextView)view.findViewById(R.id.rankcount);
        final TextView productCompany = (TextView)view.findViewById(R.id.productCompany);
        final TextView productName = (TextView)view.findViewById(R.id.productName);
        final TextView productSkinMois = (TextView)view.findViewById(R.id.productSkinMois);
        final TextView productSkinOil = (TextView)view.findViewById(R.id.productSkinOil);
        rankCount.setText(Integer.toString(cursor.getPosition()+1));
        productCompany.setText(cursor.getString(cursor.getColumnIndex("brandName")));
        productName.setText(cursor.getString(cursor.getColumnIndex("goodsName")));
        productSkinMois.setText("\t수분: " +cursor.getString(cursor.getColumnIndex("mois"))+"%");
        productSkinOil.setText("\t유분: "+cursor.getString(cursor.getColumnIndex("oil"))+"%");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.itemlayout, parent, false);
        return v;
    }

}
