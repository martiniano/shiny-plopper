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

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import couk.nucmedone.shinyplopper.clickscreen.ClickScreenListener;

public class ShinyPlopper extends Preloader implements ClickScreenListener {

	private Label clickFeedbackLabel = new Label("");
	private Stage stage;
	final Text info = new Text();
	private TrayIcon trayIcon;

	@Override
	public void start(Stage stage) {

		// Register the application for keyboard and mouse events outside of the
		// JRE
		registerNativehooks();
		
		// Create the initial GUI view
		this.stage = stage;		
		makeStage();
		
    	createTrayIcon(stage);
        
    	// Set GUI to minimise to tray when closed
    	Platform.setImplicitExit(false);
    	
        stage.show();

//
//		Button listen = new Button("listen");
//		listen.setOnAction(new EventHandler<ActionEvent>() {
//			public void handle(ActionEvent event) {
//				new ClickScreenToo(ShinyPlopper.this);
//			}
//		});
//		VBox layout = new VBox(10);
//		layout.getChildren().setAll(listen, clickFeedbackLabel, info);
//		layout.setPadding(new Insets(10));
//
//		stage.setScene(new Scene(layout, 100, 80));
//		stage.show();
	}

	/**
	 * Create an entry in the system tray
	 * @param stage
	 */
	private void createTrayIcon(final Stage stage) {

		if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            java.awt.Image image = null;
            try {
                URL url = ClassLoader.getSystemResource("couk/nucmedone/shinyplopper/images/NucMedOneIcon24.png");
                image = ImageIO.read(url);
            } catch (IOException ex) {
                System.out.println(ex);
            }

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				public void handle(WindowEvent arg0) {
					hide(stage);
				}
			});
            
            // create a action listener to listen for default action executed on the tray icon
            final ActionListener closeListener = new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            };

            ActionListener showListener = new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            stage.show();
                        }
                    });
                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Title", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(showListener);
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        }

	}

	protected void hide(Stage stage2) {
		// TODO Auto-generated method stub
		
	}

	private void makeStage() {
		
        // Close button
        Button closeBtn = new Button();
        closeBtn.setId("window-close");
        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                stage.hide();
            }
        });
        
        // Button layout
        HBox buttons = new HBox();
        closeBtn.setPrefHeight(12);
        closeBtn.setMinHeight(12);
        closeBtn.setMaxHeight(12);
        closeBtn.setPrefWidth(12);
        closeBtn.setMinWidth(12);
        closeBtn.setMaxWidth(12);
        buttons.getChildren().add(closeBtn);
        buttons.setPadding(new Insets(10));
        
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
        
        // Grid layout for calibrator stuff
        GridPane grid = new GridPane();
        grid.setId("reading-pane");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.BOTTOM_CENTER);
        // Add previous readings in zeroth column (all rows)
        grid.add(previousVBox, 0, 0, 1, 3);
        // Calibrator reading in 2nd row
        grid.add(readWithUnits, 2, 2);
   
        // Borderpane for basic app alignment
        BorderPane borderpane = new BorderPane();
        borderpane.setTop(buttons);
        borderpane.setCenter(grid);
        borderpane.setPadding(new Insets(10));
             
        Scene scene = new Scene(borderpane, 600, 450);
        scene.setFill(Color.LIGHTGRAY);        
        scene.getStylesheets().add("couk/nucmedone/shinyplopper/style.css");
        
    	stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);

		
	}

	/**
	 * Register native hooks for keyboard and mouse events outside the JRE 
	 */
	private void registerNativehooks() {
		
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
			}
			
			public void nativeMousePressed(NativeMouseEvent e) {
			}
			
			public void nativeMouseClicked(NativeMouseEvent e) {
				Point point = e.getPoint();
				System.out.println("Mouse click at: " + point);
			}
		});
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