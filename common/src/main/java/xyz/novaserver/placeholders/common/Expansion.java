package xyz.novaserver.placeholders.common;

import xyz.novaserver.placeholders.placeholder.type.Placeholder;

import java.util.List;

public interface Expansion {
    void register(Placeholders placeholders, List<Placeholder> placeholderList);
}
