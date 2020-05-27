package Application.Database;

import Application.Models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OtherModelsUtils {
    DatabaseConnection db;
    PreparedStatement pSt;

    public OtherModelsUtils(DatabaseConnection db) {
        this.db = db;
    }

    public void addCity(int state, String city_name) throws SQLException {
        String sql = "insert into cities(name, state) values (?, ?)";
        pSt = db.conn.prepareStatement(sql);
        pSt.setInt(1, state);
        pSt.setString(2,city_name);
        pSt.execute();
    }

    public void addState(String name) throws SQLException {
        String sql = "insert into states(name) values (?)";
        pSt = db.conn.prepareStatement(sql);
        pSt.setString(1,name);
        pSt.execute();

    }

    public void addPunishment(String title) throws SQLException{

        String sql = "insert into punishments(title) values (?)";
        pSt = db.conn.prepareStatement(sql);
        pSt.setString(1,title);
        pSt.execute();
    }

    public void addRoom(String type) throws SQLException{

        String sql = "insert into rooms(type) values (?)";
        pSt = db.conn.prepareStatement(sql);
        pSt.setString(1,type);
        pSt.execute();

    }

    public void addCrime(String name) throws SQLException{

        String sql = "insert into crimes(name) values (?)";
        pSt = db.conn.prepareStatement(sql);
        pSt.setString(1,name);
        pSt.execute();
    }

    public void removePunishment(int id) throws SQLException {
        String sql = "delete from punishments where id = ?";
        pSt = db.conn.prepareStatement(sql);
        pSt.setInt(1,id);
        pSt.execute();

    }

    public void removeRoom(int id)  throws SQLException{

        String sql = "delete from rooms where id = ?";
        pSt = db.conn.prepareStatement(sql);
        pSt.setInt(1,id);
        pSt.execute();
    }
    public void removeCity(int id) throws SQLException{

        String sql = "delete from cities where id = ?";
        pSt = db.conn.prepareStatement(sql);
        pSt.setInt(1,id);
        pSt.execute();
    }

    // this throws an constraint-error if a state still has other cities linked to it
    public void removeState (int id)  throws SQLException{

        String sql = "delete from states where id = ?";
        pSt = db.conn.prepareStatement(sql);
        pSt.setInt(1,id);
        pSt.execute();
    }

    // can't remove if there are prisoners still linked to the crime
    public void removeCrime(int id) throws SQLException {

        String sql = "delete from crimes where id = ?";
        pSt = db.conn.prepareStatement(sql);
        pSt.setInt(1,id);
        pSt.execute();
    }


    public List<City> findAllCities(int state) {
        String sql = "select * from cities where state = ?";
        List<City> cities = new ArrayList<>();
        try {
            pSt = db.conn.prepareStatement(sql);
            pSt.setInt(1, state);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                City city = new City();
                city.setId(rs.getInt(1));
                city.setName(rs.getString(2));
                city.setState(rs.getInt(3));
                cities.add(city);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return cities;
    }

    public List<City> findAllCities() {
        String sql = "select * from cities";
        List<City> cities = new ArrayList<>();
        try {
            pSt = db.conn.prepareStatement(sql);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                City city = new City();
                city.setId(rs.getInt(1));
                city.setName(rs.getString(2));
                city.setState(rs.getInt(3));
                cities.add(city);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return cities;
    }

    public List<Crime> findAllCrimes() {
        String sql = "select * from crimes";
        List<Crime> crimes = new ArrayList<>();
        try {
            pSt = db.conn.prepareStatement(sql);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                Crime crime = new Crime();
                crime.setId(rs.getInt(1));
                crime.setName(rs.getString(2));
                crimes.add(crime);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return crimes;
    }

    public List<Punishment> findAllPunishments() {
        String sql = "select * from punishments";
        List<Punishment> punishments = new ArrayList<>();
        try {
            pSt = db.conn.prepareStatement(sql);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                Punishment p = new Punishment();
                p.setId(rs.getInt(1));
                p.setTitle(rs.getString(2));
                punishments.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return punishments;
    }


    public List<State> findAllStates() {
        String sql = "select * from states";
        List<State> states = new ArrayList<>();
        try {
            pSt = db.conn.prepareStatement(sql);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                State s = new State();
                s.setId(rs.getInt(1));
                s.setName(rs.getString(2));
                states.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return states;
    }
  public List<Room> findAllRooms() {
        String sql = "select * from rooms";
        List<Room> rooms = new ArrayList<>();
        try {
            pSt = db.conn.prepareStatement(sql);
            ResultSet rs = pSt.executeQuery();
            while (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt(1));
                r.setType(rs.getString(2));
                rooms.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rooms;
    }
}

