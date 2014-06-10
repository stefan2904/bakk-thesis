PGPStream s = new PGPStream(FILE);
Transferable t;

t = s.readObject();
// t.getClass().getCanonicalName()

if (t instanceof PGPPrivateKey) {
    doSomething(t);
} else // ...

