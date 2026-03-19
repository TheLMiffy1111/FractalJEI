package thelm.fractaljei.mixin;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import de.dafuqs.fractal.quack.ItemGroupParent;
import mezz.jei.library.plugins.vanilla.ingredients.ItemStackListFactory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStackListFactory.class)
public class ItemStackListFactoryMixin {

	@Shadow
	private static Logger LOGGER;

	@WrapOperation(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;fillItemList(Lnet/minecraft/core/NonNullList;)V"))
	private static void fractaljei$appendSubTabContents(CreativeModeTab tab, NonNullList<ItemStack> stacks, Operation<Void> original) {
		if(tab instanceof ItemGroupParent parent && !parent.fractal$getChildren().isEmpty()) {
			for(CreativeModeTab subTab : parent.fractal$getChildren()) {
				try {
					subTab.fillItemList(stacks);
				}
				catch(Throwable e) {
					LOGGER.error("Item subgroup crashed while getting items. Some items from this subgroup will be missing from the ingredient list. {}", subTab.getDisplayName().getString(), e);
				}
			}
		}
		else {
			original.call(tab, stacks);
		}
	}
}
