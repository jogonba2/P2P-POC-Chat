package Network;
import Cryptography.DiffieHellman;
import Cryptography.AES;
import Utils.CoreUtils;
import Configuration.ConstantsNetwork;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigInteger;
public class Client
{
    private final String myName;
    private Socket mySocket;
    private PrintWriter pwOutput;
    private Scanner     scInput;
    
    public Client(){
        this.myName   = ConstantsNetwork.user;
        this.mySocket = null;
        this.pwOutput = null;
        this.scInput  = null;
        this.registerUser();
    }
    
    private final void registerUser(){
        try{
            this.mySocket = new Socket(ConstantsNetwork.ipSN,ConstantsNetwork.SNport);
            this.pwOutput = new PrintWriter(this.mySocket.getOutputStream());
            this.scInput  = new Scanner(this.mySocket.getInputStream());
            this.pwOutput.print("reg " + ConstantsNetwork.user + " " + InetAddress.getByName("localhost").toString().split("/")[1] + "\n"); 
            this.pwOutput.flush();
            System.out.println(scInput.next()); 
            this.mySocket.close();
            this.pwOutput.close();
            this.scInput.close();
        }catch(UnknownHostException e){ e.printStackTrace(); }
        catch(IOException e)          { e.printStackTrace(); }
    }
           
    public String getOtherIp(String nameOther){
        String res = "";
        try{
            this.mySocket = new Socket(ConstantsNetwork.ipSN,ConstantsNetwork.SNport);
            this.pwOutput = new PrintWriter(this.mySocket.getOutputStream());
            this.scInput  = new Scanner(this.mySocket.getInputStream());
            this.pwOutput.print("who" + " " + nameOther + "\n"); this.pwOutput.flush();
            res = scInput.nextLine(); 
            this.mySocket.close();
            this.pwOutput.close();
            this.scInput.close();
        }catch(UnknownHostException e){ e.printStackTrace(); }
        catch(IOException e)          { e.printStackTrace(); }
        finally{ return res; }
    }

    public void connectToUser(String ipUser){
        this.mySocket = null;
        try{
            this.mySocket = new Socket(InetAddress.getByName(ipUser),ConstantsNetwork.Otport);
            this.pwOutput = new PrintWriter(this.mySocket.getOutputStream());
            this.scInput  = new Scanner(this.mySocket.getInputStream());
        }catch(UnknownHostException e){ e.printStackTrace(); }
        catch(IOException e)      { e.printStackTrace(); }
    }
      
    public void sendToUser(String data){
        this.pwOutput.print(data+"\n"); this.pwOutput.flush();           
    }
    
    public String recvFromUser(){
        return this.scInput.nextLine();
    }
    
    public static void main(String args[]){ 
        Scanner tcInput = new Scanner(System.in);
        
        // Get user given information //
        System.out.println("Insert your username : ");
        ConstantsNetwork.user   = tcInput.nextLine();
        System.out.println("Insert your application port : ");
        ConstantsNetwork.Clport  = tcInput.nextInt();
        System.out.println("Insert the other application port : ");
        ConstantsNetwork.Otport = tcInput.nextInt();
        
        // Register me in user index //
        Client myClient = new Client();        
        String otherName = "",otherIp = "";   
        String text = "";
        
        // Generate my secret_number //
        DiffieHellman clientDH = new DiffieHellman();
        
        // One-time client is registered, throw another thread to act like a server waitting other connections //
        ThreadServerClient tsc = new ThreadServerClient(clientDH,myClient);
        tsc.start();
        
        // Search another user ip if the user is not connected with the other //
          //do{
                System.out.println("Name of the other user or wait to connection > ");
                tcInput.nextLine();
                otherName = tcInput.nextLine();
                if(!ConstantsNetwork.conn){
                otherIp = myClient.getOtherIp(otherName).replace("/","");
            //}while((otherIp = myClient.getOtherIp(otherName).replace("/",""))=="");      
            // Connect with that user //
            myClient.connectToUser(otherIp);  
           
            // Send my secret_number to the other user //
            myClient.sendToUser(String.valueOf(clientDH.get_data_to_send()));
            //System.out.println("Mi data: " + clientDH.get_data_to_send());
            String other_secret = myClient.recvFromUser();
            //System.out.println("Other secret : " + other_secret);
            // Recv the other user secret_number and set my secret_key with it //
            clientDH.set_secret_key(new BigInteger(other_secret));
            //System.out.println("My number (a|b) (Sender): " + clientDH.get_secret_number());
            System.out.println("Acorded Key   (Sender): " + clientDH.get_str_secret_key());
            // With all keys stablished it's possible to initialise a conversation //
            byte[] cryptedOne = {};
            while(true){
                text = tcInput.nextLine();
                //text = CoreUtils.fill_text_16(text);
                try{
                    cryptedOne = AES.encrypt(text,clientDH.get_str_secret_key());
                    myClient.sendToUser(new String(cryptedOne)); // El crypter AES usa UTF-8 mirar eso.
                    System.out.println("Transmited : " + cryptedOne);
                }catch(Exception e) { e.printStackTrace(); }
            }
        }
        else{
            System.out.println("You has been connected by other"); 
            byte[] cryptedOne = {};
            while(true){
               text = tcInput.nextLine();
               //text = CoreUtils.fill_text_16(text);
               try{
                   cryptedOne = AES.encrypt(text,(clientDH.get_str_secret_key()));
                   myClient.sendToUser(new String(cryptedOne,"UTF-8")+"\n");
                   System.out.println("Transmited : " + cryptedOne);
                   //System.out.println("1 sends to 2 : " + cryptedOne);
               }catch(Exception e) { e.printStackTrace(); }
            }
        }
}   
}
