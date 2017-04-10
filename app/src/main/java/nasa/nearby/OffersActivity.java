package nasa.nearby;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nasa.support.AppSettings;
import nasa.support.JsonParser;
import nasa.support.Offer;
import nasa.support.OfferAdapter;
import nasa.support.ServerConnector;

public class OffersActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ProgressDialog progress=new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.setMessage("Fetching new offers from server");
        progress.show();

        setContentView(R.layout.activity_offers);
        final ListView offerlist=(ListView)findViewById(R.id.offerlist);
        List<Offer> offers=new ArrayList<>();
        ServerConnector server=new ServerConnector(getApplicationContext());
        server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner() {
            @Override
            public void onServerResponded(String responce) {
                JsonParser parser=new JsonParser(getApplicationContext(),responce);
                parser.setOnJsonParseListner(new JsonParser.OnOffersParserListner() {
                    @Override
                    public void onOffersParsed(List<Offer> offers) {
                        progress.dismiss();
                        OfferAdapter adapter=new OfferAdapter(getApplicationContext(),offers);
                        offerlist.setDivider(null);
                        offerlist.setAdapter(adapter);
                    }
                });
                parser.parseOffers();
            }

            @Override
            public void onServerRevoked() {

            }
        });
        AppSettings settings=new AppSettings(getApplicationContext());
        server.connectServer(settings.retriveSettings("serverurl")+"/offerslist.php");
    }
}
