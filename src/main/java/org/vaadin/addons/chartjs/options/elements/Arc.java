package org.vaadin.addons.chartjs.options.elements;

import elemental.json.Json;
import elemental.json.JsonObject;
import java.io.Serializable;
import org.vaadin.addons.chartjs.utils.And;
import org.vaadin.addons.chartjs.utils.JUtils;
import org.vaadin.addons.chartjs.utils.JsonBuilder;

/**
 * Arcs are used in the polar area, doughnut and pie charts.
 *
 * @author michael@byteowls.com
 */
public class Arc<T> extends And<Element<T>> implements JsonBuilder, Serializable {

  private static final long serialVersionUID = -7524478219337461874L;

  private String backgroundColor;
  private String borderColor;
  private Integer borderWidth;

  public Arc(Element<T> parent) {
    super(parent);
  }

  /** Default fill color for arcs. Default: 'rgba(0,0,0,0.1)' */
  public Arc<T> backgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  /** Default stroke color for arcs. Default: #fff */
  public Arc<T> borderColor(String borderColor) {
    this.borderColor = borderColor;
    return this;
  }

  /** Default stroke width for arcs. Default: 2 */
  public Arc<T> borderWidth(int borderWidth) {
    this.borderWidth = borderWidth;
    return this;
  }

  @Override
  public JsonObject buildJson() {
    JsonObject map = Json.createObject();
    JUtils.putNotNull(map, "backgroundColor", backgroundColor);
    JUtils.putNotNull(map, "borderColor", borderColor);
    JUtils.putNotNull(map, "borderWidth", borderWidth);
    return map;
  }
}
