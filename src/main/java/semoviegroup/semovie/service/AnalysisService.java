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

}
