package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtils {

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password) {
        try {
            byte[] salt = generateSalt();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] passwordBytes = password.getBytes();
            byte[] combined = new byte[salt.length + passwordBytes.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(passwordBytes, 0, combined, salt.length, passwordBytes.length);

            byte[] hashedBytes = digest.digest(combined);

            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hashedBytes);

            return saltBase64 + ":" + hashBase64;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao hashear a senha.", e);
        }
    }

    public static boolean verifyPassword(String rawPassword, String storedPassword) {
        try {

            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {

                return false;
            }
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedHash = parts[1];


            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordBytes = rawPassword.getBytes();
            byte[] combined = new byte[salt.length + passwordBytes.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(passwordBytes, 0, combined, salt.length, passwordBytes.length);
            
            byte[] hashedBytes = digest.digest(combined);
            String newHash = Base64.getEncoder().encodeToString(hashedBytes);

            return newHash.equals(storedHash);

        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
             throw new RuntimeException("Erro ao verificar a senha.", e);
        }
    }
}