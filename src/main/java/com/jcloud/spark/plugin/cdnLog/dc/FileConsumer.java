package com.jcloud.spark.plugin.cdnLog.dc;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.Duration;
import com.jcloud.data.sdk.dbus.transport.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengcan1 on 2017/6/7.
 * 消费本地文件数据，分批构造Record + write + commit
 */

public class FileConsumer implements Runnable {
    private static final Logger logger = Logger.getLogger(FileConsumer.class);

    // *************************************Params_start*************************************
    private static Duration duration = new Duration(ConstantCDNLog.duration);

    //预发布
    /*private static String accessID = "7108D2F9EEAF410BD5C27B94EACE2C2B";
    private static String accessKey = "e5ab22ae230a11e699d0246e960b0018";
    //private static String endPoint = "http://idata-bds-dbus.jcloud.com";
    private static String endPoint = "http://idata-bds-dbus.jcloud.com";
    private static String db = "kafka_test_db";
    //private static String table = "test_cdndata_new_parquet";
    private static String table = "test_cdndata_new_parquet";*/

    //线上
    private static String accessID = "F989E0621AFFDC5666531AC92A0C509F";
    private static String accessKey = "d8522947363d11e78bc7fa163e3736ee";
    private static String endPoint = "http://idata-dbus.jcloud.com";
    private static String db = "kafka_cdn";
    //private static String table = "cdndata_new_parquet";
    //private static String table = "cdndata_parquet_new";
    private static String table = "cdndata_new_parquet";
    private static String partition = "";

    private static Account account;
    private static BDS bds;

    private static TableTransport transport = null;
    private static PartitionSpec partitionSpec = null;
    private static TableTransport.UploadSession uploadSession = null;
    private static TableSchema tableSchema = null;
    private static RecordWriter recordWriter = null;
    private static Record record = null;
    private static int commitNum = 70000;
    private static int retryTimes = 5;

    private static String cqtd = "";
    private static String cqtt = "";
    private static String ttms = "";
    private static String j_forward_for = "";
    private static String crc = "";
    private static String pssc = "";
    private static String cqhl = "";
    private static String pshl = "";
    private static String pscl = "";
    private static String cqhm = "";
    private static String cquuc = "";
    private static String cqhv = "";
    private static String phr = "";
    private static String pqsi = "";
    private static String psct = "";
    private static String referer_cqh = "";
    private static String user_agent_cqh = "";
    private static String jdclt_ms = "";
    private static String jdostran_ms = "";
    private static String jdclr = "";
    private static String shn = "";
    private static String pqhl = "";
    private static String sscl = "";
    private static String sshl = "";
    private static String hostname = "";
    private static String x_Proto = "";
    // *************************************Params_end***************************************

    static {
        try {
            account = new BdsAccount(accessID, accessKey);
            bds = new BDS(account);
            bds.setEndpoint(endPoint);
            transport = new TableTransport(bds);
        } catch (BdsException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private static FileMaker fileMaker = null;
    private static List<String> filenames = null;
    private static File file = null;   // 当前正在读取的文件
    private static InputStream inputStream = null;
    private static InputStreamReader reader = null;
    private static BufferedReader bufferedReader = null;

    public FileConsumer(FileMaker fileMaker) {
        this.fileMaker = fileMaker;
    }

    public void run() {
        try {
            while (true) {
                filenames = fileMaker.getFileNames();
                // has unconsumed log
                if (filenames.size() != 0) {
                    // construct inputIO
                    System.out.println("files total count to be consume: " + filenames.size());
//                    System.out.println("consume new file: " + filenames.get(0));
                    file = new File(filenames.get(0));
                    inputStream = new FileInputStream(file);
                    reader = new InputStreamReader(inputStream, "UTF-8");
                    bufferedReader = new BufferedReader(reader);

                    String data = null;

                    partition = "cqtd=" + DataUtil.getDay();
                    partitionSpec = new PartitionSpec(partition);
                    //uploadSession = transport.createUploadSession(db, table);
                    uploadSession = transport.createUploadSession(db, table, partitionSpec);
                    tableSchema = uploadSession.getSchema();
                    recordWriter = uploadSession.openRecordWriter(0);
                    record = uploadSession.newRecord();

                    long times = 0;

                    while ((data = bufferedReader.readLine()) != null) {
                        if(times == commitNum) {
                            // upload to hive
                            recordWriter.close();
                            uploadSession.commit(new Long[]{0L});
                            System.out.println("=====upload OK!");

                            partition = "cqtd=" + DataUtil.getDay();
                            partitionSpec = new PartitionSpec(partition);
                            //uploadSession = transport.createUploadSession(db, table);
                            uploadSession = transport.createUploadSession(db, table, partitionSpec);
                            tableSchema = uploadSession.getSchema();
                            recordWriter = uploadSession.openRecordWriter(0);
                            record = uploadSession.newRecord();

                            times = 0;
                        }
                        // consume log by line
                        if (data.trim() != null && !data.trim().equals("")) {
                            ArrayList<String> linekeys = Lists.newArrayList(data.split("\\ .\\.\\."));
                            if (linekeys.size() == 26) {
                                //cqtd = linekeys.get(0);
                                cqtt = linekeys.get(1);
                                ttms = linekeys.get(2);
                                j_forward_for = linekeys.get(3);
                                crc = linekeys.get(4);
                                pssc = linekeys.get(5);
                                cqhl = linekeys.get(6);
                                pshl = linekeys.get(7);
                                pscl = linekeys.get(8);
                                cqhm = linekeys.get(9);
                                cquuc = linekeys.get(10);
                                cqhv = linekeys.get(11);
                                phr = linekeys.get(12);
                                pqsi = linekeys.get(13);
                                psct = linekeys.get(14);
                                referer_cqh = linekeys.get(15);
                                user_agent_cqh = linekeys.get(16);
                                jdclt_ms = linekeys.get(17);
                                jdostran_ms = linekeys.get(18);
                                jdclr = linekeys.get(19);
                                shn = linekeys.get(20);
                                pqhl = linekeys.get(21);
                                sscl = linekeys.get(22);
                                sshl = linekeys.get(23);
                                hostname = linekeys.get(24);
                                x_Proto = linekeys.get(25);

                                for (int i = 0; i < tableSchema.getColumns().size(); i++) {
                                    Column column = tableSchema.getColumn(i);
                                    String cloName = column.getName();
                                    /*if (cloName.equals("cqtd")) {
                                        record.setString(i, cqtd);
                                    }*/
                                    if (cloName.equals("cqtt")) {
                                        record.setString(i, cqtt);
                                    }
                                    if (cloName.equals("ttms")) {
                                        record.setString(i, ttms);
                                    }
                                    if (cloName.equals("j_forward_for")) {
                                        record.setString(i, j_forward_for);
                                    }
                                    if (cloName.equals("crc")) {
                                        record.setString(i, crc);
                                    }
                                    if (cloName.equals("pssc")) {
                                        record.setString(i, pssc);
                                    }
                                    if (cloName.equals("cqhl")) {
                                        record.setString(i, cqhl);
                                    }
                                    if (cloName.equals("pshl")) {
                                        record.setString(i, pshl);
                                    }
                                    if (cloName.equals("pscl")) {
                                        record.setString(i, pscl);
                                    }
                                    if (cloName.equals("cqhm")) {
                                        record.setString(i, cqhm);
                                    }
                                    if (cloName.equals("cquuc")) {
                                        record.setString(i, cquuc);
                                    }
                                    if (cloName.equals("cqhv")) {
                                        record.setString(i, cqhv);
                                    }
                                    if (cloName.equals("phr")) {
                                        record.setString(i, phr);
                                    }
                                    if (cloName.equals("pqsi")) {
                                        record.setString(i, pqsi);
                                    }
                                    if (cloName.equals("psct")) {
                                        record.setString(i, psct);
                                    }
                                    if (cloName.equals("referer_cqh")) {
                                        record.setString(i, referer_cqh);
                                    }
                                    if (cloName.equals("user_agent_cqh")) {
                                        record.setString(i, user_agent_cqh);
                                    }
                                    if (cloName.equals("jdclt_ms")) {
                                        record.setString(i, jdclt_ms);
                                    }
                                    if (cloName.equals("jdostran_ms")) {
                                        record.setString(i, jdostran_ms);
                                    }
                                    if (cloName.equals("jdclr")) {
                                        record.setString(i, jdclr);
                                    }
                                    if (cloName.equals("shn")) {
                                        record.setString(i, shn);
                                    }
                                    if (cloName.equals("pqhl")) {
                                        record.setString(i, pqhl);
                                    }
                                    if (cloName.equals("sscl")) {
                                        record.setString(i, sscl);
                                    }
                                    if (cloName.equals("sshl")) {
                                        record.setString(i, sshl);
                                    }
                                    if (cloName.equals("hostname")) {
                                        record.setString(i, hostname);
                                    }
                                    if (cloName.equals("x_Proto")) {
                                        record.setString(i, x_Proto);
                                    }
                                }
                                while (retryTimes >= 0) {
                                    try {
                                        recordWriter.write(record);
                                        break;
                                    } catch (IOException e) {
                                        if(retryTimes <= 0) {
                                            Thread.currentThread().interrupt();
                                        }
                                        System.out.println("retry recordWriter.write");
                                        retryTimes--;
                                        try {
                                            Thread.sleep(1000L);
                                        } catch (InterruptedException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                                retryTimes = 5;
                                times++;
                            }
                        }
                    }
                    // upload to hive
                    recordWriter.close();
                    uploadSession.commit(new Long[]{0L});
                    System.out.println("=====upload OK!");

                    // del file after finish consume
                    if (!file.delete()) {
                        System.out.println("delete file failed");
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("delete file：" + filenames.get(0));
                    filenames.remove(0);
                } else {     // no log to be consume
                    Thread.currentThread().sleep(100);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (TransportException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (BdsException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

