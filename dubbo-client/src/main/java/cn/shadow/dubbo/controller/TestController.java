package cn.shadow.dubbo.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.shadow.api.SayHelloService;
/**
 * 配置优先级首先以客户端为准(service:服务级别;method：方法界别)-->service层
 * 
 * 如果出现循环依赖的，至少要保证一方先启动可以设置check属性或者在properties里进行设置
 * @author notto
 *
 */
@RestController
public class TestController {

	//@Reference(loadbalance = "roundrobin",timeout = 2,cluster = "failfast",mock="某一个类的某一个类的相同方法",check = false)
	@Reference(loadbalance = "roundrobin")
	private SayHelloService sayHelloService;
	
	@GetMapping("/hello")
	public String sayHello() {
		return sayHelloService.sayHello();
	}
}
