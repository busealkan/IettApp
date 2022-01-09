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
import com.h5190059.iett.activities.ArizaDetayActivity;
import com.h5190059.iett.activities.RaporDetayActivity;
import com.h5190059.iett.activities.TalepDetayActivity;
import com.h5190059.iett.model.ArizaModel;
import com.h5190059.iett.model.TalepModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class TalepAdapter extends BaseAdapter {
    ArrayList<TalepModel> talepler;
    LayoutInflater layoutInflater;
    Context context;

    public TalepAdapter(Activity activity, ArrayList<TalepModel> talepler){
        this.talepler = talepler;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return talepler.size();
    }

    @Override
    public Object getItem(int position) {
        return talepler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_talepler,null);
        TextView txtTalepSicil = view.findViewById(R.id.txtTalepSicil);
        TextView txtTalepBaslik = view.findViewById(R.id.txtTalepBaslik);
        TextView txtTalepDetayaGit = view.findViewById(R.id.txtTalepDetayaGit);
        TextView txtTalepSayi = view.findViewById(R.id.txtTalepChildSayi);
        TextView txtTalepChildUserId = view.findViewById(R.id.txtTalepChildUserId);

        txtTalepSicil.setText(talepler.get(position).getSicil());
        txtTalepBaslik.setText(talepler.get(position).getTalepBaslik());
        txtTalepDetayaGit.setText(R.string.detayaGit);
        txtTalepSayi.setText(talepler.get(position).getTalepAdet());
        txtTalepChildUserId.setText(talepler.get(position).getUserId());

        txtTalepDetayaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent talepDetay = new Intent(context, TalepDetayActivity.class);
                talepDetay.putExtra(Constants.FIREBASE_TALEPLER_TIKLANAN_TALEP_SICIL,txtTalepSicil.getText().toString());
                talepDetay.putExtra(Constants.FIREBASE_TALEPLER_TIKLANAN_TALEP_SAYI,txtTalepSayi.getText().toString());
                talepDetay.putExtra(Constants.FIREBASE_TALEPLER_TIKLANAN_TALEP_USER_ID,txtTalepChildUserId.getText().toString());
                context.startActivity(talepDetay);

            }
        });
        return view;
    }
}
