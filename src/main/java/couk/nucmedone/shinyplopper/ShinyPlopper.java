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
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
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

import couk.nucmedone.shinyplopper.chambers.AbstractChamber;
import couk.nucmedone.shinyplopper.chambers.Chamber;
import couk.nucmedone.shinyplopper.chambers.Constants;
import couk.nucmedone.shinyplopper.chambers.ChamberListener;
import couk.nucmedone.shinyplopper.chambers.Reading;
import couk.nucmedone.shinyplopper.clickscreen.ClickScreen;
import couk.nucmedone.shinyplopper.clickscreen.ClickScreenListener;
import couk.nucmedone.shinyplopper.config.PloppyConfig;
import couk.nucmedone.shinyplopper.config.PloppyProps;
import couk.nucmedone.shinyplopper.hooks.KeyPlopper;
import couk.nucmedone.shinyplopper.hooks.MousePlopper;

public class ShinyPlopper extends Application implements ActionListener,
		ChamberListener, ClickScreenListener {

	public static void main(String[] args) {
		launch(args);
	}

	private PloppyConfig config;

	private PloppyProps props;

	private Stage stage;

	private TrayIcon trayIcon;
	private Robot robot;
	private ClickScreen cst = null;
	private CharSequence activity = "";

	private final KeyPlopper keyPlopper;

	private final MousePlopper mousePlopper;

	private final Reading reading;

	private AbstractChamber chamber = null;

	// private boolean clickerOn = false;

	public ShinyPlopper() {

		// Global mouse and key events
		keyPlopper = new KeyPlopper(this);
		mousePlopper = new MousePlopper(this);

		// Reading class
		reading = new Reading();

		// Prep the config
		Platform.runLater(new Runnable() {
			public void run() {

				config = new PloppyConfig();
				props = new PloppyProps();

				// Robot class that will drop readings into other windows owned
				// by the OS
				try {
					robot = new Robot();
				} catch (AWTException e) {
					e.printStackTrace();
				}

				try {
					String type = props.getChamberType();
					String name = Constants.chambers.get(type);
					chamber = this.getClass().getClassLoader().loadClass(name)
							.asSubclass(AbstractChamber.class).newInstance();
					// Go, go , go!
					chamber.start();
					chamber.read();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		});

	}

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
		setSizes(closeBtn);

		// Configure button
		Button confBtn = new Button();
		confBtn.setId("config-button");
		confBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				config.show();
			}
		});
		setSizes(confBtn);

		// Button layout
		HBox buttons = new HBox();
		buttons.getChildren().addAll(confBtn, closeBtn);
		buttons.setPadding(new Insets(10, 0, 10, 0));
		buttons.setSpacing(5);
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

		// Add the key listener straight away - always listen for action keys
		GlobalScreen.getInstance().addNativeKeyListener(keyPlopper);

	}

	public void screenClickCancel() {
		Platform.runLater(new Runnable() {
			public void run() {
				GlobalScreen.getInstance().removeNativeMouseListener(
						mousePlopper);
				cst.hide();
				// stage.show();
			}
		});
	}

	/**
	 * The screen has been clicked... need to drop some text into someone's
	 * window
	 */
	public void screenClicked(Point point) {

		// if (clickerOn) {

		screenClickCancel();

		Platform.runLater(new Runnable() {

			public void run() {
				cst.hide();
				type(activity.toString());
			}
		});

		// }

	}

	private void setSizes(Button button) {

		button.setPrefHeight(16);
		button.setMinHeight(16);
		button.setMaxHeight(16);
		button.setPrefWidth(16);
		button.setMinWidth(16);
		button.setMaxWidth(16);

	}

	public void showClickcreen() {

		Platform.runLater(new Runnable() {
			public void run() {
				// Listen for screen clicks
				GlobalScreen.getInstance().addNativeMouseListener(mousePlopper);
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

		show();

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

	// public void clickScreenOn(boolean onOff) {
	// clickerOn = onOff;
	// }

	public void onActivityUpdate(CharSequence activity) {
		this.activity = activity;
		double currentActivity = Double.NaN;
		try {
			currentActivity = Double.parseDouble(activity.toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		// Do it again!
		chamber.read();
	}

	public void queryChamber() {
		show();
		// clickScreenOn(true);
		// showClickcreen();
	}

	public void show() {
		Platform.runLater(new Runnable() {
			public void run() {
				stage.show();
				stage.toFront();
				stage.requestFocus();
			}
		});
	}
}
