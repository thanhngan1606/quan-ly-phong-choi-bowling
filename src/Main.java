import java.time.LocalDateTime;
import java.util.*;
import models.*;
import service.*;

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
        if (!tenLane.matches("[a-zA-Z\\s]+")) {
            System.out.println("Lỗi: Tên lane chỉ được chứa chữ cái và khoảng trắng.");
            return;
        }

        System.out.print("Nhập trạng thái (trống/đang chơi/bảo trì): ");
        String trangThai = scanner.nextLine().trim().toLowerCase();
        if (!trangThai.equals("trống") && !trangThai.equals("đang chơi") && !trangThai.equals("bảo trì")) {
            System.out.println("Lỗi: Trạng thái chỉ được là 'trống', 'đang chơi', hoặc 'bảo trì'.");
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
        String maLane = scanner.nextLine();
        List<Lane> lanes = laneService.findLanes(maLane, null);
        if (lanes.isEmpty()) {
            System.out.println("Không tìm thấy lane!");
            return;
        }
        Lane existingLane = lanes.get(0);

        System.out.print("Nhập tên mới (hoặc Enter để giữ nguyên): ");
        String tenLane = scanner.nextLine().isEmpty() ? existingLane.getTenLane() : scanner.nextLine();
        System.out.print("Nhập giá mới (hoặc Enter để giữ nguyên): ");
        double gia = scanner.nextLine().isEmpty() ? existingLane.getGiaGio() : Double.parseDouble(scanner.nextLine());
        System.out.print("Nhập trạng thái mới (hoặc Enter để giữ nguyên): ");
        String trangThai = scanner.nextLine().isEmpty() ? existingLane.getTrangThai() : scanner.nextLine();
        System.out.print("Nhập thông tin bảo trì mới (hoặc Enter để giữ nguyên): ");
        String baoTri = scanner.nextLine().isEmpty() ? existingLane.getBaoTri() : scanner.nextLine();

        Lane updatedLane = new Lane(maLane, tenLane, trangThai, gia, baoTri);
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
        System.out.print("Nhập mã hoặc tên lane (hoặc Enter để bỏ qua): ");
        String searchTerm = scanner.nextLine();
        System.out.print("Nhập trạng thái (trống/đang chơi/bảo trì, hoặc Enter để bỏ qua): ");
        String trangThai = scanner.nextLine();

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
        if (!ten.matches("[a-zA-Z\\s]+")) {
            System.out.println("Lỗi: Tên chỉ được chứa chữ cái và khoảng trắng.");
            return;
        }

        System.out.print("Nhập SĐT: ");
        String sdt = scanner.nextLine().trim();
        if (!sdt.matches("\\d{10}")) {
            System.out.println("Lỗi: SĐT phải là 10 chữ số.");
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
        String maKH = scanner.nextLine();
        Customer existingCustomer = customerService.find(maKH);
        if (existingCustomer == null) {
            System.out.println("Không tìm thấy khách hàng!");
            return;
        }

        System.out.print("Nhập tên mới (hoặc Enter để giữ nguyên): ");
        String ten = scanner.nextLine().isEmpty() ? existingCustomer.getTen() : scanner.nextLine();
        System.out.print("Nhập SĐT mới (hoặc Enter để giữ nguyên): ");
        String sdt = scanner.nextLine().isEmpty() ? existingCustomer.getSdt() : scanner.nextLine();
        System.out.print("Nhập VIP mới (true/false, hoặc Enter để giữ nguyên): ");
        boolean vip = scanner.nextLine().isEmpty() ? existingCustomer.isVip() : Boolean.parseBoolean(scanner.nextLine());
        System.out.print("Nhập điểm thưởng mới (hoặc Enter để giữ nguyên): ");
        int diemThuong = scanner.nextLine().isEmpty() ? existingCustomer.getDiemThuong() : Integer.parseInt(scanner.nextLine());

        Customer updatedCustomer = new Customer(maKH, ten, sdt, vip, diemThuong);
        customerService.update(maKH, updatedCustomer);
        System.out.println("Sửa khách hàng thành công!");
    }

    private static void xoaKhachHang() {
        System.out.print("Nhập mã KH cần xóa: ");
        String maKH = scanner.nextLine();
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
        System.out.print("Nhập mã KH: ");
        String maKH = scanner.nextLine().trim();
        System.out.print("Nhập mã lane: ");
        String maLane = scanner.nextLine().trim();
        System.out.print("Nhập giờ bắt đầu (yyyy-MM-dd HH:mm): ");
        LocalDateTime batDau = LocalDateTime.parse(scanner.nextLine().trim(), GameSession.F);
        System.out.print("Nhập giờ kết thúc (yyyy-MM-dd HH:mm): ");
        LocalDateTime ketThuc = LocalDateTime.parse(scanner.nextLine().trim(), GameSession.F);

        GameSession session = new GameSession(maPhien, maKH, maLane, batDau, ketThuc, 0.0);
        gameSessionService.createOrUpdate(session);
        System.out.println("Thêm phiên chơi thành công!");
    }

    private static void suaPhienChoi() {
        System.out.print("Nhập mã phiên cần sửa: ");
        String maPhien = scanner.nextLine();
        GameSession existingSession = gameSessionService.get(maPhien).orElse(null);
        if (existingSession == null) {
            System.out.println("Không tìm thấy phiên chơi!");
            return;
        }

        System.out.print("Nhập mã KH mới (hoặc Enter để giữ nguyên): ");
        String maKH = scanner.nextLine().isEmpty() ? existingSession.getMaKH() : scanner.nextLine();
        System.out.print("Nhập mã lane mới (hoặc Enter để giữ nguyên): ");
        String maLane = scanner.nextLine().isEmpty() ? existingSession.getMaLane() : scanner.nextLine();
        System.out.print("Nhập giờ bắt đầu mới (hoặc Enter để giữ nguyên, yyyy-MM-dd HH:mm): ");
        LocalDateTime batDau = scanner.nextLine().isEmpty() ? existingSession.getThoiGianBatDau() :
                LocalDateTime.parse(scanner.nextLine().trim(), GameSession.F);
        System.out.print("Nhập giờ kết thúc mới (hoặc Enter để giữ nguyên, yyyy-MM-dd HH:mm): ");
        LocalDateTime ketThuc = scanner.nextLine().isEmpty() ? existingSession.getThoiGianKetThuc() :
                LocalDateTime.parse(scanner.nextLine().trim(), GameSession.F);

        GameSession updatedSession = new GameSession(maPhien, maKH, maLane, batDau, ketThuc, 0.0);
        gameSessionService.createOrUpdate(updatedSession);
        System.out.println("Sửa phiên chơi thành công!");
    }

    private static void xoaPhienChoi() {
        System.out.print("Nhập mã phiên cần xóa: ");
        String maPhien = scanner.nextLine();
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
        if (!shoeRentalService.isValidStatus(trangThai)) {
            System.out.println("Lỗi: Trạng thái chỉ được là 'còn', 'trả', 'mất', hoặc 'hỏng'.");
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
        String maPhien = scanner.nextLine().trim().isEmpty() ? existingRental.getMaPhien() : scanner.nextLine().trim();
        System.out.print("Nhập size mới (hoặc Enter để giữ nguyên): ");
        int size = scanner.nextLine().trim().isEmpty() ? existingRental.getSize() : Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Nhập giá mới (hoặc Enter để giữ nguyên): ");
        double gia = scanner.nextLine().trim().isEmpty() ? existingRental.getGia() : Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Nhập trạng thái mới (còn/trả/mất/hỏng, hoặc Enter để giữ nguyên): ");
        String trangThai = scanner.nextLine().trim().isEmpty() ? existingRental.getTrangThai() : scanner.nextLine().trim().toLowerCase();
        if (!scanner.nextLine().trim().isEmpty() && !shoeRentalService.isValidStatus(trangThai)) {
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
        String maPhien = scanner.nextLine().trim().isEmpty() ? existingService.getMaPhien() : scanner.nextLine().trim();
        System.out.print("Nhập tên DV mới (hoặc Enter để giữ nguyên): ");
        String tenDV = scanner.nextLine().trim().isEmpty() ? existingService.getTenDV() : scanner.nextLine().trim();
        System.out.print("Nhập số lượng mới (hoặc Enter để giữ nguyên): ");
        int soLuong = scanner.nextLine().trim().isEmpty() ? existingService.getSoLuong() : Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Nhập đơn giá mới (hoặc Enter để giữ nguyên): ");
        double gia = scanner.nextLine().trim().isEmpty() ? existingService.getGia() : Double.parseDouble(scanner.nextLine().trim());

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
}