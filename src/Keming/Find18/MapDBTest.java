package Keming.Find18;

import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class MapDBTest {

	public static void main(String[] argv) {
		DB db = DBMaker.fileDB("file.db").make();
		ConcurrentMap map = db.hashMap("map").createOrOpen();
		map.put("something", "here");
		db.close();	}
}
