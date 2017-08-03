package com.jcloud.spark.plugin.cdnLog.dc;

public class ConstantCDNLog {

    //public static String master = "spark://192.168.177.77:7077";

    //持续时间
    public static long duration = 300;
    //public static long duration = 500;
    //public static long duration = 2000;
    //public static long duration = 10000;

    //*****************************************KafkaSpark_CDNLogBV08的参数_start*****************************************
    //Running Applications Name
    public static String appName_CDNLogBV08V1 = "CDNLogBV08V1";
    public static String appName_CDNLogBV08V3T1 = "CDNLogBV08V3T1";
    public static String appName_CDNLogBV08V5T1 = "CDNLogBV08V5T1";
    public static String appName_CDNLogBV08V5T2 = "CDNLogBV08V5T2";

    public static String topicLine_CDNLogBV08V1 = "log-37-107584-cdnpub";

    public static String brokerlist_CDNLogBV08V1 = "172.20.131.67:9092,172.20.131.68:9092,172.20.131.69:9092,172.20.131.70:9092,172.20.131.71:9092,172.20.131.72:9092,172.20.131.73:9092,172.20.131.74:9092,172.20.131.75:9092,172.20.131.76:9092,172.20.131.77:9092,172.20.131.78:9092,172.20.131.79:9092,172.20.131.80:9092,172.20.131.81:9092,172.20.131.82:9092,172.20.131.83:9092,172.20.131.84:9092,172.20.131.85:9092,172.20.131.86:9092,172.20.131.87:9092,172.20.131.88:9092,172.20.131.89:9092,172.20.131.90:9092,172.20.131.91:9092";

    //zookeeper集群(zookeeper一般为单数集群)
    public static String zookeeper_CDNLogBV08V1 = "172.20.131.62:2181,172.20.131.63:2181,172.20.131.64:2181,172.20.131.65:2181,172.20.131.66:2181";

    //偏移量的名称
    public static String groupId_CDNLogBV08V1 = "CDNLogBV08V1_201705151720";
    public static String groupId_CDNLogBV08V3T1 = "CDNLogBV08V3_201705151720";
    public static String groupId_CDNLogBV08V5T1 = "CDNLogBV08V5_201705151720";
    public static String groupId_CDNLogBV08V5T2 = "CDNLogBV08V5_201705151720";

    //*****************************************KafkaSpark_CDNLogBV08的参数_end*******************************************


}
