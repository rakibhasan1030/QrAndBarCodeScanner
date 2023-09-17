package rakib.hasan.qrandbarcodescanner

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.barcode.common.Barcode
import rakib.hasan.qrandbarcodescanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val cameraPermission = android.Manifest.permission.CAMERA

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //start the scanner or camera
                startScanner()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openScannerButton.setOnClickListener { requestCameraAndStartScanner() }
    }

    private fun requestCameraAndStartScanner() {
        if (isPermissionGranted(cameraPermission)) {
            //start the scanner or camera
            startScanner()
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        when {
            shouldShowRequestPermissionRationale(cameraPermission) -> cameraPermissionRequest { openPermissionSetting() }
            else -> requestPermissionLauncher.launch(cameraPermission)
        }
    }

    private fun startScanner() {
        ScannerActivity.startScanner(this) { barcodes ->
            binding.contentInfoContainer.visibility = View.VISIBLE
            barcodes.forEach { barcode ->
                when (barcode.valueType) {
                    Barcode.TYPE_URL -> {
                        binding.typeTv.text = "URL"
                        binding.contentTv.text = barcode.url?.url.toString()
                    }

                    Barcode.TYPE_CONTACT_INFO -> {
                        binding.typeTv.text = "Contact"
                        binding.contentTv.text = barcode.contactInfo.toString()
                    }

                    Barcode.TYPE_WIFI -> {
                        binding.typeTv.text = "Wifi"
                        binding.contentTv.text = "Network Name : ${barcode.wifi?.ssid}\nPassword : ${barcode.wifi?.password}"
                    }

                    Barcode.TYPE_TEXT -> {
                        binding.typeTv.text = "Other"
                        binding.contentTv.text = barcode.rawValue.toString()
                    }

                    else -> {
                        binding.typeTv.text = "Other"
                        binding.contentTv.text = barcode.rawValue.toString()
                    }
                }
            }
        }
    }

}