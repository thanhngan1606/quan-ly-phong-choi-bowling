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
    

    public String getMaLane() {
        return MaLane;
    }

    public void setMaLane(String maLane) {
        MaLane = maLane;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        Gia = gia;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public String getBaoTri() {
        return BaoTri;
    }

    public void setBaoTri(String baoTri) {
        BaoTri = baoTri;
    }

}
