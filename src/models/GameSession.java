package models;

import java.time.LocalDateTime;

public class GameSession {
    private String maPhien;
    private String maKH;
    private String maLane;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private double tongTien;

    public GameSession() {}
    public GameSession(String maPhien, String maKH, String maLane, LocalDateTime batDau, LocalDateTime ketThuc, Double tongTien) {
        this.maPhien= maPhien;
        this.maKH = maKH;
        this.maLane = maLane;
        this.thoiGianBatDau = batDau;
        this.thoiGianKetThuc = ketThuc;
        this.tongTien = tongTien;
    }

    // Getter / Setter ...
}
