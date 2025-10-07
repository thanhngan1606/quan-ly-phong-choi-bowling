package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameSession {
    private String maPhien;
    private String maKH;
    private String maLane;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private double tongTien;

    public GameSession() {}

    public GameSession(String maPhien, String maKH, String maLane,
                       LocalDateTime batDau, LocalDateTime ketThuc, Double tongTien) {
        this.maPhien = maPhien;
        this.maKH = maKH;
        this.maLane = maLane;
        this.thoiGianBatDau = batDau;
        this.thoiGianKetThuc = ketThuc;
        this.tongTien = tongTien;
    }

    // --- Getter & Setter ---
    public String getMaPhien() { return maPhien; }
    public void setMaPhien(String maPhien) { this.maPhien = maPhien; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getMaLane() { return maLane; }
    public void setMaLane(String maLane) { this.maLane = maLane; }

    public LocalDateTime getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public LocalDateTime getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    // --- Định dạng thời gian & serialize ---
    public static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static GameSession parse(String line) {
        String[] p = line.split("\\|", -1);
        return new GameSession(
                p[0], p[1], p[2],
                LocalDateTime.parse(p[3], F),
                LocalDateTime.parse(p[4], F),
                Double.parseDouble(p[5])
        );
    }

    public String serialize() {
        return String.join("|",
                maPhien,
                maKH,
                maLane,
                F.format(thoiGianBatDau),
                F.format(thoiGianKetThuc),
                String.valueOf(tongTien));
    }

    @Override
    public String toString() {
        return String.format("Phiên %s - KH %s - Lane %s - %.2fđ (%s đến %s)",
                maPhien, maKH, maLane, tongTien,
                F.format(thoiGianBatDau), F.format(thoiGianKetThuc));
    }
}
