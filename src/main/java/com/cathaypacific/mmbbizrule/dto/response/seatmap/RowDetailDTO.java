package com.cathaypacific.mmbbizrule.dto.response.seatmap;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RowDetailDTO {
	
	private BigInteger rowNumber;
	
	private List<String> columns;

	public BigInteger getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(BigInteger rowNumber) {
		this.rowNumber = rowNumber;
	}

	public List<String> getColumns() {
		return columns;
	}
	
	public List<String> findColumns() {
		if(columns == null) {
			columns = new ArrayList<>();
		}
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
}
