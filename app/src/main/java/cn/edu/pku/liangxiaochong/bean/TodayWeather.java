package cn.edu.pku.liangxiaochong.bean;

public class TodayWeather {
    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25;
    private String quality;
    private String fengxiang;
    private String fengli;
    private String date;
    private String high;
    private String low;
    private String type;

    //昨天
    private String date11;
    private String high11;
    private String low11;
    private String type11;
    private String fengli11;

    //明天
    private String date1;
    private String high1;
    private String low1;
    private String type1;
    private String fengli1;
    //后天
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

    private String date4;
    private String high4;
    private String low4;
    private String type4;
    private String fengli4;



    public String getCity() {
        return city;
    }
    public String getUpdatetime() {
        return updatetime;
    }
    public String getWendu() {
        return wendu;
    }
    public String getShidu() {
        return shidu;
    }
    public String getPm25() {
        return pm25;
    }
    public String getQuality() {
        return quality;
    }
    public String getFengxiang() {
        return fengxiang;
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

    public String getFengli11() { return fengli11;}
    public String getDate11() { return date11; }
    public String getHigh11() {
        return high11;
    }
    public String getLow11() {
        return low11;
    }
    public String getType11() {
        return type11;
    }


    public String getFengli1() { return fengli1;}
    public String getDate1() {
        return date1;
    }
    public String getHigh1() {
        return high1;
    }
    public String getLow1() {
        return low1;
    }
    public String getType1() {
        return type1;
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

    public String getFengli4() { return fengli4;}
    public String getDate4() {
        return date4;
    }
    public String getHigh4() {
        return high4;
    }
    public String getLow4() {
        return low4;
    }
    public String getType4() {
        return type4;
    }


    public void setCity(String city) {
        this.city = city;
    }
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
    public void setWendu(String wendu) {
        this.wendu = wendu;
    }
    public void setShidu(String shidu) {
        this.shidu = shidu;
    }
    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }
    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }
    public void setFengli(String fengli)  {
        this.fengli = fengli;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setHigh(String high) {
        this.high = high;
    }
    public void setLow(String low) {
        this.low = low;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setFengli11(String fengli)  {
        this.fengli11 = fengli;
    }
    public void setDate11(String date) {
        this.date11 = date;
    }
    public void setHigh11(String high) {
        this.high11= high;
    }
    public void setLow11(String low) {
        this.low11 = low;
    }
    public void setType11(String type) {
        this.type11 = type;
    }


    public void setFengli1(String fengli)  {
        this.fengli1 = fengli;
    }
    public void setDate1(String date) {
        this.date1 = date;
    }
    public void setHigh1(String high) {
        this.high1 = high;
    }
    public void setLow1(String low) {
        this.low1 = low;
    }
    public void setType1(String type) {
        this.type1 = type;
    }

    public void setFengli2(String fengli)  {
        this.fengli2 = fengli;
    }
    public void setDate2(String date) {
        this.date2 = date;
    }
    public void setHigh2(String high) {
        this.high2 = high;
    }
    public void setLow2(String low) {
        this.low2 = low;
    }
    public void setType2(String type) {
        this.type2 = type;
    }

    public void setFengli3(String fengli)  {
        this.fengli3 = fengli;
    }
    public void setDate3(String date) {
        this.date3 = date;
    }
    public void setHigh3(String high) {
        this.high3 = high;
    }
    public void setLow3(String low) {
        this.low3 = low;
    }
    public void setType3(String type) {
        this.type3 = type;
    }

    public void setFengli4(String fengli)  {
        this.fengli4 = fengli;
    }
    public void setDate4(String date) {
        this.date4 = date;
    }
    public void setHigh4(String high) {
        this.high4 = high;
    }
    public void setLow4(String low) {
        this.low4 = low;
    }
    public void setType4(String type) {
        this.type4 = type;
    }





    @Override
    public String toString() {
        return "TodayWeather{" +
                "city=\'" + city + "\'" +
                ", updatetime=\'" + updatetime + "\'" +
                ", wendu=\'" + wendu + "\'" +
                ", shidu=\'" + shidu + "\'" +
                ", pm25=\'" + pm25 + "\'" +
                ", quality=\'" + quality + "\'" +
                ", fengxiang=\'" + fengxiang + "\'" +
                ", fengli=\'" + fengli + "\'" +
                ", date=\'" + date + "\'" +
                ", high=\'" + high + "\'" +
                ", low=\'" + low + "\'" +
                ", type=\'" + type + "\'" +
                ", date11=\'" + date11 + "\'" +
                ", date1=\'" + date1 + "\'" +
                ", date2=\'" + date2 + "\'" +
                ", date3=\'" + date3 + "\'" +
                ", date4=\'" + date4 + "\'" +
                "}";
    }


}
