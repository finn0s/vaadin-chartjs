package org.vaadin.addons.chartjs.options.scale;

import org.vaadin.addons.chartjs.utils.JUtils;

import elemental.json.JsonObject;

/**
 * The logarithmic scale is use to chart numerical data. It can be placed on either the x or y axis.
 * As the name suggests, logarithmic interpolation is used to determine where a value lies on the axis.
 *
 * @author michael@byteowls.com
 */
public class LogarithmicScale extends BaseScale<LogarithmicScale> {

    private static final long serialVersionUID = -1631504938871606532L;

    private LogarithmicTicks<LogarithmicScale> logarithmicTicks;

    public LogarithmicScale() {
        type("logarithmic");
    }

    /**
     * It defines options for the tick marks that are generated by the axis.
     */
    @Override
    public LogarithmicTicks<LogarithmicScale> ticks() {
        if (this.logarithmicTicks == null) {
            this.logarithmicTicks = new LogarithmicTicks<>(getThis());
        }
        return this.logarithmicTicks;
    }

    @Override
    public LogarithmicScale getThis() {
        return this;
    }

    @Override
    public JsonObject buildJson() {
        JsonObject map = super.buildJson();
        JUtils.putNotNull(map, "ticks", logarithmicTicks);
        return map;
    }
}