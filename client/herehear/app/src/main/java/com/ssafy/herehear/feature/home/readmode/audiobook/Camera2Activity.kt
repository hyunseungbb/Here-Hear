package com.ssafy.herehear.feature.home.readmode.audiobook

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.hardware.Camera
import android.hardware.camera2.CameraDevice
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.ssafy.herehear.BaseActivity
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.databinding.ActivityCamera2Binding
import com.ssafy.herehear.model.network.RetrofitClientAI
import com.ssafy.herehear.model.network.response.OCRTTSResponse
import com.ssafy.herehear.util.FormDataUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat

class Camera2Activity : BaseActivity() {

    val PERM_STORAGE = 99 // 외부 저장소 권한 처리
    val PERM_CAMERA = 100 // 카메라 권한 처리
    val REQ_CAMERA = 101 // 카메라 촬영 요청

    val binding by lazy { ActivityCamera2Binding.inflate(layoutInflater) }
    var realUri:Uri? = null
    var realPath: String? = null

    val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("test", "dd${result.resultCode}")
        if (result.resultCode == RESULT_OK) {
            Log.d("test", "들어옴")

            realUri?.let { uri ->
                Log.d("test", "제발..${getRealPathFromURI(uri)}")
                Log.d("test", "카메라찍었을때${uri}")
                val bitmap = loadBitmap(uri)
                Log.d("test", "비트맵${loadBitmap(uri)}")
                binding.imagePreview.setImageBitmap(bitmap)
                realPath = getRealPathFromURI(uri)
                realUri = null
            }

        } else {
            Toast.makeText(this, "camera fail", Toast.LENGTH_LONG)
        }
    }

    val getResultGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
           result.data?.data?.let { uri ->
               Log.d("test", "제발..${getRealPathFromURI(uri)}")
               Log.d("test", "갤러리에서 ${uri}")
               realPath = getRealPathFromURI(uri)
               binding.imagePreview.setImageURI(uri)
           }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)
        binding.cameraBackButton.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }
        binding.cameraNextButton.setOnClickListener {
            // 여기서 ai서버에 ocr_tts 요청을 해야하는구나 그게 왜 안되어 있지??
            var file = File(realPath)
            Log.d("test", "오디오변환 요청 전 파일경로 : ${file.path}")
            var fileBody = FormDataUtil.getImageBody("imgs", file)
            RetrofitClientAI.api.downloadAudio(fileBody).enqueue(object: Callback<OCRTTSResponse> {
                override fun onResponse(
                    call: Call<OCRTTSResponse>,
                    response: Response<OCRTTSResponse>
                ) {
                    if (response.isSuccessful) {
                        goAudioPlayActivity()
                    } else {
                        Toast.makeText(applicationContext, "오디오북 요청 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OCRTTSResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "오디오북 요청 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    fun goAudioPlayActivity() {
        // jpg 파일 혹은 파일 uri랑 bookId를 넘겨줘야함
        val bookId = intent.getIntExtra("bookId", 0)
        val playIntent = Intent(this, AudioPlayActivity::class.java)
        playIntent.putExtra("bookId", bookId)
        playIntent.putExtra("path", realPath)
        try {
            startActivity(playIntent)
            finish()
        } catch (e: Exception) {
            Log.d("err", "${e}")
        }
    }
    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERM_STORAGE -> setViews()
            PERM_CAMERA -> openCamera()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERM_STORAGE -> {
                Toast.makeText(baseContext,
                    "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다.",
                    Toast.LENGTH_LONG).show()
                finish()
            }
            PERM_CAMERA -> {
                Toast.makeText(baseContext,
                    "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다.",
                    Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    fun setViews() {
        binding.buttonCamera.setOnClickListener {
            requirePermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        }
        binding.buttonGallery.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = MediaStore.Images.Media.CONTENT_TYPE
            getResultGallery.launch(galleryIntent)

        }
    }

    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageUri(newFileName(), "image/jpg")?.let { uri ->
            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            getResultText.launch(intent)
        }
    }

    fun newFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())

        return "$filename.jpg"
    }

    fun createImageUri(filename: String, mimeType: String): Uri? {
        // ContentValues 클래스를 통해 파일명과 파일 타입을 입력한 후 contentResolver의 insert()를 통해 저장
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    // Uri를 사용해서 미디어스토어에 저장된 이미지를 읽어오는 메서도
    fun loadBitmap(photoUri: Uri): Bitmap? {
        var image: Bitmap? = null
        try {
            image = if (Build.VERSION.SDK_INT > 27) {
                val source: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return image
    }

    @SuppressLint("Range")
    fun getRealPathFromURI(uri: Uri): String? {
        val cursor = HereHear.context().contentResolver.query(uri, null, null, null, null)
        cursor?.moveToNext()
        val path = cursor?.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        cursor?.close()
        return path
    }

}