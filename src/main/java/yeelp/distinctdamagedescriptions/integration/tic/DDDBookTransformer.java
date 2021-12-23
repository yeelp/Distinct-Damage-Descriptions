package yeelp.distinctdamagedescriptions.integration.tic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

import slimeknights.mantle.client.book.BookTransformer;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.data.element.ImageData;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.mantle.client.gui.book.element.ElementImage;
import slimeknights.mantle.client.gui.book.element.ElementItem;
import slimeknights.mantle.client.gui.book.element.SizedBookElement;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.content.ContentListing;
import slimeknights.tconstruct.library.book.content.ContentPageIconList;
import slimeknights.tconstruct.library.materials.Material;

public abstract class DDDBookTransformer extends BookTransformer {

	protected static abstract class BookTarget {
		private final BookData instance;
		private final FileRepository repo;
		private boolean registered = false;

		protected BookTarget(String repoID, BookData instance) {
			this.instance = instance;
			this.repo = new FileRepository("distinctdamagedescriptions:" + repoID + "/book");
		}

		void register() {
			if(!this.registered) {
				this.instance.addRepository(this.repo);
				this.registered = true;
			}
		}

		void addTransformer(BookTransformer transformer) {
			this.instance.addTransformer(transformer);
		}
		
		protected abstract TiCBookTranslator getBookTranslator();
	}

	private final BookTarget target;
	private final String[] requestedSections;

	public DDDBookTransformer(BookTarget target, String... requestedSections) {
		this.target = target;
		this.requestedSections = requestedSections;
		Arrays.sort(this.requestedSections);
	}

	public void register() {
		this.target.register();
		this.target.addTransformer(this);
	}

	@Override
	public final void transform(BookData book) {
		this.transformBookWithRequestedSections(book, this.getRequestedSections(book));
	}
	
	protected final TiCBookTranslator getTranslator() {
		return this.target.getBookTranslator();
	}

	protected abstract void transformBookWithRequestedSections(BookData book, Map<String, SectionData> requestedSections);

	private Map<String, SectionData> getRequestedSections(BookData target) {
		Map<String, SectionData> map = new HashMap<String, SectionData>();
		for(SectionData section : target.sections) {
			int index = Arrays.binarySearch(this.requestedSections, section.name);
			if(index >= 0) {
				map.put(this.requestedSections[index], section);
				if(map.values().size() == this.requestedSections.length) {
					break;
				}
			}
		}
		return map;
	}

	protected final Optional<PageData> addInfluenceListing(SectionData data, BiFunction<PageData, Material, ContentMaterialInfluence> generator, Predicate<Material> isValidMaterial) {
		ListIterator<ContentPageIconList> iter = ContentPageIconList.getPagesNeededForItemCount(TinkerRegistry.getAllMaterials().size(), data, this.target.getBookTranslator().getTranslator().translate("materialListingTitle")).listIterator();
		if(!iter.hasNext()) {
			return Optional.empty();
		}
		PageData ret = data.pages.get(data.pages.size() - 1);
		ContentPageIconList overview = iter.next();
		for(Material m : TinkerRegistry.getAllMaterials().stream().filter(Predicates.<Material>and(ImmutableList.of(Material::hasItems, isValidMaterial::test, Predicates.not(Material::isHidden)))).collect(Collectors.toList())) {
			PageData page = new PageData(true);
			page.content = generator.apply(page, m);
			page.parent = data;
			page.name = "ddd_" + m.identifier;
			page.load();
			
			data.pages.add(page);
			SizedBookElement icon;
			if(m.getRepresentativeItem() != null) {
				icon = new ElementItem(0, 0, 1f, m.getRepresentativeItem());
			}
			else {
				icon = new ElementImage(ImageData.MISSING);
			}

			while(!overview.addLink(icon, m.getLocalizedNameColored(), page)) {
				overview = iter.next();
			}
		}
		return Optional.of(ret);
	}
	
	protected static final void createListingPage(SectionData section, BiConsumer<SectionData, ContentListing> populater) {
		final ContentListing listing = new ContentListing();
		final PageData listingPage = new PageData(true);
		listing.title = section.translate(section.name);
		listing.parent = listingPage;
		listingPage.content = listing;
		listingPage.parent = section;
		section.pages.forEach((p) -> {
			if(!p.getTitle().endsWith("contd")) {
				listing.addEntry(section.translate(p.getTitle()), p);
			}
		});
		populater.accept(section, listing);
		section.pages.add(0, listingPage);
	}
}
