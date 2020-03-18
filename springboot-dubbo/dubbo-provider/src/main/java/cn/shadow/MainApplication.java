package cn.shadow;

import org.apache.dubbo.rpc.Exporter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 服务的发布做了哪些事情
 * 1.基于spring解析配置、文件并将其存储到config
 * 2.逻辑判断保证配置信息的安全性
 * 3.组装URL(多种协议：dubbo,zookeeper,injvm等等)
 * 4.构建一个invoker(动态代理)
 * 5.RegistryProtocol.export()
 * 6.各种wrpper(filter/qos/listener)对于invoke的增强
 * 7.DubboProtocol.export()发布服务
 * 8.启动一个nettyServer开始提供服务
 * 9.扩展点的依赖注入 
 * @author notto
 * 入口在ServiceBean.onApplicationEvent()方法上
 * 
 * 
 * 
 */
@SpringBootApplication
public class MainApplication {
	public static void main(String[] args) {
		/**
		 * dubbo可以解析spring的自定义配置文件
		 * 
		 * 在源码中找到dubbo-config工程的dubbo-config-spring包
		 * 1.在该包的resources目录中有spring.handlers文件在这里需要配置处理
		 * 2.定义dubbo.xsd约束文件
		 * 3.DubboNamespaceHandler用来处理他的init()方法是用来解析标签内容的，其中NamespaceHandlerSupport与DubboBeanDefinitionParser很重要
		 * registerBeanDefinitionParser("application", new DubboBeanDefinitionParser(ApplicationConfig.class, true));
		 * 只有service、reference和ConfigCenter在DubboBeanDefinitionParser的配置中后缀上市bean其他的都是Config
		 * 其中serviceBean实现了InitializingBean, DisposableBean,ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, BeanNameAware,ApplicationEventPublisherAware
		 * InitializingBean的afterPropertiesSet()方法bean初始化后被调用
		 * DisposableBean的destroy()销毁方法但是在service中并没有实现
		 * ApplicationContextAware的setApplicationContext()方法可以获取当前容器
		 * BeanNameAware的setBeanName()方法可以获取自己本身
		 * ApplicationListener实现异步监控
		 * ApplicationEventPublisherAware的setApplicationEventPublisher()方法，事件发送器
		 * 当spring上下文被刷新或者加载的时候会触发onApplicationEvent()
		 * 最终调用export()服务发布入口
		 * 当程序配置了注册服务中心的时候会本地发布一个，远程发布一个
		 * 最终过该条语句进行发布
		 * serviceConfig类的Exporter<?> exporter = protocol.export(wrapperInvoker);
		 * 其中的Protocol是RegistryProtocol
		 * 
		 * 找到新生成的Protocol$Adaptive类中是QosProtocolWrapper(ProtocolListenerWrapper(ProtocolFileWrapper(DubboProtocol)))
		 * QosProtocolWrapper提供运维增强，启动质量监控服务默认情况向下驱动22222的端口
		 * ProtocolFileWrapper有会做一些其他的控制这个东西是一个链
		 * 最后启动nettyService开放本地20880端口提供服务
		 * 
		 * 服务注册
		 * 将服务发布到zookeeper中去，根据协议进行适配然后将内容发布出去
		 * 
		 * invoke：
		 * 生成proxyFactory，生成proxyFactory$Adaptive代理类然后调用扩展点默认javassist
		 * javassistProxyFactory被StubProxyFactoryWrapper所包装
		 * 
		 */
		SpringApplication.run(MainApplication.class, args);
	}
}
