import 'package:english_words/english_words.dart';
import 'package:flutter/material.dart';

class MyListViewDemo extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: 'Startup Name Generator',
      theme: ThemeData(primaryColor: Colors.red),
      home: RandomWords(),
    );
  }
}

class RandomWords extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return RandomWordsState();
  }
}

class RandomWordsState extends State<RandomWords> {
  /// final wordPair = WordPair.random(); // 随机获取英文字符串
  final _suggestions = <WordPair>[];
  final _saved = Set<WordPair>(); //收藏单词对集合
  final _biggerFont = const TextStyle(fontSize: 18.0);

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    /// 在RandomWordsState的build方法中为AppBar添加一个列表图标。当用户点击列表图标时，包含收藏夹的新路由页面入栈显示。
    return Scaffold(
      appBar: AppBar(
        title: Text('Start Name Generator'),
        actions: <Widget>[
          IconButton(icon: Icon(Icons.list), onPressed: _pushSaved,),
        ],
      ),
      body: _buildSuggestions(),
    );
  }

  /// 点击push路由导航
  void _pushSaved() {
    Navigator.of(context).push(
        MaterialPageRoute(
          builder: (context) {
            final tiles = _saved.map(
                    (pair) {
                  return ListTile(
                    title: Text(pair.asPascalCase, style: _biggerFont,),
                  );
                }
            );
            final divided = ListTile
                .divideTiles(
                tiles: tiles,
                context: context
            ).toList();

            return Scaffold(
              appBar: AppBar(
                title: Text('Saved Suggestions'),
                centerTitle: true,
                backgroundColor: Colors.lightBlue,
              ),
              body: ListView(children: divided,),
            );
          },
        )
    );
  }

  Widget _buildSuggestions() {
    /// ListView类提供了一个builder属性，itemBuilder 值是一个匿名回调函数， 接受两个参数- BuildContext
    /// 和行迭代器i。迭代器从0开始， 每调用一次该函数，i就会自增1，对于每个建议的单词对都会执行一次。该模型允许
    /// 建议的单词对列表在用户滚动时无限增长。
    return ListView.builder(
        padding: const EdgeInsets.all(16.0),

        itemBuilder: (context, i) {
          if (i.isOdd) return Divider(); //奇数行加分割线

          final index = i ~/ 2; // 向下取整
          if (index >= _suggestions.length) {
            // ...接着再生成10个单词对，然后添加到建议列表
            _suggestions.addAll(generateWordPairs().take(10));
          }
          return _buildRow(_suggestions[index]);
        }
    );
  }

  Widget _buildRow(WordPair pair) {
    final hasSaved = _saved.contains(pair);

    return ListTile(
      title: Text(pair.asPascalCase, style: _biggerFont,),
      // 添加点赞图标
      trailing: Icon(hasSaved ? Icons.favorite : Icons.favorite_border,
        color: hasSaved ? Colors.red : null,),
      onTap: () {
        setState(() {
          hasSaved ? _saved.remove(pair) : _saved.add(pair);
        });
      },
    );
  }
}