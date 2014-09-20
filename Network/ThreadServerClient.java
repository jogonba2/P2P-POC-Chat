package Network;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import Cryptography.DiffieHellman;
import Cryptography.AES;
import java.nio.charset.Charset;
import Configuration.ConstantsNetwork;
import java.math.BigInteger;
/**
 * Write a description of class ThreadServerClient here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ThreadServerClient extends Thread
{
   private ServerSocket sSocket;
   private DiffieHellman dh;
   private Client myClient;
   private Socket cSocket;
   
   public ThreadServerClient(DiffieHellman dh,Client myClient){
       try{
           this.sSocket  = new ServerSocket(ConstantsNetwork.Clport);
           this.cSocket  = null;
           this.dh       = dh;
           this.myClient = myClient;
       }catch(IOException e){ e.printStackTrace(); }
   }
   
   public Socket getcSocket(){ return this.cSocket; }
   public void run(){
       Socket sClient = null;
       try{
           sClient               = this.sSocket.accept();
           this.cSocket          = sClient;
           ConstantsNetwork.conn = new Boolean(true);
           Scanner scInput       = new Scanner(sClient.getInputStream()); 
           Scanner tcInput       = new Scanner(System.in);
           PrintWriter pwInput   = new PrintWriter(sClient.getOutputStream());
           // Get other secret_number //
           String other_secret = scInput.nextLine();
           //long other_secret = Long.parseLong(myClient.recvFromUser());
           this.dh.set_secret_key(new BigInteger(other_secret));
           String decryptedOne = "";
           //System  .out.println("My number (a|b) (Receiver): " + this.dh.get_secret_number());
           //System.out.println("The other number            : " + other_secret);              
           System.out.println("Acorded Key  (Receiver): " + this.dh.get_str_secret_key());
           // Send my data //
           pwInput.print(String.valueOf(this.dh.get_data_to_send()) + "\n"); pwInput.flush();
           for(;;){
                try{  
                    String received = scInput.nextLine();
                    System.out.println("String received : " + received);
                    decryptedOne = AES.decrypt(received.getBytes(),(this.dh.get_str_secret_key()));
                    if(decryptedOne!=null) System.out.println("Message: " + decryptedOne);
                }catch(Exception e) { e.printStackTrace(); }
           }
        }catch(IOException e){ e.printStackTrace(); }
       finally{ try{sClient.close();} catch(IOException e){ e.printStackTrace(); } }
   }
}
