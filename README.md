# 大型SpringCloud微服务架构项目实战




# 1 课程内容介绍

​	本课程是燕青老师录制的最经典的大型分布式系统学成在线项目视频，采用SpringCloud微服务架构开发，共有20天，课程系统讲解了Java微服务开发技术和解决方案。

​	学成在线项目借鉴MOOC的设计思想，是提供IT职业在线课程的学习平台，为即将和已经加入IT领域的技术人才提供在线学习服务。用户以任务为导向，通过录播学习、直播学习、在线练习、在线考试等学习形式，掌握并熟练应用IT技能。项目包括门户、学习中心、教学管理中心、社交系统、系统管理中心等平台构成，为学生提供学、练、测一体化的学习服务。

![1566013794794](http://www.pbteach.com/post/java_distribut/project_xc_yq/1566013794794.png)

技术架构图：

![1-framework](http://www.pbteach.com/post/java_distribut/project_xc_yq/1-framework.jpg)

​	学成在线项目按照大型互联网分布式系统的要求进行设计，采用前后端分离的技术架构，前端采用当前流行的Vue.js技术栈进行构建，服务端基于Spring Boot框架，采用Spring Cloud微服务架构、Spring MVC、Spring Data JPA、Spring Security Oauth2、Spring AMQP等Spring全家桶技术栈进行设计开发。项目还采用了Maven、Git/GitLab、Docker等流行的DevOps工具，有力的支撑项目的整个开发和运维过程。

1、 基于SpringCloud微服务技术开发
项目是基于SpringCloud构建微服务架构，应用了Eureka、Feign、Zuul等众多SpringCloud组件。

2、 RabbitMQ消息列队
项目中使用RabbitMQ完成异步消息通信，业务场景包括：CMS页面发布、分布式事务控制、课程发布等。

3、 Logstash+ElasticSearch 全文检索
项目中使用ES完成课程、师资信息的检索，采用Logstash数据采集组件完成源信息索引。

4、 Spring Security Oauth2+JWT
项目使用Spring Security Oauth2+JWT完成认证授权业务功能。

5、 FastDFS+GridFS分布式文件系统
项目中采用FastDFS存储图片、css等小文件，作为图片服务器、门户资源服务器。

项目中采用GridFS存储CMS页面文件，作为CMS文件服务器。

6、 Nuxt.js服务端渲染
项目中采用Nuxt.js服务端渲染技术完成课程搜索、动态信息展示等功能。

7、 SpringTask+FFmpeg+Nginx+Video.js+ HLS视频处理及点播技术方案

项目中使用SpringTAsk+FFmpeg完成视频的编码处理，生成m3u8文件。

项目中使用Nginx部署视频点播服务器，使用Video.js基于HLS协议完成视频在线点播。

8、 大文件断点续传技术  WebUploader

项目中采用 百度开源组件 WebUploader完成媒资文件的分块上传，实现断点续传功能。

9、 SpringTask+MQ完成分布式事务控制解决方案

项目中采用SpringTask+MQ消息队列完成分布式事务最终一致性控制。

10、 微信扫码支付
项目采用微信扫码支付技术完成收费课程订单支付。

11、 虚拟化部署技术Docker
项目最终在Docker中部署、运行。

12、 Spring Data JPA+MyBatis结合
Spring Data JPA以其简便性著称，Spring Data JPA主要面向于对象，项目中使用Spring Data JPA完成对实体增、删、改、查操作。

MyBatis从Ibatis发展至今简单易用，功能强大，相比Spring Data JPA，MyBatis直接面向Sql语句，对复杂的SQL语句优化非常方便，项目使用MyBatis完成复杂SQL的查询操作。

13、 MongoDB+MySQL结合
项目中对于课程、用户等重要信息采用MySQL存储。
对于CMS、查询视图、配置信息等采用MongoDB，发挥它的非关系数据库灵活多变的特性。



# 2 课程讲义

http://www.pbteach.com/post/java_distribut/project_xc_yq_01/
http://www.pbteach.com/post/java_distribut/project_xc_yq_02/
http://www.pbteach.com/post/java_distribut/project_xc_yq_03/
http://www.pbteach.com/post/java_distribut/project_xc_yq_04/
http://www.pbteach.com/post/java_distribut/project_xc_yq_05/
http://www.pbteach.com/post/java_distribut/project_xc_yq_06/
http://www.pbteach.com/post/java_distribut/project_xc_yq_07/
http://www.pbteach.com/post/java_distribut/project_xc_yq_08/
http://www.pbteach.com/post/java_distribut/project_xc_yq_09/
http://www.pbteach.com/post/java_distribut/project_xc_yq_10/
http://www.pbteach.com/post/java_distribut/project_xc_yq_11/
http://www.pbteach.com/post/java_distribut/project_xc_yq_12/
http://www.pbteach.com/post/java_distribut/project_xc_yq_13/
http://www.pbteach.com/post/java_distribut/project_xc_yq_14/
http://www.pbteach.com/post/java_distribut/project_xc_yq_15/
http://www.pbteach.com/post/java_distribut/project_xc_yq_16/
http://www.pbteach.com/post/java_distribut/project_xc_yq_17/
http://www.pbteach.com/post/java_distribut/project_xc_yq_18/
http://www.pbteach.com/post/java_distribut/project_xc_yq_19/
http://www.pbteach.com/post/java_distribut/project_xc_yq_20a/
http://www.pbteach.com/post/java_distribut/project_xc_yq_20b/



# 3 课程视频资料

[关注公众号](http://www.pbteach.com/about/)，发送“ 传智燕青学成在线项目” 获取项目全部下载链接。



第1天：

链接：https://pan.baidu.com/s/1ab0UGmWZs2BNLZ1nIBFCcQ 
提取码：678y 

第2天：

链接：https://pan.baidu.com/s/1lkJ2vy1bITUOH9Nm4M17Jw 
提取码：mfxd 

第3天：

链接：https://pan.baidu.com/s/1tERf-6AA5jG63tYBnmawCA 
提取码：dlk2 

第4天：

链接：https://pan.baidu.com/s/1Hatm6N-ZNFDYaoRkDLqnrQ 
提取码：ftu3 

第5天：

链接：https://pan.baidu.com/s/1YXjxfnxP8h4DJiSmK4W-Bg 
提取码：qx5i 

第6天：

链接：https://pan.baidu.com/s/1cwzRJfwvFkF1eSncy2Ev3A 
提取码：swwx 

第7天：

链接：https://pan.baidu.com/s/1M6Za8O7fUVL5_dtPaleqYg 
提取码：d2ww 

第8天：

链接：https://pan.baidu.com/s/1aNUqIGz1Oc1k7hPy8rmmvQ 
提取码：af1x 

第9天：

链接：https://pan.baidu.com/s/1AmhWU-Fgd3RcmYYulcdRRg 
提取码：o74y 

第10天：

链接：https://pan.baidu.com/s/1Gg2b03wNTfv5NduRLrQvXw 
提取码：57ox 

第11天：

链接：https://pan.baidu.com/s/145B9LgVyt592PXb1_yLh3Q 
提取码：0m6k 

第12天：

链接：https://pan.baidu.com/s/1R4bBokS19v75lBgibS1Y3w 
提取码：2c0i 

第13天：

链接：https://pan.baidu.com/s/1iGmAWXYEnSqEMDozIftQGQ 
提取码：2j9u 

第14天：

链接：https://pan.baidu.com/s/1v5IF0PhaPDXdkccwVKD2lA 
提取码：xq8j 

第15天：

链接：https://pan.baidu.com/s/1EvOnZfDK0q3QiUnqGV1iag 
提取码：8qgy 

第16天：

[关注公众号](http://www.pbteach.com/about/)，发送“ 传智燕青学成在线项目” 获取项目全部下载链接。

第17天：

[关注公众号](http://www.pbteach.com/about/)，发送“ 传智燕青学成在线项目” 获取项目全部下载链接。

第18天：

[关注公众号](http://www.pbteach.com/about/)，发送“ 传智燕青学成在线项目” 获取项目全部下载链接。

第19天：

[关注公众号](http://www.pbteach.com/about/)，发送“ 传智燕青学成在线项目” 获取项目全部下载链接。

第20天：

[关注公众号](http://www.pbteach.com/about/)，发送“ 传智燕青学成在线项目” 获取项目全部下载链接。

