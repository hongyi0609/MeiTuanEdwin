import 'package:flutter/material.dart';

class LayoutDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build

    return MaterialApp(
      title: 'Flutter Layout Demo',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: MyLayout(),
    );
  }
}

class MyLayout extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return MyLayoutState();
  }
}

class MyLayoutState extends State<MyLayout> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    Widget titleSection = Container(
      padding: const EdgeInsets.all(32.0),
      child: Row(
        // 水平方向，行方向
        children: <Widget>[
          Expanded(
              flex: 1,
              child: Column(
                // 竖直方向，列方向
                crossAxisAlignment: CrossAxisAlignment.start, // children排列方式
                children: <Widget>[
                  Container(
                      padding: const EdgeInsets.only(bottom: 8.0),
                      child: Text(
                        'Oeschinen Lake Campground',
                        style: TextStyle(fontWeight: FontWeight.bold),
                      )),
                  Text(
                    'Kandersteg, Switzerland',
                    style: TextStyle(color: Colors.grey),
                  )
                ],
              )),
          Icon(
            Icons.star,
            color: Colors.red[500],
          ),
          Text("41")
        ],
      ),
    );

    /// 构建底层按钮单元
    Column buildButtonColumn(IconData icon, String label) {
      Color color = Theme.of(context).primaryColor;
      return Column(
        mainAxisSize: MainAxisSize.min,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Icon(icon, color: color),
          Container(
            margin: EdgeInsets.only(top: 8.0),
            child: Text(
              label,
              style: TextStyle(
                  fontSize: 12.0, fontWeight: FontWeight.w400, color: color),
            ),
          )
        ],
      );
    }

    Widget buttonSection = Container(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: <Widget>[
          buildButtonColumn(Icons.call, 'CALL'),
          buildButtonColumn(Icons.near_me, "ROUTE"),
          buildButtonColumn(Icons.share, "SHARE")
        ],
      ),
    );

    Widget textSection = Container(
      padding: const EdgeInsets.all(32.0),
      child: Text(
        '''Lake Oeschinen lies at the foot of the Blüemlisalp in the Bernese Alps. Situated 1,578 meters above sea level, it is one of the larger Alpine Lakes. A gondola ride from Kandersteg, followed by a half-hour walk through pastures and pine forest, leads you to the lake, which warms to 20 degrees Celsius in the summer. Activities enjoyed here include rowing, and riding the summer toboggan run.
''',
        softWrap: true, //softwrap属性表示文本是否应在软换行符（例如句点或逗号）之间断开。
      ),
    );
    Widget imgSection = Image.asset(
      // 图片部分
      'images/lake.png',
      width: 640.0,
      height: 240.0,
      fit: BoxFit.cover,
    );

    return Scaffold(
      appBar: AppBar(
        title: Text('Layout Demo'),
        backgroundColor: Colors.blueAccent,
        centerTitle: true,
      ),
      body: ListView(
        // 出于兼容性考虑，尽量不要用Column
        children: <Widget>[
//        Image.network
          imgSection,
          titleSection,
          buttonSection,
          textSection
        ],
      ),
    );
  }
}
