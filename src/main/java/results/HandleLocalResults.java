package results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HandleLocalResults {
	public static void localDirResults(ClassResultsMap classResults, ApplicationLevelResults applicationResults, String outputResultsPath) {
		String resultsString = "";
		Map<String, ArrayList<Entry<Integer, Double>>> finalResults = applicationResults.getFinalResults();
		for (String metric : finalResults.keySet()) {
			for (Entry<Integer, Double> resultPair : finalResults.get(metric)) {
				
				resultsString = resultsString.concat("Application Level Metric: " + metric + " " + resultPair.getValue() + "\r\n");
			}
		}
		resultsString = resultsString.concat("\r\n");
		String notNormalizedResults = resultsString;
		for (String classKey : classResults.getResults().keySet()) {
			Map<String, List<Entry<Integer, Double>>> metricResults = classResults.getResults().get(classKey);
			for (String metricKey : metricResults.keySet()) {
				List<Double> allMetricResults = classResults.getMetricResultList(metricKey);
				Double metricMax = Collections.max(allMetricResults);
				Double metricMin = Collections.min(allMetricResults);
				
				for (Entry<Integer, Double> resultPair : metricResults.get(metricKey)) {
					if (!metricMax.equals(0.0)) {
						Double outputResult = (resultPair.getValue() - metricMin)/(metricMax-metricMin);
						notNormalizedResults = notNormalizedResults.concat(classKey + " " + metricKey + " " + resultPair.getValue() + "\r\n");
						resultsString = resultsString.concat(classKey + " " + metricKey + " " + outputResult + "\r\n");
					}
				}
			}
			notNormalizedResults = notNormalizedResults.concat("\r\n");
			resultsString = resultsString.concat("\r\n");
		}
		File resultsFile = new File(outputResultsPath);
		resultsFile.mkdirs();
		File resultsTxtFile = new File(outputResultsPath+"//"+"SoftwareMetricsToolResultsNormalized.txt");
		File notNormalizedResultsFile = new File(outputResultsPath+"//"+"SoftwareMetricsToolResultsNotNormalized.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(resultsTxtFile));
			writer.write(resultsString);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(notNormalizedResultsFile));
			writer.write(notNormalizedResults);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
