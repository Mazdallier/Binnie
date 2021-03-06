package binnie.genetics.machine.isolator;

import binnie.core.genetics.Gene;
import binnie.core.machines.Machine;
import binnie.core.machines.MachineUtil;
import binnie.core.machines.inventory.IChargedSlots;
import binnie.core.machines.power.ComponentProcessSetCost;
import binnie.core.machines.power.ErrorState;
import binnie.core.machines.power.IProcess;
import binnie.genetics.Genetics;
import binnie.genetics.item.ItemSequence;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IChromosomeType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class IsolatorLogic extends ComponentProcessSetCost implements IProcess {
	public static final float ENZYME_PER_PROCESS = 0.5f;
	public static final int ETHANOL_PER_PROCESS = 10;
	public static final float TARGET_LOSS_CHANCE = 0.05f;

	public IsolatorLogic(final Machine machine) {
		super(machine, 192000, 4800);
	}

	@Override
	public ErrorState canWork() {
		if (this.getUtil().isSlotEmpty(Isolator.SLOT_TARGET)) {
			return new ErrorState.NoItem(Genetics.proxy.localise("machine.machine.isolator.errors.no.individual.desc"), Isolator.SLOT_TARGET);
		}
		if (!this.getUtil().isSlotEmpty(Isolator.SLOT_RESULUT)) {
			return new ErrorState.NoSpace(Genetics.proxy.localise("machine.machine.isolator.errors.no.room.desc"), Isolator.SLOT_FINISHED);
		}
		if (this.getUtil().isSlotEmpty(Isolator.SLOT_SEQUENCER_VIAL)) {
			return new ErrorState.NoItem(Genetics.proxy.localise("machine.machine.isolator.errors.no.empty.sequencer.desc"), Isolator.SLOT_SEQUENCER_VIAL);
		}
		return super.canWork();
	}

	@Override
	public ErrorState canProgress() {
		if (!this.getUtil().liquidInTank(Isolator.TANK_ETHANOL, ETHANOL_PER_PROCESS)) {
			return new ErrorState.InsufficientLiquid(Genetics.proxy.localise("machine.machine.isolator.errors.insufficient.ethanol.desc"), Isolator.TANK_ETHANOL);
		}
		if (this.getUtil().getSlotCharge(Isolator.SLOT_ENZYME) == 0.0f) {
			return new ErrorState.NoItem(Genetics.proxy.localise("machine.errors.no.enzyme.desc"), Isolator.SLOT_ENZYME);
		}
		return super.canProgress();
	}

	@Override
	protected void onFinishTask() {
		super.onFinishTask();
		final Random rand = this.getMachine().getWorld().rand;
		MachineUtil util = this.getUtil();
		final ISpeciesRoot root = AlleleManager.alleleRegistry.getSpeciesRoot(util.getStack(Isolator.SLOT_TARGET));
		if (root == null) {
			return;
		}
		final IIndividual individual = root.getMember(util.getStack(Isolator.SLOT_TARGET));
		if (individual == null) {
			return;
		}

		IChromosomeType[] karyotype = root.getKaryotype();
		IChromosomeType chromosome = karyotype[rand.nextInt(karyotype.length)];
		IGenome genome = individual.getGenome();
		IAllele allele = rand.nextBoolean() ? genome.getActiveAllele(chromosome) : genome.getInactiveAllele(chromosome);
		Gene gene = Gene.create(allele, chromosome, root);

		final ItemStack serum = ItemSequence.create(gene);
		util.setStack(Isolator.SLOT_RESULUT, serum);
		util.decreaseStack(Isolator.SLOT_SEQUENCER_VIAL, 1);
		if (rand.nextFloat() < TARGET_LOSS_CHANCE) {
			util.decreaseStack(Isolator.SLOT_TARGET, 1);
		}
		util.drainTank(Isolator.TANK_ETHANOL, ETHANOL_PER_PROCESS);
	}

	@Override
	protected void onTickTask() {
		IChargedSlots chargedSlots = this.getMachine().getInterface(IChargedSlots.class);
		chargedSlots.alterCharge(Isolator.SLOT_ENZYME, -ENZYME_PER_PROCESS * this.getProgressPerTick() / 100.0f);
	}
}
