package net.totobirdcreations.unobtainables.mixin;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.gui.screen.ingame.StructureBlockScreen;
import net.minecraft.util.Identifier;
import net.totobirdcreations.unobtainables.Unobtainables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(StructureBlockScreen.class)
public class StructureBlockScreenMixin {

    @Inject(
            method = "updateStructureBlock(Lnet/minecraft/block/entity/StructureBlockBlockEntity$Action;)Z",
            at = @At("HEAD")
    )
    public void updateStructureBlock(StructureBlockBlockEntity.Action action, CallbackInfoReturnable<Boolean> callback) {
        StructureBlockScreenInterface ithis = (StructureBlockScreenInterface) this;
        Unobtainables.rememberStructureName(new Identifier(ithis.getInputName().getText()));
    }

}
