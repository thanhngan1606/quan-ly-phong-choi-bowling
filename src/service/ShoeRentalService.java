package service;

import models.ShoeRental;
import java.io.*;
import java.util.*;

public class ShoeRentalService {
    private static final String FILE_NAME = "C://dev//quan-ly-phong-choi-bowling//data//src//shoe_rentals.txt";
    private List<ShoeRental> rentals = new ArrayList<>();

    public ShoeRentalService() {
        loadFromFile();
    }

    public void add(ShoeRental r) {
        if (find(r.getMaThue()) != null) {
            System.out.println("Lỗi: Mã thuê giày " + r.getMaThue() + " đã tồn tại.");
            return;
        }
        rentals.add(r);
        saveToFile();
        System.out.println("Thêm thuê giày " + r.getMaThue() + " thành công.");
    }

    public ShoeRental find(String ma) {
        for (ShoeRental r : rentals) {
            if (r.getMaThue().equalsIgnoreCase(ma)) {
                return r;
            }
        }
        return null;
    }

    public List<ShoeRental> findAll() {
        return new ArrayList<>(rentals);
    }

    public void printAll() {
        if (rentals.isEmpty()) {
            System.out.println("Danh sách thuê giày hiện đang trống.");
            return;
        }
        System.out.println("\n--- DANH SÁCH THUÊ GIÀY ---");
        for (ShoeRental r : rentals) {
            System.out.println(r.toString());
        }
        System.out.println("--------------------------------");
    }

    public void update(String ma, ShoeRental newR) {
        ShoeRental oldR = find(ma);
        if (oldR != null) {
            if (!isValidStatus(newR.getTrangThai())) {
                System.out.println("Lỗi: Trạng thái không hợp lệ!");
                return;
            }
            oldR.setMaPhien(newR.getMaPhien());
            oldR.setSize(newR.getSize());
            oldR.setGia(newR.getGia());
            oldR.setTrangThai(newR.getTrangThai());
            saveToFile();
            System.out.println("Cập nhật thuê giày " + ma + " thành công.");
        } else {
            System.out.println("Lỗi: Không tìm thấy mã thuê giày: " + ma);
        }
    }

    public void delete(String ma) {
        ShoeRental r = find(ma);
        if (r != null) {
            if (r.getTrangThai().equalsIgnoreCase("Đang thuê")) {
                System.out.println("Lỗi: Không thể xóa giày đang ở trạng thái 'Đang thuê'.");
                return;
            }
            rentals.remove(r);
            saveToFile();
            System.out.println("Xóa thuê giày " + ma + " thành công.");
        } else {
            System.out.println("Lỗi: Không tìm thấy mã thuê giày: " + ma);
        }
    }

    public List<ShoeRental> searchByCriteria(String searchTerm) {
        return rentals.stream()
                .filter(r -> String.valueOf(r.getSize()).contains(searchTerm) ||
                        r.getMaPhien().contains(searchTerm) ||
                        r.getTrangThai().contains(searchTerm))
                .toList();
    }

    public boolean isValidStatus(String status) {
        return status != null && (status.equalsIgnoreCase("còn") ||
                status.equalsIgnoreCase("trả") ||
                status.equalsIgnoreCase("mất") ||
                status.equalsIgnoreCase("hỏng"));
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        rentals.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("File " + FILE_NAME + " không tồn tại. Tạo danh sách trống.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            rentals = (List<ShoeRental>) ois.readObject();
            System.out.println("Tải " + rentals.size() + " mục thuê giày từ " + FILE_NAME + " thành công.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(rentals);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }
}