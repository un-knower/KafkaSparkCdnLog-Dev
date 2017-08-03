
import java.io.*;
import java.util.List;

/**
 * Created by dengcan1 on 2017/6/7.
 * 消费本地文件数据，分批构造Record + write + commit
 */
public class FileConsumer implements Runnable{
    // *************************************Params_start*************************************

    public static FileMaker fileMaker;
    public static List<String> filenames;
    private static File file;   // 当前正在读取的文件
    private static InputStream inputStream = null;
    private static InputStreamReader reader = null;
    private static BufferedReader bufferedReader = null;
    private static File dest = new File("d:\\myTest.txt");   // 写入的文件
    private static PrintStream printStream = null;

    public FileConsumer() {
        this.fileMaker = FileMaker.getInstance();
        filenames = fileMaker.getFileNames();
    }

    public List getFilesName() {
        return filenames;
    }
    public void run() {
        int i = 1;

        try {
            printStream = new PrintStream(new FileOutputStream(dest));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            // 有新文件
            if(filenames.size() != 0) {
                System.out.println(i++);
                try {
                    // 创建输入流
                    file = new File(filenames.get(0));
                    inputStream = new FileInputStream(file);
                    reader = new InputStreamReader(inputStream, "UTF-8");
                    bufferedReader = new BufferedReader(reader);

                    String data = null;
                    // 逐行读取数据，构造record，达到指定条数就提交一次
                    while((data = bufferedReader.readLine()) != null) {
                        // 逐行消费数据
                        printStream.print(data + "\r\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                // 文件数据读取完毕，删除文件
                    if(!file.delete()) {
                        Thread.currentThread().interrupt();
                    }
                    filenames.remove(0);
            }else {     // 当前没有需要消费的文件
//                try {
//                    Thread.sleep(1000); // 如果文件都消费完了，休息一秒
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
////                    Thread.currentThread().interrupt();
//                }
            }
        }
    }

//    public static void main(String[] args) {
////        new Thread(new FileConsumer()).start();
//        System.out.println(new FileConsumer().getFilesName().size());
//    }
}
