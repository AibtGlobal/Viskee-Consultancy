package com.viskee.brochure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.viskee.brochure.R;
import com.viskee.brochure.model.Brochure;
import com.viskee.brochure.util.PDFFileDownloader;
import com.viskee.brochure.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

public class BrochureDownloadAdapter extends BaseAdapter {

    private List<Brochure> brochures;
    private Context context;

    public BrochureDownloadAdapter(Context context, List<Brochure> brochures) {
        this.brochures = brochures;
        this.context = context;
    }

    @Override
    public int getCount() {
        return brochures.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.layout_brochure_download, null);
        }

        final Brochure brochure = brochures.get(position);
        Button downloadBrochure = convertView.findViewById(R.id.download_brochure);
        if (brochure != null && StringUtils.isNotBlank(brochure.getName()) && StringUtils.isNotBlank(brochure.getLink())) {
            downloadBrochure.setText(brochure.getName());
        }
        downloadBrochure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadBrochure(brochure);
            }
        });
        return convertView;
    }

    public void downloadBrochure(Brochure brochure) {
        if (Utils.checkInternetConnection(context)) {
            new PDFFileDownloader(context).execute(brochure.getLink(), brochure.getName());
        } else {
            File pdfFile =
                    new File(context.getFilesDir() + "/" + context.getString(R.string.BROCHURE_DIRECTORY) + "/" + brochure.getName());
            if (!pdfFile.exists()) {
                new AlertDialog.Builder(context)
                        .setTitle("No brochure file found")
                        .setMessage("Could you please connect to the Internet and re-download the brochure file ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Utils.openPdfFile(context, brochure.getName());
            }
        }
    }
}
