package yeelp.distinctdamagedescriptions.util.lib;

public enum FilterListType {
	WHITELIST {
		@Override
		public <T> boolean checkMob(T id, T[] list) {
			for(T t : list) {
				if(id.equals(t)) {
					return true;
				}
			}
			return false;
		}
	},
	BLACKLIST {
		@Override
		public <T> boolean checkMob(T id, T[] list) {
			return !WHITELIST.checkMob(id, list);
		}
	};
	
	public abstract <T> boolean checkMob(T id, T[] list);
}
