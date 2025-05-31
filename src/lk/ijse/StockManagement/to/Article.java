package lk.ijse.StockManagement.to;


import java.time.LocalDate;

public class Article {

    private String reference;
    private String name;
    private String category;
    private boolean isConsumable;
    private LocalDate expirationDate;
    private int quantity;
    private int minStock;

    // Fields for your controller methods
    private String designation;
    private long prixAchat;
    private int isActive;

    // Constructor
    public Article(String reference, String name, boolean isConsumable, LocalDate expirationDate, int quantity, int minStock) throws Exception {
        if (reference == null) {
            throw new IllegalArgumentException("Reference cannot be null or blank");
        }

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }

        if (expirationDate.isBefore(LocalDate.now())) {
            throw new Exception("Expiration Date should be after current date");
        }

        if (quantity <= 0) {
            throw new Exception("Quantity should be greater than 0");
        }

        this.reference = reference;
        this.name = name;
        this.isConsumable = isConsumable;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.minStock = minStock;
    }

    public Article() {
    }

    public Article(String quantity , String reference){
        this.reference=reference;
        this.quantity=Integer.valueOf(quantity);
    }

    public Article(String ref, String name, String category, int isCon, String expirDate,
                   int quantity, int minStock, String description, double price, int isActive) {
        this.reference = ref;
        this.name = name;
        this.category = category;
        if (isCon == 0) {
            this.isConsumable = false;
        } else {
            this.isConsumable = true;
        }
        this.expirationDate = LocalDate.parse(expirDate);
        this.quantity = quantity;
        this.minStock = minStock;
        this.designation = description;
        this.prixAchat = (long) price;
        this.isActive = isActive;
    }

    public String getDesignation() {
        return designation;
    }

    // Getters and Setters for all fields

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public long getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(long prixAchat) {
        this.prixAchat = prixAchat;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }


    public String getIdCat() {
        return category; // Returns the name of the enum as a String, or null if idCat is null
    }



    public String getReference() {
        return reference;
    }

    // Existing Getters and Setters for other fields

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isConsumable() {
        return isConsumable;
    }

    public void setConsumable(boolean consumable) {
        isConsumable = consumable;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return reference.equals(article.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

}
