package be.mathiasdejong.endercrop.fabric.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ConfigScreen implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return be.mathiasdejong.endercrop.config.ConfigScreen::getConfigScreen;
    }
}
