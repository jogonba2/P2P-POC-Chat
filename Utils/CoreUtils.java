package Utils;

public class CoreUtils
{
    /**
     * 
     * 
     * @param  
     * @return     
     */
    public final static String fill_text_16(String text){
        int nextMultiple = next_multiple(text.length());
        for(int i=text.length();i<nextMultiple-2;i++) text += "\00";
        return text;
    }
    
    /**
     * 
     * 
     * @param  
     * @return   
     */
    private final static int next_multiple(int length){
        length         = (length%2==0) ? length : length+1;
        boolean finded = false;
        int multiple   = length;
        for(;multiple<length*2 && !finded;multiple+=2){ if(multiple%16==0) finded = true; }
        return multiple;
    }
}
