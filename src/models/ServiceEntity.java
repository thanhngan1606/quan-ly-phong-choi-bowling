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

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public String getMaPhien() {
        return maPhien;
    }

    public void setMaPhien(String maPhien) {
        this.maPhien = maPhien;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        this.thanhTien = this.soLuong * this.gia; // cập nhật lại thành tiền khi đổi số lượng
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
        this.thanhTien = this.soLuong * this.gia; // cập nhật lại thành tiền khi đổi giá
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    @Override
    public String toString() {
        return String.format("Mã DV: %s | Mã Phiên: %s | Tên DV: %s | SL: %d | Giá: %.2f | Thành Tiền: %.2f",
                maDV, maPhien, tenDV, soLuong, gia, thanhTien);
    }
}
