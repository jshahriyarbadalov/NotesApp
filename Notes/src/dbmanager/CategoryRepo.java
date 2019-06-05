package dbmanager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.Category;

/**
 *
 * @author User
 */
public class CategoryRepo extends DbConnection implements Repository<Category> {

    @Override
    public void add(Category t) {
        String query = "INSERT INTO category (name) VALUES (?)";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, t.getName());
            preparedStatement.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public void update(Category t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Category t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Category> findAll() {
        String query = "SELECT * FROM category";
        List<Category> categoryList = new ArrayList<>();
        try {
            connect();
            preparedStatement=connection.prepareStatement(query);
            resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                 Category ctg=new Category();
                 ctg.setId(resultSet.getInt(1));
                 ctg.setName(resultSet.getString(2));
                 categoryList.add(ctg);
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
        return categoryList; 
    }

    @Override
    public List<Category> find(Map<String, String> fieldList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
