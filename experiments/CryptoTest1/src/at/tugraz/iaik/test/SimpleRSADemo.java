/**
 * 
 */
package at.tugraz.iaik.test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import javax.crypto.Cipher;

import iaik.security.provider.IAIK;
import iaik.security.random.SecRandom;

/**
 * @author Stefan
 *
 */
public class SimpleRSADemo {

	private final static String encryptionAlgorithm = "RSA/ECB/PKCS1Padding";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello crypto world!");
		
		IAIK.addAsProvider();
		
		try {
			SecureRandom random = SecRandom.getDefault();

			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "IAIK");
			kpg.initialize(1024, random);
			KeyPair kp = kpg.generateKeyPair();

			Cipher cipher1 = Cipher.getInstance(encryptionAlgorithm, "IAIK");
			cipher1.init(Cipher.ENCRYPT_MODE, kp.getPublic());
			
			System.out.println(kp.getPublic().toString());
			
			byte[] text = "This is a test".getBytes("ASCII");
			
			System.out.println("text = " +(new String(text)));
			
			byte[] ciphertext = cipher1.doFinal(text);
			
			System.out.println("ciphertext = " + ciphertext);
			
			Cipher cipher2 = Cipher.getInstance(encryptionAlgorithm, "IAIK");
			cipher2.init(Cipher.DECRYPT_MODE, kp.getPrivate());
			
			byte[] cleartext = cipher2.doFinal(ciphertext);
			
			System.out.println("cleartext = " +(new String(cleartext)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("done!");
	}

}
