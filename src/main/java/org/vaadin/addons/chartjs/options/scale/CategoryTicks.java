package org.vaadin.addons.chartjs.options.scale;

import elemental.json.JsonObject;
import org.vaadin.addons.chartjs.utils.JUtils;
import org.vaadin.addons.chartjs.utils.JsonBuilder;

/**
 * @author michael@byteowls.com
 */
public class CategoryTicks<T> extends Ticks<T> implements JsonBuilder {

  private static final long serialVersionUID = 266502561665391111L;

  private String min;
  private String max;

  public CategoryTicks(T parent) {
    super(parent);
  }

  /** The minimum item to display. Must be a value in the data.labels array */
  public CategoryTicks<T> min(String min) {
    this.min = min;
    return this;
  }

  /** The maximum item to display. Must be a value in the data.labels array */
  public CategoryTicks<T> max(String max) {
    this.max = max;
    return this;
  }

  @Override
  public JsonObject buildJson() {
    JsonObject map = super.buildJson();
    JUtils.putNotNull(map, "min", min);
    JUtils.putNotNull(map, "max", max);
    return map;
  }
}
