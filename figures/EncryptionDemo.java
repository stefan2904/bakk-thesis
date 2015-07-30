// prepare plaintext:
byte[] plaintext = "hello".getBytes();
ByteArrayInputStream is = new ByteArrayInputStream(msg);

// initialize PGP Encrypter:
PGPData data = new PGPData();
data.setData(is);

// set encryption parameters:
PGPEncryptionInfo encInfo = new PGPEncryptionInfo();
// encrypt for recipient
encInfo.addEncryptionKey(receiver.getPub());
// encrypt for sender, too
encInfo.addEncryptionKey(sender.getPub());
// set symmetric cipher
encInfo.setSymmetricCipher(SymmetricKey.AES128);
data.setEncryptionInfo(encInfo);

// set signing parameters:
PGPSigningInfo sigInfo = new PGPSigningInfo();
sigInfo.setSigningKey(sender.getPriv());
sigInfo.setHashAlgoID(DigestCommons.SHA256);
data.addSigningInfo(sigInfo);

// prepare target for encryption
PGPWriter pgpout = new PGPWriter(out);

// perform encryption
data.encryptTo(pgpout, 2048);
pgpout.close();
