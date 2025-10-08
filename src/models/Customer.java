package models;

import java.io.Serializable;

public class Customer implements Serializable {
    private String maKH;
    private String ten;
    private String sdt;
    private boolean vip;
    private int diemThuong;

    public Customer() {}

    public Customer(String maKH, String ten, String sdt, boolean vip, int diemThuong) {
        this.maKH = maKH;
        this.ten = ten;
        this.sdt = sdt;
        this.vip = vip;
        this.diemThuong = diemThuong;
    }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public boolean isVip() { return vip; }
    public void setVip(boolean vip) { this.vip = vip; }
    public int getDiemThuong() { return diemThuong; }
    public void setDiemThuong(int diemThuong) { this.diemThuong = diemThuong; }

    @Override
    public String toString() {
        return String.format("Mã: %-10s | Tên: %-20s | SĐT: %-12s | VIP: %-5b | Điểm: %d",
                maKH, ten, sdt, vip, diemThuong);
    }
}