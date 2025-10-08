package service;
import java.io.*;
import java.util.*;
import models.Customer;
public class CustomerService {
    
    private static final String DATA_FILE = "customers.txt";
   

    private List<Customer> customers = new ArrayList<>();
    public CustomerService(){
        loadFromFile();
    }

    @SuppressWarnings("unchecked")
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
                    String maKH = parts[0];
                    String tenKH = parts[1];
                    String soDT = parts[2];
                    boolean isVip = Boolean.parseBoolean(parts[3]);
                    int diemThuong = Integer.parseInt(parts[4]);
                    Customer c = new Customer(maKH, tenKH, soDT, isVip ? "VIP" : "Thường", diemThuong);
                    customers.add(c);
                }
            }
        } catch (IOException e) {
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
                        c.getVip().equals("VIP") ? "true" : "false",
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
    }
    public void delete(String ma) {
        customers.removeIf(c -> c.getMaKH().equals(ma));
        saveToFile();
    }
    public Customer findById(String ma) {
        for (Customer c : customers) {
            if (c.getMaKH().equals(ma)) {
                return c;
            }
        }
        return null;
    }
    public Customer findByName(String ten) {
    List<Customer> result = new ArrayList<>();
    for (Customer c : customers) {
        if (c.getTen().toLowerCase().contains(ten.toLowerCase())) {
            result.add(c);
        }
    }
    return result;
}

    public void printAll() {
        for (Customer c : customers) {
            System.out.println(c);
        }
    }
}
