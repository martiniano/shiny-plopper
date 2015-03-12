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
package couk.nucmedone.shinyplopper.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import couk.nucmedone.shinyplopper.chambers.Constants;
import jssc.SerialPort;

public class PloppyProps {

	private static final int DEFAULT_CONFIG_HEIGHT = 450;
	private static final int DEFAULT_CONFIG_WIDTH = 450;
	
	public static final String HEIGHT = "config_height";
	public static final String SAVE_FILE = "sp.properties";
	public static final String WIDTH = "config_width";

	public static final String BAUD_RATE = "couk.nucmedone.shinyplopper.baud";
	public static final String CHAMBER_TYPE = "couk.nucmedone.shinyplopper.type";
	public static final String DATA_BITS = "couk.nucmedone.shinyplopper.data_bits";
	public static final String MIN_READS = "couk.nucmedone.shinyplopper.min_reads";
	public static final String MAX_TIME = "couk.nucmedone.shinyplopper.max_time";
	public static final String NO_DEVICE = "No device selected";
	public static final String PARITY = "couk.nucmedone.shinyplopper.parity";
	public static final String REFRESH_RATE = "couk.nucmedone.shinyplopper.refresh";
	public static final String SERIAL_DEVICE = "couk.nucmedone.shinyplopper.device";
	public static final String STOP_BITS = "couk.nucmedone.shinyplopper.stop_bits";
	public static final String TOLERANCE = "couk.nucmedone.shinyplopper.tolerance";

	public static final Map<String, Integer> PARITIES = new HashMap<String, Integer>();

	public static final Map<String, String> FRIENDLY_NAMES = new HashMap<String, String>();

	static {
		FRIENDLY_NAMES.put(BAUD_RATE, "Baud rate");
		FRIENDLY_NAMES.put(CHAMBER_TYPE, "Chamber type");
		FRIENDLY_NAMES.put(DATA_BITS, "Start bits");
		FRIENDLY_NAMES.put(MIN_READS, "Minimum reads");
		FRIENDLY_NAMES.put(MAX_TIME, "Maximum time");
		FRIENDLY_NAMES.put(PARITY, "Parity");
		FRIENDLY_NAMES.put(REFRESH_RATE, "Refresh rate");
		FRIENDLY_NAMES.put(SERIAL_DEVICE, "Serial device");
		FRIENDLY_NAMES.put(STOP_BITS, "Stop bits");
		FRIENDLY_NAMES.put(TOLERANCE, "Tolerance");
	}

	private final Properties p;

	public PloppyProps() {

		// Get saved props
		p = load();

		// Populate hashmap of parity items against integer values
		PARITIES.put("None", SerialPort.PARITY_NONE);
		PARITIES.put("Even", SerialPort.PARITY_EVEN);
		PARITIES.put("Mark", SerialPort.PARITY_MARK);
		PARITIES.put("Odd", SerialPort.PARITY_ODD);
		PARITIES.put("Space", SerialPort.PARITY_SPACE);

	}

	public String getBaudrate() {
		return p.getProperty(BAUD_RATE, "4800");
	}

	public String getChamberType() {
		return p.getProperty(CHAMBER_TYPE, Constants.CHAM_RND); // "Capintec CRC35R");
	}
	
	public int getConfigHeight() {
		
		Object o = p.get(HEIGHT);
		

		
	}
	
	public void setConfigWidth(int width) {
		p.put(WIDTH, width);
	}

	public String getDevice() {
		return p.getProperty(SERIAL_DEVICE, NO_DEVICE);
	}

	public String getMaxTime() {
		return p.getProperty(MAX_TIME, "30");
	}

	public String getMinReads() {
		return p.getProperty(MIN_READS, "16");
	}

	public String getParity() {
		return p.getProperty(PARITY, "None");
	}

	public int getParityAsInteger() {
		return PARITIES.get(getParity());
	}

	public String getRefreshRate() {
		return p.getProperty(REFRESH_RATE, "0.25");
	}

	public String getDataBits() {
		return p.getProperty(DATA_BITS, "8");
	}

	public String getStopBits() {
		return p.getProperty(STOP_BITS, "1");
	}

	public String getTolerance() {
		return p.getProperty(TOLERANCE, "0.02");
	}

	private Properties load() {

		Properties props = new Properties();
		InputStream is = null;

		// User directory
		// file = new File(System.getProperty("user.dir") +
		// "/shinyplopper.config");

		// First try loading from the current directory
		try {
			File f = new File(SAVE_FILE);
			is = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// is remains null
		}

		try {
			if (is == null) {
				// Try loading from classpath
				is = getClass().getResourceAsStream(SAVE_FILE);
			}

			// Try loading properties from the file (if found)
			if (is != null) {
				props.load(is);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return props;

	}

	public void save() {

		FileOutputStream fos = null;
		try {
			File f = new File(SAVE_FILE);
			fos = new FileOutputStream(f);
			p.store(fos, " Shiny plopper save data ");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void set(String key, String value) {
		p.put(key, value);
	}

	public void setBaudrate(String baudrate) {
		p.put(BAUD_RATE, baudrate);
	}

	public void setChamberType(String chamberType) {
		p.put(CHAMBER_TYPE, chamberType);
	}
	
	public void setConfigHeight(int height) {
		p.put(HEIGHT, height);
	}
	
	public void setConfigWidth(int width) {
		p.put(WIDTH, width);
	}

	public void setDevice(String device) {
		p.put(SERIAL_DEVICE, device);
	}

	public void setMaxTime(String maxTime) {
		p.put(MAX_TIME, maxTime);
	}

	public void setMinReads(String minReads) {
		p.put(MIN_READS, minReads);
	}

	public void setParity(String parity) {
		p.put(PARITY, parity);
	}

	public void setRefreshRate(String refreshRate) {
		p.put(REFRESH_RATE, refreshRate);
	}

	public void setDataBits(String startBits) {
		p.put(DATA_BITS, startBits);
	}

	public void setStopBits(String stopBits) {
		p.put(STOP_BITS, stopBits);
	}

	public void setTolerance(String tolerance) {
		p.put(TOLERANCE, tolerance);
	}

}