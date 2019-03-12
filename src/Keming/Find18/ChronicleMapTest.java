package Keming.Find18;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

public class ChronicleMapTest {
	interface PostalCodeRange {
	    int minCode();
	    void minCode(int minCode);

	    int maxCode();
	    void maxCode(int maxCode);
	}

	ChronicleMapBuilder<CharSequence, PostalCodeRange> cityPostalCodesMapBuilder =
	    ChronicleMapBuilder.of(CharSequence.class, PostalCodeRange.class)
	        .name("city-postal-codes-map")
	        .averageKey("Amsterdam")
	        .entries(50_000);
	ChronicleMap<CharSequence, PostalCodeRange> cityPostalCodes =
	    cityPostalCodesMapBuilder.create();
	
	
	public static void main(String[] argv) {
		ChronicleMapTest cmapTest = new ChronicleMapTest();
		
		long max = 1_000;
		for(int i=0; i<max; i++) {
			System.out.println( cmapTest.cityPostalCodes.get("Beijing" + i) );
		}
	}
}
