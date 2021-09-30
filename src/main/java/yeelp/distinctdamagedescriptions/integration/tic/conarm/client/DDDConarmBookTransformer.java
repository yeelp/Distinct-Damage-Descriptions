package yeelp.distinctdamagedescriptions.integration.tic.conarm.client;

import java.util.Map;

import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.SectionData;
import yeelp.distinctdamagedescriptions.integration.tic.DDDBookTransformer;

public class DDDConarmBookTransformer extends DDDBookTransformer {

	public DDDConarmBookTransformer() {
		super(BookTarget.CONARM);
	}

	@Override
	protected void transformBookWithRequestedSections(BookData book, Map<String, SectionData> requestedSections) {
		//TODO
	}
}
