package xyz.novaserver.placeholders.placeholder.seasons;

public class TemperatureData {
    private int temperature;
    private boolean rpApplied;
    private String display;

    public TemperatureData(int temperature, boolean rpApplied) {
        this.temperature = temperature;
        this.rpApplied = rpApplied;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public boolean isRpApplied() {
        return rpApplied;
    }

    public void setRpApplied(boolean rpApplied) {
        this.rpApplied = rpApplied;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public boolean equalsData(int temperature, boolean rpApplied) {
        return temperature == this.temperature
                && rpApplied == this.rpApplied;
    }
}
