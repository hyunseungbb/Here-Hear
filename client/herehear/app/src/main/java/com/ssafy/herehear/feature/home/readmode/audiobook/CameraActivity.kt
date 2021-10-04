package com.ssafy.herehear.feature.home.readmode.audiobook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.ssafy.herehear.BaseActivity
import com.ssafy.herehear.MainActivity
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityCameraBinding


class CameraActivity : BaseActivity() {

    val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }

    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            REQUEST_CAMERA -> {
//            binding.cameraTextureView =
//                findViewById<View>(R.id.cameraTextureView) as TextureView
                mPreview = Preview(
                    cameraActivity,
                    binding.cameraTextureView,
                    binding.wide,
                    binding.capture,
                    binding.buttonNext,
                )
                mPreview!!.openCamera()
                Log.d(TAG, "mPreview set")
            }

            PERM_STORAGE -> {
                Toast.makeText(baseContext,
                    "good",
                    Toast.LENGTH_LONG).show()
                mPreview!!.openCamera()
//                requirePermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
            }
        }
    }

    override fun permissionDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }

//    private var mCameraTextureView: TextureView? = null
    private var mPreview: Preview? = null
//    private var mNormalAngleButton: Button? = null
//    private var mWideAngleButton: Button? = null
//    private var mCameraCaptureButton: Button? = null
//    private var mCameraDirectionButton: Button? = null
//    private var buttonGallery: Button? = null
//    private var buttonNext: Button? = null
    var cameraActivity: Activity = this
    private var selectedUri: Uri? = null
    var bookId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        mWideAngleButton = findViewById<View>(R.id.wide) as Button
//        mCameraCaptureButton = findViewById<View>(R.id.capture) as Button
//        mCameraTextureView = findViewById<View>(R.id.cameraTextureView) as TextureView
//        buttonGallery = findViewById<View>(R.id.buttonGallery) as Button
//        buttonNext = findViewById<View>(R.id.buttonNext) as Button
        mPreview = Preview(
            this,
            binding.cameraTextureView,
            binding.wide,
            binding.capture,
            binding.buttonNext
        )
        bookId = intent.getIntExtra("bookId", 0)
        Log.d("test", "bookId ${bookId}를 받았습니다.")
        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)
    }

    override fun onResume() {
        super.onResume()
        mPreview!!.onResume()
        binding.buttonGalleryy!!.setOnClickListener {
            openGallery()
        }
        binding.buttonNext!!.setOnClickListener {
            selectedUri = mPreview!!.getUri()
            if (selectedUri == null) {
                Log.d("test", "no selected picture")
            } else {
                goNext(selectedUri)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mPreview!!.onPause()
    }

    companion object {
        private const val TAG = "CAMERAACTIVITY"
        const val REQUEST_CAMERA = 1
        const val PERM_STORAGE = 99
        const val REQ_STORAGE = 102
    }


    fun goNext(uri: Uri?) {
//        Log.d("test", "${selectedUri}")
//        val ocrIntent = Intent(this, MainActivity::class.java)
//        ocrIntent.putExtra("val", uri)
//        startActivity(ocrIntent)
        finish()
    }

    fun openGallery() {
        mPreview!!.onPause()

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQ_STORAGE -> {
                data?.data?.let { uri ->
                    Log.d("test", "${uri}")
                    goNext(uri)
                }
            }
        }
    }
}