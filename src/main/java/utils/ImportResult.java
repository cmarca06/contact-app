package utils;

public class ImportResult {
    private final int importedCount;
    private final int duplicateCount;

    public ImportResult(int importedCount, int duplicateCount) {
        this.importedCount = importedCount;
        this.duplicateCount = duplicateCount;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public int getDuplicateCount() {
        return duplicateCount;
    }

}
