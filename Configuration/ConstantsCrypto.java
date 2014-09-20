package Configuration;
import Utils.CryptographyUtils;
import java.math.BigInteger;
/**
 * Constants for cryptographic algorithms 
 * 
 * @author (Overxfl0w) 
 * @version (Test)
 */
public class ConstantsCrypto
{
    public static final BigInteger diffie_p = new BigInteger(String.valueOf(CryptographyUtils.get_prime((long)Math.pow(10,10),(int)(Math.random()))));
    public static final BigInteger diffie_g = new BigInteger(String.valueOf(CryptographyUtils.get_prime((long)Math.pow(5,5),(int)(Math.random()))));
    public static final String iv     = CryptographyUtils.get_iv(16);
    public static final String alphch = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
}
