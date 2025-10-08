package service;

import java.io.*;
import java.util.*;
import models.Customer;

public class CustomerService {
    private static Scanner scanner = new Scanner(System.in);
    private static final String DATA_FILE = "customers.txt";

    interface Validator {
        boolean validate(String value);
    }

    public static String nhapGiaTriHopLe(String prompt, Validator validator, String errorMessage) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim(); // Lấy đầu vào và loại bỏ khoảng trắng dư thừa
            if (validator.validate(input)) {
                return input; // Hợp lệ, trả về và thoát khỏi vòng lặp
            } else {
                System.out.println("Lỗi: " + errorMessage + " Vui lòng nhập lại.");
            }
        }
    }

    private List<Customer> customers = new ArrayList<>();

    public CustomerService() {
        loadFromFile();
    }

    public void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("File dữ liệu không tồn tại, khởi tạo danh sách trống.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String maKH = parts[0].trim();
                    String tenKH = parts[1].trim();
                    String soDT = parts[2].trim();
                    boolean isVip = Boolean.parseBoolean(parts[3].trim());
                    int diemThuong = Integer.parseInt(parts[4].trim());
                    Customer c = new Customer(maKH, tenKH, soDT, isVip, diemThuong);
                    customers.add(c);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Customer c : customers) {
                String line = String.join(",",
                        c.getMaKH(),
                        c.getTen(),
                        c.getSdt(),
                        String.valueOf(c.isVip()),
                        String.valueOf(c.getDiemThuong())
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }

    public void add(Customer c) {
        customers.add(c);
        saveToFile();
    }

    public void update(String ma, Customer newC) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getMaKH().equals(ma)) {
                customers.set(i, newC);
                saveToFile();
                return;
            }
        }
        System.out.println("Không tìm thấy khách hàng để cập nhật!");
    }

    public void delete(String ma) {
        if (customers.removeIf(c -> c.getMaKH().equals(ma))) {
            saveToFile();
            System.out.println("Xóa khách hàng thành công!");
        } else {
            System.out.println("Không tìm thấy khách hàng để xóa!");
        }
    }

    public Customer find(String ma) {
        for (Customer c : customers) {
            if (c.getMaKH().equals(ma)) {
                return c;
            }
        }
        return null;
    }

    public void printAll() {
        if (customers.isEmpty()) {
            System.out.println("Không có khách hàng nào!");
        } else {
            for (Customer c : customers) {
                System.out.println(c);
            }
        }
    }
}
