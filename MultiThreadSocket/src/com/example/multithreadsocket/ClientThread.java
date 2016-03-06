package com.example.multithreadsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 *  ClientThread子线程负责建立与远程服务器的连接，
 *  并负责与远程服务器通信，读到数据之后便通过handler发送一条消息；
 *  
 *  当ClientThread子线程收到UI线程发送过来的消息（消息携带了用户输入的内容）之后，
 *  还负责将用户输入的内容发送给远程服务器。
 *  
 * @author ii
 *
 */
public class ClientThread implements Runnable {

	private Socket s;
	//定义向UI线程发送信息的handler对象
	private Handler handler;
	//定义接收UI线程的消息的handler对象
	public Handler revHandler;
	//该线程所处理的socket所对应高度输入流
	private BufferedReader br = null;
	private OutputStream os = null;

	public ClientThread(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			//这里给出的是服务器主机号和端口号
			s = new Socket("192.168.1.88",30000);
			br = new BufferedReader(
					new InputStreamReader(s.getInputStream()));
			os = s.getOutputStream();
			
			//启动一条子线程来读取服务器响应的数据
			new Thread(){
				@Override
				public void run(){
					String content = null;
					//不断读取socket输入流中的内容
					try{
						while((content = br.readLine()) != null){
							//每当读到来自服务器的数据之后，
							//发送消息通知程序界面显示数据
							Message msg = new Message();
							msg.what = 0x123;
							msg.obj = content;
							handler.sendMessage(msg);
						}
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}.start();
			
			//为当前线程初始化Looper
			Looper.prepare();
			//创建revHandler对象
			revHandler = new Handler(){
				@Override
				public void handleMessage(Message msg){
					//接收到UI线程中用户输入的数据
					if(msg.what == 0x345){
						//将用户在文本框内输入的内容写入网络
						try{
							os.write((msg.obj.toString()+"\r\n")
									.getBytes("utf-8"));
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			};
			
			//启动Looper
			Looper.loop();
		}catch(SocketTimeoutException sete){
//			System.out.println("网络连接超时");
			Log.i("text","网络连接超时");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
