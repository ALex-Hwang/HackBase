package com.hackbase.regionserver.Controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;

public class Minisql {

    private static String execPath = "D:\\Repository\\Java\\demo\\minisql.exe";

    public static String exec(String order) throws Exception {
        if (order.startsWith("http")) {
            recovery(order + "/log.txt");
            return "Recovery Done!";
        }

        // 指定执行路径
        var processBuilder = new ProcessBuilder();
        processBuilder.command(execPath);

        // 输入输出重定向
        File out = new File("./out");
        File in = new File("./input");
        processBuilder.redirectOutput(out);
        processBuilder.redirectInput(in);

        FileWriter fileWritter = new FileWriter(in.getName(), false);
        fileWritter.write(order + "\nquit;\n");
        fileWritter.flush();

        // 读取结果输出
        return execPro(processBuilder, out);
    }

    public static void recovery(String IP) throws Exception {
        downloadNet(IP);
        File out = new File("./out");
        File in = new File("./log_temp.txt");

        var processBuilder = new ProcessBuilder();
        processBuilder.command(execPath);

        processBuilder.redirectOutput(out);
        processBuilder.redirectInput(in);

        FileWriter fileWritter = new FileWriter(in.getName(), true);
        fileWritter.write("quit;\n");
        fileWritter.flush();

        var process = processBuilder.start();
        var ret = process.waitFor();
        in.delete();
        return;
    }

    public static String execPro(ProcessBuilder processBuilder, File out) throws Exception {
        var process = processBuilder.start();
        var ret = process.waitFor();
        char[] temp = new char[1024];
        FileReader fileReader = new FileReader(out);
        fileReader.read(temp);
        return new String(temp);
    }

    public static void downloadNet(String IP) throws Exception {
        int bytesum = 0;
        int byteread = 0;

        // 指定log下载路径
        URL url = new URL(IP);
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream("./log_temp.txt");
            byte[] buffer = new byte[1204];

            // 写入本地指定文件
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
