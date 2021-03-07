package com.brahman.kotpermission

import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object PermissionCoroutine {

    suspend fun request(activity: AppCompatActivity?, permission: String) =
        suspendCoroutine<PermissionResult> { continuation ->

            val listener = object : IPermissionResult {
                override fun handlePermissionResult(statusMap: HashMap<String, PermissionResult>) {
                    if (statusMap.containsKey(permission)) {
                        continuation.resume(statusMap[permission]!!)
                    } else {
                        continuation.resume(PermissionResult(PermissionStatus.DONT_ASK_AGAIN))
                    }
                }
            }

            if (activity == null || activity.isFinishing) {
                continuation.resume(PermissionResult(PermissionStatus.ONE_TIME_DENIAL))
                return@suspendCoroutine
            }
            if (ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_GRANTED) {
                continuation.resume(PermissionResult(PermissionStatus.GRANTED))
                return@suspendCoroutine
            }
            startPermissionFragment(activity, listOf(permission), listener)
        }


    suspend fun request(activity: AppCompatActivity?, permissions: List<String>) =
        suspendCoroutine<Map<String, PermissionResult>> { continuation ->

            val listener = object : IPermissionResult {
                override fun handlePermissionResult(statusMap: HashMap<String, PermissionResult>) {
                    continuation.resume(statusMap)
                }
            }

            if (activity == null || activity.isFinishing) {
                continuation.resume(emptyMap())
                return@suspendCoroutine
            }
            startPermissionFragment(activity, permissions, listener)
        }

    fun showPermissionSettings(activity: AppCompatActivity?) {
        if (activity == null) {
            return
        }
        activity.startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.packageName, null),
            )
        )
    }

    private fun startPermissionFragment(
        activity: AppCompatActivity,
        permissions: List<String>,
        listener: IPermissionResult
    ) {
        val tag = "brahman_perm_frag"

        val fragManager = activity.supportFragmentManager
        val permissionFrag =
            fragManager.findFragmentByTag(tag) as? PermissionFragment

        if (permissionFrag != null) {
            permissionFrag.listener = listener
            return
        }

        activity.runOnUiThread {
            val newFrag = PermissionFragment.getInstance(permissions, listener)
            fragManager
                .beginTransaction()
                .add(newFrag, tag)
                .commitAllowingStateLoss()
        }
    }

}