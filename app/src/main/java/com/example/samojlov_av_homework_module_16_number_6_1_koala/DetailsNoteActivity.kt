package com.example.samojlov_av_homework_module_16_number_6_1_koala

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.samojlov_av_homework_module_16_number_6_1_koala.databinding.ActivityDetailsNoteBinding
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.CategoriesOfNotes
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Note
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Profile
import com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.note.NoteListViewModel
import com.google.gson.Gson
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
class DetailsNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsNoteBinding
    private lateinit var categoriesSP: Spinner
    private lateinit var textNoteET: EditText
    private lateinit var saveNoteBT: Button
    private lateinit var deleteNoteBT: Button
    private lateinit var viewModel: NoteListViewModel

    private var adapterCategories: ArrayAdapter<String>? = null
    private var currentCategories = ""
    private var currentProfile: Profile? = null
    private var currentNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
//        setContentView(R.layout.activity_details_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {

        viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NoteListViewModel::class.java]



        categoriesSP = binding.categoriesSP
        textNoteET = binding.textNoteET
        saveNoteBT = binding.createNoteButtom.newNewDeleteAllButtomItemBT
        deleteNoteBT = binding.createNoteButtom.deleteNewDeleteAllButtomItemBT

        saveNoteBT.text = getString(R.string.profile_enter_alter_dialog_button_saveBT_Text)
        deleteNoteBT.text = getString(R.string.deleteNoteButtom)

        currentCategories = CategoriesOfNotes.listOfCategoriesNotes[0].categories

        getData()

        if (currentNote != null) {
            textNoteET.setText(currentNote!!.text)
        }


        spinnerInit()

        saveNoteBT.setOnClickListener {
            if (textNoteET.text.isEmpty()) return@setOnClickListener
            val text = textNoteET.text.toString()
            val time = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .format(DateTimeFormatter.ofPattern("HH.mm.ss\ndd.MM.yy"))
            var complited = false
            var id = 0
            if (currentNote != null) {
                complited = currentNote!!.complited
                id = currentNote!!.id
            }
            if (currentProfile != null) {
                val note = Note(currentProfile!!.id, currentCategories, text, time, complited, id)
                val intent = Intent()
                val type = typeOf<Note>().javaType
                val gsonNote = Gson().toJson(note, type)
                intent.putExtra("noteBack", gsonNote)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        deleteNoteBT.setOnLongClickListener {
            val intent = Intent()
            val type = typeOf<Note>().javaType
            val gsonNote = Gson().toJson(currentNote, type)
            intent.putExtra("delete", gsonNote)
            setResult(RESULT_OK, intent)
            finish()
            false
        }
    }

    private fun getData() {
        val profileString = intent.getStringExtra("profile")
        val type = typeOf<Profile>().javaType
        currentProfile = Gson().fromJson(profileString, type)
        val noteString = intent.getStringExtra("note")
        val typeNote = typeOf<Note>().javaType
        currentNote = Gson().fromJson(noteString, typeNote)

    }

    private fun spinnerInit() {
        val categoriesList = mutableListOf<String>()
        for (categories in CategoriesOfNotes.listOfCategoriesNotes) {
            categoriesList.add(categories.categories)
        }
        adapterCategories = ArrayAdapter(this, R.layout.multiline_spinner_item_post, categoriesList)
        adapterCategories!!.setDropDownViewResource(R.layout.multiline_spinner_item_post)
        categoriesSP.adapter = adapterCategories

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val item: String = parent?.getItemAtPosition(position) as String
                    currentCategories = item
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        categoriesSP.onItemSelectedListener = itemSelectedListener
    }
}