package cn.shadow;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Protocol;

public class SPIDemo {

	public static void main(String[] args) {
		Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension("MyProtocol");
		System.out.println(protocol.getDefaultPort());
		/*
		 * for() {
		 * 
		 * }
		 */
	}
}
