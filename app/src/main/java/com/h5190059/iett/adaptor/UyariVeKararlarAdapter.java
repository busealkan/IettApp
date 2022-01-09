package com.h5190059.iett.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.h5190059.iett.R;
import com.h5190059.iett.model.KazaModel;
import com.h5190059.iett.model.UyariVeKararlarModel;

import java.util.ArrayList;

public class UyariVeKararlarAdapter extends BaseAdapter {
    ArrayList<UyariVeKararlarModel> uyariVeKararlar;
    LayoutInflater layoutInflater;
    Context context;

    public UyariVeKararlarAdapter(Activity activity, ArrayList<UyariVeKararlarModel> uyariVeKararlar){
        this.uyariVeKararlar = uyariVeKararlar;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return uyariVeKararlar.size();
    }

    @Override
    public Object getItem(int position) {
        return uyariVeKararlar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_uyari_ve_kararlar,null);
        TextView txtUyariVeKararlarTarih = view.findViewById(R.id.txtUyariVeKararlarTarih);
        TextView txtUyariVeKararlarAciklama = view.findViewById(R.id.txtUyariVeKararlarAciklama);
        TextView txtUyariVeKararlarDisiplin = view.findViewById(R.id.txtUyariVeKararlarDisiplin);


        txtUyariVeKararlarTarih.setText(uyariVeKararlar.get(position).getUyariVeKararTarihi());
        txtUyariVeKararlarAciklama.setText(uyariVeKararlar.get(position).getUyariVeKararAciklama());
        txtUyariVeKararlarDisiplin.setText(uyariVeKararlar.get(position).getUyariVeKararDisiplin());

        return view;
    }
}
