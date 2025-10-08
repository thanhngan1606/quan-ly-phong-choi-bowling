package service;

import models.ServiceEntity;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
public class ServiceEntityService {
    private Path filePath; // file path
    private List<ServiceEntity> services = new ArrayList<>();

    // Constructor with file path parameter
    public ServiceEntityService(String filePathStr) {
        this.filePath = Paths.get(filePathStr);
    }

    public void loadFromFile() {
        services.clear(); // clear old data

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String maDV = parts[0].trim();
                    String maPhien = parts[1].trim();
                    String tenDV = parts[2].trim();
                    int soLuong = Integer.parseInt(parts[3].trim());
                    double gia = Double.parseDouble(parts[4].trim());

                    ServiceEntity service = new ServiceEntity(maDV, maPhien, tenDV, soLuong, gia);
                    services.add(service);
                } else {
                    System.out.println("Invalid format line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    public void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (ServiceEntity s : services) {
                // save as: maDV,maPhien,tenDV,soLuong,gia
                String line = String.format("%s,%s,%s,%d,%.2f",
                        s.getMaDV(), s.getMaPhien(), s.getTenDV(),
                        s.getSoLuong(), s.getGia());
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Data saved to file: " + filePath);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public void add(ServiceEntity s) {
        if (s != null) {
            services.add(s);
            System.out.println("Service added: " + s.getTenDV());
            saveToFile();
        } else {
            System.out.println("Invalid service!");
        }
    }

}


