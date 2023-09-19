package com.solocaddesigner.pdfreader.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.solocaddesigner.pdfreader.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        String webUrl = getIntent().getStringExtra("webUrl");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Print Pdf");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(webUrl);
    }


    private void printTheWebPage(WebView webView) {
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " webpage"+webView.getUrl();
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);
        printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            case R.id.print: {
                printTheWebPage(webView);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf_reader_menu, menu);
        return true;
    }



}