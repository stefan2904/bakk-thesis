package at.tugraz.iaik.testcase;

import static org.junit.Assert.*;

import javax.xml.bind.DatatypeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.tugraz.iaik.test.CRC24;
import at.tugraz.iaik.test.CRC24bouncycastle;
import at.tugraz.iaik.test.MyBase64;

public class CRC24Test {
	
	private CRC24 c1;
	private CRC24bouncycastle c2;

	@Before
	public void setUp() throws Exception {
		c1 = new CRC24();
		c2 = new CRC24bouncycastle(); 
	}

	@Test
	public void testUpdate() {
		c1.init();
		c2.reset();
		
		int c1r;
		int c2r;
		
		c1.update(0xB);
		c2.update(0xB);
		
		c1r = c1.get();
		c2r = c2.getValue();
		
		System.out.println("c1r = " +c1r);
		System.out.println("c2r = " +c2r);
		
		
		byte b[] = new byte[3];
		
		b[0] = (byte)((c1r >> 16) & 0xFF);
		b[1] = (byte)((c1r >>  8) & 0xFF);
		b[2] = (byte)((c1r >>  0) & 0xFF);
		
		// shift around, build 4 radix bytes:
		// use the The Unsigned Right Shift Operator >>>
		// http://grepcode.com/file/repo1.maven.org/maven2/org.bouncycastle/bcpg-jdk16/1.43/org/bouncycastle/bcpg/ArmoredOutputStream.java#65
		
		String base64 = MyBase64.encode(b);
		System.out.println(base64);
		//System.out.println(DatatypeConverter.printBase64Binary(b));
		
		assertEquals(base64, DatatypeConverter.printBase64Binary(b));
		//assertEquals(MyBase64.decode(base64), DatatypeConverter.parseBase64Binary(base64));
		assertEquals(c1r, c2r); // sould be 13398300 for 0xB
	}

}
