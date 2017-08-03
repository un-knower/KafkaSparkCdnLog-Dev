package com.jcloud.spark.plugin.cdnLog.dc;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dengcan1 on 2017/6/6.
 * 将List里的数据按照指定文件大小存入文件
 */

public class FileMaker {
    private static final Logger logger = Logger.getLogger(FileMaker.class);

    private File file = null;
    private String fileName = null;
    private PrintStream ps = null;
    private int fileSize = 64;  // file size 64M
    private ArrayList<String> fileNames = null;

    // Singleton mode to get FileMaker
    private volatile static FileMaker instance;

    public static FileMaker getInstance() {
        if (instance == null) {
            synchronized (FileMaker.class) {
                if (instance == null) {
                    instance = new FileMaker();
                }
            }
        }
        return instance;
    }

    private FileMaker() {
        try {
            this.fileName = "KafkaSparkBV08V1/KafkaSparkCDNLogBV08V1T1/" + DateUtils.getFragmentInMilliseconds(new Date(), 1) + ".txt";
            File f = new File(fileName);
            this.fileNames = new ArrayList<String>();
//            this.fileNames.add(fileName);
            this.file = f;
            this.ps = new PrintStream(new FileOutputStream(this.file));
            System.out.println("create first file: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 写文件，如果文件超过64M，new新文件
     *
     * @param strings 数据
     */
    public void writeLine(List strings) {
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i).toString();
            try {
                if ((file.length() / (1024 * 1024)) > fileSize || !file.exists()) {
//                    synchronized (fileNames) {
//                        fileNames.add(fileName);
//                    }
                    this.fileName = "KafkaSparkBV08V1/KafkaSparkCDNLogBV08V1T1/" + DateUtils.getFragmentInMilliseconds(new Date(), 1) + ".txt";
                    this.file = new File(fileName);
                    this.ps = new PrintStream(new FileOutputStream(file));
                    System.out.println("create new file: " + fileName);
                }
                ps.print(s + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public List<String> getFileNames() {
        return this.fileNames;
    }

}