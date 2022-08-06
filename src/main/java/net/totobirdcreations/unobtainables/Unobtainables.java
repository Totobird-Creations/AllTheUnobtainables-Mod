package net.totobirdcreations.unobtainables;

import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@Environment(EnvType.CLIENT)
public class Unobtainables implements ClientModInitializer {

	public static final String ID     = "unobtainables";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static final ArrayList<Identifier> rememberedStructureNames = new ArrayList<>();

	public static final ItemGroup GROUP_BASIC = FabricItemGroupBuilder.create(new Identifier(ID, "basic"))
			.icon(() -> new ItemStack(Items.BARRIER))
			.appendItems(Unobtainables::getBasicStacks)
			.build();
	public static final ItemGroup GROUP_STRUCTURE = FabricItemGroupBuilder.create(new Identifier(ID, "structure"))
			.icon(() -> new ItemStack(Items.STRUCTURE_BLOCK))
			.appendItems(Unobtainables::getStructureStacks)
			.build();
	public static final ItemGroup GROUP_HEAD = FabricItemGroupBuilder.create(new Identifier(ID, "head"))
			.icon(() -> new ItemStack(Items.PLAYER_HEAD))
			.appendItems(Unobtainables::getHeadStacks)
			.build();




	public static void getBasicStacks(List<ItemStack> stacks) {
		stacks.add(newStack(Items.BARRIER));
		stacks.add(newStack(Items.SPAWNER));
		addSpecialStacks(stacks);
		addCommandBlockStacks(stacks);
		addLightBlockStacks(stacks);
		stacks.add(newStack(Items.WRITTEN_BOOK));
		stacks.add(newStack(Items.ENCHANTED_BOOK));
		stacks.add(newStack(Items.KNOWLEDGE_BOOK));
		stacks.add(newStack(Items.DRAGON_EGG));
		stacks.add(newStack(Items.POTION));
		stacks.add(newStack(Items.SPLASH_POTION));
		stacks.add(newStack(Items.LINGERING_POTION));
		stacks.add(newStack(Items.TIPPED_ARROW));
		stacks.add(newStack(Items.DEBUG_STICK));
	}

	public static void addSpecialStacks(List<ItemStack> stacks) {
		ItemStack stack;
		stack = newStack(Items.BLACK_WOOL);
		stack.getOrCreateNbt().putString("specialPlacement", "end_portal");
		putName(stack, "block.minecraft.end_portal");
		//putGlint(stack);
		stacks.add(stack);
		stack = newStack(Items.BLACK_WOOL);
		stack.getOrCreateNbt().putString("specialPlacement", "end_gateway");
		putName(stack, "block.minecraft.end_gateway");
		//putGlint(stack);
		stacks.add(stack);
		stack = newStack(Items.PURPLE_WOOL);
		stack.getOrCreateNbt().putString("specialPlacement", "nether_portal");
		putName(stack, "block.minecraft.nether_portal");
		//putGlint(stack);
		stacks.add(stack);
		stack = newStack(Items.PISTON);
		stack.getOrCreateNbt().putString("specialPlacement", "moving_piston");
		putName(stack, "block.minecraft.moving_piston");
		//putGlint(stack);
		stacks.add(stack);
		stack = newStack(Items.ORANGE_WOOL);
		stack.getOrCreateNbt().putString("specialPlacement", "fire");
		putName(stack, "block.minecraft.fire");
		//putGlint(stack);
		stacks.add(stack);
		stack = newStack(Items.LIGHT_BLUE_WOOL);
		stack.getOrCreateNbt().putString("specialPlacement", "soul_fire");
		putName(stack, "block.minecraft.soul_fire");
		//putGlint(stack);
		stacks.add(stack);
	}

	public static void addCommandBlockStacks(List<ItemStack> stacks) {
		addCommandBlockStacks(stacks, Items.COMMAND_BLOCK);
		addCommandBlockStacks(stacks, Items.CHAIN_COMMAND_BLOCK);
		addCommandBlockStacks(stacks, Items.REPEATING_COMMAND_BLOCK);
		stacks.add(newStack(Items.COMMAND_BLOCK_MINECART));
	}
	public static void addCommandBlockStacks(List<ItemStack> stacks, Item item) {
		ItemStack stack;
		stack = newStack(item);
		stacks.add(stack);
		NbtCompound blockState = new NbtCompound();
		blockState.putString("conditional", "true");
		stack = newStack(item);
		putNameNote(stack, "advMode.mode.conditional");
		putBlockState(stack, "conditional", "true");
		stacks.add(stack);
	}


	public static void addLightBlockStacks(List<ItemStack> stacks) {
		for (int i = 0; i < 16; i++) {
			ItemStack stack = newStack(Items.LIGHT);
			putBlockState(stack, "level", String.valueOf(i));
			stacks.add(stack);
		}
	}


	public static void getStructureStacks(List<ItemStack> stacks) {
		stacks.add(getStructureBlockStack("LOAD"));
		stacks.add(getStructureBlockStack("CORNER"));
		stacks.add(getStructureBlockStack("SAVE"));
		stacks.add(getStructureBlockStack("DATA"));
		stacks.add(newStack(Items.STRUCTURE_VOID));
		stacks.add(newStack(Items.JIGSAW));
		while (stacks.size() < 9) {
			stacks.add(ItemStack.EMPTY);
		}
		for (String mode : new String[]{"LOAD", "CORNER", "SAVE"}) {
			for (Identifier name : rememberedStructureNames) {
				ItemStack stack = getStructureBlockStack(mode);
				putLore(stack, Text.literal(name.toString()));
				putBlockEntity(stack, "name", name.toString());
				stacks.add(stack);
			}
			for (int i = 0; i < 9 - rememberedStructureNames.size(); i++) {
				stacks.add(ItemStack.EMPTY);
			}
		}
	}
	public static ItemStack getStructureBlockStack(String mode) {
		ItemStack stack = newStack(Items.STRUCTURE_BLOCK);
		putNameNote(stack, "structure_block.mode." + mode.toLowerCase());
		putBlockState(stack, "mode", mode.toLowerCase());
		putBlockEntity(stack, "mode", mode.toUpperCase());
		return stack;
	}


	public static void getHeadStacks(List<ItemStack> stacks) {
		MinecraftServer server = MinecraftClient.getInstance().getServer();
		if (server != null) {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				ItemStack stack = newStack(Items.PLAYER_HEAD);
				stack.getOrCreateNbt().putString("SkullOwner", player.getName().getString());
				stacks.add(stack);
			}
		}
	}


	public static ItemStack newStack(Item item) {
		return new ItemStack(item);
	}

	public static void putNameNote(ItemStack stack, String text) {
		putNameNote(stack, Text.translatable(text));
	}
	public static void putNameNote(ItemStack stack, MutableText text) {
		putName(stack, stack.getName().copy().append(" (").append(text).append(")"));
	}
	public static void putName(ItemStack stack, String name) {
		putName(stack, Text.translatable(name));
	}
	public static void putName(ItemStack stack, MutableText name) {
		name.setStyle(name.getStyle()
				.withItalic(false)
		);
		stack.setCustomName(name);
	}
	public static void putLore(ItemStack stack, MutableText lore) {
		lore = lore.setStyle(lore.getStyle()
				.withItalic(false)
				.withColor(Formatting.GRAY)
		);
		NbtList list = new NbtList();
		list.add(NbtString.of(Text.Serializer.toJson(lore).toString()));
		stack.getOrCreateSubNbt("display").put("Lore", list);
	}
	public static void putBlockState(ItemStack stack, String key, String value) {
		stack.getOrCreateSubNbt("BlockStateTag").putString(key, value);
	}
	public static void putBlockEntity(ItemStack stack, String key, String value) {
		stack.getOrCreateSubNbt("BlockEntityTag").putString(key, value);
	}
	public static void putGlint(ItemStack stack) {
		NbtList list = new NbtList();
		list.add(new NbtCompound());
		stack.getOrCreateNbt().put("Enchantments", list);
	}


	public static void rememberStructureName(Identifier name) {
		if (name.getPath().replace(" ", "").length() > 0) {
			rememberedStructureNames.remove(name);
			rememberedStructureNames.add(0, name);
			while (rememberedStructureNames.size() > 9) {
				rememberedStructureNames.remove(rememberedStructureNames.size() - 1);
			}
		}
	}


	@Override
	public void onInitializeClient() {

		ModelPredicateProviderRegistry.register(Items.STRUCTURE_BLOCK, new Identifier("mode"), (stack, world, entity, i) -> switch (
				stack.getOrCreateSubNbt("BlockEntityTag").getString("mode").toLowerCase()
		) {
			case "load"   -> 0.1f;
			case "corner" -> 0.2f;
			case "save"   -> 0.3f;
			case "data"   -> 0.4f;
			default       -> 0.0f;
		});
		ModelPredicateProviderRegistry.register(Items.COMMAND_BLOCK, new Identifier("conditional"), (stack, world, entity, i) ->
				stack.getOrCreateSubNbt("BlockStateTag").getString("conditional").equalsIgnoreCase("true") ? 1.0f : 0.0f
		);
		ModelPredicateProviderRegistry.register(Items.CHAIN_COMMAND_BLOCK, new Identifier("conditional"), (stack, world, entity, i) ->
				stack.getOrCreateSubNbt("BlockStateTag").getString("conditional").equalsIgnoreCase("true") ? 1.0f : 0.0f
		);
		ModelPredicateProviderRegistry.register(Items.REPEATING_COMMAND_BLOCK, new Identifier("conditional"), (stack, world, entity, i) ->
				stack.getOrCreateSubNbt("BlockStateTag").getString("conditional").equalsIgnoreCase("true") ? 1.0f : 0.0f
		);
		ModelPredicateProviderRegistry.register(Items.BLACK_WOOL, new Identifier("special_placement"), (stack, world, entity, i) -> switch(
				stack.getOrCreateNbt().getString("specialPlacement").toLowerCase()
		) {
			case "end_portal"  -> 0.01f;
			case "end_gateway" -> 0.02f;
			default            -> 0.0f;
		});
		ModelPredicateProviderRegistry.register(Items.PURPLE_WOOL, new Identifier("special_placement"), (stack, world, entity, i) ->
				stack.getOrCreateNbt().getString("specialPlacement").equalsIgnoreCase("nether_portal") ? 0.01f : 0.0f
		);
		ModelPredicateProviderRegistry.register(Items.PISTON, new Identifier("special_placement"), (stack, world, entity, i) ->
				stack.getOrCreateNbt().getString("specialPlacement").equalsIgnoreCase("moving_piston") ? 0.01f : 0.0f
		);
		ModelPredicateProviderRegistry.register(Items.ORANGE_WOOL, new Identifier("special_placement"), (stack, world, entity, i) ->
				stack.getOrCreateNbt().getString("specialPlacement").equalsIgnoreCase("fire") ? 0.01f : 0.0f
		);
		ModelPredicateProviderRegistry.register(Items.LIGHT_BLUE_WOOL, new Identifier("special_placement"), (stack, world, entity, i) ->
				stack.getOrCreateNbt().getString("specialPlacement").equalsIgnoreCase("soul_fire") ? 0.01f : 0.0f
		);

		LOGGER.info("Initialised.");
	}

}
