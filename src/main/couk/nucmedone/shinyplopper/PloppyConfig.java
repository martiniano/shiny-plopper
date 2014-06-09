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

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PloppyConfig {

	private final Stage stage;
	private final PloppyProps props;

	public PloppyConfig() {

		props = new PloppyProps();

		// The chamber type
		// ComboBox<String> chamberCombo = new ComboBox<String>();
		HBox chamberBox = hbox("Chamber type:", props.getChamberType());

		// The serial device config
		HBox devBox = hbox("Serial device:", props.getDevice());
		HBox baudBox = hbox("Baud rate:", props.getBaudrate());
		HBox parityBox = hbox("Parity", props.getParity());
		HBox startBitsBox = hbox("Start bits:", props.getStartBits());
		HBox stopBitsBox = hbox("Stop bits:", props.getStopBits());
		HBox toleranceBox = hbox("Tolerance", props.getTolerance());
		HBox refreshBox = hbox("Refresh rate:", props.getRefreshRate());
		HBox maxTimeBox = hbox("Maximum time:", props.getMaxTime());

		// Line up everything vertically
		VBox propsBox = new VBox();
		propsBox.setPadding(new Insets(10));
		propsBox.getChildren()
				.addAll(chamberBox, devBox, baudBox, parityBox, startBitsBox,
						stopBitsBox, toleranceBox, refreshBox, maxTimeBox);

		stage = new Stage();
		stage.setTitle("My New Stage Title");
		stage.setScene(new Scene(propsBox, 450, 450));

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

	public void show() {
		stage.show();
	}

}
