package com.example.samojlov_av_homework_module_16_number_6_1_koala

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_16_number_6_1_koala.databinding.ActivityImageGalleryMainBinding
import com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.profile.EditImageAdapter

class ImageGalleryMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageGalleryMainBinding
    private lateinit var editImageRV: RecyclerView

    private val listOfImages = mutableListOf<String>()
    private var adapter: EditImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImageGalleryMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
//        setContentView(R.layout.activity_image_gallery_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllImages()
        init()
    }

    private fun init() {
        editImageRV = binding.editImageRV
        getImage(listOfImages)
    }

    private fun getImage(listOfImages: MutableList<String>) {
        val gridLayoutManager = GridLayoutManager(applicationContext, 2)
        editImageRV.setLayoutManager(gridLayoutManager)
        adapter = EditImageAdapter(listOfImages.toList())
        editImageRV.adapter = adapter

        adapter?.setIconClickListener(object : EditImageAdapter.OnEditImageClickListener {
            override fun onEditImageClickListener(image: String, position: Int) {
                val intent = Intent()
                intent.putExtra("uri",image)
                setResult(RESULT_OK,intent)
                finish()
            }
        })
    }

    @SuppressLint("Recycle")
    private fun getAllImages() {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var imagePatch: String
        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndexData = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor!!.moveToNext()) {
            imagePatch = cursor.getString(columnIndexData!!)
            listOfImages.add(imagePatch)
        }
    }
}