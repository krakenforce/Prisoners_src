package Application.Models;

public class Photo {
    private int id;
    private String path;
    private int prisoner;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPrisoner() {
        return prisoner;
    }

    public void setPrisoner(int prisoner) {
        this.prisoner = prisoner;
    }
}
