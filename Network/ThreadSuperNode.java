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
/** Commands **/
/*  Register username -> reg [username] [ip]
 *  Query    username -> who [username]
 */
/**
 * Server for a SuperNodes which compounds the system. (Partially centralized)
 * 
 * @author (Overxfl0w13) 
 * @version (a version number or a date)
 */
public class ThreadSuperNode extends Thread
{
    private Socket cSocket;
    private ConcurrentHashMap<String,InetAddress> userIndex;
    private Scanner scInput;
    private PrintWriter pwOutput;
    private String[] superNodeList;
    /**
     * Constructor for objects of class ServerSuperNode
    */
    public ThreadSuperNode(Socket cSocket,ConcurrentHashMap<String,InetAddress> userIndex,String[] superNodeList){
        this.cSocket   = cSocket;
        this.userIndex = userIndex;
        this.superNodeList = superNodeList;
        try{
            this.scInput  = new Scanner(this.cSocket.getInputStream());
            this.pwOutput = new PrintWriter(this.cSocket.getOutputStream(),true);
        }catch(IOException e){ e.printStackTrace(); }
    }
    
    public void run(){
        String userCommand = this.scInput.nextLine();
        System.out.println(userCommand);
        if(userCommand.indexOf("reg")==0) this.registerUsername(userCommand);
        else if(userCommand.indexOf("who")==0) this.getUserIp(userCommand);
        else this.communicateError("Command is not correct\n");
        try{
            this.cSocket.close();
            this.pwOutput.close();
            this.scInput.close();
        }catch(IOException e){ e.printStackTrace(); }
    }
    
    // splitted[3] = myPort
    public void registerUsername(String userCommand){
        String splitted[] = userCommand.split(" ");
        // Check if there are enough items in splitted //
        boolean exists = (userIndex.get(splitted[1])!=null) ? true : false;       
        if(!exists){           
            try{ userIndex.put(splitted[1],InetAddress.getByName(splitted[2])); }
            catch(UnknownHostException e){ e.printStackTrace(); }
            this.pwOutput.print("Username " + splitted[1] + " registered with ip: " + splitted[2] + "\n"); this.pwOutput.flush();            
        }else{
            this.pwOutput.print("Username " + splitted[1] + " is already registered" + "\n"); this.pwOutput.flush();   
        }  
    }
    
    public void getUserIp(String userCommand){
        String splitted[] = userCommand.split(" ");
        // Check if there are enough items in splitted //
        boolean exists = (userIndex.get(splitted[1])!=null) ? true : false;
        if(exists){
            InetAddress ia = userIndex.get(splitted[1]); 
            this.pwOutput.print(ia.toString()); 
            this.pwOutput.flush();
        }else{
            /** Communicate with neighbours supernodes (it's essential that exists the supernode if it is indicated in superNodeList array) **/
            String ia;
            // Si no está en este supernodo, se conecta con el vecino para pedirle a el y así iterativo hasta el último que no tenga vecinos //
            if((ia = this.getFromOtherNodes(userCommand))!="") this.pwOutput.print(ia);
            else this.pwOutput.print("Username " + splitted[1] + " is not registered in our supernodes \n"); 
            this.pwOutput.flush();  
        }
    }
    
    public void communicateError(String error){
        this.pwOutput.print(error); this.pwOutput.flush();
    }
    
    private String getFromOtherNodes(String command){
        Socket cSocket       = null;
        PrintWriter pwSocket = null;
        Scanner scSocket     = null;
        String res           = ""  ;
        try{
            for(String ip : superNodeList){
                if(ip!=null){
                    cSocket = new Socket(ip,ConstantsNetwork.SNport);
                    pwSocket = new PrintWriter(cSocket.getOutputStream());
                    scSocket = new Scanner(cSocket.getInputStream());
                    pwSocket.print(command); pwSocket.flush();
                    res = scSocket.next();
                    if(res.indexOf("Username")!=0) return res;
                }
            }
        }catch(UnknownHostException e){ e.printStackTrace(); }
        catch(IOException e){ e.printStackTrace(); }
        finally{
            try{
                pwSocket.close();
                scSocket.close();
                cSocket.close();
            }catch(IOException e){ e.printStackTrace(); }
            finally{ return res; }
        }
    }

}
