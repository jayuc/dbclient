# dbclient
把数据库客户端做成一个web服务，在浏览器中访问数据库
## 背景
在实际开发中会使用到许多数据库，比如：oracle mysql redis mongodb等等。这样就会在电脑上安装对应的客户端连接数据库，久而久之客户端越装越多。为了解决这个问题，我想到可以做一个连接数据库的服务端跑在web服务器上，这样可以通过浏览器连接到各个数据库，使用浏览器就可以实现简单查询数据库的功能。
## 项目简介
项目为一个web项目，包括后台(webServer)和前端(webFront)两个子项目。后台是java + springboot项目；前端是vue + element-ui项目。
界面风格：
## 开发
### 后台
环境：java1.8  maven</br>
入口文件：com.github.jayuc.dbclient.DbclientApplication</br>
启动：java运行入口文件即可</br>
***注意***：*配置文件/src/main/resources/application.properties 中的 spring.profiles.active=dev 开发时必须配置为dev,否则会出现跨域异常，生成环境请配置prod。*</br>
### 前端
环境：javascript  nodejs  npm</br>
初始化项目：执行 `npm install`</br>
启动：执行package.json中script中的 `node start`</br>
配置服务ip:/static/config.js 中的 restRoot 配置成后台服务ip地址</br>
## 部署
*步骤*：
1.打包前端项目：执行 `node build` ，执行成功后会在/dist/目录下面出现index.html文件和static文件夹。
2.打包后台项目：将前端项目打包的index.html和static文件夹复制到后台/src/main/resources/static/文件夹下面执行 `mvn package`，得到dbclent.xx.xx.jar。
3.运行项目： `java -jar dbclent.xx.xx.jar`
## docker镜像