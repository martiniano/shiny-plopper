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


public class Reading {

	private final double[] readings;
	private double mean = Double.NaN;
	private double current = Double.NaN;

	public Reading() {

		// New array of readings. Initialise as NaN to ease running average
		readings = new double[16];
		for (int i = 0; i < readings.length; i++) {
			readings[i] = Double.NaN;
		}

	}

	public void addReading(double reading) {

		// Shift readings "up one"
		for (int i = 0; i < readings.length - 1; i++) {
			readings[i] = readings[i + 1];
		}

		current = reading;
		readings[readings.length - 1] = reading;

		// Calculate new average
		int count = 0;
		int total = 0;

		for (int i = 0; i < readings.length; i++) {
			if (readings[i] != Double.NaN && reading != Double.NaN) {
				count++;
				total += reading;
			}
		}

		if (count > 0) {
			mean = total / count;
		}

	}

	public double getCurrentReading() {
		return current;
	}

	public double[] getReadings() {
		return readings;
	}

	public double getRunningAverage() {
		return mean;
	}

}
