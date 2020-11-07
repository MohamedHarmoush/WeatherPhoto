package com.harmoush.photoweather.ui.weatherphoto.camera;

import java.io.File;

public interface CameraInteractionListener {

    void onPhotoCaptureSuccess(File imageFile);

    void onPhotoCaptureFailure(String errorMessage);
}
