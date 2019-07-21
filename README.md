#### 创建一个初始化的父工程
* 首先使用IDEA创建一个Maven父工程，下面是父工程的pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.visiontalk</groupId>
    <artifactId>springcloud</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.1.6.RELEASE</version>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Greenwich.SR2</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

</project>
```
#### 创建一个注册中心
* 在父工程下创建Module，pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.visiontalk</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>child01</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
            <version>2.1.2.RELEASE</version>
        </dependency>
    </dependencies>

</project>
```
* 创建配置文件application.yml
```yml
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url: 
      defaultZone: http://127.0.0.1:${server.port}/eureka

spring:
  application:
    name: server-center
server:
  port: 8761
```
__配置属性说明__
---
`server.port`：当前`Eureka Server`服务端口。
`eureka.client.register-with-eureka`：是否将当前的`Eureka Server`服务作为客户端进行注册。
`eureka.client.fetch-fegistry`：是否获取其他 `Eureka Server`服务的数据。
`eureka.client.service-url.defaultZone`：注册中心的访问地址。
***
* 创建启动类
```java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```
`@SpringBootApplication`：声明该类是`Spring Boot`服务的入口。
`@EnableEurekaServer`：声明该类是一个`Eureka Server`微服务，提供服务注册和服务发现功能，即注册中心。
#### 创建一个服务提供者
在父工程下创建一个Module，pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>cn.visiontalk</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>childclient01</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <version>2.1.2.RELEASE</version>
        </dependency>
    </dependencies>
</project>
```
* 创建服务提供者的配置文件application.yml
```yml
server:
  port: 8081
spring:
  application:
    name: provide
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
```
__配置属性说明__
---
`spring.application.name`：当前服务注册在`Eureka Server`上的名称。
`eureka.client.service-url.defaultZone`：注册中心的访问地址。
`eureka.instance.prefer-ip-address`：是否将当前服务的`IP`注册到`Eureka Server`
* 创建服务提供者的启动类
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
```

#### 将注册中心和服务提供者分别运行后用浏览器打开http://localhost:8761即可看到像下图所示
![](https://upload-images.jianshu.io/upload_images/7473008-d1500496b371a092.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 为服务提供者创建`Controller`，用来提供服务
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    Registration registration;

    @GetMapping("/hello")
    public String hello() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println("service = " + service);
        }

        String host = registration.getHost();
        int port = registration.getPort();
        System.out.printf("Host:%s,Port:%d", host, port);
        return "hello world";
    }
}
```
#### 重新启动服务提供者，访问地址[http://192.168.1.105:8081/hello](http://192.168.1.105:8081/hello)，浏览器显示如下图
![](https://upload-images.jianshu.io/upload_images/7473008-34bde2e1dda22158.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
__控制台显示如下图__
![](https://upload-images.jianshu.io/upload_images/7473008-201a95c32435fd6f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
#### 接下来创建服务消费者
__为能有多个服务提供者，这里我们创建多个服务提供者的启动入口，点击`Edit Configurations`__
![](https://upload-images.jianshu.io/upload_images/7473008-dfcffaa7fe6ae906.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

添加命令行参数主要是为了防止端口被占用的问题
![](https://upload-images.jianshu.io/upload_images/7473008-5366b6530de3738e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
##### 接下来在父工程下创建一个Module，pom.xml如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>cn.visiontalk</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>consumer01</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <version>2.1.2.RELEASE</version>
        </dependency>
    </dependencies>

</project>
```
创建配置文件application.yml
```yml
server:
  port: 8030
spring:
  application:
    name: consumer
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
```
创建启动类
```java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumerApplication {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
```
创建控制器作为服务消费者
```java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/consumer")
    public String consumer() {
        return restTemplate.getForObject("http://provide/hello", String.class);
    }
}
```
#### 将所有的程序启动入口都运行起来，有浏览器打开http://localhost:8761如下所示
![](https://upload-images.jianshu.io/upload_images/7473008-eda60b745b6cd50d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

___为了知道消费者消费的是哪一个服务提供者提供的服务，这里我将服务提供者的`Controller`进行更改，___
```java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    Registration registration;

    @GetMapping("/hello")
    public String hello() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println("service = " + service);
        }

        String host = registration.getHost();
        int port = registration.getPort();
        
//        只是在这个地方做了小量更改，为了知道是那个提供了服务
        String format = String.format("Host:%s,Port:%d\t------>hello world", host, port);
        return format;
    }
}
```
#### 用浏览器访问网址[http://192.168.1.105:8030/consumer](http://192.168.1.105:8030/consumer)，即可看到如下图所示
![](https://upload-images.jianshu.io/upload_images/7473008-5f90b9ede9f9872b.gif?imageMogr2/auto-orient/strip)
上面就是一个用`Ribbon + RestTemplate`实现的一个负载均衡的客户端消费者，接下来我们用`Feign`实现一个负载均衡的消费者。
#### 同样在父工程下创建一个Module，pom.xml如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>cn.visiontalk</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>feignConsumer01</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <version>2.1.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>2.1.2.RELEASE</version>
        </dependency>
    </dependencies>

</project>
```
创建配置文件
```yml
server:
  port: 8034
spring:
  application:
    name: feign-consumer
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
```
创建启动类
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignApplication.class, args);
    }
}
```
___这里注意要使用`@EnableFeignClients`注解，启动`FeignClient`组件的扫描___

下面就是编写接口，用`@FeignClient`注解来标识，`value=(服务名ID)`
```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "provide")
public interface Service {

    @GetMapping("hello")
    String getInfoByProvide();
}
```
创建控制器，处理请求
```java
import com.example.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {

    @Autowired
    Service service;

    @GetMapping("feignconsumer")
    public String getInfo() {
        return service.getInfoByProvide();
    }
}
```
运行后效果如下：
![](https://upload-images.jianshu.io/upload_images/7473008-afdc574ec73bfe1b.gif?imageMogr2/auto-orient/strip)
#### 这里和Ribbon一样实现了负载均衡，但是我们如果现在将服务提供者的一个实例关闭，再次刷新几次浏览器就会发生如下的情况：
![](https://upload-images.jianshu.io/upload_images/7473008-b271ab6953fd76d2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

因为有一个服务提供者实例被关闭，所以出现消费者访问被拒绝的情况
当是这样的一个信息对用户来说是非常不友好的。为了防止这种情况的发生我们对消费者Feign-consumer进行如下改造：
__首先更改配置文件application.yml，如下：__
```yml
server:
  port: 8034
spring:
  application:
    name: feign-consumer
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
# 主要是添加了下面的这一段信息
feign:
  hystrix:
    enabled: true
```
`feign.hystrix.enabled`：是否开启feign的熔断机制
__然后创建如下类，实现上面编写的Service接口__
```java
import org.springframework.stereotype.Component;

@Component
public class MyService implements Service {
    @Override
    public String getInfoByProvide() {
        return "服务器正在维护，请稍后重试......";
    }
}
```
最后在Service接口上面标的注解添加一个`fallback`字段
```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
// 注意下面的注解，进行了更改了。降级处理
@FeignClient(value = "provide", fallback = MyService.class)
public interface Service {

    @GetMapping("hello")
    String getInfoByProvide();
}
```
![](https://upload-images.jianshu.io/upload_images/7473008-dc0059f6c24f4798.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
___如果再次出现访问不到的情况，就是下面的样子了。对于用户来说，这样就比较友好的。___
#### Hystrix容错机制
在不改变各个微服务调用关系的前提下，针对错误情况进行预先处理。
* 设计原则
1、服务隔离机制
2、服务降级机制
3、熔断机制
4、提供实时的监控和报警功能
5、提供实时的配置修改功能
Hystrix数据监控需要结合Spring Cloud Actuator 来使用，Actuator提供了对服务的健康监控、数据统计，可以通过hystrix-stream节点获取监控的请求数据，提供了可视化的监控界面。

__在父工程下创建Module，pom.xml如下：__
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>cn.visiontalk</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>hystrixconsumer01</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <version>2.1.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
            <version>2.1.6.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>2.1.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
    </dependencies>
</project>
```
__创建配置文件__
```yml
server:
  port: 8034
spring:
  application:
    name: feign-consumer
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
feign:
  hystrix:
    enabled: true
management:
  endpoints:
    web:
      exposure:
        include: 'hystrix.stream'
```
__创建启动类__
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrixDashboard
public class HystrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class, args);
    }
}
```
创建控制器，为了方便这里用到上面的Service接口，创建的
```java
import com.example.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    Service service;

    @GetMapping("/index")
    public String index() {
        return service.getInfoByProvide();
    }
}
```
#### 创建好后启动所需要的程序
* 用浏览器访问下面这个网址，可以看到不懂访问的ping消息，但是并没有内容
[http://127.0.0.1:8034/actuator/hystrix.stream](http://192.168.1.105:8034/actuator/hystrix.stream)
* 用打开一个标签页，访问下面这个网址
[http://127.0.0.1:8034/index](http://127.0.0.1:8034/index)
![](https://upload-images.jianshu.io/upload_images/7473008-1a49bf09a929d82c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
* 在切换到刚刚的标签页，可以看到类似下面的访问信息，这里需要访问的是一个消费者
![2019-07-21_115417.png](https://upload-images.jianshu.io/upload_images/7473008-00998f53a0644136.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
* 访问这个网址即可查看可视化信息[http://192.168.1.105:8034/hystrix/](http://192.168.1.105:8034/hystrix/)
![2019-07-21_115314.png](https://upload-images.jianshu.io/upload_images/7473008-93693c31e49c3ee9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 总结：启动成功之后，访问http://localhost:{服务端口号}/actuator/hystrix.stream可以监控到请求数据，访问http://localhost:{服务端口号}/hystrix，可以看到可视化的监控界面，输入要监控的地址节点即可看到该节点的可视化数据监控。

#### Spring Cloud 配置中心
`Spring Cloud Config`，通过服务端可以为多个客户端提供配置服务。`Spring Cloud Config`可以将配置文件存储在本地，也可以将配置文件存储在远程`Git`仓库，
在父工程下创建Module，pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>cn.visiontalk</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>Serviceconfig01</artifactId>


    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
    </dependencies>
</project>
```
* 创建配置文件application.yml
```yml
server:
  port: 8035
spring:
  application:
    name: config-server
  profiles:
    active: native
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/shared
```
`resources` 路径下创建 `shared`文件夹，并在此路径下创建 `configclient-dev.yml`
```yml
server:
  port: 8070
foo: foo version 1
```
* 创建启动类
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SCApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SCApplication.class, args);
    }
}
```
`@EnableConfigServer`:声明配置中心
#### 创建客户端读取本地配置中心的配置文件
* 在父工程中创建Module，pom.xml如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>hystrixconsumer01</artifactId>
        <groupId>cn.visiontalk</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>nativeconfigclient</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
    </dependencies>

</project>
```
* 创建 bootstrap.yml，配置读取本地配置中心的相关信息
```yml
spring:
  application:
    name: configclient
  profiles:
    active: dev
  cloud:
    config:
      uri: http://localhost:8035
```
__字段说明：__
`spring.cloud.config.uri`：本地`Config Server`的访问路径
`spring.cloud.config.fail-fase`：设置客户端优先判断 `Config Server`获取是否正常。
通过`spring.application.name` 结合`spring.profiles.active`拼接目标配置文件名，`configclient-dev.yml`，`Config Server`
* 创建启动类
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NCCApplication {

    public static void main(String[] args) {
        SpringApplication.run(NCCApplication.class,args);
    }
}
```
* 创建访问控制器
```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("native")
public class GetConfig {

    @Value("${foo}")
    private String foo;

    @Value("${server.port}")
    private int port;

    @GetMapping("getconfig")
    public String index() {
        return String.format("foo:%s , port:%d", foo, port);
    }
}
```
在这个项目中我们只需要启动配置提供者，和配置消费者即可，因为在配置中我们直接写了配置服务器的`ip`和端口`port`
![](https://upload-images.jianshu.io/upload_images/7473008-15c338754150c3c5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
