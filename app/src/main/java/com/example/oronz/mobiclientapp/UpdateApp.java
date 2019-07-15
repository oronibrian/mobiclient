package com.example.oronz.mobiclientapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

import java.io.File;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class UpdateApp extends Activity {
    boolean isDeleted;
    Button btnupdate;
    TextView mesg;
    String version, url1;
    private BroadcastReceiver receiver;
    private long enqueue;
    private DownloadManager dm, downloadManager;
    private long refid;
    ACProgressFlower mProgress;
    MobiClientApplication app;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);


        app = (MobiClientApplication) getApplication();

        mProgress = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .text("Checking...")
                .fadeColor(Color.DKGRAY).build();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        mProgress.show();



        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("oronibrian", "mobiclient")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        Log.e("Latest Version", update.getLatestVersion());
                        Log.e("Latest Version Code", String.valueOf(update.getLatestVersionCode()));
                        Log.e("Release notes", ""+ update.getReleaseNotes());
                        Log.e("URL", String.valueOf(update.getUrlToDownload()));
                        Log.e("Is update available?", Boolean.toString(isUpdateAvailable));



                         if (isUpdateAvailable){
                             builder.show();
                             mProgress.dismiss();



                             version="v"+update.getLatestVersion();
                             url1= update.getUrlToDownload() +"/download/"+app.getApk_name();
                             Log.e("url?", url1);



                         }else {

                             Toast.makeText(UpdateApp.this, "No update", Toast.LENGTH_SHORT).show();
                             mProgress.dismiss();

                         }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();




        builder.setTitle("Mobiticket Update");
        builder.setMessage("New Version Available.");
        builder.getContext().setTheme(R.style.AppTheme_NoActionBar);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "App Downloading...Please Wait", Toast.LENGTH_LONG).show();
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                new Thread(new Runnable() {
                    public void run() {
                        DownloadFiles();
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }).start();


            }
        });
        builder.setNegativeButton("Remind Me Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                UpdateApp.this.finish();

            }
        });
    }


    public void DownloadFiles() {

        Uri Download_Uri = Uri.parse(url1);

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("App Downloading " + "app-debug" + ".apk");
        request.setDescription("Downloading " + "app-debug" + ".apk");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/" + "app-debug.apk");


        refid = downloadManager.enqueue(request);

        File file = new File(Environment.DIRECTORY_DOWNLOADS, "/" + "app-debug.apk");

        if (file.exists())
            //file.delete() - test this, I think sometimes it doesnt work
            file.delete();

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
                            progressBar.setVisibility(View.GONE);
//
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





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}