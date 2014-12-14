package at.tugraz.iaik.test;

// Demo code by http://www.cs.ucf.edu/courses/cis3362/fall2010/hmk/Radix64.java

import java.io.*;
import java.util.*;

public class Radix64Experiments {
	
	private final static char[] code = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
	private final static char pad = '=';

	
	// Pre-condition: c is a valid Radix-64 character.
	// Post-condition: c's integer code (in between 0 and 63, inclusive), will be returned. 
	public static int convertChar(char c) {
		
		if (c >= 'A' && c <= 'Z')
			return (int)(c - 'A');
		else if (c >= 'a' && c <= 'z')
			return (int)(c - 'a' + 26);
		else if (c >= '0' && c <= '9')
			return (int)(c - '0' + 52);
		else if (c == '+')
			return 62;
		return 63;
	}
	
	public static char convertInt(int value) {
		if (value < 26)
			return (char)('A' + value);
		else if (value < 52)
			return (char)('a' + value - 26);
		else if (value < 62)
			return (char)('0' + value - 52);
		else if (value == 62)
			return '+';
		return '/';
	}
	
	// Pre-condition: radix64 is a 10 character block in radix64 characters only.
	// Post-condition: A 64 bit equivalent binary string will be returned, solely 
	//                 consisting of the characters '0' and '1'. Since the 10
	//                 characters only convert to 60 bits, the last four bits of the
	//                 returned string are guaranteed to be '0'.
	public static String convertBlock(String radix64) {
		
		String answer = "";
		
		for (int i=0; i<10; i++) {
			int value = convertChar(radix64.charAt(i));
			answer = answer  +convertIntToBinString(value);
		}
		
		answer = answer+ "0000";
		return answer;
	}
	
	// Returns a String of '0' and '1' that is the binary equivalent of value, in
	// 6 bits.
	public static String convertIntToBinString(int value) {
		
		String answer = "";
		for (int i=0; i<6; i++) {
			if (value%2 == 0)
				answer = "0" + answer;
			else 
				answer = "1" + answer;
			value /= 2;
		}
		
		return answer;
	}
	
	// Pre-condition: Length of binary is 66.
	// Post-condition: Returns a string of length 11 with the Radix-64 equivalent of binary.
	public static String decryptBlock(String binary) {
		
		String answer = "";
		
		for (int i=0; i<11; i++) {
			int value = 0;
			for (int j=0; j<6; j++)
				value = 2*value + (int)(binary.charAt(6*i+j) - '0');
			answer = answer + convertInt(value);
		}
		return answer;
	}
	
	/* Converted Radix-64 file to 0s and 1s for DES.java
	public static void main(String[] args) throws Exception {
		
		Scanner stdin = new Scanner(System.in);
		
		// Input file must have 10 chars per line.
		System.out.println("Enter the name of the Radix 64 input file.");
		String filename = stdin.next();
		
		Scanner fin = new Scanner(new File(filename));
		
		while (fin.hasNext()) {
			String line = fin.next();
			System.out.println(convertBlock(line));
		}
		
	}
	*/
	
	public static void main(String[] args) throws Exception {
			
			String block = "hQIMA7WAH9";
			
			String bin   = convertBlock(block);
			
			
	}
	
	/*public static void main(String[] args) throws Exception {
		
		// = hQIMA7WAH9
		String bin = "1000010100000010000011000000001110110101100000000001111111010000";
		String block = "";
			
		block = bin + "00";
		
		System.out.println(decryptBlock(block));
		
	} */
}