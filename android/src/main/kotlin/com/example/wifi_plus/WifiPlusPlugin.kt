package com.example.wifi_plus

import android.app.Application
import android.annotation.TargetApi
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PatternMatcher
import android.os.PatternMatcher.PATTERN_PREFIX
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.lang.Error
import java.util.*

class WifiPlusPlugin() : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var networkId: Int? = null
    private val connectivityManager: ConnectivityManager by lazy(LazyThreadSafetyMode.NONE) {
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val wifiManager: WifiManager by lazy(LazyThreadSafetyMode.NONE) {
        context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext()
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "wifi_plus")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "connectWifi" -> {
                connectToWifi(call, result)
            }
            "switchWifi" -> {
                switchWifi(call, result)
            }
            "wifiStatus" -> {
                wifiStatus(result)
            }
            else -> result.notImplemented()
        }
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun connectToWifi(call: MethodCall, result: MethodChannel.Result) {
        val arguments: Map<String, String> = call.arguments();
        val ssid = arguments["ssid"]
        val password = arguments["password"]
        try {
            val wifiNetworkSuggestion: WifiNetworkSuggestion = WifiNetworkSuggestion.Builder().setSsid(ssid.toString()).setWpa2Passphrase(password.toString()).build()
            val list: MutableList<WifiNetworkSuggestion> = ArrayList()
            list.add(wifiNetworkSuggestion)
            wifiManager.removeNetworkSuggestions(ArrayList<WifiNetworkSuggestion>())
            wifiManager.addNetworkSuggestions(list)
            result.success("connected successfully");
        } catch (e: Exception) {
            e.printStackTrace()
            result.success(e.toString());
        }
    }

    private fun switchWifi(call: MethodCall, result: MethodChannel.Result) {
        val arguments: Map<String, String> = call.arguments();
        val state = arguments["state"]
        try {
            wifiManager.setWifiEnabled(state.toString() == "ON");
        } catch (e: Exception) {
            e.printStackTrace()
            result.success(e.toString());
        }
    }

    private fun wifiStatus(result: MethodChannel.Result) {
        try {
            if(wifiManager.isWifiEnabled()) {
                result.success("ON")
            }else{
                result.success("OFF")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            result.success(e.toString());
        }
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

}

//import android.app.Application
//import android.annotation.TargetApi
//import android.content.Context
//import android.net.wifi.WifiManager
//import android.net.wifi.WifiNetworkSuggestion
//import android.os.Build
//import androidx.annotation.NonNull
//import io.flutter.embedding.android.FlutterActivity
//import io.flutter.embedding.engine.FlutterEngine
//import io.flutter.plugin.common.MethodCall
//import io.flutter.plugin.common.MethodChannel
//import java.util.*
//
//class MyApplication : Application() {
//
//    override fun onCreate() {
//        super.onCreate()
//        MyApplication.appContext = applicationContext
//    }
//
//    companion object {
//
//        lateinit var appContext: Context
//
//    }
//}
//
//class WifiPlusPlugin : FlutterActivity() {
//    private val CHANNEL = "wifi_plus"
//
//
//    @ExperimentalStdlibApi
//    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
//        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
//            when {
//                call.method.equals("connectWifi") -> {
//                    connectToWifi(call, result)
//                }
//                call.method.equals("switchWifi") -> {
//                    switchWifi(call, result)
//                }
//            }
//        }
//
//        @TargetApi(Build.VERSION_CODES.Q)
//        private fun connectToWifi(call: MethodCall, result: MethodChannel.Result) {
//            val arguments: Map<String, String> = call.arguments();
//            val ssid = arguments["ssid"]
//            val password = arguments["password"]
//            try {
//                val wifiNetworkSuggestion: WifiNetworkSuggestion = WifiNetworkSuggestion.Builder().setSsid(ssid.toString()).setWpa2Passphrase(password.toString()).build()
//                val list: MutableList<WifiNetworkSuggestion> = ArrayList()
//                list.add(wifiNetworkSuggestion)
//                val context = MyApplication.appContext
//                val wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//                wifiManager.removeNetworkSuggestions(ArrayList<WifiNetworkSuggestion>())
//                wifiManager.addNetworkSuggestions(list)
//                result.success("connected successfully");
//            } catch (e: Exception) {
//                e.printStackTrace()
//                result.success(e.toString());
//            }
//        }

//        private fun switchWifi(call: MethodCall, result: MethodChannel.Result) {
//            val arguments: Map<String, String> = call.arguments();
//            val state: Boolean = arguments["state"]
//            try {
//                val context = MyApplication.appContext
//                val wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//                wifiManager.setWifiEnabled(state);
//            } catch (e: Exception) {
//                e.printStackTrace()
//                result.success(e.toString());
//            }
//        }
//    }
