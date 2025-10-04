package models;

public class ShoeRental {
    private String maThue;
    private String maPhien;
    private int size;
    private double gia;
public String trangThai;
    public ShoeRental() {}
    public ShoeRental(String maThue, String maPhien, int size, double gia, String trangThai) {
        this.maThue = maThue;
        this.maPhien= maPhien;
        this.size = size;
        this.gia = gia;
        this.trangThai=trangThai;
    }

    // Getter / Setter ...
}
