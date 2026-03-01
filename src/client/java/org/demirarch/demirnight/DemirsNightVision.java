package org.demirarch.demirnight;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class DemirsNightVision implements ClientModInitializer {

    private static final Category MISC = new Category(Identifier.of("minecraft", "misc"));
    private KeyBinding nightVisionKey;
    private boolean nightVisionEnabled = false;

    @Override
    public void onInitializeClient() {
        nightVisionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nightvisiondemir.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (nightVisionKey.wasPressed()) {
                toggleNightVision(client);
            }
        });
    }

    private void toggleNightVision(MinecraftClient client) {
        if (client.player == null) return;

        nightVisionEnabled = !nightVisionEnabled;

        if (nightVisionEnabled) {
            client.player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.NIGHT_VISION,
                    Integer.MAX_VALUE,
                    0, false, false, false
            ));
            client.player.sendMessage(
                    Text.literal("Night Vision: ").append(
                            Text.literal("ON").formatted(Formatting.GREEN)
                    ), true
            );
        } else {
            client.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            client.player.sendMessage(
                    Text.literal("Night Vision: ").append(
                            Text.literal("OFF").formatted(Formatting.RED)
                    ), true
            );
        }
    }
}