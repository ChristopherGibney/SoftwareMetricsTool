package results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassResultsMap {
	private Map<String, Map<String, List<Double>>> allClassResults = new HashMap<>();
	
	public void addResult(String fileClassName, String metric, Double result) {
		if (allClassResults.containsKey(fileClassName) && !result.isNaN() && !result.isInfinite()) {
			if (allClassResults.get(fileClassName).containsKey(metric)) {
				allClassResults.get(fileClassName).get(metric).add(result);
			}
			else {
				List<Double> results = new ArrayList<>();
				results.add(result);
				allClassResults.get(fileClassName).put(metric, results);
			}
		}
		else if (!result.isNaN() && !result.isInfinite()) {
			List<Double> results = new ArrayList<>();
			results.add(result);
			Map<String, List<Double>> metricResults = new HashMap<>();
			metricResults.put(metric, results);
			allClassResults.put(fileClassName, metricResults);
		}	
	}
	public Map<String, Map<String, List<Double>>> getResults() {
		return allClassResults;
	}
}
