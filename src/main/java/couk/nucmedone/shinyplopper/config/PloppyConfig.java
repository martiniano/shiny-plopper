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

import java.util.ArrayList;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jssc.SerialPort;
import jssc.SerialPortList;
import couk.nucmedone.shinyplopper.chambers.Constants;

public class PloppyConfig {

	private final Stage stage;
	private final PloppyProps props;
	private final ArrayList<ConfigItem> configItems;

	public PloppyConfig() {

		props = new PloppyProps();

		// Line up everything vertically
		VBox propsBox = new VBox();
		propsBox.setPadding(new Insets(10));

		// The serial device config
		configItems = new ArrayList<ConfigItem>();
		configItem(PloppyProps.CHAMBER_TYPE, props.getChamberType(), chamberTypes());
		configItem(PloppyProps.SERIAL_DEVICE, props.getDevice(), getPortNames());
		configItem(PloppyProps.BAUD_RATE, props.getBaudrate(), baudrates());
		configItem(PloppyProps.PARITY, props.getParity(), parities());
		configItem(PloppyProps.DATA_BITS, props.getDataBits(), databits());
		configItem(PloppyProps.STOP_BITS, props.getStopBits(), stopBits());
		configItem(PloppyProps.TOLERANCE, props.getTolerance());
		configItem(PloppyProps.REFRESH_RATE, props.getRefreshRate());
		configItem(PloppyProps.MAX_TIME, props.getMaxTime());
		configItem(PloppyProps.MIN_READS, props.getMinReads());

		Iterator<ConfigItem> it = configItems.iterator();
		while(it.hasNext()){
			propsBox.getChildren().add(it.next());
		}

		stage = new Stage();
		stage.setTitle("Configuration");
		Image ico = new Image("couk/nucmedone/shinyplopper/images/configure-3-16.png");
		stage.getIcons().add(ico);
		
		double w = props.getConfigWidth();
		double h = props.getConfigHeight();
		stage.setScene(new Scene(propsBox, w, h));

		stage.setOnShowing(new EventHandler<WindowEvent>() {
			
			public void handle(WindowEvent arg0) {
				
				double ww = props.getConfigWidth();
				stage.setWidth(ww);
				
				double hh = props.getConfigHeight();								
				stage.setHeight(hh);			
				
			}
			
		});
		
		// Save props when hiding window
		stage.setOnHiding(new EventHandler<WindowEvent>() {

			public void handle(WindowEvent arg0) {

				// Store height and width of the window
				Double h = new Double(stage.getHeight());
				final int hh = h.intValue();
				System.out.println(h + " >> " + hh);
				props.setConfigHeight(hh);
				
				Double w = new Double(stage.getWidth());
				final int ww = w.intValue();
				System.out.println(w + " >> " + ww);
				props.setConfigWidth(ww);
				
				save();

			}
		});

	}

	private ObservableList<String> baudrates() {

		return FXCollections.observableArrayList("" + SerialPort.BAUDRATE_110,
				"" + SerialPort.BAUDRATE_300, "" + SerialPort.BAUDRATE_600, ""
						+ SerialPort.BAUDRATE_1200, ""
						+ SerialPort.BAUDRATE_4800, ""
						+ SerialPort.BAUDRATE_9600, ""
						+ SerialPort.BAUDRATE_14400, ""
						+ SerialPort.BAUDRATE_19200, ""
						+ SerialPort.BAUDRATE_38400, ""
						+ SerialPort.BAUDRATE_57600, ""
						+ SerialPort.BAUDRATE_115200, ""
						+ SerialPort.BAUDRATE_128000, ""
						+ SerialPort.BAUDRATE_256000);

	}

	private ObservableList<String> chamberTypes() {

		return FXCollections.observableArrayList(Constants.chambers.keySet());

	}

	private void configItem(String name, String item) {

		StringConfigItem configItem = new StringConfigItem(name, item);
		configItems.add(configItem);

	}

	private void configItem(String name, String item, ObservableList<String> list) {

		ComboConfigItem configItem = new ComboConfigItem(name, item);
		configItem.setOptions(list);
		configItems.add(configItem);

	}

	private ObservableList<String> databits() {
		return FXCollections.observableArrayList("" + SerialPort.DATABITS_5, ""
				+ SerialPort.DATABITS_6, "" + SerialPort.DATABITS_7, ""
				+ SerialPort.DATABITS_8);
	}

	private ObservableList<String> getPortNames() {

		String[] ports = SerialPortList.getPortNames();
		ObservableList<String> list = FXCollections.observableArrayList(ports);
		list.add(0, PloppyProps.NO_DEVICE);
		return list;

	}

	private ObservableList<String> parities() {
		return FXCollections.observableArrayList(PloppyProps.PARITIES.keySet());
	}

	protected void save() {

		Iterator<ConfigItem> it = configItems.iterator();
		while(it.hasNext()){

			ConfigItem item = it.next();

			String name = item.getName();
			String value = item.getItem();

			props.set(name, value);

		}

		props.save();

	}

	public void show() {
		stage.show();
	}

	private ObservableList<String> stopBits() {
		return FXCollections.observableArrayList("" + SerialPort.STOPBITS_1, ""
				+ SerialPort.STOPBITS_2, "" + SerialPort.STOPBITS_1_5);
	}

}
