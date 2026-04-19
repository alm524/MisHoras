package com.mishoras.app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.MimeTypeMap
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : ComponentActivity() {

    private lateinit var webView: WebView
    private var fileChooserCallback: ValueCallback<Array<Uri>>? = null

    private val fileChooserLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            val cb = fileChooserCallback
            fileChooserCallback = null
            cb?.onReceiveValue(if (uri != null) arrayOf(uri) else null)
        }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Barra de estado oscura con texto claro, para combinar con el header del HTML
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        webView = WebView(this)
        webView.setBackgroundColor(0xFFF4F0E8.toInt())
        setContentView(webView)

        val s = webView.settings
        s.javaScriptEnabled = true
        s.domStorageEnabled = true
        s.databaseEnabled = true
        s.allowFileAccess = true
        s.allowContentAccess = true
        s.setSupportZoom(false)
        s.builtInZoomControls = false
        s.displayZoomControls = false
        s.textZoom = 100

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                view: WebView?,
                callback: ValueCallback<Array<Uri>>,
                params: FileChooserParams?
            ): Boolean {
                fileChooserCallback?.onReceiveValue(null)
                fileChooserCallback = callback

                val mimeTypes: Array<String> = (params?.acceptTypes ?: emptyArray())
                    .filter { it.isNotEmpty() }
                    .map { type ->
                        if (type.startsWith(".")) {
                            MimeTypeMap.getSingleton()
                                .getMimeTypeFromExtension(type.substring(1)) ?: "*/*"
                        } else {
                            type
                        }
                    }
                    .toTypedArray()

                return try {
                    fileChooserLauncher.launch(
                        if (mimeTypes.isEmpty()) arrayOf("*/*") else mimeTypes
                    )
                    true
                } catch (e: Exception) {
                    fileChooserCallback = null
                    false
                }
            }
        }

        webView.addJavascriptInterface(AndroidBridge(), "AndroidDownloader")

        webView.loadUrl("file:///android_asset/index.html")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * Puente JS <-> Android. La WebView expone esto como `window.AndroidDownloader`.
     * El HTML lo detecta y lo usa para guardar archivos directamente en la
     * carpeta Descargas real del sistema, sin depender de Chrome ni de blob: URLs.
     */
    inner class AndroidBridge {

        @JavascriptInterface
        fun saveText(filename: String, mimeType: String, textContent: String): Boolean {
            return try {
                val bytes = textContent.toByteArray(Charsets.UTF_8)
                val uri = saveToDownloads(filename, mimeType, bytes)
                val ok = uri != null
                runOnUiThread {
                    if (ok) {
                        Toast.makeText(
                            this@MainActivity,
                            "Guardado en Descargas:\n$filename",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "No se pudo guardar el archivo.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                ok
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Error al guardar: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                false
            }
        }
    }

    private fun saveToDownloads(filename: String, mimeType: String, data: ByteArray): Uri? {
        val resolver = contentResolver
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        } else {
            return null
        }
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, filename)
            put(
                MediaStore.Downloads.MIME_TYPE,
                if (mimeType.isEmpty()) "application/octet-stream" else mimeType
            )
            put(MediaStore.Downloads.IS_PENDING, 1)
        }
        val uri = resolver.insert(collection, values) ?: return null
        try {
            resolver.openOutputStream(uri)?.use { it.write(data) }
            values.clear()
            values.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
            return uri
        } catch (e: Exception) {
            resolver.delete(uri, null, null)
            throw e
        }
    }
}
