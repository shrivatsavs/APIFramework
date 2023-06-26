package com.api.helpers;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
/**
 * Created By: Subhodip
 * Date:- 13-10-2020
 * */
public class ReadWriteCSV {
    private static Logger logger = Logger.getLogger(ReadWriteCSV.class);
    public static List<String> readCSV(String filePath) throws FileNotFoundException {
        List<String> list = new LinkedList<>();
        Scanner sc = new Scanner(new File(filePath));
        sc.useDelimiter("\n");
        while (sc.hasNext())
        {
            list.add(sc.next().replace("\r",""));
        }
        sc.close();
        return list;
    }
    public static void writeCSV(String str, String path) throws IOException {
        File file = new File(path);
        FileWriter fr = null;
        BufferedWriter br = null;
        PrintWriter pr = null;
        try {
            fr = new FileWriter(file, true);
            br = new BufferedWriter(fr);
            pr = new PrintWriter(br);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            pr.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pr.close();
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
