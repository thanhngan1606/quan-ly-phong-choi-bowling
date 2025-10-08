package service;

import models.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ServiceEntityService {
    private static LaneService laneService = new LaneService();
    private static GameSessionService gameSessionService = new GameSessionService(
        java.nio.file.Paths.get("C://dev//quan-ly-phong-choi-bowling//data//src//gamesession.txt"),
        laneId -> laneService.findLanes(laneId, null).stream().findFirst()
                .map(Lane::getGiaGio).orElse(0.0)
    );
    private static ShoeRentalService shoeRentalService = new ShoeRentalService();
    private java.nio.file.Path filePath;
    private java.util.List<ServiceEntity> services = new java.util.ArrayList<>();

    public ServiceEntityService(String filePathStr) {
        this.filePath = java.nio.file.Paths.get(filePathStr);
        loadFromFile();
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        services.clear();
        java.io.File file = new java.io.File(filePath.toString());
        if (!file.exists()) {
            System.out.println("File dữ liệu không tồn tại, khởi tạo danh sách trống.");
            return;
        }
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(filePath.toString()))) {
            services = (java.util.List<ServiceEntity>) ois.readObject();
        } catch (java.io.IOException | java.lang.ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(filePath.toString()))) {
            oos.writeObject(services);
            System.out.println("Data saved to file: " + filePath);
        } catch (java.io.IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public void add(ServiceEntity service) {
        services.add(service);
        saveToFile();
        System.out.println("Thêm dịch vụ thành công!");
    }

    public boolean delete(String ma) {
        ServiceEntity service = find(ma);
        if (service != null) {
            services.remove(service);
            saveToFile();
            return true;
        }
        return false;
    }

    public ServiceEntity find(String ma) {
        if (ma == null) return null;
        for (ServiceEntity s : services) {
            if (s.getMaDV().equals(ma)) {
                return s;
            }
        }
        return null;
    }

    public java.util.List<ServiceEntity> findAll() {
        return new java.util.ArrayList<>(services);
    }

    public java.util.List<ServiceEntity> searchByCriteria(String searchTerm) {
        return services.stream()
                .filter(s -> s.getTenDV().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        s.getMaPhien().contains(searchTerm))
                .toList();
    }

    public void printAll() {
        if (services.isEmpty()) {
            System.out.println("No service data available.");
        } else {
            System.out.println("\n--- SERVICE LIST ---");
            for (ServiceEntity s : services) {
                System.out.printf("%-6s | %-6s | %-15s | Qty: %-3d | Price: %-8.2f | Total: %.2f%n",
                        s.getMaDV(), s.getMaPhien(), s.getTenDV(),
                        s.getSoLuong(), s.getGia(), s.getThanhTien());
            }
        }
    }

    public void update(String ma, String maPhien, String tenDV, int soLuong, double gia) {
        ServiceEntity s = find(ma);
        if (s != null) {
            s.setMaPhien(maPhien);
            s.setTenDV(tenDV);
            s.setSoLuong(soLuong);
            s.setGia(gia);
            saveToFile();
            System.out.println("Service updated with ID " + ma);
        } else {
            System.out.println("Service with ID " + ma + " not found");
        }
    }

    public static double calculateTotalCost(List<String> sessionIds, List<String> rentalIds, List<String> serviceIds) {
        double totalCost = 0.0;

        // Tính chi phí chơi bowling
        for (String maPhien : sessionIds) {
            GameSession session = gameSessionService.get(maPhien).orElse(null);
            if (session != null) {
                String maLane = session.getMaLane();
                Lane lane = laneService.findLanes(maLane, null).stream().findFirst().orElse(null);
                if (lane != null) {
                    long hours = ChronoUnit.HOURS.between(session.getThoiGianBatDau(), session.getThoiGianKetThuc());
                    if (hours < 0) hours = 0; // Xử lý trường hợp thời gian không hợp lệ
                    double cost = lane.getGiaGio() * hours;
                    totalCost += cost;
                    System.out.printf("Chi phí chơi lane %s: %.2f VNĐ (%.2f x %d giờ)%n",
                            maLane, cost, lane.getGiaGio(), hours);
                }
            }
        }

        // Tính chi phí thuê giày
        for (String maThue : rentalIds) {
            ShoeRental rental = shoeRentalService.find(maThue);
            if (rental != null && rental.getTrangThai().equalsIgnoreCase("còn")) {
                totalCost += rental.getGia();
                System.out.printf("Chi phí thuê giày %s: %.2f VNĐ%n", maThue, rental.getGia());
            }
        }

        // Tính chi phí dịch vụ
        ServiceEntityService serviceEntityService = new ServiceEntityService("C://dev//quan-ly-phong-choi-bowling//data//src//service.txt");
        for (String maDV : serviceIds) {
            ServiceEntity service = serviceEntityService.find(maDV);
            if (service != null) {
                double serviceCost = service.getSoLuong() * service.getGia();
                totalCost += serviceCost;
                System.out.printf("Chi phí dịch vụ %s: %.2f VNĐ (%d x %.2f)%n",
                        maDV, serviceCost, service.getSoLuong(), service.getGia());
            }
        }

        return Math.round(totalCost * 100.0) / 100.0; // Làm tròn 2 chữ số thập phân
    }
}
