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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortList;
import couk.nucmedone.shinyplopper.chambers.Constants;

public class PloppyConfig implements ConfigItemInterface {

	private final Stage stage;
	private final PloppyProps props;

	public PloppyConfig() {

		props = new PloppyProps();


		// The chamber type
		// ComboBox<String> chamberCombo = new ComboBox<String>();
		HBox chamberBox = new ConfigItem(PloppyProps.CHAMBER_TYPE, props.getChamberType(),
				chamberTypes(), this);

		// The serial device config
		HBox devBox = new ConfigItem(PloppyProps.SERIAL_DEVICE, props.getDevice(), getPortNames(), this);
		HBox baudBox = new ConfigItem(PloppyProps.BAUD_RATE, props.getBaudrate(), baudrates(), this);
		HBox parityBox = new ConfigItem(PloppyProps.PARITY, props.getParity(), parities(), this);
		HBox startBitsBox = new ConfigItem(PloppyProps.DATA_BITS, props.getDataBits(), databits(), this);
		HBox stopBitsBox = new ConfigItem(PloppyProps.STOP_BITS, props.getStopBits(), stopBits(), this);
		HBox toleranceBox = new ConfigItem(PloppyProps.TOLERANCE, props.getTolerance(), this);
		HBox refreshBox = new ConfigItem(PloppyProps.REFRESH_RATE, props.getRefreshRate(), this);
		HBox maxTimeBox = new ConfigItem(PloppyProps.MAX_TIME, props.getMaxTime(), this);
		HBox minReadsBox = new ConfigItem(PloppyProps.MIN_READS, props.getMinReads(), this);

		// Line up everything vertically
		VBox propsBox = new VBox();
		propsBox.setPadding(new Insets(10));
		propsBox.getChildren().addAll(chamberBox, devBox, baudBox, parityBox,
				startBitsBox, stopBitsBox, toleranceBox, refreshBox,
				maxTimeBox, minReadsBox);

		stage = new Stage();
		stage.setTitle("My New Stage Title");
		stage.setScene(new Scene(propsBox, 450, 450));

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

	public void show() {
		stage.show();
	}

	private ObservableList<String> stopBits() {
		return FXCollections.observableArrayList("" + SerialPort.STOPBITS_1, ""
				+ SerialPort.STOPBITS_2, "" + SerialPort.STOPBITS_1_5);
	}

	@Override
	public void onItemUpdate() {
		// TODO Auto-generated method stub

	}

}
