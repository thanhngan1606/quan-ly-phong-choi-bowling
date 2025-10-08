package service;

import models.ServiceEntity;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ServiceEntityService {
    private Path filePath;
    private List<ServiceEntity> services = new ArrayList<>();

    public ServiceEntityService(String filePathStr) {
        this.filePath = Paths.get(filePathStr);
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        services.clear();
        File file = new File(filePath.toString());
        if (!file.exists()) {
            System.out.println("File dữ liệu không tồn tại, khởi tạo danh sách trống.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toString()))) {
            services = (List<ServiceEntity>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toString()))) {
            oos.writeObject(services);
            System.out.println("Data saved to file: " + filePath);
        } catch (IOException e) {
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

    public List<ServiceEntity> findAll() {
        return new ArrayList<>(services);
    }

    public List<ServiceEntity> searchByCriteria(String searchTerm) {
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

    public List<ServiceEntity> findByName(String keyword) {
        List<ServiceEntity> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return result;

        String keyLower = keyword.toLowerCase();
        for (ServiceEntity s : services) {
            if (s.getTenDV() != null && s.getTenDV().toLowerCase().contains(keyLower)) {
                result.add(s);
            }
        }
        return result;
    }

    public void printSearchResult(List<ServiceEntity> list) {
        if (list.isEmpty()) {
            System.out.println("No matching service found.");
        } else {
            System.out.println("\n--- SEARCH RESULTS ---");
            for (ServiceEntity s : list) {
                System.out.printf("%-6s | %-6s | %-15s | Qty: %-3d | Price: %-8.2f | Total: %.2f%n",
                        s.getMaDV(), s.getMaPhien(), s.getTenDV(),
                        s.getSoLuong(), s.getGia(), s.getThanhTien());
            }
        }
    }
}
