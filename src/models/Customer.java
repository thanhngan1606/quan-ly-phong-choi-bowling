package models;

public class Customer {
    private String MaKH;
    private String Ten;
    private String Sdt;
    private String Vip;
    private int DiemThuong;

    public Customer() {}
    public Customer(String MaKH, String Ten, String Sdt, String Vip, int DiemThuong) {
        this.MaKH = MaKH;
        this.Ten = Ten;
        this.Sdt = Sdt;
        this.Vip = Vip;
        this.DiemThuong = DiemThuong;
    }
    public String getMaKH() {
        return MaKH;
    }
    public void setMaKH(String MaKH) {
        this.MaKH = MaKH;
    }
    public String getTen() {
        return Ten;
    }
    public void setTen(String Ten) {
        this.Ten = Ten;
    }
    public String getSdt() {
        return Sdt;
    }
    public void setSdt(String Sdt) {
        this.Sdt = Sdt;
    }
    public String getVip() {
        return Vip;
    }
    public void setVip(String Vip) {
        this.Vip = Vip;
    }
    public int getDiemThuong() {
        return DiemThuong;
    }
    public void setDiemThuong(int DiemThuong) {
        this.DiemThuong = DiemThuong;
    }
    @Override
    public String toString() {
        return String.format("Mã: %-10s | Tên: %-20s | SĐT: %-12s | VIP: %-5s | Điểm: %d",
                MaKH, Ten, Sdt, Vip, DiemThuong);
    }
    // Getter / Setter ...
}
