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

import couk.nucmedone.shinyplopper.chambers.Reading;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CalibratorScreen {

	private GridPane grid = new GridPane();
	private Reading readings = null;
	private Label units = new Label("---");
	private Label activity = new Label("----.-");
	private Label nuclide = new Label("ZZ-000m");
	private Label[] previousLists = new Label[10];

	public CalibratorScreen(){
		
        // Previous calibrator readings
        VBox previousVBox = new VBox();        
        for(int i=0; i<10; i++){
        	previousLists[i] = new Label("0 MBq" + i);
        	previousLists[i].setId("previous-readings");
        	previousVBox.getChildren().add(previousLists[i]);
        }
                
        activity.setId("reading");
        units.setId("units");
        nuclide.setId("nuclide");
        
        // Reading and units together... extra box for units to set bottom padding
        VBox unitBox = new VBox();
        unitBox.getChildren().add(units);
        unitBox.setPadding(new Insets(10));
        unitBox.setAlignment(Pos.BOTTOM_RIGHT);
        
        HBox readWithUnits = new HBox();
        readWithUnits.setAlignment(Pos.BOTTOM_RIGHT);
        readWithUnits.getChildren().addAll(activity, unitBox);
        
        // Nuclide box
        HBox nuclideBox = new HBox();
        nuclideBox.setAlignment(Pos.BOTTOM_RIGHT);
        nuclideBox.getChildren().addAll(nuclide);
        
        grid.setId("reading-pane");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.BOTTOM_LEFT);
        // Add previous readings in zeroth column (all rows)
        grid.add(previousVBox, 0, 0, 1, 3);
        // Nuclide bit in 1st row
        grid.add(nuclideBox, 2, 1);
        // Calibrator reading in 2nd row
        grid.add(readWithUnits, 2, 2);
		
	}
	
	public Pane getPane(){
		return grid;
	}
	
	public void setReadings(Reading readings){
		this.readings = readings;
	}
	
	public void update(){
		
		// Set current activity
		final double currentReading = readings.getCurrentReading();
		final double read = Reading.roundToSignificantFigures(currentReading, 5);
		final String text = Double.toString(read);
		
		activity.setText(text);
		
		// Set units
		units.setText(readings.getUnits().toString());
		
		// Set nuclide
		nuclide.setText(readings.getNuclide().toString());
		
		final double[] prevs = readings.getReadings();
		
		for (int i=0; i<previousLists.length; i++){
			
			final double last = Reading.roundToSignificantFigures(prevs[i], 5);
			
			StringBuffer buff = new StringBuffer(Double.toString(last));
			buff.append(" ");
			buff.append(units.getText());
			
			previousLists[i].setText(buff.toString());
		}
		
	}
	
}
