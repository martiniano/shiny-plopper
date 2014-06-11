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

public abstract class AbstractChamber extends Thread implements Chamber {

	protected Map<String, String> unitMap = new HashMap<String, String>();
	
	protected String port = "com1";
	protected int baudrate = 4800;
	protected String parity = "N";
	protected int charBits = 8;
	protected int stopBit = 1;
	
	protected double tolerance = 0.02;
	
	public AbstractChamber(){
		super();
		setUnits();
	}
	
	public abstract void setUnits();
	
}
