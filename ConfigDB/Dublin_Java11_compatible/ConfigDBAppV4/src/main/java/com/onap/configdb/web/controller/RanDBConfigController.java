package com.onap.configdb.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onap.configdb.app.main.CellNbrInfoRepository;
import com.onap.configdb.app.main.CellRepository;
import com.onap.configdb.db.domain.Cell;
import com.onap.configdb.db.domain.CellNbrInfo;
import com.onap.configdb.db.domain.CellNbrInfoCompositePrimaryKey;
import com.onap.configdb.web.mapper.CellData;
import com.onap.configdb.web.mapper.CellInputPayload;
import com.onap.configdb.web.mapper.CellNbrInfoResponse;
import com.onap.configdb.web.mapper.CellObj;
import com.onap.configdb.web.mapper.CellPciValueObj;
import com.onap.configdb.web.mapper.NbrCellsNetworkResponse;
import com.onap.configdb.web.mapper.NbrList;
import com.onap.configdb.web.mapper.NbrListResponse;
import com.onap.configdb.web.mapper.NbrObj;
import com.onap.configdb.web.mapper.Result;

@Controller
@RequestMapping(path = "/api/sdnc-config-db/v4")

// param
/**
 * Controller class exposes the REST Api for ConfigDB
 * @author Devendra Chauhan
 *
 */

public class RanDBConfigController {
	@Autowired
	private CellRepository cellRepository;
	private static final Logger logger = LoggerFactory.getLogger(RanDBConfigController.class);

	@Autowired
	private CellNbrInfoRepository cellNbrInfoRepository;

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * Gets the CellList
	 * 
	 * @param networkId
	 * @param ts
	 * @return
	 */
	@GetMapping(path = "/getCellList/{networkId}/{ts}", produces = "application/json")

	public @ResponseBody List<String> getCellList(@PathVariable("networkId") String networkId,
			@PathVariable("ts") Date ts) {
		logger.debug("Getting Cell List for networkId {}", networkId);
		List<String> cellList = new ArrayList<>();
		try {
			cellList = cellRepository.findCellIdsByNetworkIdAndLastModifiedTS(networkId, ts);
			logger.debug("Found Cell List>> {} for networkId :{}", cellList, networkId);
		} catch (Exception e) {
			logger.debug(" Exception occurred on finding Cell List for networkId ", e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return cellList;
	}

	/**
	 * Gets PCI
	 * 
	 * @param cellId
	 * @param ts
	 * @return
	 */
	@GetMapping(path = "/getPCI/{cellId}/{ts}", produces = "application/json")
	public @ResponseBody Result getPCI(@PathVariable("cellId") String cellId, @PathVariable("ts") Date ts) {

		logger.debug("Getting PCI for CellId  {}", cellId);

		try {
			List<Cell> cellList = cellRepository.findByCellIdAndLastModifiedTS(cellId, ts);

			if (cellList.isEmpty()) {
				logger.debug("No data found for CellId  {}", cellId);
				return new Result("Error", "Data not found");
			}
			return new Result("PCIvalue", cellList.get(0).getPciValue() + "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug("Exception occurred while getting PCI for CellId  {}", e.getMessage());
			return new Result("Error", e.getMessage());
		}

	}

	/**
	 * Gets the Cell data based on cellId
	 * @param cellId
	 * @return Celldata
	 */
	@GetMapping(path = "/getCell/{cellId}", produces = "application/json")
	public ResponseEntity<CellObj> getCell(@PathVariable("cellId") String cellId) {
		logger.debug("Getting Cell data for CellId  {}", cellId);

		try {
			Cell cell = cellRepository.findById(cellId).get();

			List<String> nbrCellIdList = cellNbrInfoRepository.findNbrCellIdListByCellIds(cellId);

			CellData cellData = populateCellData(cell);
			CellObj cellObj = new CellObj();

			cellObj.setCell(cellData);
			if (nbrCellIdList != null && !nbrCellIdList.isEmpty()) {
				cellObj.setNeighbor(nbrCellIdList);
			} else {
				cellObj.setNeighbor(new ArrayList<String>());
			}
			return new ResponseEntity<CellObj>(cellObj, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			// e.printStackTrace();
			logger.debug("No  record found for CellId  {}", cellId);
			return new ResponseEntity<CellObj>(HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception occurred while finding cell: {}", cellId);
			e.printStackTrace();
			return new ResponseEntity<CellObj>(HttpStatus.INTERNAL_SERVER_ERROR);
			// TODO Auto-generated catch block

		}
	}

	private CellData populateCellData(Cell cell) {
		logger.debug("Populating  Celldata for CellId  {}", cell.getCellId());

		CellData cellData = new CellData();

		cellData.setNetworkId(cell.getNetworkId());
		cellData.setNodeId(cell.getCellId());
		cellData.setPnfId(cell.getPnfId());
		cellData.setPhysicalCellId(cell.getPciValue());
		String location = cell.getLocation();
		if (location != null && !"".equals(location) && location.indexOf(',') > -1) {
			String longitude = location.split(",")[0];
			String latitude = location.split(",")[1];
			cellData.setLongitude(longitude);
			cellData.setLatitude(latitude);

		} else {
			cellData.setLongitude("NA");
			cellData.setLatitude("NA");

		}
		if (cell.getNotes() != null) {
			cellData.setNotes(cell.getNotes());

		} else {
			cellData.setNotes("NA");
		}

		return cellData;

	}

	/**
	 * Gets NeighborList based in  CellId and time
	 * 
	 * @param cellId
	 * @param ts
	 * @return
	 */
	@GetMapping(path = "/getNbrList/{cellId}/{ts}", produces = "application/json")
	public ResponseEntity<NbrListResponse> getNbrList(@PathVariable("cellId") String cellId) {

		logger.debug("Getting NbrList  for CellId  {}", cellId);

		try {
			List<CellNbrInfo> cellNbrInfoList = cellNbrInfoRepository.findNbrObjByCellId(cellId);

			if (cellNbrInfoList != null && !cellNbrInfoList.isEmpty()) {
				logger.debug("Got Cell Info NbrList  for CellId  {}", cellId);

				List<String> idList = new ArrayList<>();

				Map<String, CellNbrInfo> cellNbrInfoMap = new HashMap<>();

				logger.debug("Getting NbrList  for CellId  {}", cellNbrInfoList);
				for (CellNbrInfo cellNbrInfo : cellNbrInfoList) {
					String id = cellNbrInfo.getCellNbrInfoCompositePrimaryKey().getTargetCellId();
					idList.add(id);
					cellNbrInfoMap.put(id, cellNbrInfo);

				}

				List<CellPciValueObj> cellPciValueObjList = cellRepository.findPciValueByCellIds(idList);

				List<NbrList> nbrInfoList = new ArrayList<>();

				for (CellPciValueObj cellPciValueObj : cellPciValueObjList) {

					NbrList nbrListObj = new NbrList();

					nbrListObj.setHo(cellNbrInfoMap.get(cellPciValueObj.getCellId()).isHo());
					nbrListObj.setPciValue(cellPciValueObj.getPciValue());
					nbrListObj.setTargetCellId(cellPciValueObj.getCellId());
					nbrInfoList.add(nbrListObj);

				}

				NbrListResponse nbrListResponse = new NbrListResponse();

				nbrListResponse.setCellId(cellId);

				nbrListResponse.setNbrList(nbrInfoList);
				return new ResponseEntity<NbrListResponse>(nbrListResponse, HttpStatus.OK);

			} else {
				logger.debug("No  record found for Cell Info NbrList  for CellId  {}", cellId);
				return new ResponseEntity<NbrListResponse>(HttpStatus.NO_CONTENT);

			}
		} catch (Exception e) {
			logger.debug("Exception Occurred in getting Nbr List{}  ...", e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<NbrListResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
/**
 * Gets the Neighbor cells data based on network id  
 * @param networkId
 * @return Neighbor cells data
 */
	@GetMapping(path = "/getNbrCellsNetwork/{networkId}", produces = "application/json")
	public ResponseEntity<NbrCellsNetworkResponse> getNbrCellsNetwork(@PathVariable("networkId") String networkId) {

		logger.debug("Getting the NbrCells for  Network Id   {}", networkId);

		try {
			// get the list of Cell Ids
			List<String> cellIds = cellRepository.findCellIdsByNetworkIdAndLastModifiedTS(networkId, new Date());

			if (cellIds != null && !cellIds.isEmpty()) {
				logger.debug("Got the NbrCells for  Network Id   {}", networkId);

				List<CellNbrInfo> cellNbrInfoList = cellNbrInfoRepository.findNbrObjListByCellIds(cellIds);

				// populate Set of TargetCellIds
				List<String> targetCellIds = new ArrayList<>();
				for (CellNbrInfo cellNbrInfo : cellNbrInfoList) {
					String targetCellId = cellNbrInfo.getCellNbrInfoCompositePrimaryKey().getTargetCellId();
					if (!targetCellIds.contains(targetCellId)) {
						targetCellIds.add(targetCellId);

					}

				}

				// convert to the List

				List<CellPciValueObj> cellPciValueObjList = cellRepository.findPciValueByCellIds(targetCellIds);

				Map<String, Long> cellPcivalueMap = cellPciValueObjList.stream()
						.collect(Collectors.toMap(CellPciValueObj::getCellId, CellPciValueObj::getPciValue));

				// populate map key>> CellID value>> NbrList
				Map<String, List<NbrList>> cellNbrInfoMap = new HashMap();
				for (CellNbrInfo cellNbrInfo : cellNbrInfoList) {
					String cellId = cellNbrInfo.getCellNbrInfoCompositePrimaryKey().getCellId();
					NbrList nbrListObj = new NbrList();

					nbrListObj.setHo(cellNbrInfo.isHo());
					nbrListObj.setPciValue(
							cellPcivalueMap.get(cellNbrInfo.getCellNbrInfoCompositePrimaryKey().getTargetCellId()));
					nbrListObj.setTargetCellId(cellNbrInfo.getCellNbrInfoCompositePrimaryKey().getTargetCellId());
					// nbrInfoList.add(nbrListObj);

					if (cellNbrInfoMap.containsKey(cellId)) {

						List<NbrList> list = cellNbrInfoMap.get(cellId);

						list.add(nbrListObj);
					} else {

						List<NbrList> list = new ArrayList();
						list.add(nbrListObj);

						cellNbrInfoMap.put(cellId, list);

					}

				}

				List<NbrListResponse> respList = new ArrayList();
				for (String cellId : cellIds) {

					NbrListResponse listResponse = new NbrListResponse();
					listResponse.setCellId(cellId);
					listResponse.setNbrList(cellNbrInfoMap.getOrDefault(cellId, new ArrayList()));
					respList.add(listResponse);

				}
				NbrCellsNetworkResponse nbrCellsNetworkResponse = new NbrCellsNetworkResponse();
				nbrCellsNetworkResponse.setNetworkId(networkId);
				nbrCellsNetworkResponse.setCellsNbrList(respList);

				logger.debug("Returning  OK  for Network Id   {}", networkId);

				return new ResponseEntity<NbrCellsNetworkResponse>(nbrCellsNetworkResponse, HttpStatus.OK);

			} else {
				logger.debug("No Cells found for Network Id   {}", networkId);
				return new ResponseEntity<NbrCellsNetworkResponse>(HttpStatus.NO_CONTENT);

			}
		} catch (Exception e) {
			logger.debug("Exception Occurred in getting NbrCell for network : {}  ...", e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<NbrCellsNetworkResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Gets PnfId based on CellId and timestamp
	 * @param cellId
	 * @param ts
	 * @return pnfId
	 */
	@GetMapping(path = "/getPnfId/{cellId}/{ts}", produces = "application/json")
	@ResponseBody
	public Result getPnfId(@PathVariable("cellId") String cellId, @PathVariable("ts") Date ts) throws Exception {

		logger.debug("Getting PnfId for CellId {}", cellId);
		try {
			List<Cell> cellList = cellRepository.findByCellIdAndLastModifiedTS(cellId, ts);

			logger.debug("cellList Size {}>>", cellList.size());
			if (cellList.isEmpty()) {
				return new Result("Error", "Data not found");
			}

			return new Result("pnf-name", cellList.get(0).getPnfId());
		} catch (Exception e) {
			logger.debug("Exception Occurred in getting NbrCell for network : {}  ...", e.getMessage());
			e.printStackTrace();
			return new Result("Error", e.getMessage());
		}
	}

	/**
	 * 
	 * Bulk upload Cell Data
	 * 
	 * @param cellInputPayload
	 * @return
	 */

	@PutMapping(path = "/insertData")
	public ResponseEntity<Void> insertCellData(

			@RequestBody CellInputPayload cellInputPayload) {

		try {
			deleteAllRecord();

			logger.debug("Bulk upload of Data started for  Cell count : {} it will take few seconds ...",
					cellInputPayload.getCellList().size());
			List<Cell> cellListData = new ArrayList<>();

			List<CellNbrInfo> cellNbrInfoListData = new ArrayList<>();

			for (CellObj cellObj : cellInputPayload.getCellList()) {

				Cell cell = populateCellData(cellObj);
				cellListData.add(cell);

				cellNbrInfoListData.addAll(populateNbrCellInfoData(cellObj));

				cellNbrInfoRepository.saveAll(cellNbrInfoListData);

			}

			cellRepository.saveAll(cellListData);

			logger.debug("Bulk upload of Data finished successfully  for  Cell count : {}  ...",
					cellInputPayload.getCellList().size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug("Exception in Bulk upload of Data  {}  ...", e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.CREATED);

	}

	private Cell populateCellData(CellObj cellObj) {

		Cell cell = new Cell();
		cell.setCellId(cellObj.getCell().getNodeId());
		cell.setNetworkId(cellObj.getCell().getNetworkId());
		cell.setPciValue(cellObj.getCell().getPhysicalCellId());
		cell.setPnfId(cellObj.getCell().getPnfId());
		cell.setLastModifiedTS(new Date());
		if (cellObj.getCell().getLongitude() != null && cellObj.getCell().getLatitude() != null
				&& !"".equals(cellObj.getCell().getLongitude()) && !"".equals(cellObj.getCell().getLatitude())) {
			cell.setLocation(cellObj.getCell().getLongitude() + "," + cellObj.getCell().getLatitude());

		}
		return cell;

	}

	private List<CellNbrInfo> populateNbrCellInfoData(CellObj cellObj) {

		List<CellNbrInfo> cellNbrInfoList = new ArrayList<>();
		if (!cellObj.getNeighbor().isEmpty()) {

			for (String targetCellId : cellObj.getNeighbor()) {
				CellNbrInfo cellNbrInfo = new CellNbrInfo();
				cellNbrInfo.setHo(true);
				CellNbrInfoCompositePrimaryKey key = new CellNbrInfoCompositePrimaryKey(cellObj.getCell().getNodeId(),
						targetCellId);
				cellNbrInfo.setCellNbrInfoCompositePrimaryKey(key);
				cellNbrInfoList.add(cellNbrInfo);

			}
		}

		return cellNbrInfoList;

	}
/**
 * Creates Neighbor for particular Cell
 * @param cellId
 * @param createNbrRequest
 * @return
 */
	@PutMapping(path = "/createNbr/{cellId}")
	public ResponseEntity<CellNbrInfoResponse> createNbr(@PathVariable String cellId,
			@RequestBody NbrObj createNbrRequest) {
		logger.debug("Creating Nbr for CellId : {} ", cellId);
		try {
			CellNbrInfoCompositePrimaryKey cellNbrInfoCompositePrimaryKey = new CellNbrInfoCompositePrimaryKey(cellId,
					createNbrRequest.getTargetCellId());
			CellNbrInfo cellNbrInfo = new CellNbrInfo();

			cellNbrInfo.setCellNbrInfoCompositePrimaryKey(cellNbrInfoCompositePrimaryKey);
			cellNbrInfo.setHo(createNbrRequest.isHo());

			cellNbrInfoRepository.save(cellNbrInfo);
			logger.debug("Created Nbr for CellId : {} successfully.. ", cellId);
			CellNbrInfoResponse cellNbrInfoResponse = new CellNbrInfoResponse(cellId,
					createNbrRequest.getTargetCellId(), createNbrRequest.isHo());

			return new ResponseEntity<CellNbrInfoResponse>(cellNbrInfoResponse, HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug("Exception in createNbr {}  ...", e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<CellNbrInfoResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	/**
	 * Deletes Neighbor for particular Cell
	 * @param cellId
	 * @param targetCellId
	 * @return
	 */

	@DeleteMapping(path = "/deleteNbr/{cellId}/{targetCellId}")
	public ResponseEntity<Void> deleteNbr(@PathVariable("cellId") String cellId,
			@PathVariable("targetCellId") String targetCellId) {

		logger.debug("Deleting Nbr  for Cell Id {}  ...", cellId);

		try {
			CellNbrInfoCompositePrimaryKey id = new CellNbrInfoCompositePrimaryKey(cellId, targetCellId);
			cellNbrInfoRepository.deleteById(id);
			logger.debug("Deleted Nbr  for Cell Id {}  ...", cellId);
			

			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("No Nbr found  for cell Id : {} and targetCellId{} ...", cellId, targetCellId);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.debug("Exception in deleteNbr {}  ...", e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Modifies Neighbor HO 
	 * @param cellId
	 * @param targetCellId
	 * @param fields
	 * @return
	 */
	@PatchMapping(path = "/modifyNbrHO/{cellId}/{targetCellId}")
	public ResponseEntity<Void> modifyNbrHo(@PathVariable("cellId") String cellId,
			@PathVariable("targetCellId") String targetCellId, @RequestBody Map<String, Object> fields) {

		logger.debug("Modifying  NbrHO  for Cell Id : {} and targetCellId :{} ...", cellId, targetCellId);
		try {
			Boolean ho = (Boolean) fields.get("ho");
			CellNbrInfoCompositePrimaryKey id = new CellNbrInfoCompositePrimaryKey(cellId, targetCellId);
			CellNbrInfo cellNbrInfo = cellNbrInfoRepository.findById(id).get();

			cellNbrInfo.setHo(ho);
			cellNbrInfoRepository.save(cellNbrInfo);

			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			logger.debug("No Nbr found  for cell Id : {} and targetCellId{} ...", cellId, targetCellId);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.debug("Exception occurred while Modifying  NbrHO  :{} ...", e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
/**
 * Modifies PCI for particular Cell
 * @param cellId
 * @param fields
 * @return
 */
	@PatchMapping(path = "/modifyPci/{cellId}")
	public ResponseEntity<Void> modifyNbrPci(@PathVariable("cellId") String cellId,
			@RequestBody Map<String, Object> fields) {
		// ram
		logger.debug("Modifying  NbrPci  for Cell Id : {}  ...", cellId);
		try {
			Integer pciValue = (Integer) fields.get("pci-value");

			Cell cell = cellRepository.findById(cellId).get();

			cell.setPciValue(pciValue);
			cellRepository.save(cell);
			logger.debug("Modified   NbrPci  for Cell Id : {} successfully ...", cellId);

			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			logger.debug("No data found  for cell Id : {}  ...", cellId);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.debug("Exception occurred while Modifying  PCI  :{} ...", e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
/**
 * Modifies PnfId for particular Cell
 * @param cellId
 * @param fields
 * @return
 */
	@PatchMapping(path = "/modifyPnfId/{cellId}")
	public ResponseEntity<Void> modifyPnfId(@PathVariable("cellId") String cellId,
			@RequestBody Map<String, Object> fields) {

		logger.debug("Modifying  PnfId  for Cell Id : {}  ...", cellId);
		try {

			String pnfId = (String) fields.get("pnf-id");

			Cell cell = cellRepository.findById(cellId).get();

			cell.setPnfId(pnfId);
			cellRepository.save(cell);
			logger.debug("Modified   PnfId  for Cell Id : {} sucessfully ...", cellId);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			logger.debug("No data found  for cell Id : {}  ...", cellId);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.debug("Exception occurred while Modifying  PnfId :{} ...", e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	private void deleteAllRecord() {

		logger.debug("Deleting all records...");

		cellNbrInfoRepository.deleteAll();
		cellRepository.deleteAll();
		logger.debug("Deleted all records successfully.....");

	}

}
