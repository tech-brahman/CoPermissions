package com.brahman.kotpermission

data class PermissionResult(private val status: PermissionStatus) {
    fun isGranted() = status == PermissionStatus.GRANTED
    fun isRefused() = status != PermissionStatus.GRANTED
    fun isNeverAskAgain() = status == PermissionStatus.DONT_ASK_AGAIN
    fun isTemporaryDenied() = status == PermissionStatus.ONE_TIME_DENIAL
}