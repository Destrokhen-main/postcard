package com.muroming.postcardeditor.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.muroming.postcardeditor.data.dto.DrawablePicture
import com.muroming.postcardeditor.data.dto.UriPicture
import com.muroming.postcardeditor.data.dto.UserPicture
import com.muroming.postcardeditor.utils.toBitmap
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user_medium_picture.view.*

class UserPicturesAdapter(
    context: Context,
    @LayoutRes private val pictureLayout: Int,
    private val onUriClicked: (Uri) -> Unit = {},
    private val onDrawableClicked: (Bitmap) -> Unit = {}
) : RecyclerView.Adapter<UserPicturesAdapter.ViewHolder>() {

    private val items: MutableList<UserPicture> = mutableListOf()
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(pictureLayout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(newItems: List<UserPicture>) {
        items.apply {
            clear()
            addAll(newItems)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(userPicture: UserPicture) {
            with(itemView) {
                if (userPicture is UriPicture) {
                    Picasso.get()
                        .load(userPicture.uri)
                        .into(ivUserPicture)
                    setOnClickListener { onUriClicked(userPicture.uri) }
                } else if (userPicture is DrawablePicture) {
                    val bitmap = userPicture.gradientDrawable.also {
                        userPicture.tint?.let(it::setColor)
                    }.toBitmap()
                    ivUserPicture.setImageBitmap(bitmap)
                    setOnClickListener { onDrawableClicked(bitmap) }
                }
            }
        }
    }
}