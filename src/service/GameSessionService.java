package service;

import models.GameSession;
import java.time.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class GameSessionService {
    private final Path path;
    private final List<GameSession> data = new ArrayList<>();
    private final java.util.function.Function<String, Double> lanePriceById;

    public GameSessionService(Path path,
                              java.util.function.Function<String, Double> lanePriceById) {
        this.path = path;
        this.lanePriceById = lanePriceById;
        reload(); // Load dữ liệu từ file khi khởi tạo
    }

    /** Lấy toàn bộ danh sách phiên chơi */
    public List<GameSession> list() {
        return new ArrayList<>(data);
    }

    /** Lấy phiên chơi theo mã */
    public Optional<GameSession> get(String id) {
        return data.stream()
                .filter(x -> Objects.equals(x.getMaPhien(), id))
                .findFirst();
    }

    /** Tạo mới hoặc cập nhật phiên chơi */
    public void createOrUpdate(GameSession s) {
        validate(s); // kiểm tra hợp lệ & trùng giờ
        s.setTongTien(calcAmount(s)); // tính tiền tự động
        get(s.getMaPhien()).ifPresentOrElse(
                old -> data.set(data.indexOf(old), s),
                () -> data.add(s)
        );
        flush(); // ghi lại file
    }

    /** Xóa phiên chơi theo mã */
    public boolean delete(String id) {
        Optional<GameSession> o = get(id);
        if (o.isEmpty()) return false;
        data.remove(o.get());
        flush();
        return true;
    }

    /** Kiểm tra tính hợp lệ của phiên chơi */
    private void validate(GameSession s) {
        if (s.getMaPhien() == null || s.getMaPhien().isBlank())
            throw new IllegalArgumentException("Mã phiên rỗng");

        if (s.getThoiGianKetThuc() == null || s.getThoiGianBatDau() == null ||
                !s.getThoiGianKetThuc().isAfter(s.getThoiGianBatDau()))
            throw new IllegalArgumentException("Thời gian không hợp lệ");

        // Kiểm tra trùng giờ cùng lane
        for (GameSession g : data) {
            if (g.getMaPhien().equals(s.getMaPhien())) continue;
            if (!Objects.equals(g.getMaLane(), s.getMaLane())) continue;

            boolean overlap = !s.getThoiGianKetThuc().isBefore(g.getThoiGianBatDau())
                    && !s.getThoiGianBatDau().isAfter(g.getThoiGianKetThuc());
            if (overlap)
                throw new IllegalArgumentException("Lane trùng giờ");
        }
    }

    /** Tính tổng tiền dựa vào thời gian và giá lane */
    private double calcAmount(GameSession s) {
        double gia = lanePriceById.apply(s.getMaLane());
        double hours = Duration.between(s.getThoiGianBatDau(), s.getThoiGianKetThuc()).toMinutes() / 60.0;
        return Math.round(hours * gia * 100.0) / 100.0;
    }


    //  TÌM DANH SÁCH LANE TRỐNG TRONG KHOẢNG THỜI GIAN
    public List<String> findAvailableLanes(List<String> allLaneIds,
                                           LocalDateTime start,
                                           LocalDateTime end) {
        List<String> available = new ArrayList<>();

        for (String laneId : allLaneIds) {
            boolean isBusy = data.stream().anyMatch(session ->
                    session.getMaLane().equalsIgnoreCase(laneId)
                            && !(end.isBefore(session.getThoiGianBatDau())
                            || start.isAfter(session.getThoiGianKetThuc()))
            );

            if (!isBusy) {
                available.add(laneId); // nếu không trùng giờ, thêm vào danh sách lane trống
            }
        }
        return available;
    }


    private void reload() {
        data.clear();
        for (String s : readAll(path))
            if (!s.isBlank())
                data.add(GameSession.parse(s));
    }

    private void flush() {
        List<String> lines = new ArrayList<>();
        for (GameSession l : data)
            lines.add(l.serialize());
        writeAll(path, lines);
    }

    private static List<String> readAll(Path p) {
        try {
            if (!Files.exists(p)) {
                Files.createDirectories(p.getParent());
                Files.createFile(p);
            }
            return Files.readAllLines(p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeAll(Path p, List<String> lines) {
        try {
            Files.createDirectories(p.getParent());
            Files.write(p, lines,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
