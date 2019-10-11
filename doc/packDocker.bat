@ECHO OFF

@REM 打包批处理脚本

echo process start ... 

@REM 项目名
set project_name=dbclient
set server_name=webServer
set front_name=webFront
@REM 项目路径
set project_path=./%project_name%
@REM 项目版本号
set project_version=1.0.0

if "%1" == "" (
	echo Error: the mirror version number must be filled in. The first parameter is the project version number.
	goto end
) else (
	set project_version=%1
)

@REM step 1/5:
@REM 获取代码
echo step 1/5: fetch code
if exist ../%server_name%/pom.xml (
	set project_path=../
	echo code already exists
) else (
	if exist %project_path%/%server_name%/pom.xml (
		echo code already exists
	) else (
		call git clone https://github.com/jayuc/%project_name%.git
	)
)

cd %project_path%

@REM step 2/5:
@REM 编译前端代码
echo setp 2/5: web front pack
if exist ./%front_name%/node_modules (
	echo node modules already exists
) else (
	echo node modules is not exist, start npm install ...
	cd ./%front_name%
	call npm install
	cd ../
)

echo start node build ...
cd %front_name%
call node ./build/build.js
cd ../

@REM step 3/5:
@REM 配置
echo setp 3/5: config
copy /y .\doc\config.js .\%front_name%\dist\static\
copy /y .\doc\application.properties .\%server_name%\src\main\resources\
xcopy .\%front_name%\dist\*.* .\%server_name%\src\main\resources\static\ /s /e /y

@REM setp 4/5:
@REM 编译项目
echo setp 4/5: mvn package
cd %server_name%
call mvn clean package -Dmaven.test.skip=true

@REM setp 5/5:
@REM 构建docker镜像
@REM 注意：DockerFile 中的版本号要配置和项目版本号一致
echo setp 5/5: docker build
copy /y ..\doc\Dockerfile .\target\
copy /y ..\doc\application.properties .\target\
cd ./target/
call docker build -t db/client:%project_version% .
call docker save db/client:%project_version% -o dbclient_%project_version%.jar

echo "finshed."

@REM 结束
:end
