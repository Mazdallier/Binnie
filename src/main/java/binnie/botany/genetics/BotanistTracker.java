package binnie.botany.genetics;

import binnie.Binnie;
import binnie.botany.api.IBotanistTracker;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IIndividual;
import forestry.core.genetics.BreedingTracker;
import net.minecraft.entity.player.EntityPlayer;

public class BotanistTracker extends BreedingTracker implements IBotanistTracker {
	public BotanistTracker(final String s) {
		super(s, "NORMAL");
	}

	@Override
	public void registerPickup(final IIndividual individual) {
	}

	@Override
	protected IBreedingTracker getBreedingTracker(final EntityPlayer player) {
		return Binnie.GENETICS.getFlowerRoot().getBreedingTracker(player.world, player.getGameProfile());
	}

	@Override
	protected String speciesRootUID() {
		return "rootFlowers";
	}
}
