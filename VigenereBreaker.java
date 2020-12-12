import java.util.*;
import edu.duke.*;
import java.io.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        String output = "";
        for (int i=whichSlice; i<message.length(); i+=totalSlices){
            output = output + message.charAt(i);
        }
        return output;
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        for (int i=0; i<klength; i++){
            String currSlicedString = sliceString(encrypted, i, klength);
            CaesarCracker cc = new CaesarCracker(mostCommon);
            key[i] = cc.getKey(currSlicedString);
        }
        return key;
    }

    public void breakVigenere () {
        DirectoryResource dr = new DirectoryResource();
        HashMap<String, HashSet<String>> dic = new HashMap<String, HashSet<String>>();
        for (File f: dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            HashSet<String> currLang = readDictionary(fr);
            dic.put(f.getName(), currLang);
        }
        FileResource fr = new FileResource("messages/secretmessage4.txt");
        String message = fr.asString();
        System.out.println(breakForALLLangs(message, dic));
    }
    
    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> wordsSet = new HashSet<String>();
        for (String word: fr.lines()){
            word = word.toLowerCase();
            wordsSet.add(word);
        }
        return wordsSet;
    }
    
    public int countWords(String message, HashSet<String> dictionary){
        String[] words = message.split("\\W+");
        int count = 0;
        for (String word: words){
            if (dictionary.contains(word.toLowerCase())){
                count++;
            }
        }
        return count;
    }
    
    public String breakForLanguage(String encrypted, HashSet<String> dictionary, char mostCommonChar){
        int count = 0;
        int keyLength = 0;
        //int count38 = 0;
        String answer = "";
        for (int i=1; i<=100; i++){
            int[] currKey = tryKeyLength(encrypted, i, mostCommonChar);
            VigenereCipher vc = new VigenereCipher(currKey);
            String decrypted = vc.decrypt(encrypted);
            if (countWords(decrypted, dictionary) > count){
                count = countWords(decrypted, dictionary);
                answer = decrypted;
                keyLength = i;
                //if (i==38){
                //    count38 = countWords(decrypted, dictionary);
                //}
            }
        }
        //System.out.println("key length: "+keyLength+"\t"+Arrays.toString(tryKeyLength(encrypted, keyLength, mostCommonChar)));
        //System.out.println("valid words: "+count);
        //System.out.println("count38: " + count38);
        return answer;
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int[] counts = new int[26];
        for (String word: dictionary){
            for (int i=0; i<word.length(); i++){
                char ch = Character.toLowerCase(word.charAt(i));
                int idx = alphabet.indexOf(ch);
                if (idx != -1){
                    counts[idx]++;
                }
            }
        }
        int max = 0;
        int maxCount = 0;
        for (int i=0; i<26; i++){
            if (counts[i] > maxCount){
                max = i;
                maxCount = counts[i];
            }
        }
        return alphabet.charAt(max);
    }
    
    public void tesMostCommonCharIn(){
        FileResource fr = new FileResource();
        HashSet<String> dic = readDictionary(fr);
        System.out.println(mostCommonCharIn(dic));
    }
    
    public String breakForALLLangs(String encrypted, HashMap<String, HashSet<String>> languages){
        int validWords = 0;
        String correctLang = "";
        String answer = "";
        char mostCommonChar = 'e';
        for (String currLang: languages.keySet()){
            mostCommonChar = mostCommonCharIn(languages.get(currLang));
            String decrypted = breakForLanguage(encrypted, languages.get(currLang), mostCommonChar);
            if (countWords(decrypted, languages.get(currLang)) > validWords){
                validWords = countWords(decrypted, languages.get(currLang));
                correctLang = currLang;
                answer = decrypted;
            }
        }
        System.out.println("correct language: "+correctLang);
        System.out.println("most common char: "+mostCommonChar);   
        System.out.println("valid words: "+validWords);
        return answer;
    }
}
