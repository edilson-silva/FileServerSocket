package fileserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author edilson-silva
 */
public class FileServer {

    public static void main(String[] args) throws SocketException, IOException {
        DatagramSocket serverSocket = new DatagramSocket(Util.PORT);
        System.out.printf("======== RUNNING ON %s PORT ========\n\n", Util.PORT);

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            
            String fileName = new String(receivePacket.getData());
            System.out.println("Filename: " + fileName);
            
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            
            String filePath = Util.ROOT_DIR + "filesInput/" + fileName;
            filePath = filePath.trim();
            
            System.out.println("Complete url: "+filePath);
            
            try {
                sendData = Files.readAllBytes(Paths.get(filePath));
            } catch (NoSuchFileException ex){
                System.out.println(Util.ERR_MESSAGE);
                sendData = Util.ERR_MESSAGE.getBytes();
            }
            
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }

}
