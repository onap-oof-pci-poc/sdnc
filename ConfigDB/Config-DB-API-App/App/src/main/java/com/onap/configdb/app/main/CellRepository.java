package com.onap.configdb.app.main;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.onap.configdb.db.domain.Cell;
import com.onap.configdb.web.mapper.NbrList;
//param
/**
 * 
 * @author Devendra
 *
 */
@Repository
public interface CellRepository extends CrudRepository <Cell,String> {
	
	 @Query("SELECT c FROM Cell c WHERE c.cellId =:cellId and c.lastModifiedTS<=:lastModifiedTS")
	List<Cell> findByCellIdAndLastModifiedTS (@Param("cellId") String cellId, @Param("lastModifiedTS") Date lastModifiedTS);
	
	 @Query("SELECT cellId FROM Cell c WHERE c.networkId=:networkId and c.lastModifiedTS<=:lastModifiedTS")
	List<String> findCellIdsByNetworkIdAndLastModifiedTS (@Param("networkId") String networkId,@Param("lastModifiedTS") Date lastModifiedTS);
	List<Cell> findByLocation (String location);
	
	@Query("SELECT nbrList FROM Cell c WHERE c.cellId=:cellId and c.lastModifiedTS<=:lastModifiedTS")
	String   findNbrListByCellIdAndLastModifiedTS(@Param("cellId") String cellId, @Param("lastModifiedTS") Date lastModifiedTS);
	
	@Query("SELECT c.pciValue FROM Cell c WHERE c.cellId =:cellId")
    Long   findPciValueByCellId(@Param("cellId") String cellId);
	
	@Query("SELECT cellId,pciValue FROM Cell c WHERE c.cellId in (:cellIdList)")
    List<NbrList>   findNbrListByCellIds(@Param("cellIdList") List<String> cellIdList);
	

}