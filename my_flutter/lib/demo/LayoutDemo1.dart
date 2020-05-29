import 'package:flutter/material.dart';

class LayoutDemo1 extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
//      title: 'LayoutDemo1',
//      theme: ThemeData(primarySwatch: Colors.blue),
      home: Layout1(),
    );
  }
}

class Layout1 extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return LayoutState1();
  }
}

class LayoutState1 extends State<Layout1> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build

    Widget packedRow = Row(
      mainAxisSize: MainAxisSize.min, // 聚合
      children: <Widget>[
        Icon(Icons.star, color: Colors.green[500], size: 12),
        Icon(Icons.star, color: Colors.green[500], size: 12),
        Icon(Icons.star, color: Colors.green[500], size: 12),
        Icon(Icons.star, color: Colors.black, size: 12),
        Icon(Icons.star, color: Colors.black, size: 12),
      ],
    );

    var ratings = Container(
      padding: const EdgeInsets.all(2.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: <Widget>[
          packedRow,
          Text(
            "170 reviewers",
            style: TextStyle(
                color: Colors.black,
                fontWeight: FontWeight.w800,
                fontFamily: 'Roboto',
                letterSpacing: 0.5,
                fontSize: 14.0),
          )
        ],
      ),
    );

    var descTextStyle = TextStyle(
      color: Colors.black,
      fontWeight: FontWeight.w800,
      fontFamily: 'Roboto',
      letterSpacing: 0.5,
      fontSize: 12.0,
      height: 2.0,
    );
    // DefaultTextStyle.merge可以允许您创建一个默认的文本样式，该样式会被其
    // 所有的子节点继承
    var iconList = DefaultTextStyle.merge(
        style: descTextStyle,
        child: Container(
          padding: EdgeInsets.all(20.0),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              Column(
                children: <Widget>[
                  Icon(Icons.kitchen, color: Colors.green[500]),
                  Text('PREP:'),
                  Text('25 min'),
                ],
              ),
              Column(
                children: [
                  Icon(Icons.timer, color: Colors.green[500]),
                  Text('COOK:'),
                  Text('1 hr'),
                ],
              ),
              Column(
                children: [
                  Icon(Icons.restaurant, color: Colors.green[500]),
                  Text('FEEDS:'),
                  Text('4-6'),
                ],
              )
            ],
          ),
        ));

    var leftColumn = Container(
      padding: EdgeInsets.fromLTRB(20.0, 30.0, 20.0, 0.0),
      child: Column(
        children: [
          Text(
            "Stawberry Pavlova",
            textAlign: TextAlign.center,
            style: TextStyle(
                color: Colors.black,
                fontSize: 24.0,
                fontWeight: FontWeight.bold),
          ),
          Text(
            '''Lake Oeschinen lies at the foot of the Blüemlisalp in the Bernese Alps. Situated 1,578 meters above sea level, it is one of the larger Alpine Lakes. A gondola ride from Kandersteg, followed by a half-hour walk through pastures and pine forest, leads you to the lake, which warms to 20 degrees Celsius in the summer. Activities enjoyed here include rowing, and riding the summer toboggan run.
''',
            textAlign: TextAlign.center,
            softWrap: true,
          ),
          ratings,
          iconList,
        ],
      ),
    );
    var mainImg = Image.network(
      'https://images.unsplash.com/photo-1471115853179-bb1d604434e0?dpr=1&auto=format&fit=crop&w=767&h=583&q=80&cs=tinysrgb&crop=',
      width: 120.0,
      fit: BoxFit.cover,
    );
    var stack = Stack(
      alignment: const Alignment(0.0, 0.0), // child居中
      children: <Widget>[
        CircleAvatar(
          backgroundImage: AssetImage('images/lake.png'),
          radius: 30.0,
        ),
        Container(
          decoration: BoxDecoration(color: Colors.black45),
          child: Text('Stack',
              style: TextStyle(
                fontSize: 16.0,
                fontWeight: FontWeight.bold,
                color: Colors.white,
              )),
        )
      ],
    );
    var row = Row(
      children: <Widget>[
        Expanded(
            child: Container(
          decoration: BoxDecoration(
              border: Border.all(color: Colors.black38, width: 5),
              borderRadius: const BorderRadius.all(const Radius.circular(8.0))),
          margin: const EdgeInsets.all(4.0),
          child: Image.network(
              "https://pic1.zhimg.com/v2-edf063d6de88faba84ff067d5bfde900.jpg"),
        )),
        Expanded(
            child: Container(
          decoration: BoxDecoration(
              border: Border.all(color: Colors.black38, width: 5),
              borderRadius: const BorderRadius.all(const Radius.circular(8.0))),
          margin: const EdgeInsets.all(4.0),
          child: Image.asset('images/pic2.jpg'),
        )),
      ],
    );
    var container = Container(
      margin: const EdgeInsets.only(left: 12.0, top: 12.0),
      decoration: BoxDecoration(color: Colors.black26),
      child: Column(
        children: <Widget>[row, row, row, row],
      ),
    );

    var rightColumn = Container(
      padding: EdgeInsets.fromLTRB(2.0, 3.0, 2.0, 2.0),
      child: Column(
        children: <Widget>[
          mainImg,
          Container(
            margin: const EdgeInsets.only(left: 2.0, top: 2.0),
            child: stack,
          ),
          container
        ],
      ),
    );

    var row1 = Container(
      margin: EdgeInsets.fromLTRB(0.0, 20.0, 0.0, 15.0),
      height: 480.0,
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Container(
            width: 240.0,
            child: leftColumn,
          ),
          Container(
            width: 140.0,
            child: rightColumn,
          )
        ],
      ),
    );

    List<Container> _buildGridTileList(int count) {
      return List<Container>.generate(
        count,
        (int index) => Container(
                child: Stack(
              alignment: const Alignment(0.0, 0.9), // 标题居中下
              children: <Widget>[
                Image.asset('images/pic${index % 4 + 1}.jpg'),
                Container(
                  decoration: BoxDecoration(color: Colors.black45),
                  child: Text('pic$index',
                      style: TextStyle(
                        fontSize: 16.0,
                        fontWeight: FontWeight.bold,
                        color: Colors.white,
                      )),
                )
              ],
            )),
      );
    }

    Widget buildGrid() {
      return GridView.extent(
        maxCrossAxisExtent: 150.0,
        padding: EdgeInsets.all(4.0),
        mainAxisSpacing: 4.0,
        crossAxisSpacing: 4.0,
        children: _buildGridTileList(30),
        shrinkWrap: true, //解决无限高度问题
        physics: NeverScrollableScrollPhysics(), //禁用滑动事件
      );
    }

    var row2 = Container(
      margin: EdgeInsets.fromLTRB(0.0, 20.0, 0.0, 15.0),
//      height: 480.0,
      child: buildGrid(),
    );

    List<Widget> list = <Widget>[
      ListTile(
        title: Text('CineArts at the Empire',
            style: TextStyle(fontWeight: FontWeight.w500, fontSize: 20.0)),
        subtitle: Text('85 W Portal Ave'),
        leading: Icon(
          Icons.theaters,
          color: Colors.blue[500],
        ),
      ),
      Divider(),
      Card(
          child: ListTile(
        title: Text('The Castro Theater',
            style: TextStyle(fontWeight: FontWeight.w500, fontSize: 20.0)),
        subtitle: Text('429 Castro St'),
        leading: Icon(
          Icons.theaters,
          color: Colors.blue[500],
        ),
      )),
      Divider(indent: 100.0),
      ListTile(
        title: Text('Emmy\'s Restaurant',
            style: TextStyle(fontWeight: FontWeight.w500, fontSize: 20.0)),
        subtitle: Text('1923 Ocean Ave'),
        leading: Icon(
          Icons.restaurant,
          color: Colors.blue[500],
        ),
      ),
    ];

    var row3 = Container(
      margin: EdgeInsets.fromLTRB(0.0, 20.0, 0.0, 15.0),
      child: ListView(
        children: list,
        shrinkWrap: true,
        physics: NeverScrollableScrollPhysics(),
      ),
    );

    return Scaffold(
//      appBar: AppBar(
//        title: Text('Layout State 1'),
//      ),
      body: Center(
          child: Container(
              child: ListView(
        children: <Widget>[row1, row2, row3],
        shrinkWrap: true,
      ))),
    );
  }
}
