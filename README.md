# MultiThreadSocket
多线程客户端socket编程

      客户端包含两条线程：
  一条负责生成主界面，并响应用户动作，并将用户输入的数据写入socket对应的输出流中；
  另一条负责读取socket对应输入流中的数据（从服务器发送过来的数据），
  并负责将这些数据在程序界面上显示出来。
      应用界面中包含两个文本框：一个用于接收用户输入，另一个用于显示聊天信息；
  界面中还有一个按钮，当用户单击该按钮时，程序想服务器发送聊天信息。
  
