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

import couk.nucmedone.shinyplopper.chambers.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortList;

public class PloppyConfig {

	private final Stage stage;
	private final PloppyProps props;

	public PloppyConfig() {

		props = new PloppyProps();


		// The chamber type
		// ComboBox<String> chamberCombo = new ComboBox<String>();
		HBox chamberBox = hbox("Chamber type:", props.getChamberType(),
				chamberTypes());

		// The serial device config
		getPortNames();
		HBox devBox = hbox("Serial device:", props.getDevice(), getPortNames());
		HBox baudBox = hbox("Baud rate:", props.getBaudrate(), baudrates());
		HBox parityBox = hbox("Parity", props.getParity(), parities());
		HBox startBitsBox = hbox("Data bits:", props.getDataBits(), databits());
		HBox stopBitsBox = hbox("Stop bits:", props.getStopBits(), stopBits());
		HBox toleranceBox = hbox("Tolerance", props.getTolerance());
		HBox refreshBox = hbox("Refresh rate:", props.getRefreshRate());
		HBox maxTimeBox = hbox("Maximum time:", props.getMaxTime());
		HBox minReadsBox = hbox("Minimum reads:", props.getMinReads());

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

	private HBox hbox(String labeltext, String item) {

		HBox box = new HBox();
		box.setPadding(new Insets(2));
		box.setSpacing(10);

		Label theLabel = new Label(labeltext);
		Label val = new Label(item);

		box.getChildren().addAll(theLabel, val);

		return box;
	}

	private HBox hbox(String labeltext, String item, ObservableList<String> list) {

		HBox box = new HBox();
		box.setAlignment(Pos.CENTER_LEFT);
		box.setPadding(new Insets(2));
		box.setSpacing(10);

		Label theLabel = new Label(labeltext);
		final ComboBox<String> combo = new ComboBox<String>(list);
		if (item != null) {
			combo.setValue(item);
		}

		box.getChildren().addAll(theLabel, combo);

		return box;
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

}
