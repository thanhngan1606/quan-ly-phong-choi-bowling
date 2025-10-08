import java.util.Scanner;

public class InputExampleRecursion {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    // Phương thức đệ quy để lấy tên hợp lệ
    public static String layTenKhachHang() {
        
        System.out.print("Nhập tên KH: ");
        String ten = scanner.nextLine().trim();

        // Kiểm tra tính hợp lệ
        if (ten.matches("[a-zA-Z\\s]+")) {
            // Trường hợp 1: Hợp lệ -> Trả về kết quả
            return ten;
        } else {
            // Trường hợp 2: KHÔNG hợp lệ -> In lỗi và gọi lại chính phương thức
            System.out.println("Lỗi: Tên chỉ được chứa chữ cái và khoảng trắng. Vui lòng nhập lại.");
            return layTenKhachHang(); // TỰ GỌI LẠI
        }
    }

    public static void main(String[] args) {
        String tenHopLe = layTenKhachHang();
        System.out.println("Tên khách hàng hợp lệ: " + tenHopLe);
    }
}
