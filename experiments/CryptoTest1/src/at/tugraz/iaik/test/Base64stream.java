package at.tugraz.iaik.test;

import iaik.utils.Base64InputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Base64stream {

	public static void main(String[] args) throws IOException {
		FileInputStream fis = new FileInputStream("test.base64");
		 Base64InputStream base64is = new Base64InputStream(fis);
		 byte[] data = new byte[1024];
		 int r;
		 while ((r = base64is.read(data)) != -1) {
		   System.out.print(new String(data));
		 }

	}

}
