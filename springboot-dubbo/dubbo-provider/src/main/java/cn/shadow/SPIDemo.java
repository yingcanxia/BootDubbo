package cn.shadow;

import java.util.List;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.compiler.Compiler;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Protocol;


public class SPIDemo {

	public static void main(String[] args) {
		//1.加载指定的路径下对应的spi扩展点的实现，缓存到hashmap，keyi对应的就是文件中定义的key
		//2.getExtension("name")->会在EXTENSION_INSTANCES.get("name")
		
		
		//如果当前扩展点中依赖其他扩展点，则需要进行依赖注入
		//可以作为扩展接口的必须要有@SPI("dubbo")这个注释,括号中的value就是走的扩展点，如果走的是空的话就进入默认扩展点
		//dubbo基于set方法来时先依赖注入例如一个类有一个接口类的私有变量，他需要有这个变量的set方法
		
		Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension("MyProtocol");
		System.out.println(protocol.getDefaultPort());
		 
		/* 自适应扩展点
		 * @adaptive
		 * 两种方法
		 * 一个是基于类的(提前写好了一个自适应的扩展点->对具体的实现进行适配)
		 * 以Compiler为例它在配置文件中有三个key：adaptive、jdk、javassist
		 * 但是Compiler的注解是： @SPI("javassist") 所以默认情况下走javassist
		 * 一个是基于方法的
		 * 动态生成类Protocol$Adaptive implement Protocol 在该类的export方法中根据配置的protocolname通过getExtension()获得实体类并调用对应的export方法
		 */
		/*
		 * Compiler protocol=ExtensionLoader.getExtensionLoader(Compiler.class).
		 * getAdaptiveExtension();
		 */
		//激活扩展点：在spring boot中
		//找到了参数就配置激活
		URL url=new URL("dubbo", "", 0);
		url=url.addParameter("cache", "cache");
		List<Filter>list= ExtensionLoader.getExtensionLoader(Filter.class).getActivateExtension(url,"cache");
		System.out.println(list.size());
	}
}
