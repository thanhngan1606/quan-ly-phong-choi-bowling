package service;
import java.io.*;
import java.util.*;
import models.Customer;
public class CustomerService {
    private static Scanner scanner = new Scanner(System.in);
    private static final String DATA_FILE = "customers.dat";
    /**
     * Hàm nhập liệu chung, lặp cho đến khi giá trị nhập vào hợp lệ theo logic xác thực.
     * * @param prompt Lời nhắc hiển thị cho người dùng (ví dụ: "Nhập Tên: ")
     * @param validator Logic xác thực cho trường dữ liệu này (ví dụ: (value) -> value.matches("[a-zA-Z\\s]+"))
     * @param errorMessage Thông báo lỗi khi nhập sai
     * @return Chuỗi dữ liệu đã được xác thực
     */
    // Lưu ý: Mình dùng Interface Functional (Lambda) cho Validator để code gọn nhất
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
    public Customer find(String ma) {
        for (Customer c : customers) {
            if (c.getMaKH().equals(ma)) {
                return c;
            }
        }
        return null;
    }
    public void printAll() {
        for (Customer c : customers) {
            System.out.println(c);
        }
    }
}
