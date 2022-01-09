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
import com.h5190059.iett.activities.ArizaDetayActivity;
import com.h5190059.iett.activities.KutuphaneDosyalarActivity;
import com.h5190059.iett.activities.RaporDetayActivity;
import com.h5190059.iett.model.ArizaModel;
import com.h5190059.iett.model.KutuphaneDosyalarModel;
import com.h5190059.iett.utils.Constants;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class KutuphaneDosyaAdapter extends BaseAdapter {
    ArrayList<KutuphaneDosyalarModel> dosyalar;
    LayoutInflater layoutInflater;
    Context context;

    public KutuphaneDosyaAdapter(Activity activity, ArrayList<KutuphaneDosyalarModel> dosyalar){
        this.dosyalar = dosyalar;
        this.context = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dosyalar.size();
    }

    @Override
    public Object getItem(int position) {
        return dosyalar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.cardview_dosyalar,null);
        TextView txtDosyaBaslik = view.findViewById(R.id.txtDosyaBaslik);
        TextView txtDosyaAdet = view.findViewById(R.id.txtDosyaAdet);

        txtDosyaBaslik.setText(dosyalar.get(position).getDosyaBaslik());

        txtDosyaBaslik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = dosyalar.get(position).getDosyaUrl();
                String dosyaAdi = dosyalar.get(position).getDosyaAdi();
                String dosyaBaslik = dosyalar.get(position).getDosyaBaslik();

                dosyayiIndir(context,dosyaAdi,".pdf",DIRECTORY_DOWNLOADS,url,dosyaBaslik);
            }
        });
        return view;
    }

    private void dosyayiIndir(Context context, String fileName, String fileExtension, String destinationDirectory, String url, String dosyaBaslik) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName+fileExtension);
        downloadManager.enqueue(request);
        Toast.makeText(context, dosyaBaslik+" Dosyası İndirildi", Toast.LENGTH_SHORT).show();
    }
}
