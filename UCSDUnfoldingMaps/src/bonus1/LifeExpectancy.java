package bonus1;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

public class LifeExpectancy extends PApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UnfoldingMap map;
	Map<String, Float> lifeExpByCountries = null;

	@Override
	public void draw() {
		map.draw();
	}

	@Override
	public void setup() {

		println("SetUp");
		loadData("LifeExpectancyWorldBankModule3.csv");
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		List<Feature> countries = GeoJSONReader.loadData(this, "../data/countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
	}

	private Map<String, Float> loadData(String filename) {
		try {
			File file = new File(filename);
			FileInputStream in = new FileInputStream(file);
			int c  =0;
			byte b[] = new byte[1];
			byte buffer[] = new byte[10000];
			int i = 0;
			String newline;
			while ((c = in.read(b)) != -1) {
				if (b[0] =='\r') {
					in.read(b);
					if(b[0] == '\n') {
					println("Found new Line");
						newline = new String(buffer);
						println(newline);
						buffer = new byte[10000];
						i = 0;
					}
				}
				buffer[i++] = b[0];
				
			}
			println("done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
