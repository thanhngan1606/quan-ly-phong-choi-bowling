
import models.ShoeRental;
import service.ShoeRentalService;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    // Khai báo Service và Scanner là static final để sử dụng xuyên suốt chương trình
    private static final ShoeRentalService service = new ShoeRentalService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🌟 Khởi động ứng dụng Quản lý Thuê Giày...");

        int luaChon;
        do {
            hienThiMenu();
            System.out.print("▶️ Nhập lựa chọn của bạn (0-5): ");

            // Xử lý lỗi nếu người dùng nhập không phải là số
            try {
                // Sử dụng nextLine() để tránh lỗi trôi dòng (buffer issue)
                luaChon = Integer.parseInt(scanner.nextLine());
                xuLyLuaChon(luaChon);
            } catch (NumberFormatException e) {
                System.out.println("❌ LỖI: Lựa chọn không hợp lệ. Vui lòng nhập một số nguyên từ 0 đến 5.");
                luaChon = -1;
            }
        } while (luaChon != 0);
    }

    public static void hienThiMenu() {
        System.out.println("\n===== ỨNG DỤNG QUẢN LÝ THUÊ GIÀY =====");
        System.out.println("1. Thêm giày mới");
        System.out.println("2. Hiển thị danh sách giày");
        System.out.println("3. Cập nhật thông tin giày");
        System.out.println("4. Xóa giày");
        System.out.println("5. Tìm kiếm giày (Chưa triển khai trong Service)");
        System.out.println("0. Thoát chương trình và lưu dữ liệu");
        System.out.println("=======================================");
    }

    public static void xuLyLuaChon(int luaChon) {
        switch (luaChon) {
            case 1:
                themGiayMoi();
                break;
            case 2:
                service.printAll(); // Gọi trực tiếp hàm in từ Service
                break;
            case 3:
                capNhatThongTinGiay();
                break;
            case 4:
                xoaThueGiay();
                break;
            case 5:
                timKiemGiay(); // Sẽ cần bổ sung hàm này vào Service
                break;
            case 0:
                // service.saveToFile() đã được gọi sau mỗi thao tác, nhưng vẫn thông báo thoát
                System.out.println("👋 Chương trình kết thúc. Dữ liệu đã được lưu!");
                break;
            default:
                System.out.println("❓ Lựa chọn ngoài phạm vi. Vui lòng chọn lại.");
        }
    }

    // --- CHỨC NĂNG 1: THÊM MỚI ---
    public static void themGiayMoi() {
        System.out.println("\n--- THÊM GIÀY MỚI ---");
        try {
            System.out.print("Nhập Mã Thuê (RTxxx): ");
            String maThue = scanner.nextLine();

            // Kiểm tra tính hợp lệ của Mã Thuê (ví dụ: không được rỗng)
            if (maThue.trim().isEmpty()) {
                System.out.println("❌ Lỗi: Mã thuê không được để trống.");
                return;
            }

            System.out.print("Nhập Mã Phiên (Pxx): ");
            String maPhien = scanner.nextLine();

            System.out.print("Nhập Size giày (số nguyên): ");
            int size = Integer.parseInt(scanner.nextLine());

            System.out.print("Nhập Giá thuê (số thập phân): ");
            double gia = Double.parseDouble(scanner.nextLine());

            System.out.print("Nhập Trạng thái (Sẵn sàng/Đang thuê/Hỏng): ");
            String trangThai = scanner.nextLine();

            // Tạo đối tượng dữ liệu tại Main
            ShoeRental newShoe = new ShoeRental(maThue, maPhien, size, gia, trangThai);

            // GỌI HÀM SERVICE để thực hiện logic nghiệp vụ (kiểm tra trùng mã, thêm vào list, lưu file)
            service.add(newShoe);

        } catch (NumberFormatException e) {
            System.out.println("❌ LỖI: Dữ liệu Size hoặc Giá không hợp lệ. Vui lòng nhập đúng kiểu số.");
        }
    }

    // --- CHỨC NĂNG 3: CẬP NHẬT ---
    public static void capNhatThongTinGiay() {
        System.out.println("\n--- CẬP NHẬT THÔNG TIN GIÀY ---");
        System.out.print("Nhập Mã Thuê của giày cần sửa: ");
        String maCanSua = scanner.nextLine();

        ShoeRental giayCanSua = service.find(maCanSua);

        if (giayCanSua != null) {
            try {
                System.out.println("Thông tin cũ: " + giayCanSua.toString());
                System.out.println("--- Bỏ trống nếu không muốn thay đổi ---");

                // Nhận các giá trị mới từ người dùng (Vẫn sử dụng thông tin cũ làm mặc định)

                System.out.print("Nhập Mã Phiên mới (" + giayCanSua.getMaPhien() + "): ");
                String newMaPhien = scanner.nextLine();
                if (newMaPhien.isEmpty()) newMaPhien = giayCanSua.getMaPhien();

                int newSize = giayCanSua.getSize();
                System.out.print("Nhập Size mới (" + newSize + "): ");
                String sizeStr = scanner.nextLine();
                if (!sizeStr.isEmpty()) {
                    newSize = Integer.parseInt(sizeStr);
                }

                double newGia = giayCanSua.getGia();
                System.out.print("Nhập Giá mới (" + newGia + "): ");
                String giaStr = scanner.nextLine();
                if (!giaStr.isEmpty()) {
                    newGia = Double.parseDouble(giaStr);
                }

                System.out.print("Nhập Trạng thái mới (" + giayCanSua.getTrangThai() + "): ");
                String newTrangThai = scanner.nextLine();
                if (newTrangThai.isEmpty()) newTrangThai = giayCanSua.getTrangThai();

                // Tạo đối tượng tạm thời chứa dữ liệu mới để chuyển cho Service
                ShoeRental newShoeData = new ShoeRental(
                        maCanSua, newMaPhien, newSize, newGia, newTrangThai
                );

                // GỌI HÀM SERVICE để cập nhật
                service.update(maCanSua, newShoeData);

            } catch (NumberFormatException e) {
                System.out.println("❌ LỖI: Dữ liệu Size hoặc Giá mới không hợp lệ. Cập nhật thất bại.");
            }
        } else {
            System.out.println("❌ LỖI: Không tìm thấy giày với Mã Thuê: " + maCanSua);
        }
    }

    // --- CHỨC NĂNG 4: XÓA ---
    public static void xoaThueGiay() {
        System.out.println("\n--- XÓA THUÊ GIÀY ---");
        System.out.print("Nhập Mã Thuê của giày cần xóa: ");
        String maCanXoa = scanner.nextLine();

        service.delete(maCanXoa);
    }

    // --- CHỨC NĂNG 5: TÌM KIẾM ---
    public static void timKiemGiay() {
        System.out.println("\n--- TÌM KIẾM GIÀY ---");
        System.out.print("Nhập từ khóa (Mã Thuê, Mã Phiên, Size, Trạng thái): ");
        String tuKhoa = scanner.nextLine();

    }
}
