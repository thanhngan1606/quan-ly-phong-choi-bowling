package service;

import models.ShoeRental;
import java.io.*;
import java.util.*;

public class ShoeRentalService {

    private static final String FILE_NAME = "shoe_rentals.txt";
    private List<ShoeRental> rentals = new ArrayList<>();

    public ShoeRentalService() {
        loadFromFile();
    }

    public void add(ShoeRental r) {
        if (find(r.getMaThue()) != null) {
            System.out.println(" Lỗi: Mã thuê giày " + r.getMaThue() + " đã tồn tại.");
            return;
        }
        rentals.add(r);
        saveToFile();
        System.out.println(" Thêm thuê giày " + r.getMaThue() + " thành công.");
    }

    public ShoeRental find(String ma) {
        for (ShoeRental r : rentals) {
            if (r.getMaThue().equalsIgnoreCase(ma)) {
                return r;
            }
        }
        return null;
    }

    public void printAll() {
        if (rentals.isEmpty()) {
            System.out.println(" Danh sách thuê giày hiện đang trống.");
            return;
        }
        System.out.println("\n--- DANH SÁCH THUÊ GIÀY ---");
        for (ShoeRental r : rentals) {
            // Sử dụng phương thức toString() của lớp ShoeRental
            System.out.println(r.toString());
        }
        System.out.println("--------------------------------");
    }

    public void update(String ma, ShoeRental newR) {
        ShoeRental oldR = find(ma);
        if (oldR != null) {
            oldR.setMaPhien(newR.getMaPhien());
            oldR.setSize(newR.getSize());
            oldR.setGia(newR.getGia());
            oldR.setTrangThai(newR.getTrangThai());

            saveToFile();
            System.out.println(" Cập nhật thuê giày " + ma + " thành công.");
        } else {
            System.out.println(" Lỗi: Không tìm thấy mã thuê giày: " + ma);
        }
    }

    public void delete(String ma) {
        ShoeRental r = find(ma);
        if (r != null) {
            // Thêm logic kiểm tra: Không cho xóa giày đang thuê (ví dụ ràng buộc nghiệp vụ)
            if (r.getTrangThai().equalsIgnoreCase("Đang thuê")) {
                System.out.println(" Lỗi: Không thể xóa giày đang ở trạng thái 'Đang thuê'.");
                return;
            }

            rentals.remove(r);
            saveToFile();
            System.out.println(" Xóa thuê giày " + ma + " thành công.");
        } else {
            System.out.println(" Lỗi: Không tìm thấy mã thuê giày để xóa: " + ma);
        }
    }

    /**
     * Tìm kiếm giày theo từ khóa (Mã Thuê, Mã Phiên, Size, Giá, Trạng Thái).
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách các ShoeRental khớp với từ khóa.
     */
    public List<ShoeRental> search(String keyword) {
        List<ShoeRental> results = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return rentals; // Trả về toàn bộ danh sách nếu từ khóa rỗng
        }

        String lowerCaseKeyword = keyword.trim().toLowerCase();

        for (ShoeRental r : rentals) {
            // Kiểm tra khớp từ khóa trong tất cả các trường
            if (r.getMaThue().toLowerCase().contains(lowerCaseKeyword) ||
                    r.getMaPhien().toLowerCase().contains(lowerCaseKeyword) ||
                    String.valueOf(r.getSize()).contains(lowerCaseKeyword) ||
                    String.valueOf(r.getGia()).contains(lowerCaseKeyword) ||
                    r.getTrangThai().toLowerCase().contains(lowerCaseKeyword)) {

                results.add(r);
            }
        }
        return results;
    }

    public void loadFromFile() {
        rentals.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    try {
                        String maThue = parts[0].trim();
                        String maPhien = parts[1].trim();
                        int size = Integer.parseInt(parts[2].trim());
                        double gia = Double.parseDouble(parts[3].trim());
                        String trangThai = parts[4].trim();

                        rentals.add(new ShoeRental(maThue, maPhien, size, gia, trangThai));
                    } catch (NumberFormatException e) {
                        System.err.println("Cảnh báo: Lỗi định dạng số trong file: " + line);
                    }
                }
            }
            System.out.println(" Tải " + rentals.size() + " mục thuê giày từ " + FILE_NAME + " thành công.");
        } catch (FileNotFoundException e) {
            System.out.println(" File " + FILE_NAME + " không tồn tại. Tạo danh sách trống.");
        } catch (IOException e) {
            System.err.println(" Lỗi khi đọc file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (ShoeRental r : rentals) {
                // Lưu ý: Định dạng giá %.0f có thể làm mất phần thập phân nếu giá là 20.5.
                // Tôi giữ nguyên định dạng của bạn để không thay đổi cấu trúc file,
                // nhưng nếu cần độ chính xác, nên dùng %.2f
                String line = String.format("%s,%s,%d,%.0f,%s",
                        r.getMaThue(),
                        r.getMaPhien(),
                        r.getSize(),
                        r.getGia(),
                        r.getTrangThai());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println(" Lỗi khi ghi file: " + e.getMessage());
        }
    }
}
