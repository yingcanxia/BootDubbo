package cn.shadow.service;

import org.apache.dubbo.config.annotation.Service;

import cn.shadow.api.SayHelloService;
/**
 * 可以再service注释中设置参数
 * 比如说负载均衡，以及权重
 * 由于现在是属于微服务层面，所以服务的侧重点是在服务层。controller只用于传参使用，controller的负载均衡可以使用NGINX来做
 * 大部分的业务服务要放在服务层部分
 * 在service中可以添加cluster设置重拾策略，重拾策略有一下几种
 * 默认情况是走3次，一次正常+两次重试 failover
 * 不重试，快速失败：failfast
 * 失败之后记录日志：failback
 * 失败安全,出现错误的话就直接忽略failsafe
 * 并行 调用多个服务，一个成功就是成功 forking
 * 广播，一个失败就是全部失败 
 * @author notto
 *
 */
//@Service(loadbalance = "random",weight = 10,cluster = "failsafe")
//@Service(export = false,delay = 10000)如果export是false的话则表明当前类不启动和加载，delay是否延迟启动防止启动是调用出错
@Service(loadbalance = "random",weight = 10)
public class SayHelloServiceImpl implements SayHelloService{

	/**
	 * 服务降级
	 * 就是在集群中某一个服务出现问题了，就讲服务暂时停掉，保证其他的服务没有问题
	 * 异常降级，可以降低数据实时性的要求，可以返回错误也可以返回静态的数据 
	 * 限流降级，超过了本来并发量
	 * 熔断降级，如同股票熔断，在十秒钟之内超过50%请求响应时间打到5s，达到出发条件 
	 * moke机制
	 * 容错机制一般配置在客户端，毕竟客户端调用服务部分
	 */
	@Override
	public String sayHello() {
		// TODO Auto-generated method stub
		return "hello dubbo";
	}

}
