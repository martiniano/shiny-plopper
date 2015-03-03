package couk.nucmedone.shinyplopper;

import couk.nucmedone.shinyplopper.chambers.AbstractChamber;

public class TestChamber extends AbstractChamber {

	public TestChamber(){
		super();
		units = new StringBuffer("MBq");
		nuclide = new StringBuffer("Tc-99m");
	}
	
	@Override
	protected void populateUnitMap() {

	}

	@Override
	public void read() {

		// use a random number generator to get a reading of approximately 1000
		// MBq in the range 990-1010 MBq
		double rnd = Math.random() * 20.0;
		rnd = 1010 - rnd;
		activity = new StringBuffer(Double.toString(rnd));
		update();

	}

	@Override
	public void setNuclide(CharSequence nuclide) {
		this.nuclide = new StringBuffer(nuclide);
	}

}
