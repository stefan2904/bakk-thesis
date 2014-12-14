package at.tugraz.iaik.test;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.DatatypeConverter;

import iaik.utils.Base64OutputStream;

public class Base64CRC24OutputStream extends Base64OutputStream {
	
	private CRC24 crc = new CRC24();
	private int checksum = -1;

	public Base64CRC24OutputStream(OutputStream out) {
		super(out);
		crc.init();
	}

	@Override
	public synchronized void write(byte[] in, int inOff, int inLen)
			throws IOException {
		crc.update(in, inOff, inLen);
		super.write(in, inOff, inLen);
	}

	@Override
	public synchronized void write(int b) throws IOException {
		crc.update(b);
		super.write(b);
	}
	
	public int getChecksum() {
		return checksum;
	}

	@Override
	public void close() throws IOException {
		out.write("\n".getBytes());
		checksum = crc.get();
		
		byte b[] = new byte[3];
		
		b[0] = (byte)((checksum >> 16) & 0xFF);
		b[1] = (byte)((checksum >>  8) & 0xFF);
		b[2] = (byte)((checksum >>  0) & 0xFF);
		
		//String c = MyBase64.encode(b);
		String c = DatatypeConverter.printBase64Binary(b); // TODO: replace with better implementation
		c = "=" + c.replace("=", "");

		out.write(c.getBytes());
		crc.init();
		super.close();
	}
}
