package com.solocaddesigner.pdfreader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.solocaddesigner.pdfreader.R;
import com.solocaddesigner.pdfreader.activity.PDFActivity;
import com.solocaddesigner.pdfreader.holder.PdfViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfViewHolder> {

    private Context context;
    private List<File> pdfFiles;

    public PdfAdapter(Context context, List<File> pdfFiles) {
        this.context = context;
        this.pdfFiles = pdfFiles;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PdfViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        holder.textName.setText(pdfFiles.get(position).getName().substring(pdfFiles.get(position).getName().lastIndexOf("/")+1).replace(".pdf",""));
        holder.textName.setSelected(true);

        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PDFActivity.class);
            intent.putExtra("path",pdfFiles.get(position).getAbsolutePath());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }
}
