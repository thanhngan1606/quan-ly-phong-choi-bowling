package models;

import java.time.LocalDateTime;

public class GameSession {
    private String MaPhien;
    private String MaKH;
    private String MaLane;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private double tongTien;

    public GameSession() {}
    public GameSession(String MaPhien, String MaKH, String MaLane, LocalDateTime batDau, LocalDateTime ketThuc, Double tongTien) {
        this.MaPhien= MaPhien;
        this.MaKH = MaKH;
        this.MaLane = MaLane;
        this.thoiGianBatDau = batDau;
        this.thoiGianKetThuc = ketThuc;
        this.tongTien = tongTien;
    }

    // Getter / Setter ...
}
