IAIK.addAsProvider();

PGPMessage pgpMessage = parseMessage(fis1);
PGPPrivateKey privKey = parsePrivatekey(fis2);

PGPCipher cipher = new PGPCipher();
cipher.init(PGPCipher.DECRYPT_MODE, privkey);

byte[] cleartext = cipher.doFinal(pgpmessage);
