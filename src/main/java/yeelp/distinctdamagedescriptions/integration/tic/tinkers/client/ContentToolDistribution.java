package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.mantle.client.gui.book.element.BookElement;
import slimeknights.mantle.client.gui.book.element.ElementText;
import slimeknights.tconstruct.library.book.content.ContentTool;
import slimeknights.tconstruct.library.tools.ToolCore;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.TiCConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

@SideOnly(Side.CLIENT)
public class ContentToolDistribution extends ContentTool {
	
	public static final transient String ID = "toolDist";
	private final transient IDamageDistribution dist;
	private final transient float variability;

	public ContentToolDistribution() {
		super();
		this.dist = null;
		this.variability = 0;
	}

	public ContentToolDistribution(PageData parent, ToolCore tool) {
		super(tool);
		this.parent = parent;
		String key = YResources.getRegistryString(tool);
		this.dist = DDDConfigurations.items.getOrFallbackToDefault(key).copy();
		this.variability = TiCConfigurations.toolBiasResistance.getOrFallbackToDefault(key);
		this.text = generateTextData(tool);
		this.properties = this.dist.getCategories().stream().sorted().map((t) -> new TextComponentTranslation("distinctdamagedescriptions.tinkers.book.distributions.entry", (int)(this.dist.getWeight(t)*100), t.getDisplayName()).getFormattedText()).collect(Collectors.toList()).toArray(this.properties);
		super.load();
	}
	
	private TextData[] generateTextData(ToolCore tool) {
		Queue<TextData> q = new LinkedList<TextData>();
		Tuple<IBookString, Optional<IBookString>> distPreferences = DistributionPreference.determinePreferences(tool, this.dist);
		q.add(new TextData(Flexibility.determineFlexibility(this.variability).toBookString()));
		q.add(TextData.LINEBREAK);
		q.add(new TextData(distPreferences.getFirst().toBookString() + distPreferences.getSecond().map((b) -> " " + b.toBookString()).orElse("")));
		q.add(TextData.LINEBREAK);
		q.add(new TextData(new TextComponentTranslation("distinctdamagedescriptions.tinkers.book.flexibility.measure", this.variability).getFormattedText()));
		return q.toArray(new TextData[q.size()]);
	}

	@Override
	public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
		super.build(book, list, rightSide);
		//Replace the properties title only, leave everything else as is.
		
		for(int i = 0; i < list.size(); i++) {
			BookElement element = list.get(i);
			if(element instanceof ElementText) {
				ElementText text = (ElementText) element;
				if(text.text[0].text.equals(this.parent.translate("tool.properties"))) {
					TextData data = new TextData(new TextComponentTranslation("distinctdamagedescriptions.tinkers.book.distributions.start").getFormattedText());
					data.underlined = true;
					list.set(i, new ElementText(text.x, text.y, text.width, text.height, data));
					break;
				}
			}
		}
	}
}
