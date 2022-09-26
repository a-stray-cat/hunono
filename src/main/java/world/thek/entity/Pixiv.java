package world.thek.entity;

/**
 * @author: thek
 * @date: 2022/9/22 上午10:56
 */
public class Pixiv {
    private int id;

    public Pixiv() {
    }

    public Pixiv(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Pixiv{" +
                "id=" + id +
                '}';
    }
}
