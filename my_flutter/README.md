# my_flutter

A new flutter module project.

## 项目指导
1. 简书对my_flutter 的解释说明：
https://www.jianshu.com/p/dd6de90f9e2a?v=1705760662519
2. 准备转战掘金了，文章也同步了一份
https://juejin.cn/post/7325717778597625892
3. aliyun.properties
阿里云效username 和 password 存储在本地，要自己创建。**千万注意不要加引号**
4. artifact 配置上传指定文件
https://stackoverflow.com/questions/26874498/publish-an-android-library-to-maven-with-aar-and-sources-jar


## Getting Started

For help getting started with Flutter, view our online
[documentation](https://flutter.dev/).

Add Flutter to an existing app
[documentation](https://docs.flutter.dev/add-to-app)

## 版本升级

命令： flutter build aar --build-number 1.1

debug: flutter build aar --build-number [version_name] --no-profile --no-release

参考：https://juejin.cn/post/6867705799293009927

profile：在 Android 开发中，"profile" 版本通常指的是应用的性能分析版本。这种版本允许开发者在设备上运行应用程序以进行性
能分析和调试。这种版本通常会包含更多的日志记录和性能分析工具，用于分析应用在不同设备上的性能表现，并检测可能存在的性能问题。
Flutter 构建过程中的 --no-profile 标志会指示系统不生成这种性能分析版本。这样做是为了加快构建过程并减小生成的输出文件大小。

### 发布

更改gradle_script="
your_flutter_sdk_parent_dir/flutter/packages/flutter_tools/gradle/aar_init_script.gradle"
文件下的代码添加发布配置
```
    project.publishing {
        repositories {
            maven {
                url = uri("file://${outputDir}/outputs/repo")
            }
            maven {
                url = 'https://packages.aliyun.com/maven/repository/2443959-snapshot-5T0GnA/'
                credentials {
                    username = '***'
                    password = '***'
                }
            }
```


