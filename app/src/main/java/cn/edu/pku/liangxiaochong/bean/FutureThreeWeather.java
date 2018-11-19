package cn.edu.pku.liangxiaochong.bean;

public class FutureThreeWeather {

    private String date;
    private String high;
    private String low;
    private String type;
    private String fengli;

    private String date2;
    private String high2;
    private String low2;
    private String type2;
    private String fengli2;

    private String date3;
    private String high3;
    private String low3;
    private String type3;
    private String fengli3;

    public FutureThreeWeather(
            String date, String high, String low, String type, String wind,
            String date2, String high2, String low2, String type2, String wind2,
            String date3, String high3, String low3, String type3, String wind3
    ) {
        this.date=date;
        this.high=high;
        this.low=low;
        this.type=type;
        this.fengli=fengli;

        this.date2=date2;
        this.high2=high2;
        this.low2=low2;
        this.type2=type2;
        this.fengli2=fengli2;

        this.date3=date3;
        this.high3=high3;
        this.low3=low3;
        this.type3=type3;
        this.fengli3=fengli3;
    }


    public String getFengli() { return fengli;}
    public String getDate() {
        return date;
    }
    public String getHigh() {
        return high;
    }
    public String getLow() {
        return low;
    }
    public String getType() {
        return type;
    }

    public String getFengli2() { return fengli2;}
    public String getDate2() {
        return date2;
    }
    public String getHigh2() {
        return high2;
    }
    public String getLow2() {
        return low2;
    }
    public String getType2() {
        return type2;
    }

    public String getFengli3() { return fengli3;}
    public String getDate3() {
        return date3;
    }
    public String getHigh3() {
        return high3;
    }
    public String getLow3() {
        return low3;
    }
    public String getType3() {
        return type3;
    }
}
