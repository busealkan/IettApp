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

import java.util.ArrayList;

public class KazaAdapter extends BaseAdapter {
    ArrayList<KazaModel> kazalar;
    LayoutInflater layoutInflater;
    Context context;

    public KazaAdapter(Activity activity, ArrayList<KazaModel> kazalar){
        this.kazalar = kazalar;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return kazalar.size();
    }

    @Override
    public Object getItem(int position) {
        return kazalar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_kazalar,null);
        TextView txtKazalarKazaTarihi = view.findViewById(R.id.txtKazalarKazaTarihi);
        TextView txtKazalarKazaTuru = view.findViewById(R.id.txtKazalarKazaTuru);
        TextView txtKazalarHasar = view.findViewById(R.id.txtKazalarHasar);


        txtKazalarKazaTarihi.setText(kazalar.get(position).getKazaTarihi());
        txtKazalarKazaTuru.setText(kazalar.get(position).getKazaTuru());
        txtKazalarHasar.setText(kazalar.get(position).getKazaHasar());

        return view;
    }
}
