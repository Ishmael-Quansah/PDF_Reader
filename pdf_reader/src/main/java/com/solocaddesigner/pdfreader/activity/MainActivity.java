package com.solocaddesigner.pdfreader.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.solocaddesigner.pdfreader.BuildConfig;
import com.solocaddesigner.pdfreader.R;
import com.solocaddesigner.pdfreader.adapter.PdfAdapter;
import com.solocaddesigner.pdfreader.databinding.ActivityMainBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PdfAdapter adapter;
    private List<File> pdfList;
    private boolean isFileLoaded = false;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                displayPdf();
                return;
            }
            Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
            startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
        } else {
            runtimePermission();
        }

    }

    public void openWebActivity(View view){
        if (TextUtils.isEmpty(binding.webUrlEdt.getText().toString())) {
            Toast.makeText(MainActivity.this, "Enter url", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
        intent.putExtra("webUrl",binding.webUrlEdt.getText().toString());
        startActivity(intent);
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            displayPdf();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }

    };

    private void runtimePermission() {
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    public ArrayList<File> findPdf() {
        ArrayList<File> arrayList = new ArrayList<>();

        String[] projection = {
                "_data",
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.SIZE,
        };

        String mimeType = "application/pdf";

        String whereClause = MediaStore.Files.FileColumns.MIME_TYPE + " IN ('" + mimeType + "')";
        String orderBy = MediaStore.Files.FileColumns.SIZE + " DESC";
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"),
                projection,
                whereClause,
                null,
                orderBy);

        int uriData = cursor.getColumnIndexOrThrow("_data");

        if (cursor.moveToFirst()) {
            do {
                String path = cursor.getString(uriData);
                File file1 = new File(path);
                arrayList.add(file1);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return arrayList;
    }

    public void displayPdf() {
        isFileLoaded = true;
        binding.rv.setHasFixedSize(true);
        binding.rv.setLayoutManager(new GridLayoutManager(this, 3));
        pdfList = new ArrayList<>();
        pdfList.addAll(findPdf());
        adapter = new PdfAdapter(this, pdfList);
        binding.rv.setAdapter(adapter);
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isFileLoaded && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager())
            displayPdf();
    }
}