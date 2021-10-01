package com.ssafy.herehear.feature.home.readmode.audiobook

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.net.Uri
import android.os.*
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import java.io.*
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Semaphore

/**
 * Created by BRB_LAB on 2016-06-07.
 * Modified by JS on 2019-04-03.
 */
class Preview(
    private val mContext: Context,
    private val mTextureView: TextureView,
    private val mWideAngleButton: Button,
    private val mCameraCaptureButton: Button,
    private val buttonNext: Button,
) :
    Thread() {
    private var mPreviewSize: Size? = null
    private var mCameraDevice: CameraDevice? = null
    private var mPreviewBuilder: CaptureRequest.Builder? = null
    private var mPreviewSession: CameraCaptureSession? = null
    private var mCameraId = "0"
    private var uri: Uri? = null

    companion object {
        private const val TAG = "Preview : "
        private val ORIENTATIONS = SparseIntArray(4)

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

    private fun getBackFacingCameraId(cManager: CameraManager): String? {
        try {
            for (cameraId in cManager.cameraIdList) {
                val characteristics = cManager.getCameraCharacteristics(cameraId)
                val cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING)!!
                if (cOrientation == CameraCharacteristics.LENS_FACING_BACK) return cameraId
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return null
    }

    fun openCamera() {
        val manager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Log.e(TAG, "openCamera E")
        try {
            val cameraId = getBackFacingCameraId(manager)
            val characteristics = manager.getCameraCharacteristics(mCameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            mPreviewSize = map!!.getOutputSizes(SurfaceTexture::class.java)[0]
            val permissionCamera =
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
            if (permissionCamera == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    (mContext as Activity),
                    arrayOf(Manifest.permission.CAMERA),
                    CameraActivity.REQUEST_CAMERA
                )
            } else {
                manager.openCamera(mCameraId, mStateCallback, null)
            }
        } catch (e: CameraAccessException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        Log.e(TAG, "openCamera X")
    }

    private val mSurfaceTextureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            // TODO Auto-generated method stub
            Log.e(
                TAG,
                "onSurfaceTextureAvailable, width=$width, height=$height"
            )
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(
            surface: SurfaceTexture,
            width: Int, height: Int
        ) {
            // TODO Auto-generated method stub
            Log.e(TAG, "onSurfaceTextureSizeChanged")
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            // TODO Auto-generated method stub
            return false
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            // TODO Auto-generated method stub
        }
    }
    private val mStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            // TODO Auto-generated method stub
            Log.e(TAG, "onOpened")
            mCameraDevice = camera
            Log.d("test", "${camera} startpreview 시작")
            startPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            // TODO Auto-generated method stub
            Log.e(TAG, "onDisconnected")
        }

        override fun onError(camera: CameraDevice, error: Int) {
            // TODO Auto-generated method stub
            Log.e(TAG, "onError")
        }
    }

    protected fun startPreview() {
        // TODO Auto-generated method stub
        Log.d("test", "1${mTextureView.surfaceTexture}")
        if (null == mCameraDevice || !mTextureView.isAvailable || null == mPreviewSize) {
            Log.e(TAG, "startPreview fail, return")
        }
        val texture = mTextureView.surfaceTexture
        if (null == texture) {
            Log.e(TAG, "texture is null, return")
            return
        }
        texture.setDefaultBufferSize(mPreviewSize!!.width, mPreviewSize!!.height)
        val surface = Surface(texture)
        try {
            mPreviewBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        } catch (e: CameraAccessException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        mPreviewBuilder!!.addTarget(surface)
        try {
            mCameraDevice!!.createCaptureSession(
                Arrays.asList(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        // TODO Auto-generated method stub
                        mPreviewSession = session
                        updatePreview()
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        // TODO Auto-generated method stub
                        Toast.makeText(mContext, "onConfigureFailed", Toast.LENGTH_LONG).show()
                    }
                },
                null
            )
        } catch (e: CameraAccessException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    protected fun updatePreview() {
        // TODO Auto-generated method stub
        if (null == mCameraDevice) {
            Log.e(TAG, "updatePreview error, return")
        }
        mPreviewBuilder!!.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        val thread = HandlerThread("CameraPreview")
        thread.start()
        val backgroundHandler = Handler(thread.looper)
        try {
            mPreviewSession!!.setRepeatingRequest(
                mPreviewBuilder!!.build(),
                null,
                backgroundHandler
            )
        } catch (e: CameraAccessException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    private val mDelayPreviewRunnable = Runnable { startPreview() }
    protected fun takePicture() {
        if (null == mCameraDevice) {
            Log.e(TAG, "mCameraDevice is null, return")
            return
        }
        try {
            var jpegSizes: Array<Size>? = null
            val cameraManager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val characteristics = cameraManager.getCameraCharacteristics(mCameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            if (map != null) {
                jpegSizes = map.getOutputSizes(ImageFormat.JPEG)
                //                Log.d("TEST", "map != null " + jpegSizes.length);
            }
            var width = 640
            var height = 480
            if (jpegSizes != null && 0 < jpegSizes.size) {
//                for (int i = 0 ; i < jpegSizes.length; i++) {
//                    Log.d("TEST", "getHeight = " + jpegSizes[i].getHeight() + ", getWidth = " + jpegSizes[i].getWidth());
//                }
                width = jpegSizes[0].width
                height = jpegSizes[0].height
            }
            val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
            val outputSurfaces: MutableList<Surface> = ArrayList(2)
            outputSurfaces.add(reader.surface)
            outputSurfaces.add(Surface(mTextureView.surfaceTexture))
            val captureBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(reader.surface)
            //            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            captureBuilder.set(
                CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_START
            )

            // Orientation
            val rotation = (mContext as Activity).windowManager.defaultDisplay.rotation
            captureBuilder.set(
                CaptureRequest.JPEG_ORIENTATION,
                ORIENTATIONS[rotation]
            )
            val date = Date()
            val dateFormat = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss")
            val file = File(
                Environment.getExternalStorageDirectory().toString() + "/DCIM",
                "pic_" + dateFormat.format(date) + ".jpg"
            )
            val readerListener: OnImageAvailableListener = object : OnImageAvailableListener {
                override fun onImageAvailable(reader: ImageReader) {
                    var image: Image? = null
                    try {
                        Log.d("test", "here")
                        image = reader.acquireLatestImage()
                        val buffer = image.planes[0].buffer
                        val bytes = ByteArray(buffer.capacity())
                        buffer[bytes]
                        var output: OutputStream? = null
                        try {
                            output = FileOutputStream(file)
                            output.write(bytes)
                            Log.d("test", "output 확인 ${output}")
                        } finally {
                            output?.close()

                            uri = Uri.fromFile(file)
                            Log.d("test", "uri 제대로 잘 바뀌었는지 확인 ${uri}")
                            Log.d("test", "file 확인 ${file}")
                            Log.d("test", "file.path 확인 ${file.path}")
                            // 프리뷰 이미지에 set 해줄 비트맵을 만들어준다
                            var bitmap: Bitmap = BitmapFactory.decodeFile(file.path)

                            // 비트맵 사진이 90도 돌아가있는 문제를 해결하기 위해 rotate 해준다
                            var rotateMatrix = Matrix()
                            rotateMatrix.postRotate(90F)
                            var rotatedBitmap: Bitmap = Bitmap.createBitmap(
                                bitmap,
                                0,
                                0,
                                bitmap.width,
                                bitmap.height,
                                rotateMatrix,
                                false
                            )
                            // 90도 돌아간 비트맵을 이미지뷰에 set 해준다
//                                                    img_previewImage.setImageBitmap(rotatedBitmap)
                            //                        binding.imageView.setImageBitmap(rotatedBitmap)
                            // 리사이클러뷰 갤러리로 보내줄 uriList 에 찍은 사진의 uri 를 넣어준다
                            //                        uriList.add(0, uri.toString())

                        }

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        if (image != null) {
                            image.close()
                            reader.close()
                        }
                    }
                }

            }
            val thread = HandlerThread("CameraPicture")
            thread.start()
            val backgroudHandler = Handler(thread.looper)
            reader.setOnImageAvailableListener(readerListener, backgroudHandler)
            val delayPreview = Handler()
            val captureListener: CaptureCallback = object : CaptureCallback() {
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest, result: TotalCaptureResult
                ) {
                    super.onCaptureCompleted(session, request, result)
                    Toast.makeText(mContext, "Saved:$file", Toast.LENGTH_SHORT).show()
//                    delayPreview.postDelayed(mDelayPreviewRunnable, 1000)
//                    var intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//                    intent.setData(Uri.fromFile(file))
//                    sendBroadcast(intent)
//                    startPreview()

                }
            }
            mCameraDevice!!.createCaptureSession(
                outputSurfaces,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        try {
                            session.capture(
                                captureBuilder.build(),
                                captureListener,
                                backgroudHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {}
                },
                backgroudHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun setSurfaceTextureListener() {
        mTextureView.surfaceTextureListener = mSurfaceTextureListener
    }

    fun onResume() {
        Log.d(TAG, "onResume")
        setSurfaceTextureListener()
    }

    private val mCameraOpenCloseLock = Semaphore(1)
    fun onPause() {
        // TODO Auto-generated method stub
        Log.d(TAG, "onPause")
        try {
            mCameraOpenCloseLock.acquire()
            if (null != mCameraDevice) {
                mCameraDevice!!.close()
                mCameraDevice = null
                Log.d(TAG, "CameraDevice Close")
            }
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.")
        } finally {
            mCameraOpenCloseLock.release()
        }
    }

    fun getUri(): Uri? {
        return uri
    }

    init {
        mWideAngleButton.setOnClickListener {
            openCamera()
//            onResume()
        }
        mCameraCaptureButton.setOnClickListener { takePicture() }

        buttonNext.setOnClickListener {
        }
    }

}