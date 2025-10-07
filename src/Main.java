import models.GameSession;
import service.GameSessionService;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Scanner;
import java.nio.file.Path;

public class Main {
    private static GameSessionService sessionService;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        sessionService = new GameSessionService(
                Path.of("data/src/gamesession.txt"),
                id -> 200000.0
        );
        menuPhien(sc);
    }

    // ===== PHIÊN CHƠI =====
    private static void menuPhien(Scanner sc) throws Exception {
        while (true) {
            System.out.println("\n--- PHIÊN CHƠI ---");
            System.out.println("1. Thêm phiên chơi");
            System.out.println("2. Sửa phiên chơi");
            System.out.println("3. Xóa phiên chơi");
            System.out.println("4. Tìm phiên chơi (mã/khách/lane)");
            System.out.println("5. Danh sách phiên chơi");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            String c = sc.nextLine();

            if (c.equals("0")) break;
            List<GameSession> ds = sessionService.list();

            switch (c) {
                case "1" -> {
                    try {
                        GameSession s = nhapPhien(sc);
                        sessionService.createOrUpdate(s);
                        System.out.println("✅ Đã thêm phiên chơi thành công!");
                    } catch (Exception ex) {
                        System.out.println("⛔ " + ex.getMessage());
                    }
                }

                case "2" -> {
                    System.out.print("Mã phiên cần sửa: ");
                    String ma = sc.nextLine();
                    Optional<GameSession> o = sessionService.get(ma);
                    if (o.isEmpty()) {
                        System.out.println("⛔ Không tìm thấy phiên chơi!");
                        break;
                    }
                    try {
                        GameSession s = nhapPhienSua(sc, o.get());
                        sessionService.createOrUpdate(s);
                        System.out.println("✅ Đã cập nhật phiên chơi thành công!");
                    } catch (Exception ex) {
                        System.out.println("⛔ " + ex.getMessage());
                    }
                }

                case "3" -> {
                    System.out.print("Mã: ");
                    String ma = sc.nextLine();
                    if (!sessionService.delete(ma))
                        System.out.println("Không tìm thấy phiên chơi!");
                    else
                        System.out.println("Đã xóa phiên chơi thành công!");
                }

                case "4" -> {
                    System.out.print("Từ khóa: ");
                    String k = sc.nextLine().toLowerCase();
                    ds.stream().filter(x ->
                                    x.getMaPhien().toLowerCase().contains(k)
                                            || x.getMaKH().toLowerCase().contains(k)
                                            || x.getMaLane().toLowerCase().contains(k))
                            .forEach(Main::inPhien);
                }

                case "5" -> ds.forEach(Main::inPhien);
                default -> System.out.println("Sai lựa chọn!");
            }
        }
    }

    // ===== HÀM NHẬP PHIÊN MỚI =====
    private static GameSession nhapPhien(Scanner sc) {
        System.out.print("Mã phiên: ");
        String ma = sc.nextLine().trim();
        System.out.print("Mã KH: ");
        String maKh = sc.nextLine().trim();

        System.out.print("Bắt đầu (yyyy-MM-dd HH:mm): ");
        LocalDateTime bd = LocalDateTime.parse(sc.nextLine().trim(), GameSession.F);
        System.out.print("Kết thúc (yyyy-MM-dd HH:mm): ");
        LocalDateTime kt = LocalDateTime.parse(sc.nextLine().trim(), GameSession.F);

        System.out.print("Xem danh sách lane trống? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            List<String> allLanes = List.of("L1", "L2", "L3", "L4");
            List<String> lanesTrong = sessionService.findAvailableLanes(allLanes, bd, kt);
            if (lanesTrong.isEmpty())
                System.out.println("⛔ Không có lane trống trong thời gian này!");
            else
                System.out.println("✅ Lane trống: " + String.join(", ", lanesTrong));
        }

        System.out.print("Mã lane muốn đặt: ");
        String maLane = sc.nextLine().trim();

        return new GameSession(ma, maKh, maLane, bd, kt, 0.0);
    }

    // ===== 🆕 HÀM NHẬP PHIÊN SỬA (ENTER GIỮ NGUYÊN) =====
    private static GameSession nhapPhienSua(Scanner sc, GameSession old) {
        System.out.println("Giữ nguyên thông tin cũ bằng cách nhấn Enter ↓");

        System.out.printf("Mã KH (%s): ", old.getMaKH());
        String maKh = sc.nextLine().trim();
        if (maKh.isBlank()) maKh = old.getMaKH();

        System.out.printf("Mã lane (%s): ", old.getMaLane());
        String maLane = sc.nextLine().trim();
        if (maLane.isBlank()) maLane = old.getMaLane();

        System.out.printf("Bắt đầu (%s): ", GameSession.F.format(old.getThoiGianBatDau()));
        String bdStr = sc.nextLine().trim();
        LocalDateTime bd = bdStr.isBlank()
                ? old.getThoiGianBatDau()
                : LocalDateTime.parse(bdStr, GameSession.F);

        System.out.printf("Kết thúc (%s): ", GameSession.F.format(old.getThoiGianKetThuc()));
        String ktStr = sc.nextLine().trim();
        LocalDateTime kt = ktStr.isBlank()
                ? old.getThoiGianKetThuc()
                : LocalDateTime.parse(ktStr, GameSession.F);

        return new GameSession(old.getMaPhien(), maKh, maLane, bd, kt, old.getTongTien());
    }

    // ===== HIỂN THỊ =====
    private static void inPhien(GameSession s) {
        System.out.printf("%s | KH:%s | L:%s | %s -> %s | %.2f\n",
                s.getMaPhien(), s.getMaKH(), s.getMaLane(),
                GameSession.F.format(s.getThoiGianBatDau()),
                GameSession.F.format(s.getThoiGianKetThuc()),
                s.getTongTien());
    }
}
