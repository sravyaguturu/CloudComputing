package BroadCastServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NoRouteToHostException;

import constants.Constants;

// TODO: Auto-generated Javadoc
/**
 * The Class MulticastServer is the class which is the class responsible to send
 * the UDP packet. The UDP packet has the IP address and port number which is
 * used by the tablet to connect to the server. It uses the BroadCast port to
 * broadcast the UDP packet.
 */
public class MulticastServer implements Runnable {

	/** The running. */
	private boolean running = true;

	/** The multicastSocket. */
	MulticastSocket multicastSocket;

	/** The packet. */
	DatagramPacket packet;

	/** The broad cast message. */
	String broadCastMessage;

	/** The latitude. */
	String latitude;

	/** The longitude. */
	String longitude;

	/**
	 * Instantiates a new multicast server.
	 * 
	 * @param message
	 *            the message
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 */
	public MulticastServer(String message, String latitude, String longitude) {
		broadCastMessage = message;
		this.latitude = latitude;
		this.longitude = longitude;

	}

	/**
	 * Start broad cast so the data gram packet can be sent to all the tablets
	 * connected in the Factwireless1 network. The DatagramPacket packet has the
	 * IP address and port number which the tablet uses to connect to the
	 * server.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws NoRouteToHostException
	 *             the no route to host exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 */

	public void startBroadCast() throws IOException, NoRouteToHostException,
			InterruptedException {
		multicastSocket = new MulticastSocket();
		multicastSocket.setInterface(InetAddress.getLocalHost());

		String localIp = InetAddress.getLocalHost().getHostAddress();
		String packetContents = localIp + ":" + 6543 + broadCastMessage + "#"
				+ latitude + "#" + longitude + "$";

		byte buf[] = packetContents.getBytes();

		packet = new DatagramPacket(buf, buf.length,
				InetAddress.getByName(Constants.GROUP_IP), 6543);
		multicastSocket.send(packet);
		try {
			Thread.sleep(1000);
		} finally {
			multicastSocket.close();
		}

	}

	/**
	 * This method closes the multi cast server.
	 */
	public void closeMultiCastServer() {
		running = false;
		multicastSocket.close();

	}

	/**
	 * This will start the thread and the call the startBroadCast() method which
	 * will broadcast the datagram packet.
	 */

	public void run() {
		try {
			startBroadCast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
