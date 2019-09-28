#!/bin/bash

#set -eo pipefail

echo "process start ... "

suf_path=webServer/target/
# 代码路径
code_path=./dbclient/
# 输出路径
out_path="${code_path}${suf_path}"
# 版本号
m_version=1.0.0

if [ $1 ]; then
  m_version=$1
else
  echo "镜像版本号必填，第一个参数是镜像版本号"
  exit 5
fi

if [ $2 ]; then
  out_path=$2
fi

echo "step 1/5: fetch code"
# step 1/5:
# 获取代码
if [ -e ../webServer/pom.xml -a -e ../webFront/package.json ]; then
  code_path=../
  out_path="../${suf_path}"
  echo "code already exists"  
else
  if [ -e ./dbclient/webServer/pom.xml -a -e ./dbclient/webFront/package.json ]; then
    echo "code already exists"
  else
    git clone https://github.com/jayuc/dbclient.git
  fi
fi

echo "setp 2/5: web front pack"
# step 2/5:
# 编译前端代码
cd "${code_path}webFront/"
if [ -d "node_modules" ]; then
  echo "node modules already exists"
else
  echo "node modules is not exist, start npm install ..."
fi

npm install
echo "start node build ..."
node ./build/build.js

# step 3/5:
# 配置
echo "setp 3/5: config"
cd ../
cp ./doc/config.js ./webFront/dist/static/config.js
cp ./doc/application.properties ./webServer/src/main/resources/
cp -r ./webFront/dist/. ./webServer/src/main/resources/static/

# setp 4/5:
# 编译项目
echo "setp 4/5: mvn package"
cd ./webServer/
mvn clean package -Dmaven.test.skip=true

# setp 5/5:
# 构建docker镜像
# 注意：DockerFile 中的版本号要配置和项目版本号一致
echo "setp 5/5: docker build"
cp ../doc/Dockerfile ./target/
cp ../doc/application.properties ./target/
cd ./target/
docker build -t "db/client:${m_version}" .
docker save "db/client:${m_version}" -o "dbclient_${m_version}.jar"

echo "finshed."
