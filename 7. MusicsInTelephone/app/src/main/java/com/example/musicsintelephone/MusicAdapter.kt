package com.example.musicsintelephone

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicsintelephone.databinding.MusicItemLayoutBinding
import java.io.ByteArrayInputStream


class MusicAdapter : ListAdapter<Music, MusicAdapter.MusicViewHolder>(
    object : DiffUtil.ItemCallback<Music>() {
        override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean =
            oldItem == newItem
    }) {

    var onMusicPlayerClick: ((Music) -> (Unit))? = null
    var musicOnPause: ((Boolean) -> (Unit))? = null

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = MusicItemLayoutBinding.bind(itemView)

        fun bind(item: Music) {
            binding.apply {
                name.text = item.title
                artist.text = item.artist
                if (item.imageBitMap != null)
                    musicImage.setImageBitmap(item.imageBitMap)
                else
                    musicImage.setImageResource(R.drawable.ic_default_image_music)

                if (item.isPlaying) {
                    playBtn.visibility = GONE
                    playingLottie.visibility = VISIBLE
                    musicOnPause?.invoke(
                        if (item.isOnPause) {
                            playingLottie.pauseAnimation()
                            true
                        } else {
                            playingLottie.playAnimation()
                            false
                        }
                    )
                } else {
                    playBtn.visibility = VISIBLE
                    playingLottie.visibility = GONE
                    playingLottie.cancelAnimation()
                }
            }
        }

        init {
            binding.playBtn.setOnClickListener {
                onMusicPlayerClick?.invoke(getItem(adapterPosition))
            }
            binding.playingLottie.setOnClickListener {
                onMusicPlayerClick?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder =
        MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.music_item_layout,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}