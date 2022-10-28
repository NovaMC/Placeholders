package xyz.novaserver.placeholders.placeholder.seasons;

public class TemperatureData {
    private int temperature;
    private boolean bedrock;
    private String display;

    public TemperatureData(int temperature, boolean bedrock) {
        this.temperature = temperature;
        this.bedrock = bedrock;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setBedrock(boolean bedrock) {
        this.bedrock = bedrock;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public boolean equalsData(int temperature, boolean bedrock) {
        return temperature == this.temperature
                && bedrock == this.bedrock;
    }
}
