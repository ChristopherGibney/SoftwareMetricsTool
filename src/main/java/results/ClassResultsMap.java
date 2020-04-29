package results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class ClassResultsMap {
	private Map<String, Map<String, List<Entry<Integer, Double>>>> allClassResults = new HashMap<>();
	private Map<String, List<Double>> metricResults = new HashMap<>();
	
	public void addResult(String fileClassName, String metric, Double result, int repoVersion) {
		if (allClassResults.containsKey(fileClassName) && !result.isNaN() && !result.isInfinite()) {
			if (allClassResults.get(fileClassName).containsKey(metric)) {
				Entry<Integer, Double> repoVersion_result_pair = new SimpleEntry<>(repoVersion, result);
				allClassResults.get(fileClassName).get(metric).add(repoVersion_result_pair);
			}
			else {
				List<Entry<Integer, Double>> results = new ArrayList<>();
				Entry<Integer, Double> repoVersion_result_pair = new SimpleEntry<>(repoVersion, result);
				results.add(repoVersion_result_pair);
				allClassResults.get(fileClassName).put(metric, results);
			}
		}
		else if (!result.isNaN() && !result.isInfinite()) {
			List<Entry<Integer, Double>> results = new ArrayList<>();
			Entry<Integer, Double> repoVersion_result_pair = new SimpleEntry<>(repoVersion, result);
			results.add(repoVersion_result_pair);
			Map<String, List<Entry<Integer, Double>>> metricResults = new HashMap<>();
			metricResults.put(metric, results);
			allClassResults.put(fileClassName, metricResults);
		}	
		
		if (!result.isNaN() && !result.isInfinite()) {
			this.addToMetricList(metric, result);
		}
	}
	
	private void addToMetricList(String metric, Double result) {
		if (metricResults.containsKey(metric)) {
			metricResults.get(metric).add(result);
		}
		else {
			List<Double> resultList = new ArrayList<>();
			resultList.add(result);
			metricResults.put(metric, resultList);
		}
	}
	public Map<String, Map<String, List<Entry<Integer, Double>>>> getResults() {
		return allClassResults;
	}
	public List<Double> getMetricResultList(String metric) {
		List<Double> defaultList = new ArrayList<>();
		if (metricResults.containsKey(metric)) {
			return metricResults.get(metric);
		}
		return defaultList;
	}
}
