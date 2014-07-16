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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CalibratorScreen {

	private GridPane grid = new GridPane();

	public CalibratorScreen(){
		
        // Previous calibrator readings
        VBox previousVBox = new VBox();
        Label[] previousList = new Label[10];
        for(int i=0; i<10; i++){
        	previousList[i] = new Label("Reading: " + i);
        	previousList[i].setId("previous-readings");
        	previousVBox.getChildren().add(previousList[i]);
        }
        
        // Calibrator reading
        Label reading = new Label("1000.0");
        reading.setId("reading");
        
        // Units
        Label units = new Label("MBq");
        units.setId("units");
        
        // Reading and units together... extra box for units to set bottom padding
        VBox unitBox = new VBox();
        unitBox.getChildren().add(units);
        unitBox.setPadding(new Insets(10));
        unitBox.setAlignment(Pos.BOTTOM_LEFT);
        
        HBox readWithUnits = new HBox();
        readWithUnits.setAlignment(Pos.BOTTOM_LEFT);
        readWithUnits.getChildren().addAll(reading, unitBox);
        
        grid.setId("reading-pane");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.BOTTOM_CENTER);
        // Add previous readings in zeroth column (all rows)
        grid.add(previousVBox, 0, 0, 1, 3);
        // Calibrator reading in 2nd row
        grid.add(readWithUnits, 2, 2);
		
	}
	
	public Pane getPane(){
		return grid;
	}
	
}
