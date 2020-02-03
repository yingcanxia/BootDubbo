package cn.shadow.dubbo.controller;

import static org.apache.dubbo.common.constants.RegistryConstants.CATEGORY_KEY;
import static org.apache.dubbo.common.constants.RegistryConstants.CONFIGURATORS_CATEGORY;
import static org.apache.dubbo.common.constants.RegistryConstants.PROVIDERS_CATEGORY;
import static org.apache.dubbo.common.constants.RegistryConstants.ROUTERS_CATEGORY;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.Invoker;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.shadow.api.SayHelloService;
/**
 * 配置优先级首先以客户端为准(service:服务级别;method：方法界别)-->service层
 * 
 * 如果出现循环依赖的，至少要保证一方先启动可以设置check属性或者在properties里进行设置
 * 
 * 启动服务阶段：
 * 构建连接
 * 获得一个远程通信服务的代理对象
 * 
 * 
 * 运行阶段：
 * 触发远程调用
 * invoker.doinvoker()将请求传递到服务端 
 * @author notto
 *
 */
@RestController
/**
 * 使用ReferenceBean
 * 实现以下接口FactoryBean, ApplicationContextAware, InitializingBean, DisposableBean 
 * @author notto
 *
 */
public class TestController {
 
	//@Reference(loadbalance = "roundrobin",timeout = 2,cluster = "failfast",mock="某一个类的某一个类的相同方法",check = false)
	//返回的代理对象
	/* 
	 * 服务消费端应该做什么事情：
	 * 1.首先在客户端对应的属性加上注解之后就可以进行调用了，这使用的是一个代理类、
	 * 2.建立基于netty的通信连接
	 * 3.从zookeeper去获取目标地址
	 * 4.负载均衡
	 * 5.容错
	 * 6.mock
	 * 7.序列化
	 */
	@Reference(loadbalance = "roundrobin")
	private SayHelloService sayHelloService;
	
	@GetMapping("/hello")
	public String sayHello() {
		/*
		 * 当调用的时候会进入referenceConfig的get()方法来获取
		 * get方法会检查配置，是否被初始化过，第一次调用就初始化调用init()方法
		 * init方法会进行url的拼装，然以后进入createProxy()
		 * createProxy方法会检测是否有注册中心，是点对点还是，服务中心
		 * invoker = REF_PROTOCOL.refer(interfaceClass, urls.get(0));
		 * REF_PROTOCOL是自适应扩展点生成的是Protocol$Adaptive -> getExtension("registry")->也是经过包装的Qos(Listener(Filter(RegistoryProtocol)))
		 * 最终还是要使用RegistoryProtocol.refer()方法
		 * 在refer方法中客户端的url是zookeeper://192.168.......最终进入doRefer()方法
		 * 在doRefer方法1.连接到注册中心通过curator,从注册中心拿到providerUrl,基于provider地址建立通信
		 * Invoker invoker = cluster.join(directory);其中cluster是个自适应扩展点是MockClusterWrapper(FailoverCluster(directory))
		 * 在zk上生成的节点有4个configurators做配置,consumers消费者配置,providers生产者配置,routers做路由
		 * directory.setRegistry(registry);连接zk的api获得url地址
		 * directory.setProtocol(protocol);建立通信
		 * 订阅服务
		 * directory.subscribe(subscribeUrl.addParameter(CATEGORY_KEY,
                PROVIDERS_CATEGORY + "," + CONFIGURATORS_CATEGORY + "," + ROUTERS_CATEGORY));
		 * 到达ZookeeperRegistry的doSubscribe(...)的toCategoriesPath(url)会针对configurator,consumer,router分别去注册监听
		 * 建立通讯
		 * 
		 * dubbo通信过程
		 * 由InvokerInvocationHandler开始到mock进行远程通信，测试以及异常服务降级
		 * mock_key当为false的时候不走mock操作，当为force是强制走本地的返回。如果调用业务失败了，如果是业务上的异常则直接抛出。如果是通信的异常调用mock进行返回
		 * AbstractClusterInvoker中有进行处理，Attachment隐式传参可以通过key-》value进行控制
		 * 
		 * dubbo目前提供三种路由：条件路由；脚本路由；标签路由
		 * 
		 * 负载均衡：获得url里配置的负载均衡策略如果没有的话默认为random
		 * 负载均衡也是通过spi扩展进行处理的，有四种实现，随机，轮训，一致hash以及最小活跃
		 * 容错部分，默认情况是failover，已经调用过失败的节点下一次不会再次调用
		 * 
		 * 
		 */
		return sayHelloService.sayHello();
	}
}
