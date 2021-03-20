package yeelp.distinctdamagedescriptions.integration.hwyla;

import java.util.List;
import javax.annotation.Nonnull;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;
import yeelp.distinctdamagedescriptions.util.TooltipUtils;
import yeelp.distinctdamagedescriptions.util.lib.KeyHelper;

public class EntityHandler implements IWailaEntityProvider {
    public EntityHandler() { }

    private static final Style GRAY = new Style().setColor(TextFormatting.GRAY);
    private static final Style YELLOW = new Style().setColor(TextFormatting.YELLOW);

    private static final ITextComponent mobResistTooltip = new TextComponentTranslation("tooltips.distinctdamagedescriptions.mobresistances").setStyle(GRAY);
    private static final ITextComponent ctrlTooltip = new TextComponentTranslation("keys.distinctdamagedescriptions.ctrl").setStyle(YELLOW);
    private static final ITextComponent notGenerated = new TextComponentTranslation("tooltips.distinctdamagedescriptions.notgenerated").setStyle(new Style().setColor(TextFormatting.GOLD).setBold(true));

    /**
     * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).</br>
     * Will only be called if the implementing class is registered via {@link IWailaRegistrar#registerBodyProvider}.</br>
     * You are supposed to always return the modified input currenttip.</br>
     * <p>
     * You may return null if you have not registered this as a body provider. However, you should return the provided list
     * to be safe.
     * <p>
     * This method is only called on the client side. If you require data from the server, you should also implement
     * {@link #getNBTData(EntityPlayerMP, Entity, NBTTagCompound, World)} and add the data to the {@link NBTTagCompound}
     * there, which can then be read back using {@link IWailaDataAccessor#getNBTData()}. If you rely on the client knowing
     * the data you need, you are not guaranteed to have the proper values.
     *
     * @param entity     Current Entity scanned.
     * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be processed by other providers).
     * @param accessor   Contains most of the relevant information about the current environment.
     * @param config     Current configuration of Waila.
     * @return Modified input currenttip
     */
    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        boolean ctrlHeld = KeyHelper.isCtrlHeld();

        String entityName = EntityRegistry.getEntry(entity.getClass()).getRegistryName().toString();
        MobResistanceCategories mobCats = DDDConfigurations.mobResists.get(entityName);

        int index = 1;
        if(mobCats != null)
        {
            if(ctrlHeld)
            {
                currenttip.addAll(index, TooltipUtils.buildMobResistsTooltips(mobCats));
            }

            currenttip.add(index, mobResistTooltip.getFormattedText() + getCtrlText(ctrlHeld));
        } else {
            currenttip.add(index, notGenerated.getFormattedText());
        }

        return currenttip;
    }

    private static String getCtrlText(boolean held)
    {
        return getWhenNotHeld(held, ctrlTooltip);
    }

    private static String getWhenNotHeld(boolean held, ITextComponent tooltip)
    {
        return held ? "" : " "+tooltip.getFormattedText();
    }
}
