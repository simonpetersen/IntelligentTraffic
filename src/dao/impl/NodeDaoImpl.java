package dao.impl;

import dao.ConnectionFactory;
import dao.NodeDao;
import model.NodeTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NodeDaoImpl implements NodeDao {

    private PreparedStatement insertPreparedStmt = null;

    public NodeDaoImpl() throws Exception {

        // Initialization of prepared statements
        // (`Id`,`Type`,`Latitude`,`Longitude`)
        insertPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO node (id, type, latitude, longitude) VALUES (?,?,?,?)");
    }

    @Override
    public void insertNode(NodeTO nodeTO) {
        try {

            insertPreparedStmt.setLong(1, nodeTO.getId());
            insertPreparedStmt.setString(2, nodeTO.getType());
            insertPreparedStmt.setDouble(3, nodeTO.getLatitude());
            insertPreparedStmt.setDouble(4, nodeTO.getLongitude());

            insertPreparedStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
