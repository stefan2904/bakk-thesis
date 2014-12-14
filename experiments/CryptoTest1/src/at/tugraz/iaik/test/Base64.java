package at.tugraz.iaik.test;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

public class Base64 {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// shoule be replaced be something different?
		// something like Base64InputStream in IAIK-JCE?
		// some own Base64 code?
		
		// (Base64 first implemented in Java8!?)	
		
		String encode = DatatypeConverter.printBase64Binary("stefan".getBytes("UTF-8"));
		
		System.out.println(encode);
		
		byte[] decode = DatatypeConverter.parseBase64Binary(encode);
		
		System.out.println(new String(decode));
	}

}
