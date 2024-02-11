import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'whatsapp_method_channel.dart';

abstract class WhatsappPlatform extends PlatformInterface {
  /// Constructs a WhatsappPlatform.
  WhatsappPlatform() : super(token: _token);

  static final Object _token = Object();

  static WhatsappPlatform _instance = MethodChannelWhatsapp();

  /// The default instance of [WhatsappPlatform] to use.
  ///
  /// Defaults to [MethodChannelWhatsapp].
  static WhatsappPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [WhatsappPlatform] when
  /// they register themselves.
  static set instance(WhatsappPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
