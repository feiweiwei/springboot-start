# SpringBoot入门最详细教程
网上有很多springboot的入门教程，自己也因为项目要使用springboot，所以利用业余时间自学了下springboot和springcloud，使用下来发现springboot还是挺简单的，体现了极简的编程风格，大部分通用都是通过注解就可以完成，下面就来详细讲解下如何使用springboot来开发一个简单的restful api网关功能，可以提供给H5或者android、ios进行接口开发，还是很方便的。
## 1. 使用spring initialization创建SpringBoot项目
有很多方法可以快速创建Springboot项目，可以通过idea的springboot initialization来创建，也可以通过手工新建一个maven工程，然后引入springboot的dependency来完成sprignboot的工程导入，还可以通过spring官网的来创建springboot项目，因为有些同学可能没装idea，这里就通过官网的工程初始化指引来创建一个springboot空工程。
首先输入网址 [https://start.spring.io](https://start.spring.io)，打开后可以看到下图：  
![image](http://otxp3yk5p.bkt.clouddn.com/springcloud-start%E5%B1%8F%E5%B9%95%E5%BF%AB%E7%85%A7%202017-07-31%20%E4%B8%8A%E5%8D%8811.08.14.png)
在serch for dependency输入web，即可完成基本的restful接口网关的功能，如果要JPA或者oauth安全相关的组件，可以增加rest repository、spring security等相关组件依赖库，spring提供的配套组件还是很多的，基本涵盖了所有应用场合。  
加入web组件后，点击下方的绿色按钮Generate Project即可创建一个springboot工程，并且自动下载到本地，接下来直接在idea或者eclipse打开该工程就可以了，在创建的时候可以选择Maven工程或者Gradle工程，这里我们使用了大家比较熟悉的Maven工程。  
## 2. 工程结构
下面我们在ide中打开工程，这里使用的ide是idea，工程的目录结构为： 
![image](http://otxp3yk5p.bkt.clouddn.com/%E5%B1%8F%E5%B9%95%E5%BF%AB%E7%85%A7%202017-07-31%20%E4%B8%8B%E5%8D%882.35.09.png)  
可以看到工程中有maven的pom文件，也自动创建了SpringbootStartApplication.java该类为springboot的启动类，待会儿我们一起看下这个类，先看下maven的pom文件有哪些。这里主要是依赖了springboot的1.4.7版本，目前最新已经更新到1.5.6了，这里没有用最新版本，还是不当小白鼠了，在dependency中依赖了spring-boot-starter-web还有个test测试的组件，如果不写测试代码，可以不使用该test组件，最后还加入了支持springboot的maven plugin组件。

```
    <parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.4.7.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
```

刚看完了pom文件，在导入工程的时候，ide一般会自动导入依赖库，在国内访问maven的依赖库速度感人，建议使用阿里云的maven镜像服务器，或者使用公司的maven私服，如果公司没有私服或者自己学习可以直接使用阿里云的镜像速度还是不错的，maven setting.xml中需要添加mirror地址，具体如何配置这里就不详细描述了，可以自行百度，这里也顺便附上阿里云maven地址：

```
<mirror>
    <id>nexus-aliyun</id>
    <mirrorOf>*</mirrorOf>
    <name>Nexus aliyun</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public</url>
</mirror>
```
配置好了pom后，我们一起看下自动生成的Application.java这个类相当于我们程序的main函数入口，这里再顺便介绍下因为springboot集成了Tomcat和Jetty，默认使用Tomcat作为应用容器，开发者只需要将工程打成jar包直接丢到服务器上就可以执行了，不需要再单独部署到was、jboss、tomcat这些应用服务器上。  
SpringBootStartApplication.java

```
@SpringBootApplication
public class SpringbootStartApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootStartApplication.class, args);
	}
}
```
所有的springboot application启动类都需要在类级别上加上@SpringBootApplication注解，其他参数不用任何调整，后续可以把一些初始化的动作放到该类中进行，目前本例中就不加其他的启动加载项了。  
这样一个api网关的架子就搭好了，是不是很简单！下面我们就可以将主要精力聚焦在业务逻辑代码上了，这里为了简化程序，不会将项目进行深入的分层设计，在实际项目中，一般都会对项目进行分层设计，如果是api网关，没有view层但是起码也会有对外接入decontroller层、处理业务逻辑的service层、处理数据持久化的dao层，同时也会有一些POJO业务实体类，这里就不详细展开了，后续也会对互联网架构设计进行详细讲述，这里我们只创建了一个UserController类，里面只有获取用户信息的方法，分别根据参数和请求方式的不同用三种方法进行了重写，下面就来一一道来。  


先来讲述下最简单的使用get请求用户信息的实现方式，代码如下,写好后直接在Application类点击右键有个RunAs，点击后会自动运行，运行成功后可以使用http发包工具进行测试，这里推荐使用chrome的postman或者使用firefox的httprequester插件，都是比较简单的发包工具，get请求的上送为http://localhost:8081/springboot/getUserByGet?userName=feiweiwei
```
//@RestController注解能够使项目支持Rest
@RestController
@SpringBootApplication
//表示该controller类下所有的方法都公用的一级上下文根
@RequestMapping(value = "/springboot")
public class UserController {
    //这里使用@RequestMapping注解表示该方法对应的二级上下文路径
    @RequestMapping(value = "/getUserByGet", method = RequestMethod.GET)
    String getUserByGet(@RequestParam(value = "userName") String userName){
        return "Hello " + userName;
    }
}
```
这里用到的注解主要有@RequestMapping表示请求的URL上下文路径，该路径不能重复，为了保证与团队其他同事写的不重复，一般会在每个controller前面加一个一级上下文目录，具体路径参数放在value后面，在每个方法前加一个二级目录，这样可以有效的避免路径冲突。还有注解是@RequestParam，该注解可以通过value指定入参，这里return的返回值就是实际的接口返回。

下面介绍下POST的请求方式，可以通过在@RequestMapping注解中设置method为POST来表示该请求为POST请求，除了get、post还有put、delete等请求方式，都可以通过该参数设置。
```

    //通过RequestMethod.POST表示请求需要时POST方式
    @RequestMapping(value = "/getUserByPost", method = RequestMethod.POST)
    String getUserByPost(@RequestParam(value = "userName") String userName){
        return "Hello " + userName;
    }
```
下面介绍下请求参数为JSON格式的请求方法的写法，这里需要注意下如果请求参数是像上面那样通过url form形式提交的请求参数，那么必须使用@RequestParam注解来标示参数，如果使用的请求报文是POST形势的JSON串，那么这里在入参的注解一定要使用@RequestBody，否则会报json解析错误。

```

    //在入参设置@RequestBody注解表示接收整个报文体，这里主要用在接收整个POST请求中的json报文体，
    //目前主流的请求报文也都是JSON格式了，使用该注解就能够获取整个JSON报文体作为入参，使用JSON解析工具解析后获取具体参数
    @RequestMapping(value = "/getUserByJson",method = RequestMethod.POST)
    String getUserByJson(@RequestBody String data){
        return "Json is " + data;
    }
```
## 3. 小结
到此一个简单的restful风格的api网关就完成了，对于移动开发人员可以自己写简单的服务端进行全栈开发了，原来做spring的同学也可以很快上手springboot，springboot总体上来说还是简化了原先复杂的配置，让大家更容易快速上手和搭建服务端，代码的git地址在下方，欢迎大家下载，谢谢。

git代码地址：[https://github.com/feiweiwei/springboot-start.git](https://github.com/feiweiwei/springboot-start.git)
