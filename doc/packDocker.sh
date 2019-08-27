#!/bin/bash

set -eo pipefail

suf_path=webServer/target/
# 代码路径
code_path=./
# 输出路径
out_path="${code_path}${suf_path}"

if [ $1 ]; then
  out_path=$1
fi

# functions

# step 1:
# 获取代码
if [ -e ../webServer/pom.xml -a -e ../webFront/package.json ]; then
  code_path=../
  out_path="../${suf_path}"
else
  echo ""     
fi


echo "代码根路径: $code_path"
echo "输出路径: $out_path"
