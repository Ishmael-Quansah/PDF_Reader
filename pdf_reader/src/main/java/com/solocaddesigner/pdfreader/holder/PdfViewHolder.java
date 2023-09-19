package com.solocaddesigner.pdfreader.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.solocaddesigner.pdfreader.R;

public class PdfViewHolder extends RecyclerView.ViewHolder {

    public TextView textName;
    public CardView cardView;

    public PdfViewHolder(@NonNull View itemView) {
        super(itemView);

        textName = itemView.findViewById(R.id.pdf_txtName);
        cardView = itemView.findViewById(R.id.pdf_cardView);
    }
}
