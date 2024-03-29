// You have generated a new plugin project without
// specifying the `--platforms` flag. A plugin project supports no platforms is generated.
// To add platforms, run `flutter create -t plugin --platforms <platforms> .` under the same
// directory. You can also find a detailed instruction on how to add platforms in the `pubspec.yaml` at https://flutter.dev/docs/development/packages-and-plugins/developing-packages#plugin-platforms.

import 'dart:async';

import 'package:flutter/services.dart';

class WifiPlus {
  static const MethodChannel _channel = const MethodChannel('wifi_plus');

  static Future<bool> connectToWifi(String ssid, String password, {bool isWEP = false}) async {
    final result = await _channel.invokeMethod('connectWifi', {
      "ssid": ssid,
      "password": password
    });
    print(result);
    return result == true;
  }
  static Future<bool> switchWifi(String onOff, {bool isWEP = false}) async {
    final result = await _channel.invokeMethod('switchWifi', {
      "state": onOff,
    });
    print(result);
    return result == true;
  }
  static Future<bool> get wifiStatus async {
    final result = await _channel.invokeMethod('wifiStatus');
    return result == "ON";
  }
}
