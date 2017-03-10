package com.webcon.sus.entity;

import android.graphics.Bitmap;

/**
 * 图片结点
 * @author m
 */
public class ImageNode {


    private short imageType;
    private Bitmap bitmap;

    public short getImageType() {
        return imageType;
    }

    public void setImageType(short imageType) {
        this.imageType = imageType;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void release(){
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
    }

}
