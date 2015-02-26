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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

import jssc.SerialPortException;

public class CRC35R extends AbstractChamber {

	public static final char A = 'A';
	public static final byte STX = 2;
	public static final byte ETX = 3;	

	/**
	 * Read the CRC-35R over serial port
	 * 
	 * From the CRC-35R manual:<br /><br />
	 * 
	 * When reading a chamber the response from the Capintec will be in
	 * the format <i>DRx,ABnnnnnn,aaaaaaU</i> 
	 * <ul><li><i>ABnnnnnn</i> is isotope name or calibration number (a 
	 * division is read as an "&") padded with spaces.</li> 
	 * <li><i>aaaaaa</i> is the activity string and U is the units, 3, 4, 5 for K,M &
	 * GBq</li></ul>
	 */
	public void read() {

		if (getSerialPort() != null) {

			String chamber = "R1";

			// Create command string
			StringBuffer cmd = new StringBuffer(40);
			CharBuffer chars = CharBuffer.allocate(40);
			IntBuffer ints = IntBuffer.allocate(40);
			ByteBuffer bytes = ByteBuffer.allocate(6);

			// STX
			cmd.append(STX);
			ints.put(STX);
//			chars.put(STX);
			bytes.put((byte)STX);

			// Length of command (plus "A")
			cmd.append(commandLength(chamber));
			ints.put(commandLength(chamber));
			chars.put((char)commandLength(chamber));
			bytes.put((byte)commandLength(chamber));

			// Read command
			cmd.append(chamber);
			chars.put(chamber);
			byte[] chamBytes = chamber.getBytes();
			bytes.put(chamBytes);
			for(int i = 0; i<chamber.length(); i++){
				ints.put(chamBytes[i]);
			}

			// Append the checksum
			int checksum = getReadChecksum(chamber);
			cmd.append(checksum);
			ints.put(checksum);
			chars.put((char)checksum);
			bytes.put((byte)checksum);
			
			// Finish the message
			cmd.append(ETX);
			ints.put(ETX);
//			chars.put(ETX);
			bytes.put(ETX);

			try {
				// Send read command
				open();
				write(bytes);

				// Fill the buffer until ETX found
				StringBuffer sb = new StringBuffer(64);
				IntBuffer returnBuffer = IntBuffer.allocate(64);
				int nextChar;
				while ((nextChar = getNextByte()) != ETX) {
					sb.append(nextChar);
					returnBuffer.put(nextChar);
				}
				close();
				
				getActivity(returnBuffer);

				// Inform the listener about the info
				update();

			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Get the activity, units, etc
	 * 
	 * From the CRC-35R manual:<br /><br />
	 * 
	 * When reading a chamber the response from the Capintec will be in
	 * the format <i>DRx,ABnnnnnn,aaaaaaU</i> 
	 * <ul><li><i>ABnnnnnn</i> is isotope name or calibration number (a 
	 * division is read as an "&") padded with spaces.</li> 
	 * <li><i>aaaaaa</i> is the activity string and U is the units, 3, 4, 5 for K,M &
	 * GBq</li></ul>
	 */
	private void getActivity(IntBuffer returnBuffer) {
		
		// skip until first comma to get the nuclide
		int j=0;
		while(returnBuffer.get(j++) != 44);
		
		// Nuclide until next comma
		nuclide = new StringBuffer();
		char res;
		while((res = (char)returnBuffer.get(j++)) != 44){
			nuclide.append(Character.valueOf(res));
		}
		
		//Next 7 chars for a nuclide (long requirement for In-113m)
		activity = new StringBuffer();
		for(int i = 0; i < 6; i++){
			res = (char)returnBuffer.get(j++);
			activity.append(res);
		}
		
		// Get units from hashtable
		char u = (char) returnBuffer.get(j);
		String unit = unitMap.get(Character.toString(u));
		
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

		try {

			write(command.toString().getBytes());

			// If we find spaces at start of data stream, resend the data.

		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	private int commandLength(CharSequence chamberCommand) {
		return chamberCommand.length() + A;
	}

	private int getReadChecksum(CharSequence chamber) {

		int len = chamber.length();
		int checksum = len + 0x41;
		checksum += STX;
		
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

	@Override
	public void populateUnitMap() {
		unitMap.put("3", "kBq");
		unitMap.put("4", "MBq");
		unitMap.put("5", "GBq");
	}

}
