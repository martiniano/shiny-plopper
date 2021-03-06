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

import java.awt.Point;

import javafx.application.Platform;

import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import couk.nucmedone.shinyplopper.clickscreen.ClickScreenListener;

public class MousePlopper implements NativeMouseListener {

	private final ClickScreenListener listener;

	public MousePlopper(ClickScreenListener listener){
		this.listener = listener;
	}

	public void nativeMouseClicked(NativeMouseEvent e) {

		final Point point = e.getPoint();
		Platform.runLater(new Runnable() {
			public void run() {
				listener.screenClicked(point);
			}
		});
//		listener.clickScreenOn(false);

	}

	public void nativeMousePressed(NativeMouseEvent e) {
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
	}

}
