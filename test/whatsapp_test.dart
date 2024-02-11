import 'package:flutter_test/flutter_test.dart';
import 'package:whatsapp/whatsapp.dart';
import 'package:whatsapp/whatsapp_platform_interface.dart';
import 'package:whatsapp/whatsapp_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockWhatsappPlatform
    with MockPlatformInterfaceMixin
    implements WhatsappPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final WhatsappPlatform initialPlatform = WhatsappPlatform.instance;

  test('$MethodChannelWhatsapp is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelWhatsapp>());
  });

  test('getPlatformVersion', () async {
    Whatsapp whatsappPlugin = Whatsapp();
    MockWhatsappPlatform fakePlatform = MockWhatsappPlatform();
    WhatsappPlatform.instance = fakePlatform;

    expect(await whatsappPlugin.getPlatformVersion(), '42');
  });
}
