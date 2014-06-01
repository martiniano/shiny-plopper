/**
 * @author Neil J Thomson (njt@fishlegs.co.uk)
 *
 * Copyright (C) 2013 Neil J Thomson
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 */
package couk.nucmedone.shinyplopper.chambers;

import java.util.HashMap;
import java.util.Map;

public class CRC35R {

	public static final char A = 'A';
	public static final int STX = 2;
	public static final int ETX = 3;

	protected Map<String, String> unitMap = new HashMap<String, String>();
	
	protected String port = "com1";
	protected int baudrate = 4800;
	protected String parity = "N";
	protected int charBits = 8;
	protected int stopBit = 1;

	public double tolerance = 0.02;

	public CRC35R() {
		unitMap.put("3", "kBq");
		unitMap.put("4", "kBq");
		unitMap.put("5", "GBq");
	}

	public void read() {

		// From the CRC-35R manual:
		// When reading a chamber the response from the Capintec will be in the
		// format DRx,ABnnnnnn,aaaaaaU ABnnnnnn - isotope name or calibration
		// number (a division is read as an "&") padded with spaces. aaaaaa is
		// the activity string and U is the units, 3, 4, 5 for K,M & GBq

		String chamber = "R1";

		// Create command string
		StringBuffer cmd = new StringBuffer(40);

		// STX
		cmd.append(STX);

		// Length of command (plus "A")
		cmd.append(commandLength(chamber));

		// Read
		cmd.append(chamber);

		// Append the checksum
		int checksum = getReadChecksum(chamber);
		cmd.append(checksum);

	}

	public void setNuclide(CharSequence nuclide) {

		// Initialise chamber. The CRC35 unit can have up to 8 chambers
		// attached. Just use first one.
		String chamber = "I1";

		// Nuclide string must be padded to 6 characters
		StringBuffer nuclideBuf = new StringBuffer(nuclide);
		while (nuclideBuf.length() < 6) {
			nuclideBuf.append(" ");
		}

		// Build the command string to send to the Capintec.
		StringBuffer command = new StringBuffer(40);

		// STX
		command.append(STX);

		// Length of the chamber + nuclide and an "A"...? Don't ask!
		command.append(commandLength(nuclide + chamber));

		// Chamber and nuclide
		command.append(chamber);
		command.append(nuclide);

		// Append 32... for some reason
		command.append(32);

		// Create a checksum
		int checksum = getSetChecksum(command.charAt(1));
		command.append(checksum);

		// End text
		command.append(ETX);

		// If we find spaces at start of data stream, resend the data.
	}

	private int commandLength(CharSequence chamberCommand) {
		return chamberCommand.length() + A;
	}

	private int getReadChecksum(CharSequence chamber) {

		int len = chamber.length();
		int checksum = STX + len + 0x41;
		for (int i = 0; i < len; i++) {
			checksum += chamber.charAt(i);
		}
		checksum &= 0x7f;
		checksum += 0x21;

		return checksum;
	}

	/**
	 * Create a checksum using the recipe from the Capintec manual
	 * 
	 * @param character
	 * @return
	 */
	private int getSetChecksum(char character) {

		int checksum = STX;
		checksum += character;
		checksum = checksum & 0x7f;
		checksum += 0x21;

		return checksum;

	}

}
