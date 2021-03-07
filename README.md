# CoPermissions
Lightweight Kotlin coroutine for requesting Android runtime permissions. Use it directly in your suspend functions without overriding onPermissionResult



# Usage

## Add it in your root build.gradle at the end of repositories:
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}


## Add the dependency

	dependencies {
	        implementation 'com.github.tech-brahman:CoPermissions:0.1.0'
	}
	

## Request single permission inside a coroutine
	suspend fun yourFunc(){
      val yourActivity = this@MainActivity
      
      //Request permission
      val result = PermissionCoroutine.request(yourActivity, android.Manifest.permission.CAMERA)
   
       when{
          result.isGranted() -> {
            // Permission granted 
          }

          result.isNeverAskAgain() ->{
            // User disabled the permission permanently, show app settings page if you want
            withContext(Dispatchers.Main){
              PermissionCoroutine.showPermissionSettings(yourActivity)
            }
          }

          result.isTemporaryDenied() ->{
            // User denied the permission one time
          }

          result.isRefused() ->{
            // Permission refused either on time or permanently disabled
          }

        }
   
	}
    
    


## Request multiple permissions inside a coroutine

    suspend fun yourFunc(){
      val yourActivity = this@MainActivity
      
      //Request permission
      val resultMap = PermissionCoroutine.request(
        yourActivity, 
        listOf(
          android.Manifest.permission.CAMERA,
          android.Manifest.permission.ACCESS_FINE_LOCATION,
          android.Manifest.permission.WRITE_EXTERNAL_STORAGE
         )
      )
      
      resultMap.forEach{ perm->
        if(perm.key == android.Manifest.permission.CAMERA){
          val result = perm.value
           when{
              result.isGranted() -> {
                // Permission granted 
              }

              result.isNeverAskAgain() ->{
                // User disabled the permission permanently, show app settings page if you want
                withContext(Dispatchers.Main){
                  PermissionCoroutine.showPermissionSettings(yourActivity)
                }
              }

              result.isTemporaryDenied() ->{
                // User denied the permission one time
              }

              result.isRefused() ->{
                // Permission refused either on time or permanently disabled
              }
           
          }
          
          // end of if
        }
        
        // end of foreach
      }
   
	}
   
