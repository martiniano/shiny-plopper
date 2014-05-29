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
package couk.nucmedone.shinyplopper.clickscreen;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClickScreen {
	
	private Stage window;
	
	public ClickScreen(final ClickScreenListener listener) {
		// Get screen size
		Rectangle2D r = Screen.getPrimary().getBounds();

		// Something to put stuff in
		StackPane root = new StackPane();
		root.setStyle("-fx-background-color: null;");

		// Translucent rectangle on the pane
		Rectangle rectangle = new Rectangle(r.getWidth(), r.getHeight());
		rectangle.setFill(Color.rgb(183, 183, 183, 0.5));
		
		// Instruction label
		Label label = new Label("Click screen to drop text or press Esc to cancel.");
		label.setId("clicker");
		
		HBox hbox = new HBox();
		hbox.getChildren().add(label);
		hbox.setAlignment(Pos.TOP_CENTER);
		hbox.setId("click-screen-message");
		
		root.getChildren().addAll(rectangle, hbox);

		Scene scene = new Scene(root, r.getWidth(), r.getHeight());
		scene.setFill(null);
		scene.getStylesheets().add("couk/nucmedone/shinyplopper/style.css");

		window = new Stage();
		window.initStyle(StageStyle.TRANSPARENT);
		window.setTitle("Click drop location");
		window.setScene(scene);
	}

	public void hide(){
		window.hide();
	}
	
	public void show(){
		window.show();
	}
}