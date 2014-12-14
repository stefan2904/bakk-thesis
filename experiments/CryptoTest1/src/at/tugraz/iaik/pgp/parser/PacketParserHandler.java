package at.tugraz.iaik.pgp.parser;

import at.tugraz.iaik.pgp.parser.tags.PacketParser;
import at.tugraz.iaik.pgp.parser.tags.SecretKeyParser;
import at.tugraz.iaik.pgp.parser.tags.UserIDParser;

public class PacketParserHandler {
	void parse(byte[] packet, int tag) {
		System.out.println("parsing " +PGPArmorParser.getNameByTag(tag) +" (" +tag +")" +" ...");
		
		PacketParser parser = null;
		
		switch(tag) {
		case PGPArmorParser.PACKET_PUBLIC_KEY_ENCRYPTED_SESSION_KEY:
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_SIGNATURE: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_SYMMETRIC_KEY_ENCRYPTED_SESSION_KEY: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_ONE_PASS_SIGNATURE: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_SECRET_KEY: 
			parser = new SecretKeyParser(packet);
			break;
		case PGPArmorParser.PACKET_PUBLIC_KEY: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_SECRET_SUBKEY: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_COMPRESSED_DATA: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_SYMMETRICALLY_ENCRYPTED_DATA: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_MARKER: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_LITERAL_DATA: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_TRUST: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_USER_ID: 
			parser = new UserIDParser(packet);
			break;
		case PGPArmorParser.PACKET_PUBLIC_SUBKEY: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_USER_ATTRIBUTE: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_SYMMETRICALLY_ENCRYPTED_INTEGRETY_PROTECTED_DATA: 
			System.err.println("<tag not yet implemented>"); 
			break;
		case PGPArmorParser.PACKET_MODIFICATION_DETECTION_CODE: 
			System.err.println("<tag not yet implemented>"); 
			break;
		default: 
			System.err.println("TAG not found!");
		}
		
		if(parser != null) parser.doFinal();
	}
}
