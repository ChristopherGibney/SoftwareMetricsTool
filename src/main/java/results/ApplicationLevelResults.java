package results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class ApplicationLevelResults {
	
	public void normalizeAllResults() {
		for (List<Double> lcom5List : LCOM5Results) {
			for (int i = 0; i < lcom5List.size(); i++) {
				lcom5List.set(i, (1-(lcom5List.get(i) - lcom5Min)/(lcom5Max - lcom5Min)));
			}
		}
		normalizeList(classCohesionResults, classCohesionMax, classCohesionMin);
		normalizeList(sensitiveClassCohesionResults, sensitiveClassCohesionMax, sensitiveClassCohesionMin);
		normalizeList(CBOResults, CBOMax, CBOMin);
		normalizeList(DACResults, DACMax, DACMin);
		normalizeList(afferentResults, afferentMax, afferentMin);
		normalizeList(efferentResults, efferentMax, efferentMin);
	}
	public void averageListsAtIndex(int index) {
		int listSize = getSizeOfListsInResults() - 1;
		//the integer value in the entries below represents which version of the repository the averaged
		//result has come from. The results were inputted from 0->listSize in all lists below (all lists are of 
		//the same size because even if an empty list of results is passed in it is added in order that all lists
		//can be indexed with one value as in this method, and all of the results returned have come from the same
		//repository version. In the lists, the values at index 0 are from the most recent repo, therefore listSize-index
		//flips this value as when results are being entered into graphs 0 should be oldest repo while listSize should be most recent
		if (!LCOM5Results.get(index).isEmpty()) {
			Entry<Integer, Double> newEntry = new SimpleEntry<>(listSize-index, averageList(LCOM5Results.get(index)));
			finalLCOM5Results.add(newEntry);
		}
		if (!classCohesionResults.get(index).isEmpty()) {
			Entry<Integer, Double> newEntry = new SimpleEntry<>(listSize-index, averageList(classCohesionResults.get(index)));
			finalClassCohesionResults.add(newEntry);
		}
		if (!sensitiveClassCohesionResults.get(index).isEmpty()) {
			Entry<Integer, Double> newEntry = new SimpleEntry<>(listSize-index, averageList(sensitiveClassCohesionResults.get(index)));
			finalSensitiveClassCohesionResults.add(newEntry);
		}
		if (!CBOResults.get(index).isEmpty()) {
			Entry<Integer, Double> newEntry = new SimpleEntry<>(listSize-index, averageList(CBOResults.get(index)));
			finalCBOResults.add(newEntry);
		}
		if (!DACResults.get(index).isEmpty()) {
			Entry<Integer, Double> newEntry = new SimpleEntry<>(listSize-index, averageList(DACResults.get(index)));
			finalDACResults.add(newEntry);
		}
		if (!afferentResults.get(index).isEmpty()) {
			Entry<Integer, Double> newEntry = new SimpleEntry<>(listSize-index, averageList(afferentResults.get(index)));
			finalAfferentResults.add(newEntry);
		}
		if (!efferentResults.get(index).isEmpty()) {
			Entry<Integer, Double> newEntry = new SimpleEntry<>(listSize-index, averageList(efferentResults.get(index)));
			finalEfferentResults.add(newEntry);
		}
	}
	private Double averageList(List<Double> metricResults) {
		Double resultSum = 0.0;
		for (Double metricValue : metricResults) {
			resultSum += metricValue;
		}
		return resultSum/metricResults.size();
	}
	private void normalizeList(ArrayList<List<Double>> metricResults, Double metricMax, Double metricMin) {
		for (List<Double> resultList : metricResults) {
			for (int i = 0; i < resultList.size(); i++) {
				resultList.set(i, ((resultList.get(i) - metricMin)/(metricMax-metricMin)));
			}
		}
	}
	public void addLCOM5Results(List<Double> results) {
		LCOM5Results.add(results);
		if (!results.isEmpty()) {
			double resultsMax = Collections.max(results), resultsMin = Collections.min(results);
			if (lcom5Max < resultsMax) {
				lcom5Max = resultsMax;
			}
			if (lcom5Min > resultsMin) {
				lcom5Min = resultsMin;
			}
		}
	}
	public void addClassCohesionResults(List<Double> results) {
		classCohesionResults.add(results);
		if (!results.isEmpty()) {
			double resultsMax = Collections.max(results), resultsMin = Collections.min(results);
			if (classCohesionMax < resultsMax) {
				classCohesionMax = resultsMax;
			}
			if (classCohesionMin > resultsMin) {
				classCohesionMin = resultsMin;
			}
		}
	}
	public void addSensitiveClassCohesionResults(List<Double> results) {
		sensitiveClassCohesionResults.add(results);
		if (!results.isEmpty()) {
			double resultsMax = Collections.max(results), resultsMin = Collections.min(results);
			if (sensitiveClassCohesionMax < resultsMax) {
				sensitiveClassCohesionMax = resultsMax;
			}
			if (sensitiveClassCohesionMin > resultsMin) {
				sensitiveClassCohesionMin = resultsMin;
			}
		}
	}
	public void addCBOResults(List<Double> results) {
		CBOResults.add(results);
		if (!results.isEmpty()) {
			double resultsMax = Collections.max(results), resultsMin = Collections.min(results);
			if (CBOMax < resultsMax) {
				CBOMax = resultsMax;
			}
			if (CBOMin > resultsMin) {
				CBOMin = resultsMin;
			}
		}
	}
	public void addDACResults(List<Double> results) {
		DACResults.add(results);
		if (!results.isEmpty()) {
			double resultsMax = Collections.max(results), resultsMin = Collections.min(results);
			if (DACMax < resultsMax) {
				DACMax = resultsMax;
			}
			if (DACMin > resultsMin) {
				DACMin = resultsMin;
			}
		}
	}
	public void addAfferentResults(List<Double> results) {
		afferentResults.add(results);
		if (!results.isEmpty()) {
			double resultsMax = Collections.max(results), resultsMin = Collections.min(results);
			if (afferentMax < resultsMax) {
				afferentMax = resultsMax;
			}
			if (afferentMin > resultsMin) {
				afferentMin = resultsMin;
			}
		}
	}
	public void addEfferentResults(List<Double> results) {
		efferentResults.add(results);
		if (!results.isEmpty()) {
			double resultsMax = Collections.max(results), resultsMin = Collections.min(results);
			if (efferentMax < resultsMax) {
				efferentMax = resultsMax;
			}
			if (efferentMin > resultsMin) {
				efferentMin = resultsMin;
			}
		}
	}
	public int getSizeOfListsInResults() {
		//guaranteed to have same number of lists in each array list of results as array lists 
		//of results for each metric are added at the same time therefore can use size of any of the results array lists
		return LCOM5Results.size();
	}
	public Map<String, ArrayList<Entry<Integer, Double>>> getFinalResults() {
		Map<String, ArrayList<Entry<Integer, Double>>> finalResults = new HashMap<>();
		finalResults.put("LackOfCohesionOfMethodsFive", finalLCOM5Results);
		finalResults.put("ClassCohesion", finalClassCohesionResults);
		finalResults.put("SensitiveClassCohesion", finalSensitiveClassCohesionResults);
		finalResults.put("CouplingBetweenObjectClasses", finalCBOResults);
		finalResults.put("DataAbstractionCoupling", finalDACResults);
		finalResults.put("AfferentCoupling", finalAfferentResults);
		finalResults.put("EfferentCoupling", finalEfferentResults);
		return finalResults;
	}
	
	private ArrayList<List<Double>> LCOM5Results = new ArrayList<>();
	private ArrayList<List<Double>> classCohesionResults = new ArrayList<>();
	private ArrayList<List<Double>> sensitiveClassCohesionResults = new ArrayList<>();
	private ArrayList<List<Double>> CBOResults = new ArrayList<>();
	private ArrayList<List<Double>> DACResults = new ArrayList<>();
	private ArrayList<List<Double>> afferentResults = new ArrayList<>();
	private ArrayList<List<Double>> efferentResults = new ArrayList<>();
	private ArrayList<Entry<Integer, Double>> finalLCOM5Results = new ArrayList<>();
	private ArrayList<Entry<Integer, Double>> finalClassCohesionResults = new ArrayList<>();
	private ArrayList<Entry<Integer, Double>> finalSensitiveClassCohesionResults = new ArrayList<>();
	private ArrayList<Entry<Integer, Double>> finalCBOResults = new ArrayList<>();
	private ArrayList<Entry<Integer, Double>> finalDACResults = new ArrayList<>();
	private ArrayList<Entry<Integer, Double>> finalAfferentResults = new ArrayList<>();
	private ArrayList<Entry<Integer, Double>> finalEfferentResults = new ArrayList<>();
	//needed for normalizing data
	private double lcom5Max = 0, classCohesionMax = 0, sensitiveClassCohesionMax = 0, CBOMax = 0, DACMax = 0, afferentMax = 0, efferentMax = 0;
	private double lcom5Min = 0, classCohesionMin = 0, sensitiveClassCohesionMin = 0, CBOMin = 0, DACMin = 0, afferentMin = 0, efferentMin = 0;
}
