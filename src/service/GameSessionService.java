package service;

import models.GameSession;
import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class GameSessionService {
    private final Path path;
    private final List<GameSession> data = new ArrayList<>();
    private final Function<String, Double> lanePriceById;

    public GameSessionService(Path path, Function<String, Double> lanePriceById) {
        this.path = path;
        this.lanePriceById = lanePriceById;
        reload();
    }

    public List<GameSession> list() {
        return new ArrayList<>(data);
    }

    public Optional<GameSession> get(String id) {
        return data.stream()
                .filter(x -> Objects.equals(x.getMaPhien(), id))
                .findFirst();
    }

    public void createOrUpdate(GameSession s) {
        validate(s);
        s.setTongTien(calcAmount(s));
        get(s.getMaPhien()).ifPresentOrElse(
                old -> data.set(data.indexOf(old), s),
                () -> data.add(s)
        );
        flush();
    }

    public boolean delete(String id) {
        Optional<GameSession> o = get(id);
        if (o.isEmpty()) return false;
        data.remove(o.get());
        flush();
        return true;
    }

    private void validate(GameSession s) {
        if (s.getMaPhien() == null || s.getMaPhien().isBlank())
            throw new IllegalArgumentException("Mã phiên rỗng");

        if (s.getThoiGianKetThuc() == null || s.getThoiGianBatDau() == null ||
                !s.getThoiGianKetThuc().isAfter(s.getThoiGianBatDau()))
            throw new IllegalArgumentException("Thời gian không hợp lệ");

        for (GameSession g : data) {
            if (g.getMaPhien().equals(s.getMaPhien())) continue;
            if (!Objects.equals(g.getMaLane(), s.getMaLane())) continue;

            boolean overlap = !s.getThoiGianKetThuc().isBefore(g.getThoiGianBatDau()) &&
                    !s.getThoiGianBatDau().isAfter(g.getThoiGianKetThuc());
            if (overlap) throw new IllegalArgumentException("Lane trùng giờ");
        }
    }

    private double calcAmount(GameSession s) {
        double gia = lanePriceById.apply(s.getMaLane());
        double hours = java.time.Duration.between(s.getThoiGianBatDau(), s.getThoiGianKetThuc()).toMinutes() / 60.0;
        return Math.round(hours * gia * 100.0) / 100.0;
    }

    public List<String> findAvailableLanes(List<String> allLaneIds, LocalDateTime start, LocalDateTime end) {
        List<String> available = new ArrayList<>();
        for (String laneId : allLaneIds) {
            boolean isBusy = data.stream().anyMatch(session ->
                    session.getMaLane().equalsIgnoreCase(laneId) &&
                            !(end.isBefore(session.getThoiGianBatDau()) ||
                                    start.isAfter(session.getThoiGianKetThuc()))
            );
            if (!isBusy) available.add(laneId);
        }
        return available;
    }

    public List<GameSession> searchByCriteria(String searchTerm) {
        return data.stream()
                .filter(s -> s.getMaPhien().contains(searchTerm) ||
                        s.getMaKH().contains(searchTerm) ||
                        s.getMaLane().contains(searchTerm))
                .toList();
    }

    @SuppressWarnings("unchecked")
    private void reload() {
        data.clear();
        File file = new File(path.toString());
        if (!file.exists()) {
            System.out.println("File dữ liệu không tồn tại, khởi tạo danh sách trống.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toString()))) {
            data.addAll((List<GameSession>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    private void flush() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toString()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }
}