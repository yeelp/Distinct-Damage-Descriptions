package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.content.ContentListing;
import slimeknights.tconstruct.library.book.content.ContentModifier;
import slimeknights.tconstruct.library.modifiers.IModifier;
import yeelp.distinctdamagedescriptions.integration.tic.DDDBookTransformer;

public final class DDDTinkersBookTransformer extends DDDBookTransformer {

	private static final String MODS = "modifiers", DDD_MODS = "dddmodifiers", DDD_ABOUT = "distributions";
	
	public DDDTinkersBookTransformer() {
		super(BookTarget.TINKERS, MODS, DDD_MODS, DDD_ABOUT);
	}
	
	@Override
	protected void transformBookWithRequestedSections(BookData book, Map<String, SectionData> requestedSections) {
		if(requestedSections.containsKey(MODS) && requestedSections.containsKey(DDD_MODS)) {
			addModifiers(Objects.requireNonNull(book), Objects.requireNonNull(requestedSections.get(MODS)), Objects.requireNonNull(requestedSections.get(DDD_MODS)));
		}
		if(requestedSections.containsKey(DDD_ABOUT)) {
			addToolDists(Objects.requireNonNull(requestedSections.get(DDD_ABOUT)));			
		}
	}

	private final static void addToolDists(SectionData section) {
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
		TinkerRegistry.getTools().forEach((tool) -> {
			PageData page = new PageData(true);
			page.content = new ContentToolDistribution(page, tool);
			page.parent = section;
			page.name = "ddd_" + tool.getIdentifier();
			section.pages.add(page);
			listing.addEntry(tool.getLocalizedToolName(), page);
		});
		section.pages.add(0, listingPage);
	}

	private final static void addModifiers(@Nonnull BookData book, @Nonnull SectionData mods, @Nonnull SectionData dddmods) {
		PageData firstPage = mods.pages.get(0);
		Optional<ContentListing> listing = Optional.empty();
		if(firstPage.content instanceof ContentListing) {
			listing = Optional.of((ContentListing) firstPage.content);
		}
		for(PageData page : dddmods.pages) {
			page.parent = mods;
			mods.pages.add(page);
			if(page.content instanceof ContentModifier) {
				ContentModifier contentModifier = (ContentModifier) page.content;
				IModifier mod = TinkerRegistry.getModifier(contentModifier.modifierName);
				if(mod != null) {
					page.name = "ddd_" + mod.getIdentifier();
					listing.ifPresent((l) -> l.addEntry(mod.getLocalizedName(), page));
				}
			}
		}
		dddmods.pages.clear();
		book.sections.remove(dddmods);
	}
}
