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

import jssc.SerialPort;
import jssc.SerialPortException;
import couk.nucmedone.shinyplopper.PloppyProps;

public abstract class AbstractChamber extends Thread implements Chamber {

	protected Map<String, String> unitMap = new HashMap<String, String>();

	private SerialPort serialPort;

	private final PloppyProps props = new PloppyProps();

	public AbstractChamber() {
		super();
		populateUnitMap();

		String port = props.getDevice();

		if (port != null && !port.equals(PloppyProps.NO_DEVICE)) {

			serialPort = new SerialPort(port);
			try {

				int baudRate = Integer.parseInt(props.getBaudrate());
				int dataBits = Integer.parseInt(props.getDataBits());
				int stopBits = Integer.parseInt(props.getStopBits());
				int parity = props.getParityAsInteger();

				serialPort.openPort();
				serialPort.setParams(baudRate, dataBits, stopBits, parity);

			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void populateUnitMap();
	public abstract void read();
	public abstract void setNuclide(CharSequence nuclide);
}
