package ie.tudublin.carml;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

class XAxisStringFormater implements IAxisValueFormatter {
    private String[] values;

    public XAxisStringFormater(String[] vals) {
        this.values = vals;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return values[(int) value];
    }
}
