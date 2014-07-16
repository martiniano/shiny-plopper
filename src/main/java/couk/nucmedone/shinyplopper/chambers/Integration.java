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

/**
 * Object to hold a reading accumulating over time. May be useful for low
 * activity comparisons for QC/QA.
 *
 * @author neil
 *
 */
public class Integration {

	private double value = 0;
	private int count = 0;

	public Integration() {
	}

	public void addReading(double value, String units) {

		// Reading may change units over time. For instance, the scale may
		// change from 0.7MBq to 690kBq. Ignoring units would clearly balls-up
		// the integration
		double scale;
		try {
			scale = Constants.units.get(units);
		} catch (NullPointerException npe) {

			npe.printStackTrace();

			// Unity is assumed if the unit is not listed in the Map. Could be
			// an electrometer reading.
			scale = 1;
		}

		this.value += (value * scale);
		count++;

	}

	public int getCount(){
		return count;
	}
	
	public double getValue() {
		return value;
	}

	public void reset() {
		value = 0;
		count = 0;
	}

}
