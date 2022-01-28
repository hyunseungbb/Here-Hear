package com.ssafy.herehear.feature.home.readmode.audiobook

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
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
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.work.*
import com.ssafy.herehear.BaseActivity
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.MainActivity
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityCamera2Binding
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.RetrofitClientAI
import com.ssafy.herehear.model.network.response.OCRTTSResponse
import com.ssafy.herehear.model.network.response.UpdateBookStatusRequest
import com.ssafy.herehear.model.network.response.UpdateBookStatusResponse
import com.ssafy.herehear.util.FormDataUtil
import com.ssafy.herehear.worker.DownloadWorker
import com.ssafy.herehear.worker.UploadWorker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import kotlin.coroutines.CoroutineContext

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
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
//        binding.progressFrameLayout.visibility = View.INVISIBLE
        binding.cameraNextButton.setOnClickListener {

            showPopup()
//            binding.progressLayout.visibility = View.VISIBLE
//            val userId = HereHear.prefs.getString("userId", "")
//            val url = "ocr_tts/${userId}/"
//            var file = File(realPath)
//            Log.d("test", "오디오변환 요청 전 파일경로 : ${file.path}")
//            var fileBody = FormDataUtil.getImageBody("imgs", file)
//            RetrofitClientAI.api.downloadAudio(url, fileBody).enqueue(object: Callback<OCRTTSResponse> {
//                override fun onResponse(
//                    call: Call<OCRTTSResponse>,
//                    response: Response<OCRTTSResponse>
//                ) {
//                    binding.progressLayout.visibility = View.INVISIBLE
//                    if (response.isSuccessful) {
//                        goAudioPlayActivity()
//                    } else {
//                        Toast.makeText(applicationContext, "오디오북 요청 실패! ${response.code()}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<OCRTTSResponse>, t: Throwable) {
//                    binding.progressLayout.visibility = View.INVISIBLE
//                    t.printStackTrace()
//                    Toast.makeText(applicationContext, "오디오북 요청 실패", Toast.LENGTH_SHORT).show()
//                }
//            })
        }
    }
    fun goAudioPlayActivity() {
        // jpg 파일 혹은 파일 uri랑 bookId를 넘겨줘야함
        val bookId = intent.getIntExtra("bookId", 0)
        val libraryId = intent.getIntExtra("libraryId", 0)
        val playIntent = Intent(this, AudioPlayActivity::class.java)
        playIntent.putExtra("bookId", bookId)
        playIntent.putExtra("libraryId", libraryId)
        playIntent.putExtra("path", realPath)
        val returnIntent = Intent()
        setResult(RESULT_CANCELED, returnIntent)
        startActivity(playIntent)
        finish()
//        try {
//            startActivity(playIntent)
//            finish()
//        } catch (e: Exception) {
//            Log.d("err", "${e}")
//        }
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

    private fun showPopup() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup, null)
        val mList = arrayOf<String>("상용 버전", "베타 버전")
        val inputData = Data.Builder()
            .putString("realPath", realPath)
            .putString("userId", HereHear.prefs.getString("userId", ""))
            .build()
        val uploadWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(inputData)
            .build()
        val downloadWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .build()
        WorkManager.getInstance(applicationContext)
            .getWorkInfoByIdLiveData(downloadWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                    // 여기서 UI 변경이 가능하다.
                    Log.d("test", "work 완료!!!")
                }
            })
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("버전을 선택해주세요.")
            .setItems(mList, DialogInterface.OnClickListener { dialogInterface, i ->
                binding.progressLayout.visibility = View.VISIBLE
                val userId = HereHear.prefs.getString("userId", "")
                lateinit var file: File
                try {
                    file = File(realPath)
                    Log.d("test", "오디오변환 요청 전 파일경로 : ${file.path}")
                    var fileBody = FormDataUtil.getImageBody("imgs", file)
                    when (i) {
                        0 -> {
                            WorkManager.getInstance(applicationContext)
                                .beginWith(uploadWorkRequest)
                                .then(downloadWorkRequest)
                                .enqueue()
//                            val url = "ocr_tts/${userId}/"
//                            RetrofitClientAI.api.downloadAudio(url, fileBody).enqueue(object: Callback<OCRTTSResponse> {
//                                override fun onResponse(
//                                    call: Call<OCRTTSResponse>,
//                                    response: Response<OCRTTSResponse>
//                                ) {
//                                    binding.progressLayout.visibility = View.INVISIBLE
//                                    if (response.isSuccessful) {
//                                        goAudioPlayActivity()
//                                    } else {
//                                        Toast.makeText(applicationContext, "오디오북 요청 실패! ${response.code()}", Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<OCRTTSResponse>, t: Throwable) {
//                                    binding.progressLayout.visibility = View.INVISIBLE
//                                    t.printStackTrace()
//                                    Toast.makeText(applicationContext, "오디오북 요청 실패", Toast.LENGTH_SHORT).show()
//                                }
//                            })
                        }

                        1 -> {
                            val url = "easy_ocr_tts/${userId}/"
                            RetrofitClientAI.api.downloadAudio2(url, fileBody).enqueue(object: Callback<OCRTTSResponse> {
                                override fun onResponse(
                                    call: Call<OCRTTSResponse>,
                                    response: Response<OCRTTSResponse>
                                ) {
                                    binding.progressLayout.visibility = View.INVISIBLE
                                    if (response.isSuccessful) {
                                        goAudioPlayActivity()
                                    } else {
                                        Toast.makeText(applicationContext, "오디오북 요청 실패! ${response.code()}", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<OCRTTSResponse>, t: Throwable) {
                                    binding.progressLayout.visibility = View.INVISIBLE
                                    t.printStackTrace()
                                    Toast.makeText(applicationContext, "오디오북 요청 실패", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                } catch(e: Exception) {
                    Toast.makeText(applicationContext, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
                }

            })
//            .setPositiveButton("확인", null)
//            .setNeutralButton("취소", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }

}