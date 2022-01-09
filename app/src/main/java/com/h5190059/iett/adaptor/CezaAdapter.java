package com.h5190059.iett.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.h5190059.iett.R;
import com.h5190059.iett.model.CezaModel;

import java.util.ArrayList;

public class CezaAdapter extends BaseAdapter {
    ArrayList<CezaModel> cezalar;
    LayoutInflater layoutInflater;
    Context context;

    public CezaAdapter(Activity activity, ArrayList<CezaModel> cezalar){
        this.cezalar = cezalar;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cezalar.size();
    }

    @Override
    public Object getItem(int position) {
        return cezalar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_cezalar,null);
        TextView txtCezalarCezaYeri = view.findViewById(R.id.txtCezalarCezaYeri);
        TextView txtCezalarCezaTuru = view.findViewById(R.id.txtCezalarCeza);
        TextView txtCezalarCezaTarihi = view.findViewById(R.id.txtCezalarCezaTarihi);


        txtCezalarCezaYeri.setText(cezalar.get(position).getCezaYeri());
        txtCezalarCezaTuru.setText(cezalar.get(position).getCeza());
        txtCezalarCezaTarihi.setText(cezalar.get(position).getCezaTarihi());

        return view;
    }
}
