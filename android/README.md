# 爱佳仁 App is developed With React-Native(0.50.1)、Flutter and Android. Otherwise, it follows from huanxsd's MeiTuan Project in React Native section.

## 项目特色
### React Native
RN通过两种方式引入原生框架：
1. RN工程作为一个单独的View，理论上可以渲染到任何使用View的原生ViewGroup容器中，项目中的ReactNativeFragment便是一个示例。由于降低了RN与Native的耦合，很适合大型项目的开发，原生端、RN端三个团队相互合作，可以极大地提高开发效率。项目中RN过程被打包成index.android.bundle存放于app/src/main/assets/react目录下便是一个例子
  bundle打包命令：react-native bundle --platform android --dev false --entry-file index.js --bundle-output ./android/app/src/main/assets/react/index.android.bundle --assets-dest ./android/app/src/main/res/
  其中，在与RN交互的过程中，Android原生端「以下简称Native」引入了EventBus库。原生端接收到来自RN的消息后，通过EventBus可以很方便的将消息通知给各个组件

2. Native端作为一个容器，整个RN都是基于这个容器的开发。这里不用关心RN和Native的交互，一经创建整个工程就可以很方便的基于RN开发。由于RN代码是基于ES6语法的，这对于一些需求不大的小工程特别适合，尤其是h5开发资源丰富，Native开发资源紧张的公司。

  以上两种框架直接不存在相互依赖，但是为了节约开发成本，复用了HomeReactPackage。

## Flutter
   Flutter跟RN类似，也是基于React框架的跨平台框架实现。项目中对flutter的引入也包括两种方式：
1. 同样是考虑的跨端开发的成本问题，在一些大型项目中去耦合是必须的，项目中通过在引入aar文件依赖的方式，引入Flutter项目。值得庆幸的是，新版的Flutter不需要针对编译生成flutter-release.aar包做二次打包，因为它内部已经包括了Flutter虚拟机环境，这种开发模式在master分支实现
2. flutter代码的开发是基于Dart语言的，该语法跟ES6及其相似。状态、属性、弹性布局等元素一应俱全。特别照顾了大家的学习成本。在开发过程中，直接按照[官方教程](https://flutter.dev/docs/get-started/install)引入就好了。另外，为了能够使Flutter虚拟机一直处于激活状态，需要把虚拟机环境依赖文件导入到原生应用的assets中，根项目中的copyFlutterAssets任务就是为了这个目的而存在的。这种开发模式在develop分支实现

  其实以上两种方式，可以通过一个分支搞定，也就一个开关的事儿。

3. 为了调试方便，项目中还创建了一个完全孤立的flutter_show项目，这是一个纯Flutter项目，便于调试和热加载，仅用于初期学习

## Android
  项目创建初期，原生端是基于Java开发的。后来为了追潮流，转而使用kotlin语言进行原生开发。在公司的项目开发过程中，突然发现有人使用RxJava，搞得我很不爽，就在项目中引入了RxJava开发把它的操作符露了一遍。在研究kotlin的协程时才发现，RxJava也被直接扩充到kotlinx.coroutines的相关相应模块中。随着开发的进行，要做进一步的优化和kotlin化、Flutter化、潮流化。

## branches
1. master分支，默认分支
2. develop分支，开发分支
目前这两个分支的主要区别在于，flutter的引入方式不同。前者依赖aar，后者依赖flutter模块

## aar打包
    flutter build apk
    切换到flutter目录下，执行以上命令，在MeiTuan/.android/Flutter/build/outputs/aar/目录下生成aar，把这个aar拷出来直接放到你原生项目里取依赖便可以摆脱依赖module的麻烦。


## screen shot for Android

<!--<img src="https://github.com/huanxsd/MeiTuan/blob/master/screenshot/Android_0.png">-->

<!--<img src="https://github.com/huanxsd/MeiTuan/blob/master/screenshot/Android_1.png">-->

<!--<img src="https://github.com/huanxsd/MeiTuan/blob/master/screenshot/Android_2.png">-->

## Setup

1. **Clone the repo**

```
$ git clone git@github.com:hongyi0609/MeiTuanEdwin.git
$ cd MeiTuanEdwin
$ git checkout -b local
```

2. **Install dependencies** (npm v3+):

```
$ npm install
```


3. **Running on iOS:**

```
$ react-native run-ios
```

## Troubleshooting

> Could not connect to development server

In a separate terminal window run:

```
$ react-native start
```

## Dependency

* [react-navigation](https://github.com/react-community/react-navigation)
* [react-native-scrollable-tab-view](https://github.com/skv-headless/react-native-scrollable-tab-view)

## Contact

<!-- If you have any suggestions, leave a message here
[简书](http://www.jianshu.com/p/9211f42d5c25) -->

## At last

If you like this project, please give me a star  :)

## 第三方依赖

* [react-navigation](https://github.com/react-community/react-navigation)
* [react-native-scrollable-tab-view](https://github.com/skv-headless/react-native-scrollable-tab-view)

## 安装

1. **Clone the repo**

```
$ git clone git@github.com:hongyi0609/MeiTuanEdwin.git
$ cd MeiTuanEdwin
$ git checkout -b local
```

2. **Install dependencies** (npm v3+)

```
$ npm install
```

3. **Running on iOS**

```
$ react-native run-ios
```

## 常见问题

> Could not connect to development server

打开新的terminal窗口，并执行:

```
$ react-native start
```

## 说点啥

     我之前一直在写Android，2018年开始接触React Native编程，一直在学习和写demo。19年618期间开始正式使用ES6的语法和Flex布局方式写小程序，整体感觉这种语法相较于原生简介了不少。618之后正式接手京东店铺Android端开发，京东店铺是基于Android/ios和RN的混合编程开发，本人主要负责React Native和Android的联调及Android原生页的开发任务。
     2020年伊始，Flutter开发甚嚣尘上。集团也开始逐步推广基于Flutter的跨平台业务，我便在项目中引入了Flutter过程，同时也开始kotlin开发的旅程。
     项目中已经不再是原来的的React Native项目了，基本脱离了原开发框架，于是更名为"爱佳仁" 。
     该项目借助MeiTuan项目，融入本人在开发过程中的经验和教训，如果能给hybrid开发的朋友提供一些帮助和指引，那便是我最开心的事了~

如果对这个Demo有任何的意见或建议，欢迎在下方留言。我会在第一时间回复 :)


## 最后

如果你喜欢我的方案，请给我一个star   :)

Github: https://github.com/hongyi0609/MeiTuanEdwin

否则，请给下面的demo一个star  :)

Github：https://github.com/huanxsd/MeiTuan

## 鸣谢
 Thank huanxsd's contributions in platform [github](https://github.com/huanxsd/MeiTuan/blob/master) and [简书](http://www.jianshu.com/p/9211f42d5c25)