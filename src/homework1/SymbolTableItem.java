package homework1;

import homework1.Lexical.Type;

public class SymbolTableItem {
	public SymbolTableItem(String opSymbol, Type opType) {
	
		this.opSymbol = opSymbol;
		this.opType = opType;
	}
	String opSymbol;
	Type opType;
	
	
	public String getOpSymbol() {
		return opSymbol;
	}
	public void setOpSymbol(String opSymbol) {
		this.opSymbol = opSymbol;
	}
	public Type getOpType() {
		return opType;
	}
	public void setOpType(Type opType) {
		this.opType = opType;
	}
	
	
	
}
