package com.konka.ageestimation.ui.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.konka.ageestimation.R;

public class AppUpGradeManager {
	public static long downLoadApk(Context mContext, String downloadurl) {
		DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request down = new DownloadManager.Request(Uri.parse(downloadurl));
		// DownLoadProvider d;
		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		// down.setAllowedNetworkTypes(0);
		down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
		// DownloadManager.Request.
		// 禁止发出通知，既后台下载
		// down.setShowRunningNotification(true);
		// down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		// 不显示下载界面
		down.setVisibleInDownloadsUi(true);
		// 设置下载后文件存放的位置
		// down.setDestinationInExternalFilesDir(mContext, null,"vpic.apk");
		// 设置下载路径 /mnt/sosspad/download
		// Environment.getExternalStoragePublicDirectory(type);
		down.setDestinationInExternalPublicDir("/download/", mContext.getString(R.string.app_name) + ".apk");
		// 将下载请求放入队列
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {// sdk17
			down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		// 表示下载进行中和下载完成的通知栏是否显示。默认只显示下载中通知。
		return manager.enqueue(down);
	}

}
