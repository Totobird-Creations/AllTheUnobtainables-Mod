package net.totobirdcreations.unobtainables.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.totobirdcreations.unobtainables.Unobtainables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BlockItem.class)
public class BlockItemMixin {

    public ItemPlacementContext context;


    @Inject(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At("HEAD")
    )
    public void place(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> callback) {
        this.context = context;
    }



    @Inject(
            method = "placeFromNbt(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void placeFromNbt(BlockPos pos, World world, ItemStack stack, BlockState state, CallbackInfoReturnable<BlockState> callback) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains("specialPlacement", NbtElement.STRING_TYPE)) {
            String     placeId    = nbt.getString("specialPlacement");
            BlockState placeState = null;

            if (this.stackIsItem(stack, Items.BLACK_WOOL)) {
                if (placeId.equals("end_portal")) {
                    placeState = Blocks.END_PORTAL.getDefaultState();
                } else if (placeId.equals("end_gateway")) {
                    placeState = Blocks.END_GATEWAY.getDefaultState();
                }

            } else if (this.stackIsItem(stack, Items.PURPLE_WOOL)) {
                if (placeId.equals("nether_portal")) {
                    placeState = Blocks.NETHER_PORTAL.getDefaultState()
                            .with(NetherPortalBlock.AXIS, context.getPlayerFacing().rotateYClockwise().getAxis());
                }

            } else if (this.stackIsItem(stack, Items.PISTON)) {
                if (placeId.equals("moving_piston")) {
                    placeState = Blocks.MOVING_PISTON.getDefaultState();
                }

            }
            if (placeState != null) {
                world.setBlockState(pos, placeState, 2);
                callback.setReturnValue(placeState);
            }
        }
    }


    public boolean stackIsItem(ItemStack stack, Item item) {
        return stack.isOf(item);
    }

}
