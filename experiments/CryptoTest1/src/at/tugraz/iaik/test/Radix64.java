package at.tugraz.iaik.test;

// source:
// http://code.google.com/p/a9cipher/source/browse/src/com/a9development/cipher/Radix64.java

public class Radix64 {
	
	private final static char[] code = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
	private final static char pad = '=';
	
	
	public static String encode(byte[] b) throws Exception {
		int iLength = (b.length) / 3;
		int[] x = new int[++iLength];
		for (int i = 0; i < b.length; i++) {
		        x[i / 3] |= (b[i] & 0xFF) << (16 - (8 * (i % 3)));
		}
		//int eLength = iLength * 4;
		String encoded = "";
		for (int i = 0; i < x.length; i++) {
		        encoded += code[(x[i] >> 18) & 0x3F];
		        encoded += code[(x[i] >> 12) & 0x3F];
		        encoded += code[(x[i] >> 6) & 0x3F];
		        encoded += code[(x[i] >> 0) & 0x3F];
		}
		int padLength = 4 - (((b.length + 5) / 6) % 4);
		encoded = encoded.substring(0, encoded.length() - padLength);
		for (int i = 0; i < padLength; i++) {
		        encoded += pad;
		}
		return encoded;
	}


	public static void main(String[] args) throws Exception {
		
		// 0x14 FB 9C 03 D9 7E
		byte[] data = {(byte)0x14, (byte)0xFB, (byte)0x9c, (byte)0x03, (byte)0xD9, (byte)0x7E};
		String radix = encode(data);
		
		System.out.println(radix);

	}

}
