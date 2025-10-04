package models;

public class Lane {
  private String maLane;
    private String tenLane;
    private String trangThai;
    private double gia;
    private String baoTri;

    public Lane(String maLane, String tenLane, String trangThai, double gia, String baoTri) {
        this.maLane = maLane;
        this.tenLane = tenLane;
        this.trangThai = trangThai;
        this.gia = gia;
        this.baoTri = baoTri;
    }

}
