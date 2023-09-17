package rakib.hasan.qrandbarcodescanner

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.core.content.ContextCompat
import java.security.Permission

fun Context.isPermissionGranted(permission: String): Boolean = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

inline fun Context.cameraPermissionRequest(crossinline positive: ()-> Unit){
    AlertDialog.Builder(this)
        .setTitle(getString(R.string.camera_permission_dialog_box_title))
        .setMessage(getString(R.string.camera_permission_dialog_box_message))
        .setPositiveButton(getString(R.string.camera_permission_dialog_positive_button_text)) { dialog, which ->
            positive.invoke()
        }.setNegativeButton(getString(R.string.camera_permission_dialog_negetive_button_text)) { dialog, which ->
        }.show()
}
fun Context.openPermissionSetting(){
    Intent(ACTION_APPLICATION_DETAILS_SETTINGS).also {
        val uri: Uri = Uri.fromParts("package", packageName, null)
        it.data = uri
        startActivity(it)
    }
}