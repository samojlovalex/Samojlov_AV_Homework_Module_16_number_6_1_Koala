package com.example.samojlov_av_homework_module_16_number_6_1_koala.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_16_number_6_1_koala.ImageGalleryMainActivity
import com.example.samojlov_av_homework_module_16_number_6_1_koala.MainActivity
import com.example.samojlov_av_homework_module_16_number_6_1_koala.R
import com.example.samojlov_av_homework_module_16_number_6_1_koala.databinding.FragmentProfileBinding
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Profile
import com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.profile.ProfileAdapter
import com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.profile.ProfileListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlin.reflect.javaType
import kotlin.reflect.typeOf


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var listOfUsersRV: RecyclerView
    private lateinit var newProfileButtonBT: Button
    private lateinit var deleteProfileButtonBT: Button
    private lateinit var viewModel: ProfileListViewModel

    private var editImage: ImageView? = null
    private var adapter: ProfileAdapter? = null
    private var listOfProfiles: List<Profile>? = null
    private val binding get() = _binding!!
    private var icon: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
        if (ActivityCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.READ_MEDIA_IMAGES
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                context,
                getString(R.string.read_media_images_permission_toast), Toast.LENGTH_LONG
            ).show()
            (activity as MainActivity).permissionOfReadImage.launch(permission)

        } else {

        }
        init()
        lifeDataProfile()
    }

    private fun init() {

        viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ProfileListViewModel::class.java]

        newProfileButtonBT = binding.profileButtomItem.newNewDeleteAllButtomItemBT
        deleteProfileButtonBT = binding.profileButtomItem.deleteNewDeleteAllButtomItemBT
        listOfUsersRV = binding.listOfUsersRV

        listOfUsersRV.layoutManager = LinearLayoutManager(context)

        newProfileButtonBT.setOnClickListener {
            initProfile()
        }

        initAdapter()

        deleteProfileButtonBT.setOnLongClickListener {
            deleteAllProfile()
            false
        }
    }

    private fun initAdapter() {
        adapter = ProfileAdapter(requireContext().applicationContext)
        listOfUsersRV.adapter = adapter
        adapter!!.setOnProfileClickListener(object : ProfileAdapter.OnProfileClickListener {
            override fun onProfileClick(profile: Profile, position: Int) {
                enterProfile(profile)
            }

        })
    }

    private fun enterProfile(profile: Profile) {
        if (profile.selected) {
            editProfile(profile)
        } else {
            loginProfile(profile)
        }
    }

    private fun loginProfile(profile: Profile) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val alert = dialogBuilder.create()
        val dialogValues = inflater.inflate(R.layout.profile_enter_alter_dialog, null)
        alert.setView(dialogValues)
        dialogValues.setBackgroundColor(Color.parseColor("#FFFFFF"))
        alert.show()

        editImage = dialogValues.findViewById(R.id.profile_enter_alter_dialog_imageIV)
        val login = dialogValues.findViewById<TextView>(R.id.profile_enter_alter_dialog_loginTV)
        val password =
            dialogValues.findViewById<EditText>(R.id.profile_enter_alter_dialog_passwordET)
        val cancel = dialogValues.findViewById<Button>(R.id.profile_alter_dialog_button_cancelBT)
        val enter = dialogValues.findViewById<Button>(R.id.profile_enter_alter_dialog_button_saveBT)

        if (profile.icon != null) {
            editImage?.setImageURI(profile.icon!!.toUri())
        } else {
            editImage?.setImageResource(R.drawable.profile_icon_base)
        }

        login.text = profile.login

        cancel.setOnClickListener {
            alert.cancel()
        }

        enter.setOnClickListener {
            if (password.text.toString().trim() != profile.password) return@setOnClickListener
            profile.selected = true
            viewModel.updateProfile(profile)

            if (listOfProfiles != null) {
                for (otherProfile in listOfProfiles!!) {
                    if (otherProfile != profile) {
                        otherProfile.selected = false
                        viewModel.updateProfile(otherProfile)
                    }
                }
            }

            alert.cancel()
        }
    }

    private fun editProfile(profile: Profile) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val alert = dialogBuilder.create()
        val dialogValues = inflater.inflate(R.layout.profile_edit_alter_dialog, null)
        alert.setView(dialogValues)
        dialogValues.setBackgroundColor(Color.parseColor("#FFFFFF"))
        alert.show()

        editImage = dialogValues.findViewById(R.id.profile_edit_alter_dialog_imageIV)
        val login = dialogValues.findViewById<EditText>(R.id.profile_edit_alter_dialog_loginET)
        val password =
            dialogValues.findViewById<EditText>(R.id.profile_edit_alter_dialog_passwordET)
        val cansel =
            dialogValues.findViewById<Button>(R.id.profile_edit_alter_dialog_button_cancelBT)
        val save = dialogValues.findViewById<Button>(R.id.profile_edit_alter_dialog_button_saveBT)
        val exit = dialogValues.findViewById<Button>(R.id.profile_edit_alter_dialog_button_exitBT)
        val delete =
            dialogValues.findViewById<Button>(R.id.profile_edit_alter_dialog_button_deleteBT)
        val clearIcon = dialogValues.findViewById<FloatingActionButton>(R.id.clearIconBT)

        if (profile.icon != null) {
            editImage?.setImageURI(profile.icon!!.toUri())
        } else {
            editImage?.setImageResource(R.drawable.profile_icon_base)
        }

        login.setText(profile.login)
        password.setText(profile.password)

        editImage?.setOnClickListener {
            getImageForIcon()
        }

        cansel.setOnClickListener {
            alert.cancel()
        }

        save.setOnClickListener {
            if (login.text.isEmpty() || password.text.isEmpty()) return@setOnClickListener
            profile.icon = icon
            profile.login = login.text.toString().trim()
            profile.password = password.text.toString().trim()
            viewModel.updateProfile(profile)
            alert.cancel()
        }

        exit.setOnClickListener {
            profile.selected = false
            viewModel.updateProfile(profile)
            alert.cancel()
        }

        delete.setOnLongClickListener {
            deleteOneProfile(alert, profile)
            false
        }

        clearIcon.setOnLongClickListener {
            clearIconProfile(profile)
            false
        }
    }

    private fun deleteAllProfile() {
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
            getString(R.string.delete_all_profiles_message_Text)

        cancel.setOnClickListener {
            alert.cancel()
        }

        delete.setOnClickListener {
            viewModel.deleteAllProfile()
            alert.cancel()
            Toast.makeText(
                context,
                getString(R.string.deleteAllProfile_Toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun initProfile() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val alert = dialogBuilder.create()
        val dialogValues = inflater.inflate(R.layout.profile_alter_dialog, null)
        alert.setView(dialogValues)

        editImage = dialogValues?.findViewById(R.id.profile_alter_dialog_imageIV)
        val login = dialogValues?.findViewById<EditText>(R.id.profile_alter_dialog_loginET)
        val password = dialogValues?.findViewById<EditText>(R.id.profile_alter_dialog_passwordET)
        val cancel = dialogValues?.findViewById<Button>(R.id.profile_alter_dialog_button_cancelBT)
        val save = dialogValues?.findViewById<Button>(R.id.profile_alter_dialog_button_saveBT)

        dialogValues.setBackgroundColor(Color.parseColor("#FFFFFF"))
        alert.show()


        editImage?.setOnClickListener {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                TODO("VERSION.SDK_INT < TIRAMISU")
            }
            if (ActivityCompat.checkSelfPermission(
                    activity?.applicationContext!!,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    context,
                    getString(R.string.read_media_images_permission_toast), Toast.LENGTH_LONG
                ).show()
                (activity as MainActivity).permissionOfReadImage.launch(permission)

            } else {
                getImageForIcon()
            }
        }

        cancel?.setOnClickListener {
            alert.cancel()
        }


        save?.setOnClickListener {
            if (login!!.text.isEmpty() || password!!.text.isEmpty()) return@setOnClickListener
            viewModel.insertProfile(
                Profile(
                    icon,
                    login.text.toString().trim(),
                    password.text.toString().trim(),
                    false
                )
            )
            alert.cancel()
            deleteProfileButtonBT.visibility = View.VISIBLE
        }
    }

    private fun getImageForIcon() {
        val intent = Intent(requireContext(), ImageGalleryMainActivity::class.java)
        launchImageGalleryMainActivity.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun lifeDataProfile() {
        viewModel.profiles.observe(viewLifecycleOwner) { list ->
            list?.let {
                listOfProfiles = it
                adapter?.updateList(it)
                profileLoginIn(listOfProfiles!!)
            }
        }
    }

    private fun clearIconProfile(profile: Profile) {
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
            getString(R.string.clearIcon_profile_alter_dialog_message_Text, profile.login)

        cancel.setOnClickListener {
            alert.cancel()
        }

        delete.setOnClickListener {
            profile.icon = null
            editImage?.setImageResource(R.drawable.profile_icon_base)
            viewModel.updateProfile(profile)
            alert.cancel()
        }
    }

    private fun deleteOneProfile(alertOut: AlertDialog, profile: Profile) {
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
            getString(R.string.delete_profile_alter_dialog_message_Text, profile.login)

        cancel.setOnClickListener {
            alert.cancel()
        }

        delete.setOnClickListener {
            viewModel.deleteProfile(profile)
            alert.cancel()
            alertOut.cancel()
            Toast.makeText(
                context,
                getString(R.string.delete_profile_alert_dialog_Toast, profile.login),
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun profileLoginIn(list: List<Profile>) {
        var profileLogin: Profile? = null
        for (profile in list) {
            if (profile.selected) {
                profileLogin = profile
                (activity as MainActivity).setProfile(profileLogin)
                (activity as MainActivity).setCheckLogin(true)
            }
        }
        if (profileLogin == null) (activity as MainActivity).setCheckLogin(false)
    }

    private val launchImageGalleryMainActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val uri = data!!.getStringExtra("uri").toString()
                icon = uri
                editImage?.setImageURI(icon?.toUri())
            }
        }
}