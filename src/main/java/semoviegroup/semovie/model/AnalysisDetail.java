package semoviegroup.semovie.model;

import lombok.Data;

import java.util.List;

@Data
public class AnalysisDetail {

    private int num;//正或负面评价数量
    private List<String> list;//情感数值最极端的5条评论

    public AnalysisDetail(int num, List<String> list) {
        this.num = num;
        this.list = list;
    }

    public AnalysisDetail(){}

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "AnalysisDetail{" +
                "num=" + num +
                ", list=" + list +
                '}';
    }
}
