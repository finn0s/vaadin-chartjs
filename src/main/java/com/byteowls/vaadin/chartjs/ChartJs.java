package com.byteowls.vaadin.chartjs;

import com.byteowls.vaadin.chartjs.config.ChartConfig;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.function.SerializableConsumer;

import elemental.json.JsonArray;
import elemental.json.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@JavaScript("Moment.js")
@JavaScript("Chart.min.js") 
@JavaScript("hammer.min.js") 
@JavaScript("chartjs-plugin-zoom.min.js")
@JavaScript("chartjs-plugin-annotation.min.js") 
@JavaScript("chartjs-connector.js")
@StyleSheet("vaadin://chartjs/chartjs-connector.css")
@Tag("div")
public class ChartJs extends Component {

    private static final long serialVersionUID = 2999562112373836140L;
    /**
     * Counter for the next menu action being added, used to create a unique
     * property name.
     */
    private static final AtomicInteger nextMenuId = new AtomicInteger(0);

//    public enum ImageType {
//        PNG
//    }

    public interface DataPointClickListener {
        void onDataPointClick(int datasetIndex, int dataIndex);
    }

    public interface LegendClickListener {
        void onLegendClick(int who, boolean isVisible, int[] visibles);
    }

//    public interface DownloadListener {
//        void onDownload(byte[] imageData);
//    }

    private List<ChartJs.DataPointClickListener> dataPointClickListeners = new ArrayList<>();
    private List<ChartJs.LegendClickListener> legendClickListeners = new ArrayList<>();
//    private List<ChartJs.DownloadListener> downloadListeners = new ArrayList<>();

    private ChartConfig chartConfig;

    /**
     * Construct a ChartJs. Be aware that you have to set a {@link ChartConfig} as well. Use {@link #configure(ChartConfig)} to do so.
     */
    public ChartJs() {
        getElement().getClassList().add("v-chartjs");
        getElement().getClassList().add("-v-chartjs");
        getElement().getClassList().add("v-widget");
    }

    /**
     * Constructs a chart with a {@link ChartConfig}
     * @param chartConfig a chart configuration implementation
     */
    public ChartJs(ChartConfig chartConfig) {
        this();
        configure(chartConfig);
		getElement().setText("poop");
    }

    /**
     * Configure a ChartJs chart.
     * @param chartConfig a chart configuration implementation
     */
    public void configure(ChartConfig chartConfig) {
        if (chartConfig != null) {
            this.chartConfig = chartConfig;
        }
    }

    @Override
    protected void onAttach(AttachEvent e) {
        if (chartConfig != null) {
            getElement().setPropertyJson("chartConfig", (JsonValue)chartConfig.buildJson());
        }
        initConnector();
        super.onAttach(e);
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> 
            ui.getPage().executeJs("window.com_byteowls_vaadin_chartjs_ChartJs($0)", getElement()));
    }

    void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    } 

    /**
     * @return Chart configuration. Useful for update the data after chart drawing
     */
    public ChartConfig getConfig() {
        return this.chartConfig;
    }

    /**
     * Update the chart. Before calling this method, options must be changed and new data must be supplied.
     */
    public void update() {
        if (chartConfig != null) {
            getElement().setPropertyJson("chartConfig", (JsonValue)chartConfig.buildJson());
        }
    }

    /**
     * Destroy the chart. This will call chartjs.destroy();
     */
    public void destroy() {
        getElement().executeJs("destroy()");
    }

    /**
     * Update the chart. Before calling this method, options must be changed and new data must be supplied.
     *
     * @deprecated because this method updates not only data but also chart options. Use update() instead.
     */
    @Deprecated
    public void refreshData() {
        update();
    }

    /**
     * @return True if the connector's logs defined messages to "console.log" else logging is disabled.
     */
    public boolean isJsLoggingEnabled() {
        return getElement().getProperty("loggingEnabled").equals("true");
    }

    /**
     * Enable or disables the connector's logging to "console.log"
     * @param jsLoggingEnabled If true the connector script will log defined messages to "console.log". Defaults to false.
     */
    public void setJsLoggingEnabled(boolean jsLoggingEnabled) {
        getElement().setProperty("loggingEnabled", jsLoggingEnabled);
    }

    /**
     * Adds a listener handling clicks on charts data points.
     * @param listener the click listener.
     */
    public void addClickListener(ChartJs.DataPointClickListener listener) {
        dataPointClickListeners.add(listener);
        checkListenerState();
    }

    public void removeClickListener(ChartJs.DataPointClickListener listener) {
        dataPointClickListeners.remove(listener);
        checkListenerState();
    }
    public void addLegendClickListener(ChartJs.LegendClickListener listener) {
    	legendClickListeners.add(listener);
        checkListenerState();
    }

    public void removeLegendClickListener(ChartJs.LegendClickListener listener) {
    	legendClickListeners.remove(listener);
        checkListenerState();
    }

//    /**
//     * Adds a listener serving the downloaded image.
//     * @param listener the download listener.
//     */
//    public void addDownloadListener(ChartJs.DownloadListener listener) {
//        downloadListeners.add(listener);
//    }
//
//    public void removeDownloadListener(ChartJs.DownloadListener listener) {
//        downloadListeners.remove(listener);
//    }

    private void checkListenerState() {
        getElement().setProperty("dataPointClickListenerFound", !this.dataPointClickListeners.isEmpty());
        getElement().setProperty("legendClickListenerFound", !this.legendClickListeners.isEmpty());
    }
    
    @ClientCallable
    private void onDataPointClick(JsonArray arguments) {
        int datasetIndex = (int) arguments.getNumber(0);
        int dataIndex = (int) arguments.getNumber(1);
        for (DataPointClickListener l : dataPointClickListeners) {
            l.onDataPointClick(datasetIndex, dataIndex);
        }
    }
    
    @ClientCallable
    private void onLegendClick(JsonArray arguments) {
        int datasetIndex = (int) arguments.getNumber(0);
        boolean visible = arguments.getBoolean(1);
        JsonArray visblesJson = arguments.getArray(2);
        int[] visibles = new int[visblesJson.length()];
        for (int i = 0 ; i < visblesJson.length(); i++)
            visibles[i] = (int)visblesJson.getNumber(i);

        for (LegendClickListener l : legendClickListeners) {
            l.onLegendClick(datasetIndex, visible, visibles);
        }
    }

/*
    @Override
    protected ChartJsState getState() {
        getElement()
        return (ChartJsState) super.getState();
    } */
    
    /**
     * Show the download action in the menu.
     *
     * @param showDownloadAction
     *            True, the download action in the menu should be displayed.
     */
    public void setShowDownloadAction(boolean showDownloadAction) {
        getElement().setAttribute("showDownloadAction", showDownloadAction);
    }

    /**
     * Set the label for the download action to the given text.
     *
     * @param downloadActionText
     *            The new text for the download action.
     */
    public void setDownloadActionText(String downloadActionText) {
        getElement().setAttribute("downloadActionText", downloadActionText);
    }

    /**
     * Set the filename for the downloaded image.
     *
     * @param downloadActionFilename
     *            The filename for the download including its extension,
     *            defaults to chart.png.
     */
    public void setDownloadActionFilename(String downloadActionFilename) {
        getElement().setAttribute("downloadActionFilename", downloadActionFilename);
    }

    /**
     * If set to true, the downloaded image will receive a white background (instead of the default, which is
     * transparent).
     *
     * @param downloadSetWhiteBackground
     *            Set to true for downloading images with a white background.
     */
    public void setDownloadSetWhiteBackground(boolean downloadSetWhiteBackground) {
        getElement().setAttribute("downloadSetWhiteBackground", downloadSetWhiteBackground);
    }

    /**
     * Add a new item to the menu.
     *
     * @param menuTitle
     *            The title to be displayed in the menu.
     * @param action
     *            The action to be run when the item is clicked.
     */
    public void addMenuEntry(String menuTitle, Runnable action) {
        if (menuTitle == null || menuTitle.length() == 0) {
            throw new IllegalArgumentException("menuTitle missing");
        }
        if (action == null) {
            throw new IllegalArgumentException("action missing");
        }

        // callback ID will be used as key in ChartJsState.menuItems and also as JavaScript function name
        String callbackId = "ChartJsMenuItem" + nextMenuId.incrementAndGet();
        /*ChartJsState state = getState();
        if (state.menuItems == null) {
            state.menuItems = new HashMap<>();
        }
        state.menuItems.put(callbackId, menuTitle);
*/
        /*addFunction(callbackId, new JavaScriptFunction() {
            @Override
            public void call(JsonArray arguments) {
                action.run();
            }
        });*/ 
    }
}
