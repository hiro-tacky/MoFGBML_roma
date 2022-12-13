package cilabo.main;

public class ExperienceParameter {

	public static ClassLabel classlabel = ClassLabel.Single; //クラスラベル

	/**データセットのクラスラベル方式 */
	public static enum ClassLabel{
		Single, //単一のクラスラベル
		Multi //複数のクラスラベル
	}

	public static enum ObjectivesForMichigan{
		FitnessValue(0),
		RuleLength(1);

		private final int objectivesIndexForMichigan;
		private ObjectivesForMichigan(int objectivesIndexForMichigan) {
			this.objectivesIndexForMichigan = objectivesIndexForMichigan;
		}
		public int toInt() {return  objectivesIndexForMichigan;}
	}

	public static enum ObjectivesForPittsburgh{
		ErrorRateDtra(0),
		NumberOfRule(1),
		ErrorRateDtst(2),;

		private final int objectivesForPittsburgh;
		private ObjectivesForPittsburgh(int objectivesForPittsburgh) {
			this.objectivesForPittsburgh = objectivesForPittsburgh;
		}
		public int toInt() {return  objectivesForPittsburgh;}
	}
}
