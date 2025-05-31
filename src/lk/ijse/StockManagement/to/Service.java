package lk.ijse.StockManagement.to;

public class Service {

    private String id;
    private String name;
    private String type;
    private String desciption;

    // Constructeur par défaut
    public Service() {
    }

    // Constructeur avec paramètres
    public Service(String id, String name, String type, String desciption) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.desciption = desciption;
    }

    // Getters et Setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", desciption='" + desciption + '\'' +
                '}';
    }
}
