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

    public String getMaLane() {
        return MaLane;
    }

    public void setMaLane(String maLane) {
        MaLane = maLane;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        Gia = gia;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public String getBaoTri() {
        return BaoTri;
    }

    public void setBaoTri(String baoTri) {
        BaoTri = baoTri;
    }

}
