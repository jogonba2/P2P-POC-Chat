package Utils;
import Configuration.ConstantsCrypto;
/**
 * Write a description of class CryptographyUtils here.
 * 
 * @author (Overxfl0w13 & Sanko) 
 * @version (Test)
 */
public class CryptographyUtils
{
    /**
     * Get a pseudorandom prime number starting at @baseNumber
     * @maxPrims = Max prime selection starting at 1
     */
    public final static long get_prime(long baseNumber,int maxPrims){
        int primsGet   = (int)(Math.random()*maxPrims)+1;
        int countPrims = 0;
        baseNumber     = (baseNumber%2==0) ? baseNumber+=1 : baseNumber;
        long lastPrim  = -1;
        for(long i=baseNumber;countPrims<primsGet;i+=2)
        {
            if (CryptographyUtils.is_prime(i)){
                countPrims++;
                lastPrim = i;
            }
        }
        
        return lastPrim;
    }
    
    /**
     * Check if given @pprime is prime
     */
    public final static boolean is_prime(long pprime){
        for(int j=2;j<(long)Math.sqrt(pprime);j++) if(pprime%j==0) return false;
        return true;
    }
    
    /**
     * @length Length of iv results
     * return iv
     */
    public final static String get_iv(int length){
        String res = "";
        for(int i=0;i<length;i++) res += ConstantsCrypto.alphch.charAt((int)(Math.random()*ConstantsCrypto.alphch.length()));
        return res;
        
    }
    
    /**
     * @baseKey fill with 0 baseKey length to 16B
     * return iv
     */
    public final static String fill_dh_key(String baseKey){
        for(int i=baseKey.length();i<16;i++) baseKey += "0";
        return baseKey;
    }
    
        
    /**public static void main(String args[]){
        System.out.println(CryptographyUtils.get_iv(128));
    }**/
    /**public static void main(String args[]){
        System.out.println(CryptographyUtils.get_prime((long)Math.pow(13,10),(int)(Math.random()*100)+20));
    }**/
}
