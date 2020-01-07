package ClientFrame;



import client.DemoClient;
import common.Orders;
import common.proto.MsgProto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ClientFrame extends JFrame {


    //一个类似CRT的连接框
    public static final JTextArea InPutAndOutput  = new JTextArea();


    public ClientFrame() {
        this.setLayout(null);

        //设置组件当前使用的字体
        InPutAndOutput.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 16));
        InPutAndOutput.setLineWrap(true);

        InPutAndOutput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {


                int key = e.getKeyCode();
                if (key == '\n') {


                    //应该可以获取最后一行命令
                    String[] texts=InPutAndOutput.getText().split("\\n+");

                    String rexultOrder= texts[texts.length-1];


                    //String text = InPutAndOutput.getText().replaceAll("\n", "");

                    System.out.println("客户端输入： "+rexultOrder);

                    String[] array = rexultOrder.split("\\s+");
                    Orders orders = Orders.findByOrder(array[0]);

                    MsgProto.Msg msg = MsgProto.Msg.newBuilder()
                           .setMgsId(orders.getOrderId())
                            .setContent(rexultOrder).build();

                    //在这里将消息发送出去
                    System.out.println("DemoClient.channel "+DemoClient.channel);
                    DemoClient.channel.writeAndFlush(msg);

                }
            }


            @Override
            public void keyReleased(KeyEvent e) {
               /* int key = e.getKeyCode();
                if (key == '\n') {
                    InPutAndOutput.setCaretPosition(0);
                }*/
            }
        });

        //得到屏幕的长宽
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;

        JScrollPane displayBox = new JScrollPane(InPutAndOutput);

        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)

        displayBox.setBounds(0,0,700, 600);

        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        displayBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        this.setBounds((width - 700) / 2,(height - 600) / 2,700, 600);
        this.add(displayBox);

        this.setSize(700, 600);
        this.setVisible(true);

    }
}
