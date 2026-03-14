package com.dev.k.invoice.batch.overdue.dto;

public class BatchResult {

    private int targetCount;
    private int updatedCount;

    public BatchResult() {
    }

    public BatchResult(int targetCount, int updatedCount) {
        this.targetCount = targetCount;
        this.updatedCount = updatedCount;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(int updatedCount) {
        this.updatedCount = updatedCount;
    }
}