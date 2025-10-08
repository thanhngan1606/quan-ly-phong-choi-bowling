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
