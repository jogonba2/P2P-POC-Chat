package Cryptography;
import Configuration.ConstantsCrypto;
import Utils.CryptographyUtils;
import java.util.Random;
import java.math.BigInteger;
import java.math.BigDecimal;
/** @http://www.javiercampos.es/blog/2011/07/22/el-algoritmo-de-diffie-hellman/ **/

public class DiffieHellman{
    
    private       BigInteger secret_number;
    private       BigInteger secret_key;
    private       String str_secret_key;
    
    public DiffieHellman(){
        this.secret_number = set_secret_number();
        this.secret_key    = new BigInteger("-1");        
    }
    
    public BigInteger get_secret_number(){ return this.secret_number; }
    
    private BigInteger set_secret_number(){ 
        return (new BigDecimal(String.valueOf(Math.random())).multiply(new BigDecimal(Configuration.ConstantsCrypto.diffie_p)).add(new BigDecimal("1"))).toBigInteger();
    }
    public void set_secret_number(BigInteger sn){ this.secret_number = sn; }
     
    
    public BigInteger get_data_to_send() { 
        return Configuration.ConstantsCrypto.diffie_g.modPow(this.secret_number,Configuration.ConstantsCrypto.diffie_p);
    }
    public BigInteger get_secret_key(){ return this.secret_key; }
    public String get_str_secret_key(){ return this.str_secret_key; }
    public void set_secret_key(BigInteger the_other_data){ 
        this.secret_key = the_other_data.modPow(this.secret_number,Configuration.ConstantsCrypto.diffie_p);
        this.str_secret_key = String.valueOf(secret_key);
        while(this.str_secret_key.length()!=16) this.str_secret_key += "\00";
    } 
    
   /**public static void main(String args[]){
        DiffieHellman client1 = new DiffieHellman(); // Genera un número aleatorio < p (primo alto) para el cliente 1 (a)
        DiffieHellman client2 = new DiffieHellman(); // Genera un número aleatorio < p (primo alto) para el cliente 2 (b)
        System.out.println("Numero secreto calculado por el cliente 1 : " + client1.get_secret_number());
        System.out.println("Numero secreto calculado por el cliente 2 : " + client2.get_secret_number());
        System.out.println("El cliente 1 envía al cliente 2 ((g^a)%p) resultado del método get_data_to_send() : " + client1.get_data_to_send());
        System.out.println("El cliente 2 envía al cliente 1 ((g^b)%p) resultado del método get_data_to_send() : " + client2.get_data_to_send());
        // Aqui el cliente 2 ya tiene ((g^a)%p) y genera la clave con (((g^a)%p)^b)%p -> método set_secret_key() //
        client2.set_secret_key(client1.get_data_to_send());
        // Aqui el cliente 1 ya tiene ((g^b)%p) y genera la clave con (((g^b)%p)^a)%p -> método set_secret_key() //
        client1.set_secret_key(client2.get_data_to_send());
        System.out.println("Key calculada por el cliente 2: " + client2.get_secret_key());  
        System.out.println("Key calculada por el cliente 1: " + client1.get_secret_key());       
    } **/
}   