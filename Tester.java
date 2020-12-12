
/**
 * Write a description of Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import java.util.*;
public class Tester {
    public void testCaesarCipher(){
        FileResource fr = new FileResource("VigenereTestData/titus-small.txt");
        String s = fr.asString();
        CaesarCipher cc = new CaesarCipher(5);
        String encrypted = cc.encrypt(s);
        System.out.println("encrypted message:\n"+encrypted);
        String decrypted = cc.decrypt(encrypted);
        System.out.println("decrypted message:\n"+decrypted);
    }
    
    public void testCaesarCracker(){
        FileResource fr = new FileResource("VigenereTestData/titus-small_key5.txt");
        String s = fr.asString();
        CaesarCracker cc = new CaesarCracker();
        String decrypted = cc.decrypt(s);
        System.out.println("decrypted message:\n"+decrypted);
        
        FileResource fr2 = new FileResource("VigenereTestData/oslusiadas_key17.txt");
        String s2 = fr.asString();
        CaesarCracker cc2 = new CaesarCracker('a');
        String decrypted2 = cc.decrypt(s2);
        System.out.println("decrypted message:\n"+decrypted2);
    }
    
    public void testVigenereCipher(){
        FileResource fr = new FileResource("VigenereTestData/titus-small.txt");
        String s = fr.asString();
        int[] rome = {17, 14, 12, 4};
        VigenereCipher vc = new VigenereCipher(rome);
        String encrypted = vc.encrypt(s);
        System.out.println("encrypted message:\n"+encrypted);
    }
    
    public void testVigenereBreaker(){
        FileResource fr = new FileResource("secretmessage1.txt");
        String s = fr.asString();
        VigenereBreaker vb = new VigenereBreaker();
        System.out.println(Arrays.toString(vb.tryKeyLength(s, 4, 'e')));
    }
}
