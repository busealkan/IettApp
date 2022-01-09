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
import com.h5190059.iett.model.ArizaModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

public class ArizaAdapter extends BaseAdapter {
    ArrayList<ArizaModel> arizalar;
    LayoutInflater layoutInflater;
    Context context;

    public ArizaAdapter(Activity activity, ArrayList<ArizaModel> arizalar){
        this.arizalar = arizalar;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arizalar.size();
    }

    @Override
    public Object getItem(int position) {
        return arizalar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_arizalar,null);
        TextView txtArizaAracHatKod = view.findViewById(R.id.txtArizaAracHatKod);
        TextView txtArizaBaslik = view.findViewById(R.id.txtArizaBaslik);
        TextView txtArizaDetayaGit = view.findViewById(R.id.txtArizaDetayaGit);
        TextView txtArizaChildSayi = view.findViewById(R.id.txtArizaChildSayi);
        TextView txtArizaChildUserId = view.findViewById(R.id.txtArizaChildUserId);

        txtArizaAracHatKod.setText(arizalar.get(position).getHatKodu());
        txtArizaBaslik.setText(arizalar.get(position).getArizaBaslik());
        txtArizaDetayaGit.setText(R.string.detayaGit);
        txtArizaChildSayi.setText(arizalar.get(position).getArizaAdet());
        txtArizaChildUserId.setText(arizalar.get(position).getUserId());

        txtArizaDetayaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent arizaDetay = new Intent(context, ArizaDetayActivity.class);
                arizaDetay.putExtra(Constants.FIREBASE_ARIZALAR_TIKLANAN_ARIZA_SAYI,txtArizaChildSayi.getText().toString());
                arizaDetay.putExtra(Constants.FIREBASE_ARIZALAR_TIKLANAN_ARIZA_USER_ID,txtArizaChildUserId.getText().toString());
                context.startActivity(arizaDetay);

            }
        });
        return view;
    }
}
