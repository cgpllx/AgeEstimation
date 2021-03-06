package com.konka.ageestimation.ui.activity;

import com.konka.ageestimation.R;
import com.konka.ageestimation.ui.fragment.UpdateDialogFragment;
import com.konka.ageestimation.ui.presenter.IUpdateCallback;
import com.konka.ageestimation.ui.presenter.UpdataPresenter;
import com.konka.ageestimation.ui.util.AppUtil;
import com.kubeiwu.commontool.view.setting.GroupView;
import com.kubeiwu.commontool.view.setting.KSettingView;
import com.kubeiwu.commontool.view.setting.RowView;
import com.kubeiwu.commontool.view.setting.viewimpl.DefaultRowView;
import com.kubeiwu.commontool.view.util.DisplayOptions;
import com.kubeiwu.commontool.view.util.Listener.OnGroupViewItemClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingActivity extends BaseActivity implements OnGroupViewItemClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(initView(selectorPara));
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	protected View initView(DisplayOptions selectorPara) {
		KSettingView containerView = new KSettingView(this);

		containerView.setDisplayOptions(selectorPara);
		GroupView groupView1 = containerView.addGroupViewItem(1);
		groupView1.setOnItemClickListener(this);
		// groupView1.addRowViewItem(ListRowView.class, 1, "选择识别方式", 0,
		// R.drawable.arrow_to_right, ParaSetting.mode).setEntries("离线识别",
		// "网络识别").setEntryValues(ModeConstant.OFF_LINE, ModeConstant.ON_LINE);
		groupView1.addRowViewItem(DefaultRowView.class, 2, "关于我们");
		groupView1.addRowViewItem(DefaultRowView.class, 3, "检测更新");
		containerView.commit();
		return containerView;
	}

	DisplayOptions selectorPara = new DisplayOptions.Builder()//
			.setNormalLineColorId(android.R.color.white)//
			.setDividerResId(R.drawable.line).setRowleftpadding(21)//
			.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE).build();

	@Override
	public void onItemClick(GroupView arg0, RowView arg1) {
		switch (arg1.getItemId()) {
		case 1:
			srartRegisterDisplayActivity();
			break;
		case 2:
			srartAboutActivity();
			break;
		case 3:
			checkUpdate();
			break;
		}
	}

	UpdataPresenter updataPresenter = new UpdataPresenter();

	private void checkUpdate() {
		updataPresenter.update(AppUtil.getVersionCode(this), new IUpdateCallback() {
			@Override
			public void transmitResult(boolean result) {
				if (result) {
					new UpdateDialogFragment().show(getSupportFragmentManager(), "update");
				}else{
					Toast.makeText(getApplicationContext(), "已经是最新版本", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure() {
				Toast.makeText(getApplicationContext(), "服务器或者网络异常", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void srartAboutActivity() {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

	private void srartRegisterDisplayActivity() {
		// Intent intent = new Intent(this, RegisterFaceTip1.class);
		// startActivity(intent);
	}

}
