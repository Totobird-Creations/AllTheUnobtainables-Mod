package net.totobirdcreations.unobtainables.mixin;

import net.minecraft.client.gui.screen.ingame.StructureBlockScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(StructureBlockScreen.class)
public interface StructureBlockScreenInterface {

    @Accessor("inputName")
    TextFieldWidget getInputName();

    @Accessor("inputMetadata")
    TextFieldWidget getInputMetadata();

}
