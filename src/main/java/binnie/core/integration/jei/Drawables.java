package binnie.core.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class Drawables {
	private static Drawables INSTANCE;

	public static Drawables getDrawables(IGuiHelper guiHelper) {
		if (INSTANCE == null) {
			INSTANCE = new Drawables(guiHelper);
		}
		return INSTANCE;
	}

	public static final ResourceLocation guiTank = new ResourceLocation("binniecore:textures/gui/craftgui-slots.png");
	public static final ResourceLocation guiArrow = new ResourceLocation("binniecore:textures/gui/craftgui-panels.png");

	private final IGuiHelper guiHelper;
	private final IDrawable tank;
	private final IDrawable tankOverlay;
	private final IDrawable arrow;
	private final IDrawableStatic arrowWhite;

	private Drawables(IGuiHelper guiHelper) {
		this.guiHelper = guiHelper;
		this.tank = guiHelper.createDrawable(guiTank, 8, 28, 18, 60);
		this.tankOverlay = guiHelper.createDrawable(guiTank, 33, 29, 16, 58);
		this.arrow = guiHelper.createDrawable(guiArrow, 191, 79, 14, 10);
		this.arrowWhite = guiHelper.createDrawable(guiArrow, 207, 79, 14, 10);
	}

	@Nonnull
	public IDrawable getTank() {
		return tank;
	}

	@Nonnull
	public IDrawable getTankOverlay() {
		return tankOverlay;
	}

	@Nonnull
	public IDrawable getArrow() {
		return arrow;
	}

	@Nonnull
	public IDrawableStatic getArrowWhite() {
		return arrowWhite;
	}

	public IDrawableAnimated createArrowAnimated(int ticksPerCycle) {
		return guiHelper.createAnimatedDrawable(arrowWhite, ticksPerCycle, IDrawableAnimated.StartDirection.LEFT, false);
	}
}