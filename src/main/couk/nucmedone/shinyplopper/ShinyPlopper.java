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
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import couk.nucmedone.shinyplopper.clickscreen.ClickScreenListener;
import couk.nucmedone.shinyplopper.clickscreen.ClickScreen;
import couk.nucmedone.shinyplopper.clickscreen.NativePlopper;

public class ShinyPlopper extends Preloader implements ClickScreenListener,
		ActionListener {

	public ShinyPlopper() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private Stage stage;
	private TrayIcon trayIcon;
	private Robot robot;

	private ClickScreen cst = null;

	public void actionPerformed(java.awt.event.ActionEvent e) {

		if (Commands.TC_READ_AND_DROP.equals(e.getActionCommand())) {
			showClickcreen();
		}

	}

	/**
	 * Create an entry in the system tray
	 * 
	 * @param stage
	 */
	private void createTrayIcon(final Stage stage, ActionListener listener) {

		if (SystemTray.isSupported()) {
			// get the SystemTray instance
			SystemTray tray = SystemTray.getSystemTray();
			// load an image
			java.awt.Image image = null;
			try {
				URL url = ClassLoader
						.getSystemResource("couk/nucmedone/shinyplopper/images/NucMedOneIcon24.png");
				image = ImageIO.read(url);
			} catch (IOException ex) {
				System.out.println(ex);
			}

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				public void handle(WindowEvent arg0) {
					hide(stage);
				}
			});

			// create a action listener to listen for default action executed on
			// the tray icon
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

			MenuItem tcReadAndDrop = new MenuItem(
					"Tc-99m Read and text drop (F5)");
			tcReadAndDrop.setActionCommand(Commands.TC_READ_AND_DROP);
			tcReadAndDrop.addActionListener(listener);
			popup.add(tcReadAndDrop);

			MenuItem closeItem = new MenuItem("Close");
			closeItem.addActionListener(closeListener);
			popup.add(closeItem);
			// / ... add other items
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

	protected void hide(Stage stage) {
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
		buttons.setAlignment(Pos.TOP_RIGHT);

		// Calibrator screen area
		CalibratorScreen cal = new CalibratorScreen();

		// Borderpane for basic app alignment
		BorderPane borderpane = new BorderPane();
		borderpane.setTop(buttons);
		borderpane.setCenter(cal.getPane());
		borderpane.setPadding(new Insets(10));

		Scene scene = new Scene(borderpane, 400, 250);
		scene.setFill(Color.GRAY);
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
			System.err
					.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}

		NativePlopper nativePlopper = new NativePlopper(this);

		// Construct the native listeners and initialise
		GlobalScreen.getInstance().addNativeKeyListener(nativePlopper);
		GlobalScreen.getInstance().addNativeMouseListener(nativePlopper);

	}

	public void screenClickCancel() {
		Platform.runLater(new Runnable() {
			public void run() {
				cst.hide();
				stage.show();
			}
		});
	}

	public void screenClicked(Point point) {

		screenClickCancel();

		Platform.runLater(new Runnable() {
			public void run() {
				cst.hide();
				type("text");
			}
		});

	}

	public void showClickcreen() {

		Platform.runLater(new Runnable() {
			public void run() {
				cst.show();
				stage.hide();
			}
		});

	}

	@Override
	public void start(Stage stage) {

		// Register the application for keyboard and mouse events outside of the
		// JRE
		registerNativehooks();

		// Create the initial GUI view
		this.stage = stage;
		makeStage();

		// Make click screen in preparation
		cst = new ClickScreen(ShinyPlopper.this);

		createTrayIcon(stage, ShinyPlopper.this);

		// Set GUI to minimise to tray when closed
		Platform.setImplicitExit(false);

		stage.show();

	}

	private void type(int i) {
		robot.delay(40);
		robot.keyPress(i);
		robot.keyRelease(i);
	}

	private void type(String s) {
		byte[] bytes = s.getBytes();
		for (byte b : bytes) {
			int code = b;
			// keycode only handles [A-Z] (which is ASCII decimal [65-90])
			if (code > 96 && code < 123)
				code = code - 32;
			robot.delay(40);
			robot.keyPress(code);
			robot.keyRelease(code);
		}
	}

}