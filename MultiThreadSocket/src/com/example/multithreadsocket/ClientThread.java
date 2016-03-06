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
 *  ClientThread���̸߳�������Զ�̷����������ӣ�
 *  ��������Զ�̷�����ͨ�ţ���������֮���ͨ��handler����һ����Ϣ��
 *  
 *  ��ClientThread���߳��յ�UI�̷߳��͹�������Ϣ����ϢЯ�����û���������ݣ�֮��
 *  �������û���������ݷ��͸�Զ�̷�������
 *  
 * @author ii
 *
 */
public class ClientThread implements Runnable {

	private Socket s;
	//������UI�̷߳�����Ϣ��handler����
	private Handler handler;
	//�������UI�̵߳���Ϣ��handler����
	public Handler revHandler;
	//���߳��������socket����Ӧ�߶�������
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
			//����������Ƿ����������źͶ˿ں�
			s = new Socket("192.168.1.88",30000);
			br = new BufferedReader(
					new InputStreamReader(s.getInputStream()));
			os = s.getOutputStream();
			
			//����һ�����߳�����ȡ��������Ӧ������
			new Thread(){
				@Override
				public void run(){
					String content = null;
					//���϶�ȡsocket�������е�����
					try{
						while((content = br.readLine()) != null){
							//ÿ���������Է�����������֮��
							//������Ϣ֪ͨ���������ʾ����
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
			
			//Ϊ��ǰ�̳߳�ʼ��Looper
			Looper.prepare();
			//����revHandler����
			revHandler = new Handler(){
				@Override
				public void handleMessage(Message msg){
					//���յ�UI�߳����û����������
					if(msg.what == 0x345){
						//���û����ı��������������д������
						try{
							os.write((msg.obj.toString()+"\r\n")
									.getBytes("utf-8"));
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			};
			
			//����Looper
			Looper.loop();
		}catch(SocketTimeoutException sete){
//			System.out.println("�������ӳ�ʱ");
			Log.i("text","�������ӳ�ʱ");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
