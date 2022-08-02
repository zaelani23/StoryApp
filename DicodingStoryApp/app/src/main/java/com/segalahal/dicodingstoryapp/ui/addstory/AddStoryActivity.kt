package com.segalahal.dicodingstoryapp.ui.addstory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.segalahal.dicodingstoryapp.R
import com.segalahal.dicodingstoryapp.databinding.ActivityAddStoryBinding
import com.segalahal.dicodingstoryapp.ui.story.StoryActivity
import com.segalahal.dicodingstoryapp.utils.createCustomTempFile
import com.segalahal.dicodingstoryapp.utils.reduceFileImage
import com.segalahal.dicodingstoryapp.utils.uriToFile
import com.segalahal.dicodingstoryapp.viewmodel.ViewModelFactory
import com.segalahal.dicodingstoryapp.views.LoadingDialog
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AddStoryViewModel
    private var bearer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.title_add_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val loadingDialog = LoadingDialog(this)

        val preferences = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        bearer = "Bearer " + preferences.getString("token", "").toString()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]

        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGalery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener {
            markButtonDisable(binding.btnUpload, false)
            if (getFile != null) {
                val desc = binding.inputDesc.text.toString()
                if (desc.isNotEmpty()) {
                    val file = reduceFileImage(getFile as File)
                    val description = desc.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    loadingDialog.showDialog()
                    viewModel.uploadStory(bearer, imageMultipart, description).observe(this){
                        if (it.error){
                            loadingDialog.hideDialog()
                            markButtonDisable(binding.btnUpload, true)
                            Toast.makeText(
                                this@AddStoryActivity,
                                it.message,
                                Toast.LENGTH_SHORT).show()
                        }else{
                            loadingDialog.hideDialog()
                            markButtonDisable(binding.btnUpload, true)
                            Toast.makeText(
                                this@AddStoryActivity,
                                "Upload story berhasil!",
                                Toast.LENGTH_SHORT).show()
                            val i = Intent(this, StoryActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(i)
                            finish()
                        }
                    }
                }else{
                    markButtonDisable(binding.btnUpload, true)
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Silakan masukkan deskripsi terlebih dahulu.",
                        Toast.LENGTH_SHORT).show()
                }
            } else {
                markButtonDisable(binding.btnUpload, true)
                Toast.makeText(
                    this@AddStoryActivity,
                    "Silakan masukkan berkas gambar terlebih dahulu.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun markButtonDisable(button: Button, state: Boolean) {
        button.isEnabled = state
        if (state){
            button.background = ContextCompat.getDrawable(applicationContext, R.drawable.round_bg)
        }else {
            button.background = ContextCompat.getDrawable(applicationContext, R.drawable.round_bg_disable)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.segalahal.dicodingstoryapp.ui.addstory",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result =  BitmapFactory.decodeFile(myFile.path)
            Glide.with(this)
                .load(result)
                .into(binding.tvImage)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            Glide.with(this)
                .load(myFile)
                .into(binding.tvImage)
        }
    }

    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}