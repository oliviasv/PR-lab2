import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {

    private static String secretKey = "128";
    public static void main(String[] args) throws IOException {

        DatagramSocket ds = new DatagramSocket(4300);
        byte[] receive = new byte[65535];

        DatagramPacket DpReceive = null;
        while (true) {

            DpReceive = new DatagramPacket(receive, receive.length);

            // receive the data in byte buffer.
            ds.receive(DpReceive);

            String finString = new String(receive, 0, DpReceive.getLength());

            String decData = DiffieHelman.decrypt(finString,secretKey);
            System.out.println("Client:-" + decData);

            // exit the server if the client sends "bye"
            if (data(receive).toString().equals("bye"))
            {
                System.out.println("Client sent bye.....EXITING");
                break;
            }

            // clear the buffer after every message
            receive = new byte[65535];
        }
    }

    // method to convert the byte array data into a string representation
    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
} 