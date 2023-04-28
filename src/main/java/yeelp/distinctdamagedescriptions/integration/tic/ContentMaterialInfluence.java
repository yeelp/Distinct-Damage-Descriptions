package yeelp.distinctdamagedescriptions.integration.tic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.mantle.client.gui.book.GuiBook;
import slimeknights.mantle.client.gui.book.element.BookElement;
import slimeknights.mantle.client.gui.book.element.ElementItem;
import slimeknights.mantle.client.gui.book.element.ElementText;
import slimeknights.tconstruct.library.book.content.ContentMaterial;
import slimeknights.tconstruct.library.book.elements.ElementTinkerItem;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.BlockCasting;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.common.block.BlockToolTable;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.Translations.Translator;

public abstract class ContentMaterialInfluence extends ContentMaterial {
	public static final String ID = "materialInfluence";

	protected static final transient DecimalFormat FORMATTER = new DecimalFormat("##.##%");
	private transient Material material;

	public ContentMaterialInfluence() {
		super(Material.UNKNOWN);
		this.material = Material.UNKNOWN;
	}

	public ContentMaterialInfluence(PageData parent, Material material) {
		super(material);
		this.parent = parent;
		this.material = material;
		super.load();
	}

	@Override
	public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
		// Adapt code from ContentMaterial but modify it slightly
		super.addTitle(list, this.material.getLocalizedNameColored(), true);
		this.addDisplayItems(list, rightSide ? GuiBook.PAGE_WIDTH - 18 : 0);
		int y = 15 + 5;
		int x = (rightSide ? 0 : 22) + 5;
		int w = GuiBook.PAGE_WIDTH - 5;
		List<TextData> data = Lists.newArrayList();
		this.getAdditionalTextData().forEach(data::add);
		TextData header = new TextData(this.getTranslator().translate("materialInfluenceStart"));
		header.underlined = true;
		data.add(header);
		data.add(TextData.LINEBREAK);
		this.getWeights().entrySet().stream().sorted(Comparator.comparing(Entry::getKey)).map((e) -> new TextData(this.getTranslator().translate("materialInfluenceEntry", TiCUtil.getDDDDamageTypeNameColoured(e.getKey()), FORMATTER.format(e.getValue())))).forEach((td) -> {
			data.add(new TextData("\u25CF "));
			data.add(td);
			data.add(TextData.LINEBREAK);
		});
		list.add(new ElementText(x, y, w, GuiBook.PAGE_HEIGHT, data));
	}

	private void addDisplayItems(List<BookElement> list, int x) {
		List<ElementItem> display = Lists.newArrayList();
		if(!this.material.getRepresentativeItem().isEmpty()) {
			display.add(new ElementTinkerItem(this.material.getRepresentativeItem()));
		}

		if(this.material.isCraftable()) {
			ItemStack partbuilder = new ItemStack(TinkerTools.toolTables, 1, BlockToolTable.TableTypes.PartBuilder.meta);
			ElementItem elementItem = new ElementTinkerItem(partbuilder);
			elementItem.tooltip = ImmutableList.of(this.parent.translate("material.craft_partbuilder"));
			display.add(elementItem);
		}
		if(this.material.isCastable()) {
			ItemStack basin = new ItemStack(TinkerSmeltery.castingBlock, 1, BlockCasting.CastingType.BASIN.getMeta());
			ElementItem elementItem = new ElementTinkerItem(basin);
			String text = this.parent.translate("material.craft_casting");
			elementItem.tooltip = ImmutableList.of(String.format(text, this.material.getFluid().getLocalizedName(new FluidStack(this.material.getFluid(), 0))));
			display.add(elementItem);
		}

		this.getRegistry().stream().filter(this::isValidPart).limit(9 - display.size()).map((p) -> new ElementTinkerItem(p.getItemstackWithMaterial(this.material))).forEach(display::add);
		Iterator<Integer> ys = Stream.iterate(10, (i) -> i + ElementItem.ITEM_SIZE_HARDCODED).iterator();
		display.forEach((e) -> {
			e.x = x;
			e.y = ys.next();
			e.scale = 1.0f;
			list.add(e);
		});
	}

	protected final Material getMaterial() {
		return this.material;
	}

	protected abstract Translator getTranslator();

	protected abstract Set<? extends IToolPart> getRegistry();

	protected abstract boolean isValidPart(IToolPart part);

	protected abstract Iterable<TextData> getAdditionalTextData();

	protected abstract Map<DDDDamageType, Float> getWeights();
}
