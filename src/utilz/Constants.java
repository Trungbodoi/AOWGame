package utilz;

import main.Game;

public class Constants {

	public static class UnitConstants {

		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int IDLE = 0;


	}

	public static float GetSpeed(int id) {
		switch (id) {
			case 11 -> {
				return 5f;
			}
			default -> {
				return 5f;
			}
		}
	}

	public static int getCost(int index) {
		switch (index) {
			case 11 -> {
				return 750;
			}
			case 12 -> {
				return 1500;
			}
			case 13 -> {
				return 2300;
			}
			case 21 -> {
				return 1800;
			}
			case 22 -> {
				return 2000;
			}
			case 23 -> {
				return 2100;
			}
			case 31 -> {
				return 1500;
			}
			case 32 -> {
				return 2100;
			}
			case 33 -> {
				return 2200;
			}
			case 41 -> {
				return 1000;
			}
			case 42 -> {
				return 2000;
			}
			case 43 -> {
				return 3000;
			}
			case 51 -> {
				return 2000;
			}
			case 52 -> {
				return 3500;
			}
			case 53 -> {
				return 4000;
			}
			default -> {
				return 2000;
			}

		}
	}
	public static int getUnitCost(int unitIndex){
		switch (unitIndex){
			case 11 -> {
				return 100;
			}
			case 12 -> {
				return 125;
			}
			case 13 -> {
				return 200;
			}
			case 14 -> {
				return 400;
			}
			case 21 -> {
				return 125;
			}
			case 22 -> {
				return 100;
			}
			case 23 -> {
				return 200;
			}
			case 24 -> {
				return 400;
			}
			case 31 -> {
				return 100;
			}
			case 32 -> {
				return 125;
			}
			case 33 -> {
				return 200;
			}
			case 34 -> {
				return 400;
			}
			case 41 -> {
				return 100;
			}
			case 42 -> {
				return 125;
			}
			case 43 -> {
				return 200;
			}
			case 44 -> {
				return 400;
			}
			case 51 -> {
				return 100;
			}
			case 52 -> {
				return 125;
			}
			case 53 -> {
				return 200;
			}
			case 54 -> {
				return 400;
			}
			default -> {
				return 1200;
			}
		}
	}
	public static float loadSpawntime(int unitIndex){
		switch (unitIndex){
			case 11 -> {
				return 1f;
			}
			case 12 -> {
				return 1.5f;
			}
			case 13 -> {
				return 2.5f;
			}
			case 14 -> {
				return 4f;
			}
			case 21 -> {
				return 1.5f;
			}
			case 22 -> {
				return 1f;
			}
			case 23 -> {
				return 2.5f;
			}
			case 24 -> {
				return 4f;
			}
			case 31 -> {
				return 1;
			}
			case 32 -> {
				return 1.5f;
			}
			case 33 -> {
				return 2.5f;
			}
			case 34 -> {
				return 4f;
			}
			case 41 -> {
				return 1f;
			}
			case 42 -> {
				return 1.5f;
			}
			case 43 -> {
				return 2.5f;
			}
			case 44 -> {
				return 4f;
			}
			case 51 -> {
				return 1f;
			}
			case 52 -> {
				return 1.5f;
			}
			case 53 -> {
				return 2.5f;
			}
			case 54 -> {
				return 4f;
			}

			default -> {
				return 2f;
			}

		}
	}

	public static int getXpNeed(int age) {
		switch (age) {
			case 1:
				return 7000;
			case 2:
				return 8000;
			case 3:
				return 9000;
			case 4:
				return 10000;
			case 5:
				return 11000;
			default:
				return 7500;
		}
	}

	public static int getGoldNeeded(int hutAt) {
		switch (hutAt) {
			case 1:
				return 250;
			case 2:
				return 1000;
			case 3:
				return 3000;
			default:
				return 5000;

		}
	}

	public static final int ANI_SPEED = 25;


	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}

		public static class PauseButtons {
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
		}

		public static class URMButtons {
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);

		}

		public static class VolumeButtons {
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;

			public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}
	}

}
