package com.konka.ageestimation.ui.util;

import com.kubeiwu.commontool.view.setting.annomotion.IsPara;
import com.kubeiwu.commontool.view.util.Para;
import com.kubeiwu.commontool.view.util.SettingsUtil;

import android.content.Context;

public interface ParaSetting {
	@IsPara
	Para<Integer>  mode = new Para<Integer>("mode", 1);
	public class ParaUtil {
		public static void initPrar(Context context) {
			SettingsUtil.initPrar(context, ParaSetting.class);
		}
	}
}
