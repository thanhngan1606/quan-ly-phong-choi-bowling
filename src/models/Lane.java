package models;

public class Lane {
    private String MaLane;
    private String  Ten;

    private String Gia;
    private String TrangThai;
    private  String BaoTri;

    public Lane() {}
    public Lane(String MaLane, String Ten, String TrangThai, String BaoTri, String Gia) {
        this.MaLane = MaLane;
        this.Ten = Ten;
        this.TrangThai = TrangThai;
        this.Gia = Gia;
        this.BaoTri = BaoTri;
    }

    // Getter / Setter ...
}
