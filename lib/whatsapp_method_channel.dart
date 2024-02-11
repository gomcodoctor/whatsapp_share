import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'whatsapp_platform_interface.dart';

enum Package { whatsapp, businessWhatsapp }

/// An implementation of [WhatsappPlatform] that uses method channels.
class MethodChannelWhatsapp extends WhatsappPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('whatsapp');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }


}
