package service;

import models.Lane;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LaneService {
    private List<Lane> lanes = new ArrayList<>();

    // Load từ file TXT
    public void loadFromFile(String filePath) throws IOException {
        lanes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    Lane l = new Lane(
                            parts[0].trim(),                    // mã
                            parts[1].trim(),                    // tên
                            parts[2].trim(),                    // trạng thái
                            Double.parseDouble(parts[3].trim()),// giá
                            parts[4].trim()                     // bảo trì
                    );
                    lanes.add(l);
                }
            }
        }
    }


    // Ghi ra file TXT
    public void saveToFile(String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Lane l : lanes) {
                bw.write(l.toString());
                bw.newLine();
            }
        }
    }

    // Thêm lane
    public void addLane(Lane lane) {
        lanes.add(lane);
    }

    // Cập nhật lane
    public boolean updateLane(String maLane, Lane updatedLane) {
        Optional<Lane> existing = lanes.stream()
                .filter(l -> l.getMaLane().equals(maLane))
                .findFirst();
        if (existing.isPresent()) {
            Lane l = existing.get();
            l.setTen(updatedLane.getTen());
            l.setTrangThai(updatedLane.getTrangThai());
            l.setGia(updatedLane.getGia());
            l.setBaoTri(updatedLane.getBaoTri());
            return true;
        }
        return false;
    }

    // Xóa lane
    public boolean deleteLane(String maLane) {
        return lanes.removeIf(l -> l.getMaLane().equals(maLane));
    }

    // Tìm kiếm theo mã hoặc tên
    public List<Lane> searchLane(String keyword) {
        return lanes.stream()
                .filter(l -> l.getMaLane().equalsIgnoreCase(keyword)
                        || l.getTen().equalsIgnoreCase(keyword))
                .collect(Collectors.toList());
    }

    // Lọc theo trạng thái
    public List<Lane> filterByTrangThai(String trangThai) {
        return lanes.stream()
                .filter(l -> l.getTrangThai().equalsIgnoreCase(trangThai))
                .collect(Collectors.toList());
    }
    public List<Lane> search(String keyword) {
        return lanes.stream()
                .filter(l -> l.getMaLane().equalsIgnoreCase(keyword)
                        || l.getTen().equalsIgnoreCase(keyword)
|| l.getTrangThai().equalsIgnoreCase(keyword))
                .collect(Collectors.toList());
    }

    // Lấy tất cả lanes
    public List<Lane> getAllLanes() {
        return lanes;
    }
}