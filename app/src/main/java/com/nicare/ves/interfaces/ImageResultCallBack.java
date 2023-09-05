package com.nicare.ves.interfaces;

import com.nicare.ves.persistence.ImageProcessResult;

public interface ImageResultCallBack {

    void imageResultReady(ImageProcessResult result);
}
