package uk.co.ordnancesurvey.android.demos.osmappingdemo;


import java.lang.Override;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import uk.co.ordnancesurvey.android.maps.BitmapDescriptor;
import uk.co.ordnancesurvey.android.maps.BitmapDescriptorFactory;
import uk.co.ordnancesurvey.android.maps.CircleOptions;
import uk.co.ordnancesurvey.android.maps.GridPoint;
import uk.co.ordnancesurvey.android.maps.GridRect;
import uk.co.ordnancesurvey.android.maps.MapFragment;
import uk.co.ordnancesurvey.android.maps.MarkerOptions;
import uk.co.ordnancesurvey.android.maps.OSMap;
import uk.co.ordnancesurvey.android.maps.OSTileSource;
import uk.co.ordnancesurvey.android.maps.PolygonOptions;
import uk.co.ordnancesurvey.android.maps.PolylineOptions;

public class MainActivity extends Activity implements OSMap.OnMapClickListener{

    /**
     *  Define your OS Openspace API KEY details below
     *  @see http://www.ordnancesurvey.co.uk/oswebsite/web-services/os-openspace/index.html
     *
     */
    private final static String OS_API_KEY = "E98D7DFC9D1A5AE5E0430C6CA40A3CE5";

    private final static boolean OS_IS_PRO = false;

    private final static String TAG = "MainActivity";

    private OSMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mf = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment));

        mMap = mf.getMap();

        //create list of tileSources
        ArrayList<OSTileSource> sources = new ArrayList<OSTileSource>();

        //create web tile source with API details
        sources.add(mMap.webTileSource(OS_API_KEY, OS_IS_PRO, null));
        mMap.setTileSources(sources);

        int basex =  437500;
        int basey = 115500;
        float xStep = 218.75f;
        float yStep = 218.75f;
        for(int x = 0; x < 10; x++)
        {
            for(int y = 0; y < 10; y++) {
                BitmapDescriptor icon;
                if (x == 0) {
                    // When x=0, load an untinted marker
                    icon = BitmapDescriptorFactory.defaultMarker();
                } else {
                    // Otherwise, start tinting the markers starting from red.
                    int hue = 30*(x-1);
                    icon = BitmapDescriptorFactory.defaultMarker(hue % 360);
                }
                GridPoint gp = new GridPoint(basex+x*xStep,basey+y * yStep);
                mMap.addMarker(new MarkerOptions().gridPoint(gp).icon(icon).draggable(true)
                        .title("Test title")
                        .snippet("Test snippet"));
            }
        }

        // A test marker, pointing at the OS Head Office entrance.
        // Corresponds to approximately http://www.openstreetmap.org/?mlat=50.9378&mlon=-1.47065&zoom=18
        mMap.addMarker(new MarkerOptions().gridPoint(new GridPoint(437294, 115504)));

        // Add a simple line to be getting on with
        GridPoint gp = new GridPoint(basex, basey);

        GridRect gr= GridRect.fromCentreXYWH(basex, basey-100, 100, 100);
        GridRect gr2= GridRect.fromCentreXYWH(basex, basey+200, 100, 100);

        mMap.addPolyline(new PolylineOptions().add(gr.sw(),gr.nw(),gr.ne(),gr.se()).color(0xffff0000));
        mMap.addPolygon(new PolygonOptions().add(gr2.sw(),gr2.nw(),gr2.ne(),gr2.se()).strokeColor(0xffff0000));
        mMap.addCircle(new CircleOptions().center(gp).radius(100).fillColor(0x408080FF).strokeColor(0xFF8080FF).strokeWidth(5));


        // register as OnMapClickListener
        mMap.setOnMapClickListener(this);
    }

    @Override
    public boolean onMapClick(GridPoint gp)
    {
        final String locationMessage = String.format("Map tapped at OS GridPoint\n{%.0f, %.0f}", gp.x, gp.y);

        mMap.addMarker(new MarkerOptions()
                .gridPoint(gp)
                .title("Map clicked here!")
                .snippet(locationMessage));

        return true;
    }
}
