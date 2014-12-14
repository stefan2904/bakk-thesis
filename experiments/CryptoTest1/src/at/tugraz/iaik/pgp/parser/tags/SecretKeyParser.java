package at.tugraz.iaik.pgp.parser.tags;

import iaik.security.rsa.RSAKeyFactory;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class SecretKeyParser extends PacketParser {
	
	public PublicKey pub;
	public PrivateKey priv;
	
	public SecretKeyParser(byte[] packet_) {
		super(packet_);
	}

	@Override
	public void doFinal() {
		// https://tools.ietf.org/html/rfc4880#page-41
		// --> key material https://tools.ietf.org/html/rfc4880#page-40
		// ff: https://tools.ietf.org/html/rfc4880#page-41
		
		/*for(byte b : super.packet) {
			System.out.printf("parsing 0x%02X ...\n", b);
		}*/
		
		int version = packet[0] & 0xFF; // 3 or 4
		
		if(version == 3) {
			// 1, 2, 3, 4 = time of key generation
			// 5, 6 = time in days that this key is valid.
			int public_key_algo = packet[7]; 
			// rest: series of multiprecision integers comprising the key material.
			
			System.err.println("TODO: v3 key, algo = " +public_key_algo);
		} else {
			// 1, 2, 3, 4 = time of key generation
			int public_key_algo = packet[5]; 
			// rest: A series of multiprecision integers comprising the key material.
			
			// for MPI, see: https://tools.ietf.org/html/rfc4880#section-3.2
			
			if(public_key_algo == 1) { // RSA
				int modul_n_len = ((packet[6] & 0xFF) << 8) | (packet[7] & 0xFF);
				int modul_n_octets = ((modul_n_len+7)/8);
				// multiprecision integer (MPI) of RSA public modulus n;
				System.out.println("rsa modul len = " +modul_n_len + " = " +modul_n_octets +" octets");
				
				byte[] mb = new byte[modul_n_octets];
				for(int i = 1; i < modul_n_octets; i++) {
					mb[i] = packet[8+i];
				}
				BigInteger modulus = new BigInteger(1, mb);
				
				int exponent_e_len = ((packet[8+modul_n_octets] & 0xFF) << 8) | (packet[9+modul_n_octets] & 0xFF);
				int exponent_e_octets = ((exponent_e_len+7)/8);
				//  MPI of RSA public encryption exponent e.
				System.out.println("rsa public exponent len = " +exponent_e_len + " = " +exponent_e_octets +" octets");
				
				byte[] ex = new byte[exponent_e_octets];
				for(int i = 0; i < exponent_e_octets; i++) {
					ex[i] = packet[8+modul_n_octets+2+i];
				}
				BigInteger publicExponent = new BigInteger(1, ex);
				
				RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modulus, publicExponent);
				try {
					pub = KeyFactory.getInstance("RSA").generatePublic(pubSpec);
				} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
				// continue parsing private key with https://tools.ietf.org/html/rfc4880#section-5.5.3
				
				int start_secret_key = 8 +modul_n_octets +2 +exponent_e_octets;
				System.out.printf("string-to-key usage convention is 0x%02X ...\n", packet[start_secret_key]);
				
				if(packet[start_secret_key] != 0) {
					System.err.println("TODO: string-to-key specifier is being given");
					
					// pay attention to not access this field again is following steps
				} 
				
					/* Plain or encrypted multiprecision integers comprising the secret
				       key data.  These algorithm-specific fields are as described
				       below. */
				
				// multiprecision integer (MPI) of RSA secret exponent d.  => privateExponent
				
				int privateExponent_e_len = ((packet[start_secret_key+1] & 0xFF) << 8) | (packet[start_secret_key+2] & 0xFF);
				int privateExponent_e_octets = ((privateExponent_e_len+7)/8);
				//  MPI of RSA public encryption exponent e.
				System.out.println("rsa private exponent len = " +privateExponent_e_len + " = " +privateExponent_e_octets +" octets");
				
				byte[] ex2 = new byte[privateExponent_e_octets];
				for(int i = 0; i < privateExponent_e_octets; i++) {
					ex2[i] = packet[start_secret_key +3 +i];
				}
				BigInteger privateExponent = new BigInteger(1, ex2);
				
				// MPI of RSA secret prime value p.
				int primeP_len = ((packet[start_secret_key+3+privateExponent_e_octets] & 0xFF) << 8) | (packet[start_secret_key+3+privateExponent_e_octets+1] & 0xFF);
				int primeP_octets = ((primeP_len+7)/8);
				System.out.println("p len = " +primeP_len + " = " +primeP_octets +" octets");
				
				byte[] p = new byte[primeP_octets];
				for(int i = 0; i < primeP_octets; i++) {
					//System.out.println("reading from index " +(start_secret_key+privateExponent_e_octets+4 +i) +" of " +packet.length +" want to read more:" +(primeP_octets-i));
					p[i] = packet[start_secret_key+privateExponent_e_octets+5 +i];
				}
				BigInteger primeP = new BigInteger(1, p);
				
				// MPI of RSA secret prime value q (p < q).
				int primeQ_len = ((packet[start_secret_key +privateExponent_e_octets +5 +primeP_octets] & 0xFF) << 8) | (packet[start_secret_key +privateExponent_e_octets +5 +primeP_octets+1] & 0xFF);
				int primeQ_octets = ((primeQ_len+7)/8);
				
				byte[] q = new byte[primeQ_octets];
				for(int i = 0; i < primeQ_octets; i++) {
					q[i] = packet[start_secret_key +privateExponent_e_octets +primeP_octets +7 +i];
				}
				BigInteger primeQ = new BigInteger(1, q);
				
				System.out.println("now at " +(start_secret_key +privateExponent_e_octets +primeP_octets +7 +primeQ_octets) +" from " +packet.length);
				
				// this is d mod (p-1)
				BigInteger primeExponentP = privateExponent.remainder(primeP.subtract(BigInteger.valueOf(1)));
				
				// this is d mod (q-1)
				BigInteger primeExponentQ = privateExponent.remainder(primeQ.subtract(BigInteger.valueOf(1)));
				
				// MPI of u, the multiplicative inverse of p, mod q.
				// TODO parse next MPI and compare with:
				BigInteger crtCoefficient = primeQ.modInverse(primeP);
					
				// TODO checksum
				
				RSAPrivateCrtKeySpec privSpec = new RSAPrivateCrtKeySpec(modulus,
																			publicExponent,
																			privateExponent,
																			primeP,
																			primeQ,
																			primeExponentP,
																			primeExponentQ,
																			crtCoefficient);
				try {
					priv = KeyFactory.getInstance("RSA").generatePrivate(privSpec);
				} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				
				
				System.out.println("-- Done parsing RSA key: ---------------------------------------");
				System.out.println(pub.toString());
				System.out.println(priv.toString());
			} else {
				System.err.println("TODO: v4 key, algo = " +public_key_algo);
			}
		}
	}

}
