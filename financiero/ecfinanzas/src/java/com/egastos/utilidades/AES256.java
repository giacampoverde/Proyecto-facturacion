/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;

import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

/**
 *
 * @author Ricardo
 */
public class AES256 {

    private static final String ALGORITHM = "AES";
    private static final int ITERATIONS = 2;
    private static final String VENDOR = "BDDCA";

    /**
     * Funcion que genrea un AES256 a partir de una string
     *
     * @param value string que se desea cifrar
     * @return Cadena de texto cifrada
     * @throws Exception
     */
    public static String toAES256(String value) throws Exception {
        Key key = generateKey();
        String salt = VENDOR;
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        String valueToEnc = null;
        String eValue = value;
        for (int i = 0; i < ITERATIONS; i++) {
            valueToEnc = salt + eValue;
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            eValue = new BASE64Encoder().encode(encValue);
        }
        eValue = eValue.replaceAll("[\n\r]", "");
        return eValue;
    }

    /**
     * Obtiene un mensaje original a partir del cifrado
     *
     * @param value cadena que se desea decrifrar
     * @return cadena decifrada
     * @throws Exception
     */
    public static String toMessage(String value) throws Exception {
        Key key = generateKey();
        String salt = VENDOR;
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        String dValue = "";
        String valueToDecrypt = value;
        for (int i = 0; i < ITERATIONS; i++) {
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(valueToDecrypt);
            byte[] decValue = c.doFinal(decordedValue);
            dValue = new String(decValue).substring(salt.length());
            valueToDecrypt = dValue;
        }
        dValue = dValue.replaceAll("[\n\r]", "");
        return dValue;
    }

    /**
     * Cifrado basico Aes 256 sin agregar cadena de ofuscacion
     *
     * @param valueToEnc cadena de texto que se desea cifrar
     * @return cadena cifrada de forma basica
     * @throws Exception
     */
    public static String toAES256Basica(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encValue);
        return encryptedValue.replaceAll("[\n\r]", "");
    }

    /**
     * Retorna una cadena decifrada
     *
     * @param encryptedValue cadena que se desea decifrar
     * @return cadena decifrada
     * @throws Exception
     */
    public static String toMessageBasica(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue.replaceAll("[\n\r]", "");
    }

    /**
     * Generador de la clave con la que se encripta el mensaje
     *
     * @return
     * @throws Exception
     */
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec("IvWiUXtAGYPvnnetdIdyK9xMECCYRPjh".getBytes(), ALGORITHM);
        return key;
    }
    
    /**
     * 
     * @param args
     * @throws Exception 
     */
    
    public static void main(String args[]) throws Exception{
//        BigDecimal bd=new BigDecimal("1.22");
//        System.out.println(bd.precision());
         System.out.println(AES256.toAES256("Drcm0301"));
    
    }
}
