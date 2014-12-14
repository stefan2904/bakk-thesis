package at.tugraz.iaik.test;

import iaik.utils.Base64InputStream;
import iaik.utils.Base64OutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Base64CRC24StreamDemo {
	public static void main(String[] args) throws IOException {
		 FileOutputStream fos = new FileOutputStream("Base64CRC24Stream.test");
		 Base64CRC24OutputStream base64os = new Base64CRC24OutputStream(fos);
		 byte[] data = "Test Data to be Base64 encoded and written to a file".getBytes();
		 base64os.write(data);
		 base64os.flush();
		 base64os.close();
		 
		 System.out.println("data written to file! cheksum = " +base64os.getChecksum());
		 System.out.println("reading it back in...");
		 
		 FileInputStream fis = new FileInputStream("Base64CRC24Stream.test");
		 Base64CRC24InputStream base64is = new Base64CRC24InputStream(fis);
		 byte[] data2 = new byte[1024];
		 int r = 0;
		 while ((r = base64is.read(data2)) != -1) {
		   System.out.print("IN: " +new String(data2));
		 }
		 
		 base64is.close();
		 
		 System.out.println("");
		 System.out.println("data read from file! checksum  = " +base64is.getChecksum());
		 System.out.println("calculated checksum from input = " +base64is.getCRC());

	}
}
