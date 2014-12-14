package at.tugraz.iaik.pgp.parser;

import iaik.security.provider.IAIK;
import iaik.utils.Base64InputStream;
import iaik.utils.Base64OutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import at.tugraz.iaik.test.Base64CRC24InputStream;

public class PGPArmorParser {
	
	private static PacketParserHandler packet_parser;
	
	private static int packet_body_len = 0;
	private static int packet_body_iterator = 0;
	private static byte[] data;
	private static boolean in_packet = false;
	private static boolean in_header = false;
	private static boolean v4;
	private static int tag = 0;
	private static int lenlen = 0;
	private static short v4_octet_len = 0;
	
	private static void parse(byte b) {
		//System.out.printf("parsing 0x%02X ...\n", b);
		
		if(!in_packet) { // new packet
			in_packet = true;
			in_header = true;
			packet_body_len = 0;
			packet_body_iterator = 0;
			
			byte header = (byte)(b & 0x7F); // remove "allways 1" bit
			
			v4 = (b & 0x40) != 0;
			//System.out.println(v4 ? ">> OpenPGP v4" : ">> OpenPGP v3");
			
			if(v4) {
				tag = (b & 0x3f);
				lenlen = 0;
			} else {
				tag = (b & 0x3c) >> 2;
				lenlen = (b & 0x3) +1;
				//System.out.println("v3 lenlen = " +lenlen);
				if(lenlen == 4) {
					System.err.println("TODO: The packet is of indeterminate length! (see 4.2.1.)");
				}
			}
			//System.out.println("tag = " +tag + "                           (" +getNameByTag(tag) +")");
		} else { // in packet:
			if(in_header) {
				if(v4) { // parse packet len for v4:
					if(lenlen <= 0) {
						if(b < 192) { // 4.2.2.1.
							packet_body_len = b;
							in_header = false;
							lenlen = 0;
							data = new byte[packet_body_len];
						} else if (b >= 192 && b < 223) { // 4.2.2.2.
							packet_body_len = b - 192;
							v4_octet_len = 2;
							lenlen = 1;
						} else if(b >= 224 && b < 255) { // 4.2.2.4.
							System.err.println("TODO: Partial Body Lengths (see 4.2.2.4.)");
						}else if (b == 255) { // 4.2.2.3.
							v4_octet_len = 4;
							lenlen = 4;
						}
						
						//System.out.println("v4 len = " +packet_body_len);
					}else {
						if(v4_octet_len == 2) {
							packet_body_len <<= 8;
							packet_body_len = packet_body_len | (b & 0xFF);
							packet_body_len += 192;
						} else if(v4_octet_len == 4) {
							/*
							   bodyLen = (2nd_octet << 24) | (3rd_octet << 16) |
                 						 (4th_octet << 8)  | 5th_octet
							 */
							if(lenlen == 4) packet_body_len = ((b & 0xFF) << 24);
							else if(lenlen == 3) packet_body_len |= ((b & 0xFF) << 16);
							else if(lenlen == 2) packet_body_len |= ((b & 0xFF) << 8);
							else if(lenlen == 1) packet_body_len |= (b & 0xFF);
						}
						if(--lenlen == 0) {
							in_header = false;
							//System.out.println("v4 len = " +packet_body_len);
							data = new byte[packet_body_len];
						}
					}
					
				} else { // parse packet len for v3:
					if(lenlen > 0) {
						packet_body_len <<= 8;
						packet_body_len = packet_body_len | (b & 0xFF);
						
						if(--lenlen == 0) {
							in_header = false;
							//System.out.println("v3 len = " +packet_body_len);
							data = new byte[packet_body_len];
						}
					} else System.err.println("ERROR: still in_header, but no more len-octets?");
				}
			} else { // not in header:
				data[packet_body_iterator++] = b;
				
				if(packet_body_iterator >= packet_body_len) {
					
					packet_parser.parse(data, tag);
					
					//System.out.println("done parsing packet! onwards to next packet...");
					in_packet = false;
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		 System.out.println("reading base64 code generated by pgp (please no armor header lines) ...");
		 
		 IAIK.addAsProvider();
		 
		 packet_parser = new PacketParserHandler();
		 
		 FileInputStream fis = new FileInputStream("test.privkey.pgp");
		 Base64CRC24InputStream base64is = new Base64CRC24InputStream(fis);
		 /*byte[] data2 = new byte[1024];
		 int r = 0;
		 while ((r = base64is.read(data2)) != -1) {
			 for(int i = 0; i < r; i++) {
				 //System.out.printf("0x%02X\n", data2[i]);
				 parse(data2[i]);
			 }
		 }*/
		 
		 int data;
		 while((data = base64is.read()) != -1) {
			 parse((byte)data);
		 }
		 
		 base64is.close();
		 
		 System.out.println("");
		 System.out.println("data read from file! checksum  = " +base64is.getChecksum());
		 System.out.println("calculated checksum from input = " +base64is.getCRC());
		 
		 if(base64is.getChecksum() == base64is.getCRC()) {
			 System.out.println("CHECKSUME CORRECT!");
		 } else {
			 System.out.println("CHECKSUME NOT CORRECT!");
		 }
	}
	
	public static final int PACKET_PUBLIC_KEY_ENCRYPTED_SESSION_KEY = 1;
	public static final int PACKET_SIGNATURE = 2;
	public static final int PACKET_SYMMETRIC_KEY_ENCRYPTED_SESSION_KEY = 3;
	public static final int PACKET_ONE_PASS_SIGNATURE = 4;
	public static final int PACKET_SECRET_KEY = 5;
	public static final int PACKET_PUBLIC_KEY = 6;
	public static final int PACKET_SECRET_SUBKEY = 7;
	public static final int PACKET_COMPRESSED_DATA = 8;
	public static final int PACKET_SYMMETRICALLY_ENCRYPTED_DATA = 9;
	public static final int PACKET_MARKER = 10;
	public static final int PACKET_LITERAL_DATA = 11;
	public static final int PACKET_TRUST = 12;
	public static final int PACKET_USER_ID = 13;
	public static final int PACKET_PUBLIC_SUBKEY = 14;
	public static final int PACKET_USER_ATTRIBUTE = 17;
	public static final int PACKET_SYMMETRICALLY_ENCRYPTED_INTEGRETY_PROTECTED_DATA = 18;
	public static final int PACKET_MODIFICATION_DETECTION_CODE = 19;
	
	public static String getNameByTag(int tag) {
		switch(tag) {
			case PACKET_PUBLIC_KEY_ENCRYPTED_SESSION_KEY:
				return "Public-Key Encrypted Session Key Packets";
			case PACKET_SIGNATURE: 
				return "Signature Packet";
			case PACKET_SYMMETRIC_KEY_ENCRYPTED_SESSION_KEY: 
				return "Symmetric-Key Encrypted Session Key Packets";
			case PACKET_ONE_PASS_SIGNATURE: 
				return "One-Pass Signature Packets";
			case PACKET_SECRET_KEY: 
				return "Secret-Key Packet";
			case PACKET_PUBLIC_KEY: 
				return "Public-Key Packet";
			case PACKET_SECRET_SUBKEY: 
				return "Secret-Subkey Packet";
			case PACKET_COMPRESSED_DATA: 
				return "Compressed Data Packet";
			case PACKET_SYMMETRICALLY_ENCRYPTED_DATA: 
				return "Symmetrically Encrypted Data Packet";
			case PACKET_MARKER: 
				return "Marker Packet (Obsolete Literal Packet)";
			case PACKET_LITERAL_DATA: 
				return "Literal Data Packet";
			case PACKET_TRUST: 
				return "Trust Packet";
			case PACKET_USER_ID: 
				return "User ID Packet";
			case PACKET_PUBLIC_SUBKEY: 
				return "Public-Subkey Packet";
			
			case PACKET_USER_ATTRIBUTE: 
				return "User Attribute Packet";
			case PACKET_SYMMETRICALLY_ENCRYPTED_INTEGRETY_PROTECTED_DATA: 
				return "Sym. Encrypted Integrity Protected Data Packet";
			case PACKET_MODIFICATION_DETECTION_CODE: 
				return "Modification Detection Code Packet";
			default: 
				return "tag not found!";
		}
	}
}
