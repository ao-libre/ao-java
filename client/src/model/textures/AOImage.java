package model.textures;

public class AOImage {

    private int x;
    private int y;
    private int fileNum;
    private int id;
    private int width;
    private int height;

    public AOImage() {
    }

    public AOImage(int id, int x, int y, int fileNum, int pixelWidth, int pixelHeight) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.fileNum = fileNum;
        this.width = pixelWidth;
        this.height = pixelHeight;
    }

    public int getX() {
        return x * 2;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y * 2;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFileNum() {
        return fileNum;
    }

    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }

    public int getWidth() {
        return width * 2;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height * 2;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getId() + ":" + " file: " + getFileNum() + " x: " + getX() + " y: " + getY();
    }
}
