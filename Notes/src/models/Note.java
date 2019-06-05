package models;

import java.util.Date;

/**
 *
 * @author User
 */
public class Note {
    private int id;
    private String name;
    private Integer days;
    private Date date;
    private Category category;
    private String status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Note{" + "id=" + id + ", name=" + name + ", days=" + days + ", date=" + date + ", category=" + category + ", status=" + status + '}';
    }
    
    
    
}
