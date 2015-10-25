package com.konka.ageestimation.ui.view;

import android.graphics.Bitmap;

import java.util.List;

import com.konka.ageestimation.ui.pojo.Face;

import android.view.View;

 
public interface IPhotoView {
    public void onGetFaces(List<Face> faces);
    public void onGetImage(Bitmap bitmap,String imgPath);
	public void showProgressDialog(Boolean isShow);
    public void toast(String msg);
    public View getPhotoContainer();
}
