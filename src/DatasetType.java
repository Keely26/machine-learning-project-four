public enum DatasetType {
    Banknote("banknote"),
    Glass("glass"),
    Iris("iris"),
    Parkinsons("parkinsons"),
    Retinopathy("retinopathy");

    private String fileName;

    DatasetType(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return fileName;
    }
}