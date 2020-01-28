package cn.shadow.protocol;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Protocol;
import org.apache.dubbo.rpc.RpcException;

public class MyProtocol implements Protocol{

	@Override
	public int getDefaultPort() {
		return 9999;
	}

	/**
	 * dubbo暴露服务
	 */
	@Override
	public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
		return null;
	}

	/**
	 * dubbo调用服务
	 */
	@Override
	public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	
}
