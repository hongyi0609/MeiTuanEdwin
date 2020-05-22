import 'package:flutter/material.dart';

class PainterDemo extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
        appBar: AppBar(
          title: Text("signature"),
        ),
        body: Signature(),
    );
  }
}

class CustomTitle extends StatelessWidget {
  final String label;
  CustomTitle(this.label);
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return RaisedButton(onPressed: () {

    }, child: Text(label));
  }
}

class Signature extends StatefulWidget {
  SignatureState createState() => SignatureState();
}

class SignatureState extends State<Signature> {
  List<Offset> _points = <Offset>[];
  RenderBox renderBox;
  Offset offset;

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return GestureDetector(
      child: CustomPaint(
        painter: SignaturePrinter(_points),
        size: Size.infinite,
      ),
      onPanUpdate: (DragUpdateDetails details){
        setState(() {

          renderBox = context.findRenderObject();
          offset = renderBox.globalToLocal(details.globalPosition);
          _points = List.from(_points)..add(offset);
        });
      },
      onPanEnd: (DragEndDetails details) => _points.add(null),
    );
  }

}

class SignaturePrinter extends CustomPainter {
  SignaturePrinter(this.points);

  final List<Offset> points;

  @override
  void paint(Canvas canvas, Size size) {
    // TODO: implement paint
    var paint = new Paint()
      ..color = Colors.black
        ..strokeCap = StrokeCap.round
        ..strokeWidth = 5.0;
    for(int i = 0; i < points.length - 1; i++){
      if(points[i] != null && points[i+1] != null){
        canvas.drawLine(points[i], points[i+1], paint);
      }
    }
  }

  @override
  bool shouldRepaint(SignaturePrinter oldDelegate) {
    // TODO: implement shouldRepaint
    return oldDelegate.points != points;
  }

}