package models;

public class ServiceEntity {
    private String MaDV;
    private String MaPhien;
    private String TenDV;
    private int SoLuong;
    private double Gia;
    private double ThanhTien;
    public ServiceEntity() {}
    public ServiceEntity(String MaDV, String MaPhien, String TenDV, int SoLuong, double Gia) {
        this.MaDV = MaDV;
        this.MaPhien = MaPhien;
        this.TenDV = TenDV;
        this.SoLuong = SoLuong;
        this.Gia = Gia;
        this.ThanhTien = SoLuong * Gia; // tự động tính thành tiền
    }

    // Getter / Setter ...
}
