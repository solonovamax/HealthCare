package org.samo_lego.healthcare.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.samo_lego.config2brigadier.IBrigadierConfigurator;
import org.samo_lego.config2brigadier.annotation.BrigadierDescription;
import org.samo_lego.config2brigadier.annotation.BrigadierExcluded;
import org.samo_lego.healthcare.healthbar.HealthbarPreferences;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.logging.log4j.LogManager.getLogger;
import static org.samo_lego.healthcare.HealthCare.CONFIG_FILE;
import static org.samo_lego.healthcare.HealthCare.MODID;

public class HealthConfig implements IBrigadierConfigurator {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public final String _comment_blacklistedEntities = "// Which entities shouldn't have a healthbar above their name.";
    @SerializedName("blacklisted_entities")
    @BrigadierDescription(defaultOption = "[\"taterzens:npc\",\"specialmobs:mob_with_hidden_health\"]")
    public List<String> blacklistedEntities = new ArrayList<>(Arrays.asList(
            "taterzens:npc",
            "specialmobs:mob_with_hidden_health"
    ));
    @SerializedName("// When to activate the healthbar.")
    public String _comment_activationRange = "";
    @BrigadierDescription(defaultOption = "8.0")
    public float activationRange = 8.0F;

    @BrigadierExcluded
    public final Permissions perms = new Permissions();

    @SerializedName("// Max length of healthbar a player can use.")
    public final String _comment_maxHealthbarLength = "";
    @BrigadierDescription(defaultOption = "20")
    public int maxHealthbarLength = 20;

    @SerializedName("// Whether to show entity type next to health.")
    public final String _comment_showType = "";
    @BrigadierDescription(defaultOption = "true")
    public boolean showType = true;

    @SerializedName("// The default style of healthbar. The following are available")
    public final String _comment_defaultStyle1 = Arrays.toString(HealthbarPreferences.HealthbarStyle.values());
    @BrigadierDescription(defaultOption = "PERCENTAGE")
    public HealthbarPreferences.HealthbarStyle defaultStyle = HealthbarPreferences.HealthbarStyle.PERCENTAGE;

    @SerializedName("// Whether healthbar is enabled by default.")
    public final String _comment_enabled = "";
    @BrigadierDescription(defaultOption = "true")
    @SerializedName("enabled_by_default")
    public boolean enabledByDefault = true;

    @SerializedName("// Whether healthbar should always be visible (not just on entity hover) by default.")
    public final String _comment_alwaysVisibleDefault = "";
    @SerializedName("always_visible_by_default")
    @BrigadierDescription(defaultOption = "false")
    public boolean alwaysVisibleDefault = false;

    @Override
    public void save() {
       this.saveConfigFile(CONFIG_FILE);
    }

    public static final class Permissions {
        @SerializedName("// Enabled only if LuckPerms is loaded.")
        public final String _comment = "";
        public final String healthcare_config = "healthcare.config";
        public final String healthcare_config_edit = "healthcare.config.edit";
        public final String healthcare_config_reload = "healthcare.config.reload";

        @SerializedName("// Player permissions")
        public final String _comment_playerPermissions = "";
        public final String healthbar_toggle = "healthcare.healthbar.toggle";
        public final String healthbar_edit_style = "healthcare.healthbar.edit.style";
        public final String healthbar_edit_showEntityType = "healthcare.healthbar.edit.show_entity_type";
        public final String healthbar_edit_visibility = "healthcare.healthbar.edit.visibility";
        public final String healthbar_edit_custom_length = "healthcare.healthbar.edit.custom.length";
        public final String healthbar_edit_custom_symbols_full = "healthcare.healthbar.edit.symbol.full";
        public final String healthbar_edit_custom_symbols_empty = "healthcare.healthbar.edit.symbol.empty";
    }

    public Language lang = new Language();
    public static class Language {
        public String configReloaded = "Config was reloaded successfully.";
        public String customLengthSet = "Length of healthbar was set to %s.";
        public String customSymbolSet = "%s healthbar symbol was set to %s.";
        public String visibilitySet = "Always-visible property of healthbar was set to: %s";
        public String styleSet = "Style of your healthbar has been set to %s.";
        public String useCustomStyle = "Make sure to use style CUSTOM to have your settings applied.";
        public String healthbarEnabled = "Healthbars are now enabled.";
        public String healthbarDisabled = "Healthbars are now disabled.";
        public String reloadRequired = "Changes will be visible after relogging or entity update(s).";
        public String toggledType = "Show-entity-types property was set to %s.";
    }

    /**
     * Loads language file.
     *
     * @param file file to load the language file from.
     * @return HealthConfig object
     */
    public static HealthConfig loadConfigFile(File file) {
        HealthConfig config = null;
        if (file.exists()) {
            try (BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            )) {
                config = gson.fromJson(fileReader, HealthConfig.class);
            } catch (IOException e) {
                throw new RuntimeException(MODID + " Problem occurred when trying to load config: ", e);
            }
        }
        if (config == null) {
            config = new HealthConfig();
        }
        config.save();

        return config;
    }

    /**
     * Saves the config to the given file.
     *
     * @param file file to save config to
     */
    public void saveConfigFile(File file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            getLogger(MODID).error("Problem occurred when saving config: " + e.getMessage());
        }
    }
}
