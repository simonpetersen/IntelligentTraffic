package model.dto;

public class RouteCalculationParameterTO {

    private String parameterName, category;
    private double lowerBound, upperBound, impactFactor;

    public RouteCalculationParameterTO(String parameterName, String category, double lowerBound, double upperBound, double impactFactor) {
        this.parameterName = parameterName;
        this.category = category;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.impactFactor = impactFactor;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public double getImpactFactor() {
        return impactFactor;
    }

    public void setImpactFactor(double impactFactor) {
        this.impactFactor = impactFactor;
    }
}
