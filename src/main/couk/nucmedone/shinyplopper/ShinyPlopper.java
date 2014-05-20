/**
 * @author Neil J Thomson (njt@fishlegs.co.uk)
 *
 * Copyright (C) 2014 Neil J Thomson
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

import java.awt.Point;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import couk.nucmedone.shinyplopper.clickscreen.ClickScreenListener;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ShinyPlopper extends Preloader implements ClickScreenListener {

	private Label clickFeedbackLabel = new Label("");
	final Text info = new Text();

	@Override
	public void start(Stage stage) {

		// Register the application for keyboard and mouse events outside of the
		// JRE
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		
		// Construct the native listeners and initialise
		GlobalScreen.getInstance().addNativeKeyListener(new NativeKeyListener() {
			
			String text = "";
			
            public void nativeKeyPressed(NativeKeyEvent e) {
                text = NativeKeyEvent.getKeyText(e.getKeyCode());
                info.setText(text);

                if (e.getKeyCode() == NativeKeyEvent.VK_ESCAPE) {
                    GlobalScreen.unregisterNativeHook();
                }
            }

            public void nativeKeyReleased(NativeKeyEvent e) {
                System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
            }

            public void nativeKeyTyped(NativeKeyEvent e) {
                System.out.println("Key Typed: " + text);
            }
        });
		
		GlobalScreen.getInstance().addNativeMouseListener(new NativeMouseListener() {
			
			public void nativeMouseReleased(NativeMouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void nativeMousePressed(NativeMouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void nativeMouseClicked(NativeMouseEvent e) {
				Point point = e.getPoint();
				System.out.println("Mouse click at: " + point);
			}
		});
		
		

		Button listen = new Button("listen");
		listen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				new ClickScreenToo(ShinyPlopper.this);
			}
		});
		VBox layout = new VBox(10);
		layout.getChildren().setAll(listen, clickFeedbackLabel, info);
		layout.setPadding(new Insets(10));

		stage.setScene(new Scene(layout, 100, 80));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void screenClicked(Point2D point) {
		clickFeedbackLabel.setText(point.getX() + ", " + point.getY());
	}

}

class ClickScreenToo {
	private ClickScreenListener listener;
	private Stage window;
	private Point2D point;

	public ClickScreenToo(final ClickScreenListener listener) {
		// Get screen size
		Rectangle2D r = Screen.getPrimary().getBounds();

		// Something to put stuff in
		StackPane root = new StackPane();
		root.setStyle("-fx-background-color: null;");

		// Translucent rectangle on the pane
		Rectangle rectangle = new Rectangle(r.getWidth(), r.getHeight());
		rectangle.setFill(Color.rgb(183, 183, 183, 0.5));
		root.getChildren().add(rectangle);

		Scene scene = new Scene(root, r.getWidth(), r.getHeight());
		scene.setFill(null);

		window = new Stage();
		window.initStyle(StageStyle.TRANSPARENT);
		window.setTitle("Click drop location");
		window.setScene(scene);

		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				point = new Point2D(event.getScreenX(), event.getScreenY());
				listener.screenClicked(point);
				window.hide();
			}
		});

		window.show();

		this.listener = listener;
	}

	public Point2D getLocation() {
		return point;
	}
}