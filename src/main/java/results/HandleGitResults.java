package results;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import parser.RepoAllVersionsOnBranch;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class HandleGitResults {
	public static void gitRepoResults(RepoAllVersionsOnBranch branch, ApplicationLevelResults applicationResults, String outputPath) {
		String resultsDirPath = outputPath + "//" + branch.getBranchSimpleName();
		File dirForResults = new File(resultsDirPath);
		dirForResults.mkdirs();
		handleClassResults(branch.getResults(), resultsDirPath);
		handleApplicationResults(applicationResults, resultsDirPath, branch.getBranchSimpleName());
	}
	private static void createPNGResultsGraph(String path, String title, String xAxis, String yAxis, XYSeriesCollection graphData) {
		JFreeChart classChart = ChartFactory.createXYLineChart(title, xAxis, yAxis, graphData);
		formatChart(classChart);
		File pngFile = new File(path);
		try{
			ChartUtilities.saveChartAsPNG(pngFile, classChart, 640, 480);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private static void formatChart(JFreeChart classChart) {
		XYPlot classPlot = classChart.getXYPlot();
		classPlot.setDomainGridlinesVisible(true);
		classPlot.setDomainGridlinePaint(Color.BLACK);
		classPlot.setRangeGridlinesVisible(true);
		classPlot.setRangeGridlinePaint(Color.BLACK);
		classPlot.setBackgroundPaint(Color.LIGHT_GRAY);
		
		XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
		BasicStroke brushSize = new BasicStroke(3.0f);
		for (int i = 0; i < classPlot.getSeriesCount(); i++) {
			render.setSeriesStroke(i, brushSize);
			render.setSeriesPaint(i, colors[i]);
		}
		classPlot.setRenderer(render);
	}
	
	private static void handleApplicationResults(ApplicationLevelResults applicationResults, String resultsDirPath, String branchName) {
		String classResultPath = resultsDirPath + "//ApplicationLevelMetric.png";
		String yAxis = "Normalized Metric Result";
		String xAxis = "Versions of Repo from 0 (oldest) to most recent";
		XYSeriesCollection graphData = new XYSeriesCollection();
		Map<String, ArrayList<Entry<Integer, Double>>> finalResults = applicationResults.getFinalResults();
		
		for (String metric : finalResults.keySet()) {
			XYSeries metricSeries = new XYSeries(metric);
			for (Entry<Integer, Double> resultPair : finalResults.get(metric)) {
				metricSeries.add(resultPair.getKey(), resultPair.getValue());
			}
			graphData.addSeries(metricSeries);
		}
		createPNGResultsGraph(classResultPath, "ApplicationLevelMetric", xAxis, yAxis, graphData);
	}
	
	private static void handleClassResults(ClassResultsMap classResults, String resultsDirPath) {
		for (String classKey : classResults.getResults().keySet()) {
			String classResultPath = resultsDirPath + "//" + classKey.replaceAll("/", "_")+".png";
			String yAxis = "Normalized Metric Result";
			String xAxis = "Versions of Repo from 0 (oldest) to most recent";
			XYSeriesCollection graphData = new XYSeriesCollection();
			
			for (String metricKey : classResults.getResults().get(classKey).keySet()) {
				XYSeries metricSeries = new XYSeries(metricKey);
				Double metricMax = 0.0, metricMin = 0.0;
				List<Double> allMetricResults = classResults.getMetricResultList(metricKey);
				metricMax = Collections.max(allMetricResults);
				metricMin = Collections.min(allMetricResults);
				for (Entry<Integer, Double> resultPair : classResults.getResults().get(classKey).get(metricKey)) {
					Double normalizedResult = (resultPair.getValue() - metricMin)/(metricMax-metricMin);
					metricSeries.add(resultPair.getKey(), normalizedResult);
				}
				graphData.addSeries(metricSeries);
			}
			createPNGResultsGraph(classResultPath, classKey, xAxis, yAxis, graphData);
		}
	}
	private static Color[] colors = {Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.RED, Color.YELLOW, Color.BLUE, Color.PINK};
}
