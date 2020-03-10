import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class call {
    private static Logger logger= LoggerFactory.getLogger(call.class);
    public static void main(String[] args) throws Exception {

        //读取配置文件
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        BufferedReader inputStream1 = new BufferedReader(new FileReader("C:/Work/2020/02/callDemo/src/resources/callConfiguration.properties"));
        //读取包同一级目录下的配置文件
//        FileInputStream inputStream1 = new FileInputStream("callConfiguration.properties");
        // 使用properties对象加载输入流
        properties.load(inputStream1);
        //获取key对应的value值
        ip = properties.getProperty("IP");
        path = properties.getProperty("Path");
        isGet = properties.getProperty("isGet").toUpperCase();
        tatol = Integer.valueOf(properties.getProperty("tatol"));
        sleep = Long.valueOf(properties.getProperty("sleep"));
        token = properties.getProperty("token");
        param = properties.getProperty("param");
        for(int i=0;i<tatol;i++){
            executor.execute(makeRunnable(i));
        }
//          待线程池以及缓存队列中所有的线程任务完成后关闭线程池。
        executor.shutdown();
    }
    static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 20, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(100000)) ;
    static String ip;
    static String path;
    static String isGet="GET";
    static Integer tatol;
    static Long sleep;
    static String token;
    static String param;
//    public static final Thread runnable = new MyTask("Thread");
    public static Logger log= LoggerFactory.getLogger(call.class);
    public static final Runnable makeRunnable (int i){
        Thread runnable = new MyTask("Thread:"+i,sleep);
        return runnable;
    }
    //启动一个新的线程调用接口
    public static void runCall(){
        HttpURLConnection conn = null;
        String str;
//        if(!"GET".equals(isGet)){
//            str=ip+path+"?"+param;
//        }else {
//            str=ip+path+"?"+param;
//        }
        str=ip+path+"?"+param;
        //访问接口
        try {
            URL url = new URL(str);
//            System.out.println(token);
            conn = (HttpURLConnection) url.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("Content-type", "application/json;charset=utf-8");
            conn.setRequestProperty("Authorization",token);
//            conn.setRequestProperty("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU4MzcyNjY3OCwiaWF0IjoxNTgzNzE5NDc4fQ.ubjTT74entyfFvwB2ZpqBqRS-kk-DRvdPqiSJS3O62l44v82WvS1ecvFHmtks1EU6eYUc1c43dHh8A1suTp7wg");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//            conn.setConnectTimeout(3000);
//            conn.setReadTimeout(3000);
//            System.out.println(conn.getHeaderFields());
            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            //post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //请求方式
            conn.setRequestMethod(isGet);
//            System.out.println(conn.getHeaderFields());
            conn.connect();
//            if("POST".equals(isGet)){
//                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
//                os.write(param);
//                os.flush();
//                os.close();
//            }
//            logger.warn("responseCode="+conn.getResponseCode()+conn.getRequestMethod());

//           获取响应
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null){
//                System.out.println(line);
//            }
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }finally {
            if (conn != null) {
                conn.disconnect();
            }

        }
    }
}
/**
 *线程类
 */
class MyTask extends Thread {
    private String str;
    private Long sleep;
    public  MyTask(String i,Long sleep){
        this.str=i;
        this.sleep=sleep;
    }
    public  MyTask(){
    }
    @Override
    public void run() {
        try {
            System.out.println(str);
            call.runCall();
            this.sleep(sleep);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
