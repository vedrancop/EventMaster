package DAL;

import BE.Coordinator;
import BLL.Notifications;
import BLL.dbConnector;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;

public class CoordinatorDAO implements ICoordinatorDAO {

    private Notifications nt = new Notifications();

    @Override
    public Coordinator getCoordinator(){
        try (Connection connection1 = dbConnector.getConn()) {
            String sql = "SELECT * FROM EvCo";
            Statement stmt = connection1.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Coordinator coordinator1 = null;
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Username");
                String password = rs.getString("Password");
                coordinator1 = new Coordinator(name, id);
            }
            return coordinator1;
            //return persons;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCoordinatorbyId(int coorId){
        try (Connection connection1 = dbConnector.getConn()) {
            String sql = "SELECT Username FROM EvCo WHERE ID=?";
            PreparedStatement pstmt = connection1.prepareStatement(sql);
            pstmt.setInt(1, coorId);
            String name = null;
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    name = resultSet.getString("Username");
                }
            }
            return name;
            //return persons;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Coordinator> getCoordinatorAll(){
        try (Connection connection1 = dbConnector.getConn()) {
            String sql = "SELECT * FROM EvCo";
            Statement stmt = connection1.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Coordinator coordinator1 = null;
            ArrayList<Coordinator> coordinators = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Username");
                String password = rs.getString("Password");
                coordinator1 = new Coordinator(id, name, password);
                coordinators.add(coordinator1);
            }
            return coordinators;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void createCoordinator(Coordinator c){
        try(Connection connection= dbConnector.getConn()) {
            String sql ="INSERT INTO EvCo(Username, Password) VALUES(?,?)";
            PreparedStatement pstmt= connection.prepareStatement(sql);
            pstmt.setString(1, c.getUsername());
            pstmt.setString(2, c.getPassword());
            pstmt.execute();

        } catch (SQLException e) {
            nt.showError("System error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public void deleteCoordinator(int id){
      try(Connection connection=dbConnector.getConn()) {
          String sql="DELETE FROM EvCo WHERE ID=?";
          PreparedStatement pstmt= connection.prepareStatement(sql);
          pstmt.setInt(1, id);
          pstmt.execute();
      } catch (SQLException e) {
          nt.showError("System error: " + e.getMessage());
          e.printStackTrace();
      }

    }

}
