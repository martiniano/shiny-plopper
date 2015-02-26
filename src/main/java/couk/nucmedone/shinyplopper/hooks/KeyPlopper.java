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
package couk.nucmedone.shinyplopper.hooks;

import javafx.application.Platform;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import couk.nucmedone.shinyplopper.clickscreen.ClickScreenListener;

/**
 * Simple class to listen for global key presses - typically function keys
 *
 * @author neil
 *
 */
public class KeyPlopper implements NativeKeyListener {

	private final ClickScreenListener listener;

	public KeyPlopper(ClickScreenListener listener) {
		this.listener = listener;
	}

	/**
	 * Process key strokes... pass event actions to the listener. ESC will
	 * cancel the click screen. Function keys may pass another action to the
	 * listener (chamber read or Tc-99m read, etc.).
	 */
	public void nativeKeyPressed(NativeKeyEvent e) {

		final int keyCode = e.getKeyCode();
		final String text = NativeKeyEvent.getKeyText(keyCode);

		Platform.runLater(new Runnable() {
			public void run() {
				if (keyCode == NativeKeyEvent.VC_ESCAPE) {
					listener.screenClickCancel();
				}

				if ("F5".equals(text)) {
					listener.queryChamber();
				}
			}
		});

	}

	public void nativeKeyReleased(NativeKeyEvent e) {
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
	}

}
