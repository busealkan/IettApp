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
import com.h5190059.iett.activities.OneriDetayActivity;
import com.h5190059.iett.activities.TalepDetayActivity;
import com.h5190059.iett.model.OneriModel;
import com.h5190059.iett.model.TalepModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class OneriAdapter extends BaseAdapter {
    ArrayList<OneriModel> oneriler;
    LayoutInflater layoutInflater;
    Context context;

    public OneriAdapter(Activity activity, ArrayList<OneriModel> oneriler){
        this.oneriler = oneriler;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return oneriler.size();
    }

    @Override
    public Object getItem(int position) {
        return oneriler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_oneriler,null);
        TextView txtOneriSicil = view.findViewById(R.id.txtOneriSicil);
        TextView txtOneriBaslik = view.findViewById(R.id.txtOneriBaslik);
        TextView txtOneriDetayaGit = view.findViewById(R.id.txtOneriDetayaGit);
        TextView txtOneriChildSayi = view.findViewById(R.id.txtOneriChildSayi);
        TextView txtOneriChildUserId = view.findViewById(R.id.txtOneriChildUserId);

        txtOneriSicil.setText(oneriler.get(position).getSicil());
        txtOneriBaslik.setText(oneriler.get(position).getOneriBaslik());
        txtOneriDetayaGit.setText(R.string.detayaGit);
        txtOneriChildSayi.setText(oneriler.get(position).getOneriAdet());
        txtOneriChildUserId.setText(oneriler.get(position).getUserId());

        txtOneriDetayaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oneriDetay = new Intent(context, OneriDetayActivity.class);
                oneriDetay.putExtra(Constants.FIREBASE_ONERILER_TIKLANAN_ONERI_SICIL,txtOneriSicil.getText().toString());
                oneriDetay.putExtra(Constants.FIREBASE_ONERILER_TIKLANAN_ONERI_SAYI,txtOneriChildSayi.getText().toString());
                oneriDetay.putExtra(Constants.FIREBASE_ONERILER_TIKLANAN_ONERI_USER_ID,txtOneriChildUserId.getText().toString());
                context.startActivity(oneriDetay);

            }
        });
        return view;
    }
}
