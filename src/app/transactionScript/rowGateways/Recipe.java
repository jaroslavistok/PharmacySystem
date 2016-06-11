package app.transactionScript.rowGateways;

import app.transactionScript.db.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;


/**
 * Represents single row from table Recipes
 */
public class Recipe implements RowDataGateway {
    public int recipeID;
    public Date date;
    public int cashRegisterNumber;
    public int number;
    public int medicamentID;


    /**
     *
     * This method finds row in database by given ID
     * Return new Object of Recipe class taht represents this row
     * otherwise it returns null
     *
     */
    public static Recipe findById(int recipeId) {

        Recipe recipe = new Recipe();
        String query = "SELECT * FROM recipes WHERE recipe_id=?";
        try {
            PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(query);
            statement.setString(1, String.valueOf(recipeId));
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return null;

            recipe.recipeID = resultSet.getInt("recipe_id");
            recipe.date = resultSet.getDate("date");
            recipe.medicamentID = resultSet.getInt("medicament_id");
            recipe.number = resultSet.getInt("number");
            recipe.cashRegisterNumber = resultSet.getInt("cash_register_number");

            resultSet.close();
            ConnectionManager.close();
            return recipe;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insert new row represented by this class to the database
     *
     * Constraint: medicamentID must exists in table medicament
     * otherwise this method will not work
     */
    @Override
    public void insert() {
        try {
            String sql = "INSERT INTO recipes (date, cash_register_number, number, medicament_id)" +
                    "VALUES (?,?,?,?)";
            PreparedStatement preparedStatement =
                    ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setDate(1, date);
            preparedStatement.setInt(2, cashRegisterNumber);
            preparedStatement.setInt(3, number);
            preparedStatement.setInt(4, medicamentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates row represented by this class and sets new values
     */
    @Override
    public void update() {
        String sql = "UPDATE recipes SET date=?, cash_register_number=?, number=?, medicament_id=?" +
                "WHERE recipe_id=?";
        try {
            PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setDate(1, date);
            preparedStatement.setInt(2, cashRegisterNumber);
            preparedStatement.setInt(3, number);
            preparedStatement.setInt(4, medicamentID);
            preparedStatement.setInt(5, recipeID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes row represented by this class from database
     *
     * Recipe id should exists, otherwise nothing will be deleted
     */
    @Override
    public void delete() {
        String sql = "DELETE FROM recipes WHERE recipe_id=?";
        try {
            PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, recipeID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
