package com.ssafy.herehear.feature.home.readmode.audiobook

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.*
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.work.*
import com.ssafy.herehear.BaseActivity
import com.ssafy.herehear.CustomApplication
import com.ssafy.herehear.MainActivity
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityCamera2Binding

import com.ssafy.herehear.worker.DownloadWorker
import com.ssafy.herehear.worker.UploadWorker
import dagger.hilt.android.AndroidEntryPoint

import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat

@AndroidEntryPoint
class Camera2Activity : BaseActivity() {
    val PERM_STORAGE = 99 // 외부 저장소 권한 처리
    val PERM_CAMERA = 100 // 카메라 권한 처리
    val REQ_CAMERA = 101 // 카메라 촬영 요청

    val binding by lazy { ActivityCamera2Binding.inflate(layoutInflater) }
    var realUri:Uri? = null
    var realPath: String? = null
    var downloadId: Long = 0L
    lateinit var uploadWorkRequest: OneTimeWorkRequest
    lateinit var downloadWorkRequest: OneTimeWorkRequest
    private lateinit var downloadManager: DownloadManager
    private val cameraViewModel: CameraViewModel by viewModels()

    val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            realUri?.let { uri ->
                val bitmap = loadBitmap(uri)
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

        binding.cameraNextButton.setOnClickListener {
            if (realPath != null) {
                showPopup()
            } else {
                Toast.makeText(applicationContext, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
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
        val cursor = CustomApplication.context().contentResolver.query(uri, null, null, null, null)
        cursor?.moveToNext()
        val path = cursor?.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        cursor?.close()
        return path
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
    }

    private fun showPopup() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup, null)
        val mList = arrayOf<String>("상용 버전", "베타 버전(준비중)")

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("버전을 선택해주세요.")
            .setItems(mList, DialogInterface.OnClickListener { dialogInterface, i ->
                binding.progressLayout.visibility = View.VISIBLE
//                val userId = CustomApplication.prefs.getString("userId", "")
//                lateinit var file: File
                try {
//                    file = File(realPath)
//                    Log.d("test", "오디오변환 요청 전 파일경로 : ${file.path}")
//                    var fileBody = FormDataUtil.getImageBody("imgs", file)
                    when (i) {
                        0 -> {
                            setWorkManager()
                            WorkManager.getInstance(applicationContext)
                                .beginWith(uploadWorkRequest)
                                .enqueue()
                        }
                        1 -> {
                            Toast.makeText(applicationContext, "준비중입니다...", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch(e: Exception) {
                    Toast.makeText(applicationContext, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
                }

            })
            .create()
        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun setWorkManager() {
        val inputData = Data.Builder()
            .putString("realPath", realPath)
            .putString("userId", CustomApplication.prefs.getString("userId", ""))
            .build()
        val inputData2 = Data.Builder()
            .putString("userId", CustomApplication.prefs.getString("userId", ""))
            .build()
        uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(inputData)
            .build()
        downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(inputData2)
            .build()
        WorkManager.getInstance(applicationContext)
            .getWorkInfoByIdLiveData(downloadWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                    // 서버에서 다운로드 하기. 이걸 viewmodel에서 하자.
                    cameraViewModel.downloadAudio()
//                    val file = File(applicationContext.getExternalFilesDir(
//                        Environment.DIRECTORY_MUSIC), "myAudio.wav")
//                    val url = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"
//                    val intentFilter = IntentFilter()
//                    intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
//                    this.registerReceiver(mReceiver, intentFilter)
//                    val downloadRequest = DownloadManager.Request(Uri.parse(url))
//                        .setTitle("오디오북 다운로드 중")
//                        .setDescription("오디오파일을 다운로드 중입니다.")
//                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//                        .setDestinationUri(Uri.fromFile(file))
//                        .setAllowedOverMetered(true)
//                        .setAllowedOverRoaming(true)
//                    downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//                    downloadId = downloadManager.enqueue(downloadRequest)
                }
            })
    }

    private val mReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent!!.action)) {
                if (downloadId == id) {
                    Log.d("test", "!!!!!!!!!")
                    val query: DownloadManager.Query = DownloadManager.Query()
                    query.setFilterById(id)
                    var cursor = downloadManager.query(query)
                    if (!cursor.moveToFirst()) {
                        return
                    }

                    var columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    var status = cursor.getInt(columnIndex)
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Log.d("test", "다운로드 성공")
                        Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show()

                        // file 데이터를 리턴해주어야 한다.
                        val file = File(applicationContext.getExternalFilesDir(
                            Environment.DIRECTORY_MUSIC), "myAudio.wav")

                        val mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(file.path)
                        mediaPlayer.prepare()
                        mediaPlayer.seekTo(0)
                        mediaPlayer.start()
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Log.d("test", "다운로드 실패")
                        Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.action)) {
                Toast.makeText(context, "Notification clicked", Toast.LENGTH_SHORT).show()
            }
        }

    }
}