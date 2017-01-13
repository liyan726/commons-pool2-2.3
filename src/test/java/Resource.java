public class Resource {

    private static int id;
    private int rid;

    public Resource() {
        synchronized (this) {
            this.rid = id++;
        }
    }

    public int getRid() {
        return this.rid;
    }

    @Override
    public String toString() {
        return "id:" + this.rid;
    }

}
