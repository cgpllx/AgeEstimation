package com.konka.ageestimation.ui.pojo;

 
public class Face {
//    public int faceId;
    public FaceRectangle faceRectangle;
    public Attributes attributes;
    public  Face(){
        faceRectangle = new FaceRectangle();
        attributes = new Attributes();
    }

}