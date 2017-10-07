package com.reportes;

/**
 *
 * 
 */
public class ReportType {
    private final String value;
    private final String label;
 
    public ReportType(String label, String value) {
        super();
        this.value = value;
        this.label = label;
    }
 
    public String getValue() {
        return value;
    }
 
    public String getLabel() {
        return label;
    }
}
