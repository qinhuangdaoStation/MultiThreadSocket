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
	
	//�����������ͨ�ŵ����߳�
	private ClientThread clientThread;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        edt_input = (EditText) findViewById(R.id.edt_input);
        txt_show = (TextView) findViewById(R.id.txt_show);
        btn_send = (Button) findViewById(R.id.btn_send);
        
        /**
         * android���������̷߳��ʽ��������
         * �����handler�������������̵߳���Ϣ
         */
        handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		//�����Ϣ���������߳�
        		if(msg.what == 0x123){
        			//���ӷ�������ȡ������׷����ʾ���ı�����
        			txt_show.append("\n"+msg.obj.toString());
        			
        		}
        	}
        };
        
        clientThread = new ClientThread(handler);
        /**
         * Ϊ�˱���UI�̱߳��������������������ӡ��������ͨ�ŵȹ���������ClientThread�߳����
         */
        //�ͻ�������ClientThread�̴߳����������ӡ���ȡ���Է�����������
        new Thread(clientThread).start();
        
        btn_send.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try{
			//���û����·��Ͱ�ť�󣬽��û���������ݷ�װ��message
			//Ȼ���͸����̵߳�handler
			Message msg = new Message();
			msg.what = 0x345;
			msg.obj = edt_input.getText().toString();
			clientThread.revHandler.sendMessage(msg);
			
			Log.i("text", edt_input.getText().toString());
			
			//��������ı���
		    edt_input.setText("");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

