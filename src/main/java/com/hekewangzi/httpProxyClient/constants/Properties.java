package com.hekewangzi.httpProxyClient.constants;

/**
 * 配置 enum
 * 
 * @author qq
 * 
 */
public class Properties {
	/*
	 * 客户端相关配置(浏览器)
	 */
	/**
	 * 客户端Socket读取超时时间(单位: 秒)
	 * 
	 */
	public final static int ClientSoceketReadTimeout = 10 * 1000;

	/*
	 * 本地代理相关配置
	 */
	/**
	 * 服务端监听端口
	 */
	public final static int ListenerPort = 4444;

	/*
	 * 代理服务器相关配置
	 */
	/**
	 * 服务器IP
	 * 
	 */
	public final static String ServerIp = "47.90.8.185";

	/**
	 * 服务器端口
	 * 
	 */
	public final static int ServerPot = 6666;

	/**
	 * 服务器Socket读取超时时间(单位: 秒)
	 * 
	 */
	public final static int ServerSocketReadTimeout = 60 * 1000;

	/**
	 * 服务器连接超时时间(单位: 秒)
	 * 
	 */
	public final static int ServerConnectTimeout = 10 * 1000;

	/*
	 * 其他
	 */
	/**
	 * 线程池corePoolSize
	 */
	public final static int nThreads = 50;
}
