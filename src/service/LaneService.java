package service;

import models.Lane;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LaneService {
    private static final String FILE_PATH = "C://quan-ly-phong-choi-bowling//data//src//lane.txt";

    public void saveLane(Lane lane) {
        List<Lane> lanes = readLanes();
        lanes.add(lane);
        writeLanesToFile(lanes);
    }

    public void updateLane(String maLane, Lane updatedLane) {
        List<Lane> lanes = readLanes();
        for (int i = 0; i < lanes.size(); i++) {
            if (lanes.get(i).getMaLane().equals(maLane)) {
                lanes.set(i, updatedLane);
                break;
            }
        }
        writeLanesToFile(lanes);
    }

    public void deleteLane(String maLane) {
        List<Lane> lanes = readLanes();
        lanes.removeIf(lane -> lane.getMaLane().equals(maLane));
        writeLanesToFile(lanes);
    }

    public List<Lane> findLanes(String searchTerm, String trangThai) {
        List<Lane> lanes = readLanes();
        List<Lane> result = new ArrayList<>();
        for (Lane lane : lanes) {
            boolean matchMaHoacTen = searchTerm == null || searchTerm.isEmpty() ||
                    lane.getMaLane().equals(searchTerm) || lane.getTenLane().equalsIgnoreCase(searchTerm);
            boolean matchTrangThai = trangThai == null || trangThai.isEmpty() || lane.getTrangThai().equalsIgnoreCase(trangThai);
            if (matchMaHoacTen && matchTrangThai) {
                result.add(lane);
            }
        }
        return result;
    }

    public List<Lane> getAllLanes() {
        return readLanes();
    }

    private List<Lane> readLanes() {
        List<Lane> lanes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    lanes.add(new Lane(data[0], data[1], data[2], Double.parseDouble(data[3]), data[4]));
                }
            }
        } catch (IOException e) {
            System.out.printf("File không tồn tại hoặc lỗi: " + e.getMessage());
        }
        return lanes;
    }

    private void writeLanesToFile(List<Lane> lanes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Lane lane : lanes) {
                writer.write(lane.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }
}
