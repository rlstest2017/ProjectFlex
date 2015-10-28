package com.orange.flexoffice.business.gatewayapi.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.core.dataAccess.DualBaseOfSparseVector;
import com.orange.flexoffice.business.gatewayapi.core.dataAccess.SparseVectorSorted;
import com.orange.flexoffice.business.gatewayapi.core.dataAccess.SparseVectorTabular;
import com.orange.flexoffice.dao.gatewayapi.model.ReperioElement;
import com.orange.flexoffice.dao.gatewayapi.model.Stat;
import com.orange.flexoffice.dao.gatewayapi.model.data.Data;
import com.orange.flexoffice.dao.gatewayapi.model.support.SortedDoubleList;
import com.orange.flexoffice.dao.gatewayapi.model.support.SparseVector;
import com.orange.flexoffice.dao.gatewayapi.repository.data.DataOperations;
import com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.StatOperations;
import com.orange.flexoffice.dao.gatewayapi.repository.support.DataExtractor;
import com.orange.flexoffice.dao.gatewayapi.repository.support.StatExtractor;

/**
 * class for an item source like an item catalog or logs.
 * 
 * @author Camille Blin
 */
@Transactional
public class Source {
	private static Logger logger = Logger.getLogger(Source.class);

	private Cache rowSourceCache;
	private Cache columnSourceCache;

	private Map<String, Float> avgRowsRatings;
	private Map<String, Float> avgColumnsRatings;

	private DualBaseOfSparseVector base;
	private boolean isLoaded = false;
	
	private DataOperations<?> dataRepository;
	private StatOperations columnStatRepository;
	private StatOperations rowStatRepository;
	
	public Source() {}

	/**
	 * Create an item source from a table name in the DB.
	 * 
	 * @param table_name
	 *            the name of the table in the DB
	 * @throws WrongSourceTableFormatException
	 *             if the format of the table is incorrect
	 */
	public Source(int cacheDepth, 
			DataRepository<?> dataRepository,
			StatOperations columnStatRepository,
			StatOperations rowStatRepository) {
		
		base = new DualBaseOfSparseVector();
		avgRowsRatings = new HashMap<String, Float>();
		avgColumnsRatings = new HashMap<String, Float>();
		rowSourceCache = new Cache(cacheDepth);
		columnSourceCache = new Cache(cacheDepth);
		
		this.dataRepository = dataRepository;
		this.columnStatRepository = columnStatRepository;
		this.rowStatRepository = rowStatRepository;
	}

	/**
	 * Returns for each distinct id in the "row" column the average of ratings.
	 * 
	 * @return a map of <id, average>
	 */
	public Map<String, Float> getAvgRowsRatings() {
		return avgRowsRatings;
	}

	/**
	 * Returns for each distinct id in the "column" column the average of ratings.
	 *
	 * @return a map of <id, average>
	 */
	public Map<String, Float> getAvgColumnsRatings() {
		return avgColumnsRatings;
	}
	
	/**
	 * Returns a DualBaseOfSparseVector representation of this source.
	 * @return
	 */
	public DualBaseOfSparseVector getBase() {
		return base;
	}

	/**
	 * Returns for a couple of <columnId, rowId>, the associated rating.
	 * If no rating exist for this couple, it returns <code>null</code>.
	 * 
	 * @param columnId the "column" id
	 * @param rowId the "row" id
	 * 
	 * @return the rating or null.
	 */
	public Float getRating(String columnId, String rowId) {
		if (logger.isDebugEnabled()) {
			logger.debug("getRating :: columnId=" + columnId + " rowId=" + rowId);
		}
		
		Float result = null;
		SparseVector sv = getColumn(columnId);
		if (sv != null) {
			result = sv.getListObjects().get(rowId);
		}
		return result;
	}

	/**
	 * return the sparseVector for the columnId (1st column of DB Table)
	 * 
	 * @return SparseVector
	 */
	public SparseVector getColumn(String columnId) {
		SparseVector sv = columnSourceCache.getSV(columnId);

		if (sv == null) {
			Map<String, Float> listValue = new HashMap<String, Float>();

			List<Data> datas = dataRepository.findByColumnId(columnId);
			
			for (Data data : datas) {
				listValue.put(data.getRowId(), data.getRating());
			}
			
			if (!listValue.isEmpty()) {
				sv = new SparseVector(columnId, listValue);
				columnSourceCache.setSV(columnId, sv);
			} 
		}

		return sv;
	}

	public List<String> getColumnGT(SparseVector sv, float threshold) {

		Map<String, Float> ratings = sv.getListObjects();

		int i = 0;
		for (Float rating : ratings.values()) {
			if (rating >= threshold)
				i++;
		}

		SortedDoubleList<String> sdl = new SortedDoubleList<String>(i);

		for (Entry<String, Float> entry : ratings.entrySet()) {
			sdl.add(entry.getKey(), entry.getValue());
		}
		// ArrayList<String> result = new ArrayList<String>(sdl.getIds());
		// String[] result = sdl.getIds().toArray(new
		// String[sdl.getIds().size()]);
		return sdl.getIds();
	}

	/**
	 * return the sparseVector for the rowId (2nd column of the DB Table)
	 * 
	 * @return SparseVector
	 */
	public SparseVector getRow(String rowId) {

		SparseVector sv = rowSourceCache.getSV(rowId);

		if (sv == null) {
			Map<String, Float> listValue = new HashMap<String, Float>();
			List<Data> results = dataRepository.findByRowId(rowId);

			for (Data data : results) {
				listValue.put(data.getColumnId(), data.getRating());
			}
			
			if (!listValue.isEmpty()) {
				sv = new SparseVector(rowId, listValue);
				rowSourceCache.setSV(rowId, sv);
			}
		}
		return sv;

	}

	/**
	 * Return the nb best rows by avg
	 * 
	 * @param nb
	 *            = number of rows to return
	 * @return
	 */
	public List<ReperioElement> getBestRowsByAvg(int nb) {
		List<Stat> stats = rowStatRepository.findByBestAvg(nb);
		List<ReperioElement> elmts = new ArrayList<ReperioElement>();
		for (Stat stat : stats) {
			ReperioElement reperioElement = new ReperioElement(stat.getObjectId(), stat.getAverage());
			elmts.add(reperioElement);
		}
		return elmts;
	}
	
	/**
	 * Return the nb best rows by avg
	 * 
	 * @param nb
	 *            = number of rows to return
	 * @return
	 */
	public List<ReperioElement> getBestColumnsByAvg(int nb) {
		List<Stat> stats = columnStatRepository.findByBestAvg(nb);
		List<ReperioElement> elmts = new ArrayList<ReperioElement>();
		for (Stat stat : stats) {
			ReperioElement reperioElement = new ReperioElement(stat.getObjectId(), stat.getAverage());
			elmts.add(reperioElement);
		}
		return elmts;
	}

	/**
	 * Return every row identifiers in the DB
	 * 
	 * @return an array containing all the rowIds
	 */
	public String[] getRowIdsList() {
		List<String> results = rowStatRepository.findAllObjectIds();
		return results.toArray(new String[results.size()]);
	}

	/**
	 * Return every column identifiers in the DB
	 * 
	 * @return an array containing all the columnIds
	 */
	public String[] getColumnIdsList() {
		List<String> results = columnStatRepository.findAllObjectIds();
		return results.toArray(new String[results.size()]);
	}
	
	/**
	 * Return every column identifiers in the DB
	 * 
	 * @return an array containing all the columnIds
	 */
	public String[] getColumnIdsListRowConditions(SparseVector condition) {
		List<String> results = dataRepository.findColumnIdsRowConditions(condition.getListObjects().keySet());
		return results.toArray(new String[results.size()]);
	}

	/**
	 * Return the number of items in the DB.
	 * 
	 * @return number of items
	 */
	public int getNbRows() {
		return rowStatRepository.count().intValue();
	}

	/**
	 * Return the number of users in the DB.
	 * 
	 * @return number of users
	 */
	public int getNbColumns() {
		return columnStatRepository.count().intValue();
	}

	public int getNbRatings() {
		return dataRepository.count().intValue();
	}

	public Float getAvgRow(String rowId) {
		return avgRowsRatings.get(rowId);
	}

	public Float getAvgColumn(String columnId) {
		return avgColumnsRatings.get(columnId);
	}

	public void loadBase(boolean loadColumns, boolean loadRows) {
		if (!isLoaded) {
			base = getCore(loadColumns, loadRows);
			isLoaded = true;
		}
	}
	
	private DualBaseOfSparseVector getCore(final boolean loadColumns, final boolean loadRows) {
		// Initialize

		final DualBaseOfSparseVector dbosv = new DualBaseOfSparseVector();
		int usersNumber = 0;
		int itemsNumber = 0;
		// Get users, items and ratings numbers
		long time = System.currentTimeMillis();
		dbosv.getUserBase().initializeDataset(0);
		dbosv.getItemBase().initializeDataset(0);

		if(loadColumns){
			usersNumber = columnStatRepository.count().intValue();
			dbosv.getUserBase().initializeDataset(usersNumber);
	
			columnStatRepository.forEach(new StatExtractor() {
				@Override
				public void extractStat(Stat stat) {
					int uID = dbosv.getUserInternalKey(stat.getObjectId());
					dbosv.getUserBase().addSparseVector(uID , new SparseVectorTabular(stat.getCount()));
				}
			});
		}

		if (loadRows){
			itemsNumber = rowStatRepository.count().intValue();
			dbosv.getItemBase().initializeDataset(itemsNumber);
			
			rowStatRepository.forEach(new StatExtractor() {
				
				@Override
				public void extractStat(Stat stat) {
					int iID = dbosv.getItemInternalKey(stat.getObjectId());
					dbosv.getItemBase().addSparseVector(iID , new SparseVectorTabular(stat.getCount()));
					
				}
			});
		}

		// Load data
		dataRepository.forEach(new DataExtractor() {
			
			@Override
			public void extractData(Data data) {
				int uID = dbosv.getUserInternalKey(data.getColumnId());
				int iID = dbosv.getItemInternalKey(data.getRowId());
				float note = new Float(data.getRating());
				if(loadColumns) dbosv.getUser(uID).setValue(iID,note);
				if(loadRows) dbosv.getItem(iID).setValue(uID,note);
			}
		});
		

		// Order data for users
		
		if (loadColumns){
			Iterator<Integer> ut = dbosv.getUsersIterator();
		
			while (ut.hasNext()) {
				Integer uID = ut.next();
				SparseVectorSorted user = new SparseVectorSorted();
				user.loadFromSparseVectorTabular(dbosv.getUser(uID));
				SparseVectorTabular userT = new SparseVectorTabular(user.getSize());
				userT.loadFromSparseVectorSorted(user);
				dbosv.getUserBase().addSparseVector(uID,userT);
			}
		}
		
		// Order data for items
		if(loadRows) {
			Iterator<Integer> it = dbosv.getItemsIterator();
			while (it.hasNext()) {
				Integer iID = it.next();
				SparseVectorSorted item = new SparseVectorSorted();
				item.loadFromSparseVectorTabular(dbosv.getItem(iID));
				SparseVectorTabular itemT = new SparseVectorTabular(item.getSize());
				itemT.loadFromSparseVectorSorted(item);
				dbosv.getItemBase().addSparseVector(iID,itemT);
			}
		}
		// End
		
		dbosv.setLoadTime(System.currentTimeMillis() - time);
		return dbosv;		
	}

}