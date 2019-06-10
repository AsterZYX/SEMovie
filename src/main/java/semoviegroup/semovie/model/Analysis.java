package semoviegroup.semovie.model;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
public class Analysis {

    private AnalysisDetail positive;//正面评价信息
    private AnalysisDetail negative;//负面评价信息
    private String summary;//所有评论的关键句
    private List<String> keywords;//频次最多的10个关键词

    public Analysis(AnalysisDetail positive, AnalysisDetail negative, String summary, List<String> keywords) {
        this.positive = positive;
        this.negative = negative;
        this.summary = summary;
        this.keywords = keywords;
    }

    public Analysis() {
    }

    public AnalysisDetail getPositive() {
        return positive;
    }

    public void setPositive(AnalysisDetail positive) {
        this.positive = positive;
    }

    public AnalysisDetail getNegative() {
        return negative;
    }

    public void setNegative(AnalysisDetail negative) {
        this.negative = negative;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "Analysis{" +
                "positive=" + positive +
                ", negative=" + negative +
                ", summary='" + summary + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}
