package com.example.oronz.mobiclientapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.File;

public class UpdateApp extends Activity {
    boolean isDeleted;
    Button btnupdate;
    TextView mesg;
    private BroadcastReceiver receiver;
    private long enqueue;
    private DownloadManager dm, downloadManager;
    private long refid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Mobiticket Update");
        builder.setMessage("Latest Version is Available.");
        builder.getContext().setTheme(R.style.AppTheme_NoActionBar);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "App Downloading...Please Wait", Toast.LENGTH_LONG).show();
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                new Thread(new Runnable() {
                    public void run() {
                        DownloadFiles();
                    }
                }).start();


            }
        });
        builder.setNegativeButton("Remind Me Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                UpdateApp.this.finish();

            }
        });
        builder.show();
    }


    public void DownloadFiles() {

        Uri Download_Uri = Uri.parse("https://github.com/oronibrian/mobiclient/releases/download/v1.0/app-debug.apk");

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("App Downloading " + "app-debug" + ".apk");
        request.setDescription("Downloading " + "app-debug" + ".apk");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/" + "app-debug.apk");


        refid = downloadManager.enqueue(request);


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(refid);
                    downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    Cursor c = downloadManager.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            //TODO : Use this local uri and launch intent to open file
                            Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent();
                            i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                            startActivity(i);


                        }
                    }
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }


    private void installApk() {
        Toast.makeText(getApplicationContext(), "Installing Update", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File("/download/app-debug.apk"));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);


    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
        this.finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}