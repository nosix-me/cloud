package com.nosix.cloud.monitor.support;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import com.nosix.cloud.common.Constants;
import com.nosix.cloud.monitor.VisualService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class SimpleVisualService implements VisualService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleVisualService.class);
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmm");
    private static final DecimalFormat numberFormat = new DecimalFormat("###,##0.##");

    @Override
    public void draw() {
        File rootDir = new File(STATISTICS_DIRECTORY);
        if(!rootDir.exists()) {
            return;
        }
        File[] dateDirs = rootDir.listFiles();
        for(File dateDir:dateDirs) {
            File[] groupDirs = dateDir.listFiles();
            if(groupDirs == null) {
                break;
            }
            for(File groupDir : groupDirs) {
                File[] serviceDirs = groupDir.listFiles();
                if(serviceDirs == null ) {
                    break;
                }
                for(File serviceDir : serviceDirs) {
                    File[] versionDirs = serviceDir.listFiles();
                    if(versionDirs == null) {
                        break;
                    }
                    for(File versionDir : versionDirs) {
                        File[] methodDirs = versionDir.listFiles();
                        if(methodDirs == null) {
                            break;
                        }
                        for(File methodDir : methodDirs) {
                            String methodUri = CHARS_DIRECTORY
                                    + Constants.SEPARATOR_PATH + dateDir.getName()
                                    + Constants.SEPARATOR_PATH + groupDir.getName()
                                    + Constants.SEPARATOR_PATH + serviceDir.getName()
                                    + Constants.SEPARATOR_PATH + versionDir.getName()
                                    + Constants.SEPARATOR_PATH + methodDir.getName()
                                    + Constants.SEPARATOR_PATH + PROVIDER;
                            buildChart(methodUri, dateDir, groupDir, serviceDir, versionDir, methodDir, "ms", SUCCESS);
                            buildChart(methodUri, dateDir, groupDir, serviceDir, versionDir, methodDir,"count", FAILURE);
                            buildChart(methodUri, dateDir, groupDir, serviceDir, versionDir, methodDir,"count", CONCURRENT);
                        }

                    }
                }
            }
        }
    }
    private static void buildChart(String methodUri, File dateDir, File groupDir, File serviceDir, File versionDir, File methodDir, String key, String type) {
        File file = new File(methodUri + Constants.SEPARATOR_PATH  + type + ".png");
        long modified = file.lastModified();
        boolean isChanged = false;
        Map<String, Long> data = new HashMap<String, Long>();
        double[] summary = new double[4];

        File newFile = new File(methodDir.getAbsolutePath() + Constants.SEPARATOR_PATH + PROVIDER + "." + type);
        appendData(newFile, data, summary);
        if (newFile.lastModified() > modified) {
            isChanged = true;
        }
        if (isChanged) {
            createChart(key, dateDir.getName(), groupDir.getName(), serviceDir.getName(), methodDir.getName(), versionDir.getName(), PROVIDER, data, summary, file.getAbsolutePath());
        }
    }

    private static void createChart(String key, String date, String group, String service, String method, String version, String type, Map<String, Long> data, double[] summary, String path) {
        TimeSeriesCollection xydataset = new TimeSeriesCollection();
        TimeSeries timeseries = new TimeSeries(type);
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            try {
                timeseries.add(new Minute(sdf3.parse(date + entry.getKey())), entry.getValue());
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
        }
        xydataset.addSeries(timeseries);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(
                "max: " + numberFormat.format(summary[0]) + (summary[1] >=0 ? " min: " + numberFormat.format(summary[1]) : "")
                        + " avg: " + numberFormat.format(summary[2]) + (summary[3] >=0 ? " num: " + numberFormat.format(summary[3]) : ""),
                group + "  " + toDisplayService(service) +"  "+ version + "  " + method + "  " + toDisplayDate(date), key, xydataset, true, true, false);
        jfreechart.setBackgroundPaint(Color.WHITE);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setBackgroundPaint(Color.WHITE);
        xyplot.setDomainGridlinePaint(Color.GRAY);
        xyplot.setRangeGridlinePaint(Color.GRAY);
        xyplot.setDomainGridlinesVisible(true);
        xyplot.setRangeGridlinesVisible(true);
        DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
        dateaxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
        BufferedImage image = jfreechart.createBufferedImage(1500, 350);
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("write chart: {}", path);
            }
            File methodChartFile = new File(path);
            File methodChartDir = methodChartFile.getParentFile();
            if (methodChartDir != null && ! methodChartDir.exists()) {
                methodChartDir.mkdirs();
            }
            FileOutputStream output = new FileOutputStream(methodChartFile);
            try {
                ImageIO.write(image, "png", output);
                output.flush();
            } finally {
                output.close();
            }
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private static void appendData(File file, Map<String, Long> data, double[] summary) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            try {
                int sum = 0;
                int cnt = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    int index = line.indexOf(" ");
                    if (index > 0) {
                        String key = line.substring(0, index).trim();
                        long value = Long.parseLong(line.substring(index + 1).trim());
                        if (!data.containsKey(key)) {
                            data.put(key, value);
                        } else {
                            long maxVal = Math.max(data.get(key), value);
                            data.put(key, maxVal);
                        }
                        summary[0] = Math.max(summary[0], value);
                        summary[1] = summary[1] == 0 ? value : Math.min(summary[1], value);
                        sum += value;
                        cnt ++;
                    }
                }
                summary[3] = cnt;
                summary[2] = cnt == 0 ? 0 : sum / cnt;
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private static String toDisplayService(String service) {
        int i = service.lastIndexOf('.');
        if (i >= 0) {
            return service.substring(i + 1);
        }
        return service;
    }

    private static String toDisplayDate(String date) {
        try {
            return sdf1.format(sdf2.parse(date));
        } catch (ParseException e) {
            return date;
        }
    }
}
