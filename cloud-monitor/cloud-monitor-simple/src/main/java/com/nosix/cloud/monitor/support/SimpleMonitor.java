package com.nosix.cloud.monitor.support;

import java.io.File;

import com.nosix.cloud.common.Constants;
import com.nosix.cloud.common.URL;
import com.nosix.cloud.common.URLParam;
import com.nosix.cloud.monitor.Statistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
public class SimpleMonitor extends AbstractMonitor {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMonitor.class);
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HHmm");
    private final BlockingQueue<Statistic> queue = new LinkedBlockingQueue<Statistic>(100000);
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        write();
                    } catch (Throwable t) { //防御性容错
                        logger.error("Unexpected error occur at write stat log, cause: " + t.getMessage(), t);
                        try {
                            Thread.sleep(5000); //失败延迟
                        } catch (Throwable t2) {
                        }
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("SimpleMonitorAsyncWriteLogThread");
        thread.start();

        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    new SimpleVisualService().draw();
                } catch (Throwable t) { // 防御性容错
                    logger.error("Unexpected error occur at draw stat chart, cause: " + t.getMessage(), t);
                }
            }
        }, 1, 30, TimeUnit.SECONDS);
    }


    @Override
    public void collect(URL url, String methodName, int concurrent, long takeTime, boolean isError) {
        String serviceName = url.getPath();
        String serviceVersion = url.getParameter(URLParam.version.getName());
        String serveceGroup = url.getParameter(URLParam.group.getName());
        Statistic statistic = new Statistic(serviceName,
                serviceVersion, serveceGroup, methodName, concurrent, takeTime, isError);
        queue.offer(statistic);
    }

    @Override
    public List<URL> lookup(URL query) {
        return null;
    }

    private synchronized void write() throws Exception {
        Statistic statistic = queue.take();
        Date now = Calendar.getInstance().getTime();
        String date = sdfDate.format(now);

        for(String type : TYPES) {
            String fileName = STATISTICS_DIRECTORY
                    + Constants.SEPARATOR_PATH + date
                    + Constants.SEPARATOR_PATH + statistic.getServiceGroup()
                    + Constants.SEPARATOR_PATH + statistic.getServiceName()
                    + Constants.SEPARATOR_PATH + statistic.getServiceVersion()
                    + Constants.SEPARATOR_PATH + statistic.getMethod()
                    + Constants.SEPARATOR_PATH + PROVIDER
                    + "." + type;
            File file = new File(fileName);
            File dir = file.getParentFile();
            if(dir != null && !dir.exists()) {
                dir.mkdirs();
            }
            FileWriter writer = new FileWriter(file, true);
            try {
                if(SUCCESS.equals(type)) {
                    writer.write(sdfTime.format(now) + " " + statistic.getTakeTime() + "\n");
                } else if(FAILURE.equals(type)) {
                    writer.write(sdfTime.format(now) + " " + statistic.getTakeTime() + "\n");
                } else if(CONCURRENT.equals(type)) {
                    writer.write(sdfTime.format(now) + " " + statistic.getConcurrent() + "\n");
                }
                writer.flush();
            } finally {
                writer.close();
            }
        }
    }
}
