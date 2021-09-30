package yeelp.distinctdamagedescriptions.integration.tic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import c4.conarm.lib.book.ArmoryBook;
import slimeknights.mantle.client.book.BookTransformer;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.tconstruct.library.book.TinkerBook;

public abstract class DDDBookTransformer extends BookTransformer {

	public enum BookTarget {
		TINKERS("tinkers", TinkerBook.INSTANCE),
		CONARM("conarm", ArmoryBook.INSTANCE);

		private final BookData instance;
		private final FileRepository repo;
		private boolean registered = false;

		private BookTarget(String repoID, BookData instance) {
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
	}
	
	private final BookTarget target;
	private final String[] requestedSections;
	
	public DDDBookTransformer(BookTarget target, String...requestedSections) {
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
}
