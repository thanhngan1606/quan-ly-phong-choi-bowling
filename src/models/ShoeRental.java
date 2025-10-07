package models;

public class ShoeRental {
    private String maThue;
    private String maPhien;
    private int size;
    private double gia;
    private String trangThai;
    public ShoeRental() {}
    public ShoeRental(String maThue, String maPhien, int size, double gia, String trangThai) {
        this.maThue = maThue;
        this.maPhien= maPhien;
        this.size = size;
        this.gia = gia;
        this.trangThai=trangThai;
    }
    public String getMaThue() {
        return maThue;
    }

    public String getMaPhien() {
        return maPhien;
    }

    public int getSize() {
        return size;
    }

    public double getGia() {
        return gia;
    }

    public String getTrangThai() {
        return trangThai;
    }

    // Setters
    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

    public void setMaPhien(String maPhien) {
        this.maPhien = maPhien;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "Mã Thuê: " + maThue +
                " | Mã Phiên: " + maPhien +
                " | Size: " + size +
                " | Giá: " + gia +
                " | Trạng Thái: " + trangThai;
    }
}