package com.konka.ageestimation.ui.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

 
public interface IAnalysePresenter {
//	public void doAnalyse(String imgPath);
    public void doAnalyse(Context context,Bitmap bitmap,String imgPath);
    public void pickPhoto(Activity activity,int type);
    public void getImage(Context context,Intent intent);
}
