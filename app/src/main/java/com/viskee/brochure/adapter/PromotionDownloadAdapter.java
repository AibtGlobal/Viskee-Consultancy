package com.viskee.brochure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.viskee.brochure.R;
import com.viskee.brochure.model.Promotion;
import com.viskee.brochure.util.PDFFileDownloader;
import com.viskee.brochure.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

public class PromotionDownloadAdapter extends BaseAdapter {

    private List<Promotion> promotions;
    private Context context;

    public PromotionDownloadAdapter(Context context, List<Promotion> promotions) {
        this.promotions = promotions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return promotions.size();
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
            convertView = layoutInflater.inflate(R.layout.layout_promotion_download, null);
        }

        final Promotion promotion = promotions.get(position);
        Button downloadPromotion = convertView.findViewById(R.id.download_promotion);
        if (promotion != null && StringUtils.isNotBlank(promotion.getName()) && StringUtils.isNotBlank(promotion.getLink())) {
            downloadPromotion.setText(promotion.getName());
        }
        downloadPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPromotion(promotion);
            }
        });
        return convertView;
    }

    public void downloadPromotion(Promotion promotion) {
        if (Utils.checkInternetConnection(context)) {
            new PDFFileDownloader(context).execute(promotion.getLink(), promotion.getName());
        } else {
            File pdfFile =
                    new File(context.getFilesDir() + "/" + context.getString(R.string.PROMOTION_DIRECTORY) + "/" + promotion.getName());
            if (!pdfFile.exists()) {
                new AlertDialog.Builder(context)
                        .setTitle("No promotion file found")
                        .setMessage("Could you please connect to the Internet and re-download the promotion file ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Utils.openPdfFile(context, promotion.getName());
            }
        }
    }
}
