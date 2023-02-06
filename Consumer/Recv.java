package Consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class Recv
{
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] argv) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String saveFilePath = "...\\test.txt";  // Remember to change the file path
        File file = new File(saveFilePath);
        FileOutputStream fos = new FileOutputStream(file);

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) ->
        {
            String content = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received: " + content.length() + "bytes");

            byte[] bytes = content.getBytes();
            fos.write(bytes);
            fos.close();
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
