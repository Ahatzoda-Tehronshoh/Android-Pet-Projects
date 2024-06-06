package com.tehronshoh.tk_ozarakhsh.ui.activities

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.data.Poem
import com.tehronshoh.tk_ozarakhsh.databinding.ActivityBlurBinding
import com.tehronshoh.tk_ozarakhsh.viewmodels.BlurActivityViewModel
import kotlin.math.abs

var drawingBitmap: Bitmap? = null

class BlurActivity : AppCompatActivity() {
    private var _binding: ActivityBlurBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BlurActivityViewModel by viewModels()

    private lateinit var poem: Poem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBlurBinding.inflate(layoutInflater)
        setContentView(binding.root)

        poem = (if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra("poem", Poem::class.java)
            else
                intent.getParcelableExtra("poem")) ?: Poem(0, text = "", title = "")

        if (drawingBitmap != null) {
            binding.image.setImageBitmap(drawingBitmap)
        }

        setFavoriteIcon()
        setUpLiteners()
    }

    private fun setUpLiteners() {
        binding.apply {
            close.setOnClickListener {
                finish()
            }
            favoriteButton.setOnClickListener {
                poem.isFavorite = abs(poem.isFavorite - 1)
                viewModel.updatePoem(poem)
                setFavoriteIcon()
            }
        }
    }

    private fun setFavoriteIcon() {
        binding.favoriteImageChange.setImageResource(
            if (poem.isFavorite == 1)
                R.drawable.ic_baseline_favorite_24
            else
                R.drawable.ic_baseline_favorite_border_24
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
