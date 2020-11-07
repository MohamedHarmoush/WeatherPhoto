package com.harmoush.photoweather.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.harmoush.photoweather.R
import com.harmoush.photoweather.ui.weatherphoto.camera.CameraInteractionListener
import com.harmoush.photoweather.ui.weatherphoto.camera.UiProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CustomCameraManager(
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val uiProvider: UiProvider,
    private val cameraInteractionListener: CameraInteractionListener
) : LifecycleObserver {

    private val cameraManager: CameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val cameraFacing: Int = CameraCharacteristics.LENS_FACING_BACK
    private var surfaceTextureListener: SurfaceTextureListener? = null
    private var previewSize: Size? = null
    private var cameraId: String? = null
    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null
    private var stateCallback: CameraDevice.StateCallback? = null
    private var cameraDevice: CameraDevice? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var captureRequest: CaptureRequest? = null
    private var cameraCaptureSession: CameraCaptureSession? = null
    private var imageFile: File? = null

    init {
        setTextureListener()
        setCameraStateCallback()
    }

    private fun setCameraStateCallback() {
        stateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {
                this@CustomCameraManager.cameraDevice = cameraDevice
                createPreviewSession()
            }

            override fun onDisconnected(cameraDevice: CameraDevice) {
                cameraDevice.close()
                this@CustomCameraManager.cameraDevice = null
            }

            override fun onError(cameraDevice: CameraDevice, error: Int) {
                cameraDevice.close()
                this@CustomCameraManager.cameraDevice = null
            }
        }
    }

    private fun setTextureListener() {
        surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                setUpCamera()
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                // no impl needed
            }

            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
                // no impl needed
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        try {
            cameraManager.openCamera(cameraId!!, stateCallback!!, backgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun openBackgroundThread() {
        backgroundThread = HandlerThread(CAMERA_BACKGROUND_THREAD_NAME)
        backgroundThread?.start()
        backgroundThread?.looper?.let {
            backgroundHandler = Handler(it)
        }
    }

    private fun createImageGallery(): File {
        val storageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val galleryFolder = File(storageDirectory, context.resources.getString(R.string.app_name))
        if (!galleryFolder.exists()) {
            val wasCreated = galleryFolder.mkdirs()
            if (!wasCreated) {
                Log.e("CapturedImages", "Failed to create directory")
            }
        }
        return galleryFolder
    }

    @Throws(IOException::class)
    private fun createImageFile(galleryFolder: File): File {
        val timeStamp = SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "image_" + timeStamp + "_"
        return File.createTempFile(imageFileName, IMAGE_FORMAT, galleryFolder)
    }

    private fun createPreviewSession() {
        try {
            val surfaceTexture = uiProvider.textureView.surfaceTexture
            surfaceTexture!!.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)
            val previewSurface = Surface(surfaceTexture)
            captureRequestBuilder =
                cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder!!.addTarget(previewSurface)

            cameraDevice!!.createCaptureSession(
                listOf(previewSurface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        if (cameraDevice == null) {
                            return
                        }
                        try {
                            captureRequest = captureRequestBuilder!!.build()
                            this@CustomCameraManager.cameraCaptureSession = cameraCaptureSession
                            this@CustomCameraManager.cameraCaptureSession!!.setRepeatingRequest(
                                captureRequest!!,
                                null, backgroundHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                        // no impl needed
                    }
                }, backgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun setUpCamera() {
        try {
            for (cameraId in cameraManager.cameraIdList) {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == cameraFacing) {
                    val streamConfigurationMap = cameraCharacteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                    )
                    if (streamConfigurationMap != null) {
                        previewSize =
                            streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)[0]
                        this.cameraId = cameraId
                    }
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun onPhotoCaptureClicked() {
        var outputPhoto: FileOutputStream? = null
        var errorMessage: String? = null
        try {
            imageFile = createImageFile(createImageGallery())
            outputPhoto = FileOutputStream(imageFile)
            uiProvider.textureView.bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
        } catch (e: Exception) {
            errorMessage = e.localizedMessage
            Log.e(TAG, "${e.message}")
        } finally {
            try {
                outputPhoto?.close()
            } catch (e: IOException) {
                Log.e(TAG, "${e.message}")
                errorMessage = e.localizedMessage
            }
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) && imageFile != null) {
                cameraInteractionListener.onPhotoCaptureSuccess(imageFile)
            } else {
                cameraInteractionListener.onPhotoCaptureFailure(errorMessage)
            }
        }
    }

    private fun closeCamera() {
        cameraDevice?.close()
        cameraCaptureSession?.close()
        cameraDevice = null
        cameraCaptureSession = null
    }

    private fun closeBackgroundThread() {
        backgroundThread?.quitSafely()
        backgroundThread = null
        backgroundHandler = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        openBackgroundThread()
        if (uiProvider.textureView.isAvailable) {
            setUpCamera()
            openCamera()
        } else {
            uiProvider.textureView.surfaceTextureListener = surfaceTextureListener
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        closeCamera()
        closeBackgroundThread()
    }

    companion object {
        private const val IMAGE_FORMAT = ".jpg"
        private val TAG = CustomCameraManager::class.simpleName
        private const val CAMERA_BACKGROUND_THREAD_NAME = "camera_background_thread"
    }
}