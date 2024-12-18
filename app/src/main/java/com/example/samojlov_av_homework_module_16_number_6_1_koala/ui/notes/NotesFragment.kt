package com.example.samojlov_av_homework_module_16_number_6_1_koala.ui.notes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_16_number_6_1_koala.DetailsNoteActivity
import com.example.samojlov_av_homework_module_16_number_6_1_koala.MainActivity
import com.example.samojlov_av_homework_module_16_number_6_1_koala.R
import com.example.samojlov_av_homework_module_16_number_6_1_koala.databinding.FragmentNotesBinding
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Note
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Profile
import com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.note.NoteAdapter
import com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.note.NoteListViewModel
import com.google.gson.Gson
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private lateinit var profileImageItemIV: ImageView
    private lateinit var loginProfileItemTV: TextView
    private lateinit var new_new_delete_all_buttom_itemBT: Button
    private lateinit var delete_new_delete_all_buttom_itemBT: Button
    private lateinit var listOfNotesRV: RecyclerView
    private lateinit var profile_itemCV: CardView
    private lateinit var button_itemCV: CardView
    private lateinit var viewModel: NoteListViewModel
    private lateinit var notesViewModel: NotesViewModel

    private var currentProfile: Profile? = null
    private var loginProfile = false
    private var listOfNote: List<Note>? = null
    private var listFilter = mutableListOf<Note>()
    private var adapter: NoteAdapter? = null
    private var newNote: Note? = null
    private var newNoteCheck = true

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        lifeDataNote()
        getListNote()
    }

    private fun init() {


        viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteListViewModel::class.java]

        notesViewModel =
            ViewModelProvider(this)[NotesViewModel::class.java]

        notesViewModel._currentProfile.observe(viewLifecycleOwner) {
            currentProfile = it
        }
        notesViewModel._loginProfile.observe(viewLifecycleOwner) {
            loginProfile = it
        }

        profileImageItemIV = binding.noteProfileItem.profileImageItemIV
        loginProfileItemTV = binding.noteProfileItem.loginProfileItemTV
        profile_itemCV = binding.noteProfileItem.profileItemCV
        button_itemCV = binding.buttomNotesItem.buttonItemCV
        new_new_delete_all_buttom_itemBT = binding.buttomNotesItem.newNewDeleteAllButtomItemBT
        delete_new_delete_all_buttom_itemBT = binding.buttomNotesItem.deleteNewDeleteAllButtomItemBT
        listOfNotesRV = binding.listOfNotesRV

        initAdapter()

        new_new_delete_all_buttom_itemBT.setOnClickListener {
            createNote(null)
        }

        delete_new_delete_all_buttom_itemBT.setOnLongClickListener {
            deleteAllNote()
            false
        }

    }

    private fun deleteAllNote() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val alert = dialogBuilder.create()
        val dialogValues = inflater.inflate(R.layout.delete_profile_alter_dialog, null)
        alert.setView(dialogValues)
        dialogValues.setBackgroundColor(Color.parseColor("#FFFFFF"))
        alert.show()

        val message = dialogValues.findViewById<TextView>(R.id.delete_messageTV)
        val cancel =
            dialogValues.findViewById<Button>(R.id.delete_profile_alter_dialog_button_cancelBT)
        val delete =
            dialogValues.findViewById<Button>(R.id.delete_profile_enter_alter_dialog_button_deleteBT)

        message.text =
            getString(R.string.delete_all_note_message, currentProfile!!.login)

        cancel.setOnClickListener {
            alert.cancel()
        }

        delete.setOnClickListener {
            for (note in listFilter) {
                viewModel.deleteNote(note)
            }
            alert.cancel()
            Toast.makeText(
                context,
                getString(R.string.deleteAllProfile_Toast),
                Toast.LENGTH_LONG
            ).show()
            getListNote()
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
        checkLoginProfile(loginProfile)
        profileItem()

    }

    private fun createNote(note: Note?) {
        val intent = Intent(requireContext(), DetailsNoteActivity::class.java)
        val type = typeOf<Profile?>().javaType
        val gson = Gson().toJson(currentProfile, type)
        intent.putExtra("profile", gson)
        val typeOne = typeOf<Note?>().javaType
        val gsonNote = Gson().toJson(note, typeOne)
        intent.putExtra("note", gsonNote)
        launchDetailsNoteActivity.launch(intent)
    }

    private fun lifeDataNote() {

        viewModel.notes.observe(viewLifecycleOwner) { list ->
            list?.let {
                listOfNote = it
                getListNote()
            }
            getData()
            checkLoginProfile(loginProfile)
        }
    }

    private fun getListNote(): Unit? {
        listFilter.clear()
        if (loginProfile) {
            for (note in listOfNote!!) {
                if (note.idProfile == currentProfile?.id) {
                    listFilter.add(note)
                }
            }
        }
        return adapter?.updateList(listFilter.toList())
    }

    private fun getData() {
        notesViewModel._currentProfile.value = ((activity as MainActivity).getDataProfile()
            .also { notesViewModel.currentProfile = it })
        notesViewModel._loginProfile.value =
            ((activity as MainActivity).getCheckLogin().also { notesViewModel.loginProfile = it })
    }

    private fun initAdapter() {
        val gridLayoutManager = GridLayoutManager(requireActivity().applicationContext, 2)
        listOfNotesRV.setLayoutManager(gridLayoutManager)
        adapter = NoteAdapter(requireContext().applicationContext)
        listOfNotesRV.adapter = adapter

        adapter!!.setOnNoteClickListener(object : NoteAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note, position: Int) {
                newNoteCheck = false
                createNote(note)
            }
        })

        adapter!!.setOnCheckBoxClickListener(object :NoteAdapter.OnCheckBoxClickListener{
            override fun onCheckBoxClick(note: Note) {
                viewModel.updateNote(note)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkLoginProfile(check: Boolean) {
        if (check) {
            profile_itemCV.visibility = View.VISIBLE
            button_itemCV.visibility = View.VISIBLE
        } else {
            profile_itemCV.visibility = View.INVISIBLE
            button_itemCV.visibility = View.INVISIBLE
        }
    }

    private val launchDetailsNoteActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val note = data!!.getStringExtra("noteBack").toString()
                val noteDelete = data.getStringExtra("delete").toString()
                val type = typeOf<Note?>().javaType


                if (note != "null"){
                    newNote = Gson().fromJson(note, type)
                    if (newNoteCheck){
                        viewModel.insertNote(newNote!!)
                    }else {
                        loginProfile = true
                        viewModel.updateNote(newNote!!)
                    }
                }else{
                    newNote = Gson().fromJson(noteDelete, type)
                    viewModel.deleteNote(newNote!!)
                }

                newNoteCheck = true
                getListNote()
            }
        }

    private fun profileItem() {
        profileImageItemIV.setImageURI(currentProfile?.icon?.toUri())
        loginProfileItemTV.text = currentProfile?.login
    }
}