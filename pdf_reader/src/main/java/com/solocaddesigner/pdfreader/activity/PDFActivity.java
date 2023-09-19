package com.solocaddesigner.pdfreader.activity;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.solocaddesigner.pdfreader.R;
import com.solocaddesigner.pdfreader.utils.PdfDocumentAdapter;

import java.io.File;

public class PDFActivity extends AppCompatActivity {

    private PDFView pdfView;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfactivity);
        pdfView = findViewById(R.id.pdfView);

        filePath = getIntent().getStringExtra("path");
        File file = new File(filePath);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/") + 1).replace(".pdf", ""));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pdfView.fromFile(file).load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            case R.id.print: {
                printPdf();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void printPdf() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(this, filePath,false);
            printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf_reader_menu, menu);
        return true;
    }
}