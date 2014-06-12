IAIK.addAsProvider();

PGPMessage pgpMessage = parseMessage(is1);
PGPPrivateKey privKey = parsePrivatekey(is2);

PGPCipher cipher = new PGPCipher();
cipher.init(PGPCipher.DECRYPT_MODE, privkey);

byte[] cleartext = cipher.doFinal(pgpmessage);
