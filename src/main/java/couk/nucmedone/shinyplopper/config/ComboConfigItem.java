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
package couk.nucmedone.shinyplopper.config;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboConfigItem extends ConfigItem {

	private ComboBox<String> combo;

	public ComboConfigItem(String name, String item){
		super(name, item);
	}

	public String getItem(){

		String val = combo.getValue();
		return val;

	}

	public void setOptions(ObservableList<String> list){

		combo = new ComboBox<String>(list);
		if (item != null) {
			combo.setValue(item);
		}

		getChildren().add(combo);

	}

}
