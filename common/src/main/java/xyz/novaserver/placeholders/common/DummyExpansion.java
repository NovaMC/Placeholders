package xyz.novaserver.placeholders.common;

import xyz.novaserver.placeholders.common.Expansion;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;

import java.util.List;

public class DummyExpansion implements Expansion {
    @Override
    public void register(Placeholders placeholders, List<Placeholder> placeholderList) {
        // Do nothing
    }
}
