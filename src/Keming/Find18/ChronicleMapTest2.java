package Keming.Find18;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

public class ChronicleMapTest2 {

	ChronicleMapBuilder<CharSequence, PostalCodeRange> cityPostalCodesMapBuilder =
	    ChronicleMapBuilder.of(CharSequence.class, PostalCodeRange.class)
	        .name("city-postal-codes-map")
	        .averageKey("Amsterdam")
	        .entries(50_000);
	ChronicleMap<CharSequence, PostalCodeRange> cityPostalCodes =
	    cityPostalCodesMapBuilder.create();

	
	public static void main(String[] argv) {
		ChronicleMapTest2 cmapTest = new ChronicleMapTest2();
		
		long max = 1_000;
		for(int i=0; i<max; i++) {
			PostCode postCode = new PostCode();
//			cmapTest.cityPostalCodes.put("Beijing" + i, postCode);
		}
	}
}
