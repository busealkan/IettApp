package com.h5190059.iett.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.h5190059.iett.R;
import com.h5190059.iett.activities.RaporDetayActivity;
import com.h5190059.iett.activities.RaporDuzenleActivity;
import com.h5190059.iett.model.RaporModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class RaporAdapter extends BaseAdapter {
    ArrayList<RaporModel> raporlar;
    LayoutInflater layoutInflater;
    Context context;

    public RaporAdapter(Activity activity, ArrayList<RaporModel> raporlar){
        this.raporlar = raporlar;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return raporlar.size();
    }

    @Override
    public Object getItem(int position) {
        return raporlar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_raporlar,null);
        TextView txtRaporSicil = view.findViewById(R.id.txtRaporSicil);
        TextView txtRaporBaslik = view.findViewById(R.id.txtRaporBaslik);
        TextView txtRaporDetayaGit = view.findViewById(R.id.txtRaporDetayaGit);
        TextView txtRaporSayi = view.findViewById(R.id.txtRaporSayi);
        TextView txtRaporChildUserId = view.findViewById(R.id.txtRaporChildUserId);
        TextView txtRaporDuzenleme = view.findViewById(R.id.txtRaporDuzenleme);

        txtRaporSicil.setText(raporlar.get(position).getTutanakYazilanSicil());
        txtRaporBaslik.setText(raporlar.get(position).getTutanakBaslik());
        txtRaporDetayaGit.setText(R.string.detayaGit);
        txtRaporDuzenleme.setText(R.string.raporDuzenleButon);
        txtRaporSayi.setText(raporlar.get(position).getTutanakAdet());
        txtRaporChildUserId.setText(raporlar.get(position).getTutanakYazilanUserId());

        txtRaporDetayaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent raporDetay = new Intent(context, RaporDetayActivity.class);
                raporDetay.putExtra(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SICIL,txtRaporSicil.getText().toString());
                raporDetay.putExtra(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SAYI,txtRaporSayi.getText().toString());
                raporDetay.putExtra(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_USER_ID,txtRaporChildUserId.getText().toString());
                context.startActivity(raporDetay);

            }
        });

        txtRaporDuzenleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent raporDuzenle = new Intent(context, RaporDuzenleActivity.class);
                raporDuzenle.putExtra(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SICIL,txtRaporSicil.getText().toString());
                raporDuzenle.putExtra(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_SAYI,txtRaporSayi.getText().toString());
                raporDuzenle.putExtra(Constants.FIREBASE_TUTANAKLAR_TIKLANAN_TUTANAK_USER_ID,txtRaporChildUserId.getText().toString());
                context.startActivity(raporDuzenle);

            }
        });
        return view;
    }
}
