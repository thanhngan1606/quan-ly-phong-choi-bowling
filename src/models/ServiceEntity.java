package models;

public class ServiceEntity {
    private String maDV;
    private String maPhien;
    private String tenDV;
    private int soLuong;
    private double gia;
    private double thanhTien;
    public ServiceEntity() {}
    public ServiceEntity(String maDV, String maPhien, String tenDV, int soLuong, double gia) {
        this.maDV = maDV;
        this.maPhien = maPhien;
        this.tenDV = tenDV;
        this.soLuong = soLuong;
        this.gia = gia;
        this.thanhTien = soLuong * gia; // tự động tính thành tiền
    }

    // Getter / Setter ...
}
