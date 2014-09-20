package Cryptography;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import Configuration.ConstantsCrypto;
import java.io.*;
/**
 * Write a description of class AES here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public final class AES
{
    /**
     * Thanks to bricef https://gist.github.com/bricef/2436364
     * @param  plainText   Text plain to cipher
     * @param encriptionKey Key obtained by Diffie-Hellman algorithm
     * @return     byte[] array with @plainText ciphered with @encriptionKey
     */
    public final static byte[] encrypt(String plainText,String encryptionKey) throws Exception
    {
        Cipher cipher       = Cipher.getInstance("AES/CBC/NoPadding","SunJCE");
        SecretKeySpec key   = new SecretKeySpec(encryptionKey.getBytes("UTF-8"),"AES");
        cipher.init(Cipher.ENCRYPT_MODE,key,new IvParameterSpec(ConstantsCrypto.iv.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }
    
        /**
     * Thanks to bricef https://gist.github.com/bricef/2436364
     * @param  plainText   Text plain to decrypt
     * @param encriptionKey Key obtained by Diffie-Hellman algorithm
     * @return     byte[] array with @plainText ciphered with @encriptionKey
     */
    public final static String decrypt(byte[] cipherText,String encryptionKey) throws Exception
    {
        Cipher cipher       = Cipher.getInstance("AES/CBC/NoPadding","SunJCE");
        SecretKeySpec key   = new SecretKeySpec(encryptionKey.getBytes("UTF-8"),"AES");
        cipher.init(Cipher.DECRYPT_MODE,key,new IvParameterSpec(ConstantsCrypto.iv.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText),"UTF-8");
    }
    
   /** public static void main(String args[]){
        /** TEST 
        try{
            String txt = "holaholaholahola";
            byte enc[] = {};
            System.out.println("holaholaholahola encrypted : " + (enc = AES.encrypt(txt,"1337133713371337")));
            System.out.println(enc + " decrypted : " + AES.decrypt(enc,"1337133713371337"));
        }catch(Exception e){ e.printStackTrace(); }
    }**/
}
