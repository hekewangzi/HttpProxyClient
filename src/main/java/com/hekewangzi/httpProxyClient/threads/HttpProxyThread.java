package com.hekewangzi.httpProxyClient.threads;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hekewangzi.httpProxyClient.constants.Properties;
import com.hekewangzi.httpProxyServer.constants.RequestMethod;
import com.hekewangzi.httpProxyServer.httpMessage.HttpRequestMessage;
import com.hekewangzi.httpProxyServer.httpMessage.exception.BuildHttpMessageError;
import com.hekewangzi.httpProxyServer.httpMessage.exception.ConnectServerError;
import com.hekewangzi.httpProxyServer.httpMessage.startLine.RequestStartLine;
import com.hekewangzi.httpProxyServer.proxy.ClientProxy;
import com.hekewangzi.httpProxyServer.proxy.Proxy;
import com.hekewangzi.httpProxyServer.utils.SocketUtil;

/**
 * Http代理线程
 * 
 * @author qq
 * 
 */
public class HttpProxyThread extends Thread {
	private final static Logger log = LoggerFactory.getLogger(HttpProxyThread.class);

	/*
	 * 浏览器相关
	 */
	/**
	 * 浏览器Socket
	 */
	private Socket browserSocket;

	/**
	 * 浏览器输入流
	 */
	private InputStream browserInputStream;

	/*
	 * 代理服务器相关
	 */
	/**
	 * 代理服务器Socket
	 */
	private Socket proxyServerSocket;

	/*
	 * constructor
	 */
	private HttpProxyThread() {
	}

	public HttpProxyThread(Socket browserSocket) {
		this.browserSocket = browserSocket;
		try {
			this.browserInputStream = browserSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		/*
		 * 解析客户端请求
		 */
		HttpRequestMessage requestMessage = null;
		try {
			requestMessage = new HttpRequestMessage(browserInputStream);
		} catch (BuildHttpMessageError e1) {
			e1.printStackTrace();
			SocketUtil.closeSocket(browserSocket, proxyServerSocket);
			return;
		}

		System.out.println("[原始请求:]");
		System.out.print(requestMessage);
		System.out.println("----------");

		/*
		 * 连接代理服务器
		 */
		try {
			this.proxyServerSocket = SocketUtil.connectServer(Properties.ServerIp, Properties.ServerPot,
					Properties.ServerConnectTimeout);
		} catch (ConnectServerError e) {
			e.printStackTrace();
			SocketUtil.closeSocket(browserSocket, proxyServerSocket);
			return;
		}

		RequestMethod httpRequestMethod = ((RequestStartLine) requestMessage.getStartLine()).getMethod();
		if (RequestMethod.CONNECT != httpRequestMethod) { // 代理http
			try {
				this.browserSocket.setSoTimeout(Properties.ClientSoceketReadTimeout); // 设置读取浏览器Socket超时时间
				this.proxyServerSocket.setSoTimeout(Properties.ServerSocketReadTimeout); // 设置读取代理服务器Socket超时时间
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}

		/*
		 * 转发流量
		 */
		Proxy proxy = new ClientProxy(browserSocket, proxyServerSocket, requestMessage);
		proxy.setEncryptRequest(true);// 客户端请求加密,服务端应解密请求
		// proxy.setDecryptResponse(true); // 解密响应
		proxy.proxy();
	}

}
