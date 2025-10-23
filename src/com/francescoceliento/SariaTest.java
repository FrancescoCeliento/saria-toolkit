package com.francescoceliento;

import com.francescoceliento.system.Explorer;
import com.francescoceliento.system.Explorer.Include;
import com.francescoceliento.system.Explorer.TypeSearch;

public class SariaTest {
	
	public static void main(String[] args) {
		
		String dir = "/home/francesco/Documenti/eBook/libri versionati";
		if (Explorer.fileExist(dir, "111", "pdf", TypeSearch.CASEINSENSITIVE, Include.CONTAINS, true))
			System.out.println("Esiste");
		else
			System.out.println("Non esiste");
		
	}
	

}
