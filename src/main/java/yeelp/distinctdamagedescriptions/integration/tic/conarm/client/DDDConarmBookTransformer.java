package yeelp.distinctdamagedescriptions.integration.tic.conarm.client;

import java.util.Map;
import java.util.Objects;

import c4.conarm.lib.book.ArmoryBook;
import c4.conarm.lib.materials.ArmorMaterialType;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.SectionData;
import yeelp.distinctdamagedescriptions.integration.tic.DDDBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.TiCBookTranslator;

public class DDDConarmBookTransformer extends DDDBookTransformer {

	private static final String DDD_ABOUT = "distributions";

	public DDDConarmBookTransformer() {
		super(new BookTarget("conarm", ArmoryBook.INSTANCE) {

			@Override
			protected TiCBookTranslator getBookTranslator() {
				return TiCBookTranslator.CONARM;
			}
		}, DDD_ABOUT);
	}

	@Override
	protected void transformBookWithRequestedSections(BookData book, Map<String, SectionData> requestedSections) {
		if(requestedSections.containsKey(DDD_ABOUT)) {
			SectionData dddSection = Objects.requireNonNull(requestedSections.get(DDD_ABOUT));
			createListingPage(dddSection, (section, listing) -> addInfluenceListing(section, ContentConarmMaterialInfluence::new, (m) -> m.hasStats(ArmorMaterialType.PLATES)).ifPresent((p) -> listing.addEntry(this.getTranslator().getTranslator().translate("materialListingTitle"), p)));
			book.sections.remove(book.sections.indexOf(dddSection));
			book.sections.add(dddSection);
		}
	}
}
