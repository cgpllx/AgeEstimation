package com.konka.ageestimation;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.konka.ageestimation.ui.util.FileUtil;

public class KonkaApplication extends Application {
//	public static final String MODEL_FILE = "/storage/sdcard0/konka";
//	// 人脸解锁
//	public static final String MODEL_FACE_VERIFICATION1 = MODEL_FILE + "/subsml.xml";
//	// 人脸区域检测
//	public static final String MODEL_FACE_DETECT1 = MODEL_FILE + "/haarcascade_frontalface_alt.xml";

	public static final String UNIQUENAME = "konka";

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		String filepath = getModelPath();
//		FileUtil.copyResToSdcard(getApplicationContext(), filepath);
	}

	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
			File cacheFile = context.getExternalCacheDir();
			if (cacheFile != null && !cacheFile.exists()) {
				cacheFile.mkdirs();
			}
			cachePath = cacheFile.getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	public String modelpath;

	// 获取模型文件路径
	public String getModelPath() {
		if (modelpath == null) {
			File file = getDiskCacheDir(getApplicationContext(), UNIQUENAME);
			if (!file.exists()) {
				file.mkdirs();
			}
			modelpath = file.getAbsolutePath();
		}
		return modelpath;
	}
}
