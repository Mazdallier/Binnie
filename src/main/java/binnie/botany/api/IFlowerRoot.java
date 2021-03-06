package binnie.botany.api;

import com.mojang.authlib.GameProfile;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.ISpeciesRoot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface IFlowerRoot extends ISpeciesRoot {
	@Override
	@Nullable
	IFlower getMember(ItemStack memberStack);

	@Override
	IFlower templateAsIndividual(IAllele[] template);

	@Override
	IFlower templateAsIndividual(IAllele[] templateFirst, IAllele[] templateSecond);

	@Override
	IFlowerGenome templateAsGenome(IAllele[] template);

	@Override
	IFlowerGenome templateAsGenome(IAllele[] templateFrist, IAllele[] templateSecond);

	@Override
	IBotanistTracker getBreedingTracker(World world, @Nullable GameProfile profile);

	@Override
	List<? extends IFlowerMutation> getMutations(boolean shuffle);

	@Override
	@Nullable
	EnumFlowerStage getType(ItemStack itemStack);

	IFlower getFlower(IFlowerGenome genome);

	void addConversion(ItemStack memberStack, IAllele[] template);

	@Nullable
	IFlower getConversion(ItemStack memberStack);

	Collection<IColourMix> getColourMixes(boolean p0);

	void registerColourMix(IColourMix colourMix);
}
