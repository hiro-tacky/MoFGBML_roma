package cilabo.main;

public class ExperienceMethods {

	public static ClassLabel classlabel = ClassLabel.Single; //クラスラベル
	public static RuleWeight ruleWeight = RuleWeight.mean; //ルール重み計算方式

	public ExperienceMethods(
		ClassLabel classlabel,
		RuleWeight ruleWeight
	) {
		ExperienceMethods.classlabel = classlabel;
		ExperienceMethods.ruleWeight = ruleWeight;
	}

	/**データセットのクラスラベル方式 */
	public static enum ClassLabel{
		Single, //単一のクラスラベル
		Multi //複数のクラスラベル
	}

	public static enum RuleWeight{
		mean, median, top
	}

	public static enum AttributeIdMichigan{
		numberOfWinnerRule,
	}

	public static enum AttributeIdPittsburgh{
		numberOfWinnerRule,
	}

	public static enum ObjectivesIndexMichigan{
		FitnessValue(0),
		RuleLength(1);

		private final int objectivesIndexMichigan;
		private ObjectivesIndexMichigan(int objectivesIndexMichigan) {
			this.objectivesIndexMichigan = objectivesIndexMichigan;
		}
		public int toInt() {return  objectivesIndexMichigan;}
	}


}
