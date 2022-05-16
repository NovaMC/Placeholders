package xyz.novaserver.placeholders.actionbar;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class ActionbarConfig {

    private long interval;

    private String message;

    private List<Condition> conditions;

    private BedrockSection bedrock;

    public long interval() {
        return interval;
    }

    public String message() {
        return message;
    }

    public List<Condition> conditions() {
        return conditions;
    }

    public BedrockSection bedrock() {
        return bedrock;
    }

    @ConfigSerializable
    static class BedrockSection {

        private long interval;

        private String message;

        public long interval() {
            return interval;
        }

        public String message() {
            return message;
        }
    }

    enum Condition {
        WORLDGUARD
    }
}


