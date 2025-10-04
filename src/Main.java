import models.Lane;
import service.LaneService;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static LaneService laneService = new LaneService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("=== QUẢN LÝ ĐƯỜNG BOWLING ===");
            System.out.println("1. Thêm lane mới");
            System.out.println("2. Sửa lane");
            System.out.println("3. Xóa lane");
            System.out.println("4. Tìm kiếm lane (theo mã hoặc tên)");
            System.out.println("5. Hiển thị tất cả lane");
            System.out.println("6. Quản lý trạng thái lane");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ đệm

            switch (choice) {
                case 1:
                    themLaneMoi();
                    break;
                case 2:
                    suaLane();
                    break;
                case 3:
                    xoaLane();
                    break;
                case 4:
                    timKiemLane();
                    break;
                case 5:
                    hienThiTatCaLane();
                    break;
                case 6:
                    quanLyTrangThai();
                    break;
                case 0:
                    System.out.println("Thoát chương trình.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void themLaneMoi() {
        System.out.print("Nhập mã lane: ");
        String maLane = scanner.nextLine();
        System.out.print("Nhập tên lane: ");
        String tenLane = scanner.nextLine();
        System.out.print("Nhập trạng thái (trống/đang chơi/bảo trì): ");
        String trangThai = scanner.nextLine();
        System.out.print("Nhập giá/giờ: ");
        double giaGio = scanner.nextDouble();
        scanner.nextLine(); // Xóa bộ đệm
        System.out.print("Nhập thông tin bảo trì: ");
        String baoTri = scanner.nextLine();

        Lane lane = new Lane(maLane, tenLane, trangThai, giaGio, baoTri);
        laneService.saveLane(lane);
        System.out.println("Thêm lane thành công!");
    }

    private static void suaLane() {
        System.out.print("Nhập mã lane cần sửa: ");
        String maLane = scanner.nextLine();
        Lane existingLane = laneService.findLaneByMaHoacTen(maLane);
        if (existingLane == null) {
            System.out.println("Không tìm thấy lane!");
            return;
        }

        System.out.print("Nhập tên mới (hoặc Enter để giữ nguyên): ");
        String tenLane = scanner.nextLine().isEmpty() ? existingLane.getTenLane() : scanner.nextLine();
        System.out.print("Nhập trạng thái mới (hoặc Enter để giữ nguyên): ");
        String trangThai = scanner.nextLine().isEmpty() ? existingLane.getTrangThai() : scanner.nextLine();
        System.out.print("Nhập giá/giờ mới (hoặc Enter để giữ nguyên): ");
        double giaGio = scanner.nextLine().isEmpty() ? existingLane.getGiaGio() : scanner.nextDouble();
        scanner.nextLine(); // Xóa bộ đệm
        System.out.print("Nhập thông tin bảo trì mới (hoặc Enter để giữ nguyên): ");
        String baoTri = scanner.nextLine().isEmpty() ? existingLane.getBaoTri() : scanner.nextLine();

        Lane updatedLane = new Lane(maLane, tenLane, trangThai, giaGio, baoTri);
        laneService.updateLane(maLane, updatedLane);
        System.out.println("Sửa lane thành công!");
    }

    private static void xoaLane() {
        System.out.print("Nhập mã lane cần xóa: ");
        String maLane = scanner.nextLine();
        laneService.deleteLane(maLane);
        System.out.println("Xóa lane thành công!");
    }

    private static void timKiemLane() {
        System.out.print("Nhập mã hoặc tên lane để tìm kiếm: ");
        String searchTerm = scanner.nextLine();
        Lane lane = laneService.findLaneByMaHoacTen(searchTerm);
        if (lane != null) {
            System.out.println("Kết quả: " + lane);
        } else {
            System.out.println("Không tìm thấy lane!");
        }
    }

    private static void hienThiTatCaLane() {
        List<Lane> lanes = laneService.getAllLanes();
        if (lanes.isEmpty()) {
            System.out.println("Không có lane nào!");
        } else {
            System.out.println("Danh sách lane:");
            for (Lane lane : lanes) {
                System.out.println(lane);
            }
        }
    }

    private static void quanLyTrangThai() {
        System.out.print("Nhập mã lane để quản lý trạng thái: ");
        String maLane = scanner.nextLine();
        Lane lane = laneService.findLaneByMaHoacTen(maLane);
        if (lane == null) {
            System.out.println("Không tìm thấy lane!");
            return;
        }

        System.out.println("Trạng thái hiện tại: " + lane.getTrangThai());
        System.out.print("Nhập trạng thái mới (trống/đang chơi/bảo trì): ");
        String newTrangThai = scanner.nextLine();
        lane.setTrangThai(newTrangThai);
        laneService.updateLane(maLane, lane);
        System.out.println("Cập nhật trạng thái thành công!");
    }
}
