import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:wifi_plus/wifi_plus.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Wifi',
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: new MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int level = 0;
  String ssid = '', password = '';
  bool isChecked = false;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    getWifiStatus();
  }

  getWifiStatus() async {
    isChecked = await WifiPlus.wifiStatus;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Wifi Connection'),
        centerTitle: true,
      ),
      body: SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Column(
              children: [
                TextField(
                  decoration: InputDecoration(
                    border: UnderlineInputBorder(),
                    filled: true,
                    icon: Icon(Icons.wifi),
                    hintText: 'Your wifi ssid',
                    labelText: 'ssid',
                  ),
                  keyboardType: TextInputType.text,
                  onChanged: (value) {
                    ssid = value;
                  },
                ),
                TextField(
                  decoration: InputDecoration(
                    border: UnderlineInputBorder(),
                    filled: true,
                    icon: Icon(Icons.lock_outline),
                    hintText: 'Your wifi password',
                    labelText: 'password',
                  ),
                  keyboardType: TextInputType.text,
                  onChanged: (value) {
                    password = value;
                  },
                ),
                SizedBox(height: 20,),
                RaisedButton(
                  child: Text('connect'),
                  onPressed: () => WifiPlus.connectToWifi(ssid, password),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text("Wifi Status: "),
                    Switch(
                      value: isChecked,
                      onChanged: (bool value) {
                        setState(() {
                          isChecked = value;
                        });
                      },
                    ),
                  ],
                )
              ],
            ),
          )
      ),
    );
  }
}