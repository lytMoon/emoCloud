package com.lytredrock.emocloudmusic.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lytredrock.emocloudmusic.R
import com.lytredrock.emocloudmusic.SingerSong
import com.lytredrock.emocloudmusic.data.Artist


/**
 * description ： 热门歌手的适配器
 * author : 苟云东
 * email : 2191288460@qq.com
 * date : 2023/7/21 16:52
 */
class HotSingerAdapter(val data: List<Artist>, private val activity: FragmentActivity) :
    RecyclerView.Adapter<HotSingerAdapter.InnerHolder>() {

    private var clickInterface: ClickInterface? = null

    interface ClickInterface {
        fun onImageviewClick(view: View, position: Int)
    }

    fun setOnclick(clickInterface: ClickInterface) {
        this.clickInterface = clickInterface
    }

    inner class InnerHolder(root: View) : RecyclerView.ViewHolder(root) {
        val photo = root.findViewById<ImageView>(R.id.iv_photo)
        val name = root.findViewById<TextView>(R.id.tv_name)

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, SingerSong::class.java)
                intent.putExtra("photo", data[absoluteAdapterPosition].picUrl)
                intent.putExtra("id", data[absoluteAdapterPosition].id)
                intent.putExtra("name", data[absoluteAdapterPosition].name)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_hotsinger, parent, false)
        return InnerHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemData = data[position]
        holder.apply {
            Glide.with(activity).load(itemData.picUrl).into(photo)
            name.text = itemData.name

        }
    }


}