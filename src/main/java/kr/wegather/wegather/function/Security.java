package kr.wegather.wegather.function;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Security {
    private static String algorithm = "AES/CBC/PKCS5Padding";
    private static String aesKey = "wtfisthisalgorithmandwhyimdoingt";
    private static String aesIv = "hisomgomgsotired";

    public static String encrypt(String textToEncode) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(aesIv.getBytes());
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
        byte[] encrypted = new byte[0];
        try {
            encrypted = cipher.doFinal(textToEncode.getBytes("UTF-8"));
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(encrypted).replaceAll("/", "_").replaceAll("=", "");
    }

    public static String decrypt(String textToDecode) {
        textToDecode = textToDecode.replaceAll("_", "/") + "==";
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(aesIv.getBytes());
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
        byte[] decodedBytes = Base64.getDecoder().decode(textToDecode);
        byte[] decrypted = new byte[0];
        try {
            decrypted = cipher.doFinal(decodedBytes);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return new String(decrypted);
    }
}
