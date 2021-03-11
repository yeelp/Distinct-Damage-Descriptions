package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.HashMap;
import java.util.Map;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.registries.IDDDDamageTypeRegistry;
import yeelp.distinctdamagedescriptions.util.DamageTypeData;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

public final class DDDDamageTypes extends DDDBaseRegistry<DDDDamageType> implements IDDDDamageTypeRegistry
{
	private DataMap dataMap = new DataMap();
	private DDDDistributions extraMap = new DDDDistributions();
	
	private final class DataMap
	{
		private final Map<String, DDDDamageType> includeAllMap;
		private final Map<String, SourceMap> srcMap;
		
		protected DataMap()
		{
			this.includeAllMap = new NonNullMap<String, DDDDamageType>(DDDBuiltInDamageType.NORMAL);
			this.srcMap = new HashMap<String, SourceMap>();
		}
		
		final class SourceMap
		{
			private final Map<String, DDDDamageType> direct, indirect;
			SourceMap()
			{
				this(DDDBuiltInDamageType.NORMAL);
			}
			
			SourceMap(DDDDamageType defaultVal)
			{
				this.direct = new NonNullMap<String, DDDDamageType>(defaultVal);
				this.indirect = new NonNullMap<String, DDDDamageType>(defaultVal);
			}
			
			void update(DDDDamageType type, DamageTypeData data)
			{
				for(String s : data.getDirectSources())
				{
					this.direct.put(s, type);
				}
				for(String s : data.getIndirectSources())
				{
					this.indirect.put(s, type);
				}
			}
		}
		
		void update(DDDDamageType type, DamageTypeData data)
		{
			if(data.includeAll())
			{
				this.includeAllMap.put(data.getOriginalSource(), type);
			}
			if(!this.srcMap.containsKey(data.getOriginalSource())) //this.srcMap.putIfAbsent would still require the compute a new SourceMap every time, so it's less efficient
			{
				this.srcMap.put(data.getOriginalSource(), new SourceMap());
			}
			this.srcMap.get(data.getOriginalSource()).update(type, data);
		}
	}
	
	public DDDDamageTypes()
	{
		super(d -> d.getTypeName());
	}
	
	@Override
	public void init()
	{
		this.registerAll(DDDBuiltInDamageType.ACID, 
						 DDDBuiltInDamageType.BLUDGEONING, 
						 DDDBuiltInDamageType.COLD, 
						 DDDBuiltInDamageType.FIRE, 
						 DDDBuiltInDamageType.FORCE, 
						 DDDBuiltInDamageType.LIGHTNING, 
						 DDDBuiltInDamageType.NECROTIC, 
						 DDDBuiltInDamageType.NORMAL, 
						 DDDBuiltInDamageType.PIERCING, 
						 DDDBuiltInDamageType.POISON, 
						 DDDBuiltInDamageType.PSYCHIC, 
						 DDDBuiltInDamageType.RADIANT, 
						 DDDBuiltInDamageType.SLASHING, 
						 DDDBuiltInDamageType.THUNDER, 
						 DDDBuiltInDamageType.UNKNOWN);
	}
}
