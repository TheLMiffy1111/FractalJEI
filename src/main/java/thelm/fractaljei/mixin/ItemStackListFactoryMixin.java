package thelm.fractaljei.mixin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import de.dafuqs.fractal.interfaces.ICreativeTabParent;
import mezz.jei.library.plugins.vanilla.ingredients.ItemStackListFactory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStackListFactory.class)
public class ItemStackListFactoryMixin {

	@Shadow
	private static Logger LOGGER;

	@WrapOperation(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;buildContents(Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;)V"))
	private static void fractaljei$buildSubTabContents(CreativeModeTab tab, CreativeModeTab.ItemDisplayParameters displayParameters, Operation<Void> original) {
		if(tab instanceof ICreativeTabParent parent && !parent.fractal$getChildren().isEmpty()) {
			for(CreativeModeTab subTab : parent.fractal$getChildren()) {
				try {
					subTab.buildContents(displayParameters);
				}
				catch(Throwable e) {
					LOGGER.error("Item subgroup crashed while building contents. Items from this subgroup will be missing from the JEI ingredient list: {}", subTab.getDisplayName().getString(), e);
				}
			}
		}
		else {
			original.call(tab, displayParameters);
		}
	}

	@WrapOperation(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;getDisplayItems()Ljava/util/Collection;"))
	private static Collection<ItemStack> fractaljei$getSubTabContents(CreativeModeTab tab, Operation<Collection<ItemStack>> original) {
		if(tab instanceof ICreativeTabParent parent && !parent.fractal$getChildren().isEmpty()) {
			List<ItemStack> stacks = new ArrayList<>();
			for(CreativeModeTab subTab : parent.fractal$getChildren()) {
				try {
					stacks.addAll(subTab.getDisplayItems());
				}
				catch(Throwable e) {
					LOGGER.error("Item subgroup crashed while getting tab display items. Items from this subgroup will be missing from the JEI ingredient list: {}", subTab.getDisplayName().getString(), e);
				}
			}
			return stacks;
		}
		else {
			return original.call(tab);
		}
	}
}
