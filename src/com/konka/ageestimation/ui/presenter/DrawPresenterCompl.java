package com.konka.ageestimation.ui.presenter;

import android.app.Activity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.konka.ageestimation.ui.pojo.Face;
import com.konka.ageestimation.ui.view.IPhotoView;
import com.konka.ageestimation.ui.widget.AgeIndicatorLayout;
import com.konka.ageestimation.ui.widget.FaceImageView;

 
public class DrawPresenterCompl implements IDrawPresenter {
    List<View>views;
    IPhotoView iPhotoView;
    /*private WindowManager windowManager;
    private WindowManager.LayoutParams params;*/

    public DrawPresenterCompl(Activity activity,IPhotoView iPhotoView) {
        this.iPhotoView = iPhotoView;
        views = new ArrayList<>();

        /*windowManager = activity.getWindowManager();
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.LAST_APPLICATION_WINDOW,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;*/

    }


    @Override
    public void drawFaces(AgeIndicatorLayout ageIndicatorLayout, FaceImageView faceImageView, List<Face> faces) {
        faceImageView.drawFaces(faces);
        ageIndicatorLayout.drawAges(faces, (ageIndicatorLayout.getMeasuredWidth()-faceImageView.getMeasuredWidth())/2, (ageIndicatorLayout.getMeasuredHeight()-faceImageView.getMeasuredHeight())/2);
 
    }

    @Override
    public void clearViews() {
        /*Iterator<View> iterator = views.iterator();
        while (iterator.hasNext()){
            View view = iterator.next();
            windowManager.removeViewImmediate(view);
        }
        views.clear();*/
    }
}
