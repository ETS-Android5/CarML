/* This class allows a string array if values to be formatted so that they can form the X Axis of
 * a chart.
 * Author: Sean Coll
 * Date Created: 27/02/22
 * Last Modified: 27/02/22
 */

package ie.tudublin.carml;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

class XAxisStringFormatter implements IAxisValueFormatter {
    private final String[] values;

    public XAxisStringFormatter(String[] vals) {
        this.values = vals;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return values[(int) value];
    }
}
