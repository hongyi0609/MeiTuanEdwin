import 'package:flutter/material.dart';

class SimpleInteractiveManagerDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
      title: 'Interactive',
      home: Scaffold(
        appBar: AppBar(
          title: Text('Simple Interactive'),
        ),
        body: Container(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              TapBoxA(),
              ParentWidget(),
              HybridParentWidget()
            ],
          ),
          alignment: Alignment.center,
          padding: EdgeInsets.all(12.0),
        ),
      ),
    );
  }
}

/// 独自管理交互状态
class TapBoxA extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return TapBoxAState();
  }
}

class TapBoxAState extends State<TapBoxA> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return GestureDetector(
      onTap: _handleTap,
      child: Container(
        child: Center(
          child: Text(
            _active ? 'Active' : "Inactive",
            style: TextStyle(fontSize: 32.0, color: Colors.white),
          ),
        ),
        width: 200.0,
        height: 200.0,
        decoration: BoxDecoration(
            color: _active ? Colors.lightBlue[700] : Colors.lightGreen[600]),
      ),
    );
  }

  bool _active = false;

  void _handleTap() {
    setState(() {
      _active = !_active;
    });
  }
}

/// 父组件管理交互状态
class ParentWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return ParentWidgetState();
  }
}

class ParentWidgetState extends State<ParentWidget> {
  bool _active = false;
  void _handleTapBoxChanged(bool newValue) {
    setState(() {
      _active = !_active;
    });
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Container(
      child: TapBoxB(
        active: _active,
        onChanged: _handleTapBoxChanged,
      ),
      margin: EdgeInsets.only(top: 12.0),
    );
  }
}

class TapBoxB extends StatelessWidget {
  TapBoxB({Key key, this.active: false, @required this.onChanged})
      : super(key: key);
  final bool active;
  final ValueChanged<bool> onChanged;
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return GestureDetector(
      onTap: _handleTap,
      child: Container(
        child: Center(
          child: Text(
            active ? 'Active' : 'Inactive',
            style: TextStyle(fontSize: 32.0, color: Colors.white),
          ),
        ),
        width: 200.0,
        height: 200.0,
        decoration: BoxDecoration(
            color: active ? Colors.lightGreen[700] : Colors.grey[600]),
      ),
    );
  }

  void _handleTap() {
    onChanged(!active);
  }
}

/// 混合管理状态的交互模式
class HybridParentWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return HybridParentState();
  }
}

class HybridParentState extends State<HybridParentWidget> {
  bool _active = false;
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Container(
      margin: EdgeInsets.only(top: 12.0),
      child: TapBoxC(active: _active, onChanged: _onHandleTapBoxChanged),
    );
  }

  void _onHandleTapBoxChanged(bool newValue) {
    setState(() {
      _active = newValue;
    });
  }
}

class TapBoxC extends StatefulWidget {
  TapBoxC({Key key, this.active, @required this.onChanged}) : super(key: key);
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return TapBoxCState();
  }

  final bool active;
  final ValueChanged<bool> onChanged;
}

class TapBoxCState extends State<TapBoxC> {
  bool _highLight = false;
  @override
  Widget build(BuildContext context) {
    // This example adds a green border on tap down.
    // On tap up, the square changes to the opposite state.
    return GestureDetector(
      onTapDown: _handleTapDown,
      onTapUp: _handleTapUp,
      onTap: _handleTap,
      onTapCancel: _handleTapCancel,
      child: Container(
        child: Center(
          child: Text(
            widget.active ? 'Active' : 'Inactive',
            style: TextStyle(fontSize: 32.0, color: Colors.white),
          ),
        ),
        width: 200.0,
        height: 200.0,
        decoration: BoxDecoration(
            color: widget.active ? Colors.lightGreen[700] : Colors.red[400],
            border: _highLight
                ? Border.all(color: Colors.teal[700], width: 10.0)
                : null),
      ),
    );
  }

  void _handleTapDown(TapDownDetails details) {
    setState(() {
      _highLight = true;
    });
  }

  void _handleTapUp(TapUpDetails details) {
    setState(() {
      _highLight = false;
    });
  }

  void _handleTap() {
    widget.onChanged(!widget.active);
  }

  void _handleTapCancel() {
    setState(() {
      _highLight = false;
    });
  }
}
