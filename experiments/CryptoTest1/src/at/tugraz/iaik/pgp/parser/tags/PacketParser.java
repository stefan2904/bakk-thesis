package at.tugraz.iaik.pgp.parser.tags;

public abstract class PacketParser {
	protected byte[] packet;
	
	public PacketParser(byte[] packet_) {
		packet = packet_;
	}
	
	public abstract void doFinal();
}
