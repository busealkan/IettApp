package com.h5190059.iett.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.h5190059.iett.R;
import com.h5190059.iett.model.AcilTelefonlarModel;
import com.h5190059.iett.model.AnadoluBolgesiDeparHatlariModel;

import java.util.ArrayList;

public class AcilTelefonlarAdapter extends BaseAdapter {
    ArrayList<AcilTelefonlarModel> acilTelefonlar;
    LayoutInflater layoutInflater;
    Context context;

    public AcilTelefonlarAdapter(Activity activity, ArrayList<AcilTelefonlarModel> acilTelefonlar){
        this.acilTelefonlar = acilTelefonlar;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return acilTelefonlar.size();
    }

    @Override
    public Object getItem(int position) {
        return acilTelefonlar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_acil_telefonlar,null);
        Button btnAcilTelefonlarBaslik = view.findViewById(R.id.btnAcilTelefonlarBaslik);
        Button btnAcilTelefonlarNumara = view.findViewById(R.id.btnAcilTelefonlarNumara);

        btnAcilTelefonlarBaslik.setText(acilTelefonlar.get(position).getAcilTelefonBaslik());
        btnAcilTelefonlarNumara.setText(acilTelefonlar.get(position).getAcilTelefonNumara());

        return view;
    }


}
