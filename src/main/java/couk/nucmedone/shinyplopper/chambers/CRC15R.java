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

import jssc.SerialPortException;

public class CRC15R extends AbstractChamber {

	private final static String[] nuclideChars = new String[6];
	static {
		nuclideChars[0] = "";
	}

	@Override
	protected void populateUnitMap() {
		unitMap.put("1", "uCi");
		unitMap.put("2", "mCi");
		unitMap.put("3", "Ci");
		unitMap.put("4", "undefined");
		unitMap.put("5", "MBq");
		unitMap.put("6", "GBq");
	}

	@Override
	public void read() {
		try {

			// Begin communication
			open();
			// Tell capintec we are about to start reading
			write("@");

			// Get the nuclide one character at a time by sending A,B,C...F
			// A is decimal ASCII char 65
			int nextChar;
			nuclide = dataBuffer(65, 70);

			// Get the units (send "G")
			write("G");
			units = new StringBuffer();
			nextChar = getNextByte();
			units.append(unitMap.get(String.valueOf(nextChar)));

			// now get the activity - chars H (72) to K (75)
			update(dataBuffer(72, 75));

		} catch (SerialPortException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @throws SerialPortException
	 */
	private StringBuffer dataBuffer(int first, int last) throws SerialPortException {
		int nextChar;
		StringBuffer buffer = new StringBuffer(6);
		for (int c=first; c<last + 1; c++){
			write(String.valueOf(c));
			nextChar = getNextByte();
			buffer.append(nextChar);
		}
		return buffer;
	}

	@Override
	public void setNuclide(CharSequence nuclide) {
		System.out.println("Capintec CRC15R does not support nuclide control over RS-232");
	}

}
