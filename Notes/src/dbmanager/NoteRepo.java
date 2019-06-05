package dbmanager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.Category;
import models.Note;

/**
 *
 * @author User
 */
public class NoteRepo extends DbConnection implements Repository<Note> {

    @Override
    public void add(Note t) {
        String query = "INSERT INTO note (name, days, date, category_id) "
                + "VALUES (?, ?, NOW(), ?)";

        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, t.getName());
            preparedStatement.setInt(2, t.getDays());
            preparedStatement.setInt(3, t.getCategory().getId());
            preparedStatement.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public void update(Note t) {
        String query = "UPDATE note SET name=?, days=?, category_id=? "
                + "where id=?";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, t.getName());
            preparedStatement.setInt(2, t.getDays());
            preparedStatement.setInt(3, t.getCategory().getId());
            preparedStatement.setInt(4, t.getId());
            preparedStatement.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public void remove(Note t) {
        String query = "UPDATE note SET active=0 "
                + "where id=?";

        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, t.getId());
            preparedStatement.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public List<Note> findAll() {
        String query = "SELECT * FROM notes.note "
                + "left join notes.category "
                + "on  note.category_id=category.id "
                + "where active=1";
        List<Note> noteList = new ArrayList<>();
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Note note = new Note();
                note.setId(resultSet.getInt("note.id"));
                note.setName(resultSet.getString("note.name"));
                note.setDays(resultSet.getInt("note.days"));
                note.setDate(resultSet.getDate("note.date"));
                Category ctg = new Category();
                ctg.setId(resultSet.getInt("category.id"));
                ctg.setName(resultSet.getString("category.name"));
                note.setCategory(ctg);
                note.setStatus(resultSet.getString("note.status"));
                noteList.add(note);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }

        return noteList;
    }

    @Override
    public List<Note> find(Map<String, String> fieldList) {
        String query = "select * from note left join category "
                + "on note.category_id = category.id "
                + "where note.name like '%" + fieldList.get("name")
                + "%' and note.days like '%" + fieldList.get("days")
                + "%' and category.name like '%" + fieldList.get("category") + "%' "
                + "and note.active = 1";
        List<Note> noteList = new ArrayList<>();
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Note note = new Note();
                note.setId(resultSet.getInt("note.id"));
                note.setName(resultSet.getString("note.name"));
                note.setDays(resultSet.getInt("note.days"));
                note.setDate(resultSet.getDate("note.date"));
                Category ctg = new Category();
                ctg.setId(resultSet.getInt("category.id"));
                ctg.setName(resultSet.getString("category.name"));
                note.setCategory(ctg);
                note.setStatus(resultSet.getString("note.status"));
                noteList.add(note);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            disconnect();
        }

        return noteList;
    }

    public void makeComplete(Note note) {
        String query = "update note "
                + "set status = 'Completed' where id = ?";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, note.getId());
            preparedStatement.execute();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void makeAllComplete() {
        String query = "update note set status = 'Completed' "
                + "where status = 'Not Completed'";
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public List<Note> showNoteByStatus(String statusQuery) {
        List<Note> noteList = new ArrayList<>();
        String query = null;
        if (statusQuery.equalsIgnoreCase("Completed")) {
            query = "select * from note "
                    + "left join category "
                    + "on note.category_id = category.id "
                    + "where note.status = 'Completed' and note.active = 1";
        } else {
            query = "select * from note "
                    + "left join category "
                    + "on note.category_id = category.id "
                    + "where note.status = 'Not Completed' "
                    + "and note.active = 1";
        }
        try {
            connect();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Note note = new Note();
                note.setId(resultSet.getInt("note.id"));
                note.setName(resultSet.getString("note.name"));
                note.setDays(resultSet.getInt("note.days"));
                note.setDate(resultSet.getDate("note.date"));
                Category ctg = new Category();
                ctg.setId(resultSet.getInt("category.id"));
                ctg.setName(resultSet.getString("category.name"));
                note.setCategory(ctg);
                note.setStatus(resultSet.getString("note.status"));
                noteList.add(note);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return noteList;
    }
}
