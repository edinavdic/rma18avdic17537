package ba.unsa.etf.rma.edin.s1_17537;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Edin on 23.05.2018..
 */

public class MojResultReceiver extends ResultReceiver {

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    private Receiver mReceiver;

    public void setReceiver(Receiver receiver){
        mReceiver = receiver;
    }

    public MojResultReceiver(Handler handler){
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver != null){
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }


}
