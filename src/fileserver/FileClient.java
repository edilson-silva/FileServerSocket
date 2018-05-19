package fileserver;

import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * @author edilson-silva
 */
public class FileClient {

    public static void main(String args[]) throws Exception {
        Scanner in = new Scanner(System.in);

        while (true) {
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");

            System.out.println("Filename?");
            System.out.print("> ");
            String fileName = in.next();

            sendData = fileName.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Util.PORT);
            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String data = new String(receivePacket.getData());
            data = data.trim();
            
            if (data.equals(Util.ERR_MESSAGE)) {
                System.out.println(Util.ERR_MESSAGE);
            } else {
                String filePath = Util.ROOT_DIR + "filesOutput/" + fileName;
                byte[] fileData = receivePacket.getData();
                FileOutputStream out = new FileOutputStream(filePath);
                out.write(fileData);
                out.close();
            }

            clientSocket.close();
        }
    }

}
