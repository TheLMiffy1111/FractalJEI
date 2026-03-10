package thelm.fractaljei;

import java.util.List;

import de.dafuqs.fractal.quack.ItemGroupParent;
import de.dafuqs.fractal.quack.SubTabLocation;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.CreativeModeTab;

public class SubTabExtraAreaHandler implements IGuiContainerHandler<CreativeModeInventoryScreen> {

	@Override
	public List<Rect2i> getGuiExtraAreas(CreativeModeInventoryScreen containerScreen) {
		CreativeModeTab selected = CreativeModeTab.TABS[containerScreen.getSelectedTab()];
		if(selected instanceof ItemGroupParent parent &&
				containerScreen instanceof SubTabLocation stl &&
				parent.fractal$getChildren() != null &&
				!parent.fractal$getChildren().isEmpty()) {
			return List.of(new Rect2i(stl.fractal$getX(), stl.fractal$getY(), stl.fractal$getW(), stl.fractal$getH()));
		}
		return List.of();
	}
}
