package service;

import java.io.*;
import java.util.*;
import models.Customer;

public class CustomerService {
    private static Scanner scanner = new Scanner(System.in);
    private static final String DATA_FILE = "C://dev//quan-ly-phong-choi-bowling//data//src//customer.txt";

    interface Validator {
        boolean validate(String value);
    }

    public static String nhapGiaTriHopLe(String prompt, Validator validator, String errorMessage) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (validator.validate(input)) {
                return input;
            } else {
                System.out.println("Lỗi: " + errorMessage + " Vui lòng nhập lại.");
            }
        }
    }

    private List<Customer> customers = new ArrayList<>();

    public CustomerService() {
        loadFromFile();
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("File dữ liệu không tồn tại, khởi tạo danh sách trống.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            customers = (List<Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(customers);
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

    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    public List<Customer> searchByNameOrPhone(String searchTerm) {
        return customers.stream()
                .filter(c -> c.getTen().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        c.getSdt().contains(searchTerm))
                .toList();
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