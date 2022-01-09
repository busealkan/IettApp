
package com.h5190059.iett.adaptor;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;

        import com.h5190059.iett.R;
        import com.h5190059.iett.activities.KutuphaneBilgilerAcilTelefonlarActivity;
        import com.h5190059.iett.activities.KutuphaneBilgilerAnadoluBolgesiDeparHatlariActivity;
        import com.h5190059.iett.activities.KutuphaneBilgilerAvrupaBolgesiDeparHatlariActivity;
        import com.h5190059.iett.model.AnadoluBolgesiDeparHatlariModel;
        import com.h5190059.iett.utils.Constants;

        import java.util.ArrayList;

public class AvrupaBolgesiDeparHatlariAdapter extends BaseAdapter {
    ArrayList<AnadoluBolgesiDeparHatlariModel> anadoluBolgesiDeparHatlari;
    LayoutInflater layoutInflater;
    Context context;

    public AvrupaBolgesiDeparHatlariAdapter(Activity activity, ArrayList<AnadoluBolgesiDeparHatlariModel> anadoluBolgesiDeparHatlari){
        this.anadoluBolgesiDeparHatlari = anadoluBolgesiDeparHatlari;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return anadoluBolgesiDeparHatlari.size();
    }

    @Override
    public Object getItem(int position) {
        return anadoluBolgesiDeparHatlari.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_anadolu_bolgesi_depar_hatlari,null);
        Button btnAnadoluBolgesiHatKod = view.findViewById(R.id.btnAnadoluBolgesiHatKod);
        Button btnAnadoluBolgesiHatKodBolgeleri = view.findViewById(R.id.btnAnadoluBolgesiHatKodBolgeleri);

        btnAnadoluBolgesiHatKod.setText(anadoluBolgesiDeparHatlari.get(position).getDeparHatKod());
        btnAnadoluBolgesiHatKodBolgeleri.setText(anadoluBolgesiDeparHatlari.get(position).getDeparHatKodBolgeleri());

        return view;
    }


}
