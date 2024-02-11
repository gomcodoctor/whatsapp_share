
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'whatsapp_platform_interface.dart';

class Package {

  static const whastapp = 'com.whatsapp';
  static const businessWhatsapp = 'com.whatsapp.w4b';

  static List<Map<String, String>> appList = [{'label' : 'Whatsapp', 'value': whastapp}, {'label' : 'Whatsapp Business', 'value': businessWhatsapp}];
}

class Whatsapp {

  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('whatsapp');


  Future<String?> getPlatformVersion() {
    return WhatsappPlatform.instance.getPlatformVersion();
  }

  /// Checks whether whatsapp is installed in device or not
  ///
  /// [Package] is optional enum parameter which is defualt to [Package.whatsapp]
  /// for business whatsapp set it to [Package.businessWhatsapp], it cannot be null
  ///
  /// return true if installed otherwise false.
  Future<bool?> isInstalled({String package = 'com.whatsapp'}) async {
    final bool? success =
    await methodChannel.invokeMethod('isInstalled', <String, dynamic>{
      "package": package,
    });
    return success;
  }

  /// Shares a message or/and link url with whatsapp.
  /// - Text: Is the [text] of the message.
  /// - LinkUrl: Is the [linkUrl] to include with the message.
  /// - Phone: is the [phone] contact number to share with.

  Future<bool?> share({
    required String phone,
    String? text,
    String? linkUrl,
    String package = 'com.whatsapp',
  }) async {
    if (phone.isEmpty) {
      throw FlutterError('Phone cannot be null and with country code');
    }

    // String package0 = package.index == 0 ? "com.whatsapp" : "com.whatsapp.w4b";

    final bool? success =
    await methodChannel.invokeMethod('share', <String, dynamic>{
      'title': ' ',
      'text': text,
      'linkUrl': linkUrl,
      'chooserTitle': ' ',
      'phone': phone,
      'package': package,
    });

    return success;
  }

  /// Shares a local file with whatsapp.
  /// - Text: Is the [text] of the message.
  /// - FilePath: Is the List of paths which can be prefilled.
  /// - Phone: is the [phone] contact number to share with.
  Future<bool?> shareFile({
    required List<String> filePath,
    String? phone,
    String? text,
    String package = 'com.whatsapp',
  }) async {
    if (filePath.isEmpty) {
      throw FlutterError('FilePath cannot be null');
    }
    // else if (phone.isEmpty) {
    //   throw FlutterError('Phone cannot be null and with country code');
    // }

    // String package0 = package.index == 0 ? "com.whatsapp" : "com.whatsapp.w4b";

    final bool? success =
    await methodChannel.invokeMethod('shareFile', <String, dynamic>{
      'title': ' ',
      'text': text,
      'filePath': filePath,
      'chooserTitle': ' ',
      'phone': phone,
      'package': package,
    });

    return success;
  }
}
