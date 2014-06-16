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
package couk.nucmedone.shinyplopper;

import java.util.Properties;

public class PloppyProps {

	public static final String BAUD_RATE = "couk.nucmedone.shinyplopper.device";
	public static final String CHAMBER_TYPE = "couk.nucmedone.shinyplopper.type";
	public static final String MAX_TIME = "couk.nucmedone.shinyplopper.max_time";
	public static final String PARITY = "couk.nucmedone.shinyplopper.parity";
	public static final String REFRESH_RATE = "couk.nucmedone.shinyplopper.refresh";
	public static final String SERIAL_DEVICE = "couk.nucmedone.shinyplopper.device";
	public static final String START_BITS = "couk.nucmedone.shinyplopper.start_bits";
	public static final String STOP_BITS = "couk.nucmedone.shinyplopper.stop_bits";
	public static final String TOLERANCE = "couk.nucmedone.shinyplopper.tolerance";
	
	private final Properties p;
	
	public PloppyProps(){
		p = System.getProperties();
	}
	
	public String getBaudrate(){
		return p.getProperty(BAUD_RATE, "4800");
	}
	
	public String getChamberType(){
		return p.getProperty(CHAMBER_TYPE, "Capintec CRC35R");
	}
	
	public String getDevice(){
		return p.getProperty(SERIAL_DEVICE, "No device selected");
	}
	
	public String getMaxTime(){
		return p.getProperty(MAX_TIME, "30");
	}
	
	public String getParity(){
		return p.getProperty(PARITY, "N");
	}
	
	public String getRefreshRate(){
		return p.getProperty(REFRESH_RATE, "0.25");
	}
	
	public String getDataBits(){
		return p.getProperty(START_BITS, "8");
	}
	
	public String getStopBits(){
		return p.getProperty(STOP_BITS, "1");
	}
	
	public String getTolerance(){
		return p.getProperty(TOLERANCE, "0.02");
	}
	
	public void setBaudrate(String baudrate){
		p.put(BAUD_RATE, baudrate);
	}
	
	public void setChamberType(String chamberType){
		p.put(CHAMBER_TYPE, chamberType);
	}
	
	public void setDevice(String device){
		p.put(SERIAL_DEVICE, device);
	}
	
	public void setMaxTime(String maxTime){
		p.put(MAX_TIME, maxTime);
	}
	
	public void setParity(String parity){
		p.put(PARITY, parity);
	}
	
	public void setRefreshRate(String refreshRate){
		p.put(REFRESH_RATE, refreshRate);
	}
	
	public void setDataBits(String startBits){
		p.put(START_BITS, startBits);
	}
	
	public void setStopBits(String stopBits){
		p.put(STOP_BITS, stopBits);
	}
	
	public void setTolerance(String tolerance){
		p.put(TOLERANCE, tolerance);
	}
	
}