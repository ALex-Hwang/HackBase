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
    private static String DOWNIP = "http://10.214.241.124:8000/test_zoo.py";

    public static String exec(String order) throws Exception {
        var processBuilder = new ProcessBuilder();
        processBuilder.command(execPath);

        File out = new File("./out");
        File in = new File("./input");

        processBuilder.redirectOutput(out);
        processBuilder.redirectInput(in);

        FileWriter fileWritter = new FileWriter(in.getName(), false);
        fileWritter.write(order + "\nquit;\n");
        fileWritter.flush();

        return execPro(processBuilder, out);
    }

    public static String execPro(ProcessBuilder processBuilder, File out) throws Exception {
        var process = processBuilder.start();
        var ret = process.waitFor();
        char[] temp = new char[1024];
        FileReader fileReader = new FileReader(out);
        fileReader.read(temp);
        return new String(temp);
    }

    public static void downloadNet() throws Exception {
        int bytesum = 0;
        int byteread = 0;
        URL url = new URL(DOWNIP);
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream("D:\\test_zoo.py");

            byte[] buffer = new byte[1204];
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
