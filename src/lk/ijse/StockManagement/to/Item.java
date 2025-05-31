package lk.ijse.StockManagement.to;

public class Item {
    private String id;
    private String name;
    private Integer quantity;
    private String location;
    private String condtion;


    public Item() {
    }

    public Item(String id, String name, Integer quantity, String location, String condtion) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.location = location;
        this.condtion = condtion;
    }

    public Item(String id, String name, String quantity, String location, String condtion) {
        this.id = id;
        this.name = name;
        this.quantity = Integer.parseInt(quantity);
        this.location = location;
        this.condtion = condtion;
    }

    public Item(String name, String quantity, String location) {
        this.name = name;
        this.quantity = Integer.parseInt(quantity);
        this.location = location;
    }

    public Item(String id ,String name, String quantity, String location) {
        this.id=id;
        this.name = name;
        this.quantity = Integer.parseInt(quantity);
        this.location = location;
    }



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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQuantity() {
        return String.valueOf(quantity);
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCondtion() {
        return condtion;
    }

    public void setCondtion(String condtion) {
        this.condtion = condtion;
    }
}
