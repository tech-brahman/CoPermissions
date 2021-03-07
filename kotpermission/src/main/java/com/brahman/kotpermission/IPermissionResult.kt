package com.brahman.kotpermission

interface IPermissionResult {
    fun handlePermissionResult(statusMap: HashMap<String, PermissionResult>)
}