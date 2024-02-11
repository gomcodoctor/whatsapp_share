package com.baskit.whatsapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
//import android.text.TextUtils
import android.util.Log
import androidx.core.content.FileProvider
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File

/** WhatsappPlugin */
class WhatsappPlugin: FlutterPlugin, MethodCallHandler {

  private var context: Context? = null
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    this.context = flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "whatsapp")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {

    if (call.method == "shareFile") {
      shareFile(call, result)
    } else if (call.method == "share") {
//      share(call, result)
    } else if (call.method == "isInstalled") {
      isInstalled(call, result)
    } else {
      result.notImplemented()
    }
//    if (call.method == "getPlatformVersion") {
//      result.success("Android ${android.os.Build.VERSION.RELEASE}")
//    } else {
//      result.notImplemented()
//    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  private fun isPackageInstalled(packageName: String): Boolean {
    return try {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        context?.packageManager?.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
      }else{
        context?.packageManager?.getPackageInfo(packageName, 0)
      }
      true
    } catch (e: PackageManager.NameNotFoundException) {
//      Log.println(Log.ERROR, "", "not installed")
      false
    }
  }

  private fun isInstalled(call: MethodCall, result: Result) {
    try {
      val packageName = call.argument<String>("package")
      if (packageName == null || packageName.isEmpty()) {
        Log.println(Log.ERROR, "", "FlutterShare Error: Package name null or empty")
        result.error("FlutterShare:Package name cannot be null or empty", null, null)
        return
      }
      val isInstalled = isPackageInstalled(packageName)
      result.success(isInstalled)
    } catch (ex: Exception) {
      Log.println(Log.ERROR, "", "FlutterShare: Error")
      result.error(ex.message!!, null, null)
    }
  }

//  private fun share(call: MethodCall, result: Result) {
//    try {
//      val title = call.argument<String>("title")
//      val text = call.argument<String>("text")
//      val linkUrl = call.argument<String>("linkUrl")
////      val chooserTitle = call.argument<String>("chooserTitle")
//      val phone = call.argument<String>("phone")
//      val packageName = call.argument<String>("package")
//      if (title == null || title.isEmpty()) {
//        Log.println(Log.ERROR, "", "FlutterShare Error: Title null or empty")
//        result.error("FlutterShare: Title cannot be null or empty", null, null)
//        return
//      } else if (phone == null || phone.isEmpty()) {
//        Log.println(Log.ERROR, "", "FlutterShare Error: phone null or empty")
//        result.error("FlutterShare: phone cannot be null or empty", null, null)
//        return
//      } else if (packageName == null || packageName.isEmpty()) {
//        Log.println(Log.ERROR, "", "FlutterShare Error: Package name null or empty")
//        result.error("FlutterShare:Package name cannot be null or empty", null, null)
//        return
//      }
//      val extraTextList = ArrayList<String?>()
//      if (text != null && text.isNotEmpty()) {
//        extraTextList.add(text)
//      }
//      if (linkUrl != null && linkUrl.isNotEmpty()) {
//        extraTextList.add(linkUrl)
//      }
//      var extraText: String? = ""
//      if (!extraTextList.isEmpty()) {
//        extraText = TextUtils.join("\n\n", extraTextList)
//      }
//      val intent = Intent()
//      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//      intent.action = Intent.ACTION_SEND
//      intent.type = "text/plain"
//      intent.setPackage(packageName)
//      intent.putExtra("jid", "$phone@s.whatsapp.net")
//      intent.putExtra(Intent.EXTRA_SUBJECT, title)
//      intent.putExtra(Intent.EXTRA_TEXT, extraText)
//
//      //Intent chooserIntent = Intent.createChooser(intent, chooserTitle);
//      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//      context?.startActivity(intent)
//      result.success(true)
//    } catch (ex: Exception) {
//      Log.println(Log.ERROR, "", "FlutterShare: Error")
//      result.error(ex.message!!, null, null)
//    }
//  }

  private fun shareFile(call: MethodCall, result: Result) {
    val filePaths: ArrayList<String>

    val files = ArrayList<Uri>()
    try {
//      val title = call.argument<String>("title")
      val text = call.argument<String>("text")
      filePaths = call.argument("filePath")!!
//      val chooserTitle = call.argument<String>("chooserTitle")
      val phone = call.argument<String?>("phone")
      val packageName = call.argument<String>("package")
      if (filePaths.isEmpty()) {
        Log.println(Log.ERROR, "", "FlutterShare: ShareLocalFile Error: filePath null or empty")
        result.error("FlutterShare: FilePath cannot be null or empty", null, null)
        return
      }
//      else if (phone == null || phone.isEmpty()) {
//        Log.println(Log.ERROR, "", "FlutterShare Error: phone null or empty")
//        result.error("FlutterShare: phone cannot be null or empty", null, null)
//        return
//      }
      else if (packageName == null || packageName.isEmpty()) {
        Log.println(Log.ERROR, "", "FlutterShare Error: Package name null or empty")
        result.error("FlutterShare:Package name cannot be null or empty", null, null)
        return
      }

      for (i in filePaths.indices) {
        val file = File(filePaths[i])
        val fileUri: Uri = FileProvider.getUriForFile(context!!, context?.applicationContext!!.packageName + ".provider", file)
//        Log.println(Log.ERROR, "", fileUri.toString())
        files.add(fileUri)
      }

      val extraTextList = ArrayList<CharSequence>()
      if (text != null && text.isNotEmpty()) {
        extraTextList.add(text)
      }
//      var extraText: CharSequence = ""
//      if (!extraTextList.isEmpty()) {
//        extraText = TextUtils.join("\n\n", extraTextList)
//      }

//      Log.println(Log.ERROR, "", extraTextList.toString())

      val intent = Intent()
      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      intent.action = Intent.ACTION_SEND_MULTIPLE
      intent.type = "image/*"
      intent.setPackage(packageName)

      if(phone != null && phone.isNotEmpty()) intent.putExtra("jid", "$phone@s.whatsapp.net")

      intent.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT")
      intent.putExtra(Intent.EXTRA_TEXT, extraTextList)
      intent.putExtra(Intent.EXTRA_STREAM, files)
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

      //Intent chooserIntent = Intent.createChooser(intent, chooserTitle);
      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      context?.startActivity(intent)
      result.success(true)
    } catch (ex: Exception) {
      result.error(ex.message!!, null, null)
      Log.println(Log.ERROR, "", "FlutterShare: Error")
    }
  }
}
