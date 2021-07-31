package xyz.novaserver.placeholders.player;

import org.geysermc.floodgate.util.DeviceOs;

public class PlaceholderPlayer {
    //TODO: store device/loader type with an enum
    private boolean usingFabric = false;
    private boolean usingForge = false;
    private boolean usingBedrock = false;
    private boolean acceptedPack = false;
    private DeviceOs deviceOs = DeviceOs.UNKNOWN;

    public boolean isUsingFabric() {
        return usingFabric;
    }

    public void setUsingFabric(boolean usingFabric) {
        this.usingFabric = usingFabric;
    }

    public boolean isUsingForge() {
        return usingForge;
    }

    public void setUsingForge(boolean usingForge) {
        this.usingForge = usingForge;
    }

    public boolean isUsingBedrock() {
        return usingBedrock;
    }

    public void setUsingBedrock(boolean usingBedrock) {
        this.usingBedrock = usingBedrock;
    }

    public boolean isAcceptedPack() {
        return acceptedPack;
    }

    public void setAcceptedPack(boolean acceptedPack) {
        this.acceptedPack = acceptedPack;
    }

    public DeviceOs getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(DeviceOs deviceOs) {
        this.deviceOs = deviceOs;
    }

}
