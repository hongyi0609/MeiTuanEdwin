import 'package:flutter/material.dart';

class SimpleApp extends StatelessWidget {


  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
      title: "SimpleApp",
      theme: ThemeData(
          primarySwatch: Colors.red
      ),
      home: SimpleAppPage(),
    );
  }
}

class SimpleAppPage extends StatefulWidget {
  SimpleAppPage({Key key}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return _SimpleAppPageState();
  }

}

class _SimpleAppPageState extends State<SimpleAppPage> {

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
        title: Text("Simple Application", textAlign: TextAlign.justify,
          textDirection: TextDirection.ltr,
          style: TextStyle(
              color: Colors.amberAccent, fontWeight: FontWeight.bold),),
      ),
      body: Center(
        child: _getToggedChild(),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _toggle,
        tooltip: 'Pressed to update',
        child: Icon(Icons.update),
      ),
    );
  }

  /// 转换
  bool toggle = true;
  void _toggle(){
    setState(() {
      toggle = !toggle;
    });
  }

  _getToggedChild(){
    if(toggle){
      return Text(textContent);
    } else {
      return MaterialButton(
        onPressed: (){},
        child: Text(textContent),
        padding: EdgeInsets.only(left: 10.0,right: 10.0),
        color: Colors.cyan,
      );
    }
  }


  /// 文本内容
  String textContent = "Simple Application Text";

  ///
  void _updateData() {
    setState(() {
      toggle = !toggle;
      textContent = toggle ? "Simple Application Text" : "Simple Application Content";
    });
  }
}
