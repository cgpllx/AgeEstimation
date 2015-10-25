package com.konka.ageestimation.ui.presenter;

import java.util.List;

import com.konka.ageestimation.ui.pojo.Face;
import com.konka.ageestimation.ui.widget.AgeIndicatorLayout;
import com.konka.ageestimation.ui.widget.FaceImageView;

 
public interface IDrawPresenter {
    public void drawFaces(AgeIndicatorLayout ageIndicatorLayout, FaceImageView faceImageView,List<Face> faces);
    public void clearViews();
}
