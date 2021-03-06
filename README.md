# dbclient
把数据库客户端做成一个web服务，在浏览器中访问数据库
## 背景
在实际开发中会使用到许多数据库，比如：oracle mysql redis mongodb等等。这样就会在电脑上安装对应的客户端连接数据库，久而久之客户端越装越多。为了解决这个问题，我想到可以做一个连接数据库的服务端跑在web服务器上，这样可以通过浏览器连接到各个数据库，使用浏览器就可以实现简单查询数据库的功能。
## 项目简介
项目为一个web项目，包括后台(webServer)和前端(webFront)两个子项目。后台是java + springboot项目；前端是vue + element-ui项目。</br>
界面风格：</br>
![登陆](https://github.com/jayuc/dbclient/blob/master/profile/profile1.png)</br>
![主页](https://github.com/jayuc/dbclient/blob/master/profile/p2.png)</br>
![json页](https://github.com/jayuc/dbclient/blob/master/profile/p3.png)</br>
## 开发
### 后台
环境：java1.8  maven</br>
入口文件：com.github.jayuc.dbclient.DbclientApplication</br>
启动：java运行入口文件即可</br>
***特别说明***：*1.maven依赖的 jar包（oracle和 postgresql）需要从器官网上下载。2.另外需要安装 lombok插件。*</br>
***注意***：*配置文件/src/main/resources/application.properties 中的 spring.profiles.active=dev 开发时必须配置为dev,否则会出现跨域异常，生成环境请配置prod。*</br>
### 前端
环境：javascript  nodejs  npm</br>
初始化项目：执行 `npm install`</br>
启动：执行package.json中script中的 `npm start`</br>
配置服务ip:/static/config.js 中的 restRoot 配置成后台服务ip地址</br>
## 部署
**一键部署**：<br>
直接运行 `/doc/packDocker.sh`, 详细见 /doc/部署说明.txt</br>
**分步骤部署**：</br>
1.打包前端项目：执行 `npm build` ，执行成功后会在/dist/目录下面出现index.html文件和static文件夹。把/static/config.js 中的 restRoot 改为"/"</br>
2.打包后台项目：将前端项目打包的index.html和static文件夹复制到后台/src/main/resources/static/文件夹下面执行 `mvn package`，得到dbclent.xx.xx.jar。</br>
***注意***：*配置文件/src/main/resources/application.properties 中的 spring.profiles.active=dev 修改为prod。*</br>
3.运行项目： `java -jar dbclent.xx.xx.jar`</br>
## docker镜像
`docker run -d -p 8004:8004 -v /home/jayu/log/dbclient:/app/log db/client:v1.0.0`</br>
***说明***：*/app/log为日志所在文件夹*</br>
[下载镜像](https://pan.baidu.com/s/1CrGizypw0Le0x-HFws5YMw) 提取码：0d9q</br>
[镜像制作过程](https://www.cnblogs.com/jayu/p/10873573.html)</br>
