package com.example.musicsintelephone

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioMetadata
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicsintelephone.databinding.ActivityMainBinding
import java.io.ByteArrayInputStream
import java.util.*
import kotlin.collections.ArrayList

private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val listAdapter = MusicAdapter()

    private var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listOfMusic.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            listAdapter.apply {
                if (checkPermissionREAD_EXTERNAL_STORAGE(this@MainActivity))
                    submitList(getAllAudioFiles())
                else
                    Toast.makeText(this@MainActivity, "Try Again!", Toast.LENGTH_LONG).show()
            }
            adapter = listAdapter
        }

        listAdapter.onMusicPlayerClick = { music ->
            mediaPlayer.apply {
                if (isPlaying)
                    stop()
            }
            if (!music.isPlaying) {
                mediaPlayer = MediaPlayer()
                mediaPlayer.apply {
                    setDataSource(music.data)
                    prepare()
                    start()
                }
            }
            listAdapter.submitList(
                listAdapter.currentList.map {
                    Music(
                        id = it.id,
                        title = it.title,
                        artist = it.artist,
                        data = it.data,
                        displayName = it.displayName,
                        duration = it.duration,
                        imageBitMap = it.imageBitMap,
                        isPlaying = (if (it.id == music.id) (!music.isPlaying) else false)
                    )
                }
            )

            binding.apply {
                if (music.isPlaying)
                    showMusicCard(false)
                else {
                    showMusicCard(true)
                    name.text = music.title
                    artist.text = music.artist
                    if (music.imageBitMap != null)
                        musicImage.setImageBitmap(music.imageBitMap)
                    else
                        musicImage.setImageResource(R.drawable.ic_default_image_music)

                    var isOnPause = music.isOnPause
                    playBtn.setImageResource(R.drawable.ic_big_play_icon)

                    playBtn.setOnClickListener {
                        isOnPause = !isOnPause
                        playBtn.setImageResource(
                            if (isOnPause)
                                R.drawable.ic_big_pauze_icon
                            else
                                R.drawable.ic_big_play_icon
                        )

                        listAdapter.submitList(
                            listAdapter.currentList.map {
                                Music(
                                    id = it.id,
                                    title = it.title,
                                    artist = it.artist,
                                    data = it.data,
                                    displayName = it.displayName,
                                    duration = it.duration,
                                    imageBitMap = it.imageBitMap,
                                    isOnPause = (if (it.id == music.id) isOnPause else false),
                                    isPlaying = it.isPlaying
                                )
                            }
                        )
                    }
                }
            }
        }
        listAdapter.musicOnPause = {
            if (it && mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getAllAudioFiles(): MutableList<Music> {
        val songs: MutableList<Music> = ArrayList()

        applicationContext.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
            ),
            MediaStore.Audio.Media.IS_MUSIC + " != 0",
            null,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(cursor.getString(3))
                val byteArr = mmr.embeddedPicture
                val image = if (byteArr != null)
                    BitmapFactory.decodeStream(
                        ByteArrayInputStream(
                            byteArr
                        )
                    ) else null

                songs.add(
                    Music(
                        id = cursor.getInt(0),
                        artist = cursor.getString(1),
                        title = cursor.getString(2),
                        data = cursor.getString(3),
                        displayName = cursor.getString(4),
                        duration = cursor.getInt(5),
                        imageBitMap = image,
                        isPlaying = false
                    )
                )
            }
        }
        return songs
    }

    private fun checkPermissionREAD_EXTERNAL_STORAGE(
        context: Context
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog(
                        "External storage", context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } else {
                    ActivityCompat
                        .requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    listAdapter.submitList(getAllAudioFiles())
                else
                    Toast.makeText(
                        this, "Not required permission!",
                        Toast.LENGTH_LONG
                    ).show()
            }
            else -> super.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        }
    }

    private fun showDialog(
        msg: String,
        context: Context?,
        permission: String
    ) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
            ActivityCompat.requestPermissions(
                (context as Activity?)!!, arrayOf(permission),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
        val alert: AlertDialog = alertBuilder.create()
        alert.show()
    }

    private fun showMusicCard(show: Boolean) {
        val visibility = if (show) VISIBLE else GONE
        with(binding) {
            currentPlayingMusic.visibility = visibility
            playBtn.visibility = visibility
            musicImage.visibility = visibility
            showMusicLayout.visibility = visibility
            playBtn.visibility = visibility
            linearLayout.visibility = visibility
        }
    }
}

