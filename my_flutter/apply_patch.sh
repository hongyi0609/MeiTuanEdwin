#!/bin/bash
# 获取当前脚本所在目录的绝对路径
SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
echo "$SCRIPT_DIR"
# 读取用户输入的构建版本号：1.0.2
read -p "请输入构建版本号: " BUILD_NUMBER

# 清理旧的构建输出
flutter clean
# 构建Flutter项目
flutter build aar --build-number "$BUILD_NUMBER" --no-profile --no-release -v > build.log 2>&1

if [ $? -ne 0 ]; then
  echo "构建失败！查看 build.log 获取更多信息。"
  exit 1
fi

# 替换 publish.gradle 中的版本号
OLD_VERSION=$(grep -o "version '[0-9]\+\.[0-9]\+\.[0-9]\+'" "$SCRIPT_DIR/publish.gradle" | head -n 1)
if [ -z "$OLD_VERSION" ]; then
  echo "无法获取旧版本号！"
  exit 1
fi

sed -i'.bak' "s/$OLD_VERSION/version '$BUILD_NUMBER'/" "$SCRIPT_DIR/publish.gradle" || { echo "版本号替换失败！"; exit 1; }

# 切换到Flutter项目的根目录
cd "$SCRIPT_DIR/.android/Flutter" || exit
# 在Flutter/build.gradle文件中执行命令:
# 1）在android配置项前插入 apply 发布应用
# 2）-i '.bak'，备份原始文件
sed -i '.bak' '/^android {/i\
def currentTask = gradle.startParameter.taskNames.join(" ") \
println("currentTask =" + currentTask)\
if (!currentTask.contains("Aar")) {\
    apply from: "../../publish.gradle"\
}' "$SCRIPT_DIR/.android/Flutter/build.gradle"

# 切换到 ./android 目录下执行发布任务
cd "$SCRIPT_DIR/.android" || exit
./gradlew publish --info > publish.log 2>&1
if [ $? -ne 0 ]; then
    echo "发布任务失败！查看 publish.log 获取更多信息。"
    exit 1
fi