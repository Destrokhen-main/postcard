package com.muroming.postcardeditor.ui.themedadapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.muroming.postcardeditor.R
import com.muroming.postcardeditor.data.dto.UriPicture
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_themed_pictures.view.*

class UserThemedPicturesAdapter(
    context: Context,
    private val onUriClicked: (Uri) -> Unit = {},
    private val onAllClicked: (String) -> Unit = {}
) : RecyclerView.Adapter<UserThemedPicturesAdapter.ViewHolder>() {

    private val items: MutableList<ThemedUserPictures> = mutableListOf()
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_themed_pictures, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(newItems: List<ThemedUserPictures>) {
        items.apply {
            clear()
            addAll(newItems)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(userPicture: ThemedUserPictures) {
            with(itemView as ViewGroup) {
                tvTitle.text = userPicture.title
                children.filterIsInstance<ImageView>().forEachIndexed { index, view ->
                    if (index >= userPicture.pictures.size) {
                        view.setImageBitmap(null)
                        view.setOnClickListener(null)
                    } else {
                        val photo = userPicture.pictures[index] as UriPicture
                        Picasso.get()
                            .load(photo.uri)
                            .into(view)
                        view.setOnClickListener { onUriClicked(photo.uri) }
                    }
                }
                tvAll.setOnClickListener { onAllClicked(userPicture.title) }
            }
        }
    }
}