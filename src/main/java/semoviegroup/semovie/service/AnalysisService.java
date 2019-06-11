package semoviegroup.semovie.service;

import org.springframework.stereotype.Service;
import semoviegroup.semovie.model.Analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalysisService {


    //tyf
    public String getSummaryAndKeywords(String moviename) {
        String s = "";
        try {
            //AnalysisClient ac = new AnalysisClient();
            //String re = ac.getSummaryAndKeywords(moviename);


            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("D:\\PycharmProjects\\SEmovie\\tyfResult.txt")));
            s = bufferedReader.readLine();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    //sqq's bad comments
    public String getBadComments(String moviename) {
        String s = "";
        String r="";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("D:\\PycharmProjects\\SEmovie\\badComments.txt")));

            while((s=bufferedReader.readLine())!=null){
                r=r+s;
            }
            //s = bufferedReader.readLine();
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"+r);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    //sqq's god comments
    public String getGoodComments(String moviename) {
        String s = "";
        String r="";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("D:\\PycharmProjects\\SEmovie\\goodComments.txt")));

            while((s=bufferedReader.readLine())!=null){
                r=r+s;
            }
            //s = bufferedReader.readLine();
            System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"+r);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

}
