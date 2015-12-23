package com.konka.ageestimation.ui.presenter;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.konka.ageestimation.R;
import com.konka.ageestimation.ui.util.BitmapUtil;
import com.konka.ageestimation.ui.util.FileUtil;
import com.konka.ageestimation.ui.view.IPhotoView;

 
public class SharePresenterCompl implements ISharePresenter {
	IPhotoView iPhotoView;
	File appBaseDir;

	public SharePresenterCompl(IPhotoView iPhotoView) {
		this.iPhotoView = iPhotoView;
		File dir = new File(FileUtil.getSdpath() + File.separator + "Moe Studio");
		dir.mkdir();
		appBaseDir = new File(dir.getAbsolutePath() + File.separator + "How Old Robot");
		appBaseDir.mkdir();
	}

	@Override
	public void doShare(Context context, View view) {
		Bitmap b = BitmapUtil.getViewBitmap(view);
		if (b!=null){
			File temp=null;
			try {
				temp = FileUtil.getTempFile(appBaseDir.getAbsolutePath(),".jpg");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (temp != null) {
				BitmapUtil.saveBitmapToSd(b,80,temp.getAbsolutePath());
			}else{
				return;
			}
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/jpeg");
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			b.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
			String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),b, "Share Image", "How Old Camera's Share Image");
			Uri imageUri =  Uri.parse(path);
			share.putExtra(Intent.EXTRA_STREAM, imageUri);
            try {
                context.startActivity(Intent.createChooser(share, context.getResources().getString(R.string.share_select_title)));
            } catch (Exception e) {
                iPhotoView.toast(context.getResources().getString(R.string.share_fail));
                e.printStackTrace();
            }
        }
	}
}
