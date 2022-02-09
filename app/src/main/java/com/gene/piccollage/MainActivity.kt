package com.gene.piccollage

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.gene.piccollage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            viewBinding.add.visibility = View.INVISIBLE
            viewBinding.editPicView.visibility = View.VISIBLE
            viewBinding.editPicView.load(it,scale = 0.8f, degree = 45, position = Point(250, 250), cropping = Rect(100, 100, 500, 500))
        }
    }


    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.add.setOnClickListener {
            launcher.launch("image/*")
        }

    }
}