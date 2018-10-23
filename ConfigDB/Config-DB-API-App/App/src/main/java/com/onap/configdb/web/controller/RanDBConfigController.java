package com.onap.configdb.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onap.configdb.app.main.CellRepository;
import com.onap.configdb.db.domain.Cell;
import com.onap.configdb.web.mapper.CellInputPayload;
import com.onap.configdb.web.mapper.CellObj;
import com.onap.configdb.web.mapper.NbrList;
import com.onap.configdb.web.mapper.Result;

@Controller
@RequestMapping(path = "/SDNCConfigDBAPI")

// param

public class RanDBConfigController {
	@Autowired
	private CellRepository cellRepository;

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
		List<String> cellList = cellRepository.findCellIdsByNetworkIdAndLastModifiedTS(networkId, ts);
		System.out.println("cellList>>>" + cellList);
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

		List<Cell> cellList = cellRepository.findByCellIdAndLastModifiedTS(cellId, ts);
		
		if(cellList.isEmpty()) {
			return new Result("Error","Data not found" );
		}
		return new Result("PCIvalue", cellList.get(0).getPciValue() + "");

	}

	/**
	 * Gets NeighborList based in CellId
	 * 
	 * @param cellId
	 * @param ts
	 * @return
	 */
	@GetMapping(path = "/getNbrList/{cellId}/{ts}", produces = "application/json")
	public @ResponseBody List<NbrList> getNbrList(@PathVariable("cellId") String cellId, @PathVariable("ts") Date ts) {
		List<NbrList> listN = new ArrayList<NbrList>();
		
		List<Cell> cellList = cellRepository.findByCellIdAndLastModifiedTS(cellId, ts);
		
		if(cellList.isEmpty()) {
			return listN;
			
		}
		String nbrList = cellList.get(0).getNbrList();

		List<String> CellIdList = Arrays.asList(nbrList.split(","));

	

		for (String id : CellIdList) {
			NbrList nbrListObj = new NbrList();
			Long pciValue = cellRepository.findPciValueByCellId(id);
			nbrListObj.setCellId(id);
			nbrListObj.setPciValue(pciValue);
			listN.add(nbrListObj);
		}

		System.out.println("listN.>>" + listN);
		return listN;

	}

	/**
	 * 
	 * @param cellId
	 * @param ts
	 * @return
	 */
	@GetMapping(path = "/getPnfName/{cellId}/{ts}", produces = "application/json")
	@ResponseBody
	public Result getPnfName(@PathVariable("cellId") String cellId, @PathVariable("ts") Date ts) throws Exception {

		List<Cell> cellList = cellRepository.findByCellIdAndLastModifiedTS(cellId, ts);

		System.out.println("cellList.get(0)>>" + cellList.size());
	if(cellList.isEmpty()) {
		return new Result("Error","Data not found" );
	}

		return new Result("pnf-name", cellList.get(0).getPnfName());
	}

	/**
	 * 
	 * Bulk upload Cell Data
	 * 
	 * @param cellInputPayload
	 * @return
	 */

	@PutMapping(path = "/insertData")
	public ResponseEntity<Cell> insertCellData(

			@RequestBody CellInputPayload cellInputPayload) {

		try {
			List<Cell> cellListData = new ArrayList<>();
			for (CellObj cellObj : cellInputPayload.getCellList()) {

				Cell cell = populateCellData(cellObj);
				cellListData.add(cell);

				System.out.println(cell.getCellId());

			}

			cellRepository.saveAll(cellListData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<Cell>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		System.out.println("cellInputPayload>>>>" + cellInputPayload);

		return new ResponseEntity<Cell>(HttpStatus.CREATED);

	}

	private Cell populateCellData(CellObj cellObj) {

		Cell cell = new Cell();
		cell.setCellId(cellObj.getCell().getNodeId());
		cell.setNetworkId(cellObj.getCell().getNetworkId());
		cell.setPciValue(cellObj.getCell().getPhysicalCellId());
		cell.setPnfName(cellObj.getCell().getPnfName());
		cell.setLastModifiedTS(new Date());
		if (!cellObj.getNeighbor().isEmpty()) {
			String str = Arrays.toString(cellObj.getNeighbor().toArray());
			str = str.trim().substring(1, str.length() - 1);
			System.out.println("Str>>>" + str);
			cell.setNbrList(str);

		}

		return cell;

	}

}
