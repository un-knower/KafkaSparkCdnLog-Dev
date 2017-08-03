package com.jcloud.spark.plugin.cdnLog.dc;

import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.util.*;

public class KafkaSparkCDNLogBV08V1T1 {
    // *************************************Params_start*************************************
    private static Duration duration = new Duration(ConstantCDNLog.duration);

    // *************************************Params_end***************************************




    public static void main(String[] args) throws InterruptedException {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(ConstantCDNLog.appName_CDNLogBV08V1);
        //sparkConf.setMaster(ConstantCDNCloud.master);
        sparkConf.set("spark.executor.extraJavaOptions", "-XX:PermSize=5120m -XX:MaxPermSize=8192m");
        sparkConf.set("spark.dynamicAllocation.minExecutors", "10");
        sparkConf.set("spark.dynamicAllocation.maxExecutors", "20");
        sparkConf.set("spark.streaming.kafka.maxRatePerPartition", String.valueOf(10000));
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(javaSparkContext, duration);

        //Set<String> topicsSet = new HashSet<String>(Arrays.asList(ConstantPageEntrance.topicPageEntrance));
        Set<String> topicsSet = new HashSet<String>();
        topicsSet.add(ConstantCDNLog.topicLine_CDNLogBV08V1);


        Map<String, String> kafkaParams = new HashMap<String, String>();
        // kafkaParams.put("metadata.broker.list", ConstantCDNCloud.brokerlistLine80);
        kafkaParams.put("metadata.broker.list", ConstantCDNLog.brokerlist_CDNLogBV08V1);
        kafkaParams.put("group.id", ConstantCDNLog.groupId_CDNLogBV08V1);

        // <topic, message> pair stream
        JavaPairDStream<String, String> kafkaPairDStream = KafkaUtils.createDirectStream(javaStreamingContext,
                String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topicsSet);

        // message stream
        JavaDStream<String> message = kafkaPairDStream.map(new Function<Tuple2<String, String>, String>() {
            private static final long serialVersionUID = 1L;

            public String call(Tuple2<String, String> v1) throws Exception {
                return v1._2();
            }
        });

         // 输出到kafka
        message.foreachRDD(new VoidFunction<JavaRDD<String>>() {
            private static final long serialVersionUID = 1L;
//            private int times = 1;  // preWrite log times
            public void call(final JavaRDD<String> arg0) throws Exception {
                // has SparkDATA to consume
                if (!arg0.isEmpty() && arg0.count() >= 1) {
                    List list = arg0.collect();
                    // write to log for cache
                    FileMaker.getInstance().writeLine(list);
                }
                System.out.println("==========================" + arg0.count() + "==================================");
                Thread.currentThread().sleep(500L);     // time leave for consume
//                if(times == 0) {
//                    new Thread(new FileConsumer(FileMaker.getInstance())).start();
//                    times--;
//                } else {
//                    if(times != -1) {
//                        times--;
//                    }
//                }
            }
        });
        // Start the computation
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }

}
