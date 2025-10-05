import models.*;
import service.*;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Khởi tạo service với file dữ liệu
        

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== HỆ THỐNG QUẢN LÝ BOWLING ===");
            System.out.println("1. Quản lý khách hàng");
            System.out.println("2. Quản lý đường bowling (Lane)");
            System.out.println("3. Quản lý dịch vụ (ServiceEntity)");
            System.out.println("4. Quản lý thuê giày (ShoeRental)");
            System.out.println("5. Quản lý phiên chơi (GameSession)");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> customerMenu(customerService, sc);
                case 2 -> laneMenu(laneService, sc);
                case 3 -> serviceEntityMenu(serviceEntityService, sc);
                case 4 -> shoeRentalMenu(shoeRentalService, sc);
                case 5 -> gameSessionMenu(gameSessionService, sc);
                case 0 -> System.out.println("Thoát chương trình!");
                default -> System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);
    }

    // ==== MENU KHÁCH HÀNG ====
    private static void customerMenu(CustomerService service, Scanner sc) {
        System.out.println("\n--- QUẢN LÝ KHÁCH HÀNG ---");
        System.out.println("1. Thêm / Sửa");
        System.out.println("2. Xóa");
        System.out.println("3. Tìm kiếm");
        System.out.println("4. Danh sách");
        System.out.print("Chọn: ");
        int c = Integer.parseInt(sc.nextLine());

        switch (c) {
            case 1 -> { /* TODO: gọi service.createOrUpdate() */ }
            case 2 -> { /* TODO: gọi service.delete() */ }
            case 3 -> { /* TODO: gọi service.get() */ }
            case 4 -> { /* TODO: gọi service.list() */ }
        }
    }

    // ==== MENU LANE ====
    private static void laneMenu(LaneService service, Scanner sc) {
        System.out.println("\n--- QUẢN LÝ LANE ---");
        // TODO: tương tự customerMenu
    }

    // ==== MENU SERVICE ENTITY ====
    private static void serviceEntityMenu(ServiceEntityService service, Scanner sc) {
        System.out.println("\n--- QUẢN LÝ DỊCH VỤ ---");
        // TODO: tương tự customerMenu
    }

    // ==== MENU SHOE RENTAL ====
    private static void shoeRentalMenu(ShoeRentalService service, Scanner sc) {
        System.out.println("\n--- QUẢN LÝ THUÊ GIÀY ---");
        // TODO: tương tự customerMenu
    }

    // ==== MENU GAME SESSION ====
    private static void gameSessionMenu(GameSessionService service, Scanner sc) {
        System.out.println("\n--- QUẢN LÝ PHIÊN CHƠI ---");
        // TODO: tương tự customerMenu
    }
}
