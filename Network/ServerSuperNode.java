package Network;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import Configuration.ConstantsNetwork;
/**
 * Server for a SuperNodes which compounds the system. (Partially centralized)
 * @author (Overxfl0w13) 
 * @version (a version number or a date)
 */
public class ServerSuperNode
{
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String,InetAddress> userIndex;
    private String[] superNodeList; // It contains neighbours supernodes ip to communicate with them if there is no users with name username in it's user index
    private final int maxSuperNodes = 50; // It's possible to use a LPI to avoid limit neighbours supernodes
    /**
     * Constructor for objects of class ServerSuperNode
    */
    public ServerSuperNode()
    {
        try{
            this.serverSocket  = new ServerSocket(ConstantsNetwork.SNport);
            this.userIndex     = new ConcurrentHashMap<String,InetAddress>();
            this.superNodeList = new String[50];
        }catch(UnknownHostException e){ e.printStackTrace(); }
        catch(IOException e)          { e.printStackTrace(); }
    }

    /**
     * Throws the server
    */
    public void runServer(){
        try{
            for(;;){
                Socket sClient      = this.serverSocket.accept();
                ThreadSuperNode tsn = new ThreadSuperNode(sClient,this.userIndex,superNodeList);
                tsn.start();
            }
        }catch(UnknownHostException e){ e.printStackTrace(); }
        catch(IOException e)          { e.printStackTrace(); }
    }
        
    public static void main(String args[]){
        ServerSuperNode ssn = new ServerSuperNode();
        ssn.runServer();
    }
        
    
}
