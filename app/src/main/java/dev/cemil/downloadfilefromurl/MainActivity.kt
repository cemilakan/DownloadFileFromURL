package dev.cemil.downloadfilefromurl

import android.app.DownloadManager
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CODE_WRITE_EXTERNAL_STORAGE = 47
        private const val URL = "https://www.nasa.gov/sites/default/files/atoms/files/artemis_plan-20200921.pdf"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_download.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)  downloadIt(URL) else{ ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), CODE_WRITE_EXTERNAL_STORAGE)}
        }
    }

    fun downloadIt(url : String) {
        val request : DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
        val title = URLUtil.guessFileName(url,null,null)
        request.setTitle(title)
        request.setDescription("Download started...")
        val biscuit/*😜*/ = CookieManager.getInstance().getCookie(url)
        request.addRequestHeader("biscuit", biscuit)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title)
        val downloadManager: DownloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            CODE_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_LONG).show()
                    downloadIt(URL)
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}