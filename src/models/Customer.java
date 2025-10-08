package models;


    public class Customer {
    private String maKH;
    private String ten;
    private String sdt;
    private String vip;
    private int diemThuong;

    public Customer() {}
    public Customer(String maKH, String ten, String sdt, String vip, int diemThuong) {
        this.maKH = maKH;
        this.ten = ten;
        this.sdt = sdt;
        this.vip = vip;
        this.diemThuong = diemThuong;
    }
    public String getMaKH() {
        return maKH;
    }
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    public String getTen() {
        return ten;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }
    public String getSdt() {
        return sdt;
    }
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
    public String getVip() {
        return vip;
    }
    public void setVip(String vip) {
        this.vip = vip;
    }
    public int getDiemThuong() {
        return diemThuong;
    }
    public void setDiemThuong(int diemThuong) {
        this.diemThuong = diemThuong;
    }
    @Override
    public String toString() {
        return String.format("Mã: %-10s | Tên: %-20s | SĐT: %-12s | VIP: %-5s | Điểm: %d",
                maKH, ten, sdt, vip, diemThuong);
    }
    
    }
