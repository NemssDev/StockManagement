package lk.ijse.StockManagement.to;

public class Order {
    private String order_id;
    private String article;
    private String service;
    private String quantity;
    private String from_storage;
    private String to_storage;
    private String order_date;

    public Order(String order_id, String article, String service, String quantity, String storage , String toStorage, String order_date) {
        this.order_id = order_id;
        this.article = article;
        this.service = service;
        this.quantity = quantity;
        this.from_storage = storage;
        this.to_storage = toStorage;
        this.order_date = order_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFromStorage() {
        return from_storage;
    }

    public void setFromStorage(String storage) {
        this.from_storage = storage;
    }

    public String getToStorage() {
        return to_storage;
    }

    public void setToStorage(String storage) {
        this.to_storage = storage;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }


    @Override
    public String toString() {
        return "Order{" +
                "order_id='" + order_id + '\'' +
                ", article='" + article + '\'' +
                ", service='" + service + '\'' +
                ", quantity='" + quantity + '\'' +
                ", from_storage='" + from_storage + '\'' +
                ", order_date='" + order_date + '\'' +
                '}';
    }
}
