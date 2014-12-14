package at.tugraz.iaik.test;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.DatatypeConverter;

import iaik.utils.Base64Exception;
import iaik.utils.Base64InputStream;

public class Base64CRC24InputStream extends Base64InputStream {
	
	private Base64CRC24InputStreamFilter filter;
	private CRC24 crc = new CRC24();
	
	public Base64CRC24InputStream(InputStream in) {
		super(in);
		filter = new Base64CRC24InputStreamFilter(in);
		super.in = filter;
		crc.init();
	}
	
	public int getChecksum() {
		//return Integer.valueOf(filter.getChecksum());
		return filter.getChecksum();
	}
	public int getCRC() {
		return crc.get();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException,
			Base64Exception {
		int ret = super.read(b, off, len);
		
		if(ret > 0) crc.update(b, off, ret);
		
		return ret;
	} 
	
	protected class Base64CRC24InputStreamFilter extends FilterInputStream {

		protected Base64CRC24InputStreamFilter(InputStream in) {
			super(in);
		}
		
		private boolean done = false;
		private String checksum = "";
		

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int ret = super.read(b, off, len);

			//System.out.println("read: " +(new String(b)));
			
			if(!done && ret > 0 && (new String(b)).trim().matches("^=.*")) {
				done = true;
			}
			
			if(done) {
				checksum = new String(b);
				int r;
				while((r = super.read(b, off, len)) != -1) {
					checksum += (new String(b).substring(0, r));
				}	
				checksum = checksum.replace("=", "").trim();
				ret = -1;
			}

			return ret;
		}
		
		protected int getChecksum() {
			if(checksum.equals("")) return 0;
			byte[] b = DatatypeConverter.parseBase64Binary(checksum);
			if(b.length != 3) System.err.println("ERROR: Checksum must me 3 bytes (24 bit), read " +b.length);
			
			return ((b[2] & 0xff) | ((b[1] & 0xff) << 8) | ((b[0] & 0xff) << 16));
		}
	}
}
