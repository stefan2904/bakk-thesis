// initialize PGPReader and load keyring:
PGPReader reader = new PGPReader(in);
PGPKeyRing keyRing = new PGPKeyRing(reader);

// get keypair by User ID
PGPKeyPair rsaRsa1024 = keyRing.getKeyPair("st <crypto@2904.cc>");
