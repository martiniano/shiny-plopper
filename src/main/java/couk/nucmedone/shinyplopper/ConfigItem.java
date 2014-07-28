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

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

class ConfigItem extends HBox {

	private final String name;
	private ComboBox<String> combo;

	public ConfigItem(String name, String item){

		this.name = name;

		init();

		Label val = new Label(item);
		getChildren().add(val);

	}

	public ConfigItem(String name, String item, ObservableList<String> list){

		this.name = name;

		init();

		combo = new ComboBox<String>(list);
		if (item != null) {
			combo.setValue(item);
		}

		getChildren().add(combo);

	}

	public String getItem(){
		return combo.getValue();
	}

	public String getName(){
		return name;
	}

	private void init(){

		setPadding(new Insets(2));
		setSpacing(10);
		Label theLabel = new Label(PloppyProps.FRIENDLY_NAMES.get(name) + ":");
		getChildren().add(theLabel);

	}

}