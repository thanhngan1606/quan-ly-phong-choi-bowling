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

    // Getter / Setter ...
}
