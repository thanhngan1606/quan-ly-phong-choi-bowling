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

    // Getter / Setter ...
}
