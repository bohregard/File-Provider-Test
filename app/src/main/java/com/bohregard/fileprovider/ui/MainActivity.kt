package com.bohregard.fileprovider.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for External Storage Permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        } else {
            saveFile()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Hooray...
                    saveFile()
                }
            }
        }
    }

    private fun saveFile() {
        val contents =
            "Hello World.\n Local date time is ${LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)}"
        val filename = "${Instant.now()}"
        val rootDir = getExternalFilesDir(null)

        val file = File(rootDir, filename)

        if (file.exists() && file.canWrite()) {
            FileOutputStream(file, false).use {
                file.createNewFile()

                it.write(contents.toByteArray())
                it.flush()
                it.close()
            }
        }

        Log.d(TAG, "URI: ${file.path}")
        Log.d(TAG, "URI: ${file.toUri()}")
        Log.d(TAG, "URI: ${file.toUri().path}")

        val documentFile = DocumentFile.fromFile(file)
        val documentUri = DocumentFile.fromSingleUri(this, file.toUri())

        Log.d(TAG, "DF: ${documentFile.isDirectory}")
        Log.d(TAG, "DF: ${documentFile.isFile}")
        Log.d(TAG, "DF: ${documentFile.exists()}")
        Log.d(TAG, "DF: ${documentFile.uri}")
        Log.d(TAG, "DF: ${documentFile.uri.path}")
        Log.d(TAG, "DUri: ${documentUri?.isDirectory}")
        Log.d(TAG, "DUri: ${documentUri?.isFile}")
        Log.d(TAG, "DUri: ${documentUri?.exists()}")
        Log.d(TAG, "DUri: ${documentUri?.uri}")
        Log.d(TAG, "DUri: ${documentUri?.uri?.path}")
    }
}