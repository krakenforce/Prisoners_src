package Application.Database;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class HashingUtils {

    public static String generate_Hash_Using_PBKDF2(String saltString, char[] pw) {
        try {
            byte[] salt = saltString.getBytes(); // turn string to bytes
            KeySpec spec = new PBEKeySpec(pw, salt, 65536, 128);
            SecretKeyFactory factory = null;
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = new byte[0];
            hash = factory.generateSecret(spec).getEncoded();

            // turn hash to String from byte[]:
            Base64.Encoder enc = Base64.getEncoder();
            return enc.encodeToString(hash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e) ;
        }
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(salt);
    }

    public static String generateRandomPassword(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be at least 1");

        }
        String char_set = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

        return generateRandomChars(char_set, length);
    }

    private static String generateRandomChars(String set, int length) {
         Random random = new SecureRandom();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(set.charAt(random.nextInt(set.length())));
        }

        return sb.toString();
    }

    public static HashAndSalt getHashAndSaltFromPassword(char[] pw) {

        String salt = HashingUtils.generateSalt();
        String hash = HashingUtils.generate_Hash_Using_PBKDF2(salt, pw);

        return new HashAndSalt(hash, salt);
    }

    public static String generateRandomEmployeeCode() {
        String set = "1234567890";
        String emId = generateRandomChars(set, 6);

        return emId;
    }


}
