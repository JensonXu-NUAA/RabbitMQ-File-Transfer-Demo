package Producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.File;
import java.io.FileInputStream;

public class Send
{
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] argv) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        String filePath = "...\\test.txt";  // Remember to change the file path
        File file=new File(filePath);

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel())
        {
            FileInputStream fis = new FileInputStream(file);
            System.out.println("Start transferring file");
            byte[] bytes = new byte[(int)file.length()];
            int length;
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            length = fis.read(bytes);
            System.out.println("[x] Sent: " + length + " bytes");
            channel.basicPublish("", QUEUE_NAME, null, bytes);
        }
    }
}
