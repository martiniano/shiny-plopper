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

import couk.nucmedone.shinyplopper.PloppyProps;


public class Reading {

	private final double[] readings;
	private double mean = Double.NaN;
	private double current = Double.NaN;
	private double stability = Double.MAX_VALUE;
	private double tolerance;
	private int minReadings;
	private boolean isStable = false;

	public Reading() {

		PloppyProps props = new PloppyProps();
		try {
			tolerance = Double.parseDouble(props.getTolerance());
			minReadings = Integer.parseInt(props.getMinReads());
		} catch(NumberFormatException nfe){
			tolerance = 0.02;
			minReadings = 16;
		}

		// New array of readings. Initialise as NaN to ease running average
		readings = new double[minReadings];
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

		// Calculate new average and stability
		int count = 0;
		int total = 0;
		double max = -1 * Double.MAX_VALUE;
		double min = Double.MAX_VALUE;

		for (int i = 0; i < readings.length; i++) {
			if (readings[i] != Double.NaN && reading != Double.NaN) {
				count++;
				total += reading;
				max = readings[i] > max ? readings[i] : max;
				min = readings[i] < min ? readings[i] : min;
			}
		}

		if (count > 0) {
			mean = total / count;
			stability = (max - min)/mean;
			isStable = !(stability > tolerance);
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

	public boolean isStable(){
		return isStable;
	}

}
