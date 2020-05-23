package Application.Database;

import Application.Models.Crime;
import Application.Models.Photo;
import Application.Models.Prisoner;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrisonerUtils {
    DatabaseConnection db;
    PreparedStatement pSt;

    // search type
    public static final int NAME_OR_ADDRESS = 3;
    public static final int CITY = 4;
    public static final int STATE = 5;
    public static final int CRIME = 6;
    public static final int PUNISHMENT = 7;
    public static final int GENDER = 8;
    public static final int ROOM_NUMBER = 9;
    public static final int PHOTO = 1;


    public PrisonerUtils(DatabaseConnection db) {
        setDb(db);
    }

    private void setDb(DatabaseConnection db) {
        this.db = db;
    }

    public int deletePrisoner(int prisoner) {
        int error = 0;
        String sql = "delete from prisoners where id = ?";
        try {
            pSt = db.conn.prepareStatement(sql);
            pSt.execute();
        } catch (SQLException e) {
            System.out.println(e);
            error = 1;
        }
        return error;
    }

    public void insertPrisoner(String first_name, String last_name, ObservableList<Integer> crimes, Date DOA, Date DOB,
                               int room, int punishment, int gender, int state, int city, String address, List<String> photos) {
        String sql = "insert into prisoners(first_name, last_name, punishment, room, state, city, gender, DOB, DOA, address)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            db.conn.setAutoCommit(false);
            PreparedStatement s = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            s.setString(1, first_name);
            s.setString(2, last_name);
            s.setInt(3, punishment);
            s.setInt(4, room);
            s.setInt(5, state);
            s.setInt(6, city);
            s.setInt(7, gender);
            s.setDate(8, DOB);
            s.setDate(9, DOA);
            s.setString(10, address);
            s.execute();
            ResultSet rs = s.getGeneratedKeys();
            if (!rs.isBeforeFirst()) {
                throw new SQLException("insertion failed");
            }
            int k = 0;
            while (rs.next()) {
                k = rs.getInt(1);
            }

            if (k != 0) {
                if (crimes.size() > 0) {
                    for (int i : crimes) {
                        addCrimeToPrisoner(k, i);
                    }

                    for (String photo : photos) {
                        addPhotoToPrisoner(k, photo);
                    }
                }
            }

            //add photos to database, another sql

            db.conn.commit();

        } catch (SQLException e) {
            System.out.println(e);
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("Error Inserting. Are you trying to add crimes/state/city/punishment/room that don't exist?\nRolling back...");
            } else {
                System.out.println("Error updating. Rolling back...");
            }
            try {
                db.conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Can't roll back. We're f***ed!");
                ex.printStackTrace();
            }
        } finally {
            try {
                db.conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public Prisoner getPrisonerByID(int id)  {
        String sql = "select * from prisoners where id = ?";
        Prisoner prisoner = new Prisoner();
        try {
            pSt = db.conn.prepareStatement(sql);
            pSt.setInt(1, id);
            ResultSet rs = pSt.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new SQLException("No record with that prisoner's id");
            }
            while (rs.next()) {
                prisoner.setId(rs.getInt(1));
                prisoner.setFirst_name(rs.getString(2));
                prisoner.setLast_name(rs.getString(3));
                prisoner.setPunishment(rs.getInt(4));
                prisoner.setRoom(rs.getInt(5));
                prisoner.setState(rs.getInt(6));
                prisoner.setCity(rs.getInt(7));
                prisoner.setGender(rs.getInt(8));
                prisoner.setDOA(rs.getDate(9));
                prisoner.setDOB(rs.getDate(10));
                prisoner.setAddress(rs.getString(11));

                // find crimes and photos based on prisoners's id
                int pId = rs.getInt(1);
                List<Photo> photos = findCrimesOrPhotoByPrisonerId(PHOTO, pId);
                List<Crime> crimes = findCrimesOrPhotoByPrisonerId(CRIME, pId);

                List<String> ps = new ArrayList<>();
                for (Photo p : photos) {
                    ps.add(p.getPath());
                }

                List<Integer> cs = new ArrayList<>();
                for (Crime c : crimes) {
                    cs.add(c.getId());
                }

                prisoner.setPhotos(ps);
                prisoner.setCrimes(cs);

            }
        } catch (SQLException e) {
            prisoner = null;
            System.out.println(e);
        }

        return prisoner;
    }

    public List<Photo> getPhotos(int prisoner) throws SQLException {
        return findCrimesOrPhotoByPrisonerId(PHOTO, prisoner);
    }


    public List<Prisoner> searchPrisonersByType(int searchType, String keyword) {
        String sql = "";
        if (searchType == NAME_OR_ADDRESS) {
            return searchByNameOrAddress(keyword);
        } else if (searchType == CRIME) {
            sql = "select * from prisoners p inner join prisoners_and_crimes pac on p.id = pac.prisoner inner join crimes c on pac.crime = c.id where c.name = ?";
        } else if (searchType == CITY) {
            sql = "select * from prisoners p inner join cities c on p.city = c.id where c.name = ?";
        } else if (searchType == PUNISHMENT) {
            sql = "select * from prisoners pac inner join punishments p on pac.punishment = p.id where p.title = ?";
        } else if (searchType == GENDER) {
            sql = "select * from prisoners where gender = ?";

        } else if (searchType == ROOM_NUMBER) {
            sql = "select * from prisoners pac inner join rooms on pac.room = rooms.id where rooms.type = ?";
        } else {
            sql = "select * from prisoners pac inner join states s on pac.state = s.id where state = ?";
        }

        try {
            pSt = db.conn.prepareStatement(sql);
            pSt.setString(1, keyword);
            ResultSet rs = pSt.executeQuery();
            List<Prisoner> prisoners = new ArrayList<>();
            while (rs.next()) {
                Prisoner prisoner = new Prisoner();
                prisoner.setId(rs.getInt(1));
                prisoner.setFirst_name(rs.getString(2));
                prisoner.setLast_name(rs.getString(3));
                prisoner.setPunishment(rs.getInt(4));
                prisoner.setRoom(rs.getInt(5));
                prisoner.setState(rs.getInt(6));
                prisoner.setCity(rs.getInt(7));
                prisoner.setGender(rs.getInt(8));
                prisoner.setDOA(rs.getDate(9));
                prisoner.setDOB(rs.getDate(10));
                prisoner.setAddress(rs.getString(11));

                int pId = rs.getInt(1);
                List<Photo> photos = findCrimesOrPhotoByPrisonerId(PHOTO, pId);
                List<Crime> crimes = findCrimesOrPhotoByPrisonerId(CRIME, pId);

                List<String> ps = new ArrayList<>();
                for (Photo p : photos) {
                    ps.add(p.getPath());
                }

                List<Integer> cs = new ArrayList<>();
                for (Crime c : crimes) {
                    cs.add(c.getId());
                }

                prisoner.setPhotos(ps);
                prisoner.setCrimes(cs);

                prisoners.add(prisoner);
            }
            return prisoners;
        } catch (SQLException e) {
            System.out.println(e);
        }


        return null;
    }

    private List<Prisoner> searchByNameOrAddress(String name) {
        List<Prisoner> all;
        try {
            all = findAllPrisoners();
            List<Prisoner> rs = filterByKeyWord(all, name);
            return rs;
        } catch (SQLException e) {
            System.out.println(e);

        }
        return null;
    }


    public void updatePrisoner(int id, String first_name, String last_name, ObservableList<Integer> crimes, Date DOA, Date DOB,
                               int room, int punishment, int gender, int state, int city, String address, List<String> photos) {
        String sql = "update prisoners set first_name = ?, last_name = ?, punishment = ?, room = ?, state = ?, city = ? ," +
                " gender = ?, DOB = ?, DOA = ?, address = ? where id = ?";

        try {
            db.conn.setAutoCommit(false);
            pSt = db.conn.prepareStatement(sql);
            pSt.setString(1, first_name);
            pSt.setString(2, last_name);
            pSt.setInt(3, punishment);
            pSt.setInt(4, room);
            pSt.setInt(5, state);
            pSt.setInt(6, city);
            pSt.setInt(7, gender);
            pSt.setDate(8, DOB);
            pSt.setDate(9, DOA);
            pSt.setString(10, address);
            pSt.setInt(11, id);
            int row = pSt.executeUpdate();
            if (row != 1) {
                throw new SQLException("No prisoner with that id");
            }
            // update crimes and photos:
            // first delete old crimes and photos:
            eraseCrimes(id);
            erasePhotos(id);

            // then add updated one:
            for (int i : crimes) {
                addCrimeToPrisoner(id, i);
            }
            for (String photo : photos) {
                addPhotoToPrisoner(id, photo);
            }
            db.conn.commit();

        } catch (SQLException e) {
            System.out.println(e);
            if (e instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("Error Inserting. Are you trying to add crimes/state/city/punishment/room that don't exist?\nRolling back...");
            } else {
                System.out.println("Error updating. Rolling back...");
            }
            try {
                db.conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Can't roll back. We're f***ed!");
                ex.printStackTrace();
            }
        } finally {
            try {
                db.conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void eraseCrimes(int prisoner) throws SQLException {
        String sql = "delete from prisoners_and_crimes where prisoner = ?";

        pSt = db.conn.prepareStatement(sql);
        pSt.setInt(1, prisoner);
        pSt.executeUpdate();

    }

    private void addCrimeToPrisoner(int prisoner, int crime) throws SQLException {
        String sql = "insert into prisoners_and_crimes(crime, prisoner) values (?, ?)";

        PreparedStatement s = db.conn.prepareStatement(sql);
        s.setInt(1, crime);
        s.setInt(2, prisoner);
        int row = s.executeUpdate();
        if (row != 1) {
            throw new SQLException("Error inserting");
        }
    }

    private void erasePhotos(int prisoner) throws SQLException {
        String sql = "delete from photos where prisoner = ?";

        pSt = db.conn.prepareStatement(sql);
        pSt.setInt(1, prisoner);
        pSt.executeUpdate();

    }

    private void addPhotoToPrisoner(int prisoner, String path) throws SQLException {
        String sql = "insert into photos(path, prisoner) values (?, ?)";

        PreparedStatement s = db.conn.prepareStatement(sql);
        s.setString(1, path);
        s.setInt(2, prisoner);
        int row = s.executeUpdate();
        if (row != 1) {
            throw new SQLException("Error inserting");
        }
    }

    private List<Prisoner> filterByKeyWord(List<Prisoner> prisoners, String keyword) {
        if (keyword.isBlank()) {
            return prisoners;
        }
        keyword = keyword.trim().toLowerCase();
        StringBuilder builder = new StringBuilder();
        List<Prisoner> rs = new ArrayList<>();
        for (Prisoner prisoner : prisoners) {
            builder.setLength(0);
            builder.append(prisoner.getAddress());
            builder.append(prisoner.getLast_name());
            builder.append(prisoner.getFirst_name());
            if (builder.toString().toLowerCase().contains(keyword)) {
                rs.add(prisoner);
            }
        }
        return rs;
    }

    private List<Prisoner> filterById(List<Prisoner> prisoners, ArrayList<Integer> prisoner_ids) {
        List<Prisoner> rs = new ArrayList<>();
        for (Prisoner prisoner : prisoners) {
            for (int id : prisoner_ids) {
                if (prisoner.getId() == id) {
                    rs.add(prisoner);
                    break;
                }
            }
        }
        return rs;
    }

    private List<Prisoner> findAllPrisoners() throws SQLException {
        String sql = "select * from prisoners";
        List<Prisoner> prisoners = new ArrayList<>();
        pSt = db.conn.prepareStatement(sql);
        ResultSet rs = pSt.executeQuery();
        while (rs.next()) {
            Prisoner prisoner = new Prisoner();
            prisoner.setId(rs.getInt(1));
            prisoner.setFirst_name(rs.getString(2));
            prisoner.setLast_name(rs.getString(3));
            prisoner.setPunishment(rs.getInt(4));
            prisoner.setRoom(rs.getInt(5));
            prisoner.setState(rs.getInt(6));
            prisoner.setCity(rs.getInt(7));
            prisoner.setGender(rs.getInt(8));
            prisoner.setDOA(rs.getDate(9));
            prisoner.setDOB(rs.getDate(10));
            prisoner.setAddress(rs.getString(11));

            // find crimes and photos based on prisoners's id
            int pId = rs.getInt(1);
            List<Photo> photos = findCrimesOrPhotoByPrisonerId(PHOTO, pId);
            List<Crime> crimes = findCrimesOrPhotoByPrisonerId(CRIME, pId);

            List<String> ps = new ArrayList<>();
            for (Photo p : photos) {
                ps.add(p.getPath());
            }

            List<Integer> cs = new ArrayList<>();
            for (Crime c : crimes) {
                cs.add(c.getId());
            }

            prisoner.setPhotos(ps);
            prisoner.setCrimes(cs);

            prisoners.add(prisoner);

        }

        return prisoners;

    }

    public <T> List<T> findCrimesOrPhotoByPrisonerId(int method, int prisoner) throws SQLException {
        List<T> crimes_or_photos = new ArrayList<>();
        if (method != PHOTO & method != CRIME) {
            throw new RuntimeException("You need to select only photo or crime method\nPlease refer to the Integer constants in PrisonersUtils");
        }
        if (method == PHOTO) {
            String sql = "Select photos.id, photos.path from photos where prisoner = ?";

            pSt = db.conn.prepareStatement(sql);
            pSt.setInt(1, prisoner);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                Photo c = new Photo();
                crimes_or_photos.add((T) c);
            }


        } else {
            String sql = "Select pac.crime, c.name from prisoners_and_crimes pac inner join crimes c on pac.crime = c.id where pac.prisoner = ?";
            pSt = db.conn.prepareStatement(sql);
            pSt.setInt(1, prisoner);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                Crime c = new Crime();
                c.setId(rs.getInt(1));
                c.setName(rs.getString(2));
                crimes_or_photos.add((T) c);
            }

        }
        return crimes_or_photos;
    }

}
