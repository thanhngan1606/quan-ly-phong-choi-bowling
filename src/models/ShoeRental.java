package models;

public class ShoeRental {
    private String MaThue;
    private String MaPhien;
    private int Size;
    private double Gia;

    public ShoeRental() {}
    public ShoeRental(String MaThue, String MaPhien, int Size, double Gia) {
        this.MaThue = MaThue;
        this.MaPhien= MaPhien;
        this.Size = Size;
        this.Gia = Gia;
    }

    // Getter / Setter ...
}
