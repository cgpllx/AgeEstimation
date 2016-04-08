package com.konka.ageestimation.ui.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.konka.ageestimation.R;
import com.konka.ageestimation.ui.fragment.UpdateDialogFragment;
import com.konka.ageestimation.ui.pojo.Face;
import com.konka.ageestimation.ui.presenter.AnalysePresenterCompl;
import com.konka.ageestimation.ui.presenter.AnimationPresenterCompl;
import com.konka.ageestimation.ui.presenter.DrawPresenterCompl;
import com.konka.ageestimation.ui.presenter.IAnalysePresenter;
import com.konka.ageestimation.ui.presenter.IAnimationPresenter;
import com.konka.ageestimation.ui.presenter.IDrawPresenter;
import com.konka.ageestimation.ui.presenter.ISharePresenter;
import com.konka.ageestimation.ui.presenter.IUpdateCallback;
import com.konka.ageestimation.ui.presenter.SharePresenterCompl;
import com.konka.ageestimation.ui.presenter.UpdataPresenter;
import com.konka.ageestimation.ui.util.AppUpGradeManager;
import com.konka.ageestimation.ui.util.AppUtil;
import com.konka.ageestimation.ui.view.IPhotoView;
import com.konka.ageestimation.ui.widget.AgeIndicatorLayout;
import com.konka.ageestimation.ui.widget.FaceImageView;


public class MainActivity extends BaseActivity implements IPhotoView, View.OnClickListener {

	public static final  int ACTIVITY_REQUEST_CAMERA = 0;
	public static final int ACTIVITY_REQUEST_GALLERY = 1;
	private IAnalysePresenter analysePresenter;
	private IDrawPresenter    drawPresenter;
	private FaceImageView     faceImageView;
	private ProgressDialog progressDialog;
	private AgeIndicatorLayout ageIndicatorLayout;
    private View photoContainer;
    UpdataPresenter updataPresenter=new UpdataPresenter();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        injectView();
        setListener();
        init();
        updataPresenter.update(AppUtil.getVersionCode(this),new IUpdateCallback(){

			@Override
			public void transmitResult(boolean result) {
				if(result){
					new UpdateDialogFragment().show(getSupportFragmentManager(), "update");
				}
			}

			@Override
			public void onFailure() {
				
			}
        	
        });
	}

    private void injectView(){
        faceImageView = (FaceImageView) this.findViewById(R.id.iv_main_face);
        ageIndicatorLayout = (AgeIndicatorLayout) this.findViewById(R.id.layout_main_age);
        photoContainer = this.findViewById(R.id.layout_main_photo);
    }

    private void setListener() {
        this.findViewById(R.id.btn_main_camera).setOnClickListener(this);
        this.findViewById(R.id.btn_main_gallery).setOnClickListener(this);
        this.findViewById(R.id.btn_main_share).setOnClickListener(this);
    }

    private void init() {
        analysePresenter = new AnalysePresenterCompl(this);
        drawPresenter = new DrawPresenterCompl(this, this);
        try {
//            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        } catch (Exception e) {;
            e.printStackTrace();
        }
        IAnimationPresenter animationPresenter = new AnimationPresenterCompl();
        animationPresenter.doLogoAnimation(findViewById(R.id.iv_main_introduce_logo));
        animationPresenter.doIntroduceAnimation(findViewById(R.id.layout_main_introduce_text));
    }

	@Override
	public void onGetFaces(List<Face> faces) {
		showProgressDialog(false);
        if (faces==null){
            toast(getResources().getString(R.string.main_analyze_fail));
        }else if (faces.size()<=0){
            toast(getResources().getString(R.string.main_analyze_no_face));
        }else {
            drawPresenter.drawFaces(ageIndicatorLayout, faceImageView, faces);
        }
	}

	@Override
	public void onGetImage(Bitmap bitmap, String imgPath) {
		faceImageView.clearFaces();
		ageIndicatorLayout.clearAges();
        faceImageView.setImageBitmap(bitmap);
        this.findViewById(R.id.layout_main_introduce).setVisibility(View.GONE);
        this.findViewById(R.id.layout_main_border).setBackgroundResource(R.color.color_1da4e8);
        analysePresenter.doAnalyse(this,bitmap,imgPath);
        
    }

	@Override
	public void showProgressDialog(Boolean isShow) {
		if (progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(getResources().getString(R.string.main_loading));
		}
		if (isShow){
			if (!progressDialog.isShowing())
				progressDialog.show();
		}else {
			if (progressDialog.isShowing())
				progressDialog.dismiss();
		}


	}

    @Override
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public View getPhotoContainer() {
        return photoContainer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
		case R.id.action_setting:
			about();
			break;
		}
        return super.onOptionsItemSelected(item);
    }

    private void about() {
		Intent intent=new Intent(this, SettingActivity.class);
		startActivity(intent);
	}

	@Override
    public void onClick(View v) {

        switch(v.getId()) {
        case R.id.btn_main_camera:
            analysePresenter.pickPhoto(this,AnalysePresenterCompl.TYPE_PICK_CAMERA);
            break;
        case R.id.btn_main_gallery:
            analysePresenter.pickPhoto(this,AnalysePresenterCompl.TYPE_PICK_GALLERY);
            break;
        case R.id.btn_main_share:
	        ISharePresenter sharePresenter  = new SharePresenterCompl(this);
	        sharePresenter.doShare(this, this.findViewById(android.R.id.content));
            break;
        default:
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case ACTIVITY_REQUEST_CAMERA:
            case ACTIVITY_REQUEST_GALLERY:
            	System.out.println("cgp=getImage111---"+resultCode);
            	System.out.println("cgp=getImagedata---"+data);
                if(resultCode==RESULT_OK){
            		analysePresenter.getImage(this,data);
                }
                break;
            default:
                break;
        }
    }


}
