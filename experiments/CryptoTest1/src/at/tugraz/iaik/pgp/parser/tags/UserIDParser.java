package at.tugraz.iaik.pgp.parser.tags;

public class UserIDParser extends PacketParser {

	public UserIDParser(byte[] packet_) {
		super(packet_);
	}

	@Override
	public void doFinal() {
		// https://tools.ietf.org/html/rfc4880#page-48
		System.out.println(new String(super.packet));
	}

}
