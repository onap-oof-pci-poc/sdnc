package com.onap.configdb.app.main;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onap.configdb.db.domain.CellNbrInfo;
import com.onap.configdb.db.domain.CellNbrInfoCompositePrimaryKey;

//param
/**
 * Repository Class for CellNbrInfo
 * 
 * @author Devendra Chauhan
 *
 */
@Repository
public interface CellNbrInfoRepository extends CrudRepository<CellNbrInfo, CellNbrInfoCompositePrimaryKey> {
/**
 * Finder method to fetch the Neighbor object for particular Cell 
 * @param cellId
 * @return list of Neighbors
 */
	@Query("SELECT c FROM CellNbrInfo c WHERE c.cellNbrInfoCompositePrimaryKey.cellId =:cellId")
	List<CellNbrInfo> findNbrObjByCellId(@Param("cellId") String cellId);

	/**
	 * Finder method to fetch the Neighbor object for  Cells
	 * @param cellIdList
	 * @return list of Neighbors
	 */
	@Query("SELECT c FROM CellNbrInfo c WHERE c.cellNbrInfoCompositePrimaryKey.cellId in (:cellIdList)")
	List<CellNbrInfo> findNbrObjListByCellIds(@Param("cellIdList") List<String> cellIdList);

	/**
	 * Finder method to fetch the Neighbor CellIds  for  particular Cell
	 * @param cellId
	 * @return
	 */
	@Query("SELECT c.cellNbrInfoCompositePrimaryKey.targetCellId FROM CellNbrInfo c WHERE c.cellNbrInfoCompositePrimaryKey.cellId =:cellId")
	List<String> findNbrCellIdListByCellIds(@Param("cellId") String cellId);

}
