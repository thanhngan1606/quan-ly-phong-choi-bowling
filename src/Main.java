import java.time.LocalDateTime;
import java.util.*;
import models.*;
import service.*;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    private static LaneService laneService = new LaneService();
    private static CustomerService customerService = new CustomerService();
    private static GameSessionService gameSessionService = new GameSessionService(
        java.nio.file.Paths.get("C://dev//quan-ly-phong-choi-bowling//data//src//gamesession.txt"),
        laneId -> laneService.findLanes(laneId, null).stream().findFirst()
                .map(Lane::getGiaGio).orElse(0.0)
    );
    private static ShoeRentalService shoeRentalService = new ShoeRentalService();
    private static ServiceEntityService serviceEntityService = new ServiceEntityService("C://dev//quan-ly-phong-choi-bowling//data//src//service.txt");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("=== QUẢN LÝ BOWLING ===");
            System.out.println("1. Quản lý Lane");
            System.out.println("2. Quản lý Khách Hàng");
            System.out.println("3. Quản lý Phiên Chơi");
            System.out.println("4. Quản lý Thuê Giày");
            System.out.println("5. Quản lý Dịch Vụ");
            System.out.println("0. Thoát");
            System.out.print("Chọn module: ");

            int moduleChoice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ đệm

            switch (moduleChoice) {
                case 1:
                    manageLane();
                    break;
                case 2:
                    manageCustomer();
                    break;
                case 3:
                    manageGameSession();
                    break;
                case 4:
                    manageShoeRental();
                    break;
                case 5:
                    manageService();
                    break;
                case 0:
                    System.out.println("Thoát chương trình.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void manageLane() {
        while (true) {
            laneService.updateLaneStatusFromSessions(gameSessionService.list());
            System.out.println("=== QUẢN LÝ ĐƯỜNG BOWLING ===");
            System.out.println("1. Thêm lane mới");
            System.out.println("2. Sửa lane");
            System.out.println("3. Xóa lane");
            System.out.println("4. Tìm kiếm lane");
            System.out.println("5. Hiển thị tất cả lane");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ đệm

            if (choice == 0) break;

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
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void themLaneMoi() {
        System.out.print("Nhập mã lane: ");
        String maLane = scanner.nextLine().trim();
        if( laneService.findLanes(maLane, null).stream().anyMatch(l -> l.getMaLane().equals(maLane))) {
            System.out.println("Lỗi: Mã lane đã tồn tại. Vui lòng nhập mã khác.");
            return;
        } else if (maLane.isEmpty()) {
            System.out.println("Lỗi: Mã lane không được để trống.");
            return;
        } else if (!maLane.matches("L\\d{3}")) {
            System.out.println("Lỗi: Mã lane phải có định dạng Lxxx (x là chữ số).");
            return;
        }

        System.out.print("Nhập tên lane: ");
        String tenLane = scanner.nextLine().trim();
        if (tenLane.isEmpty()) {
            System.out.println("Lỗi: Tên lane không được để trống.");
            return;
        }

        System.out.print("Nhập trạng thái (trống/đang chơi/bảo trì): ");
        String trangThai = scanner.nextLine().trim().toLowerCase();
        if (trangThai.isEmpty() || (!trangThai.equals("trống") && !trangThai.equals("đang chơi") && !trangThai.equals("bảo trì"))) {
            System.out.println("Lỗi: Trạng thái không được để trống và chỉ được là 'trống', 'đang chơi', hoặc 'bảo trì'.");
            return;
        }

        System.out.print("Nhập giá/giờ: ");
        double gia;
        try {
            gia = Double.parseDouble(scanner.nextLine().trim());
            if (gia <= 0) {
                System.out.println("Lỗi: Giá phải là số dương.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá phải là số hợp lệ.");
            return;
        }

        System.out.print("Nhập thông tin bảo trì: ");
        String baoTri = scanner.nextLine().trim();
        if (baoTri.isEmpty()) {
            System.out.println("Lỗi: Thông tin bảo trì không được để trống.");
            return;
        }

        Lane lane = new Lane(maLane, tenLane, trangThai, gia, baoTri);
        laneService.saveLane(lane);
        System.out.println("Thêm lane thành công!");
    }

  private static void suaLane() {
    System.out.print("Nhập mã lane cần sửa: ");
    String maLane = scanner.nextLine().trim();
    List<Lane> lanes = laneService.findLanes(maLane, null);
    if (lanes.isEmpty()) {
        System.out.println("Không tìm thấy lane!");
        return;
    }
    Lane existingLane = lanes.get(0);

    System.out.print("Nhập tên mới (hoặc Enter để giữ nguyên): ");
    String tenLane = scanner.nextLine().trim();
    tenLane = tenLane.isEmpty() ? existingLane.getTenLane() : tenLane;
    if (tenLane.isEmpty()) {
        System.out.println("Lỗi: Tên lane không được để trống.");
        return;
    }

    System.out.print("Nhập giá mới (hoặc Enter để giữ nguyên): ");
    String giaInput = scanner.nextLine().trim();
    double gia = giaInput.isEmpty() ? existingLane.getGiaGio() : Double.parseDouble(giaInput);
    if (!giaInput.isEmpty() && gia <= 0) {
        System.out.println("Lỗi: Giá phải là số dương.");
        return;
    }

    System.out.print("Nhập trạng thái mới (hoặc Enter để giữ nguyên): ");
    String trangThai = scanner.nextLine().trim();
    trangThai = trangThai.isEmpty() ? existingLane.getTrangThai() : trangThai.toLowerCase();
    if (!trangThai.isEmpty() && (!trangThai.equals("trống") && !trangThai.equals("đang chơi") && !trangThai.equals("bảo trì"))) {
        System.out.println("Lỗi: Trạng thái chỉ được là 'trống', 'đang chơi', hoặc 'bảo trì'.");
        return;
    }

    System.out.print("Nhập thông tin bảo trì mới (hoặc Enter để giữ nguyên): ");
    String baoTri = scanner.nextLine().trim();
    baoTri = baoTri.isEmpty() ? existingLane.getBaoTri() : baoTri;
    if (baoTri.isEmpty()) {
        System.out.println("Lỗi: Thông tin bảo trì không được để trống.");
        return;
    }

    Lane updatedLane = new Lane(maLane, tenLane, trangThai, gia, baoTri);
    laneService.updateLane(maLane, updatedLane);
    System.out.println("Sửa lane thành công!");
}

    private static void xoaLane() {
        System.out.print("Nhập mã lane cần xóa: ");
        String maLane = scanner.nextLine().trim();
        laneService.deleteLane(maLane);
        System.out.println("Xóa lane thành công!");
    }

    private static void timKiemLane() {
        System.out.print("Nhập mã hoặc tên lane (hoặc Enter để bỏ qua): ");
        String searchTerm = scanner.nextLine().trim();
        System.out.print("Nhập trạng thái (trống/đang chơi/bảo trì, hoặc Enter để bỏ qua): ");
        String trangThai = scanner.nextLine().trim();

        List<Lane> lanes = laneService.findLanes(searchTerm, trangThai);
        if (lanes.isEmpty()) {
            System.out.println("Không tìm thấy lane nào!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            for (Lane lane : lanes) {
                System.out.println(lane);
            }
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

    private static void manageCustomer() {
        while (true) {
            System.out.println("=== QUẢN LÝ KHÁCH HÀNG ===");
            System.out.println("1. Thêm khách hàng mới");
            System.out.println("2. Sửa khách hàng");
            System.out.println("3. Xóa khách hàng");
            System.out.println("4. Tìm kiếm khách hàng");
            System.out.println("5. Hiển thị tất cả khách hàng");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ đệm

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    themKhachHangMoi();
                    break;
                case 2:
                    suaKhachHang();
                    break;
                case 3:
                    xoaKhachHang();
                    break;
                case 4:
                    timKiemKhachHang();
                    break;
                case 5:
                    customerService.printAll();
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void themKhachHangMoi() {
        System.out.print("Nhập mã KH: ");
        String maKH = scanner.nextLine().trim();
        while (customerService.find(maKH) != null) {
            System.out.println("Lỗi: Mã KH đã tồn tại. Vui lòng nhập mã khác.");
            System.out.print("Nhập mã KH: ");
            maKH = scanner.nextLine().trim();
        }

        System.out.print("Nhập tên KH: ");
        String ten = scanner.nextLine().trim();
        if (ten.isEmpty()) {
            System.out.println("Lỗi: Tên KH không được để trống.");
            return;
        }

        System.out.print("Nhập SĐT: ");
        String sdt = scanner.nextLine().trim();
        if (sdt.isEmpty()) {
            System.out.println("Lỗi: SĐT không được để trống.");
            return;
        }

        System.out.print("Nhập VIP (true/false): ");
        boolean vip = Boolean.parseBoolean(scanner.nextLine().trim());

        System.out.print("Nhập điểm thưởng: ");
        int diemThuong;
        try {
            diemThuong = Integer.parseInt(scanner.nextLine().trim());
            if (diemThuong < 0) {
                System.out.println("Lỗi: Điểm thưởng phải không âm.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Điểm thưởng phải là số hợp lệ.");
            return;
        }

        Customer customer = new Customer(maKH, ten, sdt, vip, diemThuong);
        customerService.add(customer);
        System.out.println("Thêm khách hàng thành công!");
    }

  private static void suaKhachHang() {
    System.out.print("Nhập mã KH cần sửa: ");
    String maKH = scanner.nextLine().trim();
    Customer existingCustomer = customerService.find(maKH);
    if (existingCustomer == null) {
        System.out.println("Không tìm thấy khách hàng!");
        return;
    }

    System.out.print("Nhập tên mới (hoặc Enter để giữ nguyên): ");
    String ten = scanner.nextLine().trim();
    ten = ten.isEmpty() ? existingCustomer.getTen() : ten;
    if (ten.isEmpty()) {
        System.out.println("Lỗi: Tên KH không được để trống.");
        return;
    }

    System.out.print("Nhập SĐT mới (hoặc Enter để giữ nguyên): ");
    String sdt = scanner.nextLine().trim();
    sdt = sdt.isEmpty() ? existingCustomer.getSdt() : sdt;
    if (sdt.isEmpty()) {
        System.out.println("Lỗi: SĐT không được để trống.");
        return;
    }

    System.out.print("Nhập VIP mới (true/false, hoặc Enter để giữ nguyên): ");
    String vipInput = scanner.nextLine().trim();
    boolean vip = vipInput.isEmpty() ? existingCustomer.isVip() : Boolean.parseBoolean(vipInput);

    System.out.print("Nhập điểm thưởng mới (hoặc Enter để giữ nguyên): ");
    String diemThuongInput = scanner.nextLine().trim();
    int diemThuong = diemThuongInput.isEmpty() ? existingCustomer.getDiemThuong() : Integer.parseInt(diemThuongInput);
    if (!diemThuongInput.isEmpty() && diemThuong < 0) {
        System.out.println("Lỗi: Điểm thưởng phải không âm.");
        return;
    }

    Customer updatedCustomer = new Customer(maKH, ten, sdt, vip, diemThuong);
    customerService.update(maKH, updatedCustomer);
    System.out.println("Sửa khách hàng thành công!");
}

    private static void xoaKhachHang() {
        System.out.print("Nhập mã KH cần xóa: ");
        String maKH = scanner.nextLine().trim();
        customerService.delete(maKH);
        System.out.println("Xóa khách hàng thành công!");
    }

    private static void timKiemKhachHang() {
        System.out.print("Nhập tên hoặc SĐT (hoặc Enter để bỏ qua): ");
        String searchTerm = scanner.nextLine().trim();
        List<Customer> results = new ArrayList<>();
        if (!searchTerm.isEmpty()) {
            results.addAll(customerService.searchByNameOrPhone(searchTerm));
        }
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy khách hàng!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            for (Customer c : results) {
                System.out.println(c);
            }
        }
    }

    private static void manageGameSession() {
        while (true) {
            System.out.println("=== QUẢN LÝ PHIÊN CHƠI ===");
            System.out.println("1. Thêm phiên chơi mới");
            System.out.println("2. Sửa phiên chơi");
            System.out.println("3. Xóa phiên chơi");
            System.out.println("4. Tìm kiếm phiên chơi");
            System.out.println("5. Kiểm tra lane trống");
            System.out.println("6. Hiển thị tất cả phiên chơi");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ đệm

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    themPhienChoiMoi();
                    break;
                case 2:
                    suaPhienChoi();
                    break;
                case 3:
                    xoaPhienChoi();
                    break;
                case 4:
                    timKiemPhienChoi();
                    break;
                case 5:
                    kiemTraLaneTrong();
                    break;
                case 6:
                    hienThiTatCaPhienChoi();
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void themPhienChoiMoi() {
        System.out.print("Nhập mã phiên: ");
        String maPhien = scanner.nextLine().trim();
        if (maPhien.isEmpty()) {
            System.out.println("Lỗi: Mã phiên không được để trống.");
            return;
        }

        System.out.print("Nhập mã KH: ");
        String maKH = scanner.nextLine().trim();
        if (maKH.isEmpty()) {
            System.out.println("Lỗi: Mã KH không được để trống.");
            return;
        }

        System.out.print("Nhập mã lane: ");
        String maLane = scanner.nextLine().trim();
        if (maLane.isEmpty()) {
            System.out.println("Lỗi: Mã lane không được để trống.");
            return;
        }

        System.out.print("Nhập giờ bắt đầu (yyyy-MM-dd HH:mm): ");
        LocalDateTime batDau;
        try {
            batDau = LocalDateTime.parse(scanner.nextLine().trim(), GameSession.F);
        } catch (Exception e) {
            System.out.println("Lỗi: Định dạng giờ bắt đầu không hợp lệ (yyyy-MM-dd HH:mm).");
            return;
        }

        System.out.print("Nhập giờ kết thúc (yyyy-MM-dd HH:mm): ");
        LocalDateTime ketThuc;
        try {
            ketThuc = LocalDateTime.parse(scanner.nextLine().trim(), GameSession.F);
            if (!ketThuc.isAfter(batDau)) {
                System.out.println("Lỗi: Giờ kết thúc phải sau giờ bắt đầu.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Lỗi: Định dạng giờ kết thúc không hợp lệ (yyyy-MM-dd HH:mm).");
            return;
        }

        GameSession session = new GameSession(maPhien, maKH, maLane, batDau, ketThuc, 0.0);
        gameSessionService.createOrUpdate(session);
        System.out.println("Thêm phiên chơi thành công!");
    }

    private static void suaPhienChoi() {
    System.out.print("Nhập mã phiên cần sửa: ");
    String maPhien = scanner.nextLine().trim();
    GameSession existingSession = gameSessionService.get(maPhien).orElse(null);
    if (existingSession == null) {
        System.out.println("Không tìm thấy phiên chơi!");
        return;
    }

    System.out.print("Nhập mã KH mới (hoặc Enter để giữ nguyên): ");
    String maKH = scanner.nextLine().trim();
    maKH = maKH.isEmpty() ? existingSession.getMaKH() : maKH;
    if (maKH.isEmpty()) {
        System.out.println("Lỗi: Mã KH không được để trống.");
        return;
    }

    System.out.print("Nhập mã lane mới (hoặc Enter để giữ nguyên): ");
    String maLane = scanner.nextLine().trim();
    maLane = maLane.isEmpty() ? existingSession.getMaLane() : maLane;
    if (maLane.isEmpty()) {
        System.out.println("Lỗi: Mã lane không được để trống.");
        return;
    }

    System.out.print("Nhập giờ bắt đầu mới (hoặc Enter để giữ nguyên, yyyy-MM-dd HH:mm): ");
    String batDauInput = scanner.nextLine().trim();
    LocalDateTime batDau = batDauInput.isEmpty() ? existingSession.getThoiGianBatDau() : LocalDateTime.parse(batDauInput, GameSession.F);

    System.out.print("Nhập giờ kết thúc mới (hoặc Enter để giữ nguyên, yyyy-MM-dd HH:mm): ");
    String ketThucInput = scanner.nextLine().trim();
    LocalDateTime ketThuc = ketThucInput.isEmpty() ? existingSession.getThoiGianKetThuc() : LocalDateTime.parse(ketThucInput, GameSession.F);
    if (!ketThuc.isAfter(batDau)) {
        System.out.println("Lỗi: Giờ kết thúc phải sau giờ bắt đầu.");
        return;
    }

    GameSession updatedSession = new GameSession(maPhien, maKH, maLane, batDau, ketThuc, 0.0);
    gameSessionService.createOrUpdate(updatedSession);
    System.out.println("Sửa phiên chơi thành công!");
}

    private static void xoaPhienChoi() {
        System.out.print("Nhập mã phiên cần xóa: ");
        String maPhien = scanner.nextLine().trim();
        if (gameSessionService.delete(maPhien)) {
            System.out.println("Xóa phiên chơi thành công!");
        } else {
            System.out.println("Không tìm thấy phiên chơi!");
        }
    }

    private static void timKiemPhienChoi() {
        System.out.print("Nhập mã phiên, mã KH, hoặc mã lane (hoặc Enter để bỏ qua): ");
        String searchTerm = scanner.nextLine().trim();
        List<GameSession> results = new ArrayList<>();
        if (!searchTerm.isEmpty()) {
            results.addAll(gameSessionService.searchByCriteria(searchTerm));
        }
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy phiên chơi!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            for (GameSession s : results) {
                System.out.println(s);
            }
        }
    }

    private static void kiemTraLaneTrong() {
        System.out.print("Nhập giờ bắt đầu (yyyy-MM-dd HH:mm): ");
        LocalDateTime start = LocalDateTime.parse(scanner.nextLine().trim(), GameSession.F);
        System.out.print("Nhập giờ kết thúc (yyyy-MM-dd HH:mm): ");
        LocalDateTime end = LocalDateTime.parse(scanner.nextLine().trim(), GameSession.F);
        List<String> allLanes = laneService.getAllLanes().stream().map(Lane::getMaLane).toList();
        List<String> available = gameSessionService.findAvailableLanes(allLanes, start, end);
        if (available.isEmpty()) {
            System.out.println("Không có lane trống!");
        } else {
            System.out.println("Lane trống: " + String.join(", ", available));
        }
    }

    private static void hienThiTatCaPhienChoi() {
        List<GameSession> sessions = gameSessionService.list();
        if (sessions.isEmpty()) {
            System.out.println("Không có phiên chơi nào!");
        } else {
            System.out.println("Danh sách phiên chơi:");
            for (GameSession s : sessions) {
                System.out.println(s);
            }
        }
    }

    private static void manageShoeRental() {
        while (true) {
            System.out.println("=== QUẢN LÝ THUÊ GIÀY ===");
            System.out.println("1. Thêm thuê giày mới");
            System.out.println("2. Sửa thuê giày");
            System.out.println("3. Xóa thuê giày");
            System.out.println("4. Tìm kiếm thuê giày");
            System.out.println("5. Hiển thị tất cả thuê giày");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ đệm

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    themThueGiayMoi();
                    break;
                case 2:
                    suaThueGiay();
                    break;
                case 3:
                    xoaThueGiay();
                    break;
                case 4:
                    timKiemThueGiay();
                    break;
                case 5:
                    shoeRentalService.printAll();
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void themThueGiayMoi() {
        System.out.print("Nhập mã thuê: ");
        String maThue = scanner.nextLine().trim();
        if (maThue.isEmpty()) {
            System.out.println("Lỗi: Mã thuê không được để trống.");
            return;
        }

        System.out.print("Nhập mã phiên: ");
        String maPhien = scanner.nextLine().trim();
        if (maPhien.isEmpty()) {
            System.out.println("Lỗi: Mã phiên không được để trống.");
            return;
        }

        System.out.print("Nhập size giày: ");
        int size;
        try {
            size = Integer.parseInt(scanner.nextLine().trim());
            if (size <= 0) {
                System.out.println("Lỗi: Size phải là số dương.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Size phải là số hợp lệ.");
            return;
        }

        System.out.print("Nhập giá: ");
        double gia;
        try {
            gia = Double.parseDouble(scanner.nextLine().trim());
            if (gia <= 0) {
                System.out.println("Lỗi: Giá phải là số dương.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Giá phải là số hợp lệ.");
            return;
        }

        System.out.print("Nhập trạng thái (còn/trả/mất/hỏng): ");
        String trangThai = scanner.nextLine().trim().toLowerCase();
        if (trangThai.isEmpty() || !shoeRentalService.isValidStatus(trangThai)) {
            System.out.println("Lỗi: Trạng thái không được để trống và chỉ được là 'còn', 'trả', 'mất', hoặc 'hỏng'.");
            return;
        }

        ShoeRental rental = new ShoeRental(maThue, maPhien, size, gia, trangThai);
        shoeRentalService.add(rental);
        System.out.println("Thêm thuê giày thành công!");
    }

    private static void suaThueGiay() {
    System.out.print("Nhập mã thuê cần sửa: ");
    String maThue = scanner.nextLine().trim();
    ShoeRental existingRental = shoeRentalService.find(maThue);
    if (existingRental == null) {
        System.out.println("Không tìm thấy thuê giày!");
        return;
    }

    System.out.print("Nhập mã phiên mới (hoặc Enter để giữ nguyên): ");
    String maPhien = scanner.nextLine().trim();
    maPhien = maPhien.isEmpty() ? existingRental.getMaPhien() : maPhien;
    if (maPhien.isEmpty()) {
        System.out.println("Lỗi: Mã phiên không được để trống.");
        return;
    }

    System.out.print("Nhập size mới (hoặc Enter để giữ nguyên): ");
    String sizeInput = scanner.nextLine().trim();
    int size = sizeInput.isEmpty() ? existingRental.getSize() : Integer.parseInt(sizeInput);
    if (!sizeInput.isEmpty() && size <= 0) {
        System.out.println("Lỗi: Size phải là số dương.");
        return;
    }

    System.out.print("Nhập giá mới (hoặc Enter để giữ nguyên): ");
    String giaInput = scanner.nextLine().trim();
    double gia = giaInput.isEmpty() ? existingRental.getGia() : Double.parseDouble(giaInput);
    if (!giaInput.isEmpty() && gia <= 0) {
        System.out.println("Lỗi: Giá phải là số dương.");
        return;
    }

    System.out.print("Nhập trạng thái mới (còn/trả/mất/hỏng, hoặc Enter để giữ nguyên): ");
    String trangThai = scanner.nextLine().trim();
    trangThai = trangThai.isEmpty() ? existingRental.getTrangThai() : trangThai.toLowerCase();
    if (!trangThai.isEmpty() && !shoeRentalService.isValidStatus(trangThai)) {
        System.out.println("Lỗi: Trạng thái chỉ được là 'còn', 'trả', 'mất', hoặc 'hỏng'.");
        return;
    }

    ShoeRental updatedRental = new ShoeRental(maThue, maPhien, size, gia, trangThai);
    shoeRentalService.update(maThue, updatedRental);
    System.out.println("Sửa thuê giày thành công!");
}

    private static void xoaThueGiay() {
        System.out.print("Nhập mã thuê cần xóa: ");
        String maThue = scanner.nextLine().trim();
        ShoeRental existingRental = shoeRentalService.find(maThue);
        if (existingRental == null) {
            System.out.println("Không tìm thấy thuê giày!");
            return;
        }
        if (existingRental.getTrangThai().equalsIgnoreCase("còn")) {
            System.out.println("Lỗi: Không thể xóa giày đang ở trạng thái 'còn'.");
            return;
        }
        shoeRentalService.delete(maThue);
        System.out.println("Xóa thuê giày thành công!");
    }

    private static void timKiemThueGiay() {
        System.out.print("Nhập size, mã phiên, hoặc trạng thái (hoặc Enter để bỏ qua): ");
        String searchTerm = scanner.nextLine().trim();
        List<ShoeRental> results = new ArrayList<>();
        if (!searchTerm.isEmpty()) {
            results.addAll(shoeRentalService.searchByCriteria(searchTerm));
        }
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy thuê giày!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            for (ShoeRental r : results) {
                System.out.println(r);
            }
        }
    }

    private static void manageService() {
        while (true) {
            System.out.println("=== QUẢN LÝ DỊCH VỤ ===");
            System.out.println("1. Thêm dịch vụ mới");
            System.out.println("2. Sửa dịch vụ");
            System.out.println("3. Xóa dịch vụ");
            System.out.println("4. Tìm kiếm dịch vụ");
            System.out.println("5. Hiển thị tất cả dịch vụ");
            System.out.println("6. Tính tổng chi phí dịch vụ");
            System.out.println("0. Quay lại");
            System.out.print("Chọn chức năng: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa bộ đệm

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    themDichVuMoi();
                    break;
                case 2:
                    suaDichVu();
                    break;
                case 3:
                    xoaDichVu();
                    break;
                case 4:
                    timKiemDichVu();
                    break;
                case 5:
                    serviceEntityService.printAll();
                    break;
                case 6:
                    tinhTongChiPhi();
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void themDichVuMoi() {
        System.out.print("Nhập mã DV: ");
        String maDV = scanner.nextLine().trim();
        if (maDV.isEmpty()) {
            System.out.println("Lỗi: Mã DV không được để trống.");
            return;
        }

        System.out.print("Nhập mã phiên: ");
        String maPhien = scanner.nextLine().trim();
        if (maPhien.isEmpty()) {
            System.out.println("Lỗi: Mã phiên không được để trống.");
            return;
        }

        System.out.print("Nhập tên DV: ");
        String tenDV = scanner.nextLine().trim();
        if (tenDV.isEmpty()) {
            System.out.println("Lỗi: Tên DV không được để trống.");
            return;
        }

        System.out.print("Nhập số lượng: ");
        int soLuong;
        try {
            soLuong = Integer.parseInt(scanner.nextLine().trim());
            if (soLuong <= 0) {
                System.out.println("Lỗi: Số lượng phải là số dương.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Số lượng phải là số hợp lệ.");
            return;
        }

        System.out.print("Nhập đơn giá: ");
        double gia;
        try {
            gia = Double.parseDouble(scanner.nextLine().trim());
            if (gia <= 0) {
                System.out.println("Lỗi: Đơn giá phải là số dương.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Đơn giá phải là số hợp lệ.");
            return;
        }

        ServiceEntity service = new ServiceEntity(maDV, maPhien, tenDV, soLuong, gia);
        serviceEntityService.add(service);
        System.out.println("Thêm dịch vụ thành công!");
    }

   private static void suaDichVu() {
    System.out.print("Nhập mã DV cần sửa: ");
    String maDV = scanner.nextLine().trim();
    ServiceEntity existingService = serviceEntityService.find(maDV);
    if (existingService == null) {
        System.out.println("Không tìm thấy dịch vụ!");
        return;
    }

    System.out.print("Nhập mã phiên mới (hoặc Enter để giữ nguyên): ");
    String maPhien = scanner.nextLine().trim();
    maPhien = maPhien.isEmpty() ? existingService.getMaPhien() : maPhien;
    if (maPhien.isEmpty()) {
        System.out.println("Lỗi: Mã phiên không được để trống.");
        return;
    }

    System.out.print("Nhập tên DV mới (hoặc Enter để giữ nguyên): ");
    String tenDV = scanner.nextLine().trim();
    tenDV = tenDV.isEmpty() ? existingService.getTenDV() : tenDV;
    if (tenDV.isEmpty()) {
        System.out.println("Lỗi: Tên DV không được để trống.");
        return;
    }

    System.out.print("Nhập số lượng mới (hoặc Enter để giữ nguyên): ");
    String soLuongInput = scanner.nextLine().trim();
    int soLuong = soLuongInput.isEmpty() ? existingService.getSoLuong() : Integer.parseInt(soLuongInput);
    if (!soLuongInput.isEmpty() && soLuong <= 0) {
        System.out.println("Lỗi: Số lượng phải là số dương.");
        return;
    }

    System.out.print("Nhập đơn giá mới (hoặc Enter để giữ nguyên): ");
    String giaInput = scanner.nextLine().trim();
    double gia = giaInput.isEmpty() ? existingService.getGia() : Double.parseDouble(giaInput);
    if (!giaInput.isEmpty() && gia <= 0) {
        System.out.println("Lỗi: Đơn giá phải là số dương.");
        return;
    }

    serviceEntityService.update(maDV, maPhien, tenDV, soLuong, gia);
    System.out.println("Sửa dịch vụ thành công!");
}
    private static void xoaDichVu() {
        System.out.print("Nhập mã DV cần xóa: ");
        String maDV = scanner.nextLine().trim();
        if (serviceEntityService.delete(maDV)) {
            System.out.println("Xóa dịch vụ thành công!");
        } else {
            System.out.println("Không tìm thấy dịch vụ!");
        }
    }

    private static void timKiemDichVu() {
        System.out.print("Nhập tên DV hoặc mã phiên (hoặc Enter để bỏ qua): ");
        String searchTerm = scanner.nextLine().trim();
        List<ServiceEntity> results = new ArrayList<>();
        if (!searchTerm.isEmpty()) {
            results.addAll(serviceEntityService.searchByCriteria(searchTerm));
        }
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy dịch vụ!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            for (ServiceEntity s : results) {
                System.out.println(s);
            }
        }
    }

    private static void tinhTongChiPhi() {
        List<String> selectedSessionIds = new ArrayList<>();
        List<String> selectedRentalIds = new ArrayList<>();
        List<String> selectedServiceIds = new ArrayList<>();

        // Chọn phiên chơi
        List<GameSession> sessions = gameSessionService.list();
        if (!sessions.isEmpty()) {
            System.out.println("\n--- DANH SÁCH PHIÊN CHƠI CÓ SẴN ---");
            for (int i = 0; i < sessions.size(); i++) {
                GameSession s = sessions.get(i);
                System.out.printf("%d. Mã Phiên: %-6s | Mã Lane: %-6s | Thời gian: %s - %s%n",
                        i + 1, s.getMaPhien(), s.getMaLane(), s.getThoiGianBatDau(), s.getThoiGianKetThuc());
            }
            while (true) {
                System.out.print("Nhập số thứ tự phiên chơi để chọn (0 để bỏ qua): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Xóa bộ đệm
                if (choice == 0) break;
                if (choice < 1 || choice > sessions.size()) {
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn lại.");
                    continue;
                }
                String maPhien = sessions.get(choice - 1).getMaPhien();
                if (!selectedSessionIds.contains(maPhien)) {
                    selectedSessionIds.add(maPhien);
                    System.out.println("Đã chọn phiên: " + maPhien);
                } else {
                    System.out.println("Phiên đã được chọn trước đó!");
                }
            }
        } else {
            System.out.println("Không có phiên chơi nào để tính phí!");
        }

        // Chọn thuê giày
        List<ShoeRental> rentals = shoeRentalService.findAll();
        if (!rentals.isEmpty()) {
            System.out.println("\n--- DANH SÁCH THUÊ GIÀY CÓ SẴN ---");
            for (int i = 0; i < rentals.size(); i++) {
                ShoeRental r = rentals.get(i);
                System.out.printf("%d. Mã Thuê: %-6s | Mã Phiên: %-6s | Size: %-3d | Giá: %-8.2f | Trạng thái: %s%n",
                        i + 1, r.getMaThue(), r.getMaPhien(), r.getSize(), r.getGia(), r.getTrangThai());
            }
            while (true) {
                System.out.print("Nhập số thứ tự thuê giày để chọn (0 để bỏ qua): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Xóa bộ đệm
                if (choice == 0) break;
                if (choice < 1 || choice > rentals.size()) {
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn lại.");
                    continue;
                }
                String maThue = rentals.get(choice - 1).getMaThue();
                if (!selectedRentalIds.contains(maThue)) {
                    selectedRentalIds.add(maThue);
                    System.out.println("Đã chọn thuê giày: " + maThue);
                } else {
                    System.out.println("Thuê giày đã được chọn trước đó!");
                }
            }
        } else {
            System.out.println("Không có thuê giày nào để tính phí!");
        }

        // Chọn dịch vụ
        List<ServiceEntity> services = serviceEntityService.findAll();
        if (!services.isEmpty()) {
            System.out.println("\n--- DANH SÁCH DỊCH VỤ CÓ SẴN ---");
            for (int i = 0; i < services.size(); i++) {
                ServiceEntity s = services.get(i);
                System.out.printf("%d. Mã DV: %-6s | Mã Phiên: %-6s | Tên: %-15s | Qty: %-3d | Giá: %-8.2f%n",
                        i + 1, s.getMaDV(), s.getMaPhien(), s.getTenDV(), s.getSoLuong(), s.getGia());
            }
            while (true) {
                System.out.print("Nhập số thứ tự dịch vụ để chọn (0 để bỏ qua): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Xóa bộ đệm
                if (choice == 0) break;
                if (choice < 1 || choice > services.size()) {
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn lại.");
                    continue;
                }
                String maDV = services.get(choice - 1).getMaDV();
                if (!selectedServiceIds.contains(maDV)) {
                    selectedServiceIds.add(maDV);
                    System.out.println("Đã chọn dịch vụ: " + maDV);
                } else {
                    System.out.println("Dịch vụ đã được chọn trước đó!");
                }
            }
        } else {
            System.out.println("Không có dịch vụ nào để tính phí!");
        }

        // Tính tổng chi phí
        double totalCost = ServiceEntityService.calculateTotalCost(selectedSessionIds, selectedRentalIds, selectedServiceIds);
        System.out.printf("\nTổng chi phí: %.2f VNĐ%n", totalCost);
    }
}