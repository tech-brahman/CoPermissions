package com.brahman.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.brahman.kotpermission.PermissionCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.jar.Manifest
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn_permission)
            .setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = PermissionCoroutine.request(
                        this@MainActivity,
                        android.Manifest.permission.CAMERA
                    )

                    withContext(Dispatchers.Main) {
                        if (result.isNeverAskAgain()) {
                            PermissionCoroutine.showPermissionSettings(this@MainActivity)
                        }

                        Toast.makeText(this@MainActivity, result.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        findViewById<View>(R.id.btn_multiple_permission)
            .setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = PermissionCoroutine.request(
                        this@MainActivity,
                        listOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        )
                    )
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, result.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }
}