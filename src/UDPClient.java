import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {



    static public int port = 4300;
    private static final int TIMEOUT = 3000;   // Resend timeout (milliseconds)
    private static final int MAXTRIES = 8;     // Maximum retransmissions
    private static String secretKey = "128";
    public static void main(String[] args) throws IOException {

        InetAddress serverAddress = InetAddress.getLocalHost();

        String lineReader;

        Scanner sc = new Scanner(System.in);
        lineReader = sc.nextLine();


        byte[] bytesToSend = lineReader.getBytes();

        DatagramSocket socket = new DatagramSocket();

        socket.setSoTimeout(TIMEOUT);  // Maximum receive blocking time (milliseconds)

        DatagramPacket sendPacket = new DatagramPacket(bytesToSend,
                bytesToSend.length, serverAddress, 4300);

        String encData = DiffieHelman.encrypt(lineReader, secretKey);

        DatagramPacket sencData = new DatagramPacket(encData.getBytes(), encData.getBytes().length, serverAddress, 4300);

        DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);

        System.out.println(encData);

        ErrorChecking checksum = new ErrorChecking();
        checksum.getCRC32Checksum(lineReader.getBytes());

        int tries = 0;      // Packets may be lost, so we have to keep trying
        boolean receivedResponse = false;

        do {
            socket.send(sencData);          // Send the echo string
            try {
                socket.receive(receivePacket);  // Attempt echo reply reception

                if (!receivePacket.getAddress().equals(serverAddress)) {// Check source
                    throw new IOException("Received packet from an unknown source");
                }
                receivedResponse = true;
            } catch (InterruptedIOException e) {  // We did not get anything
                tries += 1;
                System.out.println("Timed out, " + (MAXTRIES - tries) + " more tries...");
            }

        } while ((!lineReader.equals("close")) && (!receivedResponse) && (tries < MAXTRIES));

        socket.close();

        if (receivedResponse) {
            System.out.println("Received: " + new String(receivePacket.getData()));
        } else {
            System.out.println("No response -- giving up.");
        }

    }

    }

