package com.konka.ageestimation.ui.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.konka.ageestimation.R;

public class FileUtil {

	private static final String TAG = "FileUtil";

	public static String getSdpath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static File getTempFile(String dir, String fieExtension) {
		String filePath = dir + File.separator + String.valueOf(System.currentTimeMillis()) + fieExtension;
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
				return null;
			}
		}
		return file;

	}

	public static Boolean makeFile(String[] dirs) throws IOException {
		String path = getSdpath();
		for (int i = 1; i <= dirs.length; i++) {
			if (i != dirs.length) {
				path = path + File.separator + dirs[i - 1];
				File dir = new File(path);
				if (!dir.exists())
					dir.mkdir();
			} else {
				path = path + File.separator + dirs[i - 1];
				File file = new File(path);
				if (!file.exists())
					file.createNewFile();
			}
		}
		return true;
	}

	public static File makeSdFile(String[] dirs) {
		String path = getSdpath();
		try {
			for (int i = 1; i <= dirs.length; i++) {
				if (i != dirs.length) {
					path = path + File.separator + dirs[i - 1];
					File dir = new File(path);
					if (!dir.exists())
						dir.mkdir();
				} else {
					path = path + File.separator + dirs[i - 1];
					File file = new File(path);
					if (!file.exists())
						file.createNewFile();
					return file;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return null;
	}

	public static String UriToFileName(String url, String replace) {
		return url.replaceAll("[/:\\\\|?*<>\"]", replace);

	}

	/*
	 * 将raw里的文件copy到sd卡下
	 */
	public static void copyResToSdcard(Context context, String name) {// name为sd卡下制定的路径
		Field[] raw = R.raw.class.getFields();
		System.out.println(raw);
		System.out.println(raw.length);
		for (Field r : raw) {
			try {
				System.out.println("R.raw." + r.getName());
				int id = context.getResources().getIdentifier(r.getName(), "raw", context.getPackageName());
				if (!r.getName().equals("allapps")) {
					String hz = ".xml";
					if (r.getName().equals("agema") || r.getName().equals("genderma")) {
						hz = ".txt";
					}
					String path = name + "/" + r.getName() + hz;
					File file = new File(path);
					System.out.println("path=" + path);
					if (file.exists()) {
						continue;
					}
					BufferedOutputStream bufEcrivain = new BufferedOutputStream((new FileOutputStream(file)));
					BufferedInputStream VideoReader = new BufferedInputStream(context.getResources().openRawResource(id));
					byte[] buff = new byte[20 * 1024];
					int len;
					while ((len = VideoReader.read(buff)) > 0) {
						bufEcrivain.write(buff, 0, len);
					}
					bufEcrivain.flush();
					bufEcrivain.close();
					VideoReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
