package at.tugraz.iaik.test;

// as specified in https://tools.ietf.org/html/rfc4880#page-53

/*
 6.1. An Implementation of the CRC-24 in "C"


      #define CRC24_INIT 0xB704CEL
      #define CRC24_POLY 0x1864CFBL

      typedef long crc24;
      crc24 crc_octets(unsigned char *octets, size_t len)
      {
          crc24 crc = CRC24_INIT;
          int i;
          while (len--) {
              crc ^= (*octets++) << 16;
              for (i = 0; i < 8; i++) {
                  crc <<= 1;
                  if (crc & 0x1000000)
                      crc ^= CRC24_POLY;
              }
          }
          return crc & 0xFFFFFFL;
      }
 */

public class CRC24 {
	private final int CRC24_INIT = 0x0B704CE; 
	private final int CRC24_POLY = 0x1864CFB;
	
	private int crc = CRC24_INIT; // int is enough, since we need only 24 bit
	
	public void init() {
		crc = CRC24_INIT;
	}
	
	public int get() {
		return crc & 0xFFFFFF; // use only lower 3 bytes
	}
	
	public void update(int b) { // byte is only 7 bit, because it is signed! we need 8!
		crc ^= (b << 16);
		for(int i = 0; i < 8; i++) {
			crc <<= 1;
			if((crc & 0x1000000) != 0) {
				crc ^= CRC24_POLY;
			}
		}
	}

	public void update(byte[] in) {
		for(byte b : in) {
			update(b);
		}
	}
	
	public void update(byte[] in, int offset, int len) {
		for(int i = offset; i < offset + len; i++) {
			update(in[i]);
		}
	}
	
}
