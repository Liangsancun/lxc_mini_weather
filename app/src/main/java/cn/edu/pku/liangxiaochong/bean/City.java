package cn.edu.pku.liangxiaochong.bean;

public class City {
    private String province;
    private String city;
    private String number;
    private String firstPY;
    private String allPY;
    private String allFirstPY;
    //构造函数
    public City(String province, String city, String number, String firstPY, String allPY, String allFirstPY) {
        this.province = province;
        this.city = city;
        this.number = number;
        this.firstPY = firstPY;
        this.allPY = allPY;
        this.allFirstPY = allFirstPY;
    }

    public String getProvince() {
        return province;
    }
    public String getCity() {
        return city;
    }
    public String getNumber() {
        return number;
    }
    public String getFirstPY() {
        return firstPY;
    }
    public String getAllFirstPY() {
        return allFirstPY;
    }
    public String getAllPY() {return  allPY; }

}
