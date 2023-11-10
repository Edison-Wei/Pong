package pong;

public enum Difficulty {
	// Difficulty will be different starting speed and how precise the bot paddle is
	EASY, MEDIUM, HARD;
	
	/***
	 * returns an double array containing < 1
	 * [How much velocity will increase, Decrease sleepWait time in milliseconds]
	 * @param num an int representing Easy: 0, Medium: 1, Hard: 2
	 * @return an array of doubles < 1
	 */
	double[] setDifficulty(int num) {
		double[] values = new double[2];
		switch(num) {
			case 1:
				values[0] = .2;
				values[1] = 1.;
				break;
			case 2:
				values[0] = .5;
				values[1] = 2.;
				break;
			default:
				values[0] = .2;
				values[1] = 1.;
				break;
		}
		return values;
	}
}
