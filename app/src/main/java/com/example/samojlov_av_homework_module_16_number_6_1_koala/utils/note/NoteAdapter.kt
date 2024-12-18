package com.example.samojlov_av_homework_module_16_number_6_1_koala.utils.note

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_16_number_6_1_koala.R
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.CategoriesOfNotes
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Note


class NoteAdapter(private val context: Context) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val notes = ArrayList<Note>()

    private var onNoteClickListener: OnNoteClickListener? = null
    private var onCheckBoxClickListener: OnCheckBoxClickListener? = null

    interface OnNoteClickListener {
        fun onNoteClick(note: Note, position: Int)
    }

    interface OnCheckBoxClickListener {
        fun onCheckBoxClick(note: Note)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Note>) {
        notes.clear()
        notes.addAll(newList)
        notifyDataSetChanged()
    }


    @OptIn(ExperimentalStdlibApi::class)
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.iconNotesItemIV)
        private val text: TextView = itemView.findViewById(R.id.noteTextItemTV)
        private val time: TextView = itemView.findViewById(R.id.timeTV)
        private val complite: CheckBox = itemView.findViewById(R.id.noteCompletedCB)

        fun bind(note: Note) {
            var image = 0
            for (categories in CategoriesOfNotes.listOfCategoriesNotes) {
                if (note.categoriesOfNotes == categories.categories) {
                    image = categories.icon
                }
            }
            icon.setImageResource(image)
            text.text = note.text
            time.text = note.time



            complite.setOnCheckedChangeListener { _, isChecked ->
                note.complited = isChecked

                if (onCheckBoxClickListener != null) {
                    onCheckBoxClickListener!!.onCheckBoxClick(note)
                }
            }

            checkFlag(note, complite, text)

        }

    }

    private fun checkFlag(note: Note, complited: CheckBox, text: TextView) {
        if (note.complited) {
            text.setTextColor(Color.parseColor("#808080"))
            text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            complited.setChecked(true)
        } else {
            text.setTextColor(Color.parseColor("#FF000000"))
            text.paintFlags = text.paintFlags and (Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NoteViewHolder {
        val viewHolder = NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        )
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.bind(currentNote)

        holder.itemView.setOnClickListener {
            if (onNoteClickListener != null) {
                onNoteClickListener!!.onNoteClick(currentNote, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun setOnNoteClickListener(onNoteClickListener: OnNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener
    }

    fun setOnCheckBoxClickListener(onCheckBoxClickListener: OnCheckBoxClickListener) {
        this.onCheckBoxClickListener = onCheckBoxClickListener
    }

}