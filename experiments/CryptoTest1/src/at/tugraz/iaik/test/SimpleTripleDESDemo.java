/**
 * 
 */
package at.tugraz.iaik.test;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import iaik.security.provider.IAIK;
import iaik.security.random.SecRandom;
import iaik.utils.Util;

/**
 * @author Stefan
 *
 */
public class SimpleTripleDESDemo {

	private final static String keyAlgorithm = "DESede";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello crypto world!");
		
		IAIK.addAsProvider();
		
		try {
			SecureRandom random = SecRandom.getDefault();

			// generate the TripleDES Key Encryption Key (KEK)
			byte[] b = new byte[24];
			random.nextBytes(b);

			// first generate the Triple DES key 
			random.nextBytes(b);
			Key key = new SecretKeySpec(b, keyAlgorithm);
			System.out.println("Key used:");
			System.out.println(Util.toString(key.getEncoded()));

			// encrypt something 
			Cipher cipher1 = Cipher.getInstance("3DES/CBC/PKCS5Padding", "IAIK");
			cipher1.init(Cipher.ENCRYPT_MODE, key);
			byte[] text = "Encrypt this message".getBytes();
			byte[] encrypted = cipher1.doFinal(text);
			byte[] iv = cipher1.getIV();
			
			System.out.println("encrypted = " +(new String(encrypted)));
			
			// decrypt
			Cipher cipher2 = Cipher.getInstance("3DES/CBC/PKCS5Padding", "IAIK");
			cipher2.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] plaintext = cipher2.doFinal(encrypted);

			System.out.println("plaintext = " +(new String(plaintext)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("done!");
	}

}
