package com.brahman.kotpermission

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment

class PermissionFragment : Fragment() {

    private val REQUEST_CODE = 510
    private val ARG_PERMISSIONS = "permissions"
    var listener: IPermissionResult? = null

    companion object {

        fun getInstance(permissions: List<String>, permissionListener: IPermissionResult) =
            PermissionFragment().apply {
                arguments = Bundle()
                    .apply {
                        val arrayList = ArrayList<String>()
                            .apply {
                                addAll(permissions)
                            }
                        putStringArrayList(ARG_PERMISSIONS, arrayList)
                    }
                listener = permissionListener
            }

    }


    override fun onResume() {
        super.onResume()
        val requestedPermissions = arguments
            ?.getStringArrayList(ARG_PERMISSIONS)

        if (requestedPermissions.isNullOrEmpty()) {
            exit()
            return
        }
        requestPermissions(requestedPermissions.toTypedArray(), REQUEST_CODE)
    }

    private fun exit() {
        fragmentManager
            ?.beginTransaction()
            ?.remove(this)
            ?.commitAllowingStateLoss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != REQUEST_CODE || permissions.isEmpty() || listener == null) {
            exit()
            return
        }

        val statusMap = HashMap<String, PermissionResult>()

        for (i in permissions.indices) {
            val name = permissions[i]
            when {
                grantResults[i] == PackageManager.PERMISSION_GRANTED ->
                    statusMap[name] = PermissionResult(PermissionStatus.GRANTED)

                shouldShowRequestPermissionRationale(name) ->
                    statusMap[name] = PermissionResult(PermissionStatus.ONE_TIME_DENIAL)

                else ->
                    statusMap[name] = PermissionResult(PermissionStatus.DONT_ASK_AGAIN)
            }
        }
        listener?.handlePermissionResult(statusMap)
        exit()
    }


}