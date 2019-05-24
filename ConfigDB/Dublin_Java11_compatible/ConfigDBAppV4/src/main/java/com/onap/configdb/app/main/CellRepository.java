package com.onap.configdb.app.main;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onap.configdb.db.domain.Cell;
import com.onap.configdb.web.mapper.CellPciValueObj;
import com.onap.configdb.web.mapper.NbrList;
//param
/**
 * Repository class for Cell
 * @author Devendra Chauhan
 *
 */
@Repository
public interface CellRepository extends CrudRepository <Cell,String> {
	/**
	 * Finder method to fetch the Cell based on CellId and Timestamp
	 * @param cellId
	 * @param lastModifiedTS
	 * @return List of Cell
	 */
	
	 @Query("SELECT c FROM Cell c WHERE c.cellId =:cellId and c.lastModifiedTS<=:lastModifiedTS")
	List<Cell> findByCellIdAndLastModifiedTS (@Param("cellId") String cellId, @Param("lastModifiedTS") Date lastModifiedTS);
	
	 /**
	  * Finder method to fetch the Cell ids based on networkId and lastModifiedTS
	  * @param networkId
	  * @param lastModifiedTS
	  * @return list of CellIds
	  */
	 @Query("SELECT cellId FROM Cell c WHERE c.networkId=:networkId and c.lastModifiedTS<=:lastModifiedTS")
	List<String> findCellIdsByNetworkIdAndLastModifiedTS (@Param("networkId") String networkId,@Param("lastModifiedTS") Date lastModifiedTS);
	
	 /**
	  * Finder method to fetch the Cells  based on location 
	  * @param location
	  * @return list of Cell
	  */
	List<Cell> findByLocation (String location);
	
	/*@Query("SELECT nbrList FROM Cell c WHERE c.cellId=:cellId and c.lastModifiedTS<=:lastModifiedTS")
	String   findNbrListByCellIdAndLastModifiedTS(@Param("cellId") String cellId, @Param("lastModifiedTS") Date lastModifiedTS);
	*/
	/**
	 * Finder method to fetch Pcivalue by  Cell id
	 * @param cellId
	 * @return Pcivalue
	 */
	@Query("SELECT c.pciValue FROM Cell c WHERE c.cellId =:cellId")
    Long   findPciValueByCellId(@Param("cellId") String cellId);
	
	/**
	 * Finder method to fetch the Pcivalue by cellIds
	 * @param cellIdList
	 * @return list of Pcivalues
	 */
	@Query("SELECT new com.onap.configdb.web.mapper.CellPciValueObj(c.cellId,c.pciValue) FROM Cell c WHERE c.cellId in (:cellIdList)")
    List<CellPciValueObj>   findPciValueByCellIds(@Param("cellIdList") List<String> cellIdList);
	

}