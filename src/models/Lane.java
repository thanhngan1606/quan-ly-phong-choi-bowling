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

    public String getMaLane() { return maLane; }
    public void setMaLane(String maLane) { this.maLane = maLane; }
    public String getTenLane() { return tenLane; }
    public void setTenLane(String tenLane) { this.tenLane = tenLane; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public double getGiaGio() { return gia; }
    public void setGiaGio(double gia) { this.gia = gia; }
    public String getBaoTri() { return baoTri; }
    public void setBaoTri(String baoTri) { this.baoTri = baoTri; }

    @Override
    public String toString() {
        return maLane + "," + tenLane + "," + trangThai + "," + gia + "," + baoTri;
    }
}