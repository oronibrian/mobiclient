package com.example.oronz.mobiclientapp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.io.File;

public class UpdateApp extends Activity {
    private BroadcastReceiver receiver;
    private long enqueue;
    private DownloadManager dm;
    boolean isDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Mobiticket Update");
        builder.setMessage("Latest Version is Available.");
        builder.getContext().setTheme(R.style.AppTheme_NoActionBar);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "App Downloading...Please Wait", Toast.LENGTH_LONG).show();
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                File file = new File(Environment.getExternalStorageDirectory() + "Download/app-debug.apk");
                if (file.exists()) {

                    isDeleted = file.delete();
                    deleteAndInstall();
                } else {
                    firstTimeInstall();
                }
            }
        });
        builder.setNegativeButton("Remind Me Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                UpdateApp.this.finish();
            }
        });
        builder.show();
    }

    private void firstTimeInstall() {
        Log.d("May be 1st Update:", "OR deleteed from folder");
        downloadAndInstall();
    }

    private void deleteAndInstall() {
        if (isDeleted) {
            Log.d("Deleted Existance file:", String.valueOf(isDeleted));
            downloadAndInstall();

        } else {
            Log.d("NOT DELETED:", String.valueOf(isDeleted));
            Toast.makeText(getApplicationContext(), "Error in Updating...Please try Later", Toast.LENGTH_LONG).show();
        }
    }

    private void downloadAndInstall() {
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse("https://github.com/oronibrian/mobiclient/releases/download/v1.0/app-debug.apk"))
                .setTitle("Update App")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);//



        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-debug.apk");

        enqueue = dm.enqueue(request);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_LONG).show();

                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            Log.d("ainfo", uriString);

                            if (downloadId == c.getInt(0)) {
                                Log.d("DOWNLOAD PATH:", c.getString(c.getColumnIndex("local_uri")));


                                Log.d("isRooted:", String.valueOf(isRooted()));

                                if (!isRooted()) {
                                    //if your device is not rooted
                                    File file = new File(Environment.getExternalStorageDirectory() + "/download/app-debug.apk");
                                    Intent intent_install = new Intent(Intent.ACTION_VIEW);
                                    Uri fileUri = FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", file);

//                                    intent_install.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "app-debug.apk")), "application/vnd.android.package-archive");
                                    Log.d("phone path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "app-debug.apk");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                                            intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");

                                    Log.d("IN INSTALLER:", Environment.getExternalStorageDirectory() + "/download/app-debug.apk");
                                    startActivity(intent_install);
                                    Toast.makeText(getApplicationContext(), "App Installing", Toast.LENGTH_LONG).show();


                                    if (isUdateInstalled("com.example.oronz.mobiclientapp")) {
                                        //app installed
                                        Toast.makeText(getApplicationContext(), "The App has been Updated Successfully", Toast.LENGTH_LONG).show();
                                        Intent i=new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                        finish();

                                    } else {
                                    //app not installed
                                        Toast.makeText(getApplicationContext(), "Update Not Installed", Toast.LENGTH_LONG).show();

                                        Intent i=new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                        finish();

                                    }


                                } else {
                                    //if your device is rooted then you can install or update app in background directly
                                    Toast.makeText(getApplicationContext(), "App Installing...Please Wait", Toast.LENGTH_LONG).show();
                                    File file = new File(Environment.getExternalStorageDirectory() + "/download/app-debug.apk");
                                    Log.d("IN INSTALLER:", Environment.getExternalStorageDirectory() + "/download/app-debug.apk");
                                    if (file.exists()) {
                                        try {
                                            String command;
                                            Log.d("IN File exists:", Environment.getExternalStorageDirectory() + "/download/app-debug.apk");

                                            command = "pm install -r " + Environment.getExternalStorageDirectory() + "/download/app-debug.apk";
                                            Log.d("COMMAND:", command);
                                            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
                                            proc.waitFor();
                                            Toast.makeText(getApplicationContext(), "App Installed Successfully", Toast.LENGTH_LONG).show();
                                            Log.d("App Installed", "App Installed Successfully");

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    c.close();





//
//                File file = new File(Environment.getExternalStorageDirectory() + "/download/app-debug.apk");
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        Uri fileUri = FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", file);
//                        Intent intent1 = new Intent(Intent.ACTION_VIEW, fileUri);
//                        intent1.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
//                        intent1.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
//                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        startActivity(intent1);
//
//                    } else {
//                        Intent intent2 = new Intent(Intent.ACTION_VIEW);
//                        intent2.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent2);
//                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private static boolean isRooted() {
        return findBinary("su");
    }

    public static boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    private boolean isUdateInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}