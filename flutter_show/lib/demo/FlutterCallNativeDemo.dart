import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class FlutterCallNativeDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
      title: 'Flutter Call Native Demo',
      home: Scaffold(
        appBar: AppBar(
          title: Text("Flutter Call Native View"),
        ),
        body: FlutterCallNative(),
      ),
    );
  }
}

class FlutterCallNative extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return FlutterCallNativeState();
  }
}

class FlutterCallNativeState extends State<FlutterCallNative> {

  /// methodChannel 响应原生调用
  // 在initState中调用
  Future<dynamic> batteryCallHandler(MethodCall call) async {
    switch (call.method) {
      case "getFlutterContent":
        return 'This is Flutter Content!';
      default:
        print('Unknown method ${call.method}');
        throw MissingPluginException();
        break;
    }
  }
  @override
  initState(){
    super.initState();

    methodChannelBattery.setMethodCallHandler(batteryCallHandler);
  }
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
        body: Column(
      children: <Widget>[
        Container(
          height: 30.0,
          margin: EdgeInsets.only(top: 12.0),
          child: AndroidView(
            viewType: 'nativeTextView', // 使用原生组件
            creationParams: {"content": "我是来自Flutter的数据！"}, // 用map给原生组件传值
            creationParamsCodec:
                const StandardMessageCodec(), // creationParamsCodec传入的是一个编码对象这是固定写法
            onPlatformViewCreated: onAndroidViewCreated,
          ),
        ),
        Container(
          margin: EdgeInsets.only(left: 10, top: 12.0, right: 10.0),
          child: Row(
            children: <Widget>[
              RaisedButton(
                onPressed: _getBatteryLevel,
                child: Text('GetBatteryFromNative',
                    style: TextStyle(fontSize: 12)),
              ),
              Padding(
                padding: EdgeInsets.only(left: 10),
                child: Text(_batteryLevel),
              )
            ],
          ),
        ),
      ],
    ));
  }

  /// methodChannel,调用原生方法
  // 构造函数参数就是上面 Android 的 METHOD_CHANNEL 常量
  static const methodChannelBattery =
      const MethodChannel("com.edwin.flutter_show/battery");

  String _batteryLevel = 'Unknown battery level.';

  Future<void> _getBatteryLevel() async {
    String batteryLevel;
    try {
      // invokeMethod('getBatteryLevel') 会回调 MethodCallHandler
      final int result =
          await methodChannelBattery.invokeMethod("getBatteryLevel");
      batteryLevel = 'Battery level is at $result %.';
    } on PlatformException catch (e) {
      batteryLevel = "Failed to get battery level: '${e.message}'.";
    } on MissingPluginException catch (e) {
      batteryLevel = 'Plugin undefined';
    }
    setState(() {
      _batteryLevel = batteryLevel;
    });
  }

  /// 获取原生View数据,通过MethodChannel
  MethodChannel _methodChannel;
  void onAndroidViewCreated(int id) {
    _methodChannel = MethodChannel("nativeTextView_$id");
    setAndroidViewText("setAndroidViewText1");
  }

  Future<void> setAndroidViewText(String text) async {
    assert(text != null);
    return _methodChannel.invokeMethod('setText', text);
  }
}
