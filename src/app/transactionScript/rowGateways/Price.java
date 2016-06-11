package app.transactionScript.rowGateways;

import app.transactionScript.db.ConnectionManager;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents row from table prices
 */
public class Price implements RowDataGateway {
    public int priceID;
    public int medicamentID;
    public BigDecimal purchasePrice;
    public double pharmacyProfit;
    public BigDecimal insurance_company;
    public BigDecimal patient;
    public double dph;

    /**
     * finds row in table prices by given id
     * returns new object that represents this row
     */
    public static Price findById(int priceID){
        Price price = new Price();
        String sql = "SELECT * FROM prices WHERE price_id=?";
        try {
            PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql);
            statement.setString(1, String.valueOf(priceID));
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;

            price.priceID = resultSet.getInt("price_id");
            price.dph = resultSet.getDouble("dph");
            price.medicamentID = resultSet.getInt("medicament_id");
            price.purchasePrice = resultSet.getBigDecimal("purchase_price");
            price.pharmacyProfit = resultSet.getDouble("pharmacy_profit");
            price.insurance_company = resultSet.getBigDecimal("insurance_company");
            price.patient = resultSet.getBigDecimal("patient");

            resultSet.close();
            ConnectionManager.close();
            return price;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts row represented by this class to the database
     *
     * Constraint: medicament_id must exists otherwise this method won't work properly
     */
    @Override
    public void insert() {
        try {
            String sql = "INSERT INTO prices (price_id, purchase_price, pharmacy_profit, " +
                    "insurance_company, patient, dph, medicament_id)" +
                    "VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement =
                    ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, priceID);
            preparedStatement.setBigDecimal(2, purchasePrice);
            preparedStatement.setDouble(3, pharmacyProfit);
            preparedStatement.setBigDecimal(4, insurance_company);
            preparedStatement.setBigDecimal(5, patient);
            preparedStatement.setDouble(6, dph);
            preparedStatement.setInt(7, medicamentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates row repsented by this class in database
     */
    @Override
    public void update() {
        try {
            String sql = "UPDATE prices SET purchase_price=?, pharmacy_profit=? " +
                    ",insurance_company=?, patient=?, dph=?, medicament_id=?";
            PreparedStatement preparedStatement =
                    ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setBigDecimal(1, purchasePrice);
            preparedStatement.setDouble(2, pharmacyProfit);
            preparedStatement.setBigDecimal(3, insurance_company);
            preparedStatement.setBigDecimal(4, patient);
            preparedStatement.setDouble(5, dph);
            preparedStatement.setInt(6, medicamentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes row represented by this class from database
     */
    @Override
    public void delete() {
        try {
            String sql = "DELETE FROM prices WHERE price_id=?";
            PreparedStatement preparedStatement =
                    ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, priceID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}