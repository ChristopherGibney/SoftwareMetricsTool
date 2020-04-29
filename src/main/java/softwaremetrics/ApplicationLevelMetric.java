package softwaremetrics;

import results.ApplicationLevelResults;

public class ApplicationLevelMetric {
	public static void run(ApplicationLevelResults applicationLevelResults) {
		applicationLevelResults.normalizeAllResults();
		//after this for loop has executed results will be held in applicationLevelResults
		for (int i = 0; i < applicationLevelResults.getSizeOfListsInResults(); i++) {
			applicationLevelResults.averageListsAtIndex(i);
		}
	}
}
