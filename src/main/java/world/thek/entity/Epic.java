package world.thek.entity;

/**
 * @author: thek
 * @date: 2022/11/17 下午4:16
 */
public class Epic {
    public static final String EPIC_FREE = "epic";

    String title;
    String des;
    String date;

    public Epic(String title, String des, String date) {
        this.title = title;
        this.des = des;
        this.date = date;
    }

    public Epic() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + title + '\'' +
                ", des='" + des + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
