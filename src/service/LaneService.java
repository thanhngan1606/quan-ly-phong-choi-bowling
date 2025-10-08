package service;

import service.ServiceEntityService;
import models.Lane;
import java.io.*;
import java.time.LocalDateTime;
import models.*;
import java.util.ArrayList;
import java.util.List;

public class LaneService {
    private static final String FILE_NAME = "C://dev//quan-ly-phong-choi-bowling//data//src//lane.txt";
    private List<Lane> lanes = new ArrayList<>();

    public LaneService() {
        loadFromFile();
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        lanes.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("File " + FILE_NAME + " không tồn tại. Tạo danh sách trống.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            lanes = (List<Lane>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(lanes);
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }

    public void saveLane(Lane lane) {
        lanes.add(lane);
        saveToFile();
    }

    public void updateLane(String maLane, Lane updatedLane) {
        for (int i = 0; i < lanes.size(); i++) {
            if (lanes.get(i).getMaLane().equals(maLane)) {
                lanes.set(i, updatedLane);
                saveToFile();
                return;
            }
        }
        System.out.println("Không tìm thấy lane!");
    }

    public boolean deleteLane(String maLane) {
        boolean removed = lanes.removeIf(lane -> lane.getMaLane().equals(maLane));
        if (removed) saveToFile();
        return removed;
    }

    public List<Lane> findLanes(String maOrTen, String trangThai) {
        List<Lane> results = new ArrayList<>();
        for (Lane lane : lanes) {
            boolean match = true;
            if (maOrTen != null && !maOrTen.isEmpty()) {
                match &= lane.getMaLane().contains(maOrTen) || lane.getTenLane().toLowerCase().contains(maOrTen.toLowerCase());
            }
            if (trangThai != null && !trangThai.isEmpty()) {
                match &= lane.getTrangThai().equalsIgnoreCase(trangThai);
            }
            if (match) results.add(lane);
        }
        return results;
    }

    public List<Lane> getAllLanes() {
        return new ArrayList<>(lanes);
    }

    public void updateLaneStatusFromSessions(List<GameSession> sessions) {
        for (Lane lane : lanes) {
            boolean isOccupied = sessions.stream()
                    .anyMatch(session -> session.getMaLane().equals(lane.getMaLane()) &&
                            LocalDateTime.now().isAfter(session.getThoiGianBatDau()) &&
                            LocalDateTime.now().isBefore(session.getThoiGianKetThuc()));
            if (isOccupied && !lane.getTrangThai().equals("đang chơi")) {
                lane.setTrangThai("đang chơi");
                updateLane(lane.getMaLane(), lane);
            } else if (!isOccupied && lane.getTrangThai().equals("đang chơi")) {
                lane.setTrangThai("trống");
                updateLane(lane.getMaLane(), lane);
            }
        }
    }
}
