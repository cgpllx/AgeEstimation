package com.konka.ageestimation.ui.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konka.ageestimation.ui.util.AppUpGradeManager;

public class UpdateDialogFragment extends DialogFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Builder builder = new AlertDialog.Builder(getActivity()).setTitle("更新提示")//
				.setMessage("有新版本")//
				.setNegativeButton("更新", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String apkurl="http://howold.fatebird.cn/upload/AgeEstimation.apk";
						AppUpGradeManager.downLoadApk(getActivity(), apkurl);
					}
				}).setPositiveButton("取消", null);
		Dialog alertDialog = builder.create();
		return alertDialog;
	}
}
