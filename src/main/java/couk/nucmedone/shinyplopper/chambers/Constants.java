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

public class Constants {

	public static final Map<String, String> chambers;

	public static final Map<String, Double> units;

	static {

		chambers = new HashMap<String, String>();
		chambers.put("Capintec CRC35R", CRC35R.class.getName());
		chambers.put("Passive reader", AbstractChamber.class.getName());

		units = new HashMap<String, Double>();
		units.put("Bq", 1d);
		units.put("kBq", exp(3d));
		units.put("MBq", exp(6d));
		units.put("GBq", exp(9d));
		// Someone really going to put a TBq source in one of these things?
		units.put("TBq", exp(12d));
		// Account for poor taste :P
		units.put("uCi", ciToBq(exp(-6d)));
		units.put("mCi", ciToBq(exp(-3d)));
		units.put("Ci", ciToBq(1d));
		units.put("MCi", ciToBq(exp(3d))); // Unlikey in Nuclear Med
	}

	/**
	 * @return 10^radix
	 */
	private static double exp(double radix) {
		return Math.pow(10d, radix);
	}

	/**
	 * Convert old archaic units into SI units... just in case Americans ever
	 * use this software ;-)
	 *
	 * @param Ci
	 * @return The bequerel equivalent value of the input Ci value.
	 */
	public static double ciToBq(double Ci){
		return Ci * 3.7 * Math.pow(10d, 10d);
	}

	/**
	 * Convert old archaic units into SI units... just in case Americans ever
	 * use this software ;-)
	 *
	 * @param mCi
	 *
	 * @return The megabequerel equivalent value of the input mCi value.
	 */
	public static double mCiToMBq(double mCi) {
		return 37d*mCi;
	}

	private Constants() {

	}

}
