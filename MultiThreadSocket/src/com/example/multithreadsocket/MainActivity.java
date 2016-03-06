package com.example.multithreadsocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private EditText edt_input;
	private TextView txt_show;
	private Button btn_send;
	
	private Handler handler;
	
	//定义与服务器通信的子线程
	private ClientThread clientThread;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        edt_input = (EditText) findViewById(R.id.edt_input);
        txt_show = (TextView) findViewById(R.id.txt_show);
        btn_send = (Button) findViewById(R.id.btn_send);
        
        /**
         * android不允许子线程访问界面组件，
         * 定义该handler来处理来自子线程的消息
         */
        handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		//如果消息来自于子线程
        		if(msg.what == 0x123){
        			//将从服务器读取的内容追加显示在文本框中
        			txt_show.append("\n"+msg.obj.toString());
        			
        		}
        	}
        };
        
        clientThread = new ClientThread(handler);
        /**
         * 为了避免UI线程被阻塞，将建立网络连接、与服务器通信等工作都交给ClientThread线程完成
         */
        //客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
        new Thread(clientThread).start();
        
        btn_send.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try{
			//当用户按下发送按钮后，将用户输入的数据封装成message
			//然后发送给子线程的handler
			Message msg = new Message();
			msg.what = 0x345;
			msg.obj = edt_input.getText().toString();
			clientThread.revHandler.sendMessage(msg);
			
			Log.i("text", edt_input.getText().toString());
			
			//清空输入文本框
		    edt_input.setText("");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

