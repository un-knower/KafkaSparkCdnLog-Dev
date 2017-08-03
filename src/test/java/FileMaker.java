import org.apache.commons.lang.time.DateUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dengcan1 on 2017/6/6.
 * 将List里的数据按照指定文件大小存入文件
 */

public class FileMaker {
    private File file;
    private PrintStream ps;
    private long cTime; // 文件创建时间
    private int fileSize = 122;  // 写入的单个文件大小122K
    private List<String> fileNames;

    // 单例模式获取FileMaker
    private static FileMaker instance = new FileMaker();
    public static FileMaker getInstance() {
//        if(instance == null) {
//            System.out.println("instance is null");
//            instance = new FileMaker();
//        }
        return instance;
    }
    private FileMaker() {
        try {
            String fileName = "d:\\" + DateUtils.getFragmentInMilliseconds(new Date(), 1) + ".txt";
            File f = new File(fileName);
            fileNames = new ArrayList<String>();
            fileNames.add(fileName);
            this.file = f;
            this.ps = new PrintStream(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写文件，如果文件超过64M，new新文件
     * @param strings 数据
     */
    public void writeLine(List strings) throws FileNotFoundException {
        for(int i = 0; i < strings.size(); i++) {
            String s = strings.get(i).toString();
            if((file.length()/(1024)) > fileSize) {
                // make new file
                try {
                    String fileName = "d:\\" + DateUtils.getFragmentInMilliseconds(new Date(), 1) + ".txt";
                    File f = new File(fileName);
                    fileNames.add(fileName);
                    this.file = f;
                    this.ps = new PrintStream(new FileOutputStream(file));
                } catch (IOException e) {
                    Thread.currentThread().interrupt();
                }
            }
            ps.print(s + "\r\n");
        }
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public static void main(String[] args) {
        try {
            // 读取数据
            FileMaker fileMaker = FileMaker.getInstance();
            File input = new File("D:\\testData.txt");
            FileReader fr = new FileReader(input);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            List list = new ArrayList();
            while((line = br.readLine()) != null) {
                list.add(line);
            }

            // 启动消费新文件内容的线程
            new Thread(new FileConsumer()).start();

            // 重复写n次
            for(int i = 0; i < 10; i++) {
                fileMaker.writeLine(list);
//                Thread.currentThread().sleep(5000);
            }
            System.out.println(FileMaker.getInstance().fileNames.size());
            br.close();
            fr.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(FileMaker.getInstance().fileNames.size());
//        while(true) {}
    }
}