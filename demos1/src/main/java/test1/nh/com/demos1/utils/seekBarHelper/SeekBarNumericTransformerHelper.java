package test1.nh.com.demos1.utils.seekBarHelper;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 16-2-16.
 *
 * this is for showing the breath signal playback...
 */
public class SeekBarNumericTransformerHelper extends DiscreteSeekBar.NumericTransformer {


    // starting time of the display
    private final Calendar startTime;

    public SeekBarNumericTransformerHelper(Calendar startTime){
        this.startTime =startTime;
    }


    @Override
    public int transform(int value) {
        return 0;
    }


    @Override
    public boolean useStringTransform() {
        return true;
    }

    @Override
    public String transformToString(int value) {
        Calendar currentTime=(GregorianCalendar)startTime.clone();
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");
        currentTime.add(Calendar.MINUTE,value);
        return timeFormat.format(currentTime.getTime());
//        return currentTime.get(Calendar.HOUR_OF_DAY)+":"+currentTime.get(Calendar.MINUTE);
//        return startTime.get(Calendar.MONTH)+"月"+startTime.get(Calendar.DAY_OF_MONTH)+"日\n"+startTime.get(Calendar.HOUR)+":"+startTime.get(Calendar.MINUTE)+":"+startTime.get(Calendar.SECOND);
    }

}
