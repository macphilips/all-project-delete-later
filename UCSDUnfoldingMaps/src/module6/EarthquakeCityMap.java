package module6;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.*;

import java.awt.Component;
import java.awt.event.*;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/**
 * EarthquakeCityMap An application with an interactive map displaying
 * earthquake data. Author: UC San Diego Intermediate Software Development MOOC
 * team
 * 
 * @author Your name here Date: July 17, 2015
 * @param <T>
 */
public class EarthquakeCityMap<T> extends PApplet {

	private static final long serialVersionUID = 1L;

	private static final boolean offline = false;

	public static String mbTilesString = "blankLight-1-3.mbtiles";

	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";

	private UnfoldingMap map;

	private List<Marker> cityMarkers;

	private List<Marker> quakeMarkers;

	private List<Marker> countryMarkers;

	private CommonMarker lastSelected;
	private CommonMarker lastClicked;

	public void setup() {
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "quiz2.atom"; // The same feed, but saved August
											// 7, 2015
		} else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			earthquakesURL = "quiz2.atom";
		}

		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);

		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for (Feature city : cities) {
			cityMarkers.add(new CityMarker(city));
		}

		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
		quakeMarkers = new ArrayList<Marker>();
		ArrayList<EarthquakeMarker> ls = new ArrayList<>();

		for (PointFeature feature : earthquakes) {
			if (isLand(feature)) {
				LandQuakeMarker b = new LandQuakeMarker(feature);
				quakeMarkers.add(b);
				ls.add(b);
			}

			else {
				OceanQuakeMarker b = new OceanQuakeMarker(feature);
				quakeMarkers.add(b);
				ls.add(b);
			}
		}
		Collections.sort(ls, Collections.reverseOrder());

		for (int i = 0, j = 0, n = 100; i < n; i++) {
			println(++j + " Magnitude: " + ls.get(i).getMagnitude());
		}
		// printQuakes();

		map.addMarkers(quakeMarkers);
		map.addMarkers(cityMarkers);

		// sortAndPrint(15);

	} // End setup

	public void draw() {
		background(0);
		map.draw();
		addKey();

	}

	@Override
	public void mouseMoved() {
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;

		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);

	}

	private void selectMarkerIfHover(List<Marker> markers) {

		if (lastSelected != null) {
			return;
		}

		for (Marker m : markers) {
			CommonMarker marker = (CommonMarker) m;
			if (marker.isInside(map, mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}

	@Override
	public void mouseClicked() {
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		} else if (lastClicked == null) {
			checkEarthquakesForClick();
			if (lastClicked == null) {
				checkCitiesForClick();
			}
		}
	}

	private void checkCitiesForClick() {
		if (lastClicked != null)
			return;
		DisplayInfo d = new DisplayInfo();
		int num = 0;
		for (Marker marker : cityMarkers) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker) marker;

				for (Marker mhide : cityMarkers) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				for (Marker mhide : quakeMarkers) {
					EarthquakeMarker quakeMarker = (EarthquakeMarker) mhide;
					if (quakeMarker.getDistanceTo(marker.getLocation()) > quakeMarker.threatCircle()) {
						quakeMarker.setHidden(true);
						 
					}else{
						num++;
					}
				}
				d.a = "Number of City within ThreatCirlce: " + 5;
				d.b = "Average Magnitude "+3.7;
				d.c = "";
				d.d = "";
				showPopUpMenu(d, mouseX-40, mouseY - 40);
				return;
			}
		}
	}

	private void checkEarthquakesForClick() {
		if (lastClicked != null)
			return;

		DisplayInfo d = new DisplayInfo();
		int num = 0;
		for (Marker m : quakeMarkers) {
			EarthquakeMarker marker = (EarthquakeMarker) m;
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;

				for (Marker mhide : quakeMarkers) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				for (Marker mhide : cityMarkers) {
					if (mhide.getDistanceTo(marker.getLocation()) > marker.threatCircle()) {
						mhide.setHidden(true);
						
					}else{
						num++;
					}
				}
				d.a = "Number of City within ThreatCirlce: " + 5;
				d.b = "Average Magnitude "+3.7;
				d.c = "";
				d.d = "";
				showPopUpMenu(d, mouseX - 40, mouseY - 40);
				return;
			}
		}
	}

	private void unhideMarkers() {
		for (Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}

		for (Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}

	private void addKey() {

		fill(255, 250, 240);

		int xbase = 25;
		int ybase = 50;

		rect(xbase, ybase, 150, 250);

		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xbase + 25, ybase + 25);

		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		triangle(tri_xbase, tri_ybase - CityMarker.TRI_SIZE, tri_xbase - CityMarker.TRI_SIZE,
				tri_ybase + CityMarker.TRI_SIZE, tri_xbase + CityMarker.TRI_SIZE, tri_ybase + CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);

		text("Land Quake", xbase + 50, ybase + 70);
		text("Ocean Quake", xbase + 50, ybase + 90);
		text("Size ~ Magnitude", xbase + 25, ybase + 110);

		fill(255, 255, 255);
		ellipse(xbase + 35, ybase + 70, 10, 10);
		rect(xbase + 35 - 5, ybase + 90 - 5, 10, 10);

		fill(color(255, 255, 0));
		ellipse(xbase + 35, ybase + 140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase + 35, ybase + 160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase + 35, ybase + 180, 12, 12);

		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase + 50, ybase + 140);
		text("Intermediate", xbase + 50, ybase + 160);
		text("Deep", xbase + 50, ybase + 180);

		text("Past hour", xbase + 50, ybase + 200);

		fill(255, 255, 255);
		int centerx = xbase + 35;
		int centery = ybase + 200;
		ellipse(centerx, centery, 12, 12);

		strokeWeight(2);
		line(centerx - 8, centery - 8, centerx + 8, centery + 8);
		line(centerx - 8, centery + 8, centerx + 8, centery - 8);

	}

	private boolean isLand(PointFeature earthquake) {

		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}

		return false;
	}

	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers) {
				EarthquakeMarker eqMarker = (EarthquakeMarker) marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}

	private boolean isInCountry(PointFeature earthquake, Marker country) {

		Location checkLoc = earthquake.getLocation();

		if (country.getClass() == MultiMarker.class) {

			for (Marker marker : ((MultiMarker) country).getMarkers()) {

				// checking if inside
				if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));

					return true;
				}
			}
		}

		else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));

			return true;
		}
		return false;
	}

	private void sortAndPrint(int numToPrint) {
		EarthquakeMarker[] tosort = (EarthquakeMarker[]) quakeMarkers.toArray();
		Arrays.sort(tosort, Collections.reverseOrder());
		for (int i = 0; i < Math.min(numToPrint, tosort.length); i++) {
			System.out.println(tosort[i]);
		}
	}

	private void showPopUpMenu(DisplayInfo info, int getX, int getY) {
		final JPopupMenu menu = new JPopupMenu();

		// Create and add a menu item
		JMenuItem item = new JMenuItem(info.toString());

		menu.add(item);

		menu.show(this, getX, getY);

	}

	private class DisplayInfo {
		String a, b, c, d;

		@Override
		public String toString() {
			return a + " \r\n " + b + " \r\n " + c + " \r\n " + d;
		}

	}

}