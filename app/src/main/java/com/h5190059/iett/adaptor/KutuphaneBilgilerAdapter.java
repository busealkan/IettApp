package com.h5190059.iett.adaptor;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.h5190059.iett.R;
import com.h5190059.iett.activities.KutuphaneBilgilerAcilTelefonlarActivity;
import com.h5190059.iett.activities.KutuphaneBilgilerAnadoluBolgesiDeparHatlariActivity;
import com.h5190059.iett.activities.KutuphaneBilgilerAvrupaBolgesiDeparHatlariActivity;
import com.h5190059.iett.activities.RaporDetayActivity;
import com.h5190059.iett.model.KutuphaneBilgilerModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;


public class KutuphaneBilgilerAdapter extends BaseAdapter {
    ArrayList<KutuphaneBilgilerModel> bilgiler;
    LayoutInflater layoutInflater;
    Context context;

    public KutuphaneBilgilerAdapter(Activity activity, ArrayList<KutuphaneBilgilerModel> bilgiler){
        this.bilgiler = bilgiler;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bilgiler.size();
    }

    @Override
    public Object getItem(int position) {
        return bilgiler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_bilgiler,null);
        TextView txtBilgiBaslik = view.findViewById(R.id.txtBilgiBaslik);

        txtBilgiBaslik.setText(bilgiler.get(position).getBilgilerBaslik());

        txtBilgiBaslik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tiklananBilgiBaslik = txtBilgiBaslik.getText().toString();
                if(tiklananBilgiBaslik.equals(Constants.FIREBASE_KUTUPHANE_BILGILER_ACIL_TELEFONLAR)){
                    Intent acilTelefonlar = new Intent(context, KutuphaneBilgilerAcilTelefonlarActivity.class);
                    context.startActivity(acilTelefonlar);
                }
                else if(tiklananBilgiBaslik.equals(Constants.FIREBASE_KUTUPHANE_BILGILER_ANADOLU_BOLGESI_DEPAR_HATLARI_BASLIK)){
                    Intent anadoluBolgesiDeparHatlari = new Intent(context, KutuphaneBilgilerAnadoluBolgesiDeparHatlariActivity.class);
                    context.startActivity(anadoluBolgesiDeparHatlari);
                }
                else{
                    Intent avrupaBolgesiDeparHatlari = new Intent(context, KutuphaneBilgilerAvrupaBolgesiDeparHatlariActivity.class);
                    context.startActivity(avrupaBolgesiDeparHatlari);
                }

            }
        });
        return view;
    }


}
