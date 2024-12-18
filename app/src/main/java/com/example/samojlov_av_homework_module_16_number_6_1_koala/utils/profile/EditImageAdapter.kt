package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_16_number_6_1_koala.R
import java.io.ByteArrayOutputStream

const val DEST_WIDTH = 200

class EditImageAdapter(private val list: List<String>) :
    RecyclerView.Adapter<EditImageAdapter.EditImageViewHolder>() {

    private var onEditImageClickListener: OnEditImageClickListener? = null

    interface OnEditImageClickListener {
        fun onEditImageClickListener(image: String, position: Int)
    }

    class EditImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.edit_image_alert_dialogIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.edit_image_alert_dialog_item, parent, false)
        return EditImageViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: EditImageViewHolder, position: Int) {
        val icon = list[position]
        val iconSmall = zipImageIcon(icon)
        holder.image.setImageBitmap(iconSmall)


        holder.itemView.setOnClickListener {
            if (onEditImageClickListener != null) {
                onEditImageClickListener!!.onEditImageClickListener(icon, position)
            }
        }
    }

    fun setIconClickListener(onEditImageClickListener: OnEditImageClickListener) {
        this.onEditImageClickListener = onEditImageClickListener
    }

    private fun zipImageIcon(image: String): Bitmap? {
        var imageOut: Bitmap? = null
        try {
            val b = BitmapFactory.decodeFile(image)
            val origWidth = b.width
            val origHeight = b.height
            if (origWidth > DEST_WIDTH) {
                val destHeight = origHeight / (origWidth / DEST_WIDTH)
                val b2 = Bitmap.createScaledBitmap(b, DEST_WIDTH, destHeight, false)
                val outStream = ByteArrayOutputStream()
                b2.compress(Bitmap.CompressFormat.JPEG, 70, outStream)
                imageOut = b2
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imageOut
    }
}